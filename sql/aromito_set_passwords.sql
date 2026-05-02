/* ============================================================================
 * Aromito - Setup de contraseñas para pruebas (PostgreSQL)
 *
 * Establece la contraseña de dos usuarios igual a su CUIL para probar la
 * detección de "contraseña por defecto" del flujo de login.
 *
 * Hashes BCrypt (rounds=12) generados con `htpasswd -bnBC 12 ""` en GNU/Linux
 * o https://bcrypt-generator.com/. La aplicación los re-hashea silenciosamente
 * a Argon2id en el primer login (ver paso 2 del roadmap).
 *
 * Mapeo:
 *   UserA: cuil 27284644443 (Florencia Maurino), pass = '27284644443'
 *   UserB: cuil 24554978443 (Sebastián Bustos),  pass = '24554978443'
 *
 * Nota: a diferencia de la versión MariaDB, este script ya NO crea el usuario
 * de BD ni asigna privilegios. La gestión de roles/usuarios de PostgreSQL se
 * hace fuera de este script (ver INSTALL.md cuando exista).
 * ========================================================================== */

UPDATE Usuario
SET passwd = '$2a$12$TCAy.mqVsaRIwjQs0imr/uD0xNoUq/W3LSVys3EISI5hdBREwZW5a'
WHERE cuil = 27284644443;

UPDATE Usuario
SET passwd = '$2a$12$x9SDga8sk3DyjvdeIwkhl.2e9wcWHewfHEFQFKkFp1.FiAKcyRZUG'
WHERE cuil = 24554978443;

-- Verificación
SELECT nombres, apellidos, passwd
FROM Usuario
WHERE cuil IN (27284644443, 24554978443);
