-- ----------------------------------------------------------------------------
-- Migración: agregar OBSERVADO al ciclo de autorización + campo comentarios
--
-- Aplicar sobre BDs existentes que se crearon antes de habilitar el ciclo
-- "memo observado para corrección" en el módulo de memorándums.
--
-- OBSERVADO en estado_memo_autorizacion: cuando el autorizador no aprueba
-- ni rechaza, sino que solicita correcciones al remitente. El remitente
-- corrige el memo y lo reenvía, generando una nueva fila en
-- Memorandum_Autorizacion (la fila vieja queda OBSERVADO como audit trail).
--
-- comentarios en Memorandum_Autorizacion: campo libre para que el
-- autorizador deje el motivo del rechazo o las observaciones a corregir.
-- ----------------------------------------------------------------------------

ALTER TYPE estado_memo_autorizacion ADD VALUE IF NOT EXISTS 'OBSERVADO';

ALTER TABLE Memorandum_Autorizacion
    ADD COLUMN IF NOT EXISTS comentarios TEXT NULL;
