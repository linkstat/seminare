/* ============================================================================
 * Aromito - Operaciones CRUD de inicialización y prueba
 *
 * Este script:
 *  1. Limpia los registros de todas las tablas (preservando schema).
 *  2. Inserta domicilios, cargos, servicios y usuarios de prueba reales del
 *     HMU para uso de demo y pruebas funcionales.
 *  3. Asigna roles a un subconjunto de usuarios.
 *  4. Demuestra algunos UPDATE/DELETE típicos.
 *
 * Cambios respecto a la versión previa:
 *  - Eliminada la columna Usuario.tipoUsuario; el polimorfismo va por roles.
 *  - Eliminadas las tablas Direccion, JefeDeServicio, OficinaDePersonal;
 *    sólo queda Empleado como subclase con datos propios.
 *  - Convención: todos los Usuarios reales del hospital se insertan también
 *    en Empleado, dado que todos son personal con horario y compensaciones.
 *    El "tipo" se discrimina por roles asignados en Usuario_Rol.
 *  - Nomenclatura unificada: Empleado / JefaturaDeServicio / Empleado_Novedad /
 *    Servicio_JefaturaDeServicio / ParteDiario_Empleado / MarcacionEmpleado.
 * ========================================================================== */

USE aromito;

-- ----------------------------------------------------------------------------
-- 1) Limpieza de registros
-- ----------------------------------------------------------------------------
SET FOREIGN_KEY_CHECKS = 0;

DELETE FROM RegistroJornadaLaboral;
DELETE FROM MarcacionEmpleado;
DELETE FROM ParteDiario_Empleado;
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
DELETE FROM Empleado_Novedad;
DELETE FROM Novedad;
DELETE FROM EstadoTramite;
DELETE FROM Autorizacion;
DELETE FROM Usuario_Rol;
DELETE FROM Rol;
DELETE FROM Empleado;
DELETE FROM Servicio_JefaturaDeServicio;
DELETE FROM Servicio;
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

SET FOREIGN_KEY_CHECKS = 1;


-- ----------------------------------------------------------------------------
-- 2) Domicilios de prueba
-- ----------------------------------------------------------------------------
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

-- Capturar los IDs de los domicilios reales del HMU/HIM
SELECT id INTO @HMUdomID FROM Domicilio
WHERE calle = 'Catamarca' AND numeracion = 441 AND ciudad = 'Córdoba' AND provincia = 'Córdoba' LIMIT 1;
SELECT id INTO @HIMdomID FROM Domicilio
WHERE calle = 'Lavalleja' AND numeracion = 3050 AND ciudad = 'Córdoba' AND provincia = 'Córdoba' LIMIT 1;

SELECT BIN_TO_UUID(@HMUdomID) AS domicilioHMU, BIN_TO_UUID(@HIMdomID) AS domicilioHIM;


