package ar.com.hmu.constants;

public enum TipoUsuario {
    AGENTE("Agente", "Agente"),
    JEFEDESERVICIO("JefeDeServicio", "Jefe de Servicio"),
    OFICINADEPERSONAL("OficinaDePersonal", "Oficina de Personal"),
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

    // Método de conversión para obtener el Enum a partir de la cadena de la base de datos
    public static TipoUsuario fromInternalName(String internalName) {
        for (TipoUsuario tipo : TipoUsuario.values()) {
            if (tipo.getInternalName().equalsIgnoreCase(internalName)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Tipo de usuario desconocido: " + internalName);
    }

    @Override
    public String toString() {
        return displayName;  // Por defecto, toString() mostrará el texto alternativo
    }
}
