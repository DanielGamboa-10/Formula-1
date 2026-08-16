package com.formula1.service;

import com.formula1.data.DataStore;
import com.formula1.model.*;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class SimulacionService {
    private final DataStore store;
    private final Random random;

    public SimulacionService() {
        this.store = DataStore.getInstance();
        this.random = new Random();
    }

    /**
     * Genera un clima aleatorio para la sesión de clasificación.
     * 60% Seco, 30% Lluvioso, 10% Extremo.
     */
    public Clima generarClimaAleatorio() {
        int prob = random.nextInt(100);
        if (prob < 60) {
            return Clima.SECO;
        } else if (prob < 90) {
            return Clima.LLUVIOSO;
        } else {
            return Clima.EXTREMO;
        }
    }

    /**
     * Ejecuta una sesión de clasificación completa en el circuito seleccionado.
     */
    public SesionClasificacion simularClasificacion(Circuito circuito, Clima clima,
                                                     Piloto pilotoUsuario, Vehiculo vehiculoUsuario,
                                                     ConfiguracionVehiculo configUsuario) {
        if (clima == null) {
            clima = generarClimaAleatorio();
        }

        List<ResultadoVuelta> resultados = new ArrayList<>();
        double tiempoBaseCircuito = (circuito.getRecordVuelta() != null && circuito.getRecordVuelta().getTiempoSegundos() > 0)
                ? circuito.getRecordVuelta().getTiempoSegundos()
                : (circuito.getLongitudKm() * 16.5);

        // Simular para todos los pilotos registrados
        for (Piloto piloto : store.getPilotos().values()) {
            boolean esUsuario = (pilotoUsuario != null && piloto.getId() == pilotoUsuario.getId());
            Vehiculo vehiculo;
            ConfiguracionVehiculo config;

            if (esUsuario) {
                vehiculo = vehiculoUsuario != null ? vehiculoUsuario : obtenerVehiculoParaPiloto(piloto);
                config = configUsuario != null ? configUsuario : new ConfiguracionVehiculo();
            } else {
                vehiculo = obtenerVehiculoParaPiloto(piloto);
                // IA elige configuración según rol y estilo
                config = generarConfiguracionIA(piloto);
            }

            ResultadoVuelta res = calcularTiempoVuelta(circuito, clima, piloto, vehiculo, config, tiempoBaseCircuito, esUsuario);
            resultados.add(res);
        }

        // Ordenar de menor a mayor tiempo
        Collections.sort(resultados);

        // Asignar posiciones y calcular delta con el líder
        double tiempoLider = resultados.isEmpty() ? 0 : resultados.get(0).getTiempoSegundos();
        for (int i = 0; i < resultados.size(); i++) {
            ResultadoVuelta r = resultados.get(i);
            r.setPosicion(i + 1);
            r.setDiferenciaConLiderSegundos(r.getTiempoSegundos() - tiempoLider);
        }

        Piloto polePosition = resultados.isEmpty() ? null : resultados.get(0).getPiloto();
        String idSesion = "SES-" + System.currentTimeMillis();

        SesionClasificacion sesion = new SesionClasificacion(
                idSesion,
                circuito,
                clima,
                resultados,
                polePosition,
                vehiculoUsuario != null ? vehiculoUsuario.getModelo() : "N/A",
                pilotoUsuario != null ? pilotoUsuario.getNombre() : "N/A",
                configUsuario
        );

        // Guardar en el historial
        store.agregarHistorial(sesion);

        return sesion;
    }

    private Vehiculo obtenerVehiculoParaPiloto(Piloto piloto) {
        // Buscar si algún vehículo lo tiene asignado
        for (Vehiculo v : store.getVehiculos().values()) {
            if (v.getPilotosIds().contains(piloto.getId())) {
                return v;
            }
        }
        // Si no, buscar por coincidencia de equipo
        for (Vehiculo v : store.getVehiculos().values()) {
            if (v.getEquipo().equalsIgnoreCase(piloto.getEquipo())) {
                return v;
            }
        }
        // Retornar el primer vehículo disponible o uno genérico
        return store.getVehiculos().values().stream().findFirst().orElse(null);
    }

    private ConfiguracionVehiculo generarConfiguracionIA(Piloto piloto) {
        if ("Líder".equalsIgnoreCase(piloto.getRol())) {
            return new ConfiguracionVehiculo(ModoConduccion.AGRESIVO, CargaAerodinamica.MEDIA, PresionNeumaticos.ESTANDAR, EstrategiaCombustible.AGRESIVA);
        } else {
            return new ConfiguracionVehiculo(ModoConduccion.NORMAL, CargaAerodinamica.MEDIA, PresionNeumaticos.ESTANDAR, EstrategiaCombustible.BALANCEADA);
        }
    }

    /**
     * Algoritmo de cálculo físico y estratégico de vuelta rápida.
     */
    private ResultadoVuelta calcularTiempoVuelta(Circuito circuito, Clima clima, Piloto piloto,
                                                  Vehiculo vehiculo, ConfiguracionVehiculo config,
                                                  double tiempoBase, boolean esUsuario) {
        // 1. Efecto del vehículo (aceleración y velocidad máxima)
        double deltaVehiculo = 0.0;
        if (vehiculo != null) {
            deltaVehiculo -= ((vehiculo.getVelocidadMaximaKmh() - 340.0) * 0.03); // Auto veloz ahorra tiempo
            deltaVehiculo += ((vehiculo.getAceleracion0a100() - 2.5) * 1.5);     // Mejor aceleración ahorra tiempo
        }

        // 2. Efecto de la configuración del usuario / IA
        double deltaConfig = config.getImpactoTotalTiempoSegundos();

        // 3. Efecto del piloto (Habilidad: 70 a 100, Experiencia: 70 a 100)
        double factorPiloto = ((100.0 - piloto.getHabilidad()) * 0.04) + ((100.0 - piloto.getExperiencia()) * 0.02);

        // 4. Efecto del clima
        double multiplicadorClima = clima.getFactorTiempo();

        // 5. Variabilidad estocástica humana por sector
        double variabilidadAleatoria = ThreadLocalRandom.current().nextDouble(-0.35, 0.45);

        // Cálculo del tiempo total en segundos
        double tiempoTotal = (tiempoBase + deltaVehiculo + deltaConfig + factorPiloto) * multiplicadorClima + variabilidadAleatoria;

        // Asegurar que no sea negativo ni irreal
        if (tiempoTotal < 40.0) tiempoTotal = 40.0;

        // Velocidad media calculada en km/h
        double velocidadMedia = (circuito.getLongitudKm() / (tiempoTotal / 3600.0));

        // Consumo y desgaste de neumáticos
        RendimientoConduccion rend = (vehiculo != null && vehiculo.getRendimiento() != null)
                ? vehiculo.getRendimiento().getPorModo(config.getModoConduccion())
                : new RendimientoConduccion(310, 2.0, 2.2, 2.5, 1.5, 0.9, 2.5);

        double consumo = rend.getConsumo(clima) * config.getImpactoTotalConsumo() * circuito.getFactorConsumoCombustible();
        double desgaste = rend.getDesgaste(clima) * config.getImpactoTotalDesgaste() * circuito.getFactorDesgasteNeumaticos();

        return new ResultadoVuelta(0, piloto, vehiculo, tiempoTotal, velocidadMedia, desgaste, consumo, esUsuario);
    }
}
