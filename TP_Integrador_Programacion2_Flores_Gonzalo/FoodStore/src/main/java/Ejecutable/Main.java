package Ejecutable;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Wilde
 */
import entities.Categoria;
import entities.Pedido;
import entities.Producto;
import entities.Usuario;
import enums.FormaPago;
import enums.Rol;
import Menus.MenuCategorias;
import Menus.MenuPedidos;
import Menus.MenuProductos;
import Menus.MenuUsuarios;
import services.DataStore;
import services.InputHelper;

/**
 * Punto de entrada de la aplicacion.
 * Carga datos de ejemplo y presenta el menu principal.
 */
public class Main {

    public static void main(String[] args) {
        cargarDatosEjemplo();

        boolean salir = false;
        while (!salir) {
            System.out.println("\n-----------------------------");
            System.out.println("       SISTEMA DE PEDIDOS       ");
            System.out.println("-------------------------------");
            System.out.println("  1. Categorias               ");
            System.out.println("  2. Productos                ");
            System.out.println("  3. Usuarios                 ");
            System.out.println("  4. Pedidos                  ");
            System.out.println("  0. Salir                    ");
            System.out.println("-------------------------------");
            int op = InputHelper.readOption("Seleccione una opcion: ", 0, 4);
            switch (op) {
                case 1 -> new MenuCategorias().mostrar();
                case 2 -> new MenuProductos().mostrar();
                case 3 -> new MenuUsuarios().mostrar();
                case 4 -> new MenuPedidos().mostrar();
                case 0 -> salir = true;
            }
        }
        System.out.println("Fin del proceso");
    }

    private static void cargarDatosEjemplo() {
        DataStore ds = DataStore.getInstance();

        // Categorias
        Categoria bebidas = new Categoria(ds.nextCategoriaId(), "Bebidas", "Bebidas frias y calientes");
        Categoria postres = new Categoria(ds.nextCategoriaId(), "Postres", "Dulces y postres");
        Categoria comidas = new Categoria(ds.nextCategoriaId(), "Comidas", "Platos principales");
        ds.addCategoria(bebidas);
        ds.addCategoria(postres);
        ds.addCategoria(comidas);

        // Productos
        Producto cafe     = new Producto(ds.nextProductoId(), "Cafe",               1500.0, "Cafe negro",         50, "cafe.png",     bebidas);
        Producto te       = new Producto(ds.nextProductoId(), "Te",                 1200.0, "Te en hebras",       40, "te.png",       bebidas);
        Producto torta    = new Producto(ds.nextProductoId(), "Torta de chocolate", 2500.0, "Porcion individual", 20, "torta.png",    postres);
        Producto flan     = new Producto(ds.nextProductoId(), "Flan",               1800.0, "Flan casero",        15, "flan.png",     postres);
        Producto milanesa = new Producto(ds.nextProductoId(), "Milanesa con papas", 5200.0, "Milanesa de carne",  10, "milanesa.png", comidas);
        Producto pizza    = new Producto(ds.nextProductoId(), "Pizza muzzarella",   4800.0, "Porcion grande",     12, "pizza.png",    comidas);
        ds.addProducto(cafe);
        ds.addProducto(te);
        ds.addProducto(torta);
        ds.addProducto(flan);
        ds.addProducto(milanesa);
        ds.addProducto(pizza);

        // Usuarios
        Usuario admin   = new Usuario(ds.nextUsuarioId(), "Gonza",  "Flores",  "gonza@gmail.com",  "1111", "clave123", Rol.ADMIN);
        Usuario cliente = new Usuario(ds.nextUsuarioId(), "Mayra", "Arroyo", "mayra@gmail.com", "2222", "clave456", Rol.USUARIO);
        ds.addUsuario(admin);
        ds.addUsuario(cliente);

        // Pedido de ejemplo
        Pedido pedido = new Pedido(ds.nextPedidoId(), FormaPago.TARJETA, cliente);
        pedido.addDetallePedido(2, cafe);
        pedido.addDetallePedido(1, torta);
        pedido.calcularTotal();
        ds.addPedido(pedido);

        System.out.println("Datos de ejemplo cargados (3 categorias, 6 productos, 2 usuarios, 1 pedido).");
    }
}
