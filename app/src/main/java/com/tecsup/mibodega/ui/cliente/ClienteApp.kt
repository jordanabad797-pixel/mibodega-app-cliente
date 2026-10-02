package com.tecsup.mibodega.ui.cliente

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tecsup.mibodega.ui.cliente.modelo.DatosCliente
import com.tecsup.mibodega.ui.cliente.modelo.ItemCarrito
import com.tecsup.mibodega.ui.cliente.modelo.Producto
import com.tecsup.mibodega.ui.cliente.modelo.listaProductosFake
import com.tecsup.mibodega.ui.cliente.screens.bienvenida.BienvenidaScreen
import com.tecsup.mibodega.ui.cliente.screens.carrito.COSTO_DELIVERY
import com.tecsup.mibodega.ui.cliente.screens.carrito.CarritoScreen
import com.tecsup.mibodega.ui.cliente.screens.categorias.CategoriasScreen
import com.tecsup.mibodega.ui.cliente.screens.confirmacion.ConfirmacionScreen
import com.tecsup.mibodega.ui.cliente.screens.detalle.DetalleProductoScreen
import com.tecsup.mibodega.ui.cliente.screens.entrega.DatosEntregaScreen
import com.tecsup.mibodega.ui.cliente.screens.inicio.InicioScreen
import com.tecsup.mibodega.ui.cliente.screens.login.LoginScreen
import com.tecsup.mibodega.ui.cliente.screens.registro.RegistroScreen
import com.tecsup.mibodega.ui.componentes.DestinoBarra
import com.tecsup.mibodega.ui.componentes.DialogoTerminos

private object Rutas {
    const val BIENVENIDA = "bienvenida"
    const val LOGIN = "login"
    const val REGISTRO = "registro"
    const val INICIO = "inicio"
    const val CATEGORIAS = "categorias"
    const val DETALLE = "detalle/{productoId}"
    const val CARRITO = "carrito"
    const val ENTREGA = "entrega"
    const val CONFIRMACION = "confirmacion"

    fun detalle(productoId: Int) = "detalle/$productoId"
}

