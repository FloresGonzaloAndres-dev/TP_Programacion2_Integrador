/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

/**
 *
 * @author Wilde
 */
import java.util.Scanner;

/**
 * Utilidades para leer datos del usuario por consola con validaciones basicas.
 */
public class InputHelper {

    private static final Scanner sc = new Scanner(System.in);

    public static String readLine(String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim();
    }

    public static String readNonEmpty(String prompt) {
        String val;
        do {
            val = readLine(prompt);
            if (val.isEmpty()) System.out.println("  El campo no puede estar vacio.");
        } while (val.isEmpty());
        return val;
    }

    public static long readLong(String prompt) {
        while (true) {
            String raw = readLine(prompt);
            try {
                return Long.parseLong(raw);
            } catch (NumberFormatException e) {
                System.out.println("  Ingrese un numero entero valido.");
            }
        }
    }

    public static double readDouble(String prompt) {
        while (true) {
            String raw = readLine(prompt);
            try {
                return Double.parseDouble(raw);
            } catch (NumberFormatException e) {
                System.out.println("  Ingrese un numero decimal valido (use punto como separador).");
            }
        }
    }

    public static double readNonNegativeDouble(String prompt) {
        double val;
        do {
            val = readDouble(prompt);
            if (val < 0) System.out.println("  El valor no puede ser negativo.");
        } while (val < 0);
        return val;
    }

    public static int readNonNegativeInt(String prompt) {
        while (true) {
            String raw = readLine(prompt);
            try {
                int val = Integer.parseInt(raw);
                if (val < 0) {
                    System.out.println("  El valor no puede ser negativo.");
                } else {
                    return val;
                }
            } catch (NumberFormatException e) {
                System.out.println("  Ingrese un numero entero valido.");
            }
        }
    }

    public static boolean confirm(String prompt) {
        while (true) {
            String raw = readLine(prompt + " (S/N): ").toUpperCase();
            if (raw.equals("S")) return true;
            if (raw.equals("N")) return false;
            System.out.println("  Responda S o N.");
        }
    }

    public static int readOption(String prompt, int min, int max) {
        while (true) {
            String raw = readLine(prompt);
            try {
                int opt = Integer.parseInt(raw);
                if (opt >= min && opt <= max) return opt;
                System.out.printf("  Opcion invalida. Ingrese un numero entre %d y %d.%n", min, max);
            } catch (NumberFormatException e) {
                System.out.println("  Ingrese un numero valido.");
            }
        }
    }
}



