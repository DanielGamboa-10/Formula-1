package com.formula1.ui;

import com.formula1.model.ResultadoVuelta;
import com.formula1.model.SesionClasificacion;
import com.formula1.service.EstadisticaService;
import com.formula1.util.ConsoleUtils;

import java.util.List;

public class HistorialMenu {
    private final EstadisticaService estadisticaService;

    public HistorialMenu(EstadisticaService estadisticaService) {
        this.estadisticaService = estadisticaService;
    }

    public void mostrarMenu() {
        boolean salir = false;
        while (!salir) {
            ConsoleUtils.imprimirTitulo("Historial de Clasificaciones y Estadísticas");
            System.out.println("1. 📜 Ver historial de todas las sesiones de clasificación");
            System.out.println("2. 🔍 Ver detalle completo de una sesión pasada");
            System.out.println("3. 📈 Comparar tiempos de vuelta por circuito");
            System.out.println("0. ⬅️  Volver al menú principal");

            int opcion = ConsoleUtils.leerEntero("\n👉 Seleccione una opción: ", 0, 3);
            switch (opcion) {
                case 1:
                    listarSesiones();
                    break;
                case 2:
                    verDetalleSesion();
                    break;
                case 3:
                    compararTiemposPorCircuito();
                    break;
                case 0:
                    salir = true;
                    break;
            }
        }
    }

    private void listarSesiones() {
        ConsoleUtils.imprimirSubtitulo("Sesiones de Clasificación Registradas");
        List<SesionClasificacion> historial = estadisticaService.obtenerHistorialCompleto();
        if (historial.isEmpty()) {
            System.out.println(ConsoleUtils.YELLOW + "Aún no se han ejecutado sesiones de clasificación." + ConsoleUtils.RESET);
        } else {
            System.out.println(String.format("%-16s | %-19s | %-24s | %-10s | %-20s",
                    "ID Sesión", "Fecha / Hora", "Circuito", "Clima", "Pole Position"));
            System.out.println("--------------------------------------------------------------------------------------------------");
            for (SesionClasificacion s : historial) {
                String pole = (s.getPolePosition() != null) ? s.getPolePosition().getNombre() : "N/A";
                System.out.println(String.format("%-16s | %-19s | %-24s | %-10s | %-20s",
                        s.getId(), s.getFechaHoraFormateada(), s.getCircuito().getNombre(), s.getClima().getNombre(), pole));
            }
        }
        ConsoleUtils.pausar();
    }

    private void verDetalleSesion() {
        List<SesionClasificacion> historial = estadisticaService.obtenerHistorialCompleto();
        if (historial.isEmpty()) {
            System.out.println(ConsoleUtils.YELLOW + "No hay sesiones guardadas en el historial." + ConsoleUtils.RESET);
            ConsoleUtils.pausar();
            return;
        }

        System.out.println("Sesiones disponibles:");
        for (int i = 0; i < historial.size(); i++) {
            SesionClasificacion s = historial.get(i);
            System.out.println((i + 1) + ". [" + s.getFechaHoraFormateada() + "] " + s.getCircuito().getNombre() + " (" + s.getClima().getNombre() + ")");
        }

        int idx = ConsoleUtils.leerEntero("Seleccione sesión (1-" + historial.size() + "): ", 1, historial.size()) - 1;
        SesionClasificacion sesion = historial.get(idx);

        ConsoleUtils.imprimirTitulo("Detalle de Clasificación: " + sesion.getCircuito().getNombre());
        System.out.println("Fecha: " + sesion.getFechaHoraFormateada() + " | Clima: " + sesion.getClima().getNombre());
        System.out.println("Monoplaza Usuario: " + sesion.getVehiculoUsuario() + " | Piloto: " + sesion.getPilotoUsuario());
        if (sesion.getConfiguracionUsuario() != null) {
            System.out.println("Configuración Utilizada: " + sesion.getConfiguracionUsuario());
        }

        System.out.println("\nTop 5 Clasificación:");
        int top = Math.min(5, sesion.getResultados().size());
        for (int i = 0; i < top; i++) {
            ResultadoVuelta r = sesion.getResultados().get(i);
            System.out.println(String.format("P%d. %-20s (%-20s) - %s (Vel: %.1f km/h)",
                    r.getPosicion(), r.getPiloto().getNombre(), r.getPiloto().getEquipo(),
                    r.getTiempoFormateado(), r.getVelocidadMediaKmh()));
        }

        ResultadoVuelta user = sesion.getResultadoUsuario();
        if (user != null) {
            System.out.println(ConsoleUtils.GREEN + "\n🎯 Tu Resultado: Posición P" + user.getPosicion() + " con tiempo de " + user.getTiempoFormateado() + ConsoleUtils.RESET);
        }
        ConsoleUtils.pausar();
    }

    private void compararTiemposPorCircuito() {
        String circuito = ConsoleUtils.leerTexto("Ingrese nombre del circuito a comparar (ej. Silverstone, Mónaco, Spa): ");
        List<SesionClasificacion> sesiones = estadisticaService.filtrarPorCircuito(circuito);
        if (sesiones.isEmpty()) {
            System.out.println(ConsoleUtils.RED + "❌ No hay sesiones registradas para el circuito: " + circuito + ConsoleUtils.RESET);
        } else {
            ConsoleUtils.imprimirSubtitulo("Historial Comparativo en " + circuito);
            System.out.println(String.format("%-19s | %-10s | %-20s | %-10s | %-16s",
                    "Fecha", "Clima", "Poleman", "Tiempo Pole", "Tu Tiempo"));
            System.out.println("----------------------------------------------------------------------------------");
            for (SesionClasificacion s : sesiones) {
                String poleman = (s.getPolePosition() != null) ? s.getPolePosition().getNombre() : "N/A";
                String tiempoPole = (!s.getResultados().isEmpty()) ? s.getResultados().get(0).getTiempoFormateado() : "N/A";
                ResultadoVuelta user = s.getResultadoUsuario();
                String tiempoUser = (user != null) ? user.getTiempoFormateado() + " (P" + user.getPosicion() + ")" : "N/A";

                System.out.println(String.format("%-19s | %-10s | %-20s | %-10s | %-16s",
                        s.getFechaHoraFormateada(), s.getClima().getNombre(), poleman, tiempoPole, tiempoUser));
            }
        }
        ConsoleUtils.pausar();
    }
}
