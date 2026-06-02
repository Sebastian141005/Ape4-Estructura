# APE 4 — Grafos: Mapa del Campus UTA

## Estructura de Datos — Universidad Técnica de Ambato

**Alumno:** Miranda Baño Rolando Sebastián  
**Docente:** Ing. José Rubén Caiza Caizabuano  
**Nivel:** Tercero — Paralelo B  
**Ciclo:** Enero – Julio 2026

---

## Objetivo

Desarrollar habilidades en la programación con grafos mediante la implementación del caso de estudio del mapa del Campus Huachi de la UTA, comparando los algoritmos BFS y Dijkstra para la búsqueda de rutas entre ubicaciones.

---

## Descripción del problema

Se modela el Campus Huachi como un **grafo no dirigido y ponderado** usando lista de adyacencia. Cada nodo representa una ubicación del campus y cada arista representa una conexión con su distancia en metros.

### Nodos del grafo

| ID          | Nombre      |
|-------------|-------------|
| `uta`       | Universidad |
| `fisei`     | FISEI       |
| `idiomas`   | Idiomas     |
| `biblioteca`| Biblioteca  |
| `estadio`   | Estadio     |
| `comedor`   | Comedor     |

### Aristas (conexiones)

| Origen      | Destino     | Peso (m) |
|-------------|-------------|----------|
| uta         | fisei       | 50       |
| fisei       | idiomas     | 40       |
| idiomas     | biblioteca  | 30       |
| biblioteca  | estadio     | 70       |
| uta         | comedor     | 20       |
| comedor     | estadio     | 200      |

---

## Métodos implementados

### TODO 1 — `agregarNodo(String id, String nombre)`

Crea un objeto `Nodo` y lo registra en el `HashMap` de nodos. Simultáneamente inicializa una lista vacía en el mapa de adyacencia para ese nodo.

```java
public void agregarNodo(String id, String nombre) {
    Nodo nodo = new Nodo(id, nombre);
    nodos.put(id, nodo);
    adyacencia.put(id, new ArrayList<>());
}
```

---

### TODO 2 — `agregarArista(String origen, String destino, int peso)`

Conecta dos nodos con una arista ponderada. Como el grafo es **no dirigido**, la arista se añade en ambas direcciones.

```java
public void agregarArista(String origen, String destino, int peso) {
    adyacencia.get(origen).add(new Arista(destino, peso));
    adyacencia.get(destino).add(new Arista(origen, peso));
}
```

---

### TODO 3 — `bfs(String inicio, String fin)`

Búsqueda en anchura (Breadth-First Search). Usa una cola FIFO de caminos. El primer camino que alcanza el destino garantiza el **menor número de paradas**, sin importar los pesos.

**Lógica:**
1. Encolar un camino inicial con solo el nodo de inicio.
2. Marcar el nodo inicio como visitado.
3. Extraer el primer camino de la cola (`poll()`).
4. Si su último nodo es el destino, retornar ese camino.
5. Por cada vecino no visitado, crear una copia del camino, añadir el vecino, marcarlo y encolar.

```java
public List<String> bfs(String inicio, String fin) {
    Queue<List<String>> cola = new LinkedList<>();
    Set<String> visitados = new HashSet<>();
    List<String> caminoInicial = new ArrayList<>();
    caminoInicial.add(inicio);
    cola.add(caminoInicial);
    visitados.add(inicio);

    while (!cola.isEmpty()) {
        List<String> camino = cola.poll();
        String actual = camino.get(camino.size() - 1);
        if (actual.equals(fin)) return camino;

        for (Arista arista : adyacencia.get(actual)) {
            if (!visitados.contains(arista.destino)) {
                visitados.add(arista.destino);
                List<String> nuevoCamino = new ArrayList<>(camino);
                nuevoCamino.add(arista.destino);
                cola.add(nuevoCamino);
            }
        }
    }
    return null;
}
```

---

### TODO 4 — `dijkstra(String inicio, String fin)`

Algoritmo de Dijkstra. Usa una cola de prioridad ordenada por distancia acumulada para encontrar la ruta de **menor coste total**.

**Lógica:**
1. Inicializar todas las distancias en `Integer.MAX_VALUE`.
2. Distancia del nodo inicio = 0; encolar.
3. Extraer el nodo con menor distancia acumulada.
4. Para cada vecino: si `dist_actual + peso < dist_vecino`, actualizar distancia, guardar anterior y reencolar.
5. Reconstruir el camino desde el destino usando el mapa `anteriores`.

```java
public List<String> dijkstra(String inicio, String fin) {
    Map<String, Integer> distancias = new HashMap<>();
    Map<String, String>  anteriores = new HashMap<>();
    PriorityQueue<String> cola =
            new PriorityQueue<>(Comparator.comparingInt(distancias::get));

    for (String nodo : nodos.keySet()) distancias.put(nodo, Integer.MAX_VALUE);
    distancias.put(inicio, 0);
    cola.add(inicio);

    while (!cola.isEmpty()) {
        String actual = cola.poll();
        for (Arista arista : adyacencia.get(actual)) {
            int nuevaDistancia = distancias.get(actual) + arista.peso;
            if (nuevaDistancia < distancias.get(arista.destino)) {
                distancias.put(arista.destino, nuevaDistancia);
                anteriores.put(arista.destino, actual);
                cola.add(arista.destino);
            }
        }
    }

    List<String> camino = new ArrayList<>();
    String actual = fin;
    while (actual != null) { camino.add(0, actual); actual = anteriores.get(actual); }
    return camino;
}
```

---

## Compilación y ejecución

### Compilar

```bash
javac APE4_Grafos.java
```

### Ejecutar

```bash
java APE4_Grafos
```

---

## Resultados obtenidos

```
===== BFS =====
Universidad (uta) -> Comedor (comedor) -> Estadio (estadio)

===== DIJKSTRA =====
Universidad (uta) -> FISEI (fisei) -> Idiomas (idiomas) -> Biblioteca (biblioteca) -> Estadio (estadio)
```

### Comparación de algoritmos

| Algoritmo | Ruta | Paradas | Distancia |
|-----------|------|---------|-----------|
| BFS       | uta → comedor → estadio | 2 | 220 m |
| Dijkstra  | uta → fisei → idiomas → biblioteca → estadio | 4 | 190 m |

BFS encuentra la ruta con **menos paradas** (ignora pesos). Dijkstra encuentra la ruta de **menor distancia total** (considera pesos).

---

## Estructura del proyecto

```
APE4-Estructura/
│
├── src/
│   └── APE4_Grafos.java
│
├── capturas/
│   └── consola.png
│
└── README.md
```

---

## Conceptos clave

**Grafo no dirigido:** la conexión entre dos nodos funciona en ambas direcciones.

**Grafo ponderado:** cada arista tiene un peso (distancia en metros).

**Lista de adyacencia:** para cada nodo se almacena la lista de sus vecinos y el peso de cada conexión. Eficiente en memoria para grafos dispersos.

**BFS:** recorre el grafo nivel a nivel usando una cola FIFO. Garantiza la ruta con menor número de saltos; no considera pesos.

**Dijkstra:** usa una cola de prioridad para procesar siempre el nodo más cercano. Garantiza la ruta de menor coste total cuando todos los pesos son no negativos.
