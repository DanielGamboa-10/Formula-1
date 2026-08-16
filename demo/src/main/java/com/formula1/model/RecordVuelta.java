package com.formula1.model;

import java.io.Serializable;

public class RecordVuelta implements Serializable {
    private static final long serialVersionUID = 1L;

    private String tiempo; // e.g. "1:10.166"
    private String piloto;
    private int anio;
    private double tiempoSegundos; // e.g. 70.166

    public RecordVuelta() {
    }

    public RecordVuelta(String tiempo, String piloto, int anio) {
        this.tiempo = tiempo;
        this.piloto = piloto;
        this.anio = anio;
        this.tiempoSegundos = parseTiempoToSegundos(tiempo);
    }

    public static double parseTiempoToSegundos(String tiempoStr) {
        if (tiempoStr == null || tiempoStr.trim().isEmpty()) {
            return 80.0;
        }
        try {
            String[] parts = tiempoStr.trim().split(":");
            if (parts.length == 2) {
                int minutos = Integer.parseInt(parts[0]);
                double segundos = Double.parseDouble(parts[1]);
                return (minutos * 60.0) + segundos;
            } else {
                return Double.parseDouble(tiempoStr);
            }
        } catch (Exception e) {
            return 80.0;
        }
    }

    public static String formatSegundosToTiempo(double segundosTotales) {
        int minutos = (int) (segundosTotales / 60);
        double segRestantes = segundosTotales - (minutos * 60);
        return String.format("%d:%06.3f", minutos, segRestantes);
    }

    public String getTiempo() {
        return tiempo;
    }

    public void setTiempo(String tiempo) {
        this.tiempo = tiempo;
        this.tiempoSegundos = parseTiempoToSegundos(tiempo);
    }

    public String getPiloto() {
        return piloto;
    }

    public void setPiloto(String piloto) {
        this.piloto = piloto;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public double getTiempoSegundos() {
        if (tiempoSegundos <= 0 && tiempo != null) {
            tiempoSegundos = parseTiempoToSegundos(tiempo);
        }
        return tiempoSegundos;
    }

    public void setTiempoSegundos(double tiempoSegundos) {
        this.tiempoSegundos = tiempoSegundos;
    }

    @Override
    public String toString() {
        return String.format("%s (%s, %d)", tiempo, piloto, anio);
    }
}