-- ----------------------------------------------------------------------------
-- 3) UUIDs preasignados para Servicios y Usuarios "jefes"
-- ----------------------------------------------------------------------------
SET @AdministracionID         = UUID_TO_BIN(UUID());
SET @AdmisionID               = UUID_TO_BIN(UUID());
SET @AnatomiaPatologicaID     = UUID_TO_BIN(UUID());
SET @AnestesiaID              = UUID_TO_BIN(UUID());
SET @AuditoriaMedicaID        = UUID_TO_BIN(UUID());
SET @BiomedicaID              = UUID_TO_BIN(UUID());
SET @BioquimicaID             = UUID_TO_BIN(UUID());
SET @CamillerosID             = UUID_TO_BIN(UUID());
SET @CapacitacionDocenciaID   = UUID_TO_BIN(UUID());
SET @CirugiaID                = UUID_TO_BIN(UUID());
SET @CirugiaPlasticaID        = UUID_TO_BIN(UUID());
SET @ClinicaMedicaID          = UUID_TO_BIN(UUID());
SET @DiagPorImagenesID        = UUID_TO_BIN(UUID());
SET @DireccionID              = UUID_TO_BIN(UUID());
SET @EnfermeriaID             = UUID_TO_BIN(UUID());
SET @EsterilizacionID         = UUID_TO_BIN(UUID());
SET @FacturacionID            = UUID_TO_BIN(UUID());
SET @FarmaciaID               = UUID_TO_BIN(UUID());
SET @HabilitacionID           = UUID_TO_BIN(UUID());
SET @HemoterapiaID            = UUID_TO_BIN(UUID());
SET @InformaticaID            = UUID_TO_BIN(UUID());
SET @InstrumentacionID        = UUID_TO_BIN(UUID());
SET @KinesiologiaID           = UUID_TO_BIN(UUID());
SET @LaboratorioID            = UUID_TO_BIN(UUID());
SET @LavaderoID               = UUID_TO_BIN(UUID());
SET @MantenimientoID          = UUID_TO_BIN(UUID());
SET @MedicinaLegalID          = UUID_TO_BIN(UUID());
SET @NeurocirugiaID           = UUID_TO_BIN(UUID());
SET @NutricionID              = UUID_TO_BIN(UUID());
SET @PersonalID               = UUID_TO_BIN(UUID());
SET @QuirofanoID              = UUID_TO_BIN(UUID());
SET @RegMedicoID              = UUID_TO_BIN(UUID());
SET @SaludMentalID            = UUID_TO_BIN(UUID());
SET @SecTecnicaID             = UUID_TO_BIN(UUID());
SET @ServicioSocialID         = UUID_TO_BIN(UUID());
SET @TecnoelectricaID         = UUID_TO_BIN(UUID());
SET @ToxicologiaID            = UUID_TO_BIN(UUID());
SET @TraumatologiaID          = UUID_TO_BIN(UUID());

-- IDs preasignados para los Usuarios que serán Jefatura de Servicio
SET @JefeAdmisionID           = UUID_TO_BIN(UUID());
SET @JefeBiomedicaID          = UUID_TO_BIN(UUID());
SET @JefeCirugiaID            = UUID_TO_BIN(UUID());
SET @JefeDiagPorImagenesID    = UUID_TO_BIN(UUID());
SET @JefeEnfermeriaID         = UUID_TO_BIN(UUID());
SET @JefeEsterilizacionID     = UUID_TO_BIN(UUID());
SET @JefeFarmaciaID           = UUID_TO_BIN(UUID());
SET @JefeHabilitacionID       = UUID_TO_BIN(UUID());
SET @JefeInformaticaID        = UUID_TO_BIN(UUID());
SET @JefeNutricionID          = UUID_TO_BIN(UUID());
SET @JefePersonalID           = UUID_TO_BIN(UUID());
SET @JefeToxicologiaID        = UUID_TO_BIN(UUID());
SET @JefeTraumatologiaID      = UUID_TO_BIN(UUID());

-- IDs preasignados para los Usuarios que serán Dirección
SET @DirMarinoID              = UUID_TO_BIN(UUID());
SET @DirHuergoID              = UUID_TO_BIN(UUID());
SET @DirLongoniID             = UUID_TO_BIN(UUID());
SET @DirVitaliID              = UUID_TO_BIN(UUID());


-- ----------------------------------------------------------------------------
-- 4) Cargos
-- ----------------------------------------------------------------------------
SET @cargoUndefinedID            = UUID_TO_BIN(UUID());
SET @cargoDirectivoInicialID     = UUID_TO_BIN(UUID());
SET @cargoJefaturaInicialID      = UUID_TO_BIN(UUID());
SET @cargoTecnicoInicialID       = UUID_TO_BIN(UUID());
SET @cargoAdministrativoInicialID = UUID_TO_BIN(UUID());
SET @cargoServicioInicialID      = UUID_TO_BIN(UUID());
SET @cargoEnfermeriaInicialID    = UUID_TO_BIN(UUID());
SET @cargoMedicoInicialID        = UUID_TO_BIN(UUID());
SET @cargoEnfermeriaProInicialID = UUID_TO_BIN(UUID());
SET @cargoProfesionalInicialID   = UUID_TO_BIN(UUID());

