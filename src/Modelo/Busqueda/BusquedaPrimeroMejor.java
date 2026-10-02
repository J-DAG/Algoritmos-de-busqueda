package Modelo.Busqueda;

import Modelo.Conexion;
import Modelo.Grafo;
import Modelo.Nodo;
import Modelo.PasoBusqueda;
import Modelo.ResultadoBusqueda;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

/** Búsqueda Greedy: prioriza h(n) e ignora el costo acumulado al ordenar. */
public class BusquedaPrimeroMejor implements MetodoBusqueda {

    @Override
    public ResultadoBusqueda buscar(Grafo grafo, Nodo inicio, Nodo objetivo) {
        UtilidadesBusqueda.validarArgumentos(grafo, inicio, objetivo);
        Nodo nodoInicio = UtilidadesBusqueda.exigirNodoDelGrafo(grafo, inicio);
        Nodo nodoObjetivo = UtilidadesBusqueda.exigirNodoDelGrafo(grafo, objetivo);

        PriorityQueue<EntradaPrioridad> frontera = new PriorityQueue<>();
        Set<Nodo> descubiertos = new HashSet<>();
        Map<Nodo, Nodo> padres = new HashMap<>();
        Map<Nodo, Double> costos = new HashMap<>();
        List<Nodo> visitados = new ArrayList<>();
        List<PasoBusqueda> pasos = new ArrayList<>();
        long orden = 0;

        descubiertos.add(nodoInicio);
        costos.put(nodoInicio, 0.0);
        frontera.add(new EntradaPrioridad(nodoInicio, 0.0, nodoInicio.getHeuristica(), orden++));

        while (!frontera.isEmpty()) {
            EntradaPrioridad entrada = frontera.remove();
            Nodo actual = entrada.nodo;
            visitados.add(actual);
            List<Nodo> agregados = new ArrayList<>();

            if (!actual.equals(nodoObjetivo)) {
                for (Conexion conexion : actual.getConexiones()) {
                    Nodo vecino = conexion.getDestino();
                    if (descubiertos.add(vecino)) {
                        padres.put(vecino, actual);
                        double costo = costos.get(actual) + conexion.getPeso();
                        costos.put(vecino, costo);
                        frontera.add(new EntradaPrioridad(vecino, costo,
                                vecino.getHeuristica(), orden++));
                        agregados.add(vecino);
                    }
                }
            }

            pasos.add(new PasoBusqueda(pasos.size() + 1, actual,
                    PasoBusqueda.TipoFrontera.PRIORIDAD,
                    UtilidadesBusqueda.obtenerFronteraOrdenada(frontera, null), agregados));

            if (actual.equals(nodoObjetivo)) {
                List<Nodo> camino = UtilidadesBusqueda.reconstruirCamino(padres, nodoObjetivo);
                return ResultadoBusqueda.encontrado(camino, visitados, pasos, costos.get(actual),
                        "Objetivo encontrado mediante búsqueda Greedy (h(n)).");
            }
        }

        return ResultadoBusqueda.noEncontrado(visitados, pasos,
                "No existe un camino entre el inicio y el objetivo.");
    }
}
