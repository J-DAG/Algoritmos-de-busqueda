package Modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/** Representa un estado del grafo y las conexiones que salen de él. */
public class Nodo {
    private final String id;
    private double heuristica;
    private final List<Conexion> conexiones;

    public Nodo(String id) {
        this(id, 0.0);
    }

    public Nodo(String id, double heuristica) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("El id del nodo no puede estar vacío.");
        }
        validarHeuristica(heuristica);
        this.id = id.trim();
        this.heuristica = heuristica;
        this.conexiones = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public double getHeuristica() {
        return heuristica;
    }

    public void setHeuristica(double heuristica) {
        validarHeuristica(heuristica);
        this.heuristica = heuristica;
    }

    /** Devuelve una vista de solo lectura para proteger la lista interna. */
    public List<Conexion> getConexiones() {
        return Collections.unmodifiableList(conexiones);
    }

    /** Añade una conexión saliente. Grafo será responsable de crearla y quitarla. */
    void agregarConexion(Conexion conexion) {
        conexiones.add(Objects.requireNonNull(conexion, "La conexión no puede ser null."));
    }

    boolean tieneConexionA(Nodo destino) {
        return conexiones.stream().anyMatch(conexion -> conexion.getDestino().equals(destino));
    }

    boolean quitarConexionA(Nodo destino) {
        return conexiones.removeIf(conexion -> conexion.getDestino().equals(destino));
    }

    private static void validarHeuristica(double heuristica) {
        if (!Double.isFinite(heuristica)) {
            throw new IllegalArgumentException("La heurística debe ser un número finito.");
        }
    }

    // El id es inmutable y único dentro del grafo; sirve para búsquedas y mapas.
    @Override
    public boolean equals(Object objeto) {
        if (this == objeto) return true;
        if (!(objeto instanceof Nodo)) return false;
        Nodo otro = (Nodo) objeto;
        return id.equals(otro.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return id;
    }
}
