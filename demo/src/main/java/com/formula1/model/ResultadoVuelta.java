package com.formula1.model;

import java.io.Serializable;

public class ResultadoVuelta implements Serializable, Comparable<ResultadoVuelta> {
    private int posicion;
    private Piloto piloto;
    private Vehiculo vehiculo;
    private double tiempoSegundos;
    private String tiempoFormateado;
    private double diferenciaConLiderSegundos;
    private double velocidadMediaKmh;
    private double desgasteNeumaticosEstimado;
    private double consumoCombustibleEstimado;
    private boolean esUsuario;

    public ResultadoVuelta() {
    }

    public ResultadoVuelta(int posicion, Piloto piloto, Vehiculo vehiculo, double tiempoSegundos,
                           double velocidadMediaKmh, double desgaste, double consumo, boolean esUsuario) {
        this.posicion = posicion;
        this.piloto = piloto;
        this.vehiculo = vehiculo;
        this.tiempoSegundos = tiempoSegundos;
        this.tiempoFormateado = RecordVuelta.formatSegundosToTiempo(tiempoSegundos);
        this.velocidadMediaKmh = velocidadMediaKmh;
        this.desgasteNeumaticosEstimado = desgaste;
        this.consumoCombustibleEstimado = consumo;
        this.esUsuario = esUsuario;
    }

    public int getPosicion() {
        return posicion;
    }

    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }

    public Piloto getPiloto() {
        return piloto;
    }

    public void setPiloto(Piloto piloto) {
        this.piloto = piloto;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    public double getTiempoSegundos() {
        return tiempoSegundos;
    }

    public void setTiempoSegundos(double tiempoSegundos) {
        this.tiempoSegundos = tiempoSegundos;
        this.tiempoFormateado = RecordVuelta.formatSegundosToTiempo(tiempoSegundos);
    }

    public String getTiempoFormateado() {
        return tiempoFormateado;
    }

    public double getDiferenciaConLiderSegundos() {
        return diferenciaConLiderSegundos;
    }

    public void setDiferenciaConLiderSegundos(double diferenciaConLiderSegundos) {
        this.diferenciaConLiderSegundos = diferenciaConLiderSegundos;
    }

    public double getVelocidadMediaKmh() {
        return velocidadMediaKmh;
    }

    public void setVelocidadMediaKmh(double velocidadMediaKmh) {
        this.velocidadMediaKmh = velocidadMediaKmh;
    }

    public double getDesgasteNeumaticosEstimado() {
        return desgasteNeumaticosEstimado;
    }

    public void setDesgasteNeumaticosEstimado(double desgasteNeumaticosEstimado) {
        this.desgasteNeumaticosEstimado = desgasteNeumaticosEstimado;
    }

    public double getConsumoCombustibleEstimado() {
        return consumoCombustibleEstimado;
    }

    public void setConsumoCombustibleEstimado(double consumoCombustibleEstimado) {
        this.consumoCombustibleEstimado = consumoCombustibleEstimado;
    }

    public boolean isEsUsuario() {
        return esUsuario;
    }

    public void setEsUsuario(boolean esUsuario) {
        this.esUsuario = esUsuario;
    }

    @Override
    public int compareTo(ResultadoVuelta o) {
        return Double.compare(this.tiempoSegundos, o.tiempoSegundos);
    }
}
