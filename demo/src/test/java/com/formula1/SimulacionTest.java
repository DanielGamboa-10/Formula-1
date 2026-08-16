package com.formula1;

import com.formula1.data.DataLoader;
import com.formula1.data.DataStore;
import com.formula1.model.*;
import com.formula1.service.*;

import java.util.List;

public class SimulacionTest {

    public static void main(String[] args) {
        System.out.println("🧪 Iniciando batería de pruebas unitarias y de integración...");

        // 1. Carga de datos
        DataLoader.cargarDatosIniciales();
        DataStore store = DataStore.getInstance();

        assertCondition(store.getPilotos().size() == 20, "Debe haber exactamente 20 pilotos cargados.");
        assertCondition(store.getEquipos().size() >= 10, "Debe haber al menos 10 equipos cargados.");
        assertCondition(store.getCircuitos().size() == 7, "Debe haber 7 circuitos oficiales cargados.");
        assertCondition(store.getVehiculos().size() >= 10, "Debe haber al menos 10 monoplazas cargados.");

        System.out.println("✅ Prueba 1: Carga de datos iniciales en HashMap superada.");

        // 2. Servicios CRUD
        PilotoService pilotoService = new PilotoService();
        Piloto nuevoPiloto = pilotoService.agregarPiloto("Piloto Test", "Ferrari", "Escudero", 85, 88);
        assertCondition(store.getPilotos().containsKey(nuevoPiloto.getId()), "El nuevo piloto debe estar en el DataStore.");
        boolean eliminado = pilotoService.eliminarPiloto(nuevoPiloto.getId());
        assertCondition(eliminado && !store.getPilotos().containsKey(nuevoPiloto.getId()), "El piloto debe poder ser eliminado.");

        System.out.println("✅ Prueba 2: CRUD de Pilotos superada.");

        // 3. Configuración del vehículo
        ConfiguracionService configService = new ConfiguracionService();
        ConfiguracionVehiculo customConfig = new ConfiguracionVehiculo(
                ModoConduccion.AGRESIVO,
                CargaAerodinamica.BAJA,
                PresionNeumaticos.BAJA,
                EstrategiaCombustible.AGRESIVA
        );
        configService.guardarConfiguracion("Setup Monza", customConfig);
        assertCondition(configService.cargarConfiguracion("Setup Monza").isPresent(), "La configuración guardada debe poder ser recuperada.");

        System.out.println("✅ Prueba 3: Gestión de configuraciones del vehículo superada.");

        // 4. Simulación de Clasificación
        SimulacionService simulacionService = new SimulacionService();
        Circuito monza = store.getCircuitos().get("Circuito de Monza");
        Piloto verstappen = store.getPilotos().get(1);
        Vehiculo rb20 = store.getVehiculos().get("RB20");

        SesionClasificacion sesion = simulacionService.simularClasificacion(
                monza, Clima.SECO, verstappen, rb20, customConfig
        );

        assertCondition(sesion != null, "La sesión de clasificación no debe ser nula.");
        assertCondition(sesion.getResultados().size() == 20, "Deben generarse tiempos para los 20 pilotos.");
        assertCondition(sesion.getPolePosition() != null, "Debe existir un piloto en Pole Position.");
        assertCondition(sesion.getResultados().get(0).getPosicion() == 1, "El primer lugar debe tener la posición 1.");

        // Verificar orden de menor a mayor tiempo
        for (int i = 0; i < sesion.getResultados().size() - 1; i++) {
            double t1 = sesion.getResultados().get(i).getTiempoSegundos();
            double t2 = sesion.getResultados().get(i + 1).getTiempoSegundos();
            assertCondition(t1 <= t2, "La tabla de clasificación debe estar ordenada de menor a mayor tiempo.");
        }

        System.out.println("✅ Prueba 4: Motor de Simulación de Clasificación y orden de Pole Position superada.");

        // 5. Historial de estadísticas
        EstadisticaService estadisticaService = new EstadisticaService();
        List<SesionClasificacion> hist = estadisticaService.obtenerHistorialCompleto();
        assertCondition(!hist.isEmpty(), "La sesión debe quedar registrada en el historial.");

        System.out.println("✅ Prueba 5: Persistencia del historial de clasificaciones superada.");

        System.out.println("\n🎉 TODAS LAS PRUEBAS (5/5) PASARON EXITOSAMENTE.");
    }

    private static void assertCondition(boolean condition, String message) {
        if (!condition) {
            System.err.println("❌ FALLO EN ASERCIÓN: " + message);
            throw new RuntimeException("Test assertion failed: " + message);
        }
    }
}
