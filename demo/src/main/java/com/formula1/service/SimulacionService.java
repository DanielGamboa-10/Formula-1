package com.formula1.service;

import com.formula1.data.DataStore;
import com.formula1.model.*;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Servicio encargado del motor de simulación de clasificación y física de carrera.
 * Calcula tiempos de vuelta, factores climáticos, telemetría y determina la Pole Position.
 */
public class SimulacionService {
    private final DataStore store;
    private final Random random;

    public SimulacionService() {
        this.store = DataStore.getInstance();
        this.random = new Random();
    }

    /**
     * Generador estocástico de clima según probabilidades de carrera:
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
     * Ejecuta una sesión de clasificación completa para los 20 monoplazas.
     * Ordena la parrilla y corona la Pole Position.
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

        // Simula la vuelta rápida para cada piloto en la base de datos
        for (Piloto piloto : store.getPilotos().values()) {
            boolean esUsuario = (pilotoUsuario != null && piloto.getId() == pilotoUsuario.getId());
            Vehiculo vehiculo;
            ConfiguracionVehiculo config;

            if (esUsuario) {
                // Monoplaza y setup configurados por el usuario
                vehiculo = vehiculoUsuario != null ? vehiculoUsuario : obtenerVehiculoParaPiloto(piloto);
                config = configUsuario != null ? configUsuario : new ConfiguracionVehiculo();
            } else {
                // Monoplaza oficial y setup estratégico de la IA
                vehiculo = obtenerVehiculoParaPiloto(piloto);
                config = generarConfiguracionIA(piloto);
            }

            ResultadoVuelta res = calcularTiempoVuelta(circuito, clima, piloto, vehiculo, config, tiempoBaseCircuito, esUsuario);
            resultados.add(res);
        }

        // Ordenamiento polimórfico mediante Comparable<ResultadoVuelta> (menor tiempo a mayor)
        Collections.sort(resultados);

        // Asignación de posiciones (P1 a P20) y cálculo de diferencias (+delta s)
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

        // Persistencia de la sesión en el historial de DataStore
        store.agregarHistorial(sesion);

        return sesion;
    }

    // Localiza el monoplaza asignado al piloto por escudería o ID
    private Vehiculo obtenerVehiculoParaPiloto(Piloto piloto) {
        for (Vehiculo v : store.getVehiculos().values()) {
            if (v.getPilotosIds().contains(piloto.getId())) {
                return v;
            }
        }
        for (Vehiculo v : store.getVehiculos().values()) {
            if (v.getEquipo().equalsIgnoreCase(piloto.getEquipo())) {
                return v;
            }
        }
        return store.getVehiculos().values().stream().findFirst().orElse(null);
    }

    // Configuración estratégica de los monoplazas rivales controlados por IA
    private ConfiguracionVehiculo generarConfiguracionIA(Piloto piloto) {
        if ("Líder".equalsIgnoreCase(piloto.getRol())) {
            return new ConfiguracionVehiculo(ModoConduccion.AGRESIVO, CargaAerodinamica.MEDIA, PresionNeumaticos.ESTANDAR, EstrategiaCombustible.AGRESIVA);
        } else {
            return new ConfiguracionVehiculo(ModoConduccion.NORMAL, CargaAerodinamica.MEDIA, PresionNeumaticos.ESTANDAR, EstrategiaCombustible.BALANCEADA);
        }
    }

    /**
     * Algoritmo de cálculo físico y estratégico de vuelta rápida multivariable.
     */
    private ResultadoVuelta calcularTiempoVuelta(Circuito circuito, Clima clima, Piloto piloto,
                                                  Vehiculo vehiculo, ConfiguracionVehiculo config,
                                                  double tiempoBase, boolean esUsuario) {
        // 1. Rendimiento del monoplaza (velocidad punta y aceleración 0-100)
        double deltaVehiculo = 0.0;
        if (vehiculo != null) {
            deltaVehiculo -= ((vehiculo.getVelocidadMaximaKmh() - 340.0) * 0.03);
            deltaVehiculo += ((vehiculo.getAceleracion0a100() - 2.5) * 1.5);
        }

        // 2. Impacto de los reglajes de ingeniería (alerones, presión, combustible, modo motor)
        double deltaConfig = config.getImpactoTotalTiempoSegundos();

        // 3. Impacto de la destreza y experiencia del piloto
        double factorPiloto = ((100.0 - piloto.getHabilidad()) * 0.04) + ((100.0 - piloto.getExperiencia()) * 0.02);

        // 4. Multiplicador meteorológico (pista seca vs húmeda vs lluvia torrencial)
        double multiplicadorClima = clima.getFactorTiempo();

        // 5. Varianza estocástica humana y de tráfico por sector
        double variabilidadAleatoria = ThreadLocalRandom.current().nextDouble(-0.35, 0.45);

        // Cálculo consolidado del tiempo de vuelta en segundos
        double tiempoTotal = (tiempoBase + deltaVehiculo + deltaConfig + factorPiloto) * multiplicadorClima + variabilidadAleatoria;
        if (tiempoTotal < 40.0) tiempoTotal = 40.0;

        // Velocidad media calculada en km/h
        double velocidadMedia = (circuito.getLongitudKm() / (tiempoTotal / 3600.0));

        // Consumo de combustible y degradación de neumáticos según rendimiento y pista
        RendimientoConduccion rend = (vehiculo != null && vehiculo.getRendimiento() != null)
                ? vehiculo.getRendimiento().getPorModo(config.getModoConduccion())
                : new RendimientoConduccion(310, 2.0, 2.2, 2.5, 1.5, 0.9, 2.5);

        double consumo = rend.getConsumo(clima) * config.getImpactoTotalConsumo() * circuito.getFactorConsumoCombustible();
        double desgaste = rend.getDesgaste(clima) * config.getImpactoTotalDesgaste() * circuito.getFactorDesgasteNeumaticos();

        return new ResultadoVuelta(0, piloto, vehiculo, tiempoTotal, velocidadMedia, desgaste, consumo, esUsuario);
    }
}
