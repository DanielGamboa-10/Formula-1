package com.formula1.data;

import com.formula1.model.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Snapshot serializable que encapsula el estado completo de las colecciones de la aplicación.
 */
public class DataSnapshot implements Serializable {
    private static final long serialVersionUID = 1L;

    private String version = "1.0.0";
    private LocalDateTime fechaGuardado;
    private Map<Integer, Piloto> pilotos;
    private Map<String, Equipo> equipos;
    private Map<String, Circuito> circuitos;
    private Map<String, Vehiculo> vehiculos;
    private Map<String, ConfiguracionVehiculo> configuraciones;
    private List<SesionClasificacion> historialClasificaciones;

    public DataSnapshot() {
        this.fechaGuardado = LocalDateTime.now();
        this.pilotos = new HashMap<>();
        this.equipos = new HashMap<>();
        this.circuitos = new HashMap<>();
        this.vehiculos = new HashMap<>();
        this.configuraciones = new HashMap<>();
        this.historialClasificaciones = new ArrayList<>();
    }

    public DataSnapshot(Map<Integer, Piloto> pilotos,
                        Map<String, Equipo> equipos,
                        Map<String, Circuito> circuitos,
                        Map<String, Vehiculo> vehiculos,
                        Map<String, ConfiguracionVehiculo> configuraciones,
                        List<SesionClasificacion> historialClasificaciones) {
        this.fechaGuardado = LocalDateTime.now();
        this.pilotos = pilotos != null ? new HashMap<>(pilotos) : new HashMap<>();
        this.equipos = equipos != null ? new HashMap<>(equipos) : new HashMap<>();
        this.circuitos = circuitos != null ? new HashMap<>(circuitos) : new HashMap<>();
        this.vehiculos = vehiculos != null ? new HashMap<>(vehiculos) : new HashMap<>();
        this.configuraciones = configuraciones != null ? new HashMap<>(configuraciones) : new HashMap<>();
        this.historialClasificaciones = historialClasificaciones != null ? new ArrayList<>(historialClasificaciones) : new ArrayList<>();
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public LocalDateTime getFechaGuardado() {
        return fechaGuardado;
    }

    public void setFechaGuardado(LocalDateTime fechaGuardado) {
        this.fechaGuardado = fechaGuardado;
    }

    public Map<Integer, Piloto> getPilotos() {
        return pilotos;
    }

    public void setPilotos(Map<Integer, Piloto> pilotos) {
        this.pilotos = pilotos;
    }

    public Map<String, Equipo> getEquipos() {
        return equipos;
    }

    public void setEquipos(Map<String, Equipo> equipos) {
        this.equipos = equipos;
    }

    public Map<String, Circuito> getCircuitos() {
        return circuitos;
    }

    public void setCircuitos(Map<String, Circuito> circuitos) {
        this.circuitos = circuitos;
    }

    public Map<String, Vehiculo> getVehiculos() {
        return vehiculos;
    }

    public void setVehiculos(Map<String, Vehiculo> vehiculos) {
        this.vehiculos = vehiculos;
    }

    public Map<String, ConfiguracionVehiculo> getConfiguraciones() {
        return configuraciones;
    }

    public void setConfiguraciones(Map<String, ConfiguracionVehiculo> configuraciones) {
        this.configuraciones = configuraciones;
    }

    public List<SesionClasificacion> getHistorialClasificaciones() {
        return historialClasificaciones;
    }

    public void setHistorialClasificaciones(List<SesionClasificacion> historialClasificaciones) {
        this.historialClasificaciones = historialClasificaciones;
    }
}
