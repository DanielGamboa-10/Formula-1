package com.formula1.ui;

import com.formula1.service.*;
import com.formula1.util.ConsoleUtils;

public class MenuPrincipal {
    private final PilotoMenu pilotoMenu;
    private final EquipoMenu equipoMenu;
    private final CircuitoMenu circuitoMenu;
    private final VehiculoMenu vehiculoMenu;
    private final ConfiguracionMenu configuracionMenu;
    private final SimulacionMenu simulacionMenu;
    private final HistorialMenu historialMenu;

    public MenuPrincipal() {
        PilotoService pilotoService = new PilotoService();
        EquipoService equipoService = new EquipoService();
        CircuitoService circuitoService = new CircuitoService();
        VehiculoService vehiculoService = new VehiculoService();
        ConfiguracionService configuracionService = new ConfiguracionService();
        SimulacionService simulacionService = new SimulacionService();
        EstadisticaService estadisticaService = new EstadisticaService();

        this.pilotoMenu = new PilotoMenu(pilotoService);
        this.equipoMenu = new EquipoMenu(equipoService);
        this.circuitoMenu = new CircuitoMenu(circuitoService);
        this.vehiculoMenu = new VehiculoMenu(vehiculoService, pilotoService);
        this.configuracionMenu = new ConfiguracionMenu(configuracionService);
        this.simulacionMenu = new SimulacionMenu(simulacionService, circuitoService, pilotoService, vehiculoService, configuracionService);
        this.historialMenu = new HistorialMenu(estadisticaService);
    }

    public void iniciar() {
        boolean salir = false;
        while (!salir) {
            ConsoleUtils.imprimirTitulo("SISTEMA DE SIMULACIÓN Y GESTIÓN DE FÓRMULA 1");
            System.out.println(ConsoleUtils.WHITE + BOLD_TEXT("🏁 PANEL PRINCIPAL DE CONTROL FIA / F1 🏁\n") + ConsoleUtils.RESET);
            System.out.println("1. 🏎️  Gestión de Pilotos (CRUD, roles y estadísticas)");
            System.out.println("2. 🏁 Gestión de Equipos y Escuderías (CRUD y motores)");
            System.out.println("3. 🗺️  Gestión de Circuitos (CRUD, récords, clima e historial de ganadores)");
            System.out.println("4. 🚗 Gestión de Vehículos (CRUD, telemetría y comparador)");
            System.out.println("5. ⚙️  Configuración del Monoplaza (Modo de conducción, aero, neumáticos, combustible)");
            System.out.println(ConsoleUtils.YELLOW + "6. 🚦 SIMULACIÓN DE CLASIFICACIÓN (Vuelta rápida, clima y Pole Position)" + ConsoleUtils.RESET);
            System.out.println("7. 📊 Historial y Comparativa de Tiempos por Circuito");
            System.out.println("0. 🚪 Salir del Sistema");

            int opcion = ConsoleUtils.leerEntero("\n👉 Seleccione una opción: ", 0, 7);
            switch (opcion) {
                case 1:
                    pilotoMenu.mostrarMenu();
                    break;
                case 2:
                    equipoMenu.mostrarMenu();
                    break;
                case 3:
                    circuitoMenu.mostrarMenu();
                    break;
                case 4:
                    vehiculoMenu.mostrarMenu();
                    break;
                case 5:
                    configuracionMenu.mostrarMenu();
                    break;
                case 6:
                    simulacionMenu.iniciarSimulacionCompleta();
                    break;
                case 7:
                    historialMenu.mostrarMenu();
                    break;
                case 0:
                    System.out.println(ConsoleUtils.GREEN + "\n¡Gracias por utilizar el Simulador de Fórmula 1! Hasta la próxima carrera. 🏁" + ConsoleUtils.RESET);
                    salir = true;
                    break;
            }
        }
    }

    private String BOLD_TEXT(String str) {
        return ConsoleUtils.BOLD + str + ConsoleUtils.RESET;
    }
}
