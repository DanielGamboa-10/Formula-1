package com.formula1.service;

import com.formula1.data.DataStore;
import com.formula1.model.*;

import java.util.*;

public class ConfiguracionService {
    private final DataStore store;
    private ConfiguracionVehiculo configuracionActual;

    public ConfiguracionService() {
        this.store = DataStore.getInstance();
        // Por defecto
        this.configuracionActual = new ConfiguracionVehiculo(
                ModoConduccion.NORMAL,
                CargaAerodinamica.MEDIA,
                PresionNeumaticos.ESTANDAR,
                EstrategiaCombustible.BALANCEADA
        );
    }

    public ConfiguracionVehiculo getConfiguracionActual() {
        return configuracionActual;
    }

    public void setConfiguracionActual(ConfiguracionVehiculo configuracionActual) {
        this.configuracionActual = configuracionActual;
    }

    public void guardarConfiguracion(String nombre, ConfiguracionVehiculo config) {
        store.getConfiguracionesGuardadas().put(nombre, new ConfiguracionVehiculo(
                config.getModoConduccion(),
                config.getCargaAerodinamica(),
                config.getPresionNeumaticos(),
                config.getEstrategiaCombustible()
        ));
        if (store.isAutoSave()) {
            store.guardar();
        }
    }

    public Map<String, ConfiguracionVehiculo> listarConfiguracionesGuardadas() {
        return Collections.unmodifiableMap(store.getConfiguracionesGuardadas());
    }

    public Optional<ConfiguracionVehiculo> cargarConfiguracion(String nombre) {
        return Optional.ofNullable(store.getConfiguracionesGuardadas().get(nombre));
    }

    public boolean eliminarConfiguracion(String nombre) {
        boolean removed = store.getConfiguracionesGuardadas().remove(nombre) != null;
        if (removed && store.isAutoSave()) {
            store.guardar();
        }
        return removed;
    }
}
