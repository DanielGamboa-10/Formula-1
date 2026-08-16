package com.formula1.model;

public enum PresionNeumaticos {
    BAJA("Baja", "Mayor tracción y huella de contacto, mayor temperatura y desgaste", -0.3, 1.25),
    ESTANDAR("Estándar", "Presión recomendada por el fabricante para desgaste uniforme", 0.0, 1.0),
    ALTA("Alta", "Menor resistencia al rodamiento, menor agarre en curvas y menor desgaste", 0.4, 0.85);

    private final String nombre;
    private final String descripcion;
    private final double deltaTiempo;
    private final double factorDesgaste;

    PresionNeumaticos(String nombre, String descripcion, double deltaTiempo, double factorDesgaste) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.deltaTiempo = deltaTiempo;
        this.factorDesgaste = factorDesgaste;
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

    public double getFactorDesgaste() {
        return factorDesgaste;
    }
}
