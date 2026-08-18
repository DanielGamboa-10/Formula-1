package com.formula1.data;

import com.formula1.model.*;
import java.util.*;

/**
 * Persistencia en memoria temporal basada en el patrón Singleton.
 * Mantiene centralizadas las colecciones Map/HashMap y listas de todas las entidades.
 */
public class DataStore {
    private static DataStore instance;

    // Mapas en memoria con clave primaria única
    private final Map<Integer, Piloto> pilotos;                        // Clave: ID del piloto
    private final Map<String, Equipo> equipos;                         // Clave: Nombre del equipo
    private final Map<String, Circuito> circuitos;                     // Clave: Nombre del circuito
    private final Map<String, Vehiculo> vehiculos;                     // Clave: Modelo del monoplaza
    private final Map<String, ConfiguracionVehiculo> configuraciones;  // Clave: Nombre del preset
    private final List<SesionClasificacion> historialClasificaciones;  // Historial de clasificaciones

    private boolean autoSave = true;

    // Constructor privado para garantizar una única instancia (Singleton)
    private DataStore() {
        this.pilotos = new HashMap<>();
        this.equipos = new HashMap<>();
        this.circuitos = new HashMap<>();
        this.vehiculos = new HashMap<>();
        this.configuraciones = new HashMap<>();
        this.historialClasificaciones = new ArrayList<>();
    }

    // Acceso global a la única instancia de memoria
    public static synchronized DataStore getInstance() {
        if (instance == null) {
            instance = new DataStore();
        }
        return instance;
    }

    // Getters para acceder a las colecciones
    public Map<Integer, Piloto> getPilotos() { return pilotos; }
    public Map<String, Equipo> getEquipos() { return equipos; }
    public Map<String, Circuito> getCircuitos() { return circuitos; }
    public Map<String, Vehiculo> getVehiculos() { return vehiculos; }
    public Map<String, ConfiguracionVehiculo> getConfiguraciones() { return configuraciones; }
    public Map<String, ConfiguracionVehiculo> getConfiguracionesGuardadas() { return configuraciones; }
    
    public List<SesionClasificacion> getHistorialClasificaciones() { return historialClasificaciones; }

    // Registra una nueva sesión en el historial
    public void agregarHistorial(SesionClasificacion sesion) {
        if (sesion != null) {
            historialClasificaciones.add(sesion);
            if (autoSave) {
                guardar();
            }
        }
    }

    public void agregarHistorialClasificacion(SesionClasificacion sesion) {
        agregarHistorial(sesion);
    }

    // Métodos de persistencia en disco
    public boolean guardar() {
        return DataPersistenceManager.guardar(this);
    }

    public boolean cargar() {
        return DataPersistenceManager.cargar(this);
    }

    public boolean restaurarPorDefecto() {
        return DataPersistenceManager.restaurarPorDefecto(this);
    }

    public void limpiar() {
        pilotos.clear();
        equipos.clear();
        circuitos.clear();
        vehiculos.clear();
        configuraciones.clear();
        historialClasificaciones.clear();
    }

    public boolean isAutoSave() {
        return autoSave;
    }

    public void setAutoSave(boolean autoSave) {
        this.autoSave = autoSave;
    }
}
