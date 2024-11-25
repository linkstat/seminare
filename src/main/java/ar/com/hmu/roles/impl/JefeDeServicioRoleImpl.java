package ar.com.hmu.roles.impl;

import ar.com.hmu.roles.JefeDeServicioRole;
import ar.com.hmu.model.Usuario;

public class JefeDeServicioRoleImpl implements JefeDeServicioRole {
    private Usuario usuario;

    public JefeDeServicioRoleImpl(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public void aprobarFaltas() {
        System.out.println(usuario.getNombres() + " está aprobando faltas.");
        // Implementar la lógica necesaria aquí
    }
}
