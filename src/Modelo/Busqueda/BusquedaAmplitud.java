package Modelo.Busqueda;

import Modelo.Conexion;
import Modelo.Grafo;
import Modelo.Nodo;
import Modelo.PasoBusqueda;
import Modelo.ResultadoBusqueda;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** Búsqueda en amplitud (BFS), que explora por niveles usando una cola FIFO. */
public class BusquedaAmplitud implements MetodoBusqueda {

    @Override
    public ResultadoBusqueda buscar(Grafo grafo, Nodo inicio, Nodo objetivo) {
        UtilidadesBusqueda.validarArgumentos(grafo, inicio, objetivo);
        Nodo nodoInicio = UtilidadesBusqueda.exigirNodoDelGrafo(grafo, inicio);
        Nodo nodoObjetivo = UtilidadesBusqueda.exigirNodoDelGrafo(grafo, objetivo);

        Deque<Nodo> frontera = new ArrayDeque<>();
        Set<Nodo> descubiertos = new LinkedHashSet<>();
        List<Nodo> visitados = new ArrayList<>();
        List<PasoBusqueda> pasos = new ArrayList<>();
        Map<Nodo, Nodo> padres = new HashMap<>();
        Map<Nodo, Double> costos = new HashMap<>();

        frontera.addLast(nodoInicio);
        descubiertos.add(nodoInicio);
        costos.put(nodoInicio, 0.0);

        while (!frontera.isEmpty()) {
            Nodo actual = frontera.removeFirst();
            visitados.add(actual);

            List<Nodo> agregados = new ArrayList<>();
            if (actual.equals(nodoObjetivo)) {
                pasos.add(new PasoBusqueda(pasos.size() + 1, actual,
                        PasoBusqueda.TipoFrontera.COLA, new ArrayList<>(frontera), agregados));
                List<Nodo> camino = UtilidadesBusqueda.reconstruirCamino(padres, nodoObjetivo);
                return ResultadoBusqueda.encontrado(camino, visitados, pasos,
                        costos.get(nodoObjetivo), "Objetivo encontrado mediante búsqueda en amplitud.");
            }

            for (Conexion conexion : actual.getConexiones()) {
                Nodo vecino = conexion.getDestino();
                if (descubiertos.add(vecino)) {
                    padres.put(vecino, actual);
                    costos.put(vecino, costos.get(actual) + conexion.getPeso());
                    frontera.addLast(vecino);
                    agregados.add(vecino);
                }
            }
            pasos.add(new PasoBusqueda(pasos.size() + 1, actual,
                    PasoBusqueda.TipoFrontera.COLA, new ArrayList<>(frontera), agregados));
        }

        return ResultadoBusqueda.noEncontrado(visitados, pasos,
                "No existe un camino entre el inicio y el objetivo.");
    }

}
