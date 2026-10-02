package Modelo.Busqueda;

import Modelo.Conexion;
import Modelo.Grafo;
import Modelo.Nodo;
import Modelo.PasoBusqueda;
import Modelo.ResultadoBusqueda;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** Hill Climbing avanza al mejor vecino solo si mejora la heurística actual. */
public class AscensoColina implements MetodoBusqueda {

    @Override
    public ResultadoBusqueda buscar(Grafo grafo, Nodo inicio, Nodo objetivo) {
        UtilidadesBusqueda.validarArgumentos(grafo, inicio, objetivo);
        Nodo actual = UtilidadesBusqueda.exigirNodoDelGrafo(grafo, inicio);
        Nodo nodoObjetivo = UtilidadesBusqueda.exigirNodoDelGrafo(grafo, objetivo);

        List<Nodo> recorrido = new ArrayList<>();
        List<PasoBusqueda> pasos = new ArrayList<>();
        Set<Nodo> descubiertos = new HashSet<>();

        while (true) {
            recorrido.add(actual);
            descubiertos.add(actual);
            List<Nodo> vecinos = new ArrayList<>();
            for (Conexion conexion : actual.getConexiones()) {
                Nodo vecino = conexion.getDestino();
                if (!descubiertos.contains(vecino)) {
                    vecinos.add(vecino);
                }
            }
            vecinos.sort(Comparator.comparingDouble(Nodo::getHeuristica));

            if (actual.equals(nodoObjetivo)) {
                pasos.add(new PasoBusqueda(pasos.size() + 1, actual,
                        PasoBusqueda.TipoFrontera.VECINOS, vecinos, new ArrayList<>()));
                return ResultadoBusqueda.encontrado(recorrido, recorrido, pasos,
                        UtilidadesBusqueda.calcularCosto(recorrido), "Objetivo encontrado mediante ascenso a la colina.");
            }

            if (vecinos.isEmpty() || vecinos.get(0).getHeuristica() >= actual.getHeuristica()) {
                pasos.add(new PasoBusqueda(pasos.size() + 1, actual,
                        PasoBusqueda.TipoFrontera.VECINOS, vecinos, new ArrayList<>()));
                return ResultadoBusqueda.noEncontrado(recorrido, pasos,
                        "Hill Climbing se detuvo: ningún vecino mejora la heurística actual.");
            }

            Nodo siguiente = vecinos.get(0);
            List<Nodo> elegido = new ArrayList<>();
            elegido.add(siguiente);
            pasos.add(new PasoBusqueda(pasos.size() + 1, actual,
                    PasoBusqueda.TipoFrontera.VECINOS, vecinos, elegido));
            actual = siguiente;
        }
    }

}
