package com.formula1.ui;

import com.formula1.model.*;
import com.formula1.service.PilotoService;
import com.formula1.service.VehiculoService;
import com.formula1.util.ConsoleUtils;

import java.util.List;
import java.util.Optional;

public class VehiculoMenu {
    private final VehiculoService vehiculoService;
    private final PilotoService pilotoService;

    public VehiculoMenu(VehiculoService vehiculoService, PilotoService pilotoService) {
        this.vehiculoService = vehiculoService;
        this.pilotoService = pilotoService;
    }

    public void mostrarMenu() {
        boolean salir = false;
        while (!salir) {
            ConsoleUtils.imprimirTitulo("Gestión de Vehículos y Rendimiento");
            System.out.println("1. 📋 Listar todos los vehículos y especificaciones");
            System.out.println("2. ⚖️  Comparar dos o más vehículos");
            System.out.println("3. 🔍 Ver telemetría y rendimiento detallado de un auto");
            System.out.println("4. 👤 Asignar piloto a un vehículo");
            System.out.println("5. 🔎 Buscar vehículos por motor o características");
            System.out.println("6. ➕ Registrar nuevo vehículo");
            System.out.println("7. ✏️  Editar especificaciones de vehículo");
            System.out.println("8. 🗑️  Eliminar vehículo");
            System.out.println("0. ⬅️  Volver al menú principal");

            int opcion = ConsoleUtils.leerEntero("\n👉 Seleccione una opción: ", 0, 8);
            switch (opcion) {
                case 1:
                    listarVehiculos();
                    break;
                case 2:
                    compararVehiculos();
                    break;
                case 3:
                    verDetalleVehiculo();
                    break;
                case 4:
                    asignarPiloto();
                    break;
                case 5:
                    buscarVehiculo();
                    break;
                case 6:
                    registrarVehiculo();
                    break;
                case 7:
                    editarVehiculo();
                    break;
                case 8:
                    eliminarVehiculo();
                    break;
                case 0:
                    salir = true;
                    break;
            }
        }
    }

    private void listarVehiculos() {
        ConsoleUtils.imprimirSubtitulo("Lista de Monoplazas de Fórmula 1");
        List<Vehiculo> vehiculos = vehiculoService.listarTodos();
        if (vehiculos.isEmpty()) {
            System.out.println("No hay vehículos registrados.");
        } else {
            System.out.println(String.format("%-10s | %-22s | %-10s | %-12s | %-12s | %-14s",
                    "Modelo", "Escudería", "Motor", "V. Máx", "0-100 km/h", "Pilotos IDs"));
            System.out.println("---------------------------------------------------------------------------------------------");
            for (Vehiculo v : vehiculos) {
                System.out.println(String.format("%-10s | %-22s | %-10s | %3d km/h    | %4.2f seg     | %-14s",
                        v.getModelo(), v.getEquipo(), v.getMotor(), v.getVelocidadMaximaKmh(), v.getAceleracion0a100(),
                        v.getPilotosIds().toString()));
            }
        }
        ConsoleUtils.pausar();
    }

    private void compararVehiculos() {
        ConsoleUtils.imprimirSubtitulo("Comparador de Rendimiento de Monoplazas");
        List<Vehiculo> lista = vehiculoService.listarTodos();
        if (lista.size() < 2) {
            System.out.println(ConsoleUtils.RED + "Se requieren al menos 2 vehículos para comparar." + ConsoleUtils.RESET);
            ConsoleUtils.pausar();
            return;
        }

        System.out.println("Vehículos disponibles:");
        for (int i = 0; i < lista.size(); i++) {
            System.out.println((i + 1) + ". " + lista.get(i).getModelo() + " (" + lista.get(i).getEquipo() + ")");
        }

        int idx1 = ConsoleUtils.leerEntero("\nSeleccione el primer vehículo (número): ", 1, lista.size()) - 1;
        int idx2 = ConsoleUtils.leerEntero("Seleccione el segundo vehículo (número): ", 1, lista.size()) - 1;

        Vehiculo v1 = lista.get(idx1);
        Vehiculo v2 = lista.get(idx2);

        System.out.println(ConsoleUtils.GREEN + "\n==========================================================================");
        System.out.println(String.format("  CARACTERÍSTICA                   | %-18s | %-18s", v1.getModelo(), v2.getModelo()));
        System.out.println("==========================================================================" + ConsoleUtils.RESET);
        System.out.println(String.format("  Escudería                        | %-18s | %-18s", v1.getEquipo(), v2.getEquipo()));
        System.out.println(String.format("  Motor                            | %-18s | %-18s", v1.getMotor(), v2.getMotor()));
        System.out.println(String.format("  Velocidad Máxima                 | %3d km/h           | %3d km/h", v1.getVelocidadMaximaKmh(), v2.getVelocidadMaximaKmh()));
        System.out.println(String.format("  Aceleración (0-100 km/h)         | %.2f seg           | %.2f seg", v1.getAceleracion0a100(), v2.getAceleracion0a100()));

        if (v1.getRendimiento() != null && v2.getRendimiento() != null) {
            System.out.println("--------------------------------------------------------------------------");
            System.out.println(ConsoleUtils.CYAN + "  [Modo Normal - Pista Seca]" + ConsoleUtils.RESET);
            System.out.println(String.format("  Velocidad Media                  | %.0f km/h           | %.0f km/h",
                    v1.getRendimiento().getConduccionNormal().getVelocidadPromedioKmh(),
                    v2.getRendimiento().getConduccionNormal().getVelocidadPromedioKmh()));
            System.out.println(String.format("  Consumo Combustible              | %.2f L/vta         | %.2f L/vta",
                    v1.getRendimiento().getConduccionNormal().getConsumo(Clima.SECO),
                    v2.getRendimiento().getConduccionNormal().getConsumo(Clima.SECO)));
            System.out.println(String.format("  Desgaste de Neumáticos           | %.2f %%/vta        | %.2f %%/vta",
                    v1.getRendimiento().getConduccionNormal().getDesgaste(Clima.SECO),
                    v2.getRendimiento().getConduccionNormal().getDesgaste(Clima.SECO)));

            System.out.println("--------------------------------------------------------------------------");
            System.out.println(ConsoleUtils.YELLOW + "  [Modo Agresivo - Pista Seca]" + ConsoleUtils.RESET);
            System.out.println(String.format("  Velocidad Media                  | %.0f km/h           | %.0f km/h",
                    v1.getRendimiento().getConduccionAgresiva().getVelocidadPromedioKmh(),
                    v2.getRendimiento().getConduccionAgresiva().getVelocidadPromedioKmh()));
            System.out.println(String.format("  Consumo Combustible              | %.2f L/vta         | %.2f L/vta",
                    v1.getRendimiento().getConduccionAgresiva().getConsumo(Clima.SECO),
                    v2.getRendimiento().getConduccionAgresiva().getConsumo(Clima.SECO)));
            System.out.println(String.format("  Desgaste de Neumáticos           | %.2f %%/vta        | %.2f %%/vta",
                    v1.getRendimiento().getConduccionAgresiva().getDesgaste(Clima.SECO),
                    v2.getRendimiento().getConduccionAgresiva().getDesgaste(Clima.SECO)));
        }
        System.out.println("==========================================================================");
        ConsoleUtils.pausar();
    }

    private void verDetalleVehiculo() {
        String modelo = ConsoleUtils.leerTexto("Modelo del vehículo (ej. RB20, W15): ");
        Optional<Vehiculo> op = vehiculoService.buscarPorModelo(modelo);
        if (op.isEmpty()) {
            System.out.println(ConsoleUtils.RED + "❌ Vehículo no encontrado." + ConsoleUtils.RESET);
            ConsoleUtils.pausar();
            return;
        }

        Vehiculo v = op.get();
        ConsoleUtils.imprimirSubtitulo("Telemetría y Rendimiento: " + v.getModelo());
        System.out.println("Escudería: " + v.getEquipo());
        System.out.println("Motor: " + v.getMotor());
        System.out.println("Velocidad Máxima: " + v.getVelocidadMaximaKmh() + " km/h");
        System.out.println("Aceleración 0-100: " + v.getAceleracion0a100() + " s");

        if (v.getRendimiento() != null) {
            System.out.println("\n📊 Rendimiento por Modos de Conducción:");
            for (ModoConduccion m : ModoConduccion.values()) {
                RendimientoConduccion rc = v.getRendimiento().getPorModo(m);
                System.out.println(ConsoleUtils.CYAN + "• Modo " + m.getNombre() + ":" + ConsoleUtils.RESET);
                System.out.println("   - Velocidad Promedio: " + rc.getVelocidadPromedioKmh() + " km/h");
                System.out.println(String.format("   - Consumo (Seco / Lluvia / Extremo): %.2f / %.2f / %.2f L",
                        rc.getConsumo(Clima.SECO), rc.getConsumo(Clima.LLUVIOSO), rc.getConsumo(Clima.EXTREMO)));
                System.out.println(String.format("   - Desgaste Neumáticos (Seco / Lluvia / Extremo): %.2f%% / %.2f%% / %.2f%%",
                        rc.getDesgaste(Clima.SECO), rc.getDesgaste(Clima.LLUVIOSO), rc.getDesgaste(Clima.EXTREMO)));
            }
        }
        ConsoleUtils.pausar();
    }

    private void asignarPiloto() {
        int pilotoId = ConsoleUtils.leerEntero("ID del piloto: ", 1, 9999);
        String modelo = ConsoleUtils.leerTexto("Modelo del auto (ej. RB20): ");
        if (vehiculoService.asignarPilotoAVehiculo(pilotoId, modelo)) {
            System.out.println(ConsoleUtils.GREEN + "✅ Piloto asignado exitosamente al monoplaza " + modelo + ConsoleUtils.RESET);
        } else {
            System.out.println(ConsoleUtils.RED + "❌ Error: verifique que el piloto y vehículo existan." + ConsoleUtils.RESET);
        }
        ConsoleUtils.pausar();
    }

    private void buscarVehiculo() {
        String termino = ConsoleUtils.leerTexto("Término a buscar (Equipo, Motor o Modelo): ");
        List<Vehiculo> encontrados = vehiculoService.buscarPorTermino(termino);
        if (encontrados.isEmpty()) {
            System.out.println(ConsoleUtils.RED + "❌ No se encontraron monoplazas." + ConsoleUtils.RESET);
        } else {
            System.out.println(ConsoleUtils.GREEN + "\nMonoplazas encontrados:" + ConsoleUtils.RESET);
            encontrados.forEach(System.out::println);
        }
        ConsoleUtils.pausar();
    }

    private void registrarVehiculo() {
        ConsoleUtils.imprimirSubtitulo("Registrar Nuevo Monoplaza");
        String modelo = ConsoleUtils.leerTexto("Modelo (ej. AMR24): ");
        String equipo = ConsoleUtils.leerTexto("Escudería: ");
        String motor = ConsoleUtils.leerTexto("Motor: ");
        int velMax = ConsoleUtils.leerEntero("Velocidad Máxima (km/h): ", 280, 400);
        double acel0a100 = ConsoleUtils.leerDouble("Aceleración 0-100 (segundos): ", 1.5, 5.0);

        RendimientoVehiculo rend = new RendimientoVehiculo(
                new RendimientoConduccion(velMax - 40, 2.0, 2.2, 2.5, 1.6, 0.9, 2.6),
                new RendimientoConduccion(velMax - 20, 2.5, 2.7, 3.1, 2.2, 1.3, 3.6),
                new RendimientoConduccion(velMax - 60, 1.7, 1.9, 2.2, 1.1, 0.6, 1.9)
        );

        String imagen = ConsoleUtils.leerTextoOpcional("URL de imagen", "https://formula1.com/car.png");
        vehiculoService.agregarVehiculo(equipo, modelo, motor, velMax, acel0a100, rend, imagen);
        System.out.println(ConsoleUtils.GREEN + "✅ Vehículo registrado exitosamente." + ConsoleUtils.RESET);
        ConsoleUtils.pausar();
    }

    private void editarVehiculo() {
        String modelo = ConsoleUtils.leerTexto("Modelo del vehículo a editar: ");
        Optional<Vehiculo> op = vehiculoService.buscarPorModelo(modelo);
        if (op.isEmpty()) {
            System.out.println(ConsoleUtils.RED + "❌ Vehículo no encontrado." + ConsoleUtils.RESET);
            ConsoleUtils.pausar();
            return;
        }

        Vehiculo v = op.get();
        String nuevoModelo = ConsoleUtils.leerTextoOpcional("Nuevo modelo", v.getModelo());
        String nuevoEquipo = ConsoleUtils.leerTextoOpcional("Nueva escudería", v.getEquipo());
        String nuevoMotor = ConsoleUtils.leerTextoOpcional("Nuevo motor", v.getMotor());
        int nuevaVel = ConsoleUtils.leerEntero("Nueva velocidad máx km/h [" + v.getVelocidadMaximaKmh() + "]: ", 280, 400);
        double nuevaAcel = ConsoleUtils.leerDouble("Nueva aceleración 0-100s [" + v.getAceleracion0a100() + "]: ", 1.5, 5.0);

        vehiculoService.actualizarVehiculo(modelo, nuevoModelo, nuevoEquipo, nuevoMotor, nuevaVel, nuevaAcel);
        System.out.println(ConsoleUtils.GREEN + "✅ Vehículo actualizado correctamente." + ConsoleUtils.RESET);
        ConsoleUtils.pausar();
    }

    private void eliminarVehiculo() {
        String modelo = ConsoleUtils.leerTexto("Modelo del vehículo a eliminar: ");
        if (vehiculoService.eliminarVehiculo(modelo)) {
            System.out.println(ConsoleUtils.GREEN + "✅ Vehículo eliminado correctamente." + ConsoleUtils.RESET);
        } else {
            System.out.println(ConsoleUtils.RED + "❌ Vehículo no encontrado." + ConsoleUtils.RESET);
        }
        ConsoleUtils.pausar();
    }
}
