package ar.com.hmu.auth;

import ar.com.hmu.model.Usuario;
import ar.com.hmu.model.Direccion;
import ar.com.hmu.model.OficinaDePersonal;
import ar.com.hmu.model.JefaturaDeServicio;
import java.util.ArrayList;
import java.util.List;

/**
 * Servicio para manejar la lógica del menú principal, como la autorización de acceso a módulos.
 */
public class MainMenuService {

    /**
     * Devuelve la lista de módulos disponibles para un usuario en función de su tipo concreto.
     *
     * @param usuario el usuario autenticado.
     * @return una lista de nombres de módulos a los que el usuario tiene acceso.
     */
    public List<String> getAvailableModules(Usuario usuario) {
        List<String> modules = new ArrayList<>();

        switch (usuario) {
            case OficinaDePersonal oficinaDePersonal -> {
                modules.add("Gestión de Altas, Bajas y Modificaciones de empleados");
                modules.add("Módulo de Memorandums");
            }
            case JefaturaDeServicio jefaturaDeServicio -> {
                modules.add("Módulo de Diagrama de Servicios");
                modules.add("Módulo de Parte Diario");
            }
            case Direccion direccion -> {
                modules.add("Módulo de Reportes");
                modules.add("Módulo de Novedades");
                modules.add("Módulo de Memorandums");
            }
            case null, default ->
                // Módulos para otros tipos de empleados (podemos agregar "Empleado" o cualquier otro tipo específico)
                    modules.add("Módulo de Marcaciones");
        }

        return modules;
    }

}
