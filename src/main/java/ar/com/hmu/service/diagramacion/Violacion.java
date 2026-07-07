package ar.com.hmu.service.diagramacion;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Una violación detectada por {@link DiagramaValidator} sobre las jornadas
 * de un diagrama. Inmutable; apunta al empleado y fecha del problema para
 * que la UI pueda resaltar la celda correspondiente en la grilla.
 *
 * @param empleadoId empleado afectado (no null).
 * @param fecha      día donde se detectó el problema (puede ser null para
 *                   violaciones agregadas, como carga horaria mensual).
 * @param mensaje    descripción legible del problema.
 */
public record Violacion(UUID empleadoId, LocalDate fecha, String mensaje) {

    @Override
    public String toString() {
        return (fecha != null ? fecha + ": " : "") + mensaje;
    }
}