INSERT INTO Cargo (id, numero, descripcion, agrupacion) VALUES
    (@cargoUndefinedID,            0,    'Cargo sin definir',                          'INDEFINIDO'),
    (@cargoDirectivoInicialID,     200,  'Directivos',                                 'PLANTAPOLITICA'),
    (@cargoJefaturaInicialID,      300,  'Jefaturas de Servicio',                      'JEFATURA'),
    (@cargoTecnicoInicialID,       400,  'Técnicos',                                   'TECNICO'),
    (@cargoAdministrativoInicialID, 500, 'Administrativos',                            'ADMINISTRATIVO'),
    (@cargoServicioInicialID,      600,  'Servicios',                                  'SERVICIO'),
    (@cargoEnfermeriaInicialID,    700,  'Enfermería Técnicos',                        'ENFERMERIA'),
    (@cargoMedicoInicialID,        800,  'Médicos',                                    'MEDICO'),
    (@cargoEnfermeriaProInicialID, 900,  'Enfermería Profesionales',                   'ENFERMERIA'),
    (@cargoProfesionalInicialID,   1200, 'Profesionales sin agrupación específica',    'PROFESIONAL');


-- ----------------------------------------------------------------------------
-- 5) Passwords (BCrypt; migración a Argon2id pendiente)
-- ----------------------------------------------------------------------------
/* Por defecto usamos la contraseña 'Aromito1'.
 * En GNU/Linux: htpasswd -bnBC 10 "" "Aromito1" | tr -d ':\n'
 * Online: https://bcrypt-generator.com/ con String 'Aromito1' y Rounds 12.
 *
 * Para probar la detección de contraseña por defecto, dos usuarios reciben
 * su CUIL como password:
 *   UserA: 27284644443 (Florencia Maurino)
 *   UserB: 24554978443 (Sebastián Bustos)
 */
SET @initPass  = '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO';
SET @userAPass = '$2a$12$TCAy.mqVsaRIwjQs0imr/uD0xNoUq/W3LSVys3EISI5hdBREwZW5a';
SET @userBPass = '$2a$12$x9SDga8sk3DyjvdeIwkhl.2e9wcWHewfHEFQFKkFp1.FiAKcyRZUG';


-- ----------------------------------------------------------------------------
-- 6) Usuarios
--
-- Los Servicios todavía no se insertaron, así que desactivamos FK checks
-- para poder usar los IDs preasignados en Usuario.servicioID.
-- ----------------------------------------------------------------------------
SET FOREIGN_KEY_CHECKS = 0;

