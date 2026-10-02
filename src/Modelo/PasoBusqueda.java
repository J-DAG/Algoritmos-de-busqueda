package Modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/** Instantánea de un paso de búsqueda después de expandir el nodo extraído. */
public class PasoBusqueda {
    public enum TipoFrontera {
        COLA,
        PILA,
        PRIORIDAD,
        VECINOS
    }

    public enum LadoBusqueda {
        UNICO,
        INICIO,
        OBJETIVO
    }

    private final int numero;
    private final Nodo nodoExtraido;
    private final TipoFrontera tipoFrontera;
    private final LadoBusqueda ladoBusqueda;
    private final List<Nodo> frontera;
    private final List<Nodo> fronteraOpuesta;
    private final List<Nodo> nodosAgregados;
    private final Map<Nodo, Double> costosAcumuladosFrontera;
    private final int limiteProfundidad;

    public PasoBusqueda(int numero, Nodo nodoExtraido, TipoFrontera tipoFrontera,
                        List<Nodo> frontera, List<Nodo> nodosAgregados) {
        this(numero, nodoExtraido, tipoFrontera, LadoBusqueda.UNICO,
                frontera, Collections.emptyList(), nodosAgregados,
                Collections.emptyMap(), -1);
    }

    public PasoBusqueda(int numero, Nodo nodoExtraido, TipoFrontera tipoFrontera,
                        LadoBusqueda ladoBusqueda, List<Nodo> frontera,
                        List<Nodo> fronteraOpuesta, List<Nodo> nodosAgregados) {
        this(numero, nodoExtraido, tipoFrontera, ladoBusqueda,
                frontera, fronteraOpuesta, nodosAgregados,
                Collections.emptyMap(), -1);
    }

    public PasoBusqueda(int numero, Nodo nodoExtraido, TipoFrontera tipoFrontera,
                        List<Nodo> frontera, List<Nodo> nodosAgregados,
                        int limiteProfundidad) {
        this(numero, nodoExtraido, tipoFrontera, LadoBusqueda.UNICO,
                frontera, Collections.emptyList(), nodosAgregados,
                Collections.emptyMap(), limiteProfundidad);
    }

    public PasoBusqueda(int numero, Nodo nodoExtraido, TipoFrontera tipoFrontera,
                        List<Nodo> frontera, List<Nodo> nodosAgregados,
                        Map<Nodo, Double> costosAcumuladosFrontera) {
        this(numero, nodoExtraido, tipoFrontera, LadoBusqueda.UNICO,
                frontera, Collections.emptyList(), nodosAgregados,
                costosAcumuladosFrontera, -1);
    }

    private PasoBusqueda(int numero, Nodo nodoExtraido, TipoFrontera tipoFrontera,
                         LadoBusqueda ladoBusqueda, List<Nodo> frontera,
                         List<Nodo> fronteraOpuesta, List<Nodo> nodosAgregados,
                         Map<Nodo, Double> costosAcumuladosFrontera,
                         int limiteProfundidad) {
        if (numero < 1) {
            throw new IllegalArgumentException("El número de paso debe ser positivo.");
        }
        this.numero = numero;
        this.nodoExtraido = Objects.requireNonNull(nodoExtraido, "El nodo extraído no puede ser null.");
        this.tipoFrontera = Objects.requireNonNull(tipoFrontera, "El tipo de frontera no puede ser null.");
        this.ladoBusqueda = Objects.requireNonNull(ladoBusqueda, "El lado de búsqueda no puede ser null.");
        this.frontera = copiaInmutable(frontera, "La frontera no puede ser null.");
        this.fronteraOpuesta = copiaInmutable(fronteraOpuesta, "La frontera opuesta no puede ser null.");
        this.nodosAgregados = copiaInmutable(nodosAgregados, "Los nodos agregados no pueden ser null.");
        Objects.requireNonNull(costosAcumuladosFrontera,
                "Los costos acumulados de frontera no pueden ser null.");
        this.costosAcumuladosFrontera = Collections.unmodifiableMap(
                new java.util.LinkedHashMap<>(costosAcumuladosFrontera));
        if (limiteProfundidad < -1) {
            throw new IllegalArgumentException("El límite de profundidad no puede ser menor que -1.");
        }
        this.limiteProfundidad = limiteProfundidad;
    }

    public int getNumero() {
        return numero;
    }

    public Nodo getNodoExtraido() {
        return nodoExtraido;
    }

    public TipoFrontera getTipoFrontera() {
        return tipoFrontera;
    }

    public LadoBusqueda getLadoBusqueda() {
        return ladoBusqueda;
    }

    /** Ordenada desde el próximo nodo que será extraído. */
    public List<Nodo> getFrontera() {
        return frontera;
    }

    /** Segunda cola, utilizada por la búsqueda bidireccional. */
    public List<Nodo> getFronteraOpuesta() {
        return fronteraOpuesta;
    }

    /** Nodos descubiertos al expandir el nodo extraído. */
    public List<Nodo> getNodosAgregados() {
        return nodosAgregados;
    }

    /** Costos g(n) para los nodos prioritarios que siguen en frontera, si aplica. */
    public Map<Nodo, Double> getCostosAcumuladosFrontera() {
        return costosAcumuladosFrontera;
    }

    /** -1 cuando el algoritmo no trabaja con límite de profundidad. */
    public int getLimiteProfundidad() {
        return limiteProfundidad;
    }

    private static List<Nodo> copiaInmutable(List<Nodo> nodos, String mensajeError) {
        Objects.requireNonNull(nodos, mensajeError);
        return Collections.unmodifiableList(new ArrayList<>(nodos));
    }
}
