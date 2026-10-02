package Modelo;

import java.util.Objects;

/** Representa una conexión saliente desde el nodo que la contiene. */
public class Conexion {
    private final Nodo destino;
    private final double peso;

    public Conexion(Nodo destino, double peso) {
        this.destino = Objects.requireNonNull(destino, "El destino no puede ser null.");
        if (!Double.isFinite(peso) || peso < 0.0) {
            throw new IllegalArgumentException("El peso debe ser finito y no negativo.");
        }
        this.peso = peso;
    }

    public Nodo getDestino() {
        return destino;
    }

    public double getPeso() {
        return peso;
    }
}
