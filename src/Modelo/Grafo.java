package Modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Administra los nodos y las conexiones de un grafo dirigido o no dirigido. */
public class Grafo {
    private final Map<String, Nodo> nodos;
    private final boolean dirigido;

    /** Crea un grafo no dirigido. */
    public Grafo() {
        this(false);
    }

    public Grafo(boolean dirigido) {
        this.dirigido = dirigido;
        this.nodos = new LinkedHashMap<>();
    }

    public boolean isDirigido() {
        return dirigido;
    }

    /** Devuelve false si ya existe un nodo con el mismo id. */
    public boolean agregarNodo(Nodo nodo) {
        if (nodo == null || nodos.containsKey(nodo.getId())) {
            return false;
        }
        nodos.put(nodo.getId(), nodo);
        return true;
    }

    /** Devuelve el nodo o null si no existe. */
    public Nodo buscarNodo(String id) {
        if (id == null) {
            return null;
        }
        return nodos.get(id.trim());
    }

    /** Devuelve una copia de solo lectura en orden de inserción. */
    public List<Nodo> obtenerNodos() {
        return Collections.unmodifiableList(new ArrayList<>(nodos.values()));
    }

    /** Elimina el nodo y todas las conexiones que llegaban a él. */
    public boolean eliminarNodo(String id) {
        Nodo eliminado = buscarNodo(id);
        if (eliminado == null) {
            return false;
        }

        nodos.remove(eliminado.getId());
        for (Nodo nodo : nodos.values()) {
            nodo.quitarConexionA(eliminado);
        }
        return true;
    }

    /**
     * Agrega una conexión. En grafos no dirigidos crea también la conexión inversa.
     * Devuelve false si falta algún nodo o ya existe esa conexión.
     */
    public boolean agregarConexion(String origenId, String destinoId) {
        return agregarConexion(origenId, destinoId, 1.0);
    }

    public boolean agregarConexion(String origenId, String destinoId, double peso) {
        Nodo origen = buscarNodo(origenId);
        Nodo destino = buscarNodo(destinoId);
        if (origen == null || destino == null) {
            return false;
        }

        // Validar el peso antes de modificar el grafo.
        Conexion conexion = new Conexion(destino, peso);
        if (origen.tieneConexionA(destino)
                || (!dirigido && !origen.equals(destino) && destino.tieneConexionA(origen))) {
            return false;
        }

        origen.agregarConexion(conexion);
        if (!dirigido && !origen.equals(destino)) {
            destino.agregarConexion(new Conexion(origen, peso));
        }
        return true;
    }

    /** Elimina una conexión y, si el grafo no es dirigido, también su inversa. */
    public boolean eliminarConexion(String origenId, String destinoId) {
        Nodo origen = buscarNodo(origenId);
        Nodo destino = buscarNodo(destinoId);
        if (origen == null || destino == null) {
            return false;
        }

        boolean eliminada = origen.quitarConexionA(destino);
        if (!dirigido && !origen.equals(destino)) {
            destino.quitarConexionA(origen);
        }
        return eliminada;
    }

    /** Cambia el peso de una conexión existente, incluidas ambas direcciones si aplica. */
    public boolean actualizarPesoConexion(String origenId, String destinoId, double nuevoPeso) {
        Nodo origen = buscarNodo(origenId);
        Nodo destino = buscarNodo(destinoId);
        if (origen == null || destino == null || !origen.tieneConexionA(destino)) {
            return false;
        }

        // Construir primero valida el peso sin dejar el grafo a medio actualizar.
        Conexion nuevaConexion = new Conexion(destino, nuevoPeso);
        Conexion inversa = !dirigido && !origen.equals(destino)
                ? new Conexion(origen, nuevoPeso) : null;
        origen.quitarConexionA(destino);
        origen.agregarConexion(nuevaConexion);
        if (inversa != null) {
            destino.quitarConexionA(origen);
            destino.agregarConexion(inversa);
        }
        return true;
    }
}
