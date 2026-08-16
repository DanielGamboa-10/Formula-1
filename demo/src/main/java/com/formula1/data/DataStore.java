package com.formula1.data;

import com.formula1.model.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * DataStore en memoria basado en Map y HashMap para persistencia temporal.
 */
public class DataStore {
    private static DataStore instance;

    // Almacenamiento por Map / HashMap
    private final Map<Integer, Piloto> pilotos;
    private final Map<String, Equipo> equipos;
    private final Map<String, Circuito> circuitos;
    private final Map<String, Vehiculo> vehiculos;
    private final Map<String, ConfiguracionVehiculo> configuracionesGuardadas;
    private final List<SesionClasificacion> historialClasificaciones;

    private DataStore() {
        this.pilotos = new HashMap<>();
        this.equipos = new LinkedHashMap<>();
        this.circuitos = new LinkedHashMap<>();
        this.vehiculos = new LinkedHashMap<>();
        this.configuracionesGuardadas = new HashMap<>();
        this.historialClasificaciones = new ArrayList<>();
    }

    public static synchronized DataStore getInstance() {
        if (instance == null) {
            instance = new DataStore();
        }
        return instance;
    }

    public Map<Integer, Piloto> getPilotos() {
        return pilotos;
    }

    public Map<String, Equipo> getEquipos() {
        return equipos;
    }

    public Map<String, Circuito> getCircuitos() {
        return circuitos;
    }

    public Map<String, Vehiculo> getVehiculos() {
        return vehiculos;
    }

    public Map<String, ConfiguracionVehiculo> getConfiguracionesGuardadas() {
        return configuracionesGuardadas;
    }

    public List<SesionClasificacion> getHistorialClasificaciones() {
        return historialClasificaciones;
    }

    public void agregarHistorial(SesionClasificacion sesion) {
        historialClasificaciones.add(sesion);
    }
}
