package Modelo.Busqueda;

import Modelo.Nodo;

/** Entrada de frontera ordenada por prioridad y luego por orden de inserción. */
final class EntradaPrioridad implements Comparable<EntradaPrioridad> {
    final Nodo nodo;
    final double costoAcumulado;
    final double prioridad;
    final long orden;

    EntradaPrioridad(Nodo nodo, double costoAcumulado, double prioridad, long orden) {
        this.nodo = nodo;
        this.costoAcumulado = costoAcumulado;
        this.prioridad = prioridad;
        this.orden = orden;
    }

    @Override
    public int compareTo(EntradaPrioridad otra) {
        int comparacion = Double.compare(prioridad, otra.prioridad);
        return comparacion != 0 ? comparacion : Long.compare(orden, otra.orden);
    }
}
