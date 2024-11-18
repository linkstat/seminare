package ar.com.hmu.model;

import ar.com.hmu.constants.TipoUsuario;

import java.util.UUID;

public class Rol {

    private UUID id;
    private String nombre;
    private String descripcion;
    private TipoUsuario tipoUsuario;

    // Constructor por defecto
    public Rol() {
    }

    // Nuevo constructor que acepta TipoUsuario
    public Rol(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
        this.nombre = tipoUsuario.getInternalName();
        this.descripcion = tipoUsuario.getDisplayName();
    }

    // Getters

    public UUID getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }


    // Setters


    public void setId(UUID id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setTipoUsuario(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Rol)) return false;
        Rol other = (Rol) obj;
        return tipoUsuario == other.tipoUsuario;
    }

    @Override
    public int hashCode() {
        return tipoUsuario != null ? tipoUsuario.hashCode() : 0;
    }

}
