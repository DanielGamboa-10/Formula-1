package com.formula1.model;

import java.io.Serializable;

/**
 * Representa el resultado y telemetría de una vuelta de clasificación.
 * Implementa Comparable para ordenar la parrilla automáticamente de menor a mayor tiempo.
 */
public class ResultadoVuelta implements Serializable, Comparable<ResultadoVuelta> {
    private int posicion;                          // Posición final en la parrilla (P1 a P20)
    private Piloto piloto;                         // Piloto que marcó el tiempo
    private Vehiculo vehiculo;                     // Monoplaza utilizado
    private double tiempoSegundos;                 // Tiempo exacto de vuelta en segundos
    private String tiempoFormateado;               // Tiempo en formato tradicional M:SS.mmm
    private double diferenciaConLiderSegundos;     // Diferencia (+delta s) respecto a la Pole Position
    private double velocidadMediaKmh;              // Velocidad promedio en la vuelta
    private double desgasteNeumaticosEstimado;     // Porcentaje de degradación de neumáticos
    private double consumoCombustibleEstimado;     // Litros consumidos en la vuelta
    private boolean esUsuario;                     // Marca si corresponde al monoplaza del usuario

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

    // Getters y Setters
    public int getPosicion() { return posicion; }
    public void setPosicion(int posicion) { this.posicion = posicion; }

    public Piloto getPiloto() { return piloto; }
    public void setPiloto(Piloto piloto) { this.piloto = piloto; }

    public Vehiculo getVehiculo() { return vehiculo; }
    public void setVehiculo(Vehiculo vehiculo) { this.vehiculo = vehiculo; }

    public double getTiempoSegundos() { return tiempoSegundos; }
    public void setTiempoSegundos(double tiempoSegundos) {
        this.tiempoSegundos = tiempoSegundos;
        this.tiempoFormateado = RecordVuelta.formatSegundosToTiempo(tiempoSegundos);
    }

    public String getTiempoFormateado() { return tiempoFormateado; }

    public double getDiferenciaConLiderSegundos() { return diferenciaConLiderSegundos; }
    public void setDiferenciaConLiderSegundos(double diferenciaConLiderSegundos) {
        this.diferenciaConLiderSegundos = diferenciaConLiderSegundos;
    }

    public double getVelocidadMediaKmh() { return velocidadMediaKmh; }
    public void setVelocidadMediaKmh(double velocidadMediaKmh) { this.velocidadMediaKmh = velocidadMediaKmh; }

    public double getDesgasteNeumaticosEstimado() { return desgasteNeumaticosEstimado; }
    public void setDesgasteNeumaticosEstimado(double desgasteNeumaticosEstimado) {
        this.desgasteNeumaticosEstimado = desgasteNeumaticosEstimado;
    }

    public double getConsumoCombustibleEstimado() { return consumoCombustibleEstimado; }
    public void setConsumoCombustibleEstimado(double consumoCombustibleEstimado) {
        this.consumoCombustibleEstimado = consumoCombustibleEstimado;
    }

    public boolean isEsUsuario() { return esUsuario; }
    public void setEsUsuario(boolean esUsuario) { this.esUsuario = esUsuario; }

    // Criterio de ordenamiento natural: menor tiempo = mejor posición
    @Override
    public int compareTo(ResultadoVuelta o) {
        return Double.compare(this.tiempoSegundos, o.tiempoSegundos);
    }
}
