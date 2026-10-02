package Modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/** Resultado común que los algoritmos de búsqueda devolverán al controlador. */
public class ResultadoBusqueda {
    private final boolean encontrado;
    private final List<Nodo> camino;
    private final List<Nodo> visitados;
    private final List<PasoBusqueda> pasos;
    private final double costoTotal;
    private final String mensaje;

    private ResultadoBusqueda(boolean encontrado, List<Nodo> camino,
                              List<Nodo> visitados, List<PasoBusqueda> pasos,
                              double costoTotal, String mensaje) {
        this.encontrado = encontrado;
        this.camino = copiaInmutable(camino, "El camino no puede ser null.");
        this.visitados = copiaInmutable(visitados, "La lista de visitados no puede ser null.");
        this.pasos = copiaInmutable(pasos, "La lista de pasos no puede ser null.");
        this.costoTotal = costoTotal;
        this.mensaje = Objects.requireNonNull(mensaje, "El mensaje no puede ser null.");
    }

    public static ResultadoBusqueda encontrado(List<Nodo> camino, List<Nodo> visitados,
                                                double costoTotal, String mensaje) {
        return encontrado(camino, visitados, Collections.emptyList(), costoTotal, mensaje);
    }

    public static ResultadoBusqueda encontrado(List<Nodo> camino, List<Nodo> visitados,
                                                List<PasoBusqueda> pasos,
                                                double costoTotal, String mensaje) {
        if (camino == null || camino.isEmpty()) {
            throw new IllegalArgumentException("Un resultado encontrado debe incluir un camino.");
        }
        if (!Double.isFinite(costoTotal) || costoTotal < 0.0) {
            throw new IllegalArgumentException("El costo debe ser finito y no negativo.");
        }
        return new ResultadoBusqueda(true, camino, visitados, pasos, costoTotal, mensaje);
    }

    public static ResultadoBusqueda noEncontrado(List<Nodo> visitados, String mensaje) {
        return noEncontrado(visitados, Collections.emptyList(), mensaje);
    }

    public static ResultadoBusqueda noEncontrado(List<Nodo> visitados,
                                                  List<PasoBusqueda> pasos, String mensaje) {
        return new ResultadoBusqueda(false, Collections.emptyList(), visitados, pasos,
                Double.POSITIVE_INFINITY, mensaje);
    }

    public boolean isEncontrado() {
        return encontrado;
    }

    public List<Nodo> getCamino() {
        return camino;
    }

    public List<Nodo> getVisitados() {
        return visitados;
    }

    public List<PasoBusqueda> getPasos() {
        return pasos;
    }

    public int getNodosExplorados() {
        return visitados.size();
    }

    /** Es infinito cuando no se encontró un camino. */
    public double getCostoTotal() {
        return costoTotal;
    }

    public String getMensaje() {
        return mensaje;
    }

    private static <T> List<T> copiaInmutable(List<T> elementos, String mensajeError) {
        Objects.requireNonNull(elementos, mensajeError);
        return Collections.unmodifiableList(new ArrayList<>(elementos));
    }
}
