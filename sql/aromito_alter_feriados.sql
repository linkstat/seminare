-- ----------------------------------------------------------------------------
-- Migración: tabla de Feriados con workflow de autorización
--
-- Los feriados alimentan la diagramación de servicios (generadores,
-- validador, plantilla feriante): un feriado vigente cuenta como día no
-- laborable para todos los usuarios.
--
-- Workflow (decisión del usuario 2026-07-08):
--   * La CARGA ANUAL la hace la Oficina de Personal en bloque y queda
--     VIGENTE directa (sólo permitida si el año aún no tiene feriados).
--   * Los cambios posteriores (feriado administrativo imprevisto, baja)
--     son propuestas de OP que AUTORIZA LA DIRECCIÓN:
--       ALTA_PENDIENTE -(autoriza)-> VIGENTE   | -(rechaza)-> RECHAZADO
--       BAJA_PENDIENTE -(autoriza)-> ANULADO   | -(rechaza)-> VIGENTE
--   * Un feriado con baja pendiente SIGUE VIGENTE hasta la autorización.
--     RECHAZADO y ANULADO quedan como historia (audit).
-- ----------------------------------------------------------------------------

DO $$ BEGIN
    CREATE TYPE estado_feriado AS ENUM
        ('VIGENTE', 'ALTA_PENDIENTE', 'BAJA_PENDIENTE', 'RECHAZADO', 'ANULADO');
EXCEPTION WHEN duplicate_object THEN NULL;
END $$;

CREATE TABLE IF NOT EXISTS Feriado (
    id UUID PRIMARY KEY,
    fecha DATE NOT NULL,
    descripcion VARCHAR(255) NOT NULL,
    estado estado_feriado NOT NULL DEFAULT 'VIGENTE',
    creadoPorID UUID NOT NULL,
    resueltoPorID UUID NULL,          -- quién autorizó/rechazó (Dirección)
    fechaResolucion TIMESTAMP NULL,
    createdAt TIMESTAMP NOT NULL DEFAULT now(),
    FOREIGN KEY (creadoPorID) REFERENCES Usuario(id),
    FOREIGN KEY (resueltoPorID) REFERENCES Usuario(id)
);

-- Un solo feriado "activo" por fecha; los RECHAZADO/ANULADO son historia.
CREATE UNIQUE INDEX IF NOT EXISTS idx_feriado_fecha_activo ON Feriado(fecha)
    WHERE estado IN ('VIGENTE', 'ALTA_PENDIENTE', 'BAJA_PENDIENTE');
