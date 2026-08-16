package com.formula1.model;

import java.io.Serializable;

public class RendimientoVehiculo implements Serializable {
    private RendimientoConduccion conduccionNormal;
    private RendimientoConduccion conduccionAgresiva;
    private RendimientoConduccion ahorroCombustible;

    public RendimientoVehiculo() {
    }

    public RendimientoVehiculo(RendimientoConduccion normal, RendimientoConduccion agresivo, RendimientoConduccion ahorro) {
        this.conduccionNormal = normal;
        this.conduccionAgresiva = agresivo;
        this.ahorroCombustible = ahorro;
    }

    public RendimientoConduccion getConduccionNormal() {
        return conduccionNormal;
    }

    public void setConduccionNormal(RendimientoConduccion conduccionNormal) {
        this.conduccionNormal = conduccionNormal;
    }

    public RendimientoConduccion getConduccionAgresiva() {
        return conduccionAgresiva;
    }

    public void setConduccionAgresiva(RendimientoConduccion conduccionAgresiva) {
        this.conduccionAgresiva = conduccionAgresiva;
    }

    public RendimientoConduccion getAhorroCombustible() {
        return ahorroCombustible;
    }

    public void setAhorroCombustible(RendimientoConduccion ahorroCombustible) {
        this.ahorroCombustible = ahorroCombustible;
    }

    public RendimientoConduccion getPorModo(ModoConduccion modo) {
        if (modo == null) return conduccionNormal;
        switch (modo) {
            case AGRESIVO:
                return conduccionAgresiva != null ? conduccionAgresiva : conduccionNormal;
            case AHORRO:
                return ahorroCombustible != null ? ahorroCombustible : conduccionNormal;
            case NORMAL:
            default:
                return conduccionNormal;
        }
    }
}