@Composable
fun ClienteApp() {
    val navController = rememberNavController()

    var carrito by remember { mutableStateOf<List<ItemCarrito>>(emptyList()) }
    var datosCliente by remember { mutableStateOf(DatosCliente()) }
    var mostrarTerminos by remember { mutableStateOf(false) }
    var numeroPedido by remember { mutableStateOf(1023) }
    var totalPedido by remember { mutableStateOf(0.0) }

    val navegarBarra: (DestinoBarra) -> Unit = { destino ->
        val ruta = when (destino) {
            DestinoBarra.INICIO -> Rutas.INICIO
            DestinoBarra.CATEGORIAS -> Rutas.CATEGORIAS
            DestinoBarra.PEDIDOS -> null
            DestinoBarra.PERFIL -> null
        }
        if (ruta != null) {
            navController.navigate(ruta) {
                popUpTo(Rutas.INICIO)
                launchSingleTop = true
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = Rutas.BIENVENIDA
    ) {
        composable(Rutas.BIENVENIDA) {
            BienvenidaScreen(
                onRegistrarse = { navController.navigate(Rutas.REGISTRO) },
                onIniciarSesion = { navController.navigate(Rutas.LOGIN) },
                onTerminos = { mostrarTerminos = true }
            )
        }

        composable(Rutas.LOGIN) {
            LoginScreen(
                onVolver = { navController.popBackStack() },
                onIngresar = {
                    navController.navigate(Rutas.INICIO) {
                        popUpTo(Rutas.BIENVENIDA) { inclusive = true }
                    }
                }
            )
        }

        composable(Rutas.REGISTRO) {
            RegistroScreen(
                onVolver = { navController.popBackStack() },
                onCrearCuenta = { nombre, telefono, direccion, referencia ->
                    datosCliente = DatosCliente(
                        nombre = nombre,
                        telefono = telefono,
                        direccion = direccion,
                        referencia = referencia
                    )
                    navController.navigate(Rutas.INICIO) {
                        popUpTo(Rutas.BIENVENIDA) { inclusive = true }
                    }
                }
            )
        }

        composable(Rutas.INICIO) {
            InicioScreen(
                cantidadCarrito = carrito.sumOf { it.cantidad },
                onVerCarrito = { navController.navigate(Rutas.CARRITO) },
                onProductoClick = { producto ->
                    navController.navigate(Rutas.detalle(producto.id))
                },
                onAgregarProducto = { producto ->
                    carrito = agregarOSumarProducto(carrito, producto, 1)
                },
                onNavegarBarra = navegarBarra
            )
        }

        composable(Rutas.CATEGORIAS) {
            CategoriasScreen(
                cantidadCarrito = carrito.sumOf { it.cantidad },
                onVerCarrito = { navController.navigate(Rutas.CARRITO) },
                onProductoClick = { producto ->
                    navController.navigate(Rutas.detalle(producto.id))
                },
                onAgregarProducto = { producto ->
                    carrito = agregarOSumarProducto(carrito, producto, 1)
                },
                onNavegarBarra = navegarBarra
            )
        }

        composable(
            route = Rutas.DETALLE,
            arguments = listOf(navArgument("productoId") { type = NavType.IntType })
        ) { backStackEntry ->
            val productoId = backStackEntry.arguments?.getInt("productoId") ?: 0
            val producto = listaProductosFake.first { it.id == productoId }

            DetalleProductoScreen(
                producto = producto,
                onVolver = { navController.popBackStack() },
                onAgregarAlCarrito = { productoSeleccionado, cantidad ->
                    carrito = agregarOSumarProducto(carrito, productoSeleccionado, cantidad)
                    navController.popBackStack()
                }
            )
        }

        composable(Rutas.CARRITO) {
            CarritoScreen(
                carrito = carrito,
                onVolver = { navController.popBackStack() },
                onIncrementar = { producto ->
                    carrito = carrito.map {
                        if (it.producto.id == producto.id) it.copy(cantidad = it.cantidad + 1) else it
                    }
                },
                onDecrementar = { producto ->
                    carrito = carrito.mapNotNull {
                        when {
                            it.producto.id != producto.id -> it
                            it.cantidad > 1 -> it.copy(cantidad = it.cantidad - 1)
                            else -> null
                        }
                    }
                },
                onEliminar = { producto ->
                    carrito = carrito.filterNot { it.producto.id == producto.id }
                },
                onContinuarPedido = {
                    if (carrito.isNotEmpty()) navController.navigate(Rutas.ENTREGA)
                }
            )
        }

        composable(Rutas.ENTREGA) {
            DatosEntregaScreen(
                onVolver = { navController.popBackStack() },
                onConfirmarPedido = {
                    totalPedido = carrito.sumOf { it.producto.precio * it.cantidad } + COSTO_DELIVERY
                    numeroPedido += 1
                    carrito = emptyList()
                    navController.navigate(Rutas.CONFIRMACION) {
                        popUpTo(Rutas.INICIO)
                    }
                },
                nombreInicial = datosCliente.nombre,
                telefonoInicial = datosCliente.telefono,
                direccionInicial = datosCliente.direccion,
                referenciaInicial = datosCliente.referencia
            )
        }

        composable(Rutas.CONFIRMACION) {
            ConfirmacionScreen(
                numeroPedido = numeroPedido,
                total = totalPedido,
                onVolverInicio = {
                    navController.navigate(Rutas.INICIO) {
                        popUpTo(Rutas.INICIO) { inclusive = true }
                    }
                }
            )
        }
    }

    if (mostrarTerminos) {
        DialogoTerminos(onCerrar = { mostrarTerminos = false })
    }
}

private fun agregarOSumarProducto(
    carrito: List<ItemCarrito>,
    producto: Producto,
    cantidad: Int
): List<ItemCarrito> {
    val itemExistente = carrito.find { it.producto.id == producto.id }
    return if (itemExistente != null) {
        carrito.map {
            if (it.producto.id == producto.id) it.copy(cantidad = it.cantidad + cantidad) else it
        }
    } else {
        carrito + ItemCarrito(producto = producto, cantidad = cantidad)
    }
}