package com.formula1.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Vehiculo implements Serializable {
    private String equipo;
    private String modelo;
    private String motor;
    private int velocidadMaximaKmh;
    private double aceleracion0a100; // segundos
    private List<Integer> pilotosIds;
    private RendimientoVehiculo rendimiento;
    private String imagenUrl;

    public Vehiculo() {
        this.pilotosIds = new ArrayList<>();
    }

    public Vehiculo(String equipo, String modelo, String motor, int velocidadMaximaKmh, double aceleracion0a100,
                    List<Integer> pilotosIds, RendimientoVehiculo rendimiento, String imagenUrl) {
        this.equipo = equipo;
        this.modelo = modelo;
        this.motor = motor;
        this.velocidadMaximaKmh = velocidadMaximaKmh;
        this.aceleracion0a100 = aceleracion0a100;
        this.pilotosIds = pilotosIds != null ? new ArrayList<>(pilotosIds) : new ArrayList<>();
        this.rendimiento = rendimiento;
        this.imagenUrl = imagenUrl;
    }

    public String getEquipo() {
        return equipo;
    }

    public void setEquipo(String equipo) {
        this.equipo = equipo;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getMotor() {
        return motor;
    }

    public void setMotor(String motor) {
        this.motor = motor;
    }

    public int getVelocidadMaximaKmh() {
        return velocidadMaximaKmh;
    }

    public void setVelocidadMaximaKmh(int velocidadMaximaKmh) {
        this.velocidadMaximaKmh = velocidadMaximaKmh;
    }

    public double getAceleracion0a100() {
        return aceleracion0a100;
    }

    public void setAceleracion0a100(double aceleracion0a100) {
        this.aceleracion0a100 = aceleracion0a100;
    }

    public List<Integer> getPilotosIds() {
        return pilotosIds;
    }

    public void setPilotosIds(List<Integer> pilotosIds) {
        this.pilotosIds = pilotosIds;
    }

    public void asignarPiloto(int pilotoId) {
        if (!pilotosIds.contains(pilotoId)) {
            pilotosIds.add(pilotoId);
        }
    }

    public void removerPiloto(int pilotoId) {
        pilotosIds.remove(Integer.valueOf(pilotoId));
    }

    public RendimientoVehiculo getRendimiento() {
        return rendimiento;
    }

    public void setRendimiento(RendimientoVehiculo rendimiento) {
        this.rendimiento = rendimiento;
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
        Vehiculo vehiculo = (Vehiculo) o;
        return Objects.equals(equipo, vehiculo.equipo) && Objects.equals(modelo, vehiculo.modelo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(equipo, modelo);
    }

    @Override
    public String toString() {
        return String.format("%-10s (%-22s) | Motor: %-10s | V.Max: %3d km/h | 0-100: %.2fs",
                modelo, equipo, motor, velocidadMaximaKmh, aceleracion0a100);
    }
}
