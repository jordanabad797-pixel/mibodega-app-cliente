package com.tecsup.mibodega.ui.cliente.screens.carrito

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.cliente.modelo.ItemCarrito
import com.tecsup.mibodega.ui.cliente.modelo.Producto
import com.tecsup.mibodega.ui.cliente.modelo.listaProductosFake
import com.tecsup.mibodega.ui.componentes.BotonPrimario
import com.tecsup.mibodega.ui.componentes.ImagenProducto
import com.tecsup.mibodega.ui.componentes.SelectorCantidad
import com.tecsup.mibodega.ui.theme.BodegaTheme
import com.tecsup.mibodega.ui.theme.Blanco
import com.tecsup.mibodega.ui.theme.GrisTexto
import com.tecsup.mibodega.ui.theme.RojoPrecio
import com.tecsup.mibodega.ui.theme.VerdeBodega

const val COSTO_DELIVERY = 4.00

@Composable
fun CarritoScreen(
    carrito: List<ItemCarrito>,
    onVolver: () -> Unit,
    onIncrementar: (Producto) -> Unit,
    onDecrementar: (Producto) -> Unit,
    onEliminar: (Producto) -> Unit,
    onContinuarPedido: () -> Unit
) {
    var productoPorEliminar by remember { mutableStateOf<Producto?>(null) }

    val subtotal = carrito.sumOf { it.producto.precio * it.cantidad }
    val total = subtotal + COSTO_DELIVERY

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
    ) {
        EncabezadoCarrito(onVolver = onVolver)

        if (carrito.isEmpty()) {
            CarritoVacio(
                onSeguirComprando = onVolver,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 20.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(carrito, key = { it.producto.id }) { item ->
                    FilaCarrito(
                        item = item,
                        onIncrementar = { onIncrementar(item.producto) },
                        onDecrementar = {
                            if (item.cantidad == 1) {
                                productoPorEliminar = item.producto
                            } else {
                                onDecrementar(item.producto)
                            }
                        },
                        onEliminar = { productoPorEliminar = item.producto }
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                }
            }

            ResumenYBoton(
                subtotal = subtotal,
                delivery = COSTO_DELIVERY,
                total = total,
                onContinuarPedido = onContinuarPedido
            )
        }
    }

    val producto = productoPorEliminar
    if (producto != null) {
        AlertDialog(
            onDismissRequest = { productoPorEliminar = null },
            containerColor = Blanco,
            title = { Text("¿Eliminar producto?", fontWeight = FontWeight.Bold) },
            text = { Text("Se quitará \"${producto.nombre}\" de tu carrito.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onEliminar(producto)
                        productoPorEliminar = null
                    }
                ) {
                    Text("Eliminar", color = RojoPrecio, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { productoPorEliminar = null }) {
                    Text("Cancelar", color = GrisTexto)
                }
            }
        )
    }
}

@Composable
private fun CarritoVacio(
    onSeguirComprando: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.ShoppingCart,
            contentDescription = null,
            tint = VerdeBodega,
            modifier = Modifier.size(72.dp)
        )

        Spacer(Modifier.height(16.dp))

        Text(
            text = "Tu carrito está vacío",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(4.dp))

        Text(
            text = "Agrega productos desde el inicio para hacer tu pedido.",
            style = MaterialTheme.typography.bodyMedium,
            color = GrisTexto,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(24.dp))

        BotonPrimario(
            texto = "Seguir comprando",
            onClick = onSeguirComprando
        )
    }
}

@Composable
private fun EncabezadoCarrito(onVolver: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onVolver) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
        }
        Text(
            text = "Mi carrito",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun FilaCarrito(
    item: ItemCarrito,
    onIncrementar: () -> Unit,
    onDecrementar: () -> Unit,
    onEliminar: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ImagenProducto(
            producto = item.producto,
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(10.dp)),
            tamanoIcono = 26.dp
        )

        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.producto.nombre,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "S/ %.2f".format(item.producto.precio),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        SelectorCantidad(
            cantidad = item.cantidad,
            onIncrementar = onIncrementar,
            onDecrementar = onDecrementar
        )

        IconButton(onClick = onEliminar) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Eliminar ${item.producto.nombre}",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ResumenYBoton(
    subtotal: Double,
    delivery: Double,
    total: Double,
    onContinuarPedido: () -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)) {
        FilaResumen(etiqueta = "Subtotal", valor = subtotal)
        FilaResumen(etiqueta = "Costo de delivery", valor = delivery)

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Total",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "S/ %.2f".format(total),
                style = MaterialTheme.typography.titleMedium,
                color = VerdeBodega
            )
        }

        Spacer(Modifier.height(16.dp))

        BotonPrimario(
            texto = "Continuar pedido",
            onClick = onContinuarPedido
        )
    }
}

@Composable
private fun FilaResumen(etiqueta: String, valor: Double) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = etiqueta, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(text = "S/ %.2f".format(valor), color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun CarritoPreview() {
    val carritoEjemplo = listOf(
        ItemCarrito(listaProductosFake[4], 1),
        ItemCarrito(listaProductosFake[0], 2),
        ItemCarrito(listaProductosFake[2], 1)
    )
    BodegaTheme {
        CarritoScreen(
            carrito = carritoEjemplo,
            onVolver = {},
            onIncrementar = {},
            onDecrementar = {},
            onEliminar = {},
            onContinuarPedido = {}
        )
    }
}