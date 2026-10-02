package Modelo.Busqueda;

import Modelo.Conexion;
import Modelo.Grafo;
import Modelo.Nodo;
import Modelo.PasoBusqueda;
import Modelo.ResultadoBusqueda;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

/** A* ordena por f(n) = g(n) + h(n) y reabre nodos si mejora el costo. */
public class BusquedaAEstrella implements MetodoBusqueda {

    @Override
    public ResultadoBusqueda buscar(Grafo grafo, Nodo inicio, Nodo objetivo) {
        UtilidadesBusqueda.validarArgumentos(grafo, inicio, objetivo);
        Nodo nodoInicio = UtilidadesBusqueda.exigirNodoDelGrafo(grafo, inicio);
        Nodo nodoObjetivo = UtilidadesBusqueda.exigirNodoDelGrafo(grafo, objetivo);

        PriorityQueue<EntradaPrioridad> frontera = new PriorityQueue<>();
        Map<Nodo, Double> mejorCosto = new HashMap<>();
        Map<Nodo, Nodo> padres = new HashMap<>();
        List<Nodo> visitados = new ArrayList<>();
        List<PasoBusqueda> pasos = new ArrayList<>();
        long orden = 0;

        mejorCosto.put(nodoInicio, 0.0);
        frontera.add(new EntradaPrioridad(nodoInicio, 0.0,
                nodoInicio.getHeuristica(), orden++));

        while (!frontera.isEmpty()) {
            EntradaPrioridad entrada = frontera.remove();
            Double costoConocido = mejorCosto.get(entrada.nodo);
            if (costoConocido == null || Double.compare(entrada.costoAcumulado, costoConocido) != 0) {
                continue;
            }

            Nodo actual = entrada.nodo;
            visitados.add(actual);
            List<Nodo> agregados = new ArrayList<>();
            if (!actual.equals(nodoObjetivo)) {
                for (Conexion conexion : actual.getConexiones()) {
                    Nodo vecino = conexion.getDestino();
                    double nuevoCosto = costoConocido + conexion.getPeso();
                    Double costoAnterior = mejorCosto.get(vecino);
                    if (costoAnterior == null || nuevoCosto < costoAnterior) {
                        mejorCosto.put(vecino, nuevoCosto);
                        padres.put(vecino, actual);
                        double estimado = nuevoCosto + vecino.getHeuristica();
                        frontera.add(new EntradaPrioridad(vecino, nuevoCosto, estimado, orden++));
                        agregados.add(vecino);
                    }
                }
            }

            List<Nodo> fronteraOrdenada = UtilidadesBusqueda.obtenerFronteraOrdenada(frontera, mejorCosto);
            Map<Nodo, Double> costosFrontera = UtilidadesBusqueda.obtenerCostosFrontera(frontera, mejorCosto);
            pasos.add(new PasoBusqueda(pasos.size() + 1, actual,
                    PasoBusqueda.TipoFrontera.PRIORIDAD, fronteraOrdenada, agregados, costosFrontera));

            if (actual.equals(nodoObjetivo)) {
                List<Nodo> camino = UtilidadesBusqueda.reconstruirCamino(padres, nodoObjetivo);
                return ResultadoBusqueda.encontrado(camino, visitados, pasos, costoConocido,
                        "Objetivo encontrado mediante A* (g(n) + h(n)).");
            }
        }

        return ResultadoBusqueda.noEncontrado(visitados, pasos,
                "No existe un camino entre el inicio y el objetivo.");
    }
}
