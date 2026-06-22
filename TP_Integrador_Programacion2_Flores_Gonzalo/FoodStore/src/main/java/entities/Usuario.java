/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entities;

/**
 *
 * @author Wilde
 */
import enums.Rol;
import java.util.ArrayList;
import java.util.List;

public class Usuario extends Base {
    private String nombre;
    private String apellido;
    private String mail;
    private String celular;
    private String contrasena;
    private Rol rol;
    private List<Pedido> pedidos;
 
    public Usuario(Long id, String nombre, String apellido, String mail, String celular, String contrasena, Rol rol) {
        super(id);
        this.nombre = nombre;
        this.apellido = apellido;
        this.mail = mail;
        this.celular = celular;
        this.contrasena = contrasena;
        this.rol = rol;
        this.pedidos = new ArrayList<>();
    }
 
    public void agregarPedido(Pedido pedido) { pedidos.add(pedido); }
 
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
 
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
 
    public String getMail() { return mail; }
    public void setMail(String mail) { this.mail = mail; }
 
    public String getCelular() { return celular; }
    public void setCelular(String celular) { this.celular = celular; }
 
    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
 
    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }
 
    public List<Pedido> getPedidos() { return pedidos; }
 
    @Override
    public String toString() {
        return "Usuario{id=" + getId() + ", nombre='" + nombre + "', apellido='" + apellido + "', mail='" + mail + "', rol=" + rol + ", eliminado=" + isEliminado() + ", createdAt=" + getCreatedAt() + "}";
    }
}

