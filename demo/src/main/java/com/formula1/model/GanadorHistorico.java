package com.formula1.model;

import java.io.Serializable;

public class GanadorHistorico implements Serializable {
    private static final long serialVersionUID = 1L;

    private int temporada;
    private int pilotoId;

    public GanadorHistorico() {
    }

    public GanadorHistorico(int temporada, int pilotoId) {
        this.temporada = temporada;
        this.pilotoId = pilotoId;
    }

    public int getTemporada() {
        return temporada;
    }

    public void setTemporada(int temporada) {
        this.temporada = temporada;
    }

    public int getPilotoId() {
        return pilotoId;
    }

    public void setPilotoId(int pilotoId) {
        this.pilotoId = pilotoId;
    }

    @Override
    public String toString() {
        return "Temporada " + temporada + " -> Piloto ID #" + pilotoId;
    }
}
