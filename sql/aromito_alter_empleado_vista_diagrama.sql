-- ----------------------------------------------------------------------------
-- Migración RFS02 Fase 5: permiso de vista del diagrama por empleado
--
-- veDiagramaCompleto: decide qué ve el empleado en "Mi Diagrama".
--   TRUE  -> la grilla completa de su servicio (todas las filas), como la
--            cartelera de papel del servicio. Valor por defecto (decisión
--            del usuario 2026-07-08: transparencia primero).
--   FALSE -> únicamente sus propias jornadas.
--
-- Lo administra la jefatura del servicio (también OP y Dirección) desde el
-- diálogo "Vista de empleados…" de la pantalla de diagramación.
-- ----------------------------------------------------------------------------

ALTER TABLE Empleado
    ADD COLUMN IF NOT EXISTS veDiagramaCompleto BOOLEAN NOT NULL DEFAULT TRUE;