INSERT INTO Usuario (id, nombres, apellidos, mail, cuil, fechaAlta, sexo, estado, domicilioID, servicioID, passwd) VALUES
    -- Dirección (4 personas)
    (@DirMarinoID,  'Mariano Gustavo', 'Marino',          'direccion@hmu.com.ar',      20224448885, CURDATE(), 'MASCULINO', TRUE, @HMUdomID, @DireccionID, @initPass),
    (@DirHuergoID,  'Federico',        'Huergo Sánchez',  'fedesubdir@hmu.com.ar',     20259993331, CURDATE(), 'MASCULINO', TRUE, @HIMdomID, @DireccionID, @initPass),
    (@DirLongoniID, 'Gloria',          'Longoni',         'subdirectora@hmu.com.ar',   27129997773, CURDATE(), 'FEMENINO',  TRUE, (SELECT id FROM Domicilio WHERE ciudad = 'Alta Gracia' LIMIT 1), @DireccionID, @initPass),
    (@DirVitaliID,  'Fabricio',        'Vitali',          'subdireccion@hmu.com.ar',   20239997772, CURDATE(), 'MASCULINO', TRUE, @HMUdomID, @DireccionID, @initPass),

    -- Jefaturas de Servicio
    (@JefeAdmisionID,        'Andrea',          'Balconte',       'andrea@hmu.com.ar',         27295554447, CURDATE(), 'FEMENINO',  TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), @AdmisionID,        @initPass),
    (@JefeBiomedicaID,       'Silvina',         'Maestro',        'smaestro@hmu.com.ar',       27274422442, CURDATE(), 'FEMENINO',  TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), @BiomedicaID,       @initPass),
    (@JefeCirugiaID,         'Maximiliano',     'Titarelli',      'drtita@hmu.com.ar',         20268944448, CURDATE(), 'MASCULINO', TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), @CirugiaID,         @initPass),
    (@JefeDiagPorImagenesID, 'Juan Ignacio',    'Morales',        'jimorales@hmu.com.ar',      20281324547, CURDATE(), 'MASCULINO', TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), @DiagPorImagenesID, @initPass),
    (@JefeEnfermeriaID,      'Tania',           'Plaza',          'tplaza@hmu.com.ar',         27224444445, CURDATE(), 'FEMENINO',  TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), @EnfermeriaID,      @initPass),
    (@JefeEsterilizacionID,  'Matías',          'Pérez Cabral',   'mcp@hmu.com.ar',            20289445441, CURDATE(), 'MASCULINO', TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), @EsterilizacionID,  @initPass),
    (@JefeFarmaciaID,        'María Pía',       'Arancibia',      'piaarancibia@hmu.com.ar',   27254344447, CURDATE(), 'FEMENINO',  TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), @FarmaciaID,        @initPass),
    (@JefeHabilitacionID,    'Carlos Fernando', 'Roberts',        'ferroberts@hmu.com.ar',     20276444446, CURDATE(), 'MASCULINO', TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), @HabilitacionID,    @initPass),
    (@JefeInformaticaID,     'Juan Manuel',     'Roqué',          'jmroque@hmu.com.ar',        20220361118, CURDATE(), 'OTRO',      TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), @InformaticaID,     @initPass),
    (@JefeNutricionID,       'Alejandra',       'Boqué',          'aleboque@hmu.com.ar',       27174444446, CURDATE(), 'FEMENINO',  TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), @NutricionID,       @initPass),
    (@JefeTraumatologiaID,   'Omar Wenceslao',  'Sánchez',        'owsanchez@hmu.com.ar',      20124644445, CURDATE(), 'MASCULINO', TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), @TraumatologiaID,   @initPass),
    (@JefeToxicologiaID,     'Andrea',          'Vilkelis',       'avilkelis@hmu.com.ar',      27214434447, CURDATE(), 'FEMENINO',  TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), @ToxicologiaID,     @initPass),

    -- Oficina de Personal
    (@JefePersonalID,        'Florencia',       'Maurino',        'flormaurino@hmu.com.ar',    27284644443, CURDATE(), 'FEMENINO',  TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), @PersonalID,        @userAPass),

    -- Empleados
    (UUID_TO_BIN(UUID()), 'Juan',           'Aniceto',           'janiceto@hmu.com.ar',      20124543421, CURDATE(), 'MASCULINO', TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), @DiagPorImagenesID, @initPass),
    (UUID_TO_BIN(UUID()), 'Baltazar',       'Garzón',            'baltig@hmu.com.ar',        20554644448, CURDATE(), 'MASCULINO', TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), @PersonalID,        @initPass),
    (UUID_TO_BIN(UUID()), 'Pedro',          'Taborda',           'pltaborda@hmu.com.ar',     20154786445, CURDATE(), 'MASCULINO', TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), @PersonalID,        @initPass),
    (UUID_TO_BIN(UUID()), 'María Laura',    'Vargas Ruíz',       'mlvargas@hmu.com.ar',      27554664563, CURDATE(), 'FEMENINO',  TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), @EsterilizacionID,  @initPass),
    (UUID_TO_BIN(UUID()), 'María Celeste',  'Vignetta',          'mcv@hmu.com.ar',           27554685443, CURDATE(), 'FEMENINO',  TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), @CirugiaID,         @initPass),
    (UUID_TO_BIN(UUID()), 'Alicia',         'Vivas',             'avivas@hmu.com.ar',        27554567443, CURDATE(), 'FEMENINO',  TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), @HabilitacionID,    @initPass),
    (UUID_TO_BIN(UUID()), 'Matías Quique',  'Canga Castellanos', 'lestat@hmu.com.ar',        20554649743, CURDATE(), 'MASCULINO', TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), @InformaticaID,     @initPass),
    (UUID_TO_BIN(UUID()), 'Mauricio Elio',  'Garay',             'mgaray@hmu.com.ar',        24574741745, CURDATE(), 'OTRO',      TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), @InformaticaID,     @initPass),
    (UUID_TO_BIN(UUID()), 'Eva Patricia',   'Usandivares',       'epu@hmu.com.ar',           27556786453, CURDATE(), 'FEMENINO',  TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), @PersonalID,        @initPass),
    (UUID_TO_BIN(UUID()), 'Edith',          'Tolay',             'etolay@hmu.com.ar',        20454676443, CURDATE(), 'FEMENINO',  TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), @EnfermeriaID,      @initPass),
    (UUID_TO_BIN(UUID()), 'José Luis',      'Terrieris',         'jterrieris@hmu.com.ar',    20554543453, CURDATE(), 'MASCULINO', TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), @EnfermeriaID,      @initPass),
    (UUID_TO_BIN(UUID()), 'Claudia',        'Tarifa',            'clautarifa@hmu.com.ar',    27540678443, CURDATE(), 'FEMENINO',  TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), @EnfermeriaID,      @initPass),
    (UUID_TO_BIN(UUID()), 'Erica',          'Tapia',             'erikatapia@hmu.com.ar',    27554560863, CURDATE(), 'FEMENINO',  TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), @ToxicologiaID,     @initPass),
    (UUID_TO_BIN(UUID()), 'Demetrio',       'Tampares',          'demetam@hmu.com.ar',       24784044443, CURDATE(), 'OTRO',      TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), @ToxicologiaID,     @initPass),
    (UUID_TO_BIN(UUID()), 'Alejandro',      'Suizer',            'amsuizer@hmu.com.ar',      20526456443, CURDATE(), 'MASCULINO', TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), @CirugiaID,         @initPass),
    (UUID_TO_BIN(UUID()), 'David',          'Suárez',            'dsuarez@hmu.com.ar',       20554897343, CURDATE(), 'MASCULINO', TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), @BiomedicaID,       @initPass),
    (UUID_TO_BIN(UUID()), 'Soledad',        'Romero',            'soleromero@hmu.com.ar',    27864567893, CURDATE(), 'FEMENINO',  TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), @AdmisionID,        @initPass),
    (UUID_TO_BIN(UUID()), 'Pedro',          'Rius',              'pedrorius@hmu.com.ar',     20554674843, CURDATE(), 'MASCULINO', TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), @DiagPorImagenesID, @initPass),
    (UUID_TO_BIN(UUID()), 'Damian',         'Rabbat',            'drabbat@hmu.com.ar',       20558244443, CURDATE(), 'MASCULINO', TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), @TraumatologiaID,   @initPass),
    (UUID_TO_BIN(UUID()), 'Ismael',         'Puig',              'ipuig@hmu.com.ar',         20567974443, CURDATE(), 'MASCULINO', TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), @TraumatologiaID,   @initPass),
    (UUID_TO_BIN(UUID()), 'Diego',          'Pascolo',           'dpascolo@hmu.com.ar',      20456645843, CURDATE(), 'MASCULINO', TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), @CirugiaID,         @initPass),
    (UUID_TO_BIN(UUID()), 'Sebastián',      'Bustos',            'sebustos@hmu.com.ar',      24554978443, CURDATE(), 'OTRO',      TRUE, (SELECT id FROM Domicilio WHERE calle = 'Av. Siempre Viva' LIMIT 1), @BiomedicaID, @userBPass);

