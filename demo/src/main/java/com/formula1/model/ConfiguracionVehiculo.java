package com.formula1.model;

import java.io.Serializable;

public class ConfiguracionVehiculo implements Serializable {
    private ModoConduccion modoConduccion;
    private CargaAerodinamica cargaAerodinamica;
    private PresionNeumaticos presionNeumaticos;
    private EstrategiaCombustible estrategiaCombustible;

    public ConfiguracionVehiculo() {
        this.modoConduccion = ModoConduccion.NORMAL;
        this.cargaAerodinamica = CargaAerodinamica.MEDIA;
        this.presionNeumaticos = PresionNeumaticos.ESTANDAR;
        this.estrategiaCombustible = EstrategiaCombustible.BALANCEADA;
    }

    public ConfiguracionVehiculo(ModoConduccion modoConduccion, CargaAerodinamica cargaAerodinamica,
                                 PresionNeumaticos presionNeumaticos, EstrategiaCombustible estrategiaCombustible) {
        this.modoConduccion = modoConduccion != null ? modoConduccion : ModoConduccion.NORMAL;
        this.cargaAerodinamica = cargaAerodinamica != null ? cargaAerodinamica : CargaAerodinamica.MEDIA;
        this.presionNeumaticos = presionNeumaticos != null ? presionNeumaticos : PresionNeumaticos.ESTANDAR;
        this.estrategiaCombustible = estrategiaCombustible != null ? estrategiaCombustible : EstrategiaCombustible.BALANCEADA;
    }

    public ModoConduccion getModoConduccion() {
        return modoConduccion;
    }

    public void setModoConduccion(ModoConduccion modoConduccion) {
        this.modoConduccion = modoConduccion;
    }

    public CargaAerodinamica getCargaAerodinamica() {
        return cargaAerodinamica;
    }

    public void setCargaAerodinamica(CargaAerodinamica cargaAerodinamica) {
        this.cargaAerodinamica = cargaAerodinamica;
    }

    public PresionNeumaticos getPresionNeumaticos() {
        return presionNeumaticos;
    }

    public void setPresionNeumaticos(PresionNeumaticos presionNeumaticos) {
        this.presionNeumaticos = presionNeumaticos;
    }

    public EstrategiaCombustible getEstrategiaCombustible() {
        return estrategiaCombustible;
    }

    public void setEstrategiaCombustible(EstrategiaCombustible estrategiaCombustible) {
        this.estrategiaCombustible = estrategiaCombustible;
    }

    public double getImpactoTotalTiempoSegundos() {
        return modoConduccion.getDeltaTiempoSegundos()
                + cargaAerodinamica.getDeltaTiempo()
                + presionNeumaticos.getDeltaTiempo()
                + estrategiaCombustible.getDeltaTiempo();
    }

    public double getImpactoTotalConsumo() {
        return modoConduccion.getMultiplicadorConsumo() * estrategiaCombustible.getFactorConsumo();
    }

    public double getImpactoTotalDesgaste() {
        return modoConduccion.getMultiplicadorDesgaste()
                * cargaAerodinamica.getImpactoDesgaste()
                * presionNeumaticos.getFactorDesgaste();
    }

    @Override
    public String toString() {
        return String.format("[Modo: %s | Aero: %s | Presión: %s | Combustible: %s]",
                modoConduccion.getNombre(), cargaAerodinamica.getNombre(),
                presionNeumaticos.getNombre(), estrategiaCombustible.getNombre());
    }
}
