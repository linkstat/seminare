package ar.com.hmu.roles.impl;

import ar.com.hmu.roles.AgenteRole;
import ar.com.hmu.model.Usuario;

public class AgenteRoleImpl implements AgenteRole {
    private Usuario usuario;

    public AgenteRoleImpl(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public void pedirFaltaJustificada() {
        System.out.println(usuario.getNombres() + " está pidiendo una falta justificada.");
        // Implementar la lógica necesaria aquí
    }
}