SET FOREIGN_KEY_CHECKS = 1;


-- ----------------------------------------------------------------------------
-- 7) Servicios
-- ----------------------------------------------------------------------------
INSERT INTO Servicio (id, nombre, agrupacion, direccionID) VALUES
    (@AdministracionID,         'Administración',              'ADMINISTRATIVO', @DirMarinoID),
    (@AdmisionID,               'Admisión',                    'ADMINISTRATIVO', @DirMarinoID),
    (@AnatomiaPatologicaID,     'Anatomía Patológica',         'MEDICO',         @DirMarinoID),
    (@AnestesiaID,              'Anestesia',                   'MEDICO',         @DirMarinoID),
    (@AuditoriaMedicaID,        'Auditoría Médica',            'MEDICO',         @DirMarinoID),
    (@BiomedicaID,              'Biomédica',                   'TECNICO',        @DirVitaliID),
    (@BioquimicaID,             'Bioquímica',                  'SERVICIO',       @DirVitaliID),
    (@CamillerosID,             'Camilleros',                  'SERVICIO',       @DirVitaliID),
    (@CapacitacionDocenciaID,   'Capacitación y Docencia',     'MEDICO',         @DirMarinoID),
    (@CirugiaID,                'Cirugía',                     'MEDICO',         @DirMarinoID),
    (@CirugiaPlasticaID,        'Cirugía Plástica',            'MEDICO',         @DirMarinoID),
    (@ClinicaMedicaID,          'Clínica Médica',              'MEDICO',         @DirMarinoID),
    (@DiagPorImagenesID,        'Diagnóstico por Imágenes',    'MEDICO',         @DirMarinoID),
    (@DireccionID,              'Dirección',                   'ADMINISTRATIVO', @DirMarinoID),
    (@EnfermeriaID,             'Enfermería',                  'ENFERMERIA',     @DirMarinoID),
    (@EsterilizacionID,         'Esterilización',              'SERVICIO',       @DirVitaliID),
    (@FacturacionID,            'Facturación',                 'ADMINISTRATIVO', @DirMarinoID),
    (@FarmaciaID,               'Farmacia',                    'SERVICIO',       @DirVitaliID),
    (@HabilitacionID,           'Habilitación',                'ADMINISTRATIVO', @DirMarinoID),
    (@HemoterapiaID,            'Hemoterapia',                 'SERVICIO',       @DirVitaliID),
    (@InformaticaID,            'Informática',                 'TECNICO',        @DirVitaliID),
    (@InstrumentacionID,        'Instrumentación Quirúrgica',  'MEDICO',         @DirMarinoID),
    (@KinesiologiaID,           'Kinesiología',                'MEDICO',         @DirMarinoID),
    (@LaboratorioID,            'Laboratorio',                 'SERVICIO',       @DirVitaliID),
    (@LavaderoID,               'Lavadero',                    'SERVICIO',       @DirVitaliID),
    (@MantenimientoID,          'Mantenimiento',               'TECNICO',        @DirVitaliID),
    (@MedicinaLegalID,          'Medicina Legal',              'ADMINISTRATIVO', @DirMarinoID),
    (@NeurocirugiaID,           'Neurocirugía',                'MEDICO',         @DirMarinoID),
    (@NutricionID,              'Nutrición',                   'MEDICO',         @DirMarinoID),
    (@PersonalID,               'Personal',                    'ADMINISTRATIVO', @DirMarinoID),
    (@QuirofanoID,              'Quirófano',                   'MEDICO',         @DirMarinoID),
    (@RegMedicoID,              'Registro Médico (Archivo)',   'ADMINISTRATIVO', @DirMarinoID),
    (@SaludMentalID,            'Salud Mental',                'MEDICO',         @DirMarinoID),
    (@SecTecnicaID,             'Secretaría Técnica',          'ADMINISTRATIVO', @DirMarinoID),
    (@ServicioSocialID,         'Servicio Social',             'ADMINISTRATIVO', @DirMarinoID),
    (@TecnoelectricaID,         'Tecnoeléctrica',              'TECNICO',        @DirVitaliID),
    (@TraumatologiaID,          'Traumatología y Ortopedia',   'MEDICO',         @DirMarinoID);


