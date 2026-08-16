package com.formula1.service;

import com.formula1.data.DataStore;
import com.formula1.model.*;

import java.util.*;
import java.util.stream.Collectors;

public class VehiculoService {
    private final DataStore store;

    public VehiculoService() {
        this.store = DataStore.getInstance();
    }

    public List<Vehiculo> listarTodos() {
        return new ArrayList<>(store.getVehiculos().values());
    }

    public Optional<Vehiculo> buscarPorModelo(String modelo) {
        if (modelo == null) return Optional.empty();
        return Optional.ofNullable(store.getVehiculos().get(modelo.trim()));
    }

    public List<Vehiculo> buscarPorEquipo(String equipo) {
        if (equipo == null) return Collections.emptyList();
        return store.getVehiculos().values().stream()
                .filter(v -> v.getEquipo().equalsIgnoreCase(equipo.trim()))
                .collect(Collectors.toList());
    }

    public List<Vehiculo> buscarPorTermino(String termino) {
        if (termino == null || termino.trim().isEmpty()) {
            return Collections.emptyList();
        }
        String t = termino.toLowerCase();
        return store.getVehiculos().values().stream()
                .filter(v -> v.getModelo().toLowerCase().contains(t) ||
                             v.getEquipo().toLowerCase().contains(t) ||
                             v.getMotor().toLowerCase().contains(t))
                .collect(Collectors.toList());
    }

    public Vehiculo agregarVehiculo(String equipo, String modelo, String motor, int velMax, double acel0a100,
                                     RendimientoVehiculo rendimiento, String imagenUrl) {
        Vehiculo v = new Vehiculo(equipo, modelo, motor, velMax, acel0a100, new ArrayList<>(), rendimiento, imagenUrl);
        store.getVehiculos().put(modelo, v);
        return v;
    }

    public boolean actualizarVehiculo(String modeloOriginal, String nuevoModelo, String nuevoEquipo,
                                      String nuevoMotor, int velMax, double acel0a100) {
        Vehiculo v = store.getVehiculos().get(modeloOriginal);
        if (v == null) return false;

        v.setEquipo(nuevoEquipo);
        v.setMotor(nuevoMotor);
        v.setVelocidadMaximaKmh(velMax);
        v.setAceleracion0a100(acel0a100);

        if (!modeloOriginal.equalsIgnoreCase(nuevoModelo)) {
            store.getVehiculos().remove(modeloOriginal);
            v.setModelo(nuevoModelo);
            store.getVehiculos().put(nuevoModelo, v);
        }
        return true;
    }

    public boolean eliminarVehiculo(String modelo) {
        return store.getVehiculos().remove(modelo) != null;
    }

    public boolean asignarPilotoAVehiculo(int pilotoId, String modelo) {
        Vehiculo v = store.getVehiculos().get(modelo);
        Piloto p = store.getPilotos().get(pilotoId);
        if (v == null || p == null) return false;

        v.asignarPiloto(pilotoId);
        return true;
    }

    public Optional<Vehiculo> obtenerVehiculoPorPiloto(int pilotoId) {
        return store.getVehiculos().values().stream()
                .filter(v -> v.getPilotosIds().contains(pilotoId))
                .findFirst();
    }
}
