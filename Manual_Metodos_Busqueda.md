# Manual de implementación de los métodos de búsqueda

Este manual describe cómo funciona el programa Java de este repositorio. Se enfoca en la implementación: qué estructura utiliza cada método, cómo selecciona el siguiente nodo, cómo reconstruye el camino y cómo leer el análisis que muestra la consola.

En la opción **Crear nodo(s)** puedes ingresar varios identificadores separados por comas, por ejemplo `H, A, B, C`. Los IDs se recortan para quitar espacios; si alguno ya existe, se omite y se informa sin detener la creación de los demás. Los nodos creados desde esta opción comienzan con heurística `0`; después puedes ajustarla en **Asignar heurística**.

La opción **Asignar heurística** recorre los nodos existentes y solicita `h(n)` para cada uno. Presiona Enter en un nodo para conservar su valor actual. Al terminar, muestra todas las heurísticas y permite escribir el ID de cualquier nodo para corregir su valor; presiona Enter sin ID cuando la revisión esté lista.

## 1. Ejecutar el programa

Ejecuta `Main.main()` desde IntelliJ IDEA. Al iniciar aparece una selección de grafo:

1. **Ejemplo de clase H → G**, opción predeterminada: presiona Enter.
2. **Ejemplo ponderado A* H → F**.
3. Grafo vacío no dirigido.
4. Grafo vacío dirigido.

El ejemplo contiene los nodos `H, A, B, C, D, E, F, G, J, K, L`. Las conexiones tienen peso 1; `H` es el inicio y `G` el objetivo. También incluye heurísticas para probar los métodos informados.

En el menú, selecciona **9. Ejecutar búsqueda** y luego el método. Con BFS, el camino esperado es `H → C → G`.

En el segundo ejemplo, selecciona **A*** para ver la tabla de candidatos con `h(n) + g(n) = f(n)`.

## 2. Organización del código

| Paquete | Responsabilidad |
| --- | --- |
| `Modelo` | Grafo, nodos, conexiones y resultados de búsqueda. |
| `Modelo.Busqueda` | Interfaz y clases de los ocho algoritmos, además de utilidades compartidas. |
| `Controlador` | Gestión del grafo, selección y comparación de métodos. |
| `Vista` | Entrada y salida del menú de consola. |

`Main` conecta la vista con los controladores. El menú no contiene la estrategia de búsqueda: llama a `ControladorBusqueda`, que ejecuta el método seleccionado sobre el grafo.

### Clases principales

- `Nodo` guarda un id inmutable, la heurística y sus conexiones salientes. Los nodos se comparan por id.
- `Conexion` guarda el destino y el peso. El origen se deduce del nodo que contiene la conexión.
- `Grafo` conserva los nodos en un `LinkedHashMap`: permite buscarlos por id y mantiene el orden en que fueron agregados. Puede ser dirigido o no dirigido.
- `MetodoBusqueda` define el contrato común `buscar(grafo, inicio, objetivo)`.
- `ResultadoBusqueda` devuelve si se encontró el objetivo, el camino, los visitados, el costo, el mensaje y los pasos de análisis.
- `PasoBusqueda` registra el nodo extraído, los nodos agregados, la frontera restante y el tipo de frontera. También puede indicar el lado de búsqueda o el límite de profundidad.
- `UtilidadesBusqueda` comparte la validación de argumentos, la búsqueda de nodos canónicos del grafo, la reconstrucción del camino y el cálculo de su costo.
- `EntradaPrioridad` ordena las entradas de UCS, Greedy y A* por prioridad; cuando hay empate, respeta el orden de inserción.

## 3. Cómo se representa el grafo

Cada nodo tiene su propia lista de conexiones. Para agregar `A → B` con peso 4, el grafo guarda una conexión saliente en `A` cuyo destino es `B` y cuyo peso es 4.

En un grafo no dirigido, `Grafo` agrega también la conexión inversa. En uno dirigido solo agrega la indicada. Si se crea una conexión desde el menú y se deja vacío el campo del peso, se usa peso 1. Así se pueden representar grafos sin costos diferenciados; BFS y DFS ignoran los pesos para decidir el recorrido.

Los pesos deben ser finitos y no negativos. El id se guarda sin espacios exteriores y distingue mayúsculas: `A` y `a` son ids diferentes.

