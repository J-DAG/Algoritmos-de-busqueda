# Software de Métodos de Búsqueda en IA

## 1. Descripción del proyecto

Desarrollar una aplicación educativa que permita **visualizar, ejecutar
y comparar métodos de búsqueda en Inteligencia Artificial** sobre
grafos.

El software debe permitir al usuario crear o cargar un grafo,
seleccionar un algoritmo de búsqueda, configurar sus parámetros y
observar paso a paso cómo el algoritmo recorre los nodos hasta encontrar
un objetivo.

Los métodos que debe implementar el software son:

### Búsqueda no informada

1.  Búsqueda en amplitud (BFS)
2.  Búsqueda en profundidad (DFS)
3.  Búsqueda bidireccional
4.  Búsqueda en profundidad iterativa (IDDFS)
5.  Búsqueda de coste uniforme (UCS)

### Búsqueda informada / heurística

6.  Método del gradiente / Ascenso a la colina (Hill Climbing)
7.  Búsqueda primero el mejor (Best-First / Greedy)
8.  Búsqueda A\*

------------------------------------------------------------------------

# 2. Objetivo general

Crear un software que permita comprender visualmente el funcionamiento
de diferentes algoritmos de búsqueda mediante la representación gráfica
de un problema como un **grafo dirigido o no dirigido**, mostrando:

-   Estado inicial.
-   Estado objetivo.
-   Nodos y conexiones.
-   Costos de las aristas.
-   Valores heurísticos.
-   Orden de exploración.
-   Frontera de búsqueda.
-   Nodos visitados.
-   Camino encontrado.
-   Costo total.
-   Número de nodos explorados.
-   Tiempo de ejecución.
-   Resultado del algoritmo.

------------------------------------------------------------------------

# 3. Objetivos específicos

-   Permitir crear grafos manualmente.
-   Permitir agregar y eliminar nodos.
-   Permitir conectar nodos mediante aristas.
-   Permitir asignar costos a las aristas.
-   Permitir asignar valores heurísticos a los nodos.
-   Seleccionar un nodo inicial.
-   Seleccionar un nodo objetivo.
-   Ejecutar cualquiera de los ocho algoritmos.
-   Mostrar la ejecución paso a paso.
-   Permitir ejecutar la búsqueda automáticamente.
-   Permitir pausar, continuar y reiniciar una ejecución.
-   Mostrar estadísticas de cada algoritmo.
-   Comparar resultados entre diferentes algoritmos.
-   Validar entradas incorrectas.
-   Mostrar mensajes claros al usuario.

------------------------------------------------------------------------

# 4. Conceptos fundamentales

El problema de búsqueda se representa mediante un grafo:

``` text
        A
       / \
      B   C
     / \   \
    D   E   F
             \
              G
```

Cada nodo representa un **estado** y cada conexión representa una
**acción o transición**.

Un problema de búsqueda contiene:

``` text
Estado inicial
      ↓
   Espacio de estados
      ↓
Estado objetivo
```

Ejemplo:

``` text
Inicio: A
Objetivo: G

A → C → F → G
```

------------------------------------------------------------------------

# 5. Modelo de datos del grafo

Cada nodo debe tener como mínimo:

``` text
Nodo
- id
- nombre
- posición X
- posición Y
- heurística h(n)
```

Cada arista debe tener:

``` text
Arista
- nodoOrigen
- nodoDestino
- costo
```

Para un grafo no dirigido:

``` text
A ↔ B
```

Para un grafo dirigido:

``` text
A → B
```

------------------------------------------------------------------------

# 6. Funciones principales del software

## 6.1 Gestión del grafo

La aplicación debe permitir:

-   Crear un grafo nuevo.
-   Agregar nodos.
-   Eliminar nodos.
-   Mover nodos.
-   Agregar aristas.
-   Eliminar aristas.
-   Cambiar costos.
-   Cambiar valores heurísticos.
-   Seleccionar inicio.
-   Seleccionar objetivo.
-   Limpiar grafo.

