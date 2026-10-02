# PROMPTS.md — Mi Bodega (App Cliente), Fase 2 (rama `mejora-ia`)

**Alumno:** Jordan Abad
**Curso:** Programación en Móviles
**Tarea:** Mi Bodega — App Cliente (complementaria al Laboratorio 6)
**Asistente de IA:** Claude (Anthropic)
**Rama:** `mejora-ia`, creada a partir de `main`
**Repositorio:** `mibodega-app-cliente`

## 1. Mejora obligatoria

Hacer que el campo de búsqueda de la Pantalla 3 (Inicio) filtre la lista de productos en tiempo real a medida que el usuario escribe, combinándose correctamente con el filtro de categoría ya existente: ambos filtros deben funcionar juntos y no reemplazarse.

## 2. Punto de partida

El esqueleto entregado por el profesor ya incluía un buscador básico en `InicioScreen.kt`: filtraba por nombre con `contains(ignoreCase = true)` y lo combinaba con la categoría seleccionada. Antes de pedir cambios se revisó el archivo y se confirmó esto. Por eso la Fase 2 no crea el buscador desde cero: corrige las limitaciones del que ya existía.

- No encontraba "Arroz Costeño" al escribir `costeno`, porque no ignoraba tildes ni la ñ.
- Los espacios antes o después del texto afectaban la coincidencia.
- No había forma rápida de borrar el texto escrito.
- Si no había coincidencias, la pantalla quedaba en blanco y sin explicación.
- Al filtrar, el usuario no veía cuántos productos coincidían.

## 3. Requerimientos funcionales

**RF-01 — Búsqueda en tiempo real.**
El listado de productos se actualiza con cada carácter que el usuario escribe, sin botón de "Buscar".
*Criterio de aceptación:* al escribir `ac` solo permanece visible "Aceite Primor".
*Cumplido en:* base del esqueleto, mejorado en N1.

**RF-02 — Combinación con el filtro de categoría.**
El texto de búsqueda y la categoría seleccionada se aplican al mismo tiempo. Cambiar uno no reinicia ni reemplaza al otro.
*Criterio de aceptación:* con el chip Abarrotes activo y el texto `aceite`, solo aparece "Aceite Primor". Al cambiar de chip, el texto escrito se conserva.
*Cumplido en:* N1 (función `filtrarProductos`, que recibe ambos criterios).

**RF-03 — Búsqueda tolerante.**
La búsqueda ignora tildes, la letra ñ, diferencias entre mayúsculas y minúsculas, y los espacios al inicio y al final del texto.
*Criterio de aceptación:* `costeno`, `COSTEÑO` y `  costeño  ` encuentran "Arroz Costeño".
*Cumplido en:* N1 (función `normalizar`).

**RF-04 — Borrar la búsqueda.**
Cuando el campo contiene texto se muestra un botón ✕ que lo borra. Con el campo vacío el botón no aparece. El botón no modifica la categoría seleccionada.
*Criterio de aceptación:* tras tocar la ✕ el campo queda vacío y el chip activo sigue siendo el mismo.
*Cumplido en:* N2.

**RF-05 — Estado sin resultados.**
Si ningún producto cumple los filtros, se muestra un mensaje que indica el texto y la categoría consultados, junto con un botón "Limpiar filtros" que reinicia ambos filtros a la vez.
*Criterio de aceptación:* con el chip Bebidas y el texto `arroz`, aparece el mensaje y, al tocar el botón, vuelve la lista completa con el chip "Todos".
*Cumplido en:* N3.

**RF-06 — Contador de resultados.**
Mientras haya un texto o una categoría distinta de "Todos", el título de la lista muestra cuántos productos coinciden ("1 producto encontrado" o "N productos encontrados"). Sin filtros activos se mantiene "Productos destacados".
*Criterio de aceptación:* con Abarrotes activo el título indica 3 productos, y al escribir `aceite` pasa a 1.
*Cumplido en:* N4.

## 4. Plan de commits de la rama

- **N1:** el filtrado pasa a una función propia que ignora tildes, mayúsculas y espacios (RF-02, RF-03).
- **N2:** botón ✕ dentro del buscador para borrar el texto de un toque (RF-04).
- **N3:** mensaje de "Sin resultados" con un botón para limpiar los filtros (RF-05).
- **N4:** contador de resultados que se actualiza al filtrar (RF-06).
- **N5:** documentación de prompts y requerimientos funcionales en este archivo.

## 5. Prompts utilizados

### Prompt 1 — Filtrado sin tildes ni mayúsculas (commit N1)

**Prompt:** Ya está el commit 10 de la Fase 1, vamos por la Fase 2. Hay 4 commits en esta fase y no te olvides del archivo PROMPTS.md.

**Respuesta de la IA:** Advirtió que el buscador ya existía en el esqueleto del profesor y propuso mejorarlo en lugar de rehacerlo. Creó `FiltroProductos.kt` con una función `normalizar()` que descompone el texto con `Normalizer.Form.NFD`, elimina las marcas diacríticas, pasa a minúsculas y recorta espacios. También creó `filtrarProductos(productos, categoria, texto)`, que aplica a la vez el filtro de categoría y el de texto. `InicioScreen` pasó a usar esa función.

