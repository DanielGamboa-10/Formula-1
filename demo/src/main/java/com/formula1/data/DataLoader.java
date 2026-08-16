package com.formula1.data;

import com.formula1.model.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DataLoader {

    public static void cargarDatosIniciales() {
        DataStore store = DataStore.getInstance();

        // 1. Pilotos (20 pilotos oficiales del JSON)
        cargarPilotos(store);

        // 2. Equipos
        cargarEquipos(store);

        // 3. Circuitos
        cargarCircuitos(store);

        // 4. Vehículos con especificaciones y rendimientos
        cargarVehiculos(store);

        // 5. Configuración por defecto
        store.getConfiguracionesGuardadas().put("Predeterminada", new ConfiguracionVehiculo(
                ModoConduccion.NORMAL,
                CargaAerodinamica.MEDIA,
                PresionNeumaticos.ESTANDAR,
                EstrategiaCombustible.BALANCEADA
        ));
    }

    private static void cargarPilotos(DataStore store) {
        Object[][] datosPilotos = {
                {1, "Max Verstappen", "Red Bull Racing", "Líder", 96, 98},
                {2, "Sergio Pérez", "Red Bull Racing", "Escudero", 90, 88},
                {3, "Lewis Hamilton", "Mercedes-AMG Petronas", "Líder", 98, 95},
                {4, "George Russell", "Mercedes-AMG Petronas", "Escudero", 88, 91},
                {5, "Charles Leclerc", "Ferrari", "Líder", 91, 95},
                {6, "Carlos Sainz", "Ferrari", "Escudero", 89, 90},
                {7, "Lando Norris", "McLaren", "Líder", 89, 93},
                {8, "Oscar Piastri", "McLaren", "Escudero", 84, 89},
                {9, "Fernando Alonso", "Aston Martin", "Líder", 99, 94},
                {10, "Lance Stroll", "Aston Martin", "Escudero", 82, 80},
                {11, "Esteban Ocon", "Alpine", "Líder", 85, 84},
                {12, "Pierre Gasly", "Alpine", "Escudero", 86, 85},
                {13, "Valtteri Bottas", "Alfa Romeo", "Líder", 92, 85},
                {14, "Zhou Guanyu", "Alfa Romeo", "Escudero", 80, 80},
                {15, "Kevin Magnussen", "Haas", "Líder", 86, 82},
                {16, "Nico Hülkenberg", "Haas", "Escudero", 88, 85},
                {17, "Yuki Tsunoda", "AlphaTauri", "Líder", 83, 84},
                {18, "Daniel Ricciardo", "AlphaTauri", "Escudero", 91, 86},
                {19, "Alexander Albon", "Williams", "Líder", 87, 87},
                {20, "Logan Sargeant", "Williams", "Escudero", 78, 77}
        };

        for (Object[] row : datosPilotos) {
            Piloto p = new Piloto((int) row[0], (String) row[1], (String) row[2], (String) row[3], (int) row[4], (int) row[5]);
            store.getPilotos().put(p.getId(), p);
        }
    }

    private static void cargarEquipos(DataStore store) {
        store.getEquipos().put("Red Bull Racing", new Equipo(
                "Red Bull Racing", "Austria", "Honda",
                Arrays.asList(1, 2),
                "https://upload.wikimedia.org/wikipedia/commons/b/bb/Red_Bull_Racing_Logo.svg"
        ));

        store.getEquipos().put("Mercedes-AMG Petronas", new Equipo(
                "Mercedes-AMG Petronas", "Alemania", "Mercedes",
                Arrays.asList(3, 4),
                "https://upload.wikimedia.org/wikipedia/commons/3/32/Mercedes_AMG_Petronas_F1_Team_logo.svg"
        ));

        store.getEquipos().put("Ferrari", new Equipo(
                "Ferrari", "Italia", "Ferrari",
                Arrays.asList(5, 6),
                "https://upload.wikimedia.org/wikipedia/en/d/d4/Scuderia_Ferrari_Logo.svg"
        ));

        store.getEquipos().put("McLaren", new Equipo(
                "McLaren", "Reino Unido", "Mercedes",
                Arrays.asList(7, 8),
                "https://upload.wikimedia.org/wikipedia/en/6/66/McLaren_Racing_logo.svg"
        ));

        store.getEquipos().put("Aston Martin", new Equipo(
                "Aston Martin", "Reino Unido", "Mercedes",
                Arrays.asList(9, 10),
                "https://upload.wikimedia.org/wikipedia/en/thumb/7/74/Aston_Martin_Aramco_Cognizant_F1_Team_logo.svg"
        ));

        store.getEquipos().put("Alpine", new Equipo(
                "Alpine", "Francia", "Renault",
                Arrays.asList(11, 12),
                "https://upload.wikimedia.org/wikipedia/commons/7/7e/Alpine_F1_Team_Logo.svg"
        ));

        store.getEquipos().put("Alfa Romeo", new Equipo(
                "Alfa Romeo", "Suiza", "Ferrari",
                Arrays.asList(13, 14),
                "https://upload.wikimedia.org/wikipedia/commons/1/14/Alfa_Romeo_F1_Team_Stake_logo.svg"
        ));

        store.getEquipos().put("Haas", new Equipo(
                "Haas", "Estados Unidos", "Ferrari",
                Arrays.asList(15, 16),
                "https://upload.wikimedia.org/wikipedia/commons/d/d4/Haas_F1_Team_Logo.svg"
        ));

        store.getEquipos().put("AlphaTauri", new Equipo(
                "AlphaTauri", "Italia", "Honda",
                Arrays.asList(17, 18),
                "https://upload.wikimedia.org/wikipedia/commons/5/52/Scuderia_AlphaTauri_logo.svg"
        ));

        store.getEquipos().put("Williams", new Equipo(
                "Williams", "Reino Unido", "Mercedes",
                Arrays.asList(19, 20),
                "https://upload.wikimedia.org/wikipedia/commons/0/07/Williams_Racing_2020_Logo.svg"
        ));
    }

    private static void cargarCircuitos(DataStore store) {
        // 1. Mónaco
        store.getCircuitos().put("Circuito de Mónaco", new Circuito(
                "Circuito de Mónaco", "Mónaco", 3.34, 78,
                "Uno de los circuitos más prestigiosos y difíciles del calendario, conocido por sus calles angostas y la falta de zonas de adelantamiento.",
                new RecordVuelta("1:10.166", "Lewis Hamilton", 2019),
                Arrays.asList(new GanadorHistorico(2021, 1), new GanadorHistorico(2022, 2), new GanadorHistorico(2023, 1)),
                "https://upload.wikimedia.org/wikipedia/commons/4/4e/Monte_Carlo_Formula_1_track_map.svg"
        ));

        // 2. Silverstone
        store.getCircuitos().put("Silverstone", new Circuito(
                "Silverstone", "Reino Unido", 5.89, 52,
                "Uno de los circuitos más rápidos del calendario, con curvas de alta velocidad como Maggotts y Becketts.",
                new RecordVuelta("1:27.097", "Max Verstappen", 2020),
                Arrays.asList(new GanadorHistorico(2021, 3), new GanadorHistorico(2022, 5), new GanadorHistorico(2023, 1)),
                "https://upload.wikimedia.org/wikipedia/commons/5/5e/Silverstone_Circuit_2020_layout.png"
        ));

        // 3. Spa-Francorchamps
        store.getCircuitos().put("Circuito de Spa-Francorchamps", new Circuito(
                "Circuito de Spa-Francorchamps", "Bélgica", 7.00, 44,
                "Famoso por la curva Eau Rouge y la larga recta de Kemmel, un circuito donde la potencia del motor es clave.",
                new RecordVuelta("1:46.286", "Valtteri Bottas", 2018),
                Arrays.asList(new GanadorHistorico(2021, 1), new GanadorHistorico(2022, 1), new GanadorHistorico(2023, 1)),
                "https://upload.wikimedia.org/wikipedia/commons/1/1e/Circuit_Spa_2018.png"
        ));

        // 4. Monza
        store.getCircuitos().put("Circuito de Monza", new Circuito(
                "Circuito de Monza", "Italia", 5.79, 53,
                "Conocido como 'El Templo de la Velocidad', Monza es el circuito más rápido del calendario con largas rectas y chicanes icónicas.",
                new RecordVuelta("1:21.046", "Rubens Barrichello", 2004),
                Arrays.asList(new GanadorHistorico(2021, 2), new GanadorHistorico(2022, 1), new GanadorHistorico(2023, 1)),
                "https://upload.wikimedia.org/wikipedia/commons/3/3e/Monza_track_map.svg"
        ));

        // 5. Interlagos
        store.getCircuitos().put("Interlagos", new Circuito(
                "Interlagos", "Brasil", 4.31, 71,
                "Interlagos es un circuito legendario con cambios de elevación y un trazado técnico que ha sido sede de algunas de las carreras más emocionantes.",
                new RecordVuelta("1:10.540", "Valtteri Bottas", 2018),
                Arrays.asList(new GanadorHistorico(2021, 3), new GanadorHistorico(2022, 1), new GanadorHistorico(2023, 1)),
                "https://upload.wikimedia.org/wikipedia/commons/2/23/Aut%C3%B3dromo_Jos%C3%A9_Carlos_Pace_%28Interlagos%29.svg"
        ));

        // 6. Yas Marina
        store.getCircuitos().put("Circuito de Yas Marina", new Circuito(
                "Circuito de Yas Marina", "Emiratos Árabes Unidos", 5.28, 58,
                "Ubicado en Abu Dhabi, es famoso por ser el circuito donde se definen muchos campeonatos, con un diseño moderno y una espectacular carrera nocturna.",
                new RecordVuelta("1:39.283", "Lewis Hamilton", 2019),
                Arrays.asList(new GanadorHistorico(2021, 1), new GanadorHistorico(2022, 1), new GanadorHistorico(2023, 3)),
                "https://upload.wikimedia.org/wikipedia/commons/0/0a/Yas_Marina_Circuit_2021_layout.svg"
        ));

        // 7. Suzuka
        store.getCircuitos().put("Circuito de Suzuka", new Circuito(
                "Circuito de Suzuka", "Japón", 5.81, 53,
                "Un circuito desafiante con un diseño en forma de ocho, famoso por sus curvas de alta velocidad como 130R y la 'S' de Senna.",
                new RecordVuelta("1:30.983", "Lewis Hamilton", 2019),
                Arrays.asList(new GanadorHistorico(2021, 1), new GanadorHistorico(2022, 1), new GanadorHistorico(2023, 1)),
                "https://upload.wikimedia.org/wikipedia/commons/e/eb/Suzuka_circuit_map--2005.svg"
        ));
    }

    private static void cargarVehiculos(DataStore store) {
        // Red Bull Racing - RB20
        RendimientoVehiculo rendRB20 = new RendimientoVehiculo(
                new RendimientoConduccion(320, 1.9, 2.1, 2.4, 1.5, 0.8, 2.5),
                new RendimientoConduccion(340, 2.4, 2.6, 3.0, 2.2, 1.2, 3.5),
                new RendimientoConduccion(300, 1.6, 1.8, 2.1, 1.0, 0.5, 1.8)
        );
        store.getVehiculos().put("RB20", new Vehiculo(
                "Red Bull Racing", "RB20", "Honda", 360, 2.5,
                Arrays.asList(1, 2), rendRB20,
                "https://upload.wikimedia.org/wikipedia/commons/8/89/Max_Verstappen_2023_Austria_FP2_%28cropped%29.jpg"
        ));

        // Mercedes - W15
        RendimientoVehiculo rendW15 = new RendimientoVehiculo(
                new RendimientoConduccion(315, 2.0, 2.2, 2.5, 1.6, 0.9, 2.6),
                new RendimientoConduccion(335, 2.6, 2.8, 3.2, 2.3, 1.4, 3.8),
                new RendimientoConduccion(295, 1.7, 1.9, 2.2, 1.1, 0.6, 1.9)
        );
        store.getVehiculos().put("W15", new Vehiculo(
                "Mercedes-AMG Petronas", "W15", "Mercedes", 355, 2.6,
                Arrays.asList(3, 4), rendW15,
                "https://upload.wikimedia.org/wikipedia/commons/8/87/Lewis_Hamilton_2022_Imola.jpg"
        ));

        // Ferrari - SF-24
        RendimientoVehiculo rendSF24 = new RendimientoVehiculo(
                new RendimientoConduccion(318, 2.1, 2.3, 2.6, 1.7, 1.0, 2.7),
                new RendimientoConduccion(338, 2.7, 2.9, 3.3, 2.4, 1.5, 3.9),
                new RendimientoConduccion(298, 1.8, 2.0, 2.3, 1.2, 0.7, 2.0)
        );
        store.getVehiculos().put("SF-24", new Vehiculo(
                "Ferrari", "SF-24", "Ferrari", 358, 2.5,
                Arrays.asList(5, 6), rendSF24,
                "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Scuderia_Ferrari_Logo.svg"
        ));

        // McLaren - MCL38
        RendimientoVehiculo rendMCL38 = new RendimientoVehiculo(
                new RendimientoConduccion(317, 2.0, 2.2, 2.5, 1.6, 0.9, 2.5),
                new RendimientoConduccion(337, 2.5, 2.7, 3.1, 2.2, 1.3, 3.6),
                new RendimientoConduccion(297, 1.7, 1.9, 2.2, 1.1, 0.6, 1.9)
        );
        store.getVehiculos().put("MCL38", new Vehiculo(
                "McLaren", "MCL38", "Mercedes", 356, 2.55,
                Arrays.asList(7, 8), rendMCL38,
                "https://upload.wikimedia.org/wikipedia/en/6/66/McLaren_Racing_logo.svg"
        ));

        // Aston Martin - AMR24
        RendimientoVehiculo rendAMR24 = new RendimientoVehiculo(
                new RendimientoConduccion(314, 2.05, 2.25, 2.55, 1.65, 0.95, 2.65),
                new RendimientoConduccion(333, 2.65, 2.85, 3.25, 2.35, 1.45, 3.85),
                new RendimientoConduccion(294, 1.75, 1.95, 2.25, 1.15, 0.65, 1.95)
        );
        store.getVehiculos().put("AMR24", new Vehiculo(
                "Aston Martin", "AMR24", "Mercedes", 353, 2.65,
                Arrays.asList(9, 10), rendAMR24,
                "https://upload.wikimedia.org/wikipedia/en/thumb/7/74/Aston_Martin_Aramco_Cognizant_F1_Team_logo.svg"
        ));

        // Alpine - A524
        RendimientoVehiculo rendA524 = new RendimientoVehiculo(
                new RendimientoConduccion(310, 2.1, 2.3, 2.6, 1.7, 1.0, 2.8),
                new RendimientoConduccion(330, 2.7, 2.9, 3.3, 2.4, 1.5, 4.0),
                new RendimientoConduccion(290, 1.8, 2.0, 2.3, 1.2, 0.7, 2.1)
        );
        store.getVehiculos().put("A524", new Vehiculo(
                "Alpine", "A524", "Renault", 350, 2.7,
                Arrays.asList(11, 12), rendA524,
                "https://upload.wikimedia.org/wikipedia/commons/7/7e/Alpine_F1_Team_Logo.svg"
        ));

        // Alfa Romeo (Stake) - C44
        RendimientoVehiculo rendC44 = new RendimientoVehiculo(
                new RendimientoConduccion(309, 2.1, 2.3, 2.6, 1.7, 1.0, 2.8),
                new RendimientoConduccion(328, 2.7, 2.9, 3.3, 2.4, 1.5, 4.0),
                new RendimientoConduccion(289, 1.8, 2.0, 2.3, 1.2, 0.7, 2.1)
        );
        store.getVehiculos().put("C44", new Vehiculo(
                "Alfa Romeo", "C44", "Ferrari", 348, 2.75,
                Arrays.asList(13, 14), rendC44,
                "https://upload.wikimedia.org/wikipedia/commons/1/14/Alfa_Romeo_F1_Team_Stake_logo.svg"
        ));

        // Haas - VF-24
        RendimientoVehiculo rendVF24 = new RendimientoVehiculo(
                new RendimientoConduccion(311, 2.1, 2.3, 2.6, 1.75, 1.05, 2.85),
                new RendimientoConduccion(331, 2.7, 2.9, 3.3, 2.5, 1.6, 4.1),
                new RendimientoConduccion(291, 1.8, 2.0, 2.3, 1.25, 0.75, 2.15)
        );
        store.getVehiculos().put("VF-24", new Vehiculo(
                "Haas", "VF-24", "Ferrari", 351, 2.68,
                Arrays.asList(15, 16), rendVF24,
                "https://upload.wikimedia.org/wikipedia/commons/d/d4/Haas_F1_Team_Logo.svg"
        ));

        // AlphaTauri / RB - VCARB 01
        RendimientoVehiculo rendVCARB = new RendimientoVehiculo(
                new RendimientoConduccion(312, 2.05, 2.25, 2.55, 1.68, 0.98, 2.75),
                new RendimientoConduccion(332, 2.65, 2.85, 3.25, 2.38, 1.48, 3.95),
                new RendimientoConduccion(292, 1.75, 1.95, 2.25, 1.18, 0.68, 2.05)
        );
        store.getVehiculos().put("VCARB-01", new Vehiculo(
                "AlphaTauri", "VCARB-01", "Honda", 352, 2.65,
                Arrays.asList(17, 18), rendVCARB,
                "https://upload.wikimedia.org/wikipedia/commons/5/52/Scuderia_AlphaTauri_logo.svg"
        ));

        // Williams - FW46
        RendimientoVehiculo rendFW46 = new RendimientoVehiculo(
                new RendimientoConduccion(313, 2.05, 2.25, 2.55, 1.65, 0.95, 2.7),
                new RendimientoConduccion(334, 2.65, 2.85, 3.25, 2.35, 1.45, 3.9),
                new RendimientoConduccion(293, 1.75, 1.95, 2.25, 1.15, 0.65, 2.0)
        );
        store.getVehiculos().put("FW46", new Vehiculo(
                "Williams", "FW46", "Mercedes", 354, 2.65,
                Arrays.asList(19, 20), rendFW46,
                "https://upload.wikimedia.org/wikipedia/commons/0/07/Williams_Racing_2020_Logo.svg"
        ));
    }
}
