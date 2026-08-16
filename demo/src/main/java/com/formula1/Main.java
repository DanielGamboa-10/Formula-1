package com.formula1;

import com.formula1.data.DataLoader;
import com.formula1.ui.MenuPrincipal;

public class Main {
    public static void main(String[] args) {
        // Carga de datos iniciales en memoria (HashMap / Map)
        DataLoader.cargarDatosIniciales();

        // Lanzamiento del Menú Principal
        MenuPrincipal menu = new MenuPrincipal();
        menu.iniciar();
    }
}
