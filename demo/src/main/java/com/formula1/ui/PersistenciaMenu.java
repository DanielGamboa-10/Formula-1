package com.formula1.ui;

import com.formula1.data.DataPersistenceManager;
import com.formula1.data.DataStore;
import com.formula1.util.ConsoleUtils;

import java.util.Map;

/**
 * Menú de consola para gestionar la persistencia y copias de seguridad de datos.
 */
public class PersistenciaMenu {
    private final DataStore store;

    public PersistenciaMenu() {
        this.store = DataStore.getInstance();
    }

    public void mostrarMenu() {
        boolean volver = false;
        while (!volver) {
            ConsoleUtils.imprimirTitulo("GESTIÓN DE PERSISTENCIA Y ALMACENAMIENTO EN DISCO");
            System.out.println("1. 💾 Guardar datos actuales en disco");
            System.out.println("2. 📂 Recargar datos desde disco");
            System.out.println("3. ⚠️  Restaurar datos iniciales de fábrica");
            System.out.println("4. 📋 Ver información y estadísticas del archivo en disco");
            System.out.println("5. ⚙️  Alternar auto-guardado (Auto-Save: " + (store.isAutoSave() ? "ACTIVADO" : "DESACTIVADO") + ")");
            System.out.println("0. ⬅️  Volver al Menú Principal");

            int op = ConsoleUtils.leerEntero("\n👉 Seleccione una opción: ", 0, 5);
            switch (op) {
                case 1:
                    guardarDatos();
                    break;
                case 2:
                    recargarDatos();
                    break;
                case 3:
                    restaurarFabrica();
                    break;
                case 4:
                    mostrarEstadisticas();
                    break;
                case 5:
                    store.setAutoSave(!store.isAutoSave());
                    System.out.println(ConsoleUtils.GREEN + "\n✅ Auto-guardado configurado en: " + (store.isAutoSave() ? "ACTIVADO" : "DESACTIVADO") + ConsoleUtils.RESET);
                    ConsoleUtils.pausar();
                    break;
                case 0:
                    volver = true;
                    break;
            }
        }
    }

    private void guardarDatos() {
        System.out.println("\n💾 Guardando estado completo en disco...");
        boolean ok = store.guardar();
        if (ok) {
            System.out.println(ConsoleUtils.GREEN + "✅ ¡Datos guardados exitosamente en: " + DataPersistenceManager.getRutaArchivo() + ConsoleUtils.RESET);
        } else {
            System.out.println(ConsoleUtils.RED + "❌ Error al guardar datos en disco." + ConsoleUtils.RESET);
        }
        ConsoleUtils.pausar();
    }

    private void recargarDatos() {
        System.out.println(ConsoleUtils.YELLOW + "\n⚠️  Se recargarán los datos guardados en disco. Los cambios sin guardar se descartarán." + ConsoleUtils.RESET);
        String conf = ConsoleUtils.leerTexto("¿Desea continuar? (S/N): ");
        if (conf.equalsIgnoreCase("S")) {
            boolean ok = store.cargar();
            if (ok) {
                System.out.println(ConsoleUtils.GREEN + "✅ ¡Datos recargados exitosamente desde disco!" + ConsoleUtils.RESET);
            } else {
                System.out.println(ConsoleUtils.RED + "❌ No se pudo cargar el archivo persistido o no existe." + ConsoleUtils.RESET);
            }
        }
        ConsoleUtils.pausar();
    }

    private void restaurarFabrica() {
        System.out.println(ConsoleUtils.RED + "\n⚠️  ¡ADVERTENCIA! Se borrarán todos los cambios y simulaciones para restaurar los valores iniciales de fábrica." + ConsoleUtils.RESET);
        String conf = ConsoleUtils.leerTexto("Escriba 'RESTAURAR' para confirmar: ");
        if (conf.equalsIgnoreCase("RESTAURAR")) {
            boolean ok = store.restaurarPorDefecto();
            if (ok) {
                System.out.println(ConsoleUtils.GREEN + "✅ ¡Se restauraron todos los datos oficiales originales de Fórmula 1!" + ConsoleUtils.RESET);
            } else {
                System.out.println(ConsoleUtils.RED + "❌ Error al restaurar datos iniciales." + ConsoleUtils.RESET);
            }
        } else {
            System.out.println(ConsoleUtils.YELLOW + "Operación cancelada." + ConsoleUtils.RESET);
        }
        ConsoleUtils.pausar();
    }

    private void mostrarEstadisticas() {
        Map<String, String> meta = DataPersistenceManager.obtenerMetadatos();
        ConsoleUtils.imprimirTitulo("ESTADÍSTICAS DEL ALMACENAMIENTO PERSISTENTE");
        System.out.println("📁 Ruta de archivo:        " + meta.getOrDefault("ruta", "-"));
        System.out.println("📦 Archivo existente:     " + ("true".equalsIgnoreCase(meta.get("existe")) ? ConsoleUtils.GREEN + "SÍ (Sincronizado)" : ConsoleUtils.YELLOW + "NO (Pendiente)") + ConsoleUtils.RESET);
        System.out.println("💾 Tamaño en disco:        " + meta.getOrDefault("tamano", "-"));
        System.out.println("🕒 Última modificación:    " + meta.getOrDefault("ultimaModificacion", "-"));
        System.out.println("⚙️ Auto-guardado (AutoSave):" + (store.isAutoSave() ? ConsoleUtils.GREEN + "ACTIVADO" : ConsoleUtils.RED + "DESACTIVADO") + ConsoleUtils.RESET);
        System.out.println("\n--- Resumen de Entidades Registradas ---");
        System.out.println("🏎️ Pilotos FIA:            " + meta.getOrDefault("pilotos", "0"));
        System.out.println("🏁 Escuderías F1:          " + meta.getOrDefault("equipos", "0"));
        System.out.println("🗺️ Circuitos:              " + meta.getOrDefault("circuitos", "0"));
        System.out.println("🚗 Monoplazas:             " + meta.getOrDefault("vehiculos", "0"));
        System.out.println("⚙️ Setups Guardados:       " + meta.getOrDefault("configuraciones", "0"));
        System.out.println("📊 Sesiones Clasificación: " + meta.getOrDefault("historial", "0"));
        ConsoleUtils.pausar();
    }
}
