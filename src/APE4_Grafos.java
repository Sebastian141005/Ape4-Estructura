import java.util.*;

public class APE4_Grafos {

    // ═══════════════════════════════════════
    // Nodo
    // ═══════════════════════════════════════
    static class Nodo {
        String id;
        String nombre;

        public Nodo(String id, String nombre) {
            this.id = id;
            this.nombre = nombre;
        }
    }

    // ═══════════════════════════════════════
    // Arista
    // ═══════════════════════════════════════
    static class Arista {
        String destino;
        int peso;

        public Arista(String destino, int peso) {
            this.destino = destino;
            this.peso = peso;
        }
    }

    // ═══════════════════════════════════════
    // Grafo
    // ═══════════════════════════════════════
    static class Grafo {

        Map<String, Nodo> nodos = new HashMap<>();
        Map<String, List<Arista>> adyacencia = new HashMap<>();

        // TODO 1
        public void agregarNodo(String id, String nombre) {

            Nodo nodo = new Nodo(id, nombre);

            nodos.put(id, nodo);

            adyacencia.put(id, new ArrayList<>());
        }

        // TODO 2
        public void agregarArista(String origen, String destino, int peso) {

            // origen → destino
            adyacencia.get(origen)
                    .add(new Arista(destino, peso));

            // destino → origen
            adyacencia.get(destino)
                    .add(new Arista(origen, peso));
        }

        // TODO 3
        public List<String> bfs(String inicio, String fin) {

            Queue<List<String>> cola = new LinkedList<>();

            Set<String> visitados = new HashSet<>();

            List<String> caminoInicial = new ArrayList<>();

            caminoInicial.add(inicio);

            cola.add(caminoInicial);

            visitados.add(inicio);

            while (!cola.isEmpty()) {

                List<String> camino = cola.poll();

                String actual =
                        camino.get(camino.size() - 1);

                if (actual.equals(fin)) {
                    return camino;
                }

                for (Arista arista : adyacencia.get(actual)) {

                    if (!visitados.contains(arista.destino)) {

                        visitados.add(arista.destino);

                        List<String> nuevoCamino =
                                new ArrayList<>(camino);

                        nuevoCamino.add(arista.destino);

                        cola.add(nuevoCamino);
                    }
                }
            }

            return null;
        }

        // TODO 4
        public List<String> dijkstra(String inicio, String fin) {

            Map<String, Integer> distancias =
                    new HashMap<>();

            Map<String, String> anteriores =
                    new HashMap<>();

            PriorityQueue<String> cola =
                    new PriorityQueue<>(
                            Comparator.comparingInt(
                                    distancias::get
                            )
                    );

            // Inicializar distancias
            for (String nodo : nodos.keySet()) {
                distancias.put(nodo, Integer.MAX_VALUE);
            }

            distancias.put(inicio, 0);

            cola.add(inicio);

            while (!cola.isEmpty()) {

                String actual = cola.poll();

                for (Arista arista : adyacencia.get(actual)) {

                    int nuevaDistancia =
                            distancias.get(actual)
                                    + arista.peso;

                    if (nuevaDistancia <
                            distancias.get(arista.destino)) {

                        distancias.put(
                                arista.destino,
                                nuevaDistancia
                        );

                        anteriores.put(
                                arista.destino,
                                actual
                        );

                        cola.add(arista.destino);
                    }
                }
            }

            // Reconstruir camino
            List<String> camino = new ArrayList<>();

            String actual = fin;

            while (actual != null) {

                camino.add(0, actual);

                actual = anteriores.get(actual);
            }

            return camino;
        }

        // Mostrar resultado
        public void mostrarRuta(List<String> ruta) {

            if (ruta == null) {
                System.out.println("No existe ruta");
                return;
            }

            for (int i = 0; i < ruta.size(); i++) {

                String idNodo = ruta.get(i);

                Nodo nodo = nodos.get(idNodo);

                System.out.print(
                    nodo.nombre + " (" + nodo.id + ")"
                );

                if (i < ruta.size() - 1) {
                    System.out.print(" -> ");
                }
            }

            System.out.println();
        }
    }

    // ═══════════════════════════════════════
    // MAIN
    // ═══════════════════════════════════════
    public static void main(String[] args) {

        Grafo grafo = new Grafo();

        // NODOS
        grafo.agregarNodo("uta", "Universidad");
        grafo.agregarNodo("fisei", "FISEI");
        grafo.agregarNodo("idiomas", "Idiomas");
        grafo.agregarNodo("biblioteca", "Biblioteca");
        grafo.agregarNodo("estadio", "Estadio");
        grafo.agregarNodo("comedor", "Comedor");

        // ARISTAS
        grafo.agregarArista("uta", "fisei", 50);
        grafo.agregarArista("fisei", "idiomas", 40);
        grafo.agregarArista("idiomas", "biblioteca", 30);
        grafo.agregarArista("biblioteca", "estadio", 70);

        // ESTA RUTA TIENE MENOS PARADAS
        // PERO MÁS DISTANCIA
        grafo.agregarArista("uta", "comedor", 20);
        grafo.agregarArista("comedor", "estadio", 200);

        // PRUEBAS
        System.out.println("===== BFS =====");

        List<String> rutaBFS =
                grafo.bfs("uta", "estadio");

        grafo.mostrarRuta(rutaBFS);

        System.out.println("\n===== DIJKSTRA =====");

        List<String> rutaDijkstra =
                grafo.dijkstra("uta", "estadio");

        grafo.mostrarRuta(rutaDijkstra);
    }
}