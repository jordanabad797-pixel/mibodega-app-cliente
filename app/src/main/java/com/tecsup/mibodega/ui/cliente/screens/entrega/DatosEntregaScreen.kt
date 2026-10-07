package com.tecsup.mibodega.ui.cliente.screens.entrega

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.cliente.screens.carrito.COSTO_DELIVERY
import com.tecsup.mibodega.ui.componentes.BotonPrimario
import com.tecsup.mibodega.ui.theme.BodegaTheme
import com.tecsup.mibodega.ui.theme.GrisBorde
import com.tecsup.mibodega.ui.theme.GrisClaro
import com.tecsup.mibodega.ui.theme.GrisTexto
import com.tecsup.mibodega.ui.theme.VerdeBodega

private val metodosPago = listOf("Efectivo", "Yape", "Plin")

@Composable
fun DatosEntregaScreen(
    subtotal: Double,
    onVolver: () -> Unit,
    onConfirmarPedido: (Boolean) -> Unit,
    nombreInicial: String = "",
    telefonoInicial: String = "",
    direccionInicial: String = "",
    referenciaInicial: String = ""
) {
    var nombre by rememberSaveable { mutableStateOf(nombreInicial) }
    var telefono by rememberSaveable { mutableStateOf(telefonoInicial) }
    var direccion by rememberSaveable { mutableStateOf(direccionInicial) }
    var referencia by rememberSaveable { mutableStateOf(referenciaInicial) }
    var esDelivery by rememberSaveable { mutableStateOf(true) }
    var metodoPago by rememberSaveable { mutableStateOf(metodosPago.first()) }
    var intentoConfirmar by remember { mutableStateOf(false) }

    val costoEnvio = if (esDelivery) COSTO_DELIVERY else 0.0
    val total = subtotal + costoEnvio

    val formularioValido = nombre.isNotBlank() &&
            telefono.isNotBlank() &&
            (!esDelivery || direccion.isNotBlank())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
    ) {
        EncabezadoEntrega(onVolver = onVolver)

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Text(
                text = "Modalidad de entrega",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 12.dp, bottom = 4.dp)
            )

            FilaOpcion(
                texto = "Delivery",
                detalle = "+ S/ %.2f".format(COSTO_DELIVERY),
                seleccionado = esDelivery,
                onSeleccionar = { esDelivery = true }
            )
            FilaOpcion(
                texto = "Recojo en tienda",
                detalle = "Gratis",
                seleccionado = !esDelivery,
                onSeleccionar = { esDelivery = false }
            )

            CampoEntrega(
                etiqueta = "Nombre",
                valor = nombre,
                onCambio = { nombre = it },
                mostrarError = intentoConfirmar && nombre.isBlank()
            )
            CampoEntrega(
                etiqueta = "Teléfono",
                valor = telefono,
                onCambio = { telefono = it },
                mostrarError = intentoConfirmar && telefono.isBlank(),
                tipoTeclado = KeyboardType.Phone
            )

            if (esDelivery) {
                CampoEntrega(
                    etiqueta = "Dirección",
                    valor = direccion,
                    onCambio = { direccion = it },
                    mostrarError = intentoConfirmar && direccion.isBlank()
                )
                CampoEntrega(
                    etiqueta = "Referencia",
                    valor = referencia,
                    onCambio = { referencia = it }
                )
            }

            Text(
                text = "Método de pago",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 24.dp, bottom = 4.dp)
            )

            metodosPago.forEach { metodo ->
                FilaOpcion(
                    texto = metodo,
                    detalle = null,
                    seleccionado = metodo == metodoPago,
                    onSeleccionar = { metodoPago = metodo }
                )
            }

            Spacer(Modifier.height(12.dp))
        }

        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)) {
            FilaTotal(etiqueta = "Subtotal", valor = "S/ %.2f".format(subtotal))
            FilaTotal(
                etiqueta = if (esDelivery) "Costo de delivery" else "Recojo en tienda",
                valor = if (esDelivery) "S/ %.2f".format(costoEnvio) else "Gratis"
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Total",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "S/ %.2f".format(total),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = VerdeBodega
                )
            }

            Spacer(Modifier.height(16.dp))

            BotonPrimario(
                texto = "Confirmar pedido",
                onClick = {
                    intentoConfirmar = true
                    if (formularioValido) onConfirmarPedido(esDelivery)
                }
            )
        }
    }
}

@Composable
private fun EncabezadoEntrega(onVolver: () -> Unit) {
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
            text = "Datos de entrega",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun CampoEntrega(
    etiqueta: String,
    valor: String,
    onCambio: (String) -> Unit,
    mostrarError: Boolean = false,
    tipoTeclado: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onCambio,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp),
        label = { Text(etiqueta) },
        singleLine = true,
        isError = mostrarError,
        supportingText = if (mostrarError) {
            { Text("Campo obligatorio") }
        } else null,
        keyboardOptions = KeyboardOptions(keyboardType = tipoTeclado),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = GrisClaro,
            focusedContainerColor = GrisClaro,
            errorContainerColor = GrisClaro,
            unfocusedBorderColor = GrisBorde,
            focusedBorderColor = VerdeBodega,
            focusedLabelColor = VerdeBodega
        )
    )
}

@Composable
private fun FilaOpcion(
    texto: String,
    detalle: String?,
    seleccionado: Boolean,
    onSeleccionar: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(
                selected = seleccionado,
                onClick = onSeleccionar,
                role = Role.RadioButton
            )
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = seleccionado,
            onClick = null,
            colors = RadioButtonDefaults.colors(selectedColor = VerdeBodega)
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = texto,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
        if (detalle != null) {
            Text(
                text = detalle,
                style = MaterialTheme.typography.bodyMedium,
                color = GrisTexto
            )
        }
    }
}

@Composable
private fun FilaTotal(etiqueta: String, valor: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = etiqueta, color = GrisTexto)
        Text(text = valor, color = GrisTexto)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun DatosEntregaPreview() {
    BodegaTheme {
        DatosEntregaScreen(
            subtotal = 21.90,
            onVolver = {},
            onConfirmarPedido = {}
        )
    }
}