package com.formula1.ui;

import com.formula1.model.Piloto;
import com.formula1.service.PilotoService;
import com.formula1.util.ConsoleUtils;

import java.util.List;
import java.util.Optional;

public class PilotoMenu {
    private final PilotoService pilotoService;

    public PilotoMenu(PilotoService pilotoService) {
        this.pilotoService = pilotoService;
    }

    public void mostrarMenu() {
        boolean salir = false;
        while (!salir) {
            ConsoleUtils.imprimirTitulo("Gestión de Pilotos");
            System.out.println("1. 📋 Listar todos los pilotos");
            System.out.println("2. 🔍 Buscar piloto por ID");
            System.out.println("3. 🔎 Buscar pilotos por nombre");
            System.out.println("4. 🏎️  Filtrar pilotos por equipo");
            System.out.println("5. ➕ Registrar nuevo piloto");
            System.out.println("6. ✏️  Modificar piloto existente");
            System.out.println("7. 🗑️  Eliminar piloto");
            System.out.println("0. ⬅️  Volver al menú principal");

            int opcion = ConsoleUtils.leerEntero("\n👉 Seleccione una opción: ", 0, 7);
            switch (opcion) {
                case 1:
                    listarPilotos();
                    break;
                case 2:
                    buscarPorId();
                    break;
                case 3:
                    buscarPorNombre();
                    break;
                case 4:
                    filtrarPorEquipo();
                    break;
                case 5:
                    registrarPiloto();
                    break;
                case 6:
                    modificarPiloto();
                    break;
                case 7:
                    eliminarPiloto();
                    break;
                case 0:
                    salir = true;
                    break;
            }
        }
    }

    private void listarPilotos() {
        ConsoleUtils.imprimirSubtitulo("Lista Completa de Pilotos");
        List<Piloto> pilotos = pilotoService.listarTodos();
        if (pilotos.isEmpty()) {
            System.out.println("No hay pilotos registrados.");
        } else {
            System.out.println(String.format("%-5s | %-20s | %-22s | %-10s | %-8s | %-8s", "ID", "Nombre", "Equipo", "Rol", "Habilidad", "Experiencia"));
            System.out.println("-----------------------------------------------------------------------------------------");
            for (Piloto p : pilotos) {
                System.out.println(String.format("#%-4d | %-20s | %-22s | %-10s | %-8d%% | %-8d%%",
                        p.getId(), p.getNombre(), p.getEquipo(), p.getRol(), p.getHabilidad(), p.getExperiencia()));
            }
        }
        ConsoleUtils.pausar();
    }

    private void buscarPorId() {
        int id = ConsoleUtils.leerEntero("Ingrese ID del piloto: ", 1, 9999);
        Optional<Piloto> op = pilotoService.buscarPorId(id);
        if (op.isPresent()) {
            Piloto p = op.get();
            System.out.println(ConsoleUtils.GREEN + "\n✅ Piloto Encontrado:" + ConsoleUtils.RESET);
            System.out.println("ID: " + p.getId());
            System.out.println("Nombre: " + p.getNombre());
            System.out.println("Equipo: " + p.getEquipo());
            System.out.println("Rol: " + p.getRol());
            System.out.println("Habilidad: " + p.getHabilidad() + "%");
            System.out.println("Experiencia: " + p.getExperiencia() + "%");
        } else {
            System.out.println(ConsoleUtils.RED + "❌ No se encontró piloto con ID " + id + ConsoleUtils.RESET);
        }
        ConsoleUtils.pausar();
    }

    private void buscarPorNombre() {
        String termino = ConsoleUtils.leerTexto("Ingrese nombre o parte del nombre a buscar: ");
        List<Piloto> resultados = pilotoService.buscarPorNombre(termino);
        if (resultados.isEmpty()) {
            System.out.println(ConsoleUtils.RED + "No se encontraron pilotos con el término: " + termino + ConsoleUtils.RESET);
        } else {
            System.out.println(ConsoleUtils.GREEN + "\nResultados encontrados (" + resultados.size() + "):" + ConsoleUtils.RESET);
            resultados.forEach(System.out::println);
        }
        ConsoleUtils.pausar();
    }

    private void filtrarPorEquipo() {
        String equipo = ConsoleUtils.leerTexto("Ingrese el nombre del equipo: ");
        List<Piloto> resultados = pilotoService.listarPorEquipo(equipo);
        if (resultados.isEmpty()) {
            System.out.println(ConsoleUtils.RED + "No se encontraron pilotos para el equipo: " + equipo + ConsoleUtils.RESET);
        } else {
            System.out.println(ConsoleUtils.GREEN + "\nPilotos de " + equipo + ":" + ConsoleUtils.RESET);
            resultados.forEach(System.out::println);
        }
        ConsoleUtils.pausar();
    }

    private void registrarPiloto() {
        ConsoleUtils.imprimirSubtitulo("Registro de Nuevo Piloto");
        String nombre = ConsoleUtils.leerTexto("Nombre del piloto: ");
        String equipo = ConsoleUtils.leerTexto("Equipo: ");
        int rolOpt = ConsoleUtils.leerEntero("Rol (1: Líder, 2: Escudero): ", 1, 2);
        String rol = (rolOpt == 1) ? "Líder" : "Escudero";
        int experiencia = ConsoleUtils.leerEntero("Nivel de Experiencia (50-100): ", 50, 100);
        int habilidad = ConsoleUtils.leerEntero("Nivel de Habilidad (50-100): ", 50, 100);

        Piloto nuevo = pilotoService.agregarPiloto(nombre, equipo, rol, experiencia, habilidad);
        System.out.println(ConsoleUtils.GREEN + "✅ Piloto registrado exitosamente con ID #" + nuevo.getId() + ConsoleUtils.RESET);
        ConsoleUtils.pausar();
    }

    private void modificarPiloto() {
        int id = ConsoleUtils.leerEntero("Ingrese ID del piloto a modificar: ", 1, 9999);
        Optional<Piloto> op = pilotoService.buscarPorId(id);
        if (op.isEmpty()) {
            System.out.println(ConsoleUtils.RED + "❌ Piloto no encontrado." + ConsoleUtils.RESET);
            ConsoleUtils.pausar();
            return;
        }

        Piloto p = op.get();
        System.out.println("Modificando a: " + p.getNombre());
        String nuevoNombre = ConsoleUtils.leerTextoOpcional("Nuevo nombre", p.getNombre());
        String nuevoEquipo = ConsoleUtils.leerTextoOpcional("Nuevo equipo", p.getEquipo());
        String nuevoRol = ConsoleUtils.leerTextoOpcional("Nuevo rol (Líder / Escudero)", p.getRol());
        int nuevaExp = ConsoleUtils.leerEntero("Nueva experiencia (50-100) [" + p.getExperiencia() + "]: ", 50, 100);
        int nuevaHab = ConsoleUtils.leerEntero("Nueva habilidad (50-100) [" + p.getHabilidad() + "]: ", 50, 100);

        pilotoService.actualizarPiloto(id, nuevoNombre, nuevoEquipo, nuevoRol, nuevaExp, nuevaHab);
        System.out.println(ConsoleUtils.GREEN + "✅ Piloto actualizado correctamente." + ConsoleUtils.RESET);
        ConsoleUtils.pausar();
    }

    private void eliminarPiloto() {
        int id = ConsoleUtils.leerEntero("Ingrese ID del piloto a eliminar: ", 1, 9999);
        if (pilotoService.eliminarPiloto(id)) {
            System.out.println(ConsoleUtils.GREEN + "✅ Piloto eliminado exitosamente." + ConsoleUtils.RESET);
        } else {
            System.out.println(ConsoleUtils.RED + "❌ No se encontró piloto con ID " + id + ConsoleUtils.RESET);
        }
        ConsoleUtils.pausar();
    }
}
