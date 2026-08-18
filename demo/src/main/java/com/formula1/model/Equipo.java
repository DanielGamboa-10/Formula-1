package com.formula1.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Equipo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String nombre;
    private String pais;
    private String motor;
    private List<Integer> pilotosIds;
    private String imagenUrl;

    public Equipo() {
        this.pilotosIds = new ArrayList<>();
    }

    public Equipo(String nombre, String pais, String motor, List<Integer> pilotosIds, String imagenUrl) {
        this.nombre = nombre;
        this.pais = pais;
        this.motor = motor;
        this.pilotosIds = pilotosIds != null ? new ArrayList<>(pilotosIds) : new ArrayList<>();
        this.imagenUrl = imagenUrl;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getMotor() {
        return motor;
    }

    public void setMotor(String motor) {
        this.motor = motor;
    }

    public List<Integer> getPilotosIds() {
        return pilotosIds;
    }

    public void setPilotosIds(List<Integer> pilotosIds) {
        this.pilotosIds = pilotosIds;
    }

    public void agregarPiloto(int pilotoId) {
        if (!pilotosIds.contains(pilotoId)) {
            pilotosIds.add(pilotoId);
        }
    }

    public void removerPiloto(int pilotoId) {
        pilotosIds.remove(Integer.valueOf(pilotoId));
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Equipo equipo = (Equipo) o;
        return Objects.equals(nombre, equipo.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre);
    }

    @Override
    public String toString() {
        return String.format("%-22s | País: %-12s | Motor: %-12s | Pilotos Asignados: %s",
                nombre, pais, motor, pilotosIds.toString());
    }
}
