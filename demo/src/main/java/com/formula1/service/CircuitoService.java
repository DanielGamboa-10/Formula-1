package com.formula1.service;

import com.formula1.data.DataStore;
import com.formula1.model.Circuito;
import com.formula1.model.GanadorHistorico;
import com.formula1.model.RecordVuelta;

import java.util.*;
import java.util.stream.Collectors;

public class CircuitoService {
    private final DataStore store;

    public CircuitoService() {
        this.store = DataStore.getInstance();
    }

    public List<Circuito> listarTodos() {
        return new ArrayList<>(store.getCircuitos().values());
    }

    public Optional<Circuito> buscarPorNombre(String nombre) {
        if (nombre == null) return Optional.empty();
        return Optional.ofNullable(store.getCircuitos().get(nombre.trim()));
    }

    public List<Circuito> buscarPorTermino(String termino) {
        if (termino == null || termino.trim().isEmpty()) {
            return Collections.emptyList();
        }
        String t = termino.toLowerCase();
        return store.getCircuitos().values().stream()
                .filter(c -> c.getNombre().toLowerCase().contains(t) || c.getPais().toLowerCase().contains(t))
                .collect(Collectors.toList());
    }

    public Circuito agregarCircuito(String nombre, String pais, double longitudKm, int vueltas, String descripcion,
                                    String recordTiempo, String recordPiloto, int recordAnio, String imagenUrl) {
        RecordVuelta rec = new RecordVuelta(recordTiempo, recordPiloto, recordAnio);
        Circuito circuito = new Circuito(nombre, pais, longitudKm, vueltas, descripcion, rec, new ArrayList<>(), imagenUrl);
        store.getCircuitos().put(nombre, circuito);
        if (store.isAutoSave()) {
            store.guardar();
        }
        return circuito;
    }

    public boolean actualizarCircuito(String nombreOriginal, String nuevoNombre, String nuevoPais, double longitudKm,
                                      int vueltas, String descripcion) {
        Circuito c = store.getCircuitos().get(nombreOriginal);
        if (c == null) return false;

        c.setPais(nuevoPais);
        c.setLongitudKm(longitudKm);
        c.setVueltas(vueltas);
        c.setDescripcion(descripcion);

        if (!nombreOriginal.equalsIgnoreCase(nuevoNombre)) {
            store.getCircuitos().remove(nombreOriginal);
            c.setNombre(nuevoNombre);
            store.getCircuitos().put(nuevoNombre, c);
        }
        if (store.isAutoSave()) {
            store.guardar();
        }
        return true;
    }

    public boolean eliminarCircuito(String nombre) {
        boolean removed = store.getCircuitos().remove(nombre) != null;
        if (removed && store.isAutoSave()) {
            store.guardar();
        }
        return removed;
    }

    public void registrarGanador(String nombreCircuito, int temporada, int pilotoId) {
        Circuito c = store.getCircuitos().get(nombreCircuito);
        if (c != null) {
            c.getGanadores().add(new GanadorHistorico(temporada, pilotoId));
            if (store.isAutoSave()) {
                store.guardar();
            }
        }
    }
}
