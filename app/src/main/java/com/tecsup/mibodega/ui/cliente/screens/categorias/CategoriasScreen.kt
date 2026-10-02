package com.tecsup.mibodega.ui.cliente.screens.categorias

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.ShoppingBasket
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.cliente.modelo.Producto
import com.tecsup.mibodega.ui.cliente.modelo.listaCategorias
import com.tecsup.mibodega.ui.cliente.modelo.listaProductosFake
import com.tecsup.mibodega.ui.componentes.BarraInferiorCliente
import com.tecsup.mibodega.ui.componentes.DestinoBarra
import com.tecsup.mibodega.ui.componentes.ProductoCard
import com.tecsup.mibodega.ui.theme.BodegaTheme
import com.tecsup.mibodega.ui.theme.Blanco
import com.tecsup.mibodega.ui.theme.GrisClaro
import com.tecsup.mibodega.ui.theme.GrisTexto
import com.tecsup.mibodega.ui.theme.VerdeBodega

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriasScreen(
    productos: List<Producto> = listaProductosFake,
    cantidadCarrito: Int,
    onVerCarrito: () -> Unit,
    onProductoClick: (Producto) -> Unit,
    onAgregarProducto: (Producto) -> Unit,
    onNavegarBarra: (DestinoBarra) -> Unit
) {
    var categoriaAbierta by rememberSaveable { mutableStateOf<String?>(null) }
    val categorias = listaCategorias.filter { it != "Todos" }

    BackHandler(enabled = categoriaAbierta != null) {
        categoriaAbierta = null
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(categoriaAbierta ?: "Categorías", fontWeight = FontWeight.Bold)
                },
                navigationIcon = {
                    if (categoriaAbierta != null) {
                        IconButton(onClick = { categoriaAbierta = null }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Volver a categorías")
                        }
                    }
                },
                actions = {
                    IconButton(onClick = onVerCarrito) {
                        BadgedBox(
                            badge = {
                                if (cantidadCarrito > 0) {
                                    Badge { Text("$cantidadCarrito") }
                                }
                            }
                        ) {
                            Icon(Icons.Default.ShoppingCart, contentDescription = "Carrito")
                        }
                    }
                }
            )
        },
        bottomBar = {
            BarraInferiorCliente(
                seleccionado = DestinoBarra.CATEGORIAS,
                onSeleccionar = onNavegarBarra
            )
        }
    ) { paddingInterno ->
        val categoria = categoriaAbierta

        if (categoria == null) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingInterno),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(categorias) { nombre ->
                    FilaCategoria(
                        nombre = nombre,
                        cantidad = productos.count { it.categoria == nombre },
                        onClick = { categoriaAbierta = nombre }
                    )
                }
            }
        } else {
            val productosDeCategoria = productos.filter { it.categoria == categoria }

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(16.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingInterno)
            ) {
                items(productosDeCategoria) { producto ->
                    ProductoCard(
                        producto = producto,
                        onClick = { onProductoClick(producto) },
                        onAgregar = { onAgregarProducto(producto) }
                    )
                }
            }
        }
    }
}

@Composable
private fun FilaCategoria(
    nombre: String,
    cantidad: Int,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(GrisClaro)
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(Blanco, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = iconoDeCategoria(nombre),
                contentDescription = null,
                tint = VerdeBodega,
                modifier = Modifier.size(26.dp)
            )
        }

        Spacer(Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = nombre,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = if (cantidad == 1) "1 producto" else "$cantidad productos",
                style = MaterialTheme.typography.bodySmall,
                color = GrisTexto
            )
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = GrisTexto
        )
    }
}

private fun iconoDeCategoria(nombre: String): ImageVector = when (nombre) {
    "Bebidas" -> Icons.Default.LocalDrink
    "Abarrotes" -> Icons.Default.ShoppingBasket
    "Snacks" -> Icons.Default.Fastfood
    else -> Icons.Default.Category
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun CategoriasPreview() {
    BodegaTheme {
        CategoriasScreen(
            cantidadCarrito = 2,
            onVerCarrito = {},
            onProductoClick = {},
            onAgregarProducto = {},
            onNavegarBarra = {}
        )
    }
}