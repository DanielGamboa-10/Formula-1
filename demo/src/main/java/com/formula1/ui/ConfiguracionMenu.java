package com.formula1.ui;

import com.formula1.model.*;
import com.formula1.service.ConfiguracionService;
import com.formula1.util.ConsoleUtils;

import java.util.Map;
import java.util.Optional;

public class ConfiguracionMenu {
    private final ConfiguracionService configuracionService;

    public ConfiguracionMenu(ConfiguracionService configuracionService) {
        this.configuracionService = configuracionService;
    }

    public void mostrarMenu() {
        boolean salir = false;
        while (!salir) {
            ConfiguracionVehiculo actual = configuracionService.getConfiguracionActual();
            ConsoleUtils.imprimirTitulo("Configuración y Reglajes del Monoplaza");
            System.out.println(ConsoleUtils.CYAN + "⚙️ Configuración Activa: " + actual + ConsoleUtils.RESET);
            System.out.println(String.format("   Impacto en tiempo: %+5.2fs | Impacto consumo: x%.2f | Impacto desgaste: x%.2f\n",
                    actual.getImpactoTotalTiempoSegundos(), actual.getImpactoTotalConsumo(), actual.getImpactoTotalDesgaste()));

            System.out.println("1. 🏎️  Cambiar Modo de Conducción (Normal / Agresivo / Ahorro)");
            System.out.println("2. ✈️  Ajustar Carga Aerodinámica (Baja / Media / Alta)");
            System.out.println("3. 🛞  Ajustar Presión de Neumáticos (Baja / Estándar / Alta)");
            System.out.println("4. ⛽  Seleccionar Estrategia de Combustible (Agresiva / Balanceada / Ahorro)");
            System.out.println("5. 💾 Guardar configuración actual con un nombre personalizado");
            System.out.println("6. 📂 Cargar configuración guardada");
            System.out.println("7. 📋 Listar todas las configuraciones guardadas");
            System.out.println("0. ⬅️  Volver al menú principal");

            int opcion = ConsoleUtils.leerEntero("\n👉 Seleccione una opción: ", 0, 7);
            switch (opcion) {
                case 1:
                    cambiarModoConduccion();
                    break;
                case 2:
                    cambiarCargaAero();
                    break;
                case 3:
                    cambiarPresionNeumaticos();
                    break;
                case 4:
                    cambiarEstrategiaCombustible();
                    break;
                case 5:
                    guardarConfiguracion();
                    break;
                case 6:
                    cargarConfiguracion();
                    break;
                case 7:
                    listarConfiguraciones();
                    break;
                case 0:
                    salir = true;
                    break;
            }
        }
    }

    private void cambiarModoConduccion() {
        ConsoleUtils.imprimirSubtitulo("Modo de Conducción");
        System.out.println("1. Normal (" + ModoConduccion.NORMAL.getDescripcion() + ")");
        System.out.println("2. Agresivo (" + ModoConduccion.AGRESIVO.getDescripcion() + ")");
        System.out.println("3. Ahorro (" + ModoConduccion.AHORRO.getDescripcion() + ")");
        int sel = ConsoleUtils.leerEntero("Seleccione modo (1-3): ", 1, 3);
        ModoConduccion modo = (sel == 1) ? ModoConduccion.NORMAL : (sel == 2 ? ModoConduccion.AGRESIVO : ModoConduccion.AHORRO);
        configuracionService.getConfiguracionActual().setModoConduccion(modo);
        System.out.println(ConsoleUtils.GREEN + "✅ Modo de conducción actualizado a: " + modo.getNombre() + ConsoleUtils.RESET);
        ConsoleUtils.pausar();
    }

    private void cambiarCargaAero() {
        ConsoleUtils.imprimirSubtitulo("Carga Aerodinámica");
        System.out.println("1. Baja (" + CargaAerodinamica.BAJA.getDescripcion() + ")");
        System.out.println("2. Media (" + CargaAerodinamica.MEDIA.getDescripcion() + ")");
        System.out.println("3. Alta (" + CargaAerodinamica.ALTA.getDescripcion() + ")");
        int sel = ConsoleUtils.leerEntero("Seleccione carga (1-3): ", 1, 3);
        CargaAerodinamica aero = (sel == 1) ? CargaAerodinamica.BAJA : (sel == 2 ? CargaAerodinamica.MEDIA : CargaAerodinamica.ALTA);
        configuracionService.getConfiguracionActual().setCargaAerodinamica(aero);
        System.out.println(ConsoleUtils.GREEN + "✅ Carga aerodinámica actualizada a: " + aero.getNombre() + ConsoleUtils.RESET);
        ConsoleUtils.pausar();
    }

    private void cambiarPresionNeumaticos() {
        ConsoleUtils.imprimirSubtitulo("Presión de Neumáticos");
        System.out.println("1. Baja (" + PresionNeumaticos.BAJA.getDescripcion() + ")");
        System.out.println("2. Estándar (" + PresionNeumaticos.ESTANDAR.getDescripcion() + ")");
        System.out.println("3. Alta (" + PresionNeumaticos.ALTA.getDescripcion() + ")");
        int sel = ConsoleUtils.leerEntero("Seleccione presión (1-3): ", 1, 3);
        PresionNeumaticos presion = (sel == 1) ? PresionNeumaticos.BAJA : (sel == 2 ? PresionNeumaticos.ESTANDAR : PresionNeumaticos.ALTA);
        configuracionService.getConfiguracionActual().setPresionNeumaticos(presion);
        System.out.println(ConsoleUtils.GREEN + "✅ Presión de neumáticos actualizada a: " + presion.getNombre() + ConsoleUtils.RESET);
        ConsoleUtils.pausar();
    }

    private void cambiarEstrategiaCombustible() {
        ConsoleUtils.imprimirSubtitulo("Estrategia de Combustible");
        System.out.println("1. Agresiva (" + EstrategiaCombustible.AGRESIVA.getDescripcion() + ")");
        System.out.println("2. Balanceada (" + EstrategiaCombustible.BALANCEADA.getDescripcion() + ")");
        System.out.println("3. Ahorro (" + EstrategiaCombustible.AHORRO.getDescripcion() + ")");
        int sel = ConsoleUtils.leerEntero("Seleccione estrategia (1-3): ", 1, 3);
        EstrategiaCombustible est = (sel == 1) ? EstrategiaCombustible.AGRESIVA : (sel == 2 ? EstrategiaCombustible.BALANCEADA : EstrategiaCombustible.AHORRO);
        configuracionService.getConfiguracionActual().setEstrategiaCombustible(est);
        System.out.println(ConsoleUtils.GREEN + "✅ Estrategia de combustible actualizada a: " + est.getNombre() + ConsoleUtils.RESET);
        ConsoleUtils.pausar();
    }

    private void guardarConfiguracion() {
        String nombre = ConsoleUtils.leerTexto("Ingrese un nombre para guardar esta configuración (ej. Mónaco Clasif): ");
        configuracionService.guardarConfiguracion(nombre, configuracionService.getConfiguracionActual());
        System.out.println(ConsoleUtils.GREEN + "✅ Configuración '" + nombre + "' guardada con éxito." + ConsoleUtils.RESET);
        ConsoleUtils.pausar();
    }

    private void cargarConfiguracion() {
        listarConfiguraciones();
        String nombre = ConsoleUtils.leerTexto("Nombre de la configuración a cargar: ");
        Optional<ConfiguracionVehiculo> op = configuracionService.cargarConfiguracion(nombre);
        if (op.isPresent()) {
            configuracionService.setConfiguracionActual(op.get());
            System.out.println(ConsoleUtils.GREEN + "✅ Configuración '" + nombre + "' cargada y activada." + ConsoleUtils.RESET);
        } else {
            System.out.println(ConsoleUtils.RED + "❌ Configuración no encontrada." + ConsoleUtils.RESET);
        }
        ConsoleUtils.pausar();
    }

    private void listarConfiguraciones() {
        ConsoleUtils.imprimirSubtitulo("Configuraciones Guardadas");
        Map<String, ConfiguracionVehiculo> configs = configuracionService.listarConfiguracionesGuardadas();
        if (configs.isEmpty()) {
            System.out.println("No hay configuraciones personalizadas guardadas.");
        } else {
            configs.forEach((nombre, cfg) -> {
                System.out.println("🔹 [" + nombre + "] -> " + cfg);
            });
        }
    }
}
