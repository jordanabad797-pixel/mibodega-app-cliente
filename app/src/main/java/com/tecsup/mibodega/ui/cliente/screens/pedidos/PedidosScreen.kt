package com.tecsup.mibodega.ui.cliente.screens.pedidos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.cliente.modelo.ItemCarrito
import com.tecsup.mibodega.ui.cliente.modelo.Pedido
import com.tecsup.mibodega.ui.cliente.modelo.listaProductosFake
import com.tecsup.mibodega.ui.componentes.BarraInferiorCliente
import com.tecsup.mibodega.ui.componentes.DestinoBarra
import com.tecsup.mibodega.ui.theme.BodegaTheme
import com.tecsup.mibodega.ui.theme.Blanco
import com.tecsup.mibodega.ui.theme.GrisClaro
import com.tecsup.mibodega.ui.theme.GrisTexto
import com.tecsup.mibodega.ui.theme.VerdeBodega

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PedidosScreen(
    pedidos: List<Pedido>,
    cantidadCarrito: Int,
    onVerCarrito: () -> Unit,
    onNavegarBarra: (DestinoBarra) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis pedidos", fontWeight = FontWeight.Bold) },
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
                seleccionado = DestinoBarra.PEDIDOS,
                onSeleccionar = onNavegarBarra
            )
        }
    ) { paddingInterno ->
        if (pedidos.isEmpty()) {
            PedidosVacio(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingInterno)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingInterno),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(pedidos, key = { it.numero }) { pedido ->
                    TarjetaPedido(pedido = pedido)
                }
            }
        }
    }
}

@Composable
private fun PedidosVacio(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Receipt,
            contentDescription = null,
            tint = VerdeBodega,
            modifier = Modifier.size(72.dp)
        )

        Spacer(Modifier.height(16.dp))

        Text(
            text = "Aún no tienes pedidos",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(4.dp))

        Text(
            text = "Cuando confirmes una compra aparecerá aquí.",
            style = MaterialTheme.typography.bodyMedium,
            color = GrisTexto
        )
    }
}

@Composable
private fun TarjetaPedido(pedido: Pedido) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(GrisClaro, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Pedido #${pedido.numero}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Box(
                modifier = Modifier
                    .background(Blanco, RoundedCornerShape(12.dp))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = pedido.estado,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = VerdeBodega
                )
            }
        }

        Text(
            text = pedido.fecha,
            style = MaterialTheme.typography.bodySmall,
            color = GrisTexto
        )

        HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))

        pedido.items.forEach { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${item.cantidad} x ${item.producto.nombre}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "S/ %.2f".format(item.producto.precio * item.cantidad),
                    style = MaterialTheme.typography.bodyMedium,
                    color = GrisTexto
                )
            }
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Total (incluye delivery)",
                style = MaterialTheme.typography.bodyMedium,
                color = GrisTexto
            )
            Text(
                text = "S/ %.2f".format(pedido.total),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = VerdeBodega
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PedidosPreview() {
    BodegaTheme {
        PedidosScreen(
            pedidos = listOf(
                Pedido(
                    numero = 1024,
                    fecha = "01/10/2026 17:30",
                    items = listOf(
                        ItemCarrito(listaProductosFake[0], 2),
                        ItemCarrito(listaProductosFake[2], 1)
                    ),
                    total = 18.40
                )
            ),
            cantidadCarrito = 0,
            onVerCarrito = {},
            onNavegarBarra = {}
        )
    }
}