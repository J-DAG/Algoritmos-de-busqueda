package Pruebas;

import Controlador.ControladorBusqueda;
import Controlador.ControladorGrafo;
import Modelo.Busqueda.AscensoColina;
import Modelo.Busqueda.BusquedaAEstrella;
import Modelo.Busqueda.BusquedaAmplitud;
import Modelo.Busqueda.BusquedaBidireccional;
import Modelo.Busqueda.BusquedaCosteUniforme;
import Modelo.Busqueda.BusquedaPrimeroMejor;
import Modelo.Busqueda.BusquedaProfundidad;
import Modelo.Busqueda.BusquedaProfundidadIterativa;
import Modelo.Grafo;
import Modelo.Nodo;
import Modelo.PasoBusqueda;
import Modelo.ResultadoBusqueda;

import java.util.Arrays;
import java.util.Map;

/** Pruebas ejecutables sin bibliotecas externas: Run PruebasBusqueda.main(). */
public class PruebasBusqueda {
    private static int comprobaciones;

    public static void main(String[] args) {
        probarEjemploBfsDeClase();
        probarEjemploPredeterminado();
        probarConexionSinPeso();
        probarDiferenciasEntreCriterios();
        probarBidireccionalDirigida();
        probarCasosEspeciales();
        probarComparacionControlador();
        System.out.println("Todas las pruebas pasaron: " + comprobaciones + " comprobaciones.");
    }

    private static void probarEjemploBfsDeClase() {
        Grafo grafo = new Grafo();
        String[] ids = {"H", "A", "B", "C", "D", "E", "F", "G", "J", "K", "L"};
        for (String id : ids) comprobar(grafo.agregarNodo(new Nodo(id)), "crear nodo " + id);
        String[][] aristas = {{"H", "A"}, {"H", "B"}, {"H", "C"}, {"A", "D"}, {"A", "E"},
                {"B", "F"}, {"C", "G"}, {"C", "J"}, {"D", "K"}, {"D", "L"}};
        for (String[] arista : aristas) {
            comprobar(grafo.agregarConexion(arista[0], arista[1], 1), "crear conexión");
        }

        ResultadoBusqueda resultado = new BusquedaAmplitud().buscar(
                grafo, grafo.buscarNodo("H"), grafo.buscarNodo("G"));
        String[] extracciones = {"H", "A", "B", "C", "D", "E", "F", "G"};
        String[] colas = {"[A, B, C]", "[B, C, D, E]", "[C, D, E, F]", "[D, E, F, G, J]",
                "[E, F, G, J, K, L]", "[F, G, J, K, L]", "[G, J, K, L]", "[J, K, L]"};
        comprobar(resultado.isEncontrado(), "BFS encuentra G");
        comprobar(resultado.getCamino().toString().equals("[H, C, G]"), "BFS devuelve el camino");
        comprobar(resultado.getPasos().size() == extracciones.length, "BFS registra las extracciones");
        for (int i = 0; i < extracciones.length; i++) {
            PasoBusqueda paso = resultado.getPasos().get(i);
            comprobar(paso.getNodoExtraido().getId().equals(extracciones[i]), "orden de extracción " + i);
            comprobar(paso.getFrontera().toString().equals(colas[i]), "estado de cola " + i);
        }
    }

    private static void probarEjemploPredeterminado() {
        ControladorGrafo controlador = new ControladorGrafo();
        controlador.cargarEjemploClase();
        comprobar(controlador.getGrafo().obtenerNodos().size() == 11, "ejemplo predeterminado tiene 11 nodos");
        comprobar(!controlador.getGrafo().isDirigido(), "ejemplo predeterminado no es dirigido");
        comprobar(controlador.getInicio().getId().equals("H"), "ejemplo predeterminado inicia en H");
        comprobar(controlador.getObjetivo().getId().equals("G"), "ejemplo predeterminado termina en G");
        comprobar(new BusquedaAmplitud().buscar(controlador.getGrafo(), controlador.getInicio(),
                controlador.getObjetivo()).getCamino().toString().equals("[H, C, G]"),
                "ejemplo predeterminado funciona con BFS");
        Map<String, ResultadoBusqueda> comparacion = new ControladorBusqueda().comparar(
                controlador.getGrafo(), controlador.getInicio(), controlador.getObjetivo());
        comprobar(comparacion.values().stream().allMatch(ResultadoBusqueda::isEncontrado),
                "los ocho métodos encuentran el objetivo en el ejemplo predeterminado");
    }

    private static void probarConexionSinPeso() {
        Grafo grafo = new Grafo(true);
        Nodo origen = new Nodo("A");
        Nodo destino = new Nodo("B");
        grafo.agregarNodo(origen);
        grafo.agregarNodo(destino);
        comprobar(grafo.agregarConexion("A", "B"), "crear arista sin especificar peso");
        comprobar(origen.getConexiones().get(0).getPeso() == 1.0, "arista sin peso usa unidad");
    }

