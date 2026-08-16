package com.formula1.model;

public enum ModoConduccion {
    NORMAL("Normal", "Equilibrio entre velocidad y desgaste moderado", 0.0, 1.0, 1.0),
    AGRESIVO("Agresivo", "Máxima potencia y aceleración, mayor desgaste y consumo", -1.2, 1.25, 1.45),
    AHORRO("Ahorro de Combustible", "Conducción conservadora, menor desgaste y consumo", 1.5, 0.85, 0.70);

    private final String nombre;
    private final String descripcion;
    private final double deltaTiempoSegundos; // impacto en segundos
    private final double multiplicadorConsumo;
    private final double multiplicadorDesgaste;

    ModoConduccion(String nombre, String descripcion, double deltaTiempoSegundos, double multiplicadorConsumo, double multiplicadorDesgaste) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.deltaTiempoSegundos = deltaTiempoSegundos;
        this.multiplicadorConsumo = multiplicadorConsumo;
        this.multiplicadorDesgaste = multiplicadorDesgaste;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public double getDeltaTiempoSegundos() {
        return deltaTiempoSegundos;
    }

    public double getMultiplicadorConsumo() {
        return multiplicadorConsumo;
    }

    public double getMultiplicadorDesgaste() {
        return multiplicadorDesgaste;
    }
}
