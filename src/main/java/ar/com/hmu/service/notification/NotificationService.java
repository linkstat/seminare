package ar.com.hmu.service.notification;

import ar.com.hmu.model.Memorandum;
import ar.com.hmu.model.Usuario;

/**
 * Abstracción para notificaciones disparadas por eventos del módulo de
 * memorándums. La implementación concreta del pase 1 es por email
 * ({@code EmailNotificationService}); a futuro pueden sumarse otras
 * (in-app, push, etc.) sin tocar al {@code MemorandumService}.
 */
public interface NotificationService {

    /**
     * Envía una notificación al destinatario indicado para el evento dado.
     * La invocación debe ser fire-and-forget: no bloquea ni propaga errores
     * de transporte. La política de reintentos (si la hubiera) es
     * responsabilidad de la implementación.
     *
     * @param destinatario  usuario al que se le notifica.
     * @param evento        tipo de evento que disparó la notificación.
     * @param memo          memo asociado al evento (no debe ser null).
     * @param comentarios   contexto adicional (motivo de rechazo, observación);
     *                      puede ser null si el evento no aplica.
     */
    void notify(Usuario destinatario, TipoEventoMemorandum evento, Memorandum memo, String comentarios);
}