## 4. Resultado y análisis paso a paso

Todos los algoritmos devuelven un `ResultadoBusqueda`. El costo es la suma de los pesos del camino que encontró el método. Si no hay ruta, `encontrado` es falso, el camino está vacío y el costo es infinito; el mensaje explica el resultado.

Cada `PasoBusqueda` representa una extracción y el estado de la frontera al terminar ese paso. Normalmente se guarda después de procesar los vecinos; si el nodo extraído es el objetivo, la búsqueda termina sin expandirlo. El paso muestra:

- **Extrae:** el nodo que el algoritmo acaba de seleccionar.
- **Frontera:** los nodos que quedan por procesar, ordenados desde el próximo que saldrá.
- **Agregados:** vecinos que se incorporaron durante ese paso.
- **Tipo de frontera:** `COLA`, `PILA`, `PRIORIDAD` o `VECINOS`.
- **Lado:** en bidireccional, indica si se expandió desde el inicio o desde el objetivo.
- **Límite:** en IDDFS, indica el límite de profundidad de esa iteración.

El menú muestra primero la frontera inicial. La traza completa requiere memoria adicional porque guarda una copia de la frontera en cada paso; para grafos grandes esto puede consumir más memoria que ejecutar el algoritmo sin registrar el análisis.

## 5. Los ocho métodos

### 5.1 Búsqueda en amplitud (BFS)

**Clase:** `BusquedaAmplitud`

Utiliza una cola FIFO (`ArrayDeque`). Agrega los vecinos al final y extrae desde el inicio, por lo que recorre el grafo por niveles. Marca un nodo como descubierto cuando lo agrega a la cola; así evita duplicarlo y registra su padre una sola vez.

- **Prioridad:** menor número de conexiones desde el inicio.
- **Garantía:** encuentra una ruta con la menor cantidad de aristas en un grafo finito no ponderado. Si hay pesos distintos, no garantiza el menor costo.
- **Complejidad del recorrido sin traza:** `O(V + E)` tiempo y `O(V)` memoria auxiliar.

En el árbol de clase, suponiendo que las conexiones de `H` se agregan en el orden `A, B, C`, BFS produce:

| Cola inicial / cola después del paso | Extraído |
| --- | --- |
| `[H]` | — |
| `[A, B, C]` | `H` |
| `[B, C, D, E]` | `A` |
| `[C, D, E, F]` | `B` |
| `[D, E, F, G, J]` | `C` |
| `[E, F, G, J, K, L]` | `D` |
| `[F, G, J, K, L]` | `E` |
| `[G, J, K, L]` | `F` |
| `[J, K, L]` | `G` — objetivo; termina la búsqueda |

Camino devuelto: `H → C → G`.

### 5.2 Búsqueda en profundidad (DFS)

**Clase:** `BusquedaProfundidad`

Utiliza una pila LIFO (`ArrayDeque`). Para mantener el orden de los vecinos en la exploración, descubre los vecinos en el orden de la lista y los apila en orden inverso. Marca como descubiertos al apilarlos, registra el padre y evita ciclos y duplicados.

- **Prioridad:** continuar por una rama antes de retroceder.
- **Garantía:** encuentra una ruta si existe en un grafo finito; no garantiza la ruta más corta ni la de menor costo.
- **Complejidad del recorrido sin traza:** `O(V + E)` tiempo y `O(V)` memoria auxiliar.

El resultado incluye el costo real de la ruta encontrada, aunque DFS no usa ese costo para elegir la ruta.

### 5.3 Búsqueda bidireccional

**Clase:** `BusquedaBidireccional`

Ejecuta BFS desde el inicio y desde el objetivo, expandiendo niveles alternados. Guarda padres y distancias por cada lado y une ambas cadenas cuando se encuentran.

Para un grafo dirigido, la búsqueda desde el objetivo necesita recorrer aristas entrantes. La implementación prepara una lista de predecesores invirtiendo las conexiones para ese recorrido; esto no cambia la dirección del camino final.

- **Prioridad:** encontrar el encuentro con menor suma de niveles y, por tanto, una ruta con la menor cantidad de aristas.
- **Garantía:** ruta mínima en número de aristas, no necesariamente de menor peso.
- **Complejidad sobre el grafo almacenado:** `O(V + E)` tiempo y memoria adicional para las listas de sucesores y predecesores. La traza guarda además las dos fronteras.

