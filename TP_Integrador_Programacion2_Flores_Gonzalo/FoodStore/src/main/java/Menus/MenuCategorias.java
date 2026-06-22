/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Menus;
import entities.Categoria;
import entities.Producto;
import services.DataStore;
import services.InputHelper;

import java.util.List;
/**
 *
 * @author Wilde
 */
public class MenuCategorias {

    private final DataStore ds = DataStore.getInstance();

    public void mostrar() {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n----- CATEGORiAS ----");
            System.out.println("1. Listar categorias");
            System.out.println("2. Crear categoria");
            System.out.println("3. Editar categoria");
            System.out.println("4. Eliminar categoria");
            System.out.println("0. Volver al menu principal");
            int op = InputHelper.readOption("Seleccione una opcion: ", 0, 4);
            switch (op) {
                case 1 -> listar();
                case 2 -> crear();
                case 3 -> editar();
                case 4 -> eliminar();
                case 0 -> volver = true;
            }
        }
    }

    public void listar() {
        System.out.println("\n--- Listado de Categorias ---");
        List<Categoria> activas = ds.getCategorias().stream()
                .filter(c -> !c.isEliminado()).toList();
        if (activas.isEmpty()) {
            System.out.println("No hay categorias cargadas.");
            return;
        }
        System.out.printf("%-5s %-20s %-35s%n", "ID", "NOMBRE", "DESCRIPCION");
        System.out.println("-".repeat(62));
        for (Categoria c : activas) {
            System.out.printf("%-5d %-20s %-35s%n",
                    c.getId(), c.getNombre(), c.getDescripcion());
        }
    }

    private void crear() {
        System.out.println("\n--- Crear Categoria ---");
        String nombre = InputHelper.readNonEmpty("Nombre: ");

        if (ds.existsCategoriaNombre(nombre, null)) {
            System.out.println("Ya existe una categoria con ese nombre.");
            return;
        }

        String descripcion = InputHelper.readNonEmpty("Descripcion: ");
        long id = ds.nextCategoriaId();
        Categoria c = new Categoria(id, nombre, descripcion);
        ds.addCategoria(c);
        System.out.println("Categoria creada con id: " + id);
    }

    private void editar() {
        System.out.println("\n--- Editar Categoria ---");
        listar();
        long id = InputHelper.readLong("Id de la categoria a editar: ");
        Categoria c = ds.findCategoriaById(id);
        if (c == null) {
            System.out.println("Categoria no encontrada o eliminada.");
            return;
        }

        System.out.println("Deje en blanco para conservar el valor actual.");
        String nombre = InputHelper.readLine("Nuevo nombre [" + c.getNombre() + "]: ");
        if (!nombre.isEmpty()) {
            if (ds.existsCategoriaNombre(nombre, id)) {
                System.out.println("Ya existe otra categoria con ese nombre. Se cancela la edicion.");
                return;
            }
            c.setNombre(nombre);
        }

        String desc = InputHelper.readLine("Nueva descripcion [" + c.getDescripcion() + "]: ");
        if (!desc.isEmpty()) c.setDescripcion(desc);

        System.out.println("Categoria actualizada correctamente.");
    }

    private void eliminar() {
        System.out.println("\n--- Eliminar Categoria ---");
        listar();
        long id = InputHelper.readLong("Id de la categoria a eliminar: ");
        Categoria c = ds.findCategoriaById(id);
        if (c == null) {
            System.out.println("Categoria no encontrada o ya eliminada.");
            return;
        }

        // Verificar productos asociados
        long conProductos = c.getProductos().stream().filter(p -> !p.isEliminado()).count();
        if (conProductos > 0) {
            System.out.printf(
                "La categoria tiene %d producto(s) activo(s) asociado(s). " +
                "No se puede eliminar mientras tenga productos activos.%n", conProductos);
            return;
        }

        if (InputHelper.confirm("Confirma eliminar la categoria \"" + c.getNombre() + "\"?")) {
            c.setEliminado(true);
            System.out.println("Categoria eliminada (baja logica).");
        } else {
            System.out.println("Operacion cancelada.");
        }
    }
}
