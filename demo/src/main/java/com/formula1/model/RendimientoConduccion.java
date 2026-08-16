package com.formula1.model;

import java.io.Serializable;
import java.util.EnumMap;
import java.util.Map;

public class RendimientoConduccion implements Serializable {
    private double velocidadPromedioKmh;
    private Map<Clima, Double> consumoCombustible; // L/vuelta
    private Map<Clima, Double> desgasteNeumaticos; // %/vuelta

    public RendimientoConduccion() {
        this.consumoCombustible = new EnumMap<>(Clima.class);
        this.desgasteNeumaticos = new EnumMap<>(Clima.class);
    }

    public RendimientoConduccion(double velocidadPromedioKmh,
                                 double consumoSeco, double consumoLluvioso, double consumoExtremo,
                                 double desgasteSeco, double desgasteLluvioso, double desgasteExtremo) {
        this.velocidadPromedioKmh = velocidadPromedioKmh;
        this.consumoCombustible = new EnumMap<>(Clima.class);
        this.consumoCombustible.put(Clima.SECO, consumoSeco);
        this.consumoCombustible.put(Clima.LLUVIOSO, consumoLluvioso);
        this.consumoCombustible.put(Clima.EXTREMO, consumoExtremo);

        this.desgasteNeumaticos = new EnumMap<>(Clima.class);
        this.desgasteNeumaticos.put(Clima.SECO, desgasteSeco);
        this.desgasteNeumaticos.put(Clima.LLUVIOSO, desgasteLluvioso);
        this.desgasteNeumaticos.put(Clima.EXTREMO, desgasteExtremo);
    }

    public double getVelocidadPromedioKmh() {
        return velocidadPromedioKmh;
    }

    public void setVelocidadPromedioKmh(double velocidadPromedioKmh) {
        this.velocidadPromedioKmh = velocidadPromedioKmh;
    }

    public Map<Clima, Double> getConsumoCombustible() {
        return consumoCombustible;
    }

    public void setConsumoCombustible(Map<Clima, Double> consumoCombustible) {
        this.consumoCombustible = consumoCombustible;
    }

    public Map<Clima, Double> getDesgasteNeumaticos() {
        return desgasteNeumaticos;
    }

    public void setDesgasteNeumaticos(Map<Clima, Double> desgasteNeumaticos) {
        this.desgasteNeumaticos = desgasteNeumaticos;
    }

    public double getConsumo(Clima clima) {
        return consumoCombustible.getOrDefault(clima, 2.0);
    }

    public double getDesgaste(Clima clima) {
        return desgasteNeumaticos.getOrDefault(clima, 1.5);
    }
}
