package com.formula1.service;

import com.formula1.data.DataStore;
import com.formula1.model.ResultadoVuelta;
import com.formula1.model.SesionClasificacion;

import java.util.*;
import java.util.stream.Collectors;

public class EstadisticaService {
    private final DataStore store;

    public EstadisticaService() {
        this.store = DataStore.getInstance();
    }

    public List<SesionClasificacion> obtenerHistorialCompleto() {
        return Collections.unmodifiableList(store.getHistorialClasificaciones());
    }

    public List<SesionClasificacion> filtrarPorCircuito(String nombreCircuito) {
        if (nombreCircuito == null) return Collections.emptyList();
        return store.getHistorialClasificaciones().stream()
                .filter(s -> s.getCircuito().getNombre().equalsIgnoreCase(nombreCircuito.trim()))
                .collect(Collectors.toList());
    }

    public Optional<ResultadoVuelta> obtenerMejorTiempoUsuario(String nombreCircuito) {
        return store.getHistorialClasificaciones().stream()
                .filter(s -> nombreCircuito == null || s.getCircuito().getNombre().equalsIgnoreCase(nombreCircuito.trim()))
                .map(SesionClasificacion::getResultadoUsuario)
                .filter(Objects::nonNull)
                .min(Comparator.comparingDouble(ResultadoVuelta::getTiempoSegundos));
    }
}
