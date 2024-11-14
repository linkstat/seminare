package ar.com.hmu.constants;

public enum TipoUsuario {
    EMPLEADO("Empleado", "Agente"),
    JEFATURA_DE_SERVICIO("JefaturaDeServicio", "Jefe de Servicio"),
    OFICINA_DE_PERSONAL("OficinaDePersonal", "Oficina de Personal"),
    DIRECCION("Direccion", "Directivo");

    private final String internalName;
    private final String displayName;

    TipoUsuario(String internalName, String displayName) {
        this.internalName = internalName;
        this.displayName = displayName;
    }

    public String getInternalName() {
        return internalName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;  // Por defecto, toString() mostrará el texto alternativo
    }
}
