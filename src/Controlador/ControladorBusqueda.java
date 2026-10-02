package Controlador;

import Modelo.Busqueda.AscensoColina;
import Modelo.Busqueda.BusquedaAEstrella;
import Modelo.Busqueda.BusquedaAmplitud;
import Modelo.Busqueda.BusquedaBidireccional;
import Modelo.Busqueda.BusquedaCosteUniforme;
import Modelo.Busqueda.BusquedaPrimeroMejor;
import Modelo.Busqueda.BusquedaProfundidad;
import Modelo.Busqueda.BusquedaProfundidadIterativa;
import Modelo.Busqueda.MetodoBusqueda;
import Modelo.Grafo;
import Modelo.Nodo;
import Modelo.ResultadoBusqueda;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/** Selecciona estrategias y coordina su ejecución y comparación. */
public class ControladorBusqueda {
    private final Map<String, MetodoBusqueda> metodos = new LinkedHashMap<>();

    public ControladorBusqueda() {
        registrarMetodo("BFS", new BusquedaAmplitud());
        registrarMetodo("DFS", new BusquedaProfundidad());
        registrarMetodo("Bidireccional", new BusquedaBidireccional());
        registrarMetodo("IDDFS", new BusquedaProfundidadIterativa());
        registrarMetodo("UCS", new BusquedaCosteUniforme());
        registrarMetodo("Hill Climbing", new AscensoColina());
        registrarMetodo("Greedy", new BusquedaPrimeroMejor());
        registrarMetodo("A*", new BusquedaAEstrella());
    }

    public void registrarMetodo(String nombre, MetodoBusqueda metodo) {
        if (nombre == null || nombre.trim().isEmpty() || metodo == null) {
            throw new IllegalArgumentException("El nombre y el método son obligatorios.");
        }
        metodos.put(nombre, metodo);
    }

    public Map<String, MetodoBusqueda> obtenerMetodos() {
        return Collections.unmodifiableMap(metodos);
    }

    public ResultadoBusqueda buscar(String nombre, Grafo grafo, Nodo inicio, Nodo objetivo) {
        MetodoBusqueda metodo = metodos.get(nombre);
        if (metodo == null) {
            throw new IllegalArgumentException("No existe un método registrado con el nombre " + nombre + ".");
        }
        return metodo.buscar(grafo, inicio, objetivo);
    }

    /** Ejecuta todos los métodos registrados sobre la misma instancia del grafo. */
    public Map<String, ResultadoBusqueda> comparar(Grafo grafo, Nodo inicio, Nodo objetivo) {
        Map<String, ResultadoBusqueda> resultados = new LinkedHashMap<>();
        for (Map.Entry<String, MetodoBusqueda> entrada : metodos.entrySet()) {
            resultados.put(entrada.getKey(), entrada.getValue().buscar(grafo, inicio, objetivo));
        }
        return Collections.unmodifiableMap(resultados);
    }
}
