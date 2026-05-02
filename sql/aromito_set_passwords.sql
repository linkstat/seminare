/* ============================================================================
 * Aromito - Setup de contraseñas para pruebas (PostgreSQL)
 *
 * Establece la contraseña de dos usuarios igual a su CUIL para probar la
 * detección de "contraseña por defecto" del flujo de login.
 *
 * Hashes BCrypt (rounds=12) generados con `htpasswd -bnBC 12 ""` en GNU/Linux.
 * La aplicación los re-hashea silenciosamente a Argon2id en el primer login
 * (ver paso 2 del roadmap).
 *
 * Este script es redundante con aromito_operaciones_CRUD.sql, que ya setea
 * estos hashes en el INSERT inicial de Usuario. Se mantiene por si se quiere
 * reset rápido sin recargar todo el dataset.
 *
 * Mapeo:
 *   UserA: cuil 27111122277 (Medina, Rosa),  pass = '27111122277'
 *   UserB: cuil 24111144499 (Valdez, Mateo), pass = '24111144499'
 * ========================================================================== */

UPDATE Usuario
SET passwd = '$2y$12$37LI3.hbNU74v2eYm8VkgevLX47FH0jBM2OIxM0C6HtS9lxmpgoVK'
WHERE cuil = 27111122277;

UPDATE Usuario
SET passwd = '$2y$12$YhlvKLqVs19Wk6AFuU0HG.UP94gF6A9yvWYHhBdyENxazMJk/H1vC'
WHERE cuil = 24111144499;

-- Verificación
SELECT nombres, apellidos, passwd
FROM Usuario
WHERE cuil IN (27111122277, 24111144499);
