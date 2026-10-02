package Modelo.Busqueda;

import Modelo.Grafo;
import Modelo.Nodo;
import Modelo.ResultadoBusqueda;

/** Contrato común para los métodos de búsqueda del proyecto. */
public interface MetodoBusqueda {
    ResultadoBusqueda buscar(Grafo grafo, Nodo inicio, Nodo objetivo);
}
