/* ============================================================================
 * Aromito - Script de prueba: alta de usuarios con distintos roles
 *
 * Crea 4 usuarios con perfiles diferentes (Direccion, JefaturaDeServicio,
 * OficinaDePersonal, Empleado) para validar el flujo de roles del front.
 * Asume que ya corrieron aromito_creacion_de_tablas.sql y la inicialización
 * de Servicios/Cargos/Rol (ver aromito_operaciones_CRUD.sql).
 *
 * Cambios respecto a la versión previa:
 *  - Se eliminó la columna Usuario.tipoUsuario (los roles van por Usuario_Rol).
 *  - Las tablas hijas Direccion / JefeDeServicio / OficinaDePersonal ya no
 *    existen; sólo se inserta en Empleado para usuarios con rol EMPLEADO.
 *  - Nomenclatura: 'Empleado' en vez de 'Agente'; 'JefaturaDeServicio' en
 *    vez de 'JefeDeServicio'.
 * ========================================================================== */

-- Password por defecto: 'Aromito1' (BCrypt; será migrado a Argon2id en otro paso)
SET @initPass = '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO';


-- Crear usuario con rol: DIRECCION
SET @cuilDir = 20112223331;
SET @usuarioId1 = UUID_TO_BIN(UUID());
SET @fechaAltaDir = '2015-03-01';
SELECT id INTO @rolDireccion FROM Rol WHERE nombre = 'Direccion';
SELECT id INTO @domicilioId1 FROM Domicilio ORDER BY RAND() LIMIT 1;
SELECT id INTO @cargoDirectivoId FROM Cargo WHERE numero = 200;
SELECT id INTO @servDireccionId FROM Servicio WHERE nombre = 'Dirección';

INSERT INTO Usuario (id, fechaAlta, estado, cuil, apellidos, nombres, sexo, mail, tel, domicilioID, cargoID, servicioID, passwd)
VALUES (@usuarioId1, @fechaAltaDir, TRUE, @cuilDir, 'Bonifacio', 'Aguirre', 'MASCULINO', 'bonifacio.a@hmu.com.ar', 3512223344, @domicilioId1, @cargoDirectivoId, @servDireccionId, @initPass);
INSERT INTO Usuario_Rol (usuario_id, rol_id) VALUES (@usuarioId1, @rolDireccion);


-- Crear usuario con rol: JEFATURADESERVICIO
SET @cuilJefe = 20112223332;
SET @usuarioId2 = UUID_TO_BIN(UUID());
SET @fechaAltaJefe = '2012-07-01';
SELECT id INTO @rolJefatura FROM Rol WHERE nombre = 'JefaturaDeServicio';
SELECT id INTO @domicilioId2 FROM Domicilio ORDER BY RAND() LIMIT 1;
SELECT id INTO @cargoJefeId FROM Cargo WHERE numero = 300;
SELECT id INTO @servDireccionId FROM Servicio WHERE nombre = 'Dirección';

INSERT INTO Usuario (id, fechaAlta, estado, cuil, apellidos, nombres, sexo, mail, tel, domicilioID, cargoID, servicioID, passwd)
VALUES (@usuarioId2, @fechaAltaJefe, TRUE, @cuilJefe, 'Valentino', 'Marquez', 'MASCULINO', 'vale.mar@hmu.com.ar', 3512223355, @domicilioId2, @cargoJefeId, @servDireccionId, @initPass);
INSERT INTO Usuario_Rol (usuario_id, rol_id) VALUES (@usuarioId2, @rolJefatura);


-- Crear usuario con rol: OFICINADEPERSONAL
SET @cuilOdeP = 27112223333;
SET @usuarioId3 = UUID_TO_BIN(UUID());
SET @fechaAltaOdeP = '2010-12-15';
SELECT id INTO @rolOficinaPersonal FROM Rol WHERE nombre = 'OficinaDePersonal';
SELECT id INTO @domicilioId3 FROM Domicilio ORDER BY RAND() LIMIT 1;
SELECT id INTO @cargoOdePId FROM Cargo WHERE numero = 500;
SELECT id INTO @servPersonalId FROM Servicio WHERE nombre = 'Personal';

INSERT INTO Usuario (id, fechaAlta, estado, cuil, apellidos, nombres, sexo, mail, tel, domicilioID, cargoID, servicioID, passwd)
VALUES (@usuarioId3, @fechaAltaOdeP, TRUE, @cuilOdeP, 'Felicitas', 'Herrera', 'FEMENINO', 'feliherrra@hmu.com.ar', 3512223366, @domicilioId3, @cargoOdePId, @servPersonalId, @initPass);
INSERT INTO Usuario_Rol (usuario_id, rol_id) VALUES (@usuarioId3, @rolOficinaPersonal);


-- Crear usuario con rol: EMPLEADO
SET @cuilEmpleado = 20112223334;
SET @usuarioId4 = UUID_TO_BIN(UUID());
SET @fechaAltaEmpleado = '2021-09-01';
SELECT id INTO @rolEmpleado FROM Rol WHERE nombre = 'Empleado';
SELECT id INTO @domicilioId4 FROM Domicilio ORDER BY RAND() LIMIT 1;
SELECT id INTO @cargoEmpleado FROM Cargo WHERE numero = 400;
SELECT id INTO @servInformaticaId FROM Servicio WHERE nombre = 'Informática';

INSERT INTO Usuario (id, fechaAlta, estado, cuil, apellidos, nombres, sexo, mail, tel, domicilioID, cargoID, servicioID, passwd)
VALUES (@usuarioId4, @fechaAltaEmpleado, TRUE, @cuilEmpleado, 'Luis', 'Masson', 'MASCULINO', 'lmasson@hmu.com.ar', 3512223377, @domicilioId4, @cargoEmpleado, @servInformaticaId, @initPass);
INSERT INTO Empleado (id, francosCompensatoriosUtilizados) VALUES (@usuarioId4, 0);
INSERT INTO Usuario_Rol (usuario_id, rol_id) VALUES (@usuarioId4, @rolEmpleado);


-- Listar todas las asignaciones de roles
SELECT 'Aromito1' AS DefaultPass,
       u.cuil,
       CONCAT(u.apellidos, ', ', u.nombres) AS usuario_fullname,
       BIN_TO_UUID(u.id) AS usuario_id,
       BIN_TO_UUID(r.id) AS rol_id,
       r.nombre AS rol_name
FROM Usuario u
JOIN Usuario_Rol ur ON u.id = ur.usuario_id
JOIN Rol r ON ur.rol_id = r.id
ORDER BY apellidos;


-- Verificar la jefa de Oficina de Personal
SELECT cuil,
       CONCAT(apellidos, ', ', nombres) AS Jefatura_OficinaDePersonal,
       passwd
FROM Usuario
WHERE cuil = 27284644443;
