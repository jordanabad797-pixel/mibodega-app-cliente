package com.tecsup.mibodega.ui.cliente.screens.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.componentes.BotonPrimario
import com.tecsup.mibodega.ui.theme.BodegaTheme
import com.tecsup.mibodega.ui.theme.GrisBorde
import com.tecsup.mibodega.ui.theme.GrisClaro
import com.tecsup.mibodega.ui.theme.GrisTexto
import com.tecsup.mibodega.ui.theme.RojoPrecio
import com.tecsup.mibodega.ui.theme.VerdeBodega

private const val USUARIO_VALIDO = "cliente"
private const val CLAVE_VALIDA = "1234"

@Composable
fun LoginScreen(
    onVolver: () -> Unit,
    onIngresar: () -> Unit
) {
    var usuario by rememberSaveable { mutableStateOf("") }
    var clave by rememberSaveable { mutableStateOf("") }
    var verClave by rememberSaveable { mutableStateOf(false) }
    var intento by remember { mutableStateOf(false) }
    var credencialesIncorrectas by remember { mutableStateOf(false) }

    val usuarioVacio = intento && usuario.isBlank()
    val claveVacia = intento && clave.isBlank()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
    ) {
        EncabezadoLogin(onVolver = onVolver)

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
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
                text = "Ingresa tu usuario y contraseña para continuar",
                style = MaterialTheme.typography.bodyMedium,
                color = GrisTexto
            )

            OutlinedTextField(
                value = usuario,
                onValueChange = {
                    usuario = it
                    credencialesIncorrectas = false
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                label = { Text("Usuario") },
                singleLine = true,
                isError = usuarioVacio || credencialesIncorrectas,
                supportingText = if (usuarioVacio) {
                    { Text("Campo obligatorio") }
                } else null,
                shape = RoundedCornerShape(12.dp),
                colors = coloresCampo()
            )

            OutlinedTextField(
                value = clave,
                onValueChange = {
                    clave = it
                    credencialesIncorrectas = false
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                label = { Text("Contraseña") },
                singleLine = true,
                isError = claveVacia || credencialesIncorrectas,
                supportingText = if (claveVacia) {
                    { Text("Campo obligatorio") }
                } else null,
                visualTransformation = if (verClave) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(onClick = { verClave = !verClave }) {
                        Icon(
                            imageVector = if (verClave) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = if (verClave) "Ocultar contraseña" else "Mostrar contraseña"
                        )
                    }
                },
                shape = RoundedCornerShape(12.dp),
                colors = coloresCampo()
            )

            if (credencialesIncorrectas) {
                Text(
                    text = "Usuario o contraseña incorrectos",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = RojoPrecio,
                    modifier = Modifier.padding(top = 12.dp)
                )
            }

            Text(
                text = "Usuario de prueba: $USUARIO_VALIDO · Contraseña: $CLAVE_VALIDA",
                style = MaterialTheme.typography.bodySmall,
                color = GrisTexto,
                modifier = Modifier.padding(top = 20.dp)
            )
        }

        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)) {
            BotonPrimario(
                texto = "Iniciar sesión",
                onClick = {
                    intento = true
                    if (usuario.isNotBlank() && clave.isNotBlank()) {
                        if (usuario.trim() == USUARIO_VALIDO && clave == CLAVE_VALIDA) {
                            credencialesIncorrectas = false
                            onIngresar()
                        } else {
                            credencialesIncorrectas = true
                        }
                    }
                }
            )
        }
    }
}

@Composable
private fun coloresCampo() = OutlinedTextFieldDefaults.colors(
    unfocusedContainerColor = GrisClaro,
    focusedContainerColor = GrisClaro,
    errorContainerColor = GrisClaro,
    unfocusedBorderColor = GrisBorde,
    focusedBorderColor = VerdeBodega,
    focusedLabelColor = VerdeBodega
)

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