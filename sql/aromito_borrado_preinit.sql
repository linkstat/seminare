/* ============================================================================
 * Aromito - Borrado total de la BD (PostgreSQL)
 *
 * Para regenerar desde cero. Borra todas las tablas, tipos enumerados y
 * deja la BD como recién creada.
 *
 * Equivalente a `DROP DATABASE aromito; CREATE DATABASE aromito;` pero sin
 * cerrar la conexión, útil cuando se está conectado como el usuario que sólo
 * tiene permisos sobre el schema y no sobre el cluster.
 *
 * Uso:
 *   psql -d aromito -f aromito_borrado_preinit.sql
 * ========================================================================== */

DROP SCHEMA public CASCADE;
CREATE SCHEMA public;

-- Restaurar permisos por defecto en el schema public
GRANT ALL ON SCHEMA public TO public;
