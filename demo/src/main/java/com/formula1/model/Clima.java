package com.formula1.model;

public enum Clima {
    SECO("Seco", "Pista seca con máxima adherencia y velocidad.", 1.0, 1.0, 1.0),
    LLUVIOSO("Lluvioso", "Pista mojada, menor adherencia y tiempos más lentos.", 1.08, 1.15, 0.7),
    EXTREMO("Extremo", "Condiciones de lluvia torrencial y visibilidad mínima.", 1.18, 1.35, 1.6);

    private final String nombre;
    private final String descripcion;
    private final double factorTiempo; // Multiplicador de tiempo de vuelta
    private final double factorConsumo;
    private final double factorDesgaste;

    Clima(String nombre, String descripcion, double factorTiempo, double factorConsumo, double factorDesgaste) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.factorTiempo = factorTiempo;
        this.factorConsumo = factorConsumo;
        this.factorDesgaste = factorDesgaste;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public double getFactorTiempo() {
        return factorTiempo;
    }

    public double getFactorConsumo() {
        return factorConsumo;
    }

    public double getFactorDesgaste() {
        return factorDesgaste;
    }

    public static Clima desdeString(String str) {
        if (str == null) return SECO;
        String s = str.trim().toLowerCase();
        if (s.contains("lluv") || s.contains("rain")) return LLUVIOSO;
        if (s.contains("extr") || s.contains("wet")) return EXTREMO;
        return SECO;
    }
}
