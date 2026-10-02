package com.tecsup.mibodega.ui.componentes

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.tecsup.mibodega.ui.theme.VerdeBodega

enum class DestinoBarra(val etiqueta: String, val icono: ImageVector) {
    INICIO("Inicio", Icons.Default.Home),
    CATEGORIAS("Categorías", Icons.Default.List),
    PEDIDOS("Pedidos", Icons.Default.Receipt),
    PERFIL("Perfil", Icons.Default.Person)
}

@Composable
fun BarraInferiorCliente(
    seleccionado: DestinoBarra,
    onSeleccionar: (DestinoBarra) -> Unit
) {
    NavigationBar {
        DestinoBarra.entries.forEach { destino ->
            NavigationBarItem(
                selected = destino == seleccionado,
                onClick = { onSeleccionar(destino) },
                icon = { Icon(destino.icono, contentDescription = destino.etiqueta) },
                label = { Text(destino.etiqueta) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = VerdeBodega,
                    selectedTextColor = VerdeBodega
                )
            )
        }
    }
}