/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entities;

/**
 *
 * @author Wilde
 */
import java.util.ArrayList;
import java.util.List;

public class Producto extends Base {
    private String nombre;
    private Double precio;
    private String descripcion;
    private int stock;
    private String imagen;
    private Boolean disponible;
    private Categoria categoria;

    public Producto(Long id, String nombre, Double precio, String descripcion, int stock, String imagen, Categoria categoria) {
        super(id);
        this.nombre = nombre;
        this.precio = precio;
        this.descripcion = descripcion;
        this.stock = stock;
        this.imagen = imagen;
        this.disponible = stock > 0;
        this.categoria = categoria;
        if (categoria != null) {
            categoria.agregarProducto(this);
        }
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Double getPrecio() { return precio; }
    public void setPrecio(Double precio) { this.precio = precio; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public int getStock() { return stock; }
    public void setStock(int stock) {
        this.stock = stock;
        this.disponible = stock > 0;
    }

    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }

    public Boolean getDisponible() { return disponible; }
    public void setDisponible(Boolean disponible) { this.disponible = disponible; }

    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
        if (categoria != null) {
            categoria.agregarProducto(this);
        }
    }

    @Override
    public String toString() {
        return "Producto{id=" + getId() + ", nombre='" + nombre + "', precio=" + precio + ", stock=" + stock + ", categoria=" + (categoria != null ? categoria.getNombre() : "null") + ", eliminado=" + isEliminado() + ", createdAt=" + getCreatedAt() + "}";
    }
}
