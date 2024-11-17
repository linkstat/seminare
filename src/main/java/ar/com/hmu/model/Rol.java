package ar.com.hmu.model;

import java.util.UUID;

public class Rol {

    private UUID id;
    private String nombre;
    private String descripcion;

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

}
