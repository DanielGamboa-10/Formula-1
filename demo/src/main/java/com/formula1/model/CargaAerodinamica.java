package com.formula1.model;

public enum CargaAerodinamica {
    BAJA("Baja", "Mayor velocidad punta en rectas, menor agarre en curvas lentas", -0.4, 0.95),
    MEDIA("Media", "Balance óptimo entre velocidad punta y estabilidad en curva", 0.0, 1.0),
    ALTA("Alta", "Máximo agarre y estabilidad en curvas, menor velocidad punta", 0.3, 1.10);

    private final String nombre;
    private final String descripcion;
    private final double deltaTiempo; // Segundos base
    private final double impactoDesgaste;

    CargaAerodinamica(String nombre, String descripcion, double deltaTiempo, double impactoDesgaste) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.deltaTiempo = deltaTiempo;
        this.impactoDesgaste = impactoDesgaste;
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

    public double getImpactoDesgaste() {
        return impactoDesgaste;
    }
}
