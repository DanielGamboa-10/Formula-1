package com.formula1.model;

public enum EstrategiaCombustible {
    AGRESIVA("Agresiva (Mapa Motor Máximo)", "Flujo de combustible alto para máxima potencia de clasificación", -0.6, 1.30),
    BALANCEADA("Balanceada (Estándar)", "Mapa equilibrado entre potencia y fiabilidad", 0.0, 1.00),
    AHORRO("Ahorro (Lean Map)", "Mezcla pobre de combustible para preservar masa y motor", 0.8, 0.80);

    private final String nombre;
    private final String descripcion;
    private final double deltaTiempo;
    private final double factorConsumo;

    EstrategiaCombustible(String nombre, String descripcion, double deltaTiempo, double factorConsumo) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.deltaTiempo = deltaTiempo;
        this.factorConsumo = factorConsumo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public double getDeltaTiempo() {
        return deltaTiempo;
    }

    public double getFactorConsumo() {
        return factorConsumo;
    }
}
