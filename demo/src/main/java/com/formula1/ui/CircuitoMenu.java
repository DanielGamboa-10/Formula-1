package com.formula1.ui;

import com.formula1.data.DataStore;
import com.formula1.model.Circuito;
import com.formula1.model.GanadorHistorico;
import com.formula1.model.Piloto;
import com.formula1.service.CircuitoService;
import com.formula1.util.ConsoleUtils;

import java.util.List;
import java.util.Optional;

public class CircuitoMenu {
    private final CircuitoService circuitoService;

    public CircuitoMenu(CircuitoService circuitoService) {
        this.circuitoService = circuitoService;
    }

    public void mostrarMenu() {
        boolean salir = false;
        while (!salir) {
            ConsoleUtils.imprimirTitulo("Gestión de Circuitos");
            System.out.println("1. 📋 Listar todos los circuitos");
            System.out.println("2. 🔍 Ver detalle, estadísticas y récords de un circuito");
            System.out.println("3. 🏆 Ver historial de ganadores por circuito");
            System.out.println("4. 📊 Analizar impacto del circuito en neumáticos y combustible");
            System.out.println("5. 🔎 Buscar circuito por nombre o país");
            System.out.println("6. ➕ Agregar nuevo circuito");
            System.out.println("7. ✏️  Editar circuito");
            System.out.println("8. 🗑️  Eliminar circuito");
            System.out.println("0. ⬅️  Volver al menú principal");

            int opcion = ConsoleUtils.leerEntero("\n👉 Seleccione una opción: ", 0, 8);
            switch (opcion) {
                case 1:
                    listarCircuitos();
                    break;
                case 2:
                    verDetalleCircuito();
                    break;
                case 3:
                    verHistorialGanadores();
                    break;
                case 4:
                    analizarImpacto();
                    break;
                case 5:
                    buscarCircuito();
                    break;
                case 6:
                    agregarCircuito();
                    break;
                case 7:
                    editarCircuito();
                    break;
                case 8:
                    eliminarCircuito();
                    break;
                case 0:
                    salir = true;
                    break;
            }
        }
    }

    private void listarCircuitos() {
        ConsoleUtils.imprimirSubtitulo("Lista de Circuitos del Calendario");
        List<Circuito> circuitos = circuitoService.listarTodos();
        if (circuitos.isEmpty()) {
            System.out.println("No hay circuitos registrados.");
        } else {
            System.out.println(String.format("%-30s | %-16s | %-12s | %-8s | %-18s", "Nombre", "País", "Longitud", "Vueltas", "Clima Habitual"));
            System.out.println("------------------------------------------------------------------------------------------------");
            for (Circuito c : circuitos) {
                System.out.println(String.format("%-30s | %-16s | %5.2f km    | %-8d | %-18s",
                        c.getNombre(), c.getPais(), c.getLongitudKm(), c.getVueltas(), c.getClimaHabitual()));
            }
        }
        ConsoleUtils.pausar();
    }

    private void verDetalleCircuito() {
        String nombre = ConsoleUtils.leerTexto("Nombre del circuito: ");
        Optional<Circuito> op = circuitoService.buscarPorNombre(nombre);
        if (op.isEmpty()) {
            System.out.println(ConsoleUtils.RED + "❌ Circuito no encontrado." + ConsoleUtils.RESET);
        } else {
            Circuito c = op.get();
            System.out.println(ConsoleUtils.GREEN + "\n🏁 Información Detallada del Circuito:" + ConsoleUtils.RESET);
            System.out.println("Nombre: " + c.getNombre());
            System.out.println("País: " + c.getPais());
            System.out.println("Longitud de Pista: " + c.getLongitudKm() + " km (" + c.getVueltas() + " vueltas de carrera)");
            System.out.println("Clima Típico: " + c.getClimaHabitual());
            System.out.println("Descripción: " + c.getDescripcion());
            if (c.getRecordVuelta() != null) {
                System.out.println(ConsoleUtils.YELLOW + "Récord de Vuelta: " + c.getRecordVuelta().getTiempo() +
                        " por " + c.getRecordVuelta().getPiloto() + " (" + c.getRecordVuelta().getAnio() + ")" + ConsoleUtils.RESET);
            }
        }
        ConsoleUtils.pausar();
    }

    private void verHistorialGanadores() {
        String nombre = ConsoleUtils.leerTexto("Nombre del circuito: ");
        Optional<Circuito> op = circuitoService.buscarPorNombre(nombre);
        if (op.isEmpty()) {
            System.out.println(ConsoleUtils.RED + "❌ Circuito no encontrado." + ConsoleUtils.RESET);
        } else {
            Circuito c = op.get();
            ConsoleUtils.imprimirSubtitulo("Historial de Ganadores: " + c.getNombre());
            if (c.getGanadores().isEmpty()) {
                System.out.println("No hay registro histórico de ganadores.");
            } else {
                for (GanadorHistorico g : c.getGanadores()) {
                    Piloto p = DataStore.getInstance().getPilotos().get(g.getPilotoId());
                    String pilotoNombre = (p != null) ? p.getNombre() + " (" + p.getEquipo() + ")" : "Piloto #" + g.getPilotoId();
                    System.out.println("🏆 Temporada " + g.getTemporada() + " -> " + pilotoNombre);
                }
            }
        }
        ConsoleUtils.pausar();
    }

