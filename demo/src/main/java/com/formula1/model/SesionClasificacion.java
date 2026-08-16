package com.formula1.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class SesionClasificacion implements Serializable {
    private String id;
    private LocalDateTime fechaHora;
    private Circuito circuito;
    private Clima clima;
    private List<ResultadoVuelta> resultados;
    private Piloto polePosition;
    private String vehiculoUsuario;
    private String pilotoUsuario;
    private ConfiguracionVehiculo configuracionUsuario;

    public SesionClasificacion() {
        this.resultados = new ArrayList<>();
        this.fechaHora = LocalDateTime.now();
    }

    public SesionClasificacion(String id, Circuito circuito, Clima clima, List<ResultadoVuelta> resultados,
                               Piloto polePosition, String vehiculoUsuario, String pilotoUsuario,
                               ConfiguracionVehiculo configuracionUsuario) {
        this.id = id;
        this.fechaHora = LocalDateTime.now();
        this.circuito = circuito;
        this.clima = clima;
        this.resultados = resultados != null ? new ArrayList<>(resultados) : new ArrayList<>();
        this.polePosition = polePosition;
        this.vehiculoUsuario = vehiculoUsuario;
        this.pilotoUsuario = pilotoUsuario;
        this.configuracionUsuario = configuracionUsuario;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getFechaHoraFormateada() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return fechaHora != null ? fechaHora.format(formatter) : "N/A";
    }

    public Circuito getCircuito() {
        return circuito;
    }

    public void setCircuito(Circuito circuito) {
        this.circuito = circuito;
    }

    public Clima getClima() {
        return clima;
    }

    public void setClima(Clima clima) {
        this.clima = clima;
    }

    public List<ResultadoVuelta> getResultados() {
        return resultados;
    }

    public void setResultados(List<ResultadoVuelta> resultados) {
        this.resultados = resultados;
    }

    public Piloto getPolePosition() {
        return polePosition;
    }

    public void setPolePosition(Piloto polePosition) {
        this.polePosition = polePosition;
    }

    public String getVehiculoUsuario() {
        return vehiculoUsuario;
    }

    public void setVehiculoUsuario(String vehiculoUsuario) {
        this.vehiculoUsuario = vehiculoUsuario;
    }

    public String getPilotoUsuario() {
        return pilotoUsuario;
    }

    public void setPilotoUsuario(String pilotoUsuario) {
        this.pilotoUsuario = pilotoUsuario;
    }

    public ConfiguracionVehiculo getConfiguracionUsuario() {
        return configuracionUsuario;
    }

    public void setConfiguracionUsuario(ConfiguracionVehiculo configuracionUsuario) {
        this.configuracionUsuario = configuracionUsuario;
    }

    public ResultadoVuelta getResultadoUsuario() {
        for (ResultadoVuelta res : resultados) {
            if (res.isEsUsuario()) {
                return res;
            }
        }
        return null;
    }
}
