package Vista;

import Controlador.ControladorBusqueda;
import Controlador.ControladorGrafo;
import Modelo.Busqueda.MetodoBusqueda;
import Modelo.Conexion;
import Modelo.Nodo;
import Modelo.PasoBusqueda;
import Modelo.ResultadoBusqueda;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.stream.Collectors;

/** Vista de consola: recoge entradas y presenta datos devueltos por los controladores. */
public class MenuConsola {
    private final ControladorGrafo controladorGrafo;
    private final ControladorBusqueda controladorBusqueda;
    private final Scanner entrada;

    public MenuConsola(ControladorGrafo controladorGrafo, ControladorBusqueda controladorBusqueda) {
        if (controladorGrafo == null || controladorBusqueda == null) {
            throw new IllegalArgumentException("Los controladores son obligatorios.");
        }
        this.controladorGrafo = controladorGrafo;
        this.controladorBusqueda = controladorBusqueda;
        this.entrada = new Scanner(System.in);
    }

    public void iniciar() {
        System.out.println("MÉTODOS DE BÚSQUEDA EN IA");
        System.out.println("Grafo inicial:");
        System.out.println("1. Ejemplo de clase H → G (predeterminado)");
        System.out.println("2. Grafo vacío no dirigido");
        System.out.println("3. Grafo vacío dirigido");
        int opcionInicial = leerOpcionConDefecto("Seleccione una opción", 1, 3, 1);
        if (opcionInicial == 1) {
            controladorGrafo.cargarEjemploClase();
            System.out.println("Ejemplo cargado: H es el inicio y G el objetivo; cada conexión tiene peso 1.");
        } else {
            controladorGrafo.nuevoGrafo(opcionInicial == 3);
        }

        int opcion;
        do {
            mostrarMenu();
            opcion = leerOpcion("Seleccione una opción", 0, 12);
            try {
                ejecutarOpcion(opcion);
            } catch (IllegalArgumentException ex) {
                System.out.println("Entrada no válida: " + ex.getMessage());
            }
        } while (opcion != 0);
        System.out.println("Programa finalizado.");
    }

    private void mostrarMenu() {
        System.out.println("\n=========================================");
        System.out.println("       MÉTODOS DE BÚSQUEDA EN IA");
        System.out.println("=========================================");
        System.out.println("1. Crear nodo");
        System.out.println("2. Crear conexión");
        System.out.println("3. Eliminar nodo");
        System.out.println("4. Eliminar conexión");
        System.out.println("5. Mostrar grafo");
        System.out.println("6. Establecer nodo inicial");
        System.out.println("7. Establecer nodo objetivo");
        System.out.println("8. Asignar heurística");
        System.out.println("9. Ejecutar búsqueda");
        System.out.println("10. Comparar algoritmos");
        System.out.println("11. Cambiar peso de conexión");
        System.out.println("12. Información del grafo");
        System.out.println("0. Salir");
    }

    private void ejecutarOpcion(int opcion) {
        switch (opcion) {
            case 1: crearNodo(); break;
            case 2: crearConexion(); break;
            case 3: eliminarNodo(); break;
            case 4: eliminarConexion(); break;
            case 5: mostrarGrafo(); break;
            case 6: establecerInicio(); break;
            case 7: establecerObjetivo(); break;
            case 8: asignarHeuristica(); break;
            case 9: ejecutarBusqueda(); break;
            case 10: compararAlgoritmos(); break;
            case 11: actualizarPeso(); break;
            case 12: mostrarInformacion(); break;
            case 0: break;
            default: System.out.println("Opción no válida.");
        }
    }

    private void crearNodo() {
        String id = leerTexto("Id del nodo");
        if (controladorGrafo.agregarNodo(id, 0.0)) {
            System.out.println("Nodo creado.");
        } else {
            System.out.println("Ya existe un nodo con ese id.");
        }
    }

    private void crearConexion() {
        String origen = leerTexto("Nodo origen");
        String destino = leerTexto("Nodo destino");
        double peso = leerPeso();
        System.out.println(controladorGrafo.agregarConexion(origen, destino, peso)
                ? "Conexión creada." : "No se creó: revisa los nodos o si la conexión ya existe.");
    }

    private void eliminarNodo() {
        System.out.println(controladorGrafo.eliminarNodo(leerTexto("Id del nodo"))
                ? "Nodo eliminado." : "No existe ese nodo.");
    }

    private void eliminarConexion() {
        String origen = leerTexto("Nodo origen");
        String destino = leerTexto("Nodo destino");
        System.out.println(controladorGrafo.eliminarConexion(origen, destino)
                ? "Conexión eliminada." : "No existe esa conexión.");
    }

