package com.formula1.model;

import java.io.Serializable;

/**
 * Encapsula los reglajes de ingeniería personalizados del monoplaza.
 * Calcula el impacto consolidado en tiempo de vuelta, consumo y desgaste.
 */
public class ConfiguracionVehiculo implements Serializable {
    private ModoConduccion modoConduccion;             // Normal, Agresivo o Ahorro
    private CargaAerodinamica cargaAerodinamica;       // Baja, Media o Alta
    private PresionNeumaticos presionNeumaticos;       // Baja, Estándar o Alta
    private EstrategiaCombustible estrategiaCombustible; // Agresiva, Balanceada o Ahorro

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

    // Getters y Setters
    public ModoConduccion getModoConduccion() { return modoConduccion; }
    public void setModoConduccion(ModoConduccion modoConduccion) { this.modoConduccion = modoConduccion; }

    public CargaAerodinamica getCargaAerodinamica() { return cargaAerodinamica; }
    public void setCargaAerodinamica(CargaAerodinamica cargaAerodinamica) { this.cargaAerodinamica = cargaAerodinamica; }

    public PresionNeumaticos getPresionNeumaticos() { return presionNeumaticos; }
    public void setPresionNeumaticos(PresionNeumaticos presionNeumaticos) { this.presionNeumaticos = presionNeumaticos; }

    public EstrategiaCombustible getEstrategiaCombustible() { return estrategiaCombustible; }
    public void setEstrategiaCombustible(EstrategiaCombustible estrategiaCombustible) { this.estrategiaCombustible = estrategiaCombustible; }

    // Calcula la variación total de tiempo en segundos (Δt) resultante de todos los reglajes
    public double getImpactoTotalTiempoSegundos() {
        return modoConduccion.getDeltaTiempoSegundos()
                + cargaAerodinamica.getDeltaTiempo()
                + presionNeumaticos.getDeltaTiempo()
                + estrategiaCombustible.getDeltaTiempo();
    }

    // Calcula el multiplicador combinado de consumo de combustible
    public double getImpactoTotalConsumo() {
        return modoConduccion.getMultiplicadorConsumo() * estrategiaCombustible.getFactorConsumo();
    }

    // Calcula el multiplicador combinado de degradación de neumáticos
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
