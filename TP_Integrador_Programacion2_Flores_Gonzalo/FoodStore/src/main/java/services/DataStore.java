/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

/**
 *
 * @author Wilde
 */
import entities.Categoria;
import entities.Pedido;
import entities.Producto;
import entities.Usuario;

import java.util.ArrayList;
import java.util.List;

/**
 * Repositorio central en memoria para todas las colecciones.
 */
public class DataStore {

    private static final DataStore INSTANCE = new DataStore();

    private final List<Categoria> categorias = new ArrayList<>();
    private final List<Producto>  productos  = new ArrayList<>();
    private final List<Usuario>   usuarios   = new ArrayList<>();
    private final List<Pedido>    pedidos    = new ArrayList<>();

    private long nextCatId  = 1;
    private long nextProdId = 1;
    private long nextUsrId  = 1;
    private long nextPedId  = 1;

    private DataStore() {}

    public static DataStore getInstance() { return INSTANCE; }

    // --- Categorías ---
    public List<Categoria> getCategorias()       { return categorias; }
    public long nextCategoriaId()                { return nextCatId++; }
    public void addCategoria(Categoria c)        { categorias.add(c); }

    public Categoria findCategoriaById(long id) {
        return categorias.stream()
                .filter(c -> c.getId() == id && !c.isEliminado())
                .findFirst().orElse(null);
    }

    public boolean existsCategoriaNombre(String nombre, Long excludeId) {
        return categorias.stream()
                .filter(c -> !c.isEliminado())
                .filter(c -> excludeId == null || !c.getId().equals(excludeId))
                .anyMatch(c -> c.getNombre().equalsIgnoreCase(nombre));
    }

    // --- Productos ---
    public List<Producto> getProductos()         { return productos; }
    public long nextProductoId()                 { return nextProdId++; }
    public void addProducto(Producto p)          { productos.add(p); }

    public Producto findProductoById(long id) {
        return productos.stream()
                .filter(p -> p.getId() == id && !p.isEliminado())
                .findFirst().orElse(null);
    }

    // --- Usuarios ---
    public List<Usuario> getUsuarios()           { return usuarios; }
    public long nextUsuarioId()                  { return nextUsrId++; }
    public void addUsuario(Usuario u)            { usuarios.add(u); }

    public Usuario findUsuarioById(long id) {
        return usuarios.stream()
                .filter(u -> u.getId() == id && !u.isEliminado())
                .findFirst().orElse(null);
    }

    public boolean existsUsuarioMail(String mail, Long excludeId) {
        return usuarios.stream()
                .filter(u -> !u.isEliminado())
                .filter(u -> excludeId == null || !u.getId().equals(excludeId))
                .anyMatch(u -> u.getMail().equalsIgnoreCase(mail));
    }

    // --- Pedidos ---
    public List<Pedido> getPedidos()             { return pedidos; }
    public long nextPedidoId()                   { return nextPedId++; }
    public void addPedido(Pedido p)              { pedidos.add(p); }

    public Pedido findPedidoById(long id) {
        return pedidos.stream()
                .filter(p -> p.getId() == id && !p.isEliminado())
                .findFirst().orElse(null);
    }
}