    private void establecerInicio() {
        System.out.println(controladorGrafo.establecerInicio(leerTexto("Id del nodo inicial"))
                ? "Nodo inicial establecido." : "No existe ese nodo.");
    }

    private void establecerObjetivo() {
        System.out.println(controladorGrafo.establecerObjetivo(leerTexto("Id del nodo objetivo"))
                ? "Nodo objetivo establecido." : "No existe ese nodo.");
    }

    private void asignarHeuristica() {
        String id = leerTexto("Id del nodo");
        double heuristica = leerDecimal("Heurística h(n)");
        System.out.println(controladorGrafo.asignarHeuristica(id, heuristica)
                ? "Heurística actualizada." : "No existe ese nodo.");
    }

    private void actualizarPeso() {
        String origen = leerTexto("Nodo origen");
        String destino = leerTexto("Nodo destino");
        double peso = leerDecimal("Nuevo peso (no negativo)");
        System.out.println(controladorGrafo.actualizarPesoConexion(origen, destino, peso)
                ? "Peso actualizado." : "No existe esa conexión.");
    }

    private void ejecutarBusqueda() {
        Nodo inicio = controladorGrafo.getInicio();
        Nodo objetivo = controladorGrafo.getObjetivo();
        if (!validarInicioObjetivo(inicio, objetivo)) return;

        String metodo = seleccionarMetodo();
        ResultadoBusqueda resultado = controladorBusqueda.buscar(metodo,
                controladorGrafo.getGrafo(), inicio, objetivo);
        System.out.println("\nMétodo: " + metodo);
        mostrarResultado(resultado);
    }

    private void compararAlgoritmos() {
        Nodo inicio = controladorGrafo.getInicio();
        Nodo objetivo = controladorGrafo.getObjetivo();
        if (!validarInicioObjetivo(inicio, objetivo)) return;

        Map<String, ResultadoBusqueda> resultados = controladorBusqueda.comparar(
                controladorGrafo.getGrafo(), inicio, objetivo);
        System.out.printf("%-22s %-12s %-14s %-12s%n", "Algoritmo", "Encontrado", "Costo", "Explorados");
        for (Map.Entry<String, ResultadoBusqueda> entrada : resultados.entrySet()) {
            ResultadoBusqueda resultado = entrada.getValue();
            String costo = resultado.isEncontrado()
                    ? String.format("%.2f", resultado.getCostoTotal()) : "—";
            System.out.printf("%-22s %-12s %-14s %-12d%n", entrada.getKey(),
                    resultado.isEncontrado() ? "Sí" : "No", costo, resultado.getNodosExplorados());
        }
    }

    private String seleccionarMetodo() {
        List<String> nombres = new ArrayList<>(controladorBusqueda.obtenerMetodos().keySet());
        System.out.println("Seleccione el método:");
        for (int i = 0; i < nombres.size(); i++) {
            System.out.println((i + 1) + ". " + nombres.get(i));
        }
        return nombres.get(leerOpcion("Método", 1, nombres.size()) - 1);
    }

    private void mostrarResultado(ResultadoBusqueda resultado) {
        System.out.println(resultado.getMensaje());
        System.out.println("Visitados/explorados: " + listaIds(resultado.getVisitados()));
        if (resultado.isEncontrado()) {
            System.out.println("Camino encontrado: " + listaIds(resultado.getCamino()));
            System.out.printf("Costo total: %.2f%n", resultado.getCostoTotal());
        } else {
            System.out.println("Camino: no encontrado");
        }
        System.out.println("Nodos explorados: " + resultado.getNodosExplorados());
        System.out.println("Análisis paso a paso:");
        if (!resultado.getPasos().isEmpty()) {
            PasoBusqueda primero = resultado.getPasos().get(0);
            if (primero.getLadoBusqueda() != PasoBusqueda.LadoBusqueda.UNICO) {
                System.out.println("Colas iniciales: inicio=[" + controladorGrafo.getInicio().getId()
                        + "], objetivo=[" + controladorGrafo.getObjetivo().getId() + "]");
            } else if (primero.getLimiteProfundidad() < 0) {
                System.out.println("Frontera inicial (" + primero.getTipoFrontera().name()
                        + "): [" + primero.getNodoExtraido().getId() + "]");
            }
        }
        int ultimoLimiteMostrado = -1;
        for (PasoBusqueda paso : resultado.getPasos()) {
            if (paso.getLimiteProfundidad() >= 0 && paso.getLimiteProfundidad() != ultimoLimiteMostrado) {
                System.out.println("Iteración con límite " + paso.getLimiteProfundidad()
                        + ": pila inicial [" + paso.getNodoExtraido().getId() + "]");
                ultimoLimiteMostrado = paso.getLimiteProfundidad();
            }
            String lado = "";
            if (paso.getLadoBusqueda() == PasoBusqueda.LadoBusqueda.INICIO) lado = " desde inicio";
            if (paso.getLadoBusqueda() == PasoBusqueda.LadoBusqueda.OBJETIVO) lado = " desde objetivo";
            System.out.printf("Paso %d%s: extrae %s", paso.getNumero(), lado,
                    paso.getNodoExtraido().getId());
            if (paso.getLimiteProfundidad() >= 0) {
                System.out.print(" (límite " + paso.getLimiteProfundidad() + ")");
            }
            System.out.printf(" | frontera %s: %s", paso.getTipoFrontera().name(),
                    listaIds(paso.getFrontera()));
            if (!paso.getFronteraOpuesta().isEmpty()
                    || paso.getLadoBusqueda() != PasoBusqueda.LadoBusqueda.UNICO) {
                System.out.print(" | frontera opuesta: " + listaIds(paso.getFronteraOpuesta()));
            }
            System.out.println(" | agregados: " + listaIds(paso.getNodosAgregados()));
        }
    }

