package com.formula1.service;

import com.formula1.data.DataStore;
import com.formula1.model.Equipo;
import com.formula1.model.Piloto;

import java.util.*;
import java.util.stream.Collectors;

public class EquipoService {
    private final DataStore store;

    public EquipoService() {
        this.store = DataStore.getInstance();
    }

    public List<Equipo> listarTodos() {
        return new ArrayList<>(store.getEquipos().values());
    }

    public Optional<Equipo> buscarPorNombre(String nombre) {
        if (nombre == null) return Optional.empty();
        return Optional.ofNullable(store.getEquipos().get(nombre.trim()));
    }

    public List<Equipo> buscarPorTermino(String termino) {
        if (termino == null || termino.trim().isEmpty()) {
            return Collections.emptyList();
        }
        String t = termino.toLowerCase();
        return store.getEquipos().values().stream()
                .filter(e -> e.getNombre().toLowerCase().contains(t) || e.getPais().toLowerCase().contains(t) || e.getMotor().toLowerCase().contains(t))
                .collect(Collectors.toList());
    }

    public Equipo agregarEquipo(String nombre, String pais, String motor, String imagenUrl) {
        Equipo equipo = new Equipo(nombre, pais, motor, new ArrayList<>(), imagenUrl);
        store.getEquipos().put(nombre, equipo);
        return equipo;
    }

    public boolean actualizarEquipo(String nombreOriginal, String nuevoNombre, String nuevoPais, String nuevoMotor, String nuevaImagen) {
        Equipo eq = store.getEquipos().get(nombreOriginal);
        if (eq == null) return false;

        eq.setPais(nuevoPais);
        eq.setMotor(nuevoMotor);
        eq.setImagenUrl(nuevaImagen);

        if (!nombreOriginal.equalsIgnoreCase(nuevoNombre)) {
            store.getEquipos().remove(nombreOriginal);
            eq.setNombre(nuevoNombre);
            store.getEquipos().put(nuevoNombre, eq);

            // Actualizar pilotos del equipo
            for (Integer pId : eq.getPilotosIds()) {
                Piloto p = store.getPilotos().get(pId);
                if (p != null) {
                    p.setEquipo(nuevoNombre);
                }
            }
        }
        return true;
    }

    public boolean eliminarEquipo(String nombre) {
        Equipo eq = store.getEquipos().remove(nombre);
        if (eq != null) {
            // Desasociar pilotos
            for (Integer pId : eq.getPilotosIds()) {
                Piloto p = store.getPilotos().get(pId);
                if (p != null) {
                    p.setEquipo("Sin Equipo");
                }
            }
            return true;
        }
        return false;
    }

    public boolean asignarPilotoAEquipo(int pilotoId, String nombreEquipo) {
        Equipo eq = store.getEquipos().get(nombreEquipo);
        Piloto p = store.getPilotos().get(pilotoId);
        if (eq == null || p == null) return false;

        // Si estaba en otro equipo, removerlo de allí
        String equipoAnterior = p.getEquipo();
        if (store.getEquipos().containsKey(equipoAnterior)) {
            store.getEquipos().get(equipoAnterior).removerPiloto(pilotoId);
        }

        p.setEquipo(nombreEquipo);
        eq.agregarPiloto(pilotoId);
        return true;
    }
}