------------------------------------------------------------------------

## 6.2 Ejecución de algoritmos

El usuario debe poder seleccionar:

``` text
[ Búsqueda en amplitud ]
[ Búsqueda en profundidad ]
[ Búsqueda bidireccional ]
[ Profundidad iterativa ]
[ Coste uniforme ]
[ Ascenso a la colina ]
[ Primero el mejor ]
[ A* ]
```

Después:

``` text
Inicio: A
Objetivo: G

              [ EJECUTAR ]
```

------------------------------------------------------------------------

# 7. Visualización de estados

Durante la ejecución se deben utilizar diferentes estados visuales.

### Nodo sin explorar

``` text
○
```

### Nodo en frontera

``` text
◐
```

### Nodo explorado

``` text
●
```

### Nodo actual

``` text
◎
```

### Nodo perteneciente al camino final

``` text
★
```

La interfaz debe incluir una leyenda para explicar cada estado.

------------------------------------------------------------------------

# 8. Ejecución paso a paso

El usuario debe poder elegir entre:

``` text
Modo automático
Modo paso a paso
```

En modo paso a paso:

``` text
Paso 1
↓
Seleccionar nodo
↓
Actualizar frontera
↓
Marcar nodo visitado
↓
Generar sucesores
↓
Paso 2
```

Controles:

``` text
[|<] Reiniciar
[<] Paso anterior
[>] Siguiente paso
[▶] Ejecutar
[⏸] Pausar
[■] Detener
```

Si implementar paso anterior resulta demasiado complejo, se puede
limitar inicialmente a:

``` text
[ Reiniciar ] [ Siguiente ] [ Ejecutar ] [ Pausar ]
```

------------------------------------------------------------------------

# 9. Método 1: Búsqueda en amplitud (BFS)

## Concepto

BFS explora primero todos los nodos de una profundidad antes de pasar a
la siguiente.

Utiliza una cola:

``` text
FIFO
First In - First Out
```

## Función conceptual

``` text
cola ← [inicio]
visitados ← {}

mientras cola no esté vacía:

    actual ← sacar primero de cola

    si actual == objetivo:
        terminar

    marcar actual como visitado

    para cada vecino:
        si no ha sido visitado:
            agregar vecino a cola
```

## Prioridad

``` text
Menor profundidad
```

## Propiedades

-   No utiliza heurística.
-   Con costos uniformes encuentra un camino de menor número de aristas.
-   Puede consumir bastante memoria.

------------------------------------------------------------------------

# 10. Método 2: Búsqueda en profundidad (DFS)

## Concepto

DFS intenta profundizar todo lo posible antes de regresar.

Utiliza una pila:

``` text
LIFO
Last In - First Out
```

## Función conceptual

``` text
pila ← [inicio]
visitados ← {}

mientras pila no esté vacía:

    actual ← sacar de la pila

    si actual == objetivo:
        terminar

    si actual no ha sido visitado:

        marcar actual

        para cada vecino:
            agregar vecino a pila
```

## Prioridad

``` text
Mayor profundidad
```

## Propiedades

-   No utiliza heurística.
-   Consume menos memoria que BFS en muchos casos.
-   No garantiza encontrar el camino más corto.
-   Puede profundizar demasiado si no se controla.

------------------------------------------------------------------------

# 11. Método 3: Búsqueda bidireccional

## Concepto

Realiza dos búsquedas:

``` text
Inicio → → → ← ← ← Objetivo
```

Una búsqueda parte del inicio y otra del objetivo.

Cuando ambas fronteras se encuentran, se reconstruye el camino.

## Estructura

``` text
fronteraInicio
fronteraObjetivo

visitadosInicio
visitadosObjetivo
```

## Condición de encuentro

``` text
intersección(visitadosInicio, visitadosObjetivo) != vacío
```

