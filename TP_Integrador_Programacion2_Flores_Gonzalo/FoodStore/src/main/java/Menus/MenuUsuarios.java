/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Menus;
import entities.Usuario;
import enums.Rol;
import services.DataStore;
import services.InputHelper;
import java.util.List;

/**
 *
 * @author Wilde
 */
public class MenuUsuarios {

    private final DataStore ds = DataStore.getInstance();

    public void mostrar() {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n------ USUARIOS ------");
            System.out.println("1. Listar usuarios");
            System.out.println("2. Crear usuario");
            System.out.println("3. Editar usuario");
            System.out.println("4. Eliminar usuario");
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
        System.out.println("\n--- Listado de Usuarios ---");
        List<Usuario> activos = ds.getUsuarios().stream()
                .filter(u -> !u.isEliminado()).toList();
        if (activos.isEmpty()) {
            System.out.println("No hay usuarios registrados.");
            return;
        }
        System.out.printf("%-5s %-15s %-15s %-30s %-10s%n",
                "ID", "NOMBRE", "APELLIDO", "MAIL", "ROL");
        System.out.println("-".repeat(77));
        for (Usuario u : activos) {
            System.out.printf("%-5d %-15s %-15s %-30s %-10s%n",
                    u.getId(), u.getNombre(), u.getApellido(), u.getMail(), u.getRol());
        }
    }

    private void crear() {
        System.out.println("\n--- Crear Usuario ---");
        String nombre    = InputHelper.readNonEmpty("Nombre: ");
        String apellido  = InputHelper.readNonEmpty("Apellido: ");
        String mail      = leerMailUnico(null);
        String celular   = InputHelper.readNonEmpty("Celular: ");
        String contrasena = InputHelper.readNonEmpty("Contrasenia: ");
        Rol rol          = seleccionarRol();

        long id = ds.nextUsuarioId();
        Usuario u = new Usuario(id, nombre, apellido, mail, celular, contrasena, rol);
        ds.addUsuario(u);
        System.out.println("Usuario creado con id: " + id);
    }

    private void editar() {
        System.out.println("\n--- Editar Usuario ---");
        listar();
        long id = InputHelper.readLong("Id del usuario a editar: ");
        Usuario u = ds.findUsuarioById(id);
        if (u == null) {
            System.out.println("Usuario no encontrado o eliminado.");
            return;
        }

        System.out.println("Deje en blanco para conservar el valor actual.");

        String nombre = InputHelper.readLine("Nuevo nombre [" + u.getNombre() + "]: ");
        if (!nombre.isEmpty()) u.setNombre(nombre);

        String apellido = InputHelper.readLine("Nuevo apellido [" + u.getApellido() + "]: ");
        if (!apellido.isEmpty()) u.setApellido(apellido);

        String mail = InputHelper.readLine("Nuevo mail [" + u.getMail() + "]: ");
        if (!mail.isEmpty()) {
            if (ds.existsUsuarioMail(mail, id)) {
                System.out.println("Ese mail ya esta en uso, se conserva el anterior.");
            } else {
                u.setMail(mail);
            }
        }

        String celular = InputHelper.readLine("Nuevo celular [" + u.getCelular() + "]: ");
        if (!celular.isEmpty()) u.setCelular(celular);

        System.out.println("Usuario actualizado correctamente.");
    }

    private void eliminar() {
        System.out.println("\n--- Eliminar Usuario ---");
        listar();
        long id = InputHelper.readLong("Id del usuario a eliminar: ");
        Usuario u = ds.findUsuarioById(id);
        if (u == null) {
            System.out.println("Usuario no encontrado o ya eliminado.");
            return;
        }

        if (InputHelper.confirm("Confirma eliminar a " + u.getNombre() + " " + u.getApellido() + "?")) {
            u.setEliminado(true);
            System.out.println("Usuario eliminado (baja logica), sus pedidos historicos se conservan.");
        } else {
            System.out.println("Operacion cancelada.");
        }
    }

    // Helpers

    private String leerMailUnico(Long excludeId) {
        while (true) {
            String mail = InputHelper.readNonEmpty("Mail: ");
            if (ds.existsUsuarioMail(mail, excludeId)) {
                System.out.println("Ese mail ya esta registrado, ingrese otro.");
            } else {
                return mail;
            }
        }
    }

    private Rol seleccionarRol() {
        System.out.println("Rol: 1. ADMIN  2. USUARIO");
        int op = InputHelper.readOption("Opcion: ", 1, 2);
        return op == 1 ? Rol.ADMIN : Rol.USUARIO;
    }
}
