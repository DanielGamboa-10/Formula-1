package com.formula1.ui;

import com.formula1.model.*;
import com.formula1.service.CircuitoService;
import com.formula1.service.ConfiguracionService;
import com.formula1.service.PilotoService;
import com.formula1.service.SimulacionService;
import com.formula1.service.VehiculoService;
import com.formula1.util.ConsoleUtils;

import java.util.List;

public class SimulacionMenu {
    private final SimulacionService simulacionService;
    private final CircuitoService circuitoService;
    private final PilotoService pilotoService;
    private final VehiculoService vehiculoService;
    private final ConfiguracionService configuracionService;

    public SimulacionMenu(SimulacionService simulacionService, CircuitoService circuitoService,
                          PilotoService pilotoService, VehiculoService vehiculoService,
                          ConfiguracionService configuracionService) {
        this.simulacionService = simulacionService;
        this.circuitoService = circuitoService;
        this.pilotoService = pilotoService;
        this.vehiculoService = vehiculoService;
        this.configuracionService = configuracionService;
    }

    public void iniciarSimulacionCompleta() {
        ConsoleUtils.imprimirTitulo("Simulador de Sesión de Clasificación F1");

        // 1. Seleccionar Circuito
        List<Circuito> circuitos = circuitoService.listarTodos();
        if (circuitos.isEmpty()) {
            System.out.println(ConsoleUtils.RED + "❌ No hay circuitos disponibles para la simulación." + ConsoleUtils.RESET);
            ConsoleUtils.pausar();
            return;
        }

        ConsoleUtils.imprimirSubtitulo("Paso 1: Seleccione el Circuito");
        for (int i = 0; i < circuitos.size(); i++) {
            Circuito c = circuitos.get(i);
            System.out.println((i + 1) + ". " + c.getNombre() + " (" + c.getPais() + ") - Longitud: " + c.getLongitudKm() + " km");
        }
        int idxCircuito = ConsoleUtils.leerEntero("👉 Seleccione circuito (1-" + circuitos.size() + "): ", 1, circuitos.size()) - 1;
        Circuito circuitoElegido = circuitos.get(idxCircuito);

        // 2. Seleccionar Piloto del Usuario
        List<Piloto> pilotos = pilotoService.listarTodos();
        ConsoleUtils.imprimirSubtitulo("Paso 2: Seleccione su Piloto");
        for (int i = 0; i < pilotos.size(); i++) {
            Piloto p = pilotos.get(i);
            System.out.println(String.format("%2d. %-20s | Equipo: %-22s | Rol: %-8s", (i + 1), p.getNombre(), p.getEquipo(), p.getRol()));
        }
        int idxPiloto = ConsoleUtils.leerEntero("👉 Seleccione piloto (1-" + pilotos.size() + "): ", 1, pilotos.size()) - 1;
        Piloto pilotoUsuario = pilotos.get(idxPiloto);

        // 3. Seleccionar Monoplaza
        List<Vehiculo> vehiculos = vehiculoService.listarTodos();
        ConsoleUtils.imprimirSubtitulo("Paso 3: Seleccione su Monoplaza");
        for (int i = 0; i < vehiculos.size(); i++) {
            Vehiculo v = vehiculos.get(i);
            System.out.println((i + 1) + ". " + v.getModelo() + " (" + v.getEquipo() + ") - Motor: " + v.getMotor());
        }
        int idxVehiculo = ConsoleUtils.leerEntero("👉 Seleccione monoplaza (1-" + vehiculos.size() + "): ", 1, vehiculos.size()) - 1;
        Vehiculo vehiculoUsuario = vehiculos.get(idxVehiculo);

        // 4. Clima: Aleatorio o Manual
        ConsoleUtils.imprimirSubtitulo("Paso 4: Condiciones Climáticas");
        System.out.println("1. 🎲 Clima Aleatorio (Generado por el sistema meteorológico)");
        System.out.println("2. ☀️ Seco (Óptima adherencia)");
        System.out.println("3. 🌧️ Lluvioso (Pista mojada)");
        System.out.println("4. ⛈️ Extremo (Condiciones de lluvia torrencial)");
        int opcionClima = ConsoleUtils.leerEntero("👉 Opción (1-4): ", 1, 4);
        Clima climaElegido;
        if (opcionClima == 1) {
            climaElegido = simulacionService.generarClimaAleatorio();
            System.out.println(ConsoleUtils.YELLOW + "🌦️ Clima determinado por radar: " + climaElegido.getNombre() + " (" + climaElegido.getDescripcion() + ")" + ConsoleUtils.RESET);
        } else if (opcionClima == 2) {
            climaElegido = Clima.SECO;
        } else if (opcionClima == 3) {
            climaElegido = Clima.LLUVIOSO;
        } else {
            climaElegido = Clima.EXTREMO;
        }

        // 5. Configuración del vehículo
        ConfiguracionVehiculo configUsuario = configuracionService.getConfiguracionActual();
        System.out.println(ConsoleUtils.CYAN + "\n⚙️ Configuración del Monoplaza seleccionada:" + ConsoleUtils.RESET);
        System.out.println("   " + configUsuario);

        System.out.print(ConsoleUtils.GREEN + "\n🚦 Presione ENTER para encender los motores y dar inicio a la Clasificación..." + ConsoleUtils.RESET);
        ConsoleUtils.leerTextoOpcional("", "");

        // 6. Ejecutar simulación
        System.out.println("\n🟢 Pista Abierta: Los monoplazas están completando sus vueltas lanzadas...");
        try {
            Thread.sleep(800);
        } catch (InterruptedException ignored) {}

        SesionClasificacion sesion = simulacionService.simularClasificacion(
                circuitoElegido, climaElegido, pilotoUsuario, vehiculoUsuario, configUsuario
        );

        // 7. Mostrar Resultados de la Clasificación
        mostrarResultadosClasificacion(sesion);
    }

    private void mostrarResultadosClasificacion(SesionClasificacion sesion) {
        ConsoleUtils.imprimirTitulo("Resultados Oficiales de Clasificación");
        System.out.println("📍 Circuito: " + sesion.getCircuito().getNombre() + " (" + sesion.getCircuito().getPais() + ")");
        System.out.println("🌦️ Clima: " + sesion.getClima().getNombre() + " | Fecha/Hora: " + sesion.getFechaHoraFormateada());
        if (sesion.getCircuito().getRecordVuelta() != null) {
            System.out.println("⏱️ Récord de Pista: " + sesion.getCircuito().getRecordVuelta().getTiempo() + " (" + sesion.getCircuito().getRecordVuelta().getPiloto() + ")");
        }
        System.out.println("-------------------------------------------------------------------------------------------------------------");
        System.out.println(String.format("%-4s | %-20s | %-22s | %-8s | %-10s | %-9s | %-10s | %-8s",
                "POS", "PILOTO", "ESCUDERÍA", "AUTO", "TIEMPO", "DIFERENCIA", "VEL. MEDIA", "DESGASTE"));
        System.out.println("=============================================================================================================");

        for (ResultadoVuelta r : sesion.getResultados()) {
            String dif = (r.getPosicion() == 1) ? "POLE" : String.format("+%6.3fs", r.getDiferenciaConLiderSegundos());
            String color = ConsoleUtils.RESET;

            if (r.getPosicion() == 1) {
                color = ConsoleUtils.YELLOW + ConsoleUtils.BOLD; // Pole position
            } else if (r.isEsUsuario()) {
                color = ConsoleUtils.GREEN + ConsoleUtils.BOLD;  // Tu coche
            }

            String autoModelo = (r.getVehiculo() != null) ? r.getVehiculo().getModelo() : "F1-Car";
            System.out.println(color + String.format("P%-3d | %-20s | %-22s | %-8s | %-10s | %-9s | %5.1f km/h | %4.1f%%",
                    r.getPosicion(), r.getPiloto().getNombre(), r.getPiloto().getEquipo(),
                    autoModelo, r.getTiempoFormateado(), dif, r.getVelocidadMediaKmh(), r.getDesgasteNeumaticosEstimado()) + ConsoleUtils.RESET);
        }

        System.out.println("=============================================================================================================");
        if (sesion.getPolePosition() != null) {
            System.out.println(ConsoleUtils.YELLOW + ConsoleUtils.BOLD + "🏆 POLE POSITION: " +
                    sesion.getPolePosition().getNombre() + " (" + sesion.getPolePosition().getEquipo() + ") con un tiempo de " +
                    sesion.getResultados().get(0).getTiempoFormateado() + ConsoleUtils.RESET);
        }

        ResultadoVuelta userRes = sesion.getResultadoUsuario();
        if (userRes != null) {
            System.out.println(ConsoleUtils.GREEN + "🎯 Tu Posición de Partida: P" + userRes.getPosicion() + " | Tiempo: " +
                    userRes.getTiempoFormateado() + " (Dif: +" + String.format("%.3fs", userRes.getDiferenciaConLiderSegundos()) + ")" + ConsoleUtils.RESET);
        }

        System.out.println(ConsoleUtils.CYAN + "💾 Sesión guardada automáticamente en el historial de clasificaciones." + ConsoleUtils.RESET);
        ConsoleUtils.pausar();
    }
}
