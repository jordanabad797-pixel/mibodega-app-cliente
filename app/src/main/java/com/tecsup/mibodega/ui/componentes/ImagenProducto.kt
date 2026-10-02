package com.tecsup.mibodega.ui.componentes

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingBasket
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.cliente.modelo.Producto
import com.tecsup.mibodega.ui.theme.GrisClaro
import com.tecsup.mibodega.ui.theme.VerdeBodega

@Composable
fun ImagenProducto(
    producto: Producto,
    modifier: Modifier = Modifier,
    tamanoIcono: Dp = 36.dp,
    fondo: Color = GrisClaro,
    contentScale: ContentScale = ContentScale.Crop
) {
    Box(
        modifier = modifier.background(fondo),
        contentAlignment = Alignment.Center
    ) {
        val imagen = producto.imagen
        if (imagen != null) {
            Image(
                painter = painterResource(id = imagen),
                contentDescription = producto.nombre,
                modifier = Modifier.fillMaxSize(),
                contentScale = contentScale
            )
        } else {
            Icon(
                imageVector = Icons.Default.ShoppingBasket,
                contentDescription = producto.nombre,
                tint = VerdeBodega,
                modifier = Modifier.size(tamanoIcono)
            )
        }
    }
}