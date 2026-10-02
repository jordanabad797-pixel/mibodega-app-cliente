package com.tecsup.mibodega.ui.componentes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.theme.BodegaTheme
import com.tecsup.mibodega.ui.theme.Blanco
import com.tecsup.mibodega.ui.theme.GrisTexto
import com.tecsup.mibodega.ui.theme.VerdeBodega

private val clausulas = listOf(
    "1. Uso de la aplicación" to
            "Mi Bodega permite comprar productos de la bodega y recibirlos en tu domicilio. Debes ingresar datos reales para que podamos entregar tu pedido.",
    "2. Datos personales" to
            "Tu nombre, teléfono, dirección y referencia se usan únicamente para coordinar la entrega de tus pedidos y no se comparten con terceros.",
    "3. Pedidos y entregas" to
            "El tiempo de entrega es estimado. El costo de delivery se muestra antes de confirmar y se suma al total del pedido.",
    "4. Pagos" to
            "Puedes pagar en efectivo al recibir el pedido, o con Yape o Plin. El pedido se considera pagado cuando se confirma el pago.",
    "5. Cambios en los términos" to
            "Podemos actualizar estos términos. Al seguir usando la aplicación aceptas la versión vigente."
)

@Composable
fun DialogoTerminos(onCerrar: () -> Unit) {
    AlertDialog(
        onDismissRequest = onCerrar,
        containerColor = Blanco,
        title = {
            Text(
                text = "Términos y Condiciones",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .heightIn(max = 360.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                clausulas.forEach { (titulo, contenido) ->
                    Text(
                        text = titulo,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 12.dp)
                    )
                    Text(
                        text = contenido,
                        style = MaterialTheme.typography.bodyMedium,
                        color = GrisTexto,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onCerrar) {
                Text(
                    text = "Entendido",
                    color = VerdeBodega,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun DialogoTerminosPreview() {
    BodegaTheme {
        DialogoTerminos(onCerrar = {})
    }
}