## Consideraciones

-   Se necesita conocer el estado objetivo.
-   Es especialmente útil cuando el espacio de búsqueda es grande y la
    solución se encuentra a una profundidad moderada.
-   Para una implementación general, el grafo debe permitir recorrer las
    conexiones desde ambos extremos. En grafos dirigidos puede ser
    necesario construir o disponer del grafo inverso para la búsqueda
    desde el objetivo.

------------------------------------------------------------------------

# 12. Método 4: Búsqueda en profundidad iterativa (IDDFS)

## Concepto

Combina la búsqueda en profundidad con límites crecientes.

``` text
Límite = 0
Límite = 1
Límite = 2
Límite = 3
...
```

## Algoritmo

``` text
para límite desde 0 hasta infinito:

    resultado ← DFS_Limitado(inicio, objetivo, límite)

    si resultado encuentra objetivo:
        devolver resultado
```

## Ventajas

-   Utiliza poca memoria.
-   Puede encontrar soluciones a menor profundidad cuando los costos son
    uniformes.
-   Repite parte del trabajo en cada iteración.

------------------------------------------------------------------------

# 13. Método 5: Búsqueda de coste uniforme (UCS)

## Concepto

UCS selecciona el nodo cuyo costo acumulado desde el inicio sea menor.

Utiliza una cola de prioridad.

La función es:

``` text
f(n) = g(n)
```

Donde:

``` text
g(n) = costo acumulado desde el inicio
```

Ejemplo:

``` text
A --2--> B --3--> D

g(D) = 2 + 3 = 5
```

Si existe:

``` text
A --1--> C --10--> D
```

entonces:

``` text
g(D) = 1 + 10 = 11
```

UCS preferirá el camino con costo 5.

## Algoritmo conceptual

``` text
frontera ← cola de prioridad
insertar inicio con prioridad 0

mientras frontera no esté vacía:

    actual ← extraer nodo con menor g(n)

    si actual == objetivo:
        terminar

    para cada vecino:

        nuevoCosto = costoActual + costoArista

        si vecino no tiene costo
        o nuevoCosto < costoAnterior:

            actualizar costo
            insertar vecino
```

------------------------------------------------------------------------

# 14. Método 6: Ascenso a la colina (Hill Climbing)

## Concepto

Es un método local.

Desde el nodo actual observa sus vecinos y selecciona el que parece más
prometedor según la heurística.

Si:

``` text
h(n) = estimación de distancia al objetivo
```

y menor es mejor:

``` text
actual = A

B → h=8
C → h=3
D → h=6
```

Selecciona:

``` text
C
```

porque:

``` text
h(C) = 3
```

## Algoritmo conceptual

``` text
actual ← inicio

mientras verdadero:

    si actual == objetivo:
        terminar

    mejor ← mejor vecino(actual)

    si mejor no mejora la situación:
        terminar con "óptimo local"

    actual ← mejor
```

## Problemas

Puede quedar atrapado en:

-   Máximos o mínimos locales.
-   Mesetas.
-   Situaciones donde ningún vecino mejora la heurística.

## Información que debe mostrar la aplicación

``` text
Nodo actual
Heurística actual
Vecinos evaluados
Mejor vecino seleccionado
Motivo de detención
```

------------------------------------------------------------------------

# 15. Método 7: Primero el mejor (Best-First / Greedy)

## Concepto

Mantiene una frontera de candidatos y selecciona el nodo con menor
heurística.

La función es:

``` text
f(n) = h(n)
```

Donde:

``` text
h(n) = estimación desde n hasta el objetivo
```

## Algoritmo conceptual

``` text
frontera ← cola de prioridad

insertar inicio con prioridad h(inicio)

mientras frontera no esté vacía:

    actual ← extraer menor h(n)

    si actual == objetivo:
        terminar

    marcar actual

    para cada vecino:

        si no ha sido visitado:
            insertar vecino con prioridad h(vecino)
```

## Diferencia con UCS

UCS:

``` text
f(n) = g(n)
```

Best-First:

``` text
f(n) = h(n)
```

------------------------------------------------------------------------

# 16. Método 8: A\*

## Concepto

A\* combina:

``` text
Costo recorrido
+
Costo estimado restante
```

Utiliza:

``` text
f(n) = g(n) + h(n)
```

Donde:

``` text
g(n) = costo desde el inicio
h(n) = estimación hasta el objetivo
f(n) = costo total estimado
```

## Ejemplo

Si:

``` text
g(n) = 5
h(n) = 4
```

entonces:

``` text
f(n) = 5 + 4 = 9
```

## Algoritmo conceptual

``` text
frontera ← cola de prioridad

g(inicio) = 0

insertar inicio con prioridad:
    f(inicio) = g(inicio) + h(inicio)

mientras frontera no esté vacía:

    actual ← extraer menor f(n)

    si actual == objetivo:
        reconstruir camino
        terminar

    para cada vecino:

        nuevoG = g(actual) + costo(actual, vecino)

        si nuevoG < g(vecino):

            g(vecino) = nuevoG

            f(vecino) = g(vecino) + h(vecino)

            padre(vecino) = actual

            insertar o actualizar vecino
```

## Importante

Para que A\* garantice optimalidad en las condiciones habituales, la
heurística debe cumplir propiedades adecuadas, especialmente
admisibilidad; para evitar complicaciones en la aplicación educativa se
debe permitir seleccionar o validar heurísticas apropiadas.

------------------------------------------------------------------------

# 17. Comparación de algoritmos

  Algoritmo       Prioridad                 Heurística Estructura
  --------------- ----------------------- ------------ ---------------------
  BFS             Profundidad                       No Cola
  DFS             Profundidad                       No Pila
  Bidireccional   Dos frentes                       No Dos colas/fronteras
  IDDFS           Límite de profundidad             No Pila/recursión
  UCS             Menor `g(n)`                      No Cola de prioridad
  Hill Climbing   Mejor vecino                      Sí Selección local
  Best-First      Menor `h(n)`                      Sí Cola de prioridad
  A\*             Menor `g(n)+h(n)`                 Sí Cola de prioridad

------------------------------------------------------------------------

# 18. Interfaz gráfica propuesta

## Pantalla principal

``` text
┌──────────────────────────────────────────────────────────────┐
│                 MÉTODOS DE BÚSQUEDA EN IA                    │
├──────────────────────────────────────────────────────────────┤
│                                                              │
│  Algoritmo: [ A* ▼ ]                                        │
│                                                              │
│  Inicio:   [ A ]       Objetivo: [ G ]                      │
│                                                              │
│  ┌──────────────────────────────────────────────────────┐    │
│  │                                                      │    │
│  │                     GRAFO                            │    │
│  │                                                      │    │
│  │        A ─── 2 ─── B                                │    │
│  │       /             \                                │    │
│  │      4               3                               │    │
│  │     /                 \                              │    │
│  │    C ───── 2 ───────── D ─── 1 ─── G                │    │
│  │                                                      │    │
│  └──────────────────────────────────────────────────────┘    │
│                                                              │
│  [Ejecutar] [Paso siguiente] [Pausar] [Reiniciar]            │
│                                                              │
├──────────────────────────────────────────────────────────────┤
│ Estado: Explorando D                                        │
│ Frontera: B, C, G                                           │
│ Visitados: A, B, C, D                                      │
│                                                              │
│ g(n): 6      h(n): 1      f(n): 7                           │
│                                                              │
├──────────────────────────────────────────────────────────────┤
│ Resultado                                                   │
│ Camino: A → C → D → G                                      │
│ Costo: 7                                                    │
│ Nodos explorados: 4                                        │
│                                                              │
└──────────────────────────────────────────────────────────────┘
```

------------------------------------------------------------------------

# 19. Módulos del sistema

Se recomienda dividir el software en módulos.

``` text
Aplicación
│
├── Modelo
│   ├── Nodo
│   ├── Arista
│   ├── Grafo
│   └── ResultadoBusqueda
│
├── Algoritmos
│   ├── BFS
│   ├── DFS
│   ├── Bidireccional
│   ├── IDDFS
│   ├── UCS
│   ├── HillClimbing
│   ├── BestFirst
│   └── AEstrella
│
├── Controlador
│   ├── GestorGrafo
│   ├── EjecutorBusqueda
│   └── GestorSimulacion
│
└── Vista
    ├── PanelGrafo
    ├── PanelConfiguracion
    ├── PanelEjecucion
    └── PanelResultados
```

------------------------------------------------------------------------

# 20. Arquitectura recomendada

Se recomienda utilizar una arquitectura MVC:

``` text
                 ┌──────────────┐
                 │    VISTA     │
                 │              │
                 │ Interfaz GUI │
                 └──────┬───────┘
                        │
                        ↓
                 ┌──────────────┐
                 │ CONTROLADOR  │
                 │              │
                 │ Coordina     │
                 └──────┬───────┘
                        │
                        ↓
                 ┌──────────────┐
                 │    MODELO    │
                 │              │
                 │ Grafo        │
                 │ Algoritmos   │
                 │ Resultados   │
                 └──────────────┘
```

------------------------------------------------------------------------

# 21. Interfaz de los algoritmos

Para evitar repetir código, todos los algoritmos deberían implementar
una interfaz común.

Ejemplo conceptual:

``` text
AlgoritmoBusqueda

+ ejecutar(grafo, inicio, objetivo)
+ siguientePaso()
+ obtenerResultado()
+ reiniciar()
```

Cada algoritmo implementará su propia estrategia.

``` text
BusquedaAmplitud
BusquedaProfundidad
BusquedaBidireccional
ProfundidadIterativa
CosteUniforme
AscensoColina
PrimeroMejor
AEstrella
```

------------------------------------------------------------------------

# 22. Resultado de una búsqueda

Cada ejecución debería producir un objeto:

``` text
ResultadoBusqueda

- encontrado
- camino
- costoTotal
- nodosExplorados
- nodosVisitados
- pasos
- tiempoEjecucion
- algoritmo
- mensaje
```

Ejemplo:

``` text
Algoritmo: A*
Encontrado: Sí

Camino:
A → C → D → G

Costo:
7

Nodos explorados:
4

Tiempo:
2.31 ms
```

------------------------------------------------------------------------

# 23. Sistema de pasos

Para la visualización educativa, los algoritmos no deberían limitarse a
devolver el resultado final.

Cada algoritmo debe generar eventos o estados:

``` text
Paso 1:
Nodo actual = A

Paso 2:
Se agregan B y C a la frontera

Paso 3:
Se selecciona C

Paso 4:
Se agregan D y F

Paso 5:
Se selecciona D

Paso 6:
Se encuentra G
```

Esto permitirá animar el algoritmo.

------------------------------------------------------------------------

# 24. Heurísticas

El software debe permitir seleccionar o introducir heurísticas.

Ejemplos:

### Distancia Manhattan

Para coordenadas:

``` text
h(n) = |x1-x2| + |y1-y2|
```

### Distancia Euclidiana

``` text
h(n) = √((x1-x2)² + (y1-y2)²)
```

### Heurística manual

El usuario puede asignar:

``` text
A → 10
B → 8
C → 5
D → 3
G → 0
```

El objetivo normalmente debe tener:

``` text
h(objetivo) = 0
```

------------------------------------------------------------------------

# 25. Validaciones

El sistema debe validar:

-   Que exista un grafo.
-   Que exista un nodo inicial.
-   Que exista un nodo objetivo.
-   Que inicio y objetivo sean válidos.
-   Que las aristas tengan costos válidos.
-   Que los costos no sean negativos cuando el algoritmo utilizado no
    los admite.
-   Que los nodos tengan identificadores únicos.
-   Que los valores heurísticos sean numéricos.
-   Que no existan conexiones inválidas.
-   Que el algoritmo seleccionado sea compatible con la configuración.

------------------------------------------------------------------------

# 26. Casos especiales

## No existe camino

Mostrar:

``` text
No se encontró un camino entre el nodo inicial y el objetivo.
```

## Inicio igual al objetivo

Mostrar:

``` text
El nodo inicial ya es el objetivo.

Costo: 0
Nodos explorados: 1
```

## Grafo vacío

Mostrar:

``` text
El grafo está vacío.
Agregue nodos antes de ejecutar una búsqueda.
```

------------------------------------------------------------------------

# 27. Comparador de algoritmos

El software debería incluir una opción:

``` text
Comparar algoritmos
```

El usuario selecciona varios algoritmos y el sistema los ejecuta sobre
el mismo grafo.

Ejemplo:

  Algoritmo      Encontrado   Costo   Explorados   Tiempo
  ------------ ------------ ------- ------------ --------
  BFS                    Sí       7            8   1.2 ms
  DFS                    Sí      12            6   0.8 ms
  UCS                    Sí       7           10   1.5 ms
  Best-First             Sí       9            5   0.9 ms
  A\*                    Sí       7            6   1.1 ms

Los valores anteriores son solamente un ejemplo; el software debe
calcular los valores reales.

------------------------------------------------------------------------

# 28. Exportación

Opcionalmente se puede permitir:

-   Guardar grafo.
-   Cargar grafo.
-   Exportar resultados.
-   Exportar historial de pasos.
-   Exportar comparación.

Formatos sugeridos:

``` text
JSON
CSV
TXT
```

Ejemplo de grafo JSON:

``` json
{
  "nodos": [
    {
      "id": "A",
      "heuristica": 7
    },
    {
      "id": "G",
      "heuristica": 0
    }
  ],
  "aristas": [
    {
      "origen": "A",
      "destino": "G",
      "costo": 5
    }
  ],
  "inicio": "A",
  "objetivo": "G"
}
```

------------------------------------------------------------------------

# 29. Tecnologías sugeridas

La tecnología puede seleccionarse según el objetivo académico.

## Opción Python

``` text
Python
PyQt6
MVC
```

Ventajas:

-   Desarrollo rápido.
-   Buena integración con interfaces gráficas.
-   Fácil implementación de estructuras de datos.
-   Adecuado para visualización educativa.

## Opción Java

``` text
Java
JavaFX
MVC
```

Ventajas:

-   Buen manejo de estructuras de datos.
-   Adecuado para proyectos académicos.
-   Buena separación entre modelo, vista y controlador.

------------------------------------------------------------------------

# 30. Estructura de proyecto sugerida

Para Python:

``` text
busqueda_ia/
│
├── main.py
│
├── modelo/
│   ├── nodo.py
│   ├── arista.py
│   ├── grafo.py
│   └── resultado.py
│
├── algoritmos/
│   ├── algoritmo_base.py
│   ├── amplitud.py
│   ├── profundidad.py
│   ├── bidireccional.py
│   ├── profundidad_iterativa.py
│   ├── coste_uniforme.py
│   ├── ascenso_colina.py
│   ├── primero_mejor.py
│   └── a_estrella.py
│
├── controlador/
│   ├── controlador_grafo.py
│   └── controlador_busqueda.py
│
├── vista/
│   ├── ventana_principal.py
│   ├── panel_grafo.py
│   ├── panel_algoritmos.py
│   └── panel_resultados.py
│
├── datos/
│   └── grafos/
│
└── README.md
```

------------------------------------------------------------------------

# 31. Requisitos funcionales

### RF01

El sistema debe permitir crear un grafo.

### RF02

El sistema debe permitir agregar y eliminar nodos.

### RF03

El sistema debe permitir crear y eliminar aristas.

### RF04

El sistema debe permitir asignar costos a las aristas.

### RF05

El sistema debe permitir asignar valores heurísticos.

### RF06

El sistema debe permitir seleccionar nodo inicial y objetivo.

### RF07

El sistema debe implementar BFS.

### RF08

El sistema debe implementar DFS.

### RF09

El sistema debe implementar búsqueda bidireccional.

### RF10

El sistema debe implementar IDDFS.

### RF11

El sistema debe implementar UCS.

### RF12

El sistema debe implementar Hill Climbing.

### RF13

El sistema debe implementar Best-First.

### RF14

El sistema debe implementar A\*.

### RF15

El sistema debe visualizar el proceso de búsqueda.

### RF16

El sistema debe mostrar el camino final.

### RF17

El sistema debe mostrar estadísticas.

### RF18

El sistema debe permitir reiniciar la búsqueda.

### RF19

El sistema debe validar los datos ingresados.

### RF20

El sistema debería permitir comparar algoritmos.

------------------------------------------------------------------------

# 32. Requisitos no funcionales

### Usabilidad

La interfaz debe ser sencilla y comprensible para estudiantes.

### Rendimiento

La aplicación debe ejecutar grafos pequeños y medianos sin retrasos
perceptibles.

### Mantenibilidad

Cada algoritmo debe estar separado del resto del sistema.

### Extensibilidad

Debe ser posible agregar nuevos algoritmos posteriormente.

### Portabilidad

El sistema debe poder ejecutarse en los sistemas operativos definidos
para el proyecto.

------------------------------------------------------------------------

# 33. Flujo principal de usuario

``` text
INICIO
  ↓
Crear/Cargar grafo
  ↓
Agregar nodos
  ↓
Agregar aristas
  ↓
Asignar costos
  ↓
Asignar heurísticas
  ↓
Seleccionar inicio
  ↓
Seleccionar objetivo
  ↓
Seleccionar algoritmo
  ↓
Configurar ejecución
  ↓
Ejecutar
  ↓
Visualizar pasos
  ↓
Encontrar objetivo
  ↓
Mostrar camino
  ↓
Mostrar estadísticas
  ↓
¿Comparar otro algoritmo?
  ├── Sí → Ejecutar nuevamente
  └── No → FIN
```

------------------------------------------------------------------------

# 34. Diseño educativo

La aplicación debe priorizar la comprensión del algoritmo.

Por ello, cada paso debería mostrar:

``` text
ALGORITMO ACTUAL
A*

NODO ACTUAL
C

FRONTERA
B, D, F

VISITADOS
A, C

VALORES
g(C) = 4
h(C) = 3
f(C) = 7

ACCIÓN
Se selecciona C porque tiene el menor f(n).
```

Esto permite que el estudiante comprenda **por qué** el algoritmo
seleccionó cada nodo.

------------------------------------------------------------------------

# 35. Diferencias que el software debe hacer visibles

El sistema debe permitir observar claramente:

``` text
BFS
↓
Busca por niveles.

DFS
↓
Busca profundizando.

UCS
↓
Busca por menor costo acumulado.

Hill Climbing
↓
Busca localmente el vecino que parece mejor.

Best-First
↓
Busca el candidato con menor heurística.

A*
↓
Combina costo acumulado y heurística.
```

------------------------------------------------------------------------

# 36. Pruebas mínimas

Se deben crear grafos de prueba para cada algoritmo.

## Prueba 1: Grafo simple

``` text
A → B → C
```

Inicio:

``` text
A
```

Objetivo:

``` text
C
```

## Prueba 2: Diferentes profundidades

``` text
       A
      / \
     B   C
    /     \
   D       E
    \     /
      F
```

## Prueba 3: Diferentes costos

``` text
A --2--> B --2--> G

A --1--> C --8--> G
```

Sirve para demostrar que UCS y A\* consideran costos.

## Prueba 4: Heurísticas

``` text
        A
       / \
      B   C
      |   |
     D    E
      \  /
       G
```

Asignar diferentes valores de `h(n)`.

## Prueba 5: Sin solución

Crear dos componentes desconectados:

``` text
A → B → C

X → Y → Z
```

Inicio:

``` text
A
```

Objetivo:

``` text
Z
```

Resultado esperado:

``` text
No existe camino.
```

------------------------------------------------------------------------

# 37. Resultado esperado del proyecto

Al finalizar, el usuario debe poder abrir el software y realizar el
siguiente proceso:

``` text
1. Crear un grafo.
2. Definir inicio y objetivo.
3. Asignar costos.
4. Asignar heurísticas.
5. Seleccionar un algoritmo.
6. Ejecutarlo.
7. Observar la exploración.
8. Ver el camino encontrado.
9. Ver el costo.
10. Ver las estadísticas.
11. Compararlo con otros algoritmos.
```

El objetivo principal no es únicamente encontrar una solución, sino
**mostrar visualmente cómo cada algoritmo toma sus decisiones y por qué
diferentes estrategias pueden explorar caminos distintos**.

------------------------------------------------------------------------

# 38. Orden recomendado de implementación

Para reducir la complejidad, implementar en este orden:

``` text
FASE 1
Modelo del grafo
    ↓
Nodo
Arista
Grafo

FASE 2
Visualización
    ↓
Crear nodos
Crear aristas
Mover nodos

FASE 3
Algoritmos básicos
    ↓
BFS
DFS

FASE 4
Búsquedas adicionales
    ↓
Bidireccional
IDDFS
UCS

FASE 5
Búsqueda heurística
    ↓
Hill Climbing
Best-First
A*

FASE 6
Animación
    ↓
Paso a paso
Frontera
Visitados
Camino final

FASE 7
Comparación
    ↓
Costo
Nodos explorados
Tiempo

FASE 8
Persistencia y exportación
```

------------------------------------------------------------------------

# 39. Criterio de finalización

El proyecto se considera funcional cuando:

-   Los ocho algoritmos están implementados.
-   Los algoritmos producen resultados correctos.
-   El grafo puede visualizarse gráficamente.
-   El usuario puede definir inicio y objetivo.
-   Los costos y heurísticas pueden configurarse.
-   La ejecución puede observarse paso a paso.
-   Se muestra el camino encontrado.
-   Se muestran estadísticas.
-   Se pueden ejecutar diferentes algoritmos sobre el mismo problema.
-   Se manejan correctamente los casos sin solución.
-   La arquitectura permite agregar nuevos algoritmos.

------------------------------------------------------------------------

# 40. Resumen conceptual

La idea central del software puede resumirse así:

``` text
                 MÉTODOS DE BÚSQUEDA
                         │
          ┌──────────────┴──────────────┐
          │                             │
   NO INFORMADOS                    INFORMADOS
          │                             │
   ┌──────┼──────────┐            ┌─────┼─────────────┐
   │      │          │            │     │             │
  BFS    DFS   Bidireccional   Hill   Best-First      A*
   │      │          │         Climbing
   │      │       IDDFS
   │      │
   └──────┴──── UCS
```

Funciones clave:

``` text
BFS                  → profundidad
DFS                  → profundidad
Bidireccional        → dos direcciones
IDDFS                → profundidad creciente
UCS                  → g(n)
Hill Climbing        → mejor vecino
Best-First           → h(n)
A*                   → g(n) + h(n)
```

El software debe convertir estos conceptos teóricos en una **simulación
visual e interactiva**, permitiendo al estudiante observar la diferencia
entre los métodos sobre exactamente el mismo grafo.