-- ----------------------------------------------------------------------------
-- 8) Empleado: insertar a TODOS los usuarios del HMU
--
-- Convención: todo Usuario del hospital es también Empleado (tiene horario,
-- francos, etc.), incluso si su rol principal es jefatura/dirección/oficina.
-- El "tipo" se discrimina por roles asignados (Usuario_Rol), no por presencia
-- en sub-tablas.
-- ----------------------------------------------------------------------------
INSERT INTO Empleado (id, francosCompensatoriosUtilizados, horarioActualID)
SELECT id, 0, NULL FROM Usuario;


-- ----------------------------------------------------------------------------
-- 9) Vincular Jefaturas con Servicios
-- ----------------------------------------------------------------------------
INSERT INTO Servicio_JefaturaDeServicio (servicioID, empleadoID) VALUES
    (@AdmisionID,        @JefeAdmisionID),
    (@BiomedicaID,       @JefeBiomedicaID),
    (@CirugiaID,         @JefeCirugiaID),
    (@DiagPorImagenesID, @JefeDiagPorImagenesID),
    (@EnfermeriaID,      @JefeEnfermeriaID),
    (@EsterilizacionID,  @JefeEsterilizacionID),
    (@FarmaciaID,        @JefeFarmaciaID),
    (@HabilitacionID,    @JefeHabilitacionID),
    (@InformaticaID,     @JefeInformaticaID),
    (@NutricionID,       @JefeNutricionID),
    (@PersonalID,        @JefePersonalID),
    (@ToxicologiaID,     @JefeToxicologiaID),
    (@TraumatologiaID,   @JefeTraumatologiaID);