    private void mostrarGrafo() {
        if (controladorGrafo.getGrafo().obtenerNodos().isEmpty()) {
            System.out.println("El grafo está vacío.");
            return;
        }
        for (Nodo nodo : controladorGrafo.getGrafo().obtenerNodos()) {
            System.out.printf("%s (h=%.2f):", nodo.getId(), nodo.getHeuristica());
            for (Conexion conexion : nodo.getConexiones()) {
                System.out.printf(" %s %s (%.2f)", controladorGrafo.getGrafo().isDirigido() ? "→" : "—",
                        conexion.getDestino().getId(), conexion.getPeso());
            }
            System.out.println();
        }
    }

    private void mostrarInformacion() {
        System.out.println("Tipo: " + (controladorGrafo.getGrafo().isDirigido() ? "dirigido" : "no dirigido"));
        System.out.println("Nodos: " + controladorGrafo.getGrafo().obtenerNodos().size());
        System.out.println("Inicio: " + (controladorGrafo.getInicio() == null ? "sin definir" : controladorGrafo.getInicio()));
        System.out.println("Objetivo: " + (controladorGrafo.getObjetivo() == null ? "sin definir" : controladorGrafo.getObjetivo()));
        mostrarGrafo();
    }

    private boolean validarInicioObjetivo(Nodo inicio, Nodo objetivo) {
        if (inicio == null || objetivo == null) {
            System.out.println("Establece un nodo inicial y uno objetivo que existan en el grafo.");
            return false;
        }
        return true;
    }

    private static String listaIds(List<Nodo> nodos) {
        return nodos.stream().map(Nodo::getId).collect(Collectors.toList()).toString();
    }

    private String leerTexto(String mensaje) {
        System.out.print(mensaje + ": ");
        return entrada.nextLine().trim();
    }

    private double leerDecimal(String mensaje) {
        while (true) {
            String valor = leerTexto(mensaje);
            try {
                return Double.parseDouble(valor);
            } catch (NumberFormatException ex) {
                System.out.println("Escribe un número válido.");
            }
        }
    }

    private double leerPeso() {
        while (true) {
            String valor = leerTexto("Peso (Enter = 1)");
            if (valor.isEmpty()) return 1.0;
            try {
                return Double.parseDouble(valor);
            } catch (NumberFormatException ex) {
                System.out.println("Escribe un número válido o deja vacío para usar peso 1.");
            }
        }
    }

    private int leerOpcion(String mensaje, int minimo, int maximo) {
        while (true) {
            String valor = leerTexto(mensaje);
            try {
                int opcion = Integer.parseInt(valor);
                if (opcion >= minimo && opcion <= maximo) return opcion;
            } catch (NumberFormatException ignored) {
                // Se vuelve a solicitar una opción entera dentro del rango.
            }
            System.out.println("Selecciona un número entre " + minimo + " y " + maximo + ".");
        }
    }

    private int leerOpcionConDefecto(String mensaje, int minimo, int maximo, int defecto) {
        while (true) {
            String valor = leerTexto(mensaje + " [" + defecto + "]");
            if (valor.isEmpty()) return defecto;
            try {
                int opcion = Integer.parseInt(valor);
                if (opcion >= minimo && opcion <= maximo) return opcion;
            } catch (NumberFormatException ignored) {
                // Se vuelve a solicitar una opción válida.
            }
            System.out.println("Selecciona un número entre " + minimo + " y " + maximo + ".");
        }
    }
}
