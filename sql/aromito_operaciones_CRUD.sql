USE aromito;

-- Limpiar registros de las tablas

-- Deshabilitar las Restricciones de Claves Foráneas:
SET FOREIGN_KEY_CHECKS = 0;

-- Eliminar datos de las tablas
DELETE FROM RegistroJornadaLaboral;
DELETE FROM MarcacionAgente;
DELETE FROM ParteDiario_Agente;
DELETE FROM ParteDiario;
DELETE FROM HoraExtra;
DELETE FROM FrancoCompensatorio;
DELETE FROM Memorandum_Autorizacion;
DELETE FROM Memorandum_Firmante;
DELETE FROM Memorandum_Destinatario;
DELETE FROM Memorandum;
DELETE FROM Planificacion;
DELETE FROM DiagramaDeServicio;
DELETE FROM JornadaLaboral;
DELETE FROM Agente_Novedad;
DELETE FROM Novedad;
DELETE FROM EstadoTramite;
DELETE FROM Autorizacion;
DELETE FROM Usuario_Rol;
DELETE FROM Rol;
DELETE FROM Agente;
DELETE FROM OficinaDePersonal;
DELETE FROM Servicio_JefeDeServicio;
DELETE FROM JefeDeServicio;
DELETE FROM Servicio;
DELETE FROM Direccion;
DELETE FROM Usuario;
DELETE FROM Domicilio;
DELETE FROM Cargo;
DELETE FROM HorarioAbierto;
DELETE FROM HorarioJefeServicioGuardiaPasiva;
DELETE FROM HorarioGuardiaEnfermeria;
DELETE FROM HorarioGuardiaMedica;
DELETE FROM HorarioDXI;
DELETE FROM HorarioFeriante;
DELETE FROM HorarioNocturno;
DELETE FROM HorarioSemanal;
DELETE FROM HorarioEstandar;
DELETE FROM HorarioConFranquicia;
DELETE FROM Horario;
DELETE FROM HorarioBase;

-- Habilitar restricciones de claves foráneas
SET FOREIGN_KEY_CHECKS = 1;

-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --

-- Insertar domicilios para poder asignárselos a usuarios
INSERT INTO Domicilio (id, calle, numeracion, barrio, ciudad, localidad, provincia)
VALUES
    (UUID_TO_BIN(UUID()), 'Defensa', 1200, NULL, 'Córdoba', NULL, 'Córdoba'),
    (UUID_TO_BIN(UUID()), 'Lavalleja', 3050, NULL, 'Córdoba', NULL, 'Córdoba'),
    (UUID_TO_BIN(UUID()), 'Av Belgrano Sur', 134, NULL, 'La Rioja', NULL, 'La Rioja'),
    (UUID_TO_BIN(UUID()), 'Av. Marcelo T. de Alvear', 120, NULL, 'Córdoba', NULL, 'Córdoba'),
    (UUID_TO_BIN(UUID()), 'Av. Libertador', 1450, NULL, 'Alta Gracia', NULL, 'Córdoba'),
    (UUID_TO_BIN(UUID()), 'Catamarca', 441, NULL, 'Córdoba', NULL, 'Córdoba'),
    (UUID_TO_BIN(UUID()), 'Junín', FLOOR(RAND() * 1000 + 1), NULL, 'Rosario', NULL, 'Santa Fe'),
    (UUID_TO_BIN(UUID()), 'Mate de Luna', FLOOR(RAND() * 1000 + 1), NULL, 'Famaillá', NULL, 'Tucumán'),
    (UUID_TO_BIN(UUID()), 'Gobernación', FLOOR(RAND() * 1000 + 1), NULL, 'Santa Fe', NULL, 'Santa Fe'),
    (UUID_TO_BIN(UUID()), 'Güemes', FLOOR(RAND() * 1000 + 1), NULL, 'Iruya', NULL, 'Jujuy'),
    (UUID_TO_BIN(UUID()), '9 de julio', FLOOR(RAND() * 1000 + 1), NULL, 'Corrientes', NULL, 'Corrientes'),
    (UUID_TO_BIN(UUID()), 'Av. del Trabajo', FLOOR(RAND() * 1000 + 1), NULL, 'Rosario', NULL, 'Santa Fe'),
    (UUID_TO_BIN(UUID()), 'Calle del Río', FLOOR(RAND() * 1000 + 1), NULL, 'Paraná', NULL, 'Entre Ríos'),
    (UUID_TO_BIN(UUID()), 'Av. Central', FLOOR(RAND() * 1000 + 1), NULL, 'Salta', NULL, 'Salta'),
    (UUID_TO_BIN(UUID()), 'Av. Siempre Viva', FLOOR(RAND() * 1000 + 1), NULL, 'Rawson', NULL, 'Chubut'),
    (UUID_TO_BIN(UUID()), 'San Martín', FLOOR(RAND() * 1000 + 1), NULL, 'Bariloche', NULL, 'Río Negro');

-- Obtener el ID del domicilio del HMU (Catamarca 441)
SELECT id INTO @HMUdomID
FROM Domicilio
WHERE calle = 'Catamarca' AND numeracion = 441 AND ciudad = 'Córdoba' AND provincia = 'Córdoba'
LIMIT 1;
-- Opcional: verificar que se obtuvo el ID correctamente
SELECT BIN_TO_UUID(@HMUdomID) AS domicilioHMU;

-- Obtener el ID del domicilio del HIM (Lavalleja 3050)
SELECT id INTO @HIMdomID
FROM Domicilio
WHERE calle = 'Lavalleja' AND numeracion = 3050 AND ciudad = 'Córdoba' AND provincia = 'Córdoba'
LIMIT 1;
-- Opcional: verificar que se obtuvo el ID correctamente
SELECT BIN_TO_UUID(@HIMdomID) AS domicilioHIM;

-- Generar UUIDs y almacenarlos en variables, para luego usar con Servicios
SET @AdministracionID = UUID_TO_BIN(UUID());
SET @AdmisionID = UUID_TO_BIN(UUID());
SET @AnatomiaPatologicaID = UUID_TO_BIN(UUID());
SET @AnestesiaID = UUID_TO_BIN(UUID());
SET @AuditoriaMedicaID = UUID_TO_BIN(UUID());
SET @BiomedicaID = UUID_TO_BIN(UUID());
SET @BioquimicaID = UUID_TO_BIN(UUID());
SET @CamillerosID = UUID_TO_BIN(UUID());
SET @CapacitacionDocenciaID = UUID_TO_BIN(UUID());
SET @CirugiaID = UUID_TO_BIN(UUID());
SET @CirugiaPlasticaID = UUID_TO_BIN(UUID());
SET @ClinicaMedicaID = UUID_TO_BIN(UUID());
SET @DiagPorImagenesID = UUID_TO_BIN(UUID());
SET @DireccionID = UUID_TO_BIN(UUID());
SET @EnfermeriaID = UUID_TO_BIN(UUID());
SET @EsterilizacionID = UUID_TO_BIN(UUID());
SET @FacturacionID = UUID_TO_BIN(UUID());
SET @FarmaciaID = UUID_TO_BIN(UUID());
SET @HabilitacionID = UUID_TO_BIN(UUID());
SET @HemoterapiaID = UUID_TO_BIN(UUID());
SET @InformaticaID = UUID_TO_BIN(UUID());
SET @InstrumentacionID = UUID_TO_BIN(UUID());
SET @KinesiologiaID = UUID_TO_BIN(UUID());
SET @LaboratorioID = UUID_TO_BIN(UUID());
SET @LavaderoID = UUID_TO_BIN(UUID());
SET @MantenimientoID = UUID_TO_BIN(UUID());
SET @MedicinaLegalID = UUID_TO_BIN(UUID());
SET @NeurocirugiaID = UUID_TO_BIN(UUID());
SET @NutricionID = UUID_TO_BIN(UUID());
SET @PersonalID = UUID_TO_BIN(UUID());
SET @QuirofanoID = UUID_TO_BIN(UUID());
SET @RegMedicoID = UUID_TO_BIN(UUID());
SET @SaludMentalID = UUID_TO_BIN(UUID());
SET @SecTecnicaID = UUID_TO_BIN(UUID());
SET @ServicioSocialID = UUID_TO_BIN(UUID());
SET @TecnoelectricaID = UUID_TO_BIN(UUID());
SET @ToxicologiaID = UUID_TO_BIN(UUID());
SET @TraumatologiaID = UUID_TO_BIN(UUID());

-- Generar UUIDs y almacenarlos en variables, para luego usar con JefeDeServicios
SET @JefeAdmisionID = UUID_TO_BIN(UUID());
SET @JefeBiomedicaID = UUID_TO_BIN(UUID());
SET @JefeCirugiaID = UUID_TO_BIN(UUID());
SET @JefeDiagPorImagenesID = UUID_TO_BIN(UUID());
SET @JefeEnfermeriaID = UUID_TO_BIN(UUID());
SET @JefeEsterilizacionID = UUID_TO_BIN(UUID());
SET @JefeFarmaciaID = UUID_TO_BIN(UUID());
SET @JefeHabilitacionID = UUID_TO_BIN(UUID());
SET @JefeInformaticaID = UUID_TO_BIN(UUID());
SET @JefeNutricionID = UUID_TO_BIN(UUID());
SET @JefePersonalID = UUID_TO_BIN(UUID());
SET @JefeToxicologiaID = UUID_TO_BIN(UUID());
SET @JefeTraumatologiaID = UUID_TO_BIN(UUID());


-- Generar e inicializar cargos
SET @cargoUndefinedID = UUID_TO_BIN(UUID());
SET @cargoDirectivoInicialID = UUID_TO_BIN(UUID());
SET @cargoJefaturaInicialID = UUID_TO_BIN(UUID());
SET @cargoTecnicoInicialID = UUID_TO_BIN(UUID());
SET @cargoAdministrativoInicialID = UUID_TO_BIN(UUID());
SET @cargoServicioInicialID = UUID_TO_BIN(UUID());
SET @cargoEnfermeriaInicialID = UUID_TO_BIN(UUID());
SET @cargoMedicoInicialID = UUID_TO_BIN(UUID());
SET @cargoEnfermeriaProInicialID = UUID_TO_BIN(UUID());
SET @cargoProfesionalInicialID = UUID_TO_BIN(UUID());


INSERT INTO Cargo (id, numero, descripcion, agrupacion) VALUES
(@cargoUndefinedID, 0, 'Cargo sin definir', 'INDEFINIDO'),
(@cargoDirectivoInicialID, 200, 'Directivos', 'PLANTAPOLITICA'),
(@cargoJefaturaInicialID, 300, 'Jefaturas de Servicio', 'JEFATURA'),
(@cargoTecnicoInicialID, 400, 'Técnicos', 'TECNICO'),
(@cargoAdministrativoInicialID, 500, 'Administrativos', 'ADMINISTRATIVO'),
(@cargoServicioInicialID, 600, 'Servicios', 'SERVICIO'),
(@cargoEnfermeriaInicialID, 700, 'Enfermería Técnicos', 'ENFERMERIA'),
(@cargoMedicoInicialID, 800, 'Médicos', 'MEDICO'),
(@cargoEnfermeriaProInicialID, 900, 'Enfermería Profesionales', 'ENFERMERIA'),
(@cargoProfesionalInicialID, 1200, 'Profesionales sin agrupación específica', 'PROFESIONAL');


-- Generar una contraseña inicial usando Bcrypt
/* Por defecto, utlizaremos la contraseña Aromito1
 * En GNU/Linux, la herramienta 'htpasswd' (parte de Apache HTTP Server tools), genera un hash de bcrypt con:
 *
 *   htpasswd -bnBC 10 "" "miContraseñaSegura123" | tr -d ':\n'
 * 
 * De forma online, Bcrypt-Generator.com (en https://bcrypt-generator.com/ ), puede generar contraseñas.
 * String: Aromito1
 * Rounds: 12
 *
 * Además, a dos usuarios les estableceremos su contraseña por defecto (el nro de CUIL). A saber:
 * UserA: CUIL / Pass: 27284644443 (Florencia Maurino)
 * UserB: CUIL / Pass: 24554978443 (Sebastian Bustos)
 * El objetivo de esto, es probar las funcionalidades de detección de contraseña por defecto en el software.
 */
SET @initPass = '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO';
SET @userAPass = '$2a$12$TCAy.mqVsaRIwjQs0imr/uD0xNoUq/W3LSVys3EISI5hdBREwZW5a';
SET @userBPass = '$2a$12$x9SDga8sk3DyjvdeIwkhl.2e9wcWHewfHEFQFKkFp1.FiAKcyRZUG';


SET FOREIGN_KEY_CHECKS = 0; -- Deshabilitar restricciones de clave primaria (tenemos generados los UUID que usaremos para los servicios, pero no los servicios)
-- Insertar en Usuario
INSERT INTO Usuario (id, nombres, apellidos, mail, cuil, fechaAlta, sexo, estado, domicilioID, servicioID, tipoUsuario, passwd)
VALUES
    -- Dirección
    (UUID_TO_BIN(UUID()), 'Mariano Gustavo', 'Marino', 'direccion@hmu.com.ar', 20224448885, CURDATE(), 'MASCULINO', TRUE, @HMUdomID, @DireccionID, 'Direccion', @initPass),
    (UUID_TO_BIN(UUID()), 'Federico', 'Huergo Sánchez', 'fedesubdir@hmu.com.ar', 20259993331, CURDATE(), 'MASCULINO', TRUE, @HIMdomID, @DireccionID, 'Direccion', @initPass),
    (UUID_TO_BIN(UUID()), 'Gloria', 'Longoni', 'subdirectora@hmu.com.ar', 27129997773, CURDATE(), 'FEMENINO', TRUE, (SELECT id FROM Domicilio WHERE ciudad = 'Alta Gracia' LIMIT 1), @DireccionID, 'Direccion', @initPass),
    (UUID_TO_BIN(UUID()), 'Fabricio', 'Vitali', 'subdireccion@hmu.com.ar', 20239997772, CURDATE(), 'MASCULINO', TRUE, @HMUdomID, @DireccionID, 'Direccion', @initPass),

    -- Jefaturas de Servicio
    (@JefeAdmisionID, 'Andrea', 'Balconte', 'andrea@hmu.com.ar', 27295554447, CURDATE(), 'FEMENINO', TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), @AdmisionID, 'JefeDeServicio', @initPass),
    (@JefeBiomedicaID, 'Silvina', 'Maestro', 'smaestro@hmu.com.ar', 27274422442, CURDATE(), 'FEMENINO', TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), @BiomedicaID, 'JefeDeServicio', @initPass),
    (@JefeCirugiaID, 'Maximiliano', 'Titarelli', 'drtita@hmu.com.ar', 20268944448, CURDATE(), 'MASCULINO', TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), @CirugiaID, 'JefeDeServicio', @initPass),
    (@JefeDiagPorImagenesID, 'Juan Ignacio', 'Morales', 'jimorales@hmu.com.ar', 20281324547, CURDATE(), 'MASCULINO', TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), @DiagPorImagenesID, 'JefeDeServicio', @initPass),
    (@JefeEnfermeriaID, 'Tania', 'Plaza', 'tplaza@hmu.com.ar', 27224444445, CURDATE(), 'FEMENINO', TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), @EnfermeriaID, 'JefeDeServicio', @initPass),
    (@JefeEsterilizacionID, 'Matías', 'Pérez Cabral', 'mcp@hmu.com.ar', 20289445441, CURDATE(), 'MASCULINO', TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), @EsterilizacionID, 'JefeDeServicio', @initPass),
    (@JefeFarmaciaID, 'María Pía', 'Arancibia', 'piaarancibia@hmu.com.ar', 27254344447, CURDATE(), 'FEMENINO', TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), @FarmaciaID, 'JefeDeServicio', @initPass),
    (@JefeHabilitacionID, 'Carlos Fernando', 'Roberts', 'ferroberts@hmu.com.ar', 20276444446, CURDATE(), 'MASCULINO', TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), @HabilitacionID, 'JefeDeServicio', @initPass),
    (@JefeInformaticaID, 'Juan Manuel', 'Roqué', 'jmroque@hmu.com.ar', 20220361118, CURDATE(), 'OTRO', TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), @InformaticaID, 'JefeDeServicio', @initPass),
    (@JefeNutricionID, 'Alejandra', 'Boqué', 'aleboque@hmu.com.ar', 27174444446, CURDATE(), 'FEMENINO', TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), @NutricionID, 'JefeDeServicio', @initPass),
    (@JefeTraumatologiaID, 'Omar Wenceslao', 'Sánchez', 'owsanchez@hmu.com.ar', 20124644445, CURDATE(), 'MASCULINO', TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), @TraumatologiaID, 'JefeDeServicio', @initPass),
    (@JefeToxicologiaID, 'Andrea', 'Vilkelis', 'avilkelis@hmu.com.ar', 27214434447, CURDATE(), 'FEMENINO', TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), @ToxicologiaID, 'JefeDeServicio', @initPass),

    -- Oficina de Personal
    (@JefePersonalID, 'Florencia', 'Maurino', 'flormaurino@hmu.com.ar', 27284644443, CURDATE(), 'FEMENINO', TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), @PersonalID, 'OficinaDePersonal', @userAPass),

    -- Agentes
    (UUID_TO_BIN(UUID()), 'Juan', 'Aniceto', 'janiceto@hmu.com.ar', 20124543421, CURDATE(), 'MASCULINO', TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), @DiagPorImagenesID, 'Agente', @initPass),
    (UUID_TO_BIN(UUID()), 'Baltazar', 'Garzón', 'baltig@hmu.com.ar', 20554644448, CURDATE(), 'MASCULINO', TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), @PersonalID, 'Agente', @initPass),
    (UUID_TO_BIN(UUID()), 'Pedro', 'Taborda', 'pltaborda@hmu.com.ar', 20154786445, CURDATE(), 'MASCULINO', TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), @PersonalID, 'Agente', @initPass),
    (UUID_TO_BIN(UUID()), 'María Laura', 'Vargas Ruíz', 'mlvargas@hmu.com.ar', 27554664563, CURDATE(), 'FEMENINO', TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), @EsterilizacionID, 'Agente', @initPass),
    (UUID_TO_BIN(UUID()), 'María Celeste', 'Vignetta', 'mcv@hmu.com.ar', 27554685443, CURDATE(), 'FEMENINO', TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), @CirugiaID, 'Agente', @initPass),
    (UUID_TO_BIN(UUID()), 'Alicia', 'Vivas', 'avivas@hmu.com.ar', 27554567443, CURDATE(), 'FEMENINO', TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), @HabilitacionID, 'Agente', @initPass),
    (UUID_TO_BIN(UUID()), 'Matías Quique', 'Canga Castellanos', 'lestat@hmu.com.ar', 20554649743, CURDATE(), 'MASCULINO', TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), @InformaticaID, 'Agente', @initPass),
    (UUID_TO_BIN(UUID()), 'Mauricio Elio', 'Garay', 'mgaray@hmu.com.ar', 24574741745, CURDATE(), 'OTRO', TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), @InformaticaID, 'Agente', @initPass),
    (UUID_TO_BIN(UUID()), 'Eva Patricia', 'Usandivares', 'epu@hmu.com.ar', 27556786453, CURDATE(), 'FEMENINO', TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), @PersonalID, 'Agente', @initPass),
    (UUID_TO_BIN(UUID()), 'Edith', 'Tolay', 'etolay@hmu.com.ar', 20454676443, CURDATE(), 'FEMENINO', TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), @EnfermeriaID, 'Agente', @initPass),
    (UUID_TO_BIN(UUID()), 'José Luis', 'Terrieris', 'jterrieris@hmu.com.ar', 20554543453, CURDATE(), 'MASCULINO', TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), @EnfermeriaID, 'Agente', @initPass),
    (UUID_TO_BIN(UUID()), 'Claudia', 'Tarifa', 'clautarifa@hmu.com.ar', 27540678443, CURDATE(), 'FEMENINO', TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), @EnfermeriaID, 'Agente', @initPass),
    (UUID_TO_BIN(UUID()), 'Erica', 'Tapia', 'erikatapia@hmu.com.ar', 27554560863, CURDATE(), 'FEMENINO', TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), @ToxicologiaID, 'Agente', @initPass),
    (UUID_TO_BIN(UUID()), 'Demetrio', 'Tampares', 'demetam@hmu.com.ar', 24784044443, CURDATE(), 'OTRO', TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), @ToxicologiaID, 'Agente', @initPass),
    (UUID_TO_BIN(UUID()), 'Alejandro', 'Suizer', 'amsuizer@hmu.com.ar', 20526456443, CURDATE(), 'MASCULINO', TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), @CirugiaID, 'Agente', @initPass),
    (UUID_TO_BIN(UUID()), 'David', 'Suárez', 'dsuarez@hmu.com.ar', 20554897343, CURDATE(), 'MASCULINO', TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), @BiomedicaID, 'Agente', @initPass),
    (UUID_TO_BIN(UUID()), 'Soledad', 'Romero', 'soleromero@hmu.com.ar', 27864567893, CURDATE(), 'FEMENINO', TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), @AdmisionID, 'Agente', @initPass),
    (UUID_TO_BIN(UUID()), 'Pedro', 'Rius', 'pedrorius@hmu.com.ar', 20554674843, CURDATE(), 'MASCULINO', TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), @DiagPorImagenesID, 'Agente', @initPass),
    (UUID_TO_BIN(UUID()), 'Damian', 'Rabbat', 'drabbat@hmu.com.ar', 20558244443, CURDATE(), 'MASCULINO', TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), @TraumatologiaID, 'Agente', @initPass),
    (UUID_TO_BIN(UUID()), 'Ismael', 'Puig', 'ipuig@hmu.com.ar', 20567974443, CURDATE(), 'MASCULINO', TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), @TraumatologiaID, 'Agente', @initPass),
    (UUID_TO_BIN(UUID()), 'Diego', 'Pascolo', 'dpascolo@hmu.com.ar', 20456645843, CURDATE(), 'MASCULINO', TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), @CirugiaID, 'Agente', @initPass),
    (UUID_TO_BIN(UUID()), 'Sebastián', 'Bustos', 'sebustos@hmu.com.ar', 24554978443, CURDATE(), 'OTRO', TRUE, (SELECT id FROM Domicilio WHERE calle = 'Av. Siempre Viva' LIMIT 1), @BiomedicaID, 'Agente', @userBPass);
SET FOREIGN_KEY_CHECKS = 1; -- Habilitar restricciones de claves foráneas


-- Insertar en la tabla 'Direccion' utilizando los 'id' guardados previamente para esos usuarios
INSERT INTO Direccion (id)
SELECT id FROM Usuario WHERE tipoUsuario = 'Direccion';

-- Obtener el ID del actual director (cuyo apellido es 'Marino')
SELECT D.id INTO @dirID
FROM Direccion D
JOIN Usuario U ON D.id = U.id
WHERE U.apellidos = 'Marino' AND U.tipoUsuario = 'Direccion'
LIMIT 1;
-- Opcionalmente, verificar que se obtuvo el ID correctamente
SELECT BIN_TO_UUID(@dirID) AS dirID;

-- Obtener el ID del actual subdirector (cuyo apellido es 'Vitali')
SELECT D.id INTO @subdirID
FROM Direccion D
JOIN Usuario U ON D.id = U.id
WHERE U.apellidos = 'Vitali' AND U.tipoUsuario = 'Direccion'
LIMIT 1;
-- Opcionalmente, verificar que se obtuvo el ID correctamente
SELECT BIN_TO_UUID(@subdirID) AS subdirID;


-- Insertar Servicios
INSERT INTO Servicio (id, nombre, agrupacion, direccionID)
VALUES
    (@AdministracionID, 'Administración', 'ADMINISTRATIVO', @dirID),
    (@AdmisionID, 'Admisión', 'ADMINISTRATIVO', @dirID),
    (@AnatomiaPatologicaID, 'Anatomía Patológica', 'MEDICO', @dirID),
    (@AnestesiaID, 'Anestesia', 'MEDICO', @dirID),
    (@AuditoriaMedicaID, 'Auditoría Médica', 'MEDICO', @dirID),
    (@BiomedicaID, 'Biomédica', 'TECNICO', @subdirID),
    (@BioquimicaID, 'Bioquímica', 'SERVICIO', @subdirID),
    (@CamillerosID, 'Camilleros', 'SERVICIO', @subdirID),
    (@CapacitacionDocenciaID, 'Capacitación y Docencia', 'MEDICO', @dirID),
    (@CirugiaID, 'Cirugía', 'MEDICO', @dirID),
    (@CirugiaPlasticaID, 'Cirugía Plástica', 'MEDICO', @dirID),
    (@ClinicaMedicaID, 'Clínica Médica', 'MEDICO', @dirID),
    (@DiagPorImagenesID, 'Diagnóstico por Imágenes', 'MEDICO', @dirID),
    (@DireccionID, 'Dirección', 'ADMINISTRATIVO', @dirID),
    (@EnfermeriaID, 'Enfermería', 'ENFERMERIA', @dirID),
    (@EsterilizacionID, 'Esterilización', 'SERVICIO', @subdirID),
    (@FacturacionID, 'Facturación', 'ADMINISTRATIVO', @dirID),
    (@FarmaciaID, 'Farmacia', 'SERVICIO', @subdirID),
    (@HabilitacionID, 'Habilitación', 'ADMINISTRATIVO', @dirID),
    (@HemoterapiaID, 'Hemoterapia', 'SERVICIO', @subdirID),
    (@InformaticaID, 'Informática', 'TECNICO', @subdirID),
    (@InstrumentacionID, 'Instrumentación Quirúrgica', 'MEDICO', @dirID),
    (@KinesiologiaID, 'Kinesiología', 'MEDICO', @dirID),
    (@LaboratorioID, 'Laboratorio', 'SERVICIO', @subdirID),
    (@LavaderoID, 'Lavadero', 'SERVICIO', @subdirID),
    (@MantenimientoID, 'Mantenimiento', 'TECNICO', @subdirID),
    (@MedicinaLegalID, 'Medicina Legal', 'ADMINISTRATIVO', @dirID),
    (@NeurocirugiaID, 'Neurocirugía', 'MEDICO', @dirID),
    (@NutricionID, 'Nutrición', 'MEDICO', @dirID),
    (@PersonalID, 'Personal', 'ADMINISTRATIVO', @dirID),
    (@QuirofanoID, 'Quirófano', 'MEDICO', @dirID),
    (@RegMedicoID, 'Registro Médico (Archivo)', 'ADMINISTRATIVO', @dirID),
    (@SaludMentalID, 'Salud Mental', 'MEDICO', @dirID),
    (@SecTecnicaID, 'Secretaría Técnica', 'ADMINISTRATIVO', @dirID),
    (@ServicioSocialID, 'Servicio Social', 'ADMINISTRATIVO', @dirID),
    (@TecnoelectricaID, 'Tecnoeléctrica', 'TECNICO', @subdirID),
    (@TraumatologiaID, 'Traumatología y Ortopedia', 'MEDICO', @dirID);

-- Insertar en OficinaDePersonal
INSERT INTO OficinaDePersonal (id, reportesGenerados)
SELECT id, 0 FROM Usuario WHERE tipoUsuario = 'OficinaDePersonal';

-- Insertar en JefeDeServicio, todos los usuarios de tipo JefeDeServicio
INSERT INTO JefeDeServicio (id)
SELECT id FROM Usuario WHERE tipoUsuario = 'JefeDeServicio';

SET FOREIGN_KEY_CHECKS = 0; -- Deshabilitar restricciones de claves foráneas
-- Vincular Jefes de Servicio con Servicios, mediante la tabla Servicio_JefeDeservicio
INSERT INTO servicio_jefedeservicio (servicioID, jefedeservicioID)
VALUES
	(@AdmisionID, @JefeAdmisionID),
	(@BiomedicaID, @JefeBiomedicaID),
	(@CirugiaID, @JefeCirugiaID),
	(@DiagPorImagenesID, @JefeDiagPorImagenesID),
	(@EnfermeriaID, @JefeEnfermeriaID),
	(@EsterilizacionID, @JefeEsterilizacionID),
	(@FarmaciaID, @JefeFarmaciaID),
	(@HabilitacionID, @JefeHabilitacionID),
	(@InformaticaID, @JefeInformaticaID),
	(@NutricionID, @JefeNutricionID),
	(@PersonalID, @JefePersonalID),
	(@ToxicologiaID, @JefeToxicologiaID),
	(@TraumatologiaID, @JefeTraumatologiaID);
SET FOREIGN_KEY_CHECKS = 1; -- Habilitar restricciones de claves foráneas

-- Listar todas las asignaciones de jefes de servicio
SELECT BIN_TO_UUID(s.id) AS servicio_ID, s.nombre AS servicio_Nombre, BIN_TO_UUID(jds.id) AS jefe_ID, CONCAT(u.apellidos, ", ", u.nombres) AS jefe_de_servicio
FROM Servicio s
JOIN Servicio_JefeDeServicio sjds ON s.id = sjds.servicioID
JOIN JefeDeServicio jds ON sjds.jefedeservicioID = jds.id
JOIN Usuario u ON jds.id = u.id;


-- Insertar Agentes en la tabla Agente
/* Al insertar en Agente, solo necesitamos especificar
 * id, francosCompensatoriosUtilizados, y horarioActualID
 */
INSERT INTO Agente (id, francosCompensatoriosUtilizados, horarioActualID)
SELECT u.id, 0, NULL
FROM Usuario u
WHERE u.tipoUsuario = 'Agente';


-- Listar usuarios Agentes
SELECT
    BIN_TO_UUID(U.id) AS agente_id,
    U.apellidos,
    U.nombres,
    U.cuil,
    U.mail,
    U.tel,
    S.nombre AS area_de_servicio
FROM Usuario U
JOIN Agente E ON U.id = E.id
JOIN Servicio S ON U.servicioID = S.id
WHERE U.tipoUsuario = 'Agente';


-- Listar usuarios de tipo Dirección cargados
SELECT
    BIN_TO_UUID(id) AS usuario_id,
    nombres,
    apellidos,
    mail,
    cuil,
    fechaAlta,
    sexo,
    estado,
    tipoUsuario
FROM Usuario
WHERE tipoUsuario = 'Direccion';


-- Visualizar Servicios a cargo del Director
SELECT
    BIN_TO_UUID(S.id) AS servicio_id,
    S.nombre AS servicio,
    S.agrupacion,
    BIN_TO_UUID(S.direccionID) AS direccion_id,
    U.nombres AS director_nombres,
    U.apellidos AS director_apellidos
FROM Servicio S
JOIN Direccion D ON S.direccionID = D.id
JOIN Usuario U ON D.id = U.id
WHERE U.apellidos = 'Marino' AND U.tipoUsuario = 'Direccion';


-- Visualizar Servicios a cargo del Subdirector
SELECT
    BIN_TO_UUID(S.id) AS servicio_id,
    S.nombre AS servicio,
    S.agrupacion,
    BIN_TO_UUID(S.direccionID) AS direccion_id,
    U.nombres AS subdirector_nombres,
    U.apellidos AS subdirector_apellidos
FROM Servicio S
JOIN Direccion D ON S.direccionID = D.id
JOIN Usuario U ON D.id = U.id
WHERE U.apellidos = 'Vitali' AND U.tipoUsuario = 'Direccion';


-- Listar asignaciones de Jefes a Servicios y Director superior
SELECT 
    BIN_TO_UUID(s.id) AS ServicioID,
    s.nombre AS ServNombre,
    s.agrupacion AS ServAgrupacion,
    BIN_TO_UUID(jds.id) AS JefaturaID,
    CONCAT(jefe.apellidos, ', ', jefe.nombres) AS JefeFullName,
    BIN_TO_UUID(dir.id) AS DirectorID,
    CONCAT(director.apellidos, ', ', director.nombres) AS DirFullName
FROM JefeDeServicio jds
JOIN Usuario jefe ON jds.id = jefe.id
JOIN Servicio s ON jefe.servicioID = s.id
JOIN Direccion dir ON s.direccionID = dir.id
JOIN Usuario director ON dir.id = director.id;


-- Contar la cantidad de agentes de cada servicio (incluidos sus jefes)
SELECT
    S.nombre AS Servicio,
    COUNT(U.id) AS Cant_agentes
FROM Servicio S
LEFT JOIN Usuario U ON U.servicioID = S.id
WHERE U.tipoUsuario IN ('Agente', 'JefeDeServicio')
GROUP BY S.id, S.nombre
ORDER BY Cant_agentes DESC;


-- Generar Roles y almacenarlos en variables, para luego asignar a usuarios
SET @RolAgente = UUID_TO_BIN(UUID());
SET @RolJefeDeServicio = UUID_TO_BIN(UUID());
SET @RolOficinaDePersonal = UUID_TO_BIN(UUID());
SET @RolDireccion = UUID_TO_BIN(UUID());

-- Inicializar tabla de Roles
INSERT INTO Rol (id, nombre, descripcion) VALUES
(@RolAgente, 'Agente', 'Agente'),
(@RolJefeDeServicio, 'JefeDeServicio', 'Jefe de Servicio'),
(@RolOficinaDePersonal, 'OficinaDePersonal', 'Oficina de Personal'),
(@RolDireccion, 'Direccion', 'Directivo');

-- Listar roles de usuario
SELECT BIN_TO_UUID(id) AS id, nombre, descripcion FROM Rol;

-- Asignar roles al usuario cuyo apellido es 'Maurino'
INSERT INTO Usuario_Rol (usuario_id, rol_id)
SELECT u.id, r.id
FROM Usuario u
JOIN Rol r ON r.nombre IN ('Agente', 'JefeDeServicio', 'OficinaDePersonal')
WHERE u.apellidos = 'Maurino';

-- Asignar roles al usuario cuyo apellido es 'Roqué'
INSERT INTO Usuario_Rol (usuario_id, rol_id)
SELECT u.id, r.id
FROM Usuario u
JOIN Rol r ON r.nombre IN ('Agente', 'JefeDeServicio')
WHERE u.apellidos = 'Roqué';

-- Asignar roles al usuario cuyo apellido es 'Marino'
INSERT INTO Usuario_Rol (usuario_id, rol_id)
SELECT u.id, r.id
FROM Usuario u
JOIN Rol r ON r.nombre IN ('JefeDeServicio', 'Direccion')
WHERE u.apellidos = 'Marino';

-- Asignar roles al usuario cuyos apellidos son 'Canga' o 'Hamann'
INSERT INTO Usuario_Rol (usuario_id, rol_id)
SELECT u.id, r.id
FROM Usuario u
JOIN Rol r ON r.nombre = 'Agente'
WHERE u.apellidos IN ('Canga', 'Hamann');

-- Listar todas las asignaciones de roles
SELECT BIN_TO_UUID(u.id) AS usuario_id, u.apellidos, BIN_TO_UUID(r.id) AS rol_id, r.nombre
FROM Usuario u
JOIN Usuario_Rol ur ON u.id = ur.usuario_id
JOIN Rol r ON ur.rol_id = r.id
ORDER BY apellidos;


-- Agregar/modificar el teléfono a un usuario cualquiera
-- Por ejemeplo, elijiremos modificarle el teléfono al jefe de Informática (que ya teníamos almacenado en @JefeInformaticaID)
-- Actualizamos el teléfono del usuario
UPDATE Usuario
SET tel = '3517553799'
WHERE id = @JefeInformaticaID;
-- Verificamos nuevo valor:
SELECT nombres, apellidos, tel FROM Usuario WHERE id = @JefeInformaticaID;


-- Inicializar algunos cargos...
UPDATE Usuario SET cargoID = @cargoUndefinedID;
UPDATE Usuario SET cargoID = @cargoTecnicoInicialID WHERE servicioID = @BiomedicaID;
UPDATE Usuario SET cargoID = @cargoTecnicoInicialID WHERE servicioID = @InformaticaID;
UPDATE Usuario SET cargoID = @cargoJefaturaInicialID WHERE id = @JefeInformaticaID;
UPDATE Usuario SET cargoID = @cargoJefaturaInicialID WHERE id = @JefePersonalID;


-- Borrar un usuario
-- Supongamos que el usuario a eliminar es 'Soledad Romero'
SET @usuarioIdEliminar = (SELECT id FROM Usuario WHERE apellidos = 'Romero' AND nombres = 'Soledad' LIMIT 1);

-- Verificar de qué tipo de usuario se trata
SELECT tipoUsuario
FROM Usuario
WHERE id = @usuarioIdEliminar;

-- Eliminación de tablas específicas
-- Si es Agente
DELETE FROM Agente WHERE id = @usuarioIdEliminar;

-- Si es JefeDeServicio
DELETE FROM JefeDeServicio WHERE id = @usuarioIdEliminar;

-- Si es Dirección
DELETE FROM Direccion WHERE id = @usuarioIdEliminar;

-- Si está en OficinaDePersonal
DELETE FROM OficinaDePersonal WHERE id = @usuarioIdEliminar;

-- Finalmente, eliminar de la tabla Usuario
DELETE FROM Usuario WHERE id = @usuarioIdEliminar;
-- Opcionalmente, si el usuario tuviera registros en otras tablas (como Planificacion, Novedad, etc.), también los eliminaríamos (a fines de evitar inconsistencias)
-- No es el caso

