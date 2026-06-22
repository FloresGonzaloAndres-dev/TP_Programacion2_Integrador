/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Menus;
import entities.DetallePedido;
import entities.Pedido;
import entities.Producto;
import entities.Usuario;
import enums.Estado;
import enums.FormaPago;
import services.DataStore;
import services.InputHelper;

import java.util.List;

/**
 *
 * @author Wilde
 */
public class MenuPedidos {

    private final DataStore ds = DataStore.getInstance();

    public void mostrar() {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n------ PEDIDOS ------");
            System.out.println("1. Listar pedidos");
            System.out.println("2. Crear pedido");
            System.out.println("3. Actualizar estado / forma de pago");
            System.out.println("4. Eliminar pedido");
            System.out.println("0. Volver al menú principal");
            int op = InputHelper.readOption("Seleccione una opcion: ", 0, 4);
            switch (op) {
                case 1 -> listar();
                case 2 -> crear();
                case 3 -> actualizarEstadoOFormaPago();
                case 4 -> eliminar();
                case 0 -> volver = true;
            }
        }
    }

    public void listar() {
        System.out.println("\n--- Listado de Pedidos ---");
        System.out.println("1. Todos los pedidos");
        System.out.println("2. Filtrar por usuario");
        int op = InputHelper.readOption("Opcion: ", 1, 2);

        List<Pedido> lista = ds.getPedidos().stream()
                .filter(p -> !p.isEliminado()).toList();

        if (op == 2) {
            new MenuUsuarios().listar();
            long usrId = InputHelper.readLong("Id de usuario: ");
            lista = lista.stream()
                    .filter(p -> p.getUsuario() != null && p.getUsuario().getId() == usrId)
                    .toList();
        }

        if (lista.isEmpty()) {
            System.out.println("No hay pedidos para mostrar.");
            return;
        }

        System.out.printf("%-5s %-15s %-12s %-14s %-10s %-12s%n",
                "ID", "USUARIO", "ESTADO", "FORMA PAGO", "TOTAL", "FECHA");
        System.out.println("-".repeat(70));
        for (Pedido p : lista) {
            String usr = p.getUsuario() != null ? p.getUsuario().getNombre() + " " + p.getUsuario().getApellido() : "-";
            System.out.printf("%-5d %-15s %-12s %-14s %-10.2f %-12s%n",
                    p.getId(), usr, p.getEstado(), p.getFormaPago(), p.getTotal(), p.getFecha());
        }
    }

    private void crear() {
        System.out.println("\n--- Crear Pedido ---");

        // Seleccionar usuario
        new MenuUsuarios().listar();
        long usrId = InputHelper.readLong("Id de usuario: ");
        Usuario usuario = ds.findUsuarioById(usrId);
        if (usuario == null) {
            System.out.println("Usuario no encontrado o eliminado. Se cancela la operacion.");
            return;
        }

        // Forma de pago
        FormaPago formaPago = seleccionarFormaPago();

        // Crear pedido (se vincula automaticamente al usuario en el constructor)
        long pedidoId = ds.nextPedidoId();
        Pedido pedido = new Pedido(pedidoId, formaPago, usuario);

        // Agregar detalles
        boolean agregarMas = true;
        while (agregarMas) {
            new MenuProductos().listar();
            long prodId = InputHelper.readLong("Id de producto a agregar: ");
            Producto producto = ds.findProductoById(prodId);
            if (producto == null) {
                System.out.println("Producto no encontrado o eliminado.");
            } else {
                int cantidad = InputHelper.readNonNegativeInt("Cantidad: ");
                if (cantidad == 0) {
                    System.out.println("La cantidad debe ser mayor a 0.");
                } else {
                    try {
                        // Verificar stock disponible
                        if (cantidad > producto.getStock()) {
                            throw new IllegalArgumentException(
                                "Stock insuficiente. Disponible: " + producto.getStock());
                        }
                        pedido.addDetallePedido(cantidad, producto);
                        System.out.printf("Detalle agregado: %s x%d = $%.2f%n",
                                producto.getNombre(), cantidad,
                                pedido.findeDetallePedidoByProducto(producto).getSubtotal());
                    } catch (Exception e) {
                        System.out.println("Error al agregar detalle: " + e.getMessage());
                        System.out.println("Se cancela la creacion del pedido para evitar datos inconsistentes.");
                        // Eliminar el pedido parcial de la lista del usuario
                        usuario.getPedidos().remove(pedido);
                        return;
                    }
                }
            }

            agregarMas = InputHelper.confirm("¿Agregar otro producto?");
        }

        if (pedido.getDetalles().isEmpty()) {
            System.out.println("El pedido no tiene detalles. No se guardara.");
            usuario.getPedidos().remove(pedido);
            return;
        }

        // calcularTotal() ya se invoca desde addDetallePedido, pero lo llamamos
        // explicitamente para cumplir el criterio de usar la interfaz Calculable.
        pedido.calcularTotal();

        ds.addPedido(pedido);
        System.out.printf("Pedido creado con id: %d | Total: $%.2f%n", pedidoId, pedido.getTotal());
    }

    private void actualizarEstadoOFormaPago() {
        System.out.println("\n--- Actualizar Estado / Forma de Pago ---");
        listar();
        long id = InputHelper.readLong("Id del pedido a modificar: ");
        Pedido pedido = ds.findPedidoById(id);
        if (pedido == null) {
            System.out.println("Pedido no encontrado o eliminado.");
            return;
        }

        System.out.println("Que desea modificar?");
        System.out.println("1. Estado");
        System.out.println("2. Forma de pago");
        System.out.println("3. Ambos");
        int op = InputHelper.readOption("Opcion: ", 1, 3);

        if (op == 1 || op == 3) {
            Estado nuevoEstado = seleccionarEstado(pedido.getEstado());
            pedido.setEstado(nuevoEstado);
            System.out.println("Estado actualizado a: " + nuevoEstado);
        }

        if (op == 2 || op == 3) {
            FormaPago nuevaForma = seleccionarFormaPago();
            pedido.setFormaPago(nuevaForma);
            System.out.println("Forma de pago actualizada a: " + nuevaForma);
        }

        System.out.println("Pedido actualizado correctamente.");
    }

    private void eliminar() {
        System.out.println("\n--- Eliminar Pedido ---");
        listar();
        long id = InputHelper.readLong("Id del pedido a eliminar: ");
        Pedido pedido = ds.findPedidoById(id);
        if (pedido == null) {
            System.out.println("Pedido no encontrado o ya eliminado.");
            return;
        }

        if (InputHelper.confirm("¿Confirma eliminar el pedido #" + id + "?")) {
            pedido.setEliminado(true);
            // Baja logica en detalles tambien
            for (DetallePedido d : pedido.getDetalles()) {
                d.setEliminado(true);
            }
            System.out.println("Pedido eliminado (baja logica). Los detalles tambien fueron marcados como eliminados.");
        } else {
            System.out.println("Operacion cancelada.");
        }
    }

    private FormaPago seleccionarFormaPago() {
        System.out.println("Forma de pago:");
        FormaPago[] formas = FormaPago.values();
        for (int i = 0; i < formas.length; i++) {
            System.out.printf("%d. %s%n", i + 1, formas[i]);
        }
        int op = InputHelper.readOption("Opcion: ", 1, formas.length);
        return formas[op - 1];
    }

    private Estado seleccionarEstado(Estado actual) {
        System.out.println("Estado actual: " + actual);
        System.out.println("Nuevo estado:");
        Estado[] estados = Estado.values();
        for (int i = 0; i < estados.length; i++) {
            System.out.printf("%d. %s%n", i + 1, estados[i]);
        }
        int op = InputHelper.readOption("Opcion: ", 1, estados.length);
        return estados[op - 1];
    }
}