El resultado evita repetir nodos en la lista general de visitados, aunque un nodo puede ser procesado desde ambos lados.

### 5.4 Búsqueda en profundidad iterativa (IDDFS)

**Clase:** `BusquedaProfundidadIterativa`

Repite una búsqueda con pila, aumentando el límite desde 0 hasta `número de nodos − 1`. En cada iteración mantiene la mejor profundidad a la que encontró cada nodo; esto evita ciclos y permite volver a explorar un nodo si se alcanza por una ruta más corta dentro de esa iteración.

- **Prioridad:** orden de DFS dentro del límite actual.
- **Garantía:** encuentra una ruta con la menor cantidad de aristas en grafos finitos.
- **Costo:** repite parte del trabajo de las iteraciones anteriores. La implementación guarda los pasos de todas las iteraciones para poder mostrarlos.

Los pasos incluyen el límite usado. El contador de nodos explorados refleja las visitas acumuladas entre las iteraciones, por lo que un mismo nodo puede contarse más de una vez.

### 5.5 Búsqueda de coste uniforme (UCS)

**Clase:** `BusquedaCosteUniforme`

Utiliza una cola de prioridad ordenada por `g(n)`, el costo acumulado desde el inicio. Si encuentra un costo menor para un nodo, actualiza su padre y agrega una nueva entrada. Las entradas antiguas de la cola se descartan cuando se extraen.

- **Prioridad:** `g(n)`.
- **Garantía:** encuentra una ruta de costo mínimo si los pesos son no negativos.
- **Complejidad típica:** `O((V + E) log V)` con cola binaria y costos no negativos; las entradas de prioridad y la traza usan memoria adicional.

### 5.6 Ascenso a la colina (Hill Climbing)

**Clase:** `AscensoColina`

Examina los vecinos no visitados del nodo actual, los ordena por heurística y avanza al primero solo si su `h(n)` es estrictamente menor que la del nodo actual. No mantiene una frontera global.

- **Prioridad:** el vecino con menor heurística local.
- **Ventaja:** sencillo y puede avanzar con poca memoria.
- **Límite:** puede detenerse en un mínimo local o una meseta aunque exista una ruta. Informa cuando no hay un vecino que mejore la heurística.

Las heurísticas deben guiar hacia el objetivo. Si todos los nodos tienen heurística 0, el algoritmo no tiene una mejora estricta y se detiene.

### 5.7 Primero el mejor (Greedy / Best-First)

**Clase:** `BusquedaPrimeroMejor`

Utiliza una cola de prioridad que ordena los nodos por `h(n)`. Marca cada nodo al agregarlo por primera vez y conserva el padre de ese descubrimiento.

- **Prioridad:** `h(n)` solamente.
- **Ventaja:** puede dirigirse rápidamente hacia un objetivo si la heurística es útil.
- **Límite:** ignora el costo acumulado y no garantiza la ruta de menor costo.

### 5.8 A*

**Clase:** `BusquedaAEstrella`

Utiliza una cola de prioridad ordenada por `f(n) = g(n) + h(n)`. Conserva el menor `g(n)` conocido y actualiza el padre si encuentra una ruta más barata. Las entradas antiguas se descartan; si mejora el costo, el nodo puede volver a entrar en la frontera.

- `g(n)`: costo acumulado desde el inicio.
- `h(n)`: heurística estimada hasta el objetivo, almacenada en `Nodo`.
- `f(n)`: prioridad con la que A* ordena la frontera.
- **Garantía:** encuentra la ruta de costo mínimo cuando la heurística es admisible (no sobreestima el costo restante) y los pesos no son negativos.

La consola muestra una tabla por extracción. Para cada candidato imprime `nodo(h+g=f)` en el orden de prioridad de la frontera. La fila de un nodo indica los candidatos que había antes de extraerlo.

## 6. Ejemplo ponderado de A*

Al iniciar, selecciona la opción **2. Ejemplo ponderado A* H → F**. Se crea un grafo no dirigido con estas heurísticas:

| Nodo | H | A | C | D | E | G | J | K | L | B | F |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| `h(n)` | 20 | 14 | 16 | 9 | 5 | 12 | 8 | 3 | 6 | 3 | 0 |

