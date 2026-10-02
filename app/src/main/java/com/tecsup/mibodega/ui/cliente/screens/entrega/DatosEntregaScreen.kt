package com.tecsup.mibodega.ui.cliente.screens.entrega

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.tecsup.mibodega.ui.componentes.BotonPrimario
import com.tecsup.mibodega.ui.theme.BodegaTheme
import com.tecsup.mibodega.ui.theme.GrisBorde
import com.tecsup.mibodega.ui.theme.GrisClaro
import com.tecsup.mibodega.ui.theme.VerdeBodega

private val metodosPago = listOf("Efectivo al entregar", "Yape", "Plin")

@Composable
fun DatosEntregaScreen(
    onVolver: () -> Unit,
    onConfirmarPedido: () -> Unit,
    nombreInicial: String = "",
    telefonoInicial: String = "",
    direccionInicial: String = "",
    referenciaInicial: String = ""
) {
    var nombre by rememberSaveable { mutableStateOf(nombreInicial) }
    var telefono by rememberSaveable { mutableStateOf(telefonoInicial) }
    var direccion by rememberSaveable { mutableStateOf(direccionInicial) }
    var referencia by rememberSaveable { mutableStateOf(referenciaInicial) }
    var metodoPago by rememberSaveable { mutableStateOf(metodosPago.first()) }
    var intentoConfirmar by remember { mutableStateOf(false) }

    val formularioValido = nombre.isNotBlank() &&
            telefono.isNotBlank() &&
            direccion.isNotBlank()

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

            Text(
                text = "Método de pago",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 24.dp, bottom = 4.dp)
            )

            metodosPago.forEach { metodo ->
                FilaMetodoPago(
                    texto = metodo,
                    seleccionado = metodo == metodoPago,
                    onSeleccionar = { metodoPago = metodo }
                )
            }
        }

        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)) {
            BotonPrimario(
                texto = "Confirmar pedido",
                onClick = {
                    intentoConfirmar = true
                    if (formularioValido) onConfirmarPedido()
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
            unfocusedBorderColor = GrisBorde,
            focusedBorderColor = VerdeBodega,
            focusedLabelColor = VerdeBodega
        )
    )
}

@Composable
private fun FilaMetodoPago(
    texto: String,
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
        Text(text = texto, style = MaterialTheme.typography.bodyLarge)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun DatosEntregaPreview() {
    BodegaTheme {
        DatosEntregaScreen(
            onVolver = {},
            onConfirmarPedido = {}
        )
    }
}