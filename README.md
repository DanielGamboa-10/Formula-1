# 🏎️ Simulación y Gestión de Fórmula 1 (Java SE Puro)

Proyecto interactivo para la administración de escuderías, pilotos, monoplazas, circuitos y simulación de sesiones de clasificación de Fórmula 1 en **Java Puro (sin frameworks ni Spring Boot)**, utilizando **Map / HashMap** para la persistencia temporal en memoria.

---

## 👥 División de Trabajo y Ramas Git

El proyecto se encuentra modularizado y distribuido equitativamente entre los dos integrantes del equipo:

### 🔴 Integrante 1 (Rama: `DevDanielGamboa`):
* **Módulo de Monoplazas:** CRUD de vehículos, asignación de pilotos y comparador visual de rendimiento (velocidad punta, aceleración 0-100, consumo y desgaste en pista seca/mojada).
* **Módulo de Configuración / Reglajes:** Modos de conducción (*Normal, Agresivo, Ahorro*), carga aerodinámica (*Baja, Media, Alta*), presión de neumáticos (*Baja, Estándar, Alta*), estrategia de combustible y guardado de presets.
* **Motor de Simulación de Clasificación:** Algoritmo estocástico y físico de vuelta rápida, generador de clima aleatorio (*Seco, Lluvioso, Extremo*), ordenamiento de parrilla de 20 pilotos y determinación de la **Pole Position**.
* **Módulo de Historial y Estadísticas:** Persistencia de sesiones previas y comparador de tiempos por circuito.
* **Batería de Pruebas Unitarias:** `SimulacionTest.java`.

### 🔵 Integrante 2:
* **Módulo de Pilotos:** CRUD de pilotos, roles (*Líder/Escudero*), destreza, experiencia y búsquedas avanzadas.
* **Módulo de Escuderías:** CRUD de equipos, gestión de motores y asignación de competidores.
* **Módulo de Circuitos:** CRUD de circuitos, longitud, vueltas, récords históricos, historial de ganadores e impacto de abrasión/consumo.

---

## 🧱 Arquitectura del Código

```text
src/main/java/com/formula1/
├── Main.java                          # Punto de entrada de la aplicación
├── data/
│   ├── DataLoader.java                # Carga inicial de datos del enunciado
│   └── DataStore.java                 # Persistencia temporal en memoria con Map y HashMap
├── model/
│   ├── Piloto.java, Equipo.java, Circuito.java, Vehiculo.java
│   ├── RendimientoVehiculo.java, RendimientoConduccion.java
│   ├── ConfiguracionVehiculo.java, ResultadoVuelta.java, SesionClasificacion.java
│   └── Enums: Clima, ModoConduccion, CargaAerodinamica, PresionNeumaticos, EstrategiaCombustible
├── service/
│   ├── VehiculoService.java, ConfiguracionService.java, SimulacionService.java, EstadisticaService.java
│   └── PilotoService.java, EquipoService.java, CircuitoService.java
└── ui/
    ├── ConsoleUtils.java, MenuPrincipal.java
    ├── VehiculoMenu.java, ConfiguracionMenu.java, SimulacionMenu.java, HistorialMenu.java
    └── PilotoMenu.java, EquipoMenu.java, CircuitoMenu.java
```

---

## 🚀 Compilación y Ejecución

### Compilar clases:
```powershell
javac -d demo/bin (Get-ChildItem -Path demo/src/main/java -Recurse -Filter "*.java" | Select-Object -ExpandProperty FullName)
```

### Ejecutar simulador:
```powershell
java -cp demo/bin com.formula1.Main
```

### Ejecutar pruebas:
```powershell
javac -d demo/bin (Get-ChildItem -Path demo/src -Recurse -Filter "*.java" | Select-Object -ExpandProperty FullName)
java -cp demo/bin com.formula1.SimulacionTest
```
