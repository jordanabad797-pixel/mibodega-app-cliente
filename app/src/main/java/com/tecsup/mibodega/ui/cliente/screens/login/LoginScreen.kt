package com.tecsup.mibodega.ui.cliente.screens.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.componentes.BotonPrimario
import com.tecsup.mibodega.ui.theme.BodegaTheme
import com.tecsup.mibodega.ui.theme.GrisBorde
import com.tecsup.mibodega.ui.theme.GrisClaro
import com.tecsup.mibodega.ui.theme.GrisTexto
import com.tecsup.mibodega.ui.theme.VerdeBodega

private const val LONGITUD_TELEFONO = 9

@Composable
fun LoginScreen(
    onVolver: () -> Unit,
    onIngresar: () -> Unit
) {
    var telefono by rememberSaveable { mutableStateOf("") }
    var intentoIngresar by remember { mutableStateOf(false) }

    val telefonoValido = telefono.length == LONGITUD_TELEFONO

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
    ) {
        EncabezadoLogin(onVolver = onVolver)

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 20.dp)
        ) {
            Text(
                text = "Bienvenido de nuevo",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 16.dp)
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Ingresa tu número de teléfono para continuar",
                style = MaterialTheme.typography.bodyMedium,
                color = GrisTexto
            )

            OutlinedTextField(
                value = telefono,
                onValueChange = { nuevoValor ->
                    telefono = nuevoValor.filter { it.isDigit() }.take(LONGITUD_TELEFONO)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                label = { Text("Teléfono") },
                singleLine = true,
                isError = intentoIngresar && !telefonoValido,
                supportingText = if (intentoIngresar && !telefonoValido) {
                    { Text("Ingresa un teléfono de $LONGITUD_TELEFONO dígitos") }
                } else null,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
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

        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)) {
            BotonPrimario(
                texto = "Iniciar sesión",
                onClick = {
                    intentoIngresar = true
                    if (telefonoValido) onIngresar()
                }
            )
        }
    }
}

@Composable
private fun EncabezadoLogin(onVolver: () -> Unit) {
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
            text = "Iniciar sesión",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun LoginPreview() {
    BodegaTheme {
        LoginScreen(
            onVolver = {},
            onIngresar = {}
        )
    }
}