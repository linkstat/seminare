-- Actualizar contraseñas usando Bcrypt
/* Vamos a establecer una contraseña igual al nro de CUIL
 * para un par de usuarios dados, de forma tal que podamos
 * probar la funcionalidad de detección de contraseña por defecto
 * que realiza la aplicación al iniciar sesión.
 * Usando Bcrypt-Generator.com (en https://bcrypt-generator.com/ ), generamos:
 * Rounds: 12
 * String para UserA: CUIL / Pass: '27284644443' (correspondiente a: 'Florencia Maurino')
 * String para UserB: CUIL / Pass: '24554978443' (correspondiente a: 'Sebastian Bustos')
 */
SET @cuilA = '27284644443';
SET @cuilB = '24554978443';
SET @userAPass = '$2a$12$TCAy.mqVsaRIwjQs0imr/uD0xNoUq/W3LSVys3EISI5hdBREwZW5a';
SET @userBPass = '$2a$12$x9SDga8sk3DyjvdeIwkhl.2e9wcWHewfHEFQFKkFp1.FiAKcyRZUG';

UPDATE usuario
SET passwd = @userAPass
WHERE cuil = @cuilA;

UPDATE usuario
SET passwd = @userBPass
WHERE cuil = @cuilB;

-- Verificamos nuevos valores:
SELECT nombres, apellidos, passwd FROM Usuario WHERE (cuil = @cuilA OR cuil = @cuilB);



/* Creación de un usuario propietario para la BD (acceso localhost unicamente)
 * user: aromito
 * pass: aromitoSuperSecretDBPass
 */
CREATE USER 'aromito'@'localhost' IDENTIFIED BY 'aromitoSuperSecretDBPass';
GRANT ALL PRIVILEGES ON aromito.* TO 'aromito'@'localhost';
FLUSH PRIVILEGES;
