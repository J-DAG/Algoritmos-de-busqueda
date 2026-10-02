import Controlador.ControladorBusqueda;
import Controlador.ControladorGrafo;
import Vista.MenuConsola;

public class Main {
    public static void main(String[] args) {
        ControladorGrafo controladorGrafo = new ControladorGrafo();
        ControladorBusqueda controladorBusqueda = new ControladorBusqueda();
        new MenuConsola(controladorGrafo, controladorBusqueda).iniciar();
    }
}
