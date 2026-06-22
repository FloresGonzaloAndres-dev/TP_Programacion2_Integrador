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
public class MenuProductos {

    private final DataStore ds = DataStore.getInstance();

    public void mostrar() {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n------ PRODUCTOS ------");
            System.out.println("1. Listar productos");
            System.out.println("2. Crear producto");
            System.out.println("3. Editar producto");
            System.out.println("4. Eliminar producto");
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
        System.out.println("\n--- Listado de Productos ---");
        System.out.println("¿Filtrar por categoria?");
        System.out.println("1. Listar todos");
        System.out.println("2. Filtrar por categoria");
        int op = InputHelper.readOption("Opcion: ", 1, 2);

        List<Producto> lista = ds.getProductos().stream()
                .filter(p -> !p.isEliminado()).toList();

        if (op == 2) {
            new MenuCategorias().listar();
            long catId = InputHelper.readLong("Id de categoria: ");
            lista = lista.stream()
                    .filter(p -> p.getCategoria() != null && p.getCategoria().getId() == catId)
                    .toList();
        }

        if (lista.isEmpty()) {
            System.out.println("No hay productos para mostrar.");
            return;
        }

        System.out.printf("%-5s %-25s %-10s %-7s %-15s%n",
                "ID", "NOMBRE", "PRECIO", "STOCK", "CATEGORIA");
        System.out.println("-".repeat(64));
        for (Producto p : lista) {
            String cat = p.getCategoria() != null ? p.getCategoria().getNombre() : "-";
            System.out.printf("%-5d %-25s %-10.2f %-7d %-15s%n",
                    p.getId(), p.getNombre(), p.getPrecio(), p.getStock(), cat);
        }
    }

    private void crear() {
        System.out.println("\n--- Crear Producto ---");

        String nombre = InputHelper.readNonEmpty("Nombre: ");
        String descripcion = InputHelper.readNonEmpty("Descripcion: ");
        double precio = InputHelper.readNonNegativeDouble("Precio: ");
        int stock = InputHelper.readNonNegativeInt("Stock: ");
        String imagen = InputHelper.readLine("Imagen (nombre de archivo): ");

        new MenuCategorias().listar();
        long catId = InputHelper.readLong("Id de categoria: ");
        Categoria cat = ds.findCategoriaById(catId);
        if (cat == null) {
            System.out.println("Categoria no encontrada o eliminada. Se cancela la creacion.");
            return;
        }

        long id = ds.nextProductoId();
        Producto p = new Producto(id, nombre, precio, descripcion, stock, imagen, cat);
        ds.addProducto(p);
        System.out.println("Producto creado con id: " + id);
    }

    private void editar() {
        System.out.println("\n--- Editar Producto ---");
        listar();
        long id = InputHelper.readLong("Id del producto a editar: ");
        Producto p = ds.findProductoById(id);
        if (p == null) {
            System.out.println("Producto no encontrado o eliminado.");
            return;
        }

        System.out.println("Deje en blanco para conservar el valor actual.");

        String nombre = InputHelper.readLine("Nuevo nombre [" + p.getNombre() + "]: ");
        if (!nombre.isEmpty()) p.setNombre(nombre);

        String desc = InputHelper.readLine("Nueva descripcion [" + p.getDescripcion() + "]: ");
        if (!desc.isEmpty()) p.setDescripcion(desc);

        String precioStr = InputHelper.readLine("Nuevo precio [" + p.getPrecio() + "]: ");
        if (!precioStr.isEmpty()) {
            try {
                double nuevo = Double.parseDouble(precioStr);
                if (nuevo < 0) System.out.println("Precio negativo ignorado, se conserva el anterior.");
                else p.setPrecio(nuevo);
            } catch (NumberFormatException e) {
                System.out.println("Valor invalido, se conserva el precio anterior.");
            }
        }

        String stockStr = InputHelper.readLine("Nuevo stock [" + p.getStock() + "]: ");
        if (!stockStr.isEmpty()) {
            try {
                int nuevo = Integer.parseInt(stockStr);
                if (nuevo < 0) System.out.println("Stock negativo ignorado, se conserva el anterior.");
                else p.setStock(nuevo);
            } catch (NumberFormatException e) {
                System.out.println("Valor invalido, se conserva el stock anterior.");
            }
        }

        String imagen = InputHelper.readLine("Nueva imagen [" + p.getImagen() + "]: ");
        if (!imagen.isEmpty()) p.setImagen(imagen);

        // Cambiar categoria (opcional)
        if (InputHelper.confirm("¿Desea cambiar la categoria?")) {
            new MenuCategorias().listar();
            long catId = InputHelper.readLong("Id de nueva categoria: ");
            Categoria cat = ds.findCategoriaById(catId);
            if (cat == null) {
                System.out.println("Categoria no encontrada, se conserva la anterior.");
            } else {
                p.setCategoria(cat);
            }
        }

        System.out.println("Producto actualizado correctamente.");
    }

    private void eliminar() {
        System.out.println("\n--- Eliminar Producto ---");
        listar();
        long id = InputHelper.readLong("Id del producto a eliminar: ");
        Producto p = ds.findProductoById(id);
        if (p == null) {
            System.out.println("Producto no encontrado o ya eliminado.");
            return;
        }

        if (InputHelper.confirm("Confirma eliminar \"" + p.getNombre() + "\"?")) {
            p.setEliminado(true);
            System.out.println("Producto eliminado (baja logica).");
        } else {
            System.out.println("Operacion cancelada.");
        }
    }
}

