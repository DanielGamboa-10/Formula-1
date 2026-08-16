package com.formula1.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Circuito implements Serializable {
    private String nombre;
    private String pais;
    private double longitudKm;
    private int vueltas;
    private String descripcion;
    private RecordVuelta recordVuelta;
    private List<GanadorHistorico> ganadores;
    private String climaHabitual; // e.g. "Seco", "Lluvioso"
    private double factorDesgasteNeumaticos; // 1.0 estándar, >1.0 más abrasivo
    private double factorConsumoCombustible; // 1.0 estándar
    private String imagenUrl;

    public Circuito() {
        this.ganadores = new ArrayList<>();
        this.factorDesgasteNeumaticos = 1.0;
        this.factorConsumoCombustible = 1.0;
        this.climaHabitual = "Seco";
    }

    public Circuito(String nombre, String pais, double longitudKm, int vueltas, String descripcion,
                    RecordVuelta recordVuelta, List<GanadorHistorico> ganadores, String imagenUrl) {
        this.nombre = nombre;
        this.pais = pais;
        this.longitudKm = longitudKm;
        this.vueltas = vueltas;
        this.descripcion = descripcion;
        this.recordVuelta = recordVuelta;
        this.ganadores = ganadores != null ? new ArrayList<>(ganadores) : new ArrayList<>();
        this.imagenUrl = imagenUrl;
        this.climaHabitual = calcularClimaHabitual(nombre);
        this.factorDesgasteNeumaticos = calcularFactorDesgaste(nombre);
        this.factorConsumoCombustible = calcularFactorConsumo(nombre);
    }

    private String calcularClimaHabitual(String nombreCircuito) {
        if (nombreCircuito == null) return "Seco";
        String lower = nombreCircuito.toLowerCase();
        if (lower.contains("spa") || lower.contains("silverstone") || lower.contains("interlagos")) {
            return "Lluvioso / Variable";
        } else if (lower.contains("yas marina") || lower.contains("mónaco") || lower.contains("monaco")) {
            return "Seco y Cálido";
        }
        return "Templado / Seco";
    }

    private double calcularFactorDesgaste(String nombreCircuito) {
        if (nombreCircuito == null) return 1.0;
        String lower = nombreCircuito.toLowerCase();
        if (lower.contains("silverstone") || lower.contains("suzuka")) return 1.35; // Curvas rápidas
        if (lower.contains("monza")) return 0.85; // Rectas
        if (lower.contains("mónaco") || lower.contains("monaco")) return 0.75; // Lento
        if (lower.contains("spa")) return 1.20;
        return 1.0;
    }

    private double calcularFactorConsumo(String nombreCircuito) {
        if (nombreCircuito == null) return 1.0;
        String lower = nombreCircuito.toLowerCase();
        if (lower.contains("monza") || lower.contains("spa")) return 1.30; // A fondo
        if (lower.contains("mónaco") || lower.contains("monaco")) return 0.80; // Poca aceleración sostenida
        return 1.05;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public double getLongitudKm() {
        return longitudKm;
    }

    public void setLongitudKm(double longitudKm) {
        this.longitudKm = longitudKm;
    }

    public int getVueltas() {
        return vueltas;
    }

    public void setVueltas(int vueltas) {
        this.vueltas = vueltas;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public RecordVuelta getRecordVuelta() {
        return recordVuelta;
    }

    public void setRecordVuelta(RecordVuelta recordVuelta) {
        this.recordVuelta = recordVuelta;
    }

    public List<GanadorHistorico> getGanadores() {
        return ganadores;
    }

    public void setGanadores(List<GanadorHistorico> ganadores) {
        this.ganadores = ganadores;
    }

    public String getClimaHabitual() {
        return climaHabitual;
    }

    public void setClimaHabitual(String climaHabitual) {
        this.climaHabitual = climaHabitual;
    }

    public double getFactorDesgasteNeumaticos() {
        return factorDesgasteNeumaticos;
    }

    public void setFactorDesgasteNeumaticos(double factorDesgasteNeumaticos) {
        this.factorDesgasteNeumaticos = factorDesgasteNeumaticos;
    }

    public double getFactorConsumoCombustible() {
        return factorConsumoCombustible;
    }

    public void setFactorConsumoCombustible(double factorConsumoCombustible) {
        this.factorConsumoCombustible = factorConsumoCombustible;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Circuito circuito = (Circuito) o;
        return Objects.equals(nombre, circuito.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre);
    }

    @Override
    public String toString() {
        return String.format("%-28s | %-16s | Long: %4.2f km | Vueltas: %2d | Récord: %s",
                nombre, pais, longitudKm, vueltas, (recordVuelta != null ? recordVuelta.toString() : "N/A"));
    }
}
