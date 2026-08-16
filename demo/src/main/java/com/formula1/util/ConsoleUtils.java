package com.formula1.util;

import java.util.Scanner;

public class ConsoleUtils {
    // Colores ANSI
    public static final String RESET = "\u001B[0m";
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";
    public static final String BOLD = "\u001B[1m";

    private static final Scanner scanner = new Scanner(System.in);

    public static void limpiarPantalla() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void imprimirTitulo(String titulo) {
        System.out.println(RED + BOLD + "==========================================================================================" + RESET);
        System.out.println(WHITE + BOLD + "   🏁  " + titulo.toUpperCase() + "  🏁" + RESET);
        System.out.println(RED + BOLD + "==========================================================================================" + RESET);
    }

    public static void imprimirSubtitulo(String subtitulo) {
        System.out.println(CYAN + BOLD + "\n--- " + subtitulo + " ---" + RESET);
    }

    public static void pausar() {
        System.out.print(YELLOW + "\nPresione ENTER para continuar..." + RESET);
        scanner.nextLine();
    }

    public static int leerEntero(String mensaje, int min, int max) {
        while (true) {
            System.out.print(mensaje);
            String input = scanner.nextLine().trim();
            try {
                int valor = Integer.parseInt(input);
                if (valor >= min && valor <= max) {
                    return valor;
                }
                System.out.println(RED + "⚠️ Por favor ingrese un número entre " + min + " y " + max + "." + RESET);
            } catch (NumberFormatException e) {
                System.out.println(RED + "⚠️ Entrada inválida. Ingrese un número entero." + RESET);
            }
        }
    }

    public static double leerDouble(String mensaje, double min, double max) {
        while (true) {
            System.out.print(mensaje);
            String input = scanner.nextLine().trim().replace(",", ".");
            try {
                double valor = Double.parseDouble(input);
                if (valor >= min && valor <= max) {
                    return valor;
                }
                System.out.println(RED + "⚠️ Por favor ingrese un valor entre " + min + " y " + max + "." + RESET);
            } catch (NumberFormatException e) {
                System.out.println(RED + "⚠️ Entrada inválida. Ingrese un número decimal válido." + RESET);
            }
        }
    }

    public static String leerTexto(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println(RED + "⚠️ El texto no puede estar vacío." + RESET);
        }
    }

    public static String leerTextoOpcional(String mensaje, String valorPorDefecto) {
        System.out.print(mensaje + " [" + valorPorDefecto + "]: ");
        String input = scanner.nextLine().trim();
        return input.isEmpty() ? valorPorDefecto : input;
    }
}
