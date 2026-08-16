package com.formula1.service;

import com.formula1.data.DataStore;
import com.formula1.model.Piloto;

import java.util.*;
import java.util.stream.Collectors;

public class PilotoService {
    private final DataStore store;

    public PilotoService() {
        this.store = DataStore.getInstance();
    }

    public List<Piloto> listarTodos() {
        return store.getPilotos().values().stream()
                .sorted(Comparator.comparingInt(Piloto::getId))
                .collect(Collectors.toList());
    }

    public Optional<Piloto> buscarPorId(int id) {
        return Optional.ofNullable(store.getPilotos().get(id));
    }

    public List<Piloto> buscarPorNombre(String termino) {
        if (termino == null || termino.trim().isEmpty()) {
            return Collections.emptyList();
        }
        String t = termino.toLowerCase();
        return store.getPilotos().values().stream()
                .filter(p -> p.getNombre().toLowerCase().contains(t))
                .collect(Collectors.toList());
    }

    public List<Piloto> listarPorEquipo(String equipo) {
        if (equipo == null) return Collections.emptyList();
        return store.getPilotos().values().stream()
                .filter(p -> p.getEquipo().equalsIgnoreCase(equipo.trim()))
                .collect(Collectors.toList());
    }

    public Piloto agregarPiloto(String nombre, String equipo, String rol, int experiencia, int habilidad) {
        int nuevoId = store.getPilotos().keySet().stream().max(Integer::compareTo).orElse(0) + 1;
        Piloto nuevo = new Piloto(nuevoId, nombre, equipo, rol, experiencia, habilidad);
        store.getPilotos().put(nuevoId, nuevo);

        // Si el equipo existe, agregarlo a su lista
        if (store.getEquipos().containsKey(equipo)) {
            store.getEquipos().get(equipo).agregarPiloto(nuevoId);
        }
        return nuevo;
    }

    public boolean actualizarPiloto(int id, String nuevoNombre, String nuevoEquipo, String nuevoRol, int nuevaExp, int nuevaHab) {
        Piloto p = store.getPilotos().get(id);
        if (p == null) return false;

        String equipoAnterior = p.getEquipo();
        p.setNombre(nuevoNombre);
        p.setEquipo(nuevoEquipo);
        p.setRol(nuevoRol);
        p.setExperiencia(nuevaExp);
        p.setHabilidad(nuevaHab);

        // Actualizar relaciones con equipo
        if (!equipoAnterior.equalsIgnoreCase(nuevoEquipo)) {
            if (store.getEquipos().containsKey(equipoAnterior)) {
                store.getEquipos().get(equipoAnterior).removerPiloto(id);
            }
            if (store.getEquipos().containsKey(nuevoEquipo)) {
                store.getEquipos().get(nuevoEquipo).agregarPiloto(id);
            }
        }
        return true;
    }

    public boolean eliminarPiloto(int id) {
        Piloto p = store.getPilotos().remove(id);
        if (p != null) {
            // Remover del equipo
            if (store.getEquipos().containsKey(p.getEquipo())) {
                store.getEquipos().get(p.getEquipo()).removerPiloto(id);
            }
            // Remover de vehículos
            store.getVehiculos().values().forEach(v -> v.removerPiloto(id));
            return true;
        }
        return false;
    }
}