    private void analizarImpacto() {
        ConsoleUtils.imprimirSubtitulo("Impacto del Trazado en Neumáticos y Combustible");
        List<Circuito> circuitos = circuitoService.listarTodos();
        System.out.println(String.format("%-30s | %-18s | %-18s", "Circuito", "Factor Desgaste", "Factor Consumo"));
        System.out.println("------------------------------------------------------------------------");
        for (Circuito c : circuitos) {
            String nivelDesgaste = (c.getFactorDesgasteNeumaticos() > 1.1) ? "Alto (Abrasivo)" : (c.getFactorDesgasteNeumaticos() < 0.9 ? "Bajo" : "Medio");
            String nivelConsumo = (c.getFactorConsumoCombustible() > 1.1) ? "Alto (Rectas)" : (c.getFactorConsumoCombustible() < 0.9 ? "Bajo" : "Medio");
            System.out.println(String.format("%-30s | x%.2f (%-11s) | x%.2f (%-11s)",
                    c.getNombre(), c.getFactorDesgasteNeumaticos(), nivelDesgaste,
                    c.getFactorConsumoCombustible(), nivelConsumo));
        }
        ConsoleUtils.pausar();
    }

    private void buscarCircuito() {
        String termino = ConsoleUtils.leerTexto("Término de búsqueda (Nombre o País): ");
        List<Circuito> encontrados = circuitoService.buscarPorTermino(termino);
        if (encontrados.isEmpty()) {
            System.out.println(ConsoleUtils.RED + "❌ No se encontraron circuitos." + ConsoleUtils.RESET);
        } else {
            System.out.println(ConsoleUtils.GREEN + "\nCircuitos encontrados:" + ConsoleUtils.RESET);
            encontrados.forEach(System.out::println);
        }
        ConsoleUtils.pausar();
    }

    private void agregarCircuito() {
        ConsoleUtils.imprimirSubtitulo("Registrar Nuevo Circuito");
        String nombre = ConsoleUtils.leerTexto("Nombre del circuito: ");
        String pais = ConsoleUtils.leerTexto("País: ");
        double longitud = ConsoleUtils.leerDouble("Longitud en km (ej. 5.4): ", 1.0, 30.0);
        int vueltas = ConsoleUtils.leerEntero("Cantidad de vueltas: ", 10, 100);
        String desc = ConsoleUtils.leerTexto("Descripción: ");
        String recTiempo = ConsoleUtils.leerTextoOpcional("Tiempo récord (ej. 1:20.500)", "1:25.000");
        String recPiloto = ConsoleUtils.leerTextoOpcional("Piloto del récord", "Piloto Histórico");
        int recAnio = ConsoleUtils.leerEntero("Año del récord: ", 1950, 2026);
        String img = ConsoleUtils.leerTextoOpcional("URL Imagen del trazado", "https://formula1.com/track.svg");

        circuitoService.agregarCircuito(nombre, pais, longitud, vueltas, desc, recTiempo, recPiloto, recAnio, img);
        System.out.println(ConsoleUtils.GREEN + "✅ Circuito registrado exitosamente." + ConsoleUtils.RESET);
        ConsoleUtils.pausar();
    }

    private void editarCircuito() {
        String nombre = ConsoleUtils.leerTexto("Nombre del circuito a editar: ");
        Optional<Circuito> op = circuitoService.buscarPorNombre(nombre);
        if (op.isEmpty()) {
            System.out.println(ConsoleUtils.RED + "❌ Circuito no encontrado." + ConsoleUtils.RESET);
            ConsoleUtils.pausar();
            return;
        }

        Circuito c = op.get();
        String nuevoNombre = ConsoleUtils.leerTextoOpcional("Nuevo nombre", c.getNombre());
        String nuevoPais = ConsoleUtils.leerTextoOpcional("Nuevo país", c.getPais());
        double nuevaLongitud = ConsoleUtils.leerDouble("Nueva longitud km [" + c.getLongitudKm() + "]: ", 1.0, 30.0);
        int nuevasVueltas = ConsoleUtils.leerEntero("Nuevas vueltas [" + c.getVueltas() + "]: ", 10, 100);
        String nuevaDesc = ConsoleUtils.leerTextoOpcional("Nueva descripción", c.getDescripcion());

        circuitoService.actualizarCircuito(nombre, nuevoNombre, nuevoPais, nuevaLongitud, nuevasVueltas, nuevaDesc);
        System.out.println(ConsoleUtils.GREEN + "✅ Circuito actualizado correctamente." + ConsoleUtils.RESET);
        ConsoleUtils.pausar();
    }

    private void eliminarCircuito() {
        String nombre = ConsoleUtils.leerTexto("Nombre del circuito a eliminar: ");
        if (circuitoService.eliminarCircuito(nombre)) {
            System.out.println(ConsoleUtils.GREEN + "✅ Circuito eliminado exitosamente." + ConsoleUtils.RESET);
        } else {
            System.out.println(ConsoleUtils.RED + "❌ Circuito no encontrado." + ConsoleUtils.RESET);
        }
        ConsoleUtils.pausar();
    }
}