Los pesos son los números junto a las aristas del dibujo: `H-A=14`, `H-C=16`, `A-D=9`, `A-E=5`, `C-G=12`, `C-J=8`, `D-K=3`, `D-L=6`, `J-B=3`, `L-F=6` y `B-F=4`. El inicio es `H` y el objetivo es `F`.

Con el desempate estable por orden de inserción que usa `EntradaPrioridad`, A* muestra:

| Nodo seleccionado | Candidatos antes de extraer (`nodo(h+g=f)`) |
| --- | --- |
| H | H(20+0=20) |
| A | A(14+14=28), C(16+16=32) |
| E | E(5+19=24), C(16+16=32), D(9+23=32) |
| C | C(16+16=32), D(9+23=32) |
| D | D(9+23=32), J(8+24=32), G(12+28=40) |
| K | K(3+26=29), J(8+24=32), L(6+29=35), G(12+28=40) |
| J | J(8+24=32), L(6+29=35), G(12+28=40) |
| B | B(3+27=30), L(6+29=35), G(12+28=40) |
| F | F(0+31=31), L(6+29=35), G(12+28=40) |

El resultado es el camino `H → C → J → B → F`, con costo total `31`. Aunque `C` y `D` empatan con `f=32`, `C` se insertó primero y se extrae primero. La heurística indicada para `C` (`16`) sobreestima en 1 el costo real restante hasta `F` (`J-B-F = 8+3+4 = 15`), así que en este ejemplo A* encuentra el camino de costo 31, pero la condición general de optimalidad no queda garantizada por esa heurística.

**Nota sobre la tabla compartida:** siguiendo literalmente los pesos del dibujo, `g(C)=16` y `g(D)=14+9=23`; por eso no resultan `C(16+14)` ni `D(9+28)`. Además, después de `E`, `C` y `D` empatan con `f=32`, así que la implementación extrae primero `C` por orden de inserción. La tabla de arriba conserva las heurísticas y pesos del diagrama y refleja los cálculos de la implementación.

## 7. Diferencias de criterio

| Método | Estructura | Criterio de selección | ¿Garantiza costo mínimo? |
| --- | --- | --- | --- |
| BFS | Cola FIFO | Nivel / cantidad de aristas | Solo si todas las aristas tienen el mismo costo |
| DFS | Pila LIFO | Profundizar por una rama | No |
| Bidireccional | Dos colas FIFO | Niveles desde ambos extremos | Solo si se mide cantidad de aristas |
| IDDFS | Pila LIFO con límites | Profundidad creciente | Solo en cantidad de aristas |
| UCS | Cola de prioridad | `g(n)` | Sí, con pesos no negativos |
| Hill Climbing | Vecinos del nodo actual | Menor `h(n)` local | No |
| Greedy | Cola de prioridad | `h(n)` | No |
| A* | Cola de prioridad | `g(n) + h(n)` | Sí, con heurística admisible y pesos no negativos |

## 8. Cómo agregar otro método

1. Crear una clase en `Modelo.Busqueda` que implemente `MetodoBusqueda`.
2. Validar los argumentos y resolver los nodos del grafo con `UtilidadesBusqueda`.
3. Implementar su propia estructura de frontera y registrar pasos como `PasoBusqueda`.
4. Construir el camino con `UtilidadesBusqueda.reconstruirCamino` cuando aplique.
5. Devolver `ResultadoBusqueda` en éxito o fracaso.
6. Registrar la instancia en el constructor de `ControladorBusqueda`; el menú y el comparador enumeran los métodos registrados.

## 9. Notas de interpretación

- `visitados` representa nodos extraídos/procesados. En IDDFS incluye visitas repetidas entre límites; en bidireccional conserva nodos únicos.
- El costo que se muestra es el costo de la ruta devuelta, incluso para BFS y DFS. No significa que esos métodos hayan optimizado el costo.
- Para que UCS o A* sean útiles con costos diferentes, asigna pesos reales al crear conexiones. Una conexión sin peso explícito recibe peso 1.
- Hill Climbing y Greedy dependen de las heurísticas introducidas manualmente. El cálculo automático de heurísticas y las posiciones gráficas todavía no forman parte del programa.
- La ejecución completa se realiza antes de mostrar la traza. La pausa, el avance interactivo, la interfaz gráfica y las estadísticas de tiempo están pendientes.
