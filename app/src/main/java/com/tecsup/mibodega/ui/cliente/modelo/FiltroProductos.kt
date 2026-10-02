package com.tecsup.mibodega.ui.cliente.modelo

import java.text.Normalizer

private val REGEX_ACENTOS = "\\p{InCombiningDiacriticalMarks}+".toRegex()

fun String.normalizar(): String =
    Normalizer.normalize(this, Normalizer.Form.NFD)
        .replace(REGEX_ACENTOS, "")
        .lowercase()
        .trim()

fun filtrarProductos(
    productos: List<Producto>,
    categoria: String,
    texto: String
): List<Producto> {
    val textoNormalizado = texto.normalizar()

    return productos.filter { producto ->
        val coincideCategoria = categoria == "Todos" || producto.categoria == categoria
        val coincideBusqueda = textoNormalizado.isEmpty() ||
                producto.nombre.normalizar().contains(textoNormalizado)
        coincideCategoria && coincideBusqueda
    }
}