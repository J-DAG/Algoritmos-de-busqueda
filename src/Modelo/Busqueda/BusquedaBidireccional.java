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
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** Búsqueda en amplitud desde el inicio y desde el objetivo simultáneamente. */
public class BusquedaBidireccional implements MetodoBusqueda {

    @Override
    public ResultadoBusqueda buscar(Grafo grafo, Nodo inicio, Nodo objetivo) {
        UtilidadesBusqueda.validarArgumentos(grafo, inicio, objetivo);
        Nodo nodoInicio = UtilidadesBusqueda.exigirNodoDelGrafo(grafo, inicio);
        Nodo nodoObjetivo = UtilidadesBusqueda.exigirNodoDelGrafo(grafo, objetivo);

        Map<Nodo, List<Nodo>> sucesores = crearSucesores(grafo, false);
        Map<Nodo, List<Nodo>> predecesores = crearSucesores(grafo, true);
        Contexto inicioCtx = new Contexto(nodoInicio);
        Contexto objetivoCtx = new Contexto(nodoObjetivo);
        Set<Nodo> visitadosUnicos = new LinkedHashSet<>();
        List<PasoBusqueda> pasos = new ArrayList<>();
        Nodo encuentro = null;

        while (!inicioCtx.frontera.isEmpty() && !objetivoCtx.frontera.isEmpty()) {
            encuentro = expandirNivel(inicioCtx, objetivoCtx, sucesores,
                    PasoBusqueda.LadoBusqueda.INICIO, visitadosUnicos, pasos);
            if (encuentro != null) break;

            encuentro = expandirNivel(objetivoCtx, inicioCtx, predecesores,
                    PasoBusqueda.LadoBusqueda.OBJETIVO, visitadosUnicos, pasos);
            if (encuentro != null) break;
        }

        List<Nodo> visitados = new ArrayList<>(visitadosUnicos);
        if (encuentro == null) {
            return ResultadoBusqueda.noEncontrado(visitados, pasos,
                    "No existe un camino entre el inicio y el objetivo.");
        }

        List<Nodo> camino = unirCaminos(inicioCtx.padres, objetivoCtx.padres, encuentro);
        double costoTotal = UtilidadesBusqueda.calcularCosto(camino);
        return ResultadoBusqueda.encontrado(camino, visitados, pasos, costoTotal,
                "Objetivo encontrado mediante búsqueda bidireccional.");
    }

    /** Expande un nivel completo para conservar la propiedad de camino mínimo en aristas. */
    private static Nodo expandirNivel(Contexto actual, Contexto contrario,
                                      Map<Nodo, List<Nodo>> adyacencias,
                                      PasoBusqueda.LadoBusqueda lado,
                                      Set<Nodo> visitados, List<PasoBusqueda> pasos) {
        int cantidadNivel = actual.frontera.size();
        Nodo mejorEncuentro = null;
        int mejorDistancia = Integer.MAX_VALUE;

        for (int i = 0; i < cantidadNivel; i++) {
            Nodo extraido = actual.frontera.removeFirst();
            visitados.add(extraido);
            List<Nodo> agregados = new ArrayList<>();

            if (contrario.distancias.containsKey(extraido)) {
                int distancia = actual.distancias.get(extraido) + contrario.distancias.get(extraido);
                if (distancia < mejorDistancia) {
                    mejorEncuentro = extraido;
                    mejorDistancia = distancia;
                }
            }

            for (Nodo vecino : adyacencias.get(extraido)) {
                if (!actual.distancias.containsKey(vecino)) {
                    actual.distancias.put(vecino, actual.distancias.get(extraido) + 1);
                    actual.padres.put(vecino, extraido);
                    actual.frontera.addLast(vecino);
                    agregados.add(vecino);
                    if (contrario.distancias.containsKey(vecino)) {
                        int distancia = actual.distancias.get(vecino) + contrario.distancias.get(vecino);
                        if (distancia < mejorDistancia) {
                            mejorEncuentro = vecino;
                            mejorDistancia = distancia;
                        }
                    }
                }
            }

            pasos.add(new PasoBusqueda(pasos.size() + 1, extraido,
                    PasoBusqueda.TipoFrontera.COLA, lado,
                    new ArrayList<>(actual.frontera), new ArrayList<>(contrario.frontera), agregados));
        }
        return mejorEncuentro;
    }

    /** Construye sucesores normales o, en modo inverso, los predecesores de cada nodo. */
    private static Map<Nodo, List<Nodo>> crearSucesores(Grafo grafo, boolean invertir) {
        Map<Nodo, List<Nodo>> adyacencias = new LinkedHashMap<>();
        for (Nodo nodo : grafo.obtenerNodos()) {
            adyacencias.put(nodo, new ArrayList<>());
        }
        for (Nodo origen : grafo.obtenerNodos()) {
            for (Conexion conexion : origen.getConexiones()) {
                Nodo destino = conexion.getDestino();
                if (invertir) {
                    adyacencias.get(destino).add(origen);
                } else {
                    adyacencias.get(origen).add(destino);
                }
            }
        }
        return adyacencias;
    }

    private static List<Nodo> unirCaminos(Map<Nodo, Nodo> padresInicio,
                                          Map<Nodo, Nodo> padresObjetivo, Nodo encuentro) {
        List<Nodo> camino = UtilidadesBusqueda.reconstruirCamino(padresInicio, encuentro);
        Nodo siguiente = padresObjetivo.get(encuentro);
        while (siguiente != null) {
            camino.add(siguiente);
            siguiente = padresObjetivo.get(siguiente);
        }
        return camino;
    }

    private static final class Contexto {
        private final Deque<Nodo> frontera = new ArrayDeque<>();
        private final Map<Nodo, Nodo> padres = new HashMap<>();
        private final Map<Nodo, Integer> distancias = new HashMap<>();

        private Contexto(Nodo inicial) {
            frontera.add(inicial);
            distancias.put(inicial, 0);
        }
    }
}