**Correcciones y verificaciones:**
- El enfoque inicial se ajustó al revisar el código real: en vez de implementar un buscador nuevo, se mejoró el existente. Esto evita duplicar lógica y deja claro qué aporta la IA.
- Se comprobó que `costeno`, `ACEITE` y `  coca  ` encuentran sus productos, y que el filtro de categoría sigue funcionando junto con el texto.

**Requerimientos cubiertos:** RF-02, RF-03.

**Commit asociado:** `N1: Extrae el filtrado a filtrarProductos e ignora tildes, mayusculas y espacios al buscar`

### Prompt 2 — Botón para borrar la búsqueda (commit N2)

**Prompt:** Vamos por el commit 2.

**Respuesta de la IA:** Agregó un `trailingIcon` al `OutlinedTextField` de búsqueda. El ícono ✕ solo se muestra cuando `textoBusqueda` no está vacío, y al tocarlo deja el texto en blanco.

**Correcciones y verificaciones:**
- Se verificó que el botón limpia únicamente el texto y no cambia la categoría seleccionada. Así los dos filtros siguen siendo independientes, como pide el enunciado.
- Con el campo vacío no aparece ningún ícono, para no dejar un botón sin función.

**Requerimientos cubiertos:** RF-04.

**Commit asociado:** `N2: Agrega boton para borrar el texto del buscador de Inicio`

### Prompt 3 — Mensaje de sin resultados (commit N3)

**Prompt:** Ya (confirmación de que el N2 estaba subido; la IA continuó con el N3 del plan acordado).

**Respuesta de la IA:** Agregó el composable privado `SinResultados`, que se muestra cuando `productosFiltrados` está vacío. El título cambia según el caso: texto y categoría, solo texto, o solo categoría. Incluye el botón **Limpiar filtros**, que borra el texto y vuelve al chip "Todos".

**Correcciones y verificaciones:**
- Se verificó que el botón reinicia los dos filtros a la vez, a diferencia de la ✕ del N2, que solo borra el texto.
- Se comprobó la combinación categoría + texto: con el chip Bebidas y la búsqueda `arroz`, el mensaje indica que no se encontró "arroz" en Bebidas.

**Requerimientos cubiertos:** RF-05.

**Commit asociado:** `N3: Muestra mensaje de Sin resultados con boton para limpiar los filtros de Inicio`

### Prompt 4 — Contador de resultados (commit N4)

**Prompt:** Antes de hacer el PROMPTS.md, un commit más.

**Respuesta de la IA:** Propuso un contador de resultados. El título "Productos destacados" pasa a mostrar "1 producto encontrado" o "N productos encontrados" mientras haya un texto o una categoría distinta de "Todos", y vuelve al título original cuando no hay filtros.

**Correcciones y verificaciones:**
- Se comprobó que el contador refleja la combinación de ambos filtros: con el chip Abarrotes aparecen 3 productos, y al escribir `aceite` baja a 1.
- Se verificó el caso límite: con `zzz` el título dice 0 productos y debajo se muestra el mensaje de "Sin resultados" del N3.

**Requerimientos cubiertos:** RF-06.

**Commit asociado:** `N4: Muestra el contador de resultados al filtrar por categoria o texto en Inicio`

### Prompt 5 — Documentación del proceso (commit N5)

**Prompt:** No colocaré imágenes, solo la documentación de los prompts usados y los requerimientos funcionales.

**Respuesta de la IA:** Generó este archivo con la estructura: mejora, punto de partida, requerimientos funcionales con su criterio de aceptación, plan de commits, y por cada hito el prompt, la respuesta, las correcciones y los requerimientos cubiertos, más una reflexión final.

**Correcciones y verificaciones:** Se ajustó el contenido a lo pedido: se eliminaron las referencias a capturas y se agregó la trazabilidad entre cada requerimiento y el commit que lo cumple.

**Commit asociado:** `N5: Documenta prompts y requerimientos funcionales de la mejora con IA en PROMPTS.md`

## 6. Reflexión: ¿qué tuve que corregir del código generado por la IA?

1. **El alcance, antes que el código.** Lo más importante fue detectar que el buscador ya existía. Pedirle a la IA una mejora sin revisar el código base habría duplicado lógica. Revisar primero cambió el plan de "crear" a "mejorar".
2. **Dos filtros independientes.** El enunciado exige que categoría y texto funcionen juntos sin reemplazarse. Cada mejora se diseñó para respetar eso: la ✕ borra solo el texto, y "Limpiar filtros" reinicia los dos de forma explícita.
3. **Una función pura y reutilizable.** Mover el filtrado a `filtrarProductos` lo saca de la pantalla, lo hace más fácil de probar y lo deja listo para reutilizarse en la pantalla de Categorías.
4. **Validación propia.** Cada commit se probó en el emulador antes de subirlo: tildes, mayúsculas, espacios, combinación de filtros, el caso sin resultados y el contador.