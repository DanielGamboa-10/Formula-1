package com.formula1;

import com.formula1.data.DataLoader;
import com.formula1.data.DataPersistenceManager;
import com.formula1.data.DataStore;
import com.formula1.model.*;
import com.formula1.service.SimulacionService;

import java.io.File;
import java.util.List;

public class PersistenciaTest {
    private static final String TEST_STORAGE_PATH = "data" + File.separator + "test_f1_datastore.dat";

    public static void main(String[] args) {
        System.out.println("💾 Iniciando batería de pruebas unitarias para Persistencia de Datos...");

        DataPersistenceManager.setRutaArchivo(TEST_STORAGE_PATH);
        DataStore store = DataStore.getInstance();

        try {
            // Prueba 1: Guardar estado base y recargar en memoria limpia
            testGuardadoYRecargaBase(store);

            // Prueba 2: Persistencia tras mutaciones CRUD y simulaciones
            testPersistenciaMutaciones(store);

            // Prueba 3: Restauración a valores originales de fábrica
            testRestaurarFabrica(store);

            System.out.println("\n🎉 TODAS LAS PRUEBAS DE PERSISTENCIA (3/3) PASARON EXITOSAMENTE.");
        } finally {
            // Limpieza del archivo de test
            File testFile = new File(TEST_STORAGE_PATH);
            if (testFile.exists()) {
                testFile.delete();
            }
            File tempFile = new File(TEST_STORAGE_PATH + ".tmp");
            if (tempFile.exists()) {
                tempFile.delete();
            }
            // Restaurar ruta predeterminada
            DataPersistenceManager.setRutaArchivo(DataPersistenceManager.DEFAULT_FILE_PATH);
        }
    }

    private static void testGuardadoYRecargaBase(DataStore store) {
        System.out.println("--- Test 1: Guardado de baseline y recarga en memoria limpia ---");
        store.limpiar();
        DataLoader.cargarDatosIniciales();

        int pilotosOriginales = store.getPilotos().size();
        int equiposOriginales = store.getEquipos().size();
        int circuitosOriginales = store.getCircuitos().size();
        int vehiculosOriginales = store.getVehiculos().size();

        assertCondition(pilotosOriginales == 20, "Debe haber 20 pilotos cargados inicialmente.");
        assertCondition(circuitosOriginales == 7, "Debe haber 7 circuitos cargados.");

        boolean guardadoOk = store.guardar();
        assertCondition(guardadoOk, "El guardado en disco debe ser exitoso.");
        assertCondition(DataPersistenceManager.existeArchivoPersistencia(), "El archivo de persistencia debe existir físicamente.");

        // Limpiar memoria
        store.limpiar();
        assertCondition(store.getPilotos().isEmpty(), "La memoria debe estar vacía tras limpiar.");

        // Recargar desde disco
        boolean cargadoOk = store.cargar();
        assertCondition(cargadoOk, "La recarga desde disco debe ser exitosa.");
        assertCondition(store.getPilotos().size() == pilotosOriginales, "Deben haberse restaurado todos los pilotos.");
        assertCondition(store.getEquipos().size() == equiposOriginales, "Deben haberse restaurado todas las escuderías.");
        assertCondition(store.getCircuitos().size() == circuitosOriginales, "Deben haberse restaurado todos los circuitos.");
        assertCondition(store.getVehiculos().size() == vehiculosOriginales, "Deben haberse restaurado todos los monoplazas.");

        System.out.println("✅ Test 1 Superado: Serialización y deserialización de baseline verificada.");
    }

    private static void testPersistenciaMutaciones(DataStore store) {
        System.out.println("--- Test 2: Persistencia de mutaciones (CRUD y Simulaciones) ---");

        // 1. Agregar nuevo piloto
        int nuevoId = 999;
        Piloto pilotoCustom = new Piloto(nuevoId, "Piloto Persistente Test", "Ferrari", "Líder", 99, 99);
        store.getPilotos().put(nuevoId, pilotoCustom);

        // 2. Simular una clasificación para generar historial
        SimulacionService simulacionService = new SimulacionService();
        Circuito monza = store.getCircuitos().get("Circuito de Monza");
        Vehiculo f175 = store.getVehiculos().get("SF-24");
        ConfiguracionVehiculo setup = new ConfiguracionVehiculo(ModoConduccion.AGRESIVO, CargaAerodinamica.BAJA, PresionNeumaticos.BAJA, EstrategiaCombustible.AGRESIVA);

        SesionClasificacion sesion = simulacionService.simularClasificacion(monza, Clima.SECO, pilotoCustom, f175, setup);
        assertCondition(sesion != null, "La sesión simulada no debe ser nula.");
        assertCondition(!store.getHistorialClasificaciones().isEmpty(), "El historial debe contener la sesión.");

        int totalHistorialAntes = store.getHistorialClasificaciones().size();

        // 3. Forzar guardado
        store.guardar();

        // 4. Limpiar memoria y recargar
        store.limpiar();
        assertCondition(store.getPilotos().isEmpty(), "La memoria debe limpiarse.");

        store.cargar();

        // 5. Verificar que los datos modificados e historial persistieron
        assertCondition(store.getPilotos().containsKey(nuevoId), "El piloto custom (#999) debe persistir tras recargar de disco.");
        Piloto pilotoRecuperado = store.getPilotos().get(nuevoId);
        assertCondition("Piloto Persistente Test".equals(pilotoRecuperado.getNombre()), "El nombre del piloto recuperado debe coincidir.");
        assertCondition(store.getHistorialClasificaciones().size() == totalHistorialAntes, "El historial de clasificaciones debe persistir.");

        System.out.println("✅ Test 2 Superado: Mutaciones y sesiones de clasificación persistidas correctamente.");
    }

    private static void testRestaurarFabrica(DataStore store) {
        System.out.println("--- Test 3: Restauración de valores de fábrica ---");

        boolean restaurado = store.restaurarPorDefecto();
        assertCondition(restaurado, "La restauración de fábrica debe completarse con éxito.");
        assertCondition(!store.getPilotos().containsKey(999), "El piloto custom 999 no debe existir tras restaurar fábrica.");
        assertCondition(store.getPilotos().size() == 20, "Deben existir exactamente 20 pilotos oficiales tras restaurar.");
        assertCondition(store.getHistorialClasificaciones().isEmpty(), "El historial debe estar limpio tras restaurar.");

        System.out.println("✅ Test 3 Superado: Restauración de fábrica verificada.");
    }

    private static void assertCondition(boolean condition, String message) {
        if (!condition) {
            System.err.println("❌ FALLO EN ASERCIÓN: " + message);
            throw new RuntimeException("Test assertion failed: " + message);
        }
    }
}
