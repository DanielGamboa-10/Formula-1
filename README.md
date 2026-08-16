# 🏎️ Simulación y Gestión de Fórmula 1 (Java SE Puro)

Proyecto interactivo para la administración de escuderías, pilotos, monoplazas, circuitos y simulación de sesiones de clasificación de Fórmula 1 en **Java Puro (sin frameworks ni Spring Boot)**, utilizando **Map / HashMap** para la persistencia temporal en memoria.

---

## 👥 Trabajo en Equipo y Ramas Git

* **Rama Principal:** `main` (Estructura base del proyecto)
* **Rama de Desarrollo (Daniel Gamboa):** `DevDanielGamboa` (Módulos de Monoplazas, Reglajes/Configuraciones, Motor de Simulación de Clasificación, Estadísticas e Historiales, e Interfaz de Consola).

---

## 🧱 Arquitectura y Estructura del Código

```text
src/main/java/com/formula1/
├── Main.java                          # Punto de entrada principal con gestión de arranque y ShutdownHook
├── data/
│   ├── DataLoader.java                # Carga inicial de datos (20 pilotos, equipos, 7 circuitos, monoplazas)
│   ├── DataPersistenceManager.java    # Gestor atómico de serialización, guardado y carga en disco
│   ├── DataSnapshot.java              # Estructura de respaldo serializable con metadatos y versionado
│   └── DataStore.java                 # Persistencia centralizada con auto-guardado en disco
├── gui/
│   ├── MainFrame.java                 # Ventana principal y barra de navegación interactiva
│   ├── PersistenciaPanel.java         # Panel de control de persistencia, sincronización y copias de seguridad
│   └── ...                            # Paneles temáticos de F1
├── model/
│   ├── Piloto.java                    # POJO Piloto (ID, nombre, equipo, rol, experiencia, habilidad)
│   ├── Equipo.java                    # POJO Escudería (nombre, país, motor, pilotos asignados)
│   ├── Circuito.java                  # POJO Circuito (longitud, vueltas, récords, clima, desgaste/consumo)
│   ├── Vehiculo.java                  # POJO Monoplaza (modelo, motor, V.Max, 0-100, rendimientos)
│   ├── RendimientoVehiculo.java       # Rendimiento según modo normal, agresivo y ahorro
│   ├── RendimientoConduccion.java     # Velocidades y matrices de consumo/desgaste por clima
│   ├── ConfiguracionVehiculo.java     # Reglajes del monoplaza (Modo conducción, aero, neumáticos, combustible)
│   ├── Clima.java                     # Enum (Seco, Lluvioso, Extremo)
│   ├── ModoConduccion.java            # Enum (Normal, Agresivo, Ahorro)
│   ├── CargaAerodinamica.java         # Enum (Baja, Media, Alta)
│   ├── PresionNeumaticos.java         # Enum (Baja, Estándar, Alta)
│   ├── EstrategiaCombustible.java     # Enum (Agresiva, Balanceada, Ahorro)
│   ├── ResultadoVuelta.java           # Tiempos, telemetría y posiciones
│   ├── SesionClasificacion.java       # Registro histórico de sesión
│   ├── RecordVuelta.java              # Récords oficiales
│   └── GanadorHistorico.java          # Historial de victorias
├── service/
│   ├── PilotoService.java             # CRUD y búsquedas de pilotos con auto-guardado
│   ├── EquipoService.java             # CRUD y asignación de escuderías con auto-guardado
│   ├── CircuitoService.java           # CRUD y análisis de pistas con auto-guardado
│   ├── VehiculoService.java           # CRUD y comparador de rendimiento con auto-guardado
│   ├── ConfiguracionService.java      # Gestión y guardado de reglajes con auto-guardado
│   ├── SimulacionService.java         # Motor de clasificación, física y registro histórico persistente
│   └── EstadisticaService.java        # Historiales y comparativas por circuito
└── ui/
    ├── ConsoleUtils.java              # Utilidades de consola y formato ANSI
    ├── MenuPrincipal.java             # Menú raíz interactivo
    ├── PersistenciaMenu.java          # Menú de gestión de almacenamiento y copias de seguridad
    └── ...                            # Menús de consola interactivos
```

---

## 🚀 Compilación y Ejecución (Java Puro)

### 1. Compilar todas las clases
```bash
javac -d demo/bin $(Get-ChildItem -Path demo/src/main/java -Recurse -Filter "*.java" | Select-Object -ExpandProperty FullName)
```

### 2. Ejecutar la Aplicación
```bash
java -cp demo/bin com.formula1.Main
```

### 3. Ejecutar las Pruebas Unitarias y de Persistencia
```bash
javac -d demo/bin $(Get-ChildItem -Path demo/src -Recurse -Filter "*.java" | Select-Object -ExpandProperty FullName)
java -cp demo/bin com.formula1.SimulacionTest
java -cp demo/bin com.formula1.PersistenciaTest
```
