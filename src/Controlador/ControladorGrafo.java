package Controlador;

import Modelo.Grafo;
import Modelo.Nodo;

/** Coordina las operaciones del grafo y conserva la selección de inicio y objetivo. */
public class ControladorGrafo {
    private Grafo grafo;
    private String idInicio;
    private String idObjetivo;

    public ControladorGrafo() {
        this(new Grafo(true));
    }

    public ControladorGrafo(Grafo grafo) {
        if (grafo == null) throw new IllegalArgumentException("El grafo no puede ser null.");
        this.grafo = grafo;
    }

    public Grafo getGrafo() { return grafo; }
    public Nodo getInicio() { return grafo.buscarNodo(idInicio); }
    public Nodo getObjetivo() { return grafo.buscarNodo(idObjetivo); }

    public boolean agregarNodo(String id, double heuristica) {
        return grafo.agregarNodo(new Nodo(id, heuristica));
    }

    public boolean eliminarNodo(String id) {
        if (!grafo.eliminarNodo(id)) return false;
        if (id.equalsIgnoreCase(idInicio)) idInicio = null;
        if (id.equalsIgnoreCase(idObjetivo)) idObjetivo = null;
        return true;
    }

    public boolean agregarConexion(String origen, String destino, double peso) {
        return grafo.agregarConexion(origen, destino, peso);
    }
    public boolean agregarConexion(String origen, String destino) {
        return grafo.agregarConexion(origen, destino);
    }
    public boolean eliminarConexion(String origen, String destino) {
        return grafo.eliminarConexion(origen, destino);
    }
    public boolean actualizarPesoConexion(String origen, String destino, double peso) {
        return grafo.actualizarPesoConexion(origen, destino, peso);
    }

    public boolean asignarHeuristica(String id, double heuristica) {
        Nodo nodo = grafo.buscarNodo(id);
        if (nodo == null) return false;
        nodo.setHeuristica(heuristica);
        return true;
    }

    public boolean establecerInicio(String id) {
        Nodo nodo = grafo.buscarNodo(id);
        if (nodo == null) return false;
        idInicio = nodo.getId();
        return true;
    }

    public boolean establecerObjetivo(String id) {
        Nodo nodo = grafo.buscarNodo(id);
        if (nodo == null) return false;
        idObjetivo = nodo.getId();
        return true;
    }

    /** Reemplaza el grafo y borra inicio y objetivo previamente seleccionados. */
    public void nuevoGrafo(boolean dirigido) {
        grafo = new Grafo(dirigido);
        idInicio = null;
        idObjetivo = null;
    }

    /** Carga el árbol de clase y deja H como inicio y G como objetivo. */
    public void cargarEjemploClase() {
        nuevoGrafo(false);
        String[] ids = {"H", "A", "B", "C", "D", "E", "F", "G", "J", "K", "L"};
        double[] heuristicas = {2, 3, 3, 1, 4, 4, 4, 0, 2, 5, 5};
        for (int i = 0; i < ids.length; i++) {
            agregarNodo(ids[i], heuristicas[i]);
        }

        String[][] aristas = {{"H", "A"}, {"H", "B"}, {"H", "C"}, {"A", "D"}, {"A", "E"},
                {"B", "F"}, {"C", "G"}, {"C", "J"}, {"D", "K"}, {"D", "L"}};
        for (String[] arista : aristas) {
            agregarConexion(arista[0], arista[1]);
        }
        establecerInicio("H");
        establecerObjetivo("G");
    }

    /** Carga el ejemplo ponderado de A* con H como inicio y F como objetivo. */
    public void cargarEjemploAEstrella() {
        nuevoGrafo(false);
        String[] ids = {"H", "A", "C", "D", "E", "G", "J", "K", "L", "B", "F"};
        double[] heuristicas = {20, 14, 16, 9, 5, 12, 8, 3, 6, 3, 0};
        for (int i = 0; i < ids.length; i++) {
            agregarNodo(ids[i], heuristicas[i]);
        }

        String[][] aristas = {{"H", "A", "14"}, {"H", "C", "16"}, {"A", "D", "9"},
                {"A", "E", "5"}, {"C", "G", "12"}, {"C", "J", "8"}, {"D", "K", "3"},
                {"D", "L", "6"}, {"J", "B", "3"}, {"L", "F", "6"}, {"B", "F", "4"}};
        for (String[] arista : aristas) {
            agregarConexion(arista[0], arista[1], Double.parseDouble(arista[2]));
        }
        establecerInicio("H");
        establecerObjetivo("F");
    }
}