    private static void probarDiferenciasEntreCriterios() {
        Grafo grafo = crearGrafoConCostosYHeuristica();
        Nodo inicio = grafo.buscarNodo("S");
        Nodo objetivo = grafo.buscarNodo("G");

        ResultadoBusqueda bfs = new BusquedaAmplitud().buscar(grafo, inicio, objetivo);
        ResultadoBusqueda dfs = new BusquedaProfundidad().buscar(grafo, inicio, objetivo);
        ResultadoBusqueda iddfs = new BusquedaProfundidadIterativa().buscar(grafo, inicio, objetivo);
        ResultadoBusqueda ucs = new BusquedaCosteUniforme().buscar(grafo, inicio, objetivo);
        ResultadoBusqueda hill = new AscensoColina().buscar(grafo, inicio, objetivo);
        ResultadoBusqueda greedy = new BusquedaPrimeroMejor().buscar(grafo, inicio, objetivo);
        ResultadoBusqueda aEstrella = new BusquedaAEstrella().buscar(grafo, inicio, objetivo);

        comprobar(bfs.getCamino().toString().equals("[S, A, G]"), "BFS prioriza menos aristas");
        comprobar(dfs.isEncontrado(), "DFS encuentra objetivo");
        comprobar(iddfs.isEncontrado(), "IDDFS encuentra objetivo");
        comprobar(iddfs.getPasos().stream().anyMatch(p -> p.getLimiteProfundidad() == 0)
                && iddfs.getPasos().stream().anyMatch(p -> p.getLimiteProfundidad() == 2),
                "IDDFS registra límites crecientes");
        comprobar(ucs.getCamino().toString().equals("[S, B, A, G]") && ucs.getCostoTotal() == 3,
                "UCS obtiene costo mínimo");
        comprobar(aEstrella.getCamino().toString().equals("[S, B, A, G]") && aEstrella.getCostoTotal() == 3,
                "A* obtiene costo mínimo con heurística admisible");
        comprobar(hill.getCamino().toString().equals("[S, A, G]"), "Hill Climbing sigue el mejor vecino local");
        comprobar(greedy.getCamino().toString().equals("[S, A, G]"), "Greedy prioriza h(n)");
    }

    private static void probarBidireccionalDirigida() {
        Grafo grafo = new Grafo(true);
        for (String id : new String[]{"S", "A", "B", "C", "G", "X"}) {
            comprobar(grafo.agregarNodo(new Nodo(id)), "crear nodo dirigido " + id);
        }
        conectar(grafo, "S", "A", 2);
        conectar(grafo, "A", "G", 3);
        conectar(grafo, "S", "B", 1);
        conectar(grafo, "B", "C", 1);
        conectar(grafo, "C", "G", 1);

        BusquedaBidireccional busqueda = new BusquedaBidireccional();
        ResultadoBusqueda resultado = busqueda.buscar(grafo, grafo.buscarNodo("S"), grafo.buscarNodo("G"));
        comprobar(resultado.getCamino().toString().equals("[S, A, G]"), "bidireccional ruta mínima en aristas");
        comprobar(resultado.getCostoTotal() == 5, "bidireccional suma pesos de la ruta");
        comprobar(resultado.getPasos().stream().anyMatch(p ->
                p.getLadoBusqueda() == PasoBusqueda.LadoBusqueda.OBJETIVO), "registra búsqueda desde objetivo");
        comprobar(!busqueda.buscar(grafo, grafo.buscarNodo("G"), grafo.buscarNodo("S")).isEncontrado(),
                "respeta dirección inversa");
        comprobar(!busqueda.buscar(grafo, grafo.buscarNodo("S"), grafo.buscarNodo("X")).isEncontrado(),
                "informa que no hay ruta");
    }

    private static void probarCasosEspeciales() {
        Grafo grafo = new Grafo(true);
        Nodo nodo = new Nodo("N");
        grafo.agregarNodo(nodo);
        ResultadoBusqueda[] resultados = {
                new BusquedaAmplitud().buscar(grafo, nodo, nodo),
                new BusquedaProfundidad().buscar(grafo, nodo, nodo),
                new BusquedaBidireccional().buscar(grafo, nodo, nodo),
                new BusquedaProfundidadIterativa().buscar(grafo, nodo, nodo),
                new BusquedaCosteUniforme().buscar(grafo, nodo, nodo),
                new AscensoColina().buscar(grafo, nodo, nodo),
                new BusquedaPrimeroMejor().buscar(grafo, nodo, nodo),
                new BusquedaAEstrella().buscar(grafo, nodo, nodo)
        };
        for (ResultadoBusqueda resultado : resultados) {
            comprobar(resultado.isEncontrado(), "inicio igual a objetivo");
            comprobar(resultado.getCamino().equals(Arrays.asList(nodo)), "camino de un nodo");
            comprobar(resultado.getCostoTotal() == 0.0, "costo cero");
        }
    }

    private static void probarComparacionControlador() {
        Grafo grafo = crearGrafoConCostosYHeuristica();
        Map<String, ResultadoBusqueda> resultados = new ControladorBusqueda().comparar(
                grafo, grafo.buscarNodo("S"), grafo.buscarNodo("G"));
        comprobar(resultados.size() == 8, "comparador ejecuta los ocho algoritmos");
    }

    private static Grafo crearGrafoConCostosYHeuristica() {
        Grafo grafo = new Grafo(true);
        grafo.agregarNodo(new Nodo("S", 3));
        grafo.agregarNodo(new Nodo("A", 1));
        grafo.agregarNodo(new Nodo("B", 2));
        grafo.agregarNodo(new Nodo("G", 0));
        conectar(grafo, "S", "A", 5);
        conectar(grafo, "S", "B", 1);
        conectar(grafo, "A", "G", 1);
        conectar(grafo, "B", "A", 1);
        conectar(grafo, "B", "G", 10);
        return grafo;
    }

    private static void conectar(Grafo grafo, String origen, String destino, double peso) {
        comprobar(grafo.agregarConexion(origen, destino, peso), "crear arista " + origen + "-" + destino);
    }

    private static void comprobar(boolean condicion, String mensaje) {
        comprobaciones++;
        if (!condicion) throw new AssertionError("Falló: " + mensaje);
    }
}
