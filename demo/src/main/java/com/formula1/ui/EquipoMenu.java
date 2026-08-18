package com.formula1.ui;

import com.formula1.model.Equipo;
import com.formula1.service.EquipoService;
import com.formula1.util.ConsoleUtils;

import java.util.List;
import java.util.Optional;

public class EquipoMenu {
    private final EquipoService equipoService;

    public EquipoMenu(EquipoService equipoService) {
        this.equipoService = equipoService;
    }

    public void mostrarMenu() {
        boolean salir = false;
        while (!salir) {
            ConsoleUtils.imprimirTitulo("Gestión de Equipos");
            System.out.println("1. 📋 Listar todos los equipos");
            System.out.println("2. 🔍 Buscar equipo por nombre");
            System.out.println("3. ➕ Registrar nuevo equipo");
            System.out.println("4. ✏️  Modificar equipo");
            System.out.println("5. 🗑️  Eliminar equipo");
            System.out.println("6. 👤 Asignar piloto a equipo");
            System.out.println("0. ⬅️  Volver al menú principal");

            int opcion = ConsoleUtils.leerEntero("\n👉 Seleccione una opción: ", 0, 6);
            switch (opcion) {
                case 1:
                    listarEquipos();
                    break;
                case 2:
                    buscarEquipo();
                    break;
                case 3:
                    registrarEquipo();
                    break;
                case 4:
                    modificarEquipo();
                    break;
                case 5:
                    eliminarEquipo();
                    break;
                case 6:
                    asignarPiloto();
                    break;
                case 0:
                    salir = true;
                    break;
            }
        }
    }

    private void listarEquipos() {
        ConsoleUtils.imprimirSubtitulo("Lista de Equipos y Escuderías");
        List<Equipo> equipos = equipoService.listarTodos();
        if (equipos.isEmpty()) {
            System.out.println("No hay equipos registrados.");
        } else {
            System.out.println(String.format("%-24s | %-14s | %-12s | %-16s", "Nombre", "País", "Motor", "Pilotos (IDs)"));
            System.out.println("-------------------------------------------------------------------------------");
            for (Equipo e : equipos) {
                System.out.println(String.format("%-24s | %-14s | %-12s | %-16s",
                        e.getNombre(), e.getPais(), e.getMotor(), e.getPilotosIds().toString()));
            }
        }
        ConsoleUtils.pausar();
    }

    private void buscarEquipo() {
        String termino = ConsoleUtils.leerTexto("Ingrese nombre o motor del equipo: ");
        List<Equipo> resultados = equipoService.buscarPorTermino(termino);
        if (resultados.isEmpty()) {
            System.out.println(ConsoleUtils.RED + "❌ No se encontraron equipos." + ConsoleUtils.RESET);
        } else {
            System.out.println(ConsoleUtils.GREEN + "\nEquipos encontrados:" + ConsoleUtils.RESET);
            resultados.forEach(System.out::println);
        }
        ConsoleUtils.pausar();
    }

    private void registrarEquipo() {
        ConsoleUtils.imprimirSubtitulo("Registrar Nuevo Equipo");
        String nombre = ConsoleUtils.leerTexto("Nombre de la escudería: ");
        String pais = ConsoleUtils.leerTexto("País de origen: ");
        String motor = ConsoleUtils.leerTexto("Proveedor de motor: ");
        String imagen = ConsoleUtils.leerTextoOpcional("URL de imagen / logo", "https://formula1.com/team.svg");

        equipoService.agregarEquipo(nombre, pais, motor, imagen);
        System.out.println(ConsoleUtils.GREEN + "✅ Equipo registrado exitosamente." + ConsoleUtils.RESET);
        ConsoleUtils.pausar();
    }

    private void modificarEquipo() {
        String nombre = ConsoleUtils.leerTexto("Nombre del equipo a modificar: ");
        Optional<Equipo> op = equipoService.buscarPorNombre(nombre);
        if (op.isEmpty()) {
            System.out.println(ConsoleUtils.RED + "❌ Equipo no encontrado." + ConsoleUtils.RESET);
            ConsoleUtils.pausar();
            return;
        }

        Equipo eq = op.get();
        String nuevoNombre = ConsoleUtils.leerTextoOpcional("Nuevo nombre", eq.getNombre());
        String nuevoPais = ConsoleUtils.leerTextoOpcional("Nuevo país", eq.getPais());
        String nuevoMotor = ConsoleUtils.leerTextoOpcional("Nuevo motor", eq.getMotor());
        String nuevaImg = ConsoleUtils.leerTextoOpcional("Nueva imagen URL", eq.getImagenUrl());

        equipoService.actualizarEquipo(nombre, nuevoNombre, nuevoPais, nuevoMotor, nuevaImg);
        System.out.println(ConsoleUtils.GREEN + "✅ Equipo actualizado correctamente." + ConsoleUtils.RESET);
        ConsoleUtils.pausar();
    }

    private void eliminarEquipo() {
        String nombre = ConsoleUtils.leerTexto("Nombre del equipo a eliminar: ");
        if (equipoService.eliminarEquipo(nombre)) {
            System.out.println(ConsoleUtils.GREEN + "✅ Equipo eliminado correctamente." + ConsoleUtils.RESET);
        } else {
            System.out.println(ConsoleUtils.RED + "❌ Equipo no encontrado." + ConsoleUtils.RESET);
        }
        ConsoleUtils.pausar();
    }

    private void asignarPiloto() {
        int pilotoId = ConsoleUtils.leerEntero("ID del piloto: ", 1, 9999);
        String equipo = ConsoleUtils.leerTexto("Nombre del equipo destino: ");
        if (equipoService.asignarPilotoAEquipo(pilotoId, equipo)) {
            System.out.println(ConsoleUtils.GREEN + "✅ Piloto asignado exitosamente al equipo " + equipo + ConsoleUtils.RESET);
        } else {
            System.out.println(ConsoleUtils.RED + "❌ Error al asignar: verifique que el piloto y equipo existan." + ConsoleUtils.RESET);
        }
        ConsoleUtils.pausar();
    }
}
