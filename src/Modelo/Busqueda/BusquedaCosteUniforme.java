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

/** UCS expande siempre el nodo con menor costo acumulado g(n). */
public class BusquedaCosteUniforme implements MetodoBusqueda {

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
        frontera.add(new EntradaPrioridad(nodoInicio, 0.0, 0.0, orden++));

        while (!frontera.isEmpty()) {
            EntradaPrioridad entrada = frontera.remove();
            Double costoActual = mejorCosto.get(entrada.nodo);
            if (costoActual == null || Double.compare(entrada.costoAcumulado, costoActual) != 0) {
                continue; // Entrada vieja reemplazada por una ruta más barata.
            }

            Nodo actual = entrada.nodo;
            visitados.add(actual);
            List<Nodo> agregados = new ArrayList<>();
            if (!actual.equals(nodoObjetivo)) {
                for (Conexion conexion : actual.getConexiones()) {
                    Nodo vecino = conexion.getDestino();
                    double nuevoCosto = costoActual + conexion.getPeso();
                    Double costoAnterior = mejorCosto.get(vecino);
                    if (costoAnterior == null || nuevoCosto < costoAnterior) {
                        mejorCosto.put(vecino, nuevoCosto);
                        padres.put(vecino, actual);
                        frontera.add(new EntradaPrioridad(vecino, nuevoCosto, nuevoCosto, orden++));
                        agregados.add(vecino);
                    }
                }
            }
            pasos.add(new PasoBusqueda(pasos.size() + 1, actual,
                    PasoBusqueda.TipoFrontera.PRIORIDAD,
                    UtilidadesBusqueda.obtenerFronteraOrdenada(frontera, mejorCosto), agregados));

            if (actual.equals(nodoObjetivo)) {
                List<Nodo> camino = UtilidadesBusqueda.reconstruirCamino(padres, nodoObjetivo);
                return ResultadoBusqueda.encontrado(camino, visitados, pasos, costoActual,
                        "Objetivo encontrado mediante búsqueda de coste uniforme.");
            }
        }

        return ResultadoBusqueda.noEncontrado(visitados, pasos,
                "No existe un camino entre el inicio y el objetivo.");
    }
}
