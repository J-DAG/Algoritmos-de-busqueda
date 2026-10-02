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
import java.util.List;
import java.util.Map;

/** Repite una búsqueda en profundidad aumentando gradualmente su límite. */
public class BusquedaProfundidadIterativa implements MetodoBusqueda {

    @Override
    public ResultadoBusqueda buscar(Grafo grafo, Nodo inicio, Nodo objetivo) {
        UtilidadesBusqueda.validarArgumentos(grafo, inicio, objetivo);
        Nodo nodoInicio = UtilidadesBusqueda.exigirNodoDelGrafo(grafo, inicio);
        Nodo nodoObjetivo = UtilidadesBusqueda.exigirNodoDelGrafo(grafo, objetivo);

        List<Nodo> visitados = new ArrayList<>();
        List<PasoBusqueda> pasos = new ArrayList<>();
        int maximo = Math.max(0, grafo.obtenerNodos().size() - 1);

        for (int limite = 0; limite <= maximo; limite++) {
            ResultadoLimite resultado = buscarConLimite(nodoInicio, nodoObjetivo, limite,
                    visitados, pasos);
            if (resultado.camino != null) {
                return ResultadoBusqueda.encontrado(resultado.camino, visitados, pasos,
                        UtilidadesBusqueda.calcularCosto(resultado.camino),
                        "Objetivo encontrado mediante profundidad iterativa con límite " + limite + ".");
            }
        }

        return ResultadoBusqueda.noEncontrado(visitados, pasos,
                "No existe un camino entre el inicio y el objetivo.");
    }

    private static ResultadoLimite buscarConLimite(Nodo inicio, Nodo objetivo, int limite,
                                                    List<Nodo> visitados, List<PasoBusqueda> pasos) {
        Deque<Entrada> pila = new ArrayDeque<>();
        Map<Nodo, Integer> mejorProfundidad = new HashMap<>();
        Map<Nodo, Nodo> padres = new HashMap<>();
        pila.push(new Entrada(inicio, 0));
        mejorProfundidad.put(inicio, 0);

        while (!pila.isEmpty()) {
            Entrada entrada = pila.pop();
            Nodo actual = entrada.nodo;
            if (entrada.profundidad != mejorProfundidad.get(actual)) {
                continue;
            }
            visitados.add(actual);
            List<Nodo> agregados = new ArrayList<>();

            if (actual.equals(objetivo)) {
                pasos.add(crearPaso(pasos, actual, pila, agregados, limite));
                return new ResultadoLimite(UtilidadesBusqueda.reconstruirCamino(padres, objetivo));
            }

            if (entrada.profundidad < limite) {
                List<Conexion> conexiones = actual.getConexiones();
                List<Entrada> porApilar = new ArrayList<>();
                for (Conexion conexion : conexiones) {
                    Nodo vecino = conexion.getDestino();
                    int profundidadVecino = entrada.profundidad + 1;
                    Integer profundidadAnterior = mejorProfundidad.get(vecino);
                    if (profundidadAnterior == null || profundidadVecino < profundidadAnterior) {
                        mejorProfundidad.put(vecino, profundidadVecino);
                        padres.put(vecino, actual);
                        porApilar.add(new Entrada(vecino, profundidadVecino));
                        agregados.add(vecino);
                    }
                }
                for (int i = porApilar.size() - 1; i >= 0; i--) {
                    pila.push(porApilar.get(i));
                }
            }
            pasos.add(crearPaso(pasos, actual, pila, agregados, limite));
        }
        return new ResultadoLimite(null);
    }

    private static PasoBusqueda crearPaso(List<PasoBusqueda> pasos, Nodo actual,
                                          Deque<Entrada> pila, List<Nodo> agregados, int limite) {
        List<Nodo> frontera = new ArrayList<>();
        for (Entrada entrada : pila) {
            frontera.add(entrada.nodo);
        }
        return new PasoBusqueda(pasos.size() + 1, actual,
                PasoBusqueda.TipoFrontera.PILA, frontera, agregados, limite);
    }

    private static final class Entrada {
        private final Nodo nodo;
        private final int profundidad;

        private Entrada(Nodo nodo, int profundidad) {
            this.nodo = nodo;
            this.profundidad = profundidad;
        }
    }

    private static final class ResultadoLimite {
        private final List<Nodo> camino;

        private ResultadoLimite(List<Nodo> camino) {
            this.camino = camino;
        }
    }
}
