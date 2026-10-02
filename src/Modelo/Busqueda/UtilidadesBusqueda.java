package Modelo.Busqueda;

import Modelo.Grafo;
import Modelo.Conexion;
import Modelo.Nodo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

/** Operaciones pequeñas compartidas por los algoritmos de búsqueda. */
final class UtilidadesBusqueda {
    private UtilidadesBusqueda() {
    }

    static void validarArgumentos(Grafo grafo, Nodo inicio, Nodo objetivo) {
        if (grafo == null || inicio == null || objetivo == null) {
            throw new IllegalArgumentException("El grafo, el inicio y el objetivo son obligatorios.");
        }
    }

    static Nodo exigirNodoDelGrafo(Grafo grafo, Nodo nodo) {
        Nodo existente = grafo.buscarNodo(nodo.getId());
        if (existente == null) {
            throw new IllegalArgumentException("El nodo " + nodo.getId() + " no pertenece al grafo.");
        }
        return existente;
    }

    static List<Nodo> reconstruirCamino(Map<Nodo, Nodo> padres, Nodo objetivo) {
        List<Nodo> camino = new ArrayList<>();
        Nodo actual = objetivo;
        camino.add(actual);
        while (padres.containsKey(actual)) {
            actual = padres.get(actual);
            camino.add(actual);
        }
        Collections.reverse(camino);
        return camino;
    }

    static List<Nodo> obtenerFronteraOrdenada(PriorityQueue<EntradaPrioridad> frontera,
                                               Map<Nodo, Double> mejorCosto) {
        PriorityQueue<EntradaPrioridad> copia = new PriorityQueue<>(frontera);
        List<Nodo> nodos = new ArrayList<>();
        while (!copia.isEmpty()) {
            EntradaPrioridad entrada = copia.remove();
            if (mejorCosto == null || Double.compare(entrada.costoAcumulado,
                    mejorCosto.get(entrada.nodo)) == 0) {
                nodos.add(entrada.nodo);
            }
        }
        return nodos;
    }

    static double calcularCosto(List<Nodo> camino) {
        double costo = 0.0;
        for (int i = 0; i < camino.size() - 1; i++) {
            Nodo origen = camino.get(i);
            Nodo destino = camino.get(i + 1);
            for (Conexion conexion : origen.getConexiones()) {
                if (conexion.getDestino().equals(destino)) {
                    costo += conexion.getPeso();
                    break;
                }
            }
        }
        return costo;
    }
}
