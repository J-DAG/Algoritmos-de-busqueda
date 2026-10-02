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

/** Búsqueda en profundidad (DFS), implementada iterativamente con una pila. */
public class BusquedaProfundidad implements MetodoBusqueda {

    @Override
    public ResultadoBusqueda buscar(Grafo grafo, Nodo inicio, Nodo objetivo) {
        UtilidadesBusqueda.validarArgumentos(grafo, inicio, objetivo);
        Nodo nodoInicio = UtilidadesBusqueda.exigirNodoDelGrafo(grafo, inicio);
        Nodo nodoObjetivo = UtilidadesBusqueda.exigirNodoDelGrafo(grafo, objetivo);

        Deque<Nodo> pila = new ArrayDeque<>();
        Set<Nodo> descubiertos = new LinkedHashSet<>();
        List<Nodo> visitados = new ArrayList<>();
        List<PasoBusqueda> pasos = new ArrayList<>();
        Map<Nodo, Nodo> padres = new HashMap<>();
        Map<Nodo, Double> costos = new HashMap<>();

        pila.push(nodoInicio);
        descubiertos.add(nodoInicio);
        costos.put(nodoInicio, 0.0);

        while (!pila.isEmpty()) {
            Nodo actual = pila.pop();
            visitados.add(actual);

            List<Nodo> agregados = new ArrayList<>();
            if (!actual.equals(nodoObjetivo)) {
                // Descubrir en orden de conexiones y apilar al revés para visitar
                // primero el vecino que aparece primero en la lista del nodo.
                List<Conexion> conexiones = actual.getConexiones();
                List<Nodo> porApilar = new ArrayList<>();
                for (Conexion conexion : conexiones) {
                    Nodo vecino = conexion.getDestino();
                    if (descubiertos.add(vecino)) {
                        padres.put(vecino, actual);
                        double costo = costos.get(actual) + conexion.getPeso();
                        costos.put(vecino, costo);
                        porApilar.add(vecino);
                        agregados.add(vecino);
                    }
                }
                for (int i = porApilar.size() - 1; i >= 0; i--) {
                    pila.push(porApilar.get(i));
                }
            }

            pasos.add(new PasoBusqueda(pasos.size() + 1, actual,
                    PasoBusqueda.TipoFrontera.PILA, new ArrayList<>(pila), agregados));

            if (actual.equals(nodoObjetivo)) {
                List<Nodo> camino = UtilidadesBusqueda.reconstruirCamino(padres, nodoObjetivo);
                return ResultadoBusqueda.encontrado(camino, visitados, pasos,
                        costos.get(nodoObjetivo), "Objetivo encontrado mediante búsqueda en profundidad.");
            }
        }

        return ResultadoBusqueda.noEncontrado(visitados, pasos,
                "No existe un camino entre el inicio y el objetivo.");
    }

}