-- Listar todas las asignaciones de jefaturas
SELECT BIN_TO_UUID(s.id) AS servicio_ID,
       s.nombre          AS servicio_Nombre,
       BIN_TO_UUID(e.id) AS jefatura_ID,
       CONCAT(u.apellidos, ', ', u.nombres) AS jefatura_de_servicio
FROM Servicio s
JOIN Servicio_JefaturaDeServicio sjs ON s.id = sjs.servicioID
JOIN Empleado e ON sjs.empleadoID = e.id
JOIN Usuario u ON e.id = u.id;


-- ----------------------------------------------------------------------------
-- 10) Roles
-- ----------------------------------------------------------------------------
SET @RolEmpleado           = UUID_TO_BIN(UUID());
SET @RolJefaturaDeServicio = UUID_TO_BIN(UUID());
SET @RolOficinaDePersonal  = UUID_TO_BIN(UUID());
SET @RolDireccion          = UUID_TO_BIN(UUID());

INSERT INTO Rol (id, nombre, descripcion) VALUES
    (@RolEmpleado,           'Empleado',           'Empleado'),
    (@RolJefaturaDeServicio, 'JefaturaDeServicio', 'Jefatura de Servicio'),
    (@RolOficinaDePersonal,  'OficinaDePersonal',  'Oficina de Personal'),
    (@RolDireccion,          'Direccion',          'Directivo');

SELECT BIN_TO_UUID(id) AS id, nombre, descripcion FROM Rol;


-- ----------------------------------------------------------------------------
-- 11) Asignación de roles
--
-- Todos los Usuarios reciben el rol 'Empleado' (todos son personal del HMU).
-- Adicionalmente, las jefaturas/dirección/oficina de personal reciben sus
-- roles específicos.
-- ----------------------------------------------------------------------------
-- Empleado para todos
INSERT INTO Usuario_Rol (usuario_id, rol_id)
SELECT id, @RolEmpleado FROM Usuario;

-- JefaturaDeServicio para los jefes (incluye a Maurino, que también es OdP)
INSERT INTO Usuario_Rol (usuario_id, rol_id)
SELECT id, @RolJefaturaDeServicio FROM Usuario
WHERE id IN (
    @JefeAdmisionID, @JefeBiomedicaID, @JefeCirugiaID, @JefeDiagPorImagenesID,
    @JefeEnfermeriaID, @JefeEsterilizacionID, @JefeFarmaciaID, @JefeHabilitacionID,
    @JefeInformaticaID, @JefeNutricionID, @JefePersonalID, @JefeToxicologiaID,
    @JefeTraumatologiaID
);

-- OficinaDePersonal para Maurino
INSERT INTO Usuario_Rol (usuario_id, rol_id) VALUES (@JefePersonalID, @RolOficinaDePersonal);

-- Direccion para los 4 directivos (Marino también ejerce JefaturaDeServicio)
INSERT INTO Usuario_Rol (usuario_id, rol_id)
SELECT id, @RolDireccion FROM Usuario
WHERE id IN (@DirMarinoID, @DirHuergoID, @DirLongoniID, @DirVitaliID);
INSERT INTO Usuario_Rol (usuario_id, rol_id) VALUES (@DirMarinoID, @RolJefaturaDeServicio);

-- Listar todas las asignaciones de roles
SELECT BIN_TO_UUID(u.id) AS usuario_id,
       u.apellidos,
       BIN_TO_UUID(r.id) AS rol_id,
       r.nombre
FROM Usuario u
JOIN Usuario_Rol ur ON u.id = ur.usuario_id
JOIN Rol r ON ur.rol_id = r.id
ORDER BY apellidos;


-- ----------------------------------------------------------------------------
-- 12) Ejemplos de UPDATE / DELETE
-- ----------------------------------------------------------------------------

-- Modificar el teléfono al Jefe de Informática
UPDATE Usuario SET tel = '3517553799' WHERE id = @JefeInformaticaID;
SELECT nombres, apellidos, tel FROM Usuario WHERE id = @JefeInformaticaID;

-- Inicializar algunos cargos
UPDATE Usuario SET cargoID = @cargoUndefinedID;
UPDATE Usuario SET cargoID = @cargoTecnicoInicialID  WHERE servicioID = @BiomedicaID;
UPDATE Usuario SET cargoID = @cargoTecnicoInicialID  WHERE servicioID = @InformaticaID;
UPDATE Usuario SET cargoID = @cargoJefaturaInicialID WHERE id = @JefeInformaticaID;
UPDATE Usuario SET cargoID = @cargoJefaturaInicialID WHERE id = @JefePersonalID;

-- Borrar al usuario 'Soledad Romero'
SET @usuarioIdEliminar = (SELECT id FROM Usuario WHERE apellidos = 'Romero' AND nombres = 'Soledad' LIMIT 1);

-- Mostrar roles del usuario antes de borrarlo
SELECT r.nombre FROM Usuario_Rol ur JOIN Rol r ON ur.rol_id = r.id
WHERE ur.usuario_id = @usuarioIdEliminar;

-- Eliminar de Empleado y Usuario (Usuario_Rol cae por ON DELETE CASCADE)
DELETE FROM Empleado WHERE id = @usuarioIdEliminar;
DELETE FROM Usuario  WHERE id = @usuarioIdEliminar;
