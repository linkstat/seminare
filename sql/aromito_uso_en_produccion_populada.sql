/*
 Navicat Premium Dump SQL

 Source Server         : localhost
 Source Server Type    : MariaDB
 Source Server Version : 110403 (11.4.3-MariaDB)
 Source Host           : localhost:3306
 Source Schema         : aromito

 Target Server Type    : MariaDB
 Target Server Version : 110403 (11.4.3-MariaDB)
 File Encoding         : 65001

 Date: 21/11/2024 00:08:47
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for autorizacion
-- ----------------------------
DROP TABLE IF EXISTS `autorizacion`;
CREATE TABLE `autorizacion`  (
  `id` binary(16) NOT NULL,
  `fechaAutorizacion` datetime NOT NULL,
  `tipo` enum('DIRECCION','JEFATURADESERVICIO','OFICINADEPERSONAL','USUARIO') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `autorizadoPorID` binary(16) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `autorizadoPorID`(`autorizadoPorID` ASC) USING BTREE,
  CONSTRAINT `autorizacion_ibfk_1` FOREIGN KEY (`autorizadoPorID`) REFERENCES `usuario` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of autorizacion
-- ----------------------------

-- ----------------------------
-- Table structure for cargo
-- ----------------------------
DROP TABLE IF EXISTS `cargo`;
CREATE TABLE `cargo`  (
  `id` binary(16) NOT NULL,
  `numero` int(11) NOT NULL,
  `descripcion` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `agrupacion` enum('INDEFINIDO','PLANTAPOLITICA','JEFATURA','TECNICO','ADMINISTRATIVO','SERVICIO','ENFERMERIA','MEDICO','PROFESIONAL') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `numero`(`numero` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cargo
-- ----------------------------
INSERT INTO `cargo` VALUES (0x81DAD1E4A4B411EFA1E9D05099803C6D, 0, 'Cargo sin definir', 'INDEFINIDO');
INSERT INTO `cargo` VALUES (0x81DADA8CA4B411EFA1E9D05099803C6D, 200, 'Directivos', 'PLANTAPOLITICA');
INSERT INTO `cargo` VALUES (0x81DAE2BEA4B411EFA1E9D05099803C6D, 300, 'Jefaturas de Servicio', 'JEFATURA');
INSERT INTO `cargo` VALUES (0x81DAEA29A4B411EFA1E9D05099803C6D, 400, 'Técnicos', 'TECNICO');
INSERT INTO `cargo` VALUES (0x81DAF048A4B411EFA1E9D05099803C6D, 500, 'Administrativos', 'ADMINISTRATIVO');
INSERT INTO `cargo` VALUES (0x81DAF763A4B411EFA1E9D05099803C6D, 600, 'Servicios', 'SERVICIO');
INSERT INTO `cargo` VALUES (0x81DB01A8A4B411EFA1E9D05099803C6D, 700, 'Enfermería Técnicos', 'ENFERMERIA');
INSERT INTO `cargo` VALUES (0x81DB0AD2A4B411EFA1E9D05099803C6D, 800, 'Médicos', 'MEDICO');
INSERT INTO `cargo` VALUES (0x81DB16DDA4B411EFA1E9D05099803C6D, 900, 'Enfermería Profesionales', 'ENFERMERIA');
INSERT INTO `cargo` VALUES (0x81DB281BA4B411EFA1E9D05099803C6D, 1200, 'Profesionales sin agrupación específica', 'PROFESIONAL');

-- ----------------------------
-- Table structure for diagramadeservicio
-- ----------------------------
DROP TABLE IF EXISTS `diagramadeservicio`;
CREATE TABLE `diagramadeservicio`  (
  `id` binary(16) NOT NULL,
  `estado` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `fechaInicio` date NOT NULL,
  `fechaFin` date NOT NULL,
  `servicioID` binary(16) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `servicioID`(`servicioID` ASC) USING BTREE,
  CONSTRAINT `diagramadeservicio_ibfk_1` FOREIGN KEY (`servicioID`) REFERENCES `servicio` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of diagramadeservicio
-- ----------------------------

-- ----------------------------
-- Table structure for direccion
-- ----------------------------
DROP TABLE IF EXISTS `direccion`;
CREATE TABLE `direccion`  (
  `id` binary(16) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  CONSTRAINT `direccion_ibfk_1` FOREIGN KEY (`id`) REFERENCES `usuario` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of direccion
-- ----------------------------
INSERT INTO `direccion` VALUES (0x5F390597A78E11EF9F8FD05099803C6D);
INSERT INTO `direccion` VALUES (0x81DC3392A4B411EFA1E9D05099803C6D);
INSERT INTO `direccion` VALUES (0x81DC3656A4B411EFA1E9D05099803C6D);
INSERT INTO `direccion` VALUES (0x81DC378FA4B411EFA1E9D05099803C6D);
INSERT INTO `direccion` VALUES (0x81DC390BA4B411EFA1E9D05099803C6D);

-- ----------------------------
-- Table structure for domicilio
-- ----------------------------
DROP TABLE IF EXISTS `domicilio`;
CREATE TABLE `domicilio`  (
  `id` binary(16) NOT NULL,
  `calle` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `numeracion` int(11) NULL DEFAULT NULL,
  `barrio` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `ciudad` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `localidad` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `provincia` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_calle`(`calle` ASC) USING BTREE,
  INDEX `idx_barrio`(`barrio` ASC) USING BTREE,
  INDEX `idx_ciudad`(`ciudad` ASC) USING BTREE,
  INDEX `idx_localidad`(`localidad` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of domicilio
-- ----------------------------
INSERT INTO `domicilio` VALUES (0x26588A57A52611EFA1E9D05099803C6D, 'ca', 1, 'ba', 'ci', 'lo', 'pro');
INSERT INTO `domicilio` VALUES (0x3287BEEBA52611EFA1E9D05099803C6D, 'ca', 1, 'ba', 'ci', 'lo', 'pro');
INSERT INTO `domicilio` VALUES (0x3864D942A52611EFA1E9D05099803C6D, 'ca', 1, 'ba', 'ci', 'lo', 'pro');
INSERT INTO `domicilio` VALUES (0x4F1041F9A52611EFA1E9D05099803C6D, 'ca', 1, 'ba', 'ci', 'lo', 'pro');
INSERT INTO `domicilio` VALUES (0x4FFE4ABFA52511EFA1E9D05099803C6D, 'ca', 0, 'bb', 'ci', 'loc', 'p');
INSERT INTO `domicilio` VALUES (0x81D8230CA4B411EFA1E9D05099803C6D, 'Defensa', 1200, NULL, 'Córdoba', NULL, 'Córdoba');
INSERT INTO `domicilio` VALUES (0x81D826C8A4B411EFA1E9D05099803C6D, 'Lavalleja', 3050, NULL, 'Córdoba', NULL, 'Córdoba');
INSERT INTO `domicilio` VALUES (0x81D8284CA4B411EFA1E9D05099803C6D, 'Av Belgrano Sur', 134, NULL, 'La Rioja', NULL, 'La Rioja');
INSERT INTO `domicilio` VALUES (0x81D82ADDA4B411EFA1E9D05099803C6D, 'Av. Marcelo T. de Alvear', 120, NULL, 'Córdoba', NULL, 'Córdoba');
INSERT INTO `domicilio` VALUES (0x81D82C3CA4B411EFA1E9D05099803C6D, 'Av. Libertador', 1450, NULL, 'Alta Gracia', NULL, 'Córdoba');
INSERT INTO `domicilio` VALUES (0x81D82CEFA4B411EFA1E9D05099803C6D, 'Catamarca', 441, NULL, 'Córdoba', NULL, 'Córdoba');
INSERT INTO `domicilio` VALUES (0x81D82E51A4B411EFA1E9D05099803C6D, 'Junín', 162, NULL, 'Rosario', NULL, 'Santa Fe');
INSERT INTO `domicilio` VALUES (0x81D82F1AA4B411EFA1E9D05099803C6D, 'Mate de Luna', 262, NULL, 'Famaillá', NULL, 'Tucumán');
INSERT INTO `domicilio` VALUES (0x81D83077A4B411EFA1E9D05099803C6D, 'Gobernación', 824, NULL, 'Santa Fe', NULL, 'Santa Fe');
INSERT INTO `domicilio` VALUES (0x81D83138A4B411EFA1E9D05099803C6D, 'Güemes', 333, NULL, 'Iruya', NULL, 'Jujuy');
INSERT INTO `domicilio` VALUES (0x81D831E6A4B411EFA1E9D05099803C6D, '9 de julio', 192, NULL, 'Corrientes', NULL, 'Corrientes');
INSERT INTO `domicilio` VALUES (0x81D8329EA4B411EFA1E9D05099803C6D, 'Av. del Trabajo', 959, NULL, 'Rosario', NULL, 'Santa Fe');
INSERT INTO `domicilio` VALUES (0x81D8334BA4B411EFA1E9D05099803C6D, 'Calle del Río', 222, NULL, 'Paraná', NULL, 'Entre Ríos');
INSERT INTO `domicilio` VALUES (0x81D833F3A4B411EFA1E9D05099803C6D, 'Av. Central', 230, NULL, 'Salta', NULL, 'Salta');
INSERT INTO `domicilio` VALUES (0x81D834A1A4B411EFA1E9D05099803C6D, 'Av. Siempre Viva', 484, NULL, 'Rawson', NULL, 'Chubut');
INSERT INTO `domicilio` VALUES (0x81D8354CA4B411EFA1E9D05099803C6D, 'San Martín', 729, NULL, 'Bariloche', NULL, 'Río Negro');
INSERT INTO `domicilio` VALUES (0x8AB979CDA52611EFA1E9D05099803C6D, 'ca', 1, 'ba', 'ci', 'lo', 'pro');
INSERT INTO `domicilio` VALUES (0xC1BD7B93A55611EFA1E9D05099803C6D, 'asd', 0, 'asd', 'asd', 'asd', 'asd');

-- ----------------------------
-- Table structure for empleado
-- ----------------------------
DROP TABLE IF EXISTS `empleado`;
CREATE TABLE `empleado`  (
  `id` binary(16) NOT NULL,
  `francosCompensatoriosUtilizados` int(11) NULL DEFAULT NULL,
  `horarioActualID` binary(16) NULL DEFAULT NULL,
  `jefaturaID` binary(16) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `horarioActualID`(`horarioActualID` ASC) USING BTREE,
  INDEX `idx_empleado_jefatura`(`jefaturaID` ASC) USING BTREE,
  CONSTRAINT `empleado_ibfk_1` FOREIGN KEY (`id`) REFERENCES `usuario` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `empleado_ibfk_2` FOREIGN KEY (`horarioActualID`) REFERENCES `horariobase` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `empleado_ibfk_3` FOREIGN KEY (`jefaturaID`) REFERENCES `jefaturadeservicio` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of empleado
-- ----------------------------
INSERT INTO `empleado` VALUES (0x81DC5426A4B411EFA1E9D05099803C6D, 0, NULL, 0x81DA8AA8A4B411EFA1E9D05099803C6D);
INSERT INTO `empleado` VALUES (0x81DC5D14A4B411EFA1E9D05099803C6D, 0, NULL, 0x81DA9AC0A4B411EFA1E9D05099803C6D);
INSERT INTO `empleado` VALUES (0x81DC5F91A4B411EFA1E9D05099803C6D, 0, NULL, 0x81DA8463A4B411EFA1E9D05099803C6D);
INSERT INTO `empleado` VALUES (0x81DC6273A4B411EFA1E9D05099803C6D, 0, NULL, 0x81DAA89EA4B411EFA1E9D05099803C6D);
INSERT INTO `empleado` VALUES (0x81DC64ABA4B411EFA1E9D05099803C6D, 0, NULL, 0x81DAB0A8A4B411EFA1E9D05099803C6D);
INSERT INTO `empleado` VALUES (0x81DC668DA4B411EFA1E9D05099803C6D, 0, NULL, 0x81DAB0A8A4B411EFA1E9D05099803C6D);
INSERT INTO `empleado` VALUES (0x81DC6B32A4B411EFA1E9D05099803C6D, 0, NULL, 0x81DA944AA4B411EFA1E9D05099803C6D);
INSERT INTO `empleado` VALUES (0x81DC6D09A4B411EFA1E9D05099803C6D, 0, NULL, 0x81DA944AA4B411EFA1E9D05099803C6D);
INSERT INTO `empleado` VALUES (0x81DC7318A4B411EFA1E9D05099803C6D, 0, NULL, 0x81DA944AA4B411EFA1E9D05099803C6D);
INSERT INTO `empleado` VALUES (0x81DC75EFA4B411EFA1E9D05099803C6D, 0, NULL, 0x81DACB86A4B411EFA1E9D05099803C6D);
INSERT INTO `empleado` VALUES (0x81DC7813A4B411EFA1E9D05099803C6D, 0, NULL, 0x81DACB86A4B411EFA1E9D05099803C6D);
INSERT INTO `empleado` VALUES (0x81DC7AA2A4B411EFA1E9D05099803C6D, 0, NULL, 0x81DA8463A4B411EFA1E9D05099803C6D);
INSERT INTO `empleado` VALUES (0x81DC7D9BA4B411EFA1E9D05099803C6D, 0, NULL, 0x81DA7D31A4B411EFA1E9D05099803C6D);
INSERT INTO `empleado` VALUES (0x81DC8235A4B411EFA1E9D05099803C6D, 0, NULL, 0x81DA8AA8A4B411EFA1E9D05099803C6D);
INSERT INTO `empleado` VALUES (0x81DC840FA4B411EFA1E9D05099803C6D, 0, NULL, 0x81DAC47CA4B411EFA1E9D05099803C6D);
INSERT INTO `empleado` VALUES (0x81DC872FA4B411EFA1E9D05099803C6D, 0, NULL, 0x81DAC47CA4B411EFA1E9D05099803C6D);
INSERT INTO `empleado` VALUES (0x81DC896CA4B411EFA1E9D05099803C6D, 0, NULL, 0x81DA8463A4B411EFA1E9D05099803C6D);
INSERT INTO `empleado` VALUES (0x81DC8B2BA4B411EFA1E9D05099803C6D, 0, NULL, 0x81DA7D31A4B411EFA1E9D05099803C6D);
INSERT INTO `empleado` VALUES (0xC6CDA736A79611EF9F8FD05099803C6D, 0, NULL, 0x81DAB0A8A4B411EFA1E9D05099803C6D);

-- ----------------------------
-- Table structure for empleado_novedad
-- ----------------------------
DROP TABLE IF EXISTS `empleado_novedad`;
CREATE TABLE `empleado_novedad`  (
  `empleadoID` binary(16) NOT NULL,
  `novedadID` binary(16) NOT NULL,
  PRIMARY KEY (`empleadoID`, `novedadID`) USING BTREE,
  INDEX `novedadID`(`novedadID` ASC) USING BTREE,
  CONSTRAINT `empleado_novedad_ibfk_1` FOREIGN KEY (`empleadoID`) REFERENCES `empleado` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `empleado_novedad_ibfk_2` FOREIGN KEY (`novedadID`) REFERENCES `novedad` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of empleado_novedad
-- ----------------------------

-- ----------------------------
-- Table structure for estadotramite
-- ----------------------------
DROP TABLE IF EXISTS `estadotramite`;
CREATE TABLE `estadotramite`  (
  `id` binary(16) NOT NULL,
  `nombre` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of estadotramite
-- ----------------------------

-- ----------------------------
-- Table structure for francocompensatorio
-- ----------------------------
DROP TABLE IF EXISTS `francocompensatorio`;
CREATE TABLE `francocompensatorio`  (
  `id` binary(16) NOT NULL,
  `cantHoras` double NOT NULL,
  `descripcion` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `fechaAutorizacion` datetime NULL DEFAULT NULL,
  `fechaDeAplicacion` date NULL DEFAULT NULL,
  `estadoTramiteID` binary(16) NOT NULL,
  `autorizadaPorID` binary(16) NULL DEFAULT NULL,
  `empleadoID` binary(16) NOT NULL,
  `jefaturaID` binary(16) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `estadoTramiteID`(`estadoTramiteID` ASC) USING BTREE,
  INDEX `autorizadaPorID`(`autorizadaPorID` ASC) USING BTREE,
  INDEX `empleadoID`(`empleadoID` ASC) USING BTREE,
  INDEX `jefaturaID`(`jefaturaID` ASC) USING BTREE,
  CONSTRAINT `francocompensatorio_ibfk_1` FOREIGN KEY (`estadoTramiteID`) REFERENCES `estadotramite` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `francocompensatorio_ibfk_2` FOREIGN KEY (`autorizadaPorID`) REFERENCES `usuario` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `francocompensatorio_ibfk_3` FOREIGN KEY (`empleadoID`) REFERENCES `empleado` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `francocompensatorio_ibfk_4` FOREIGN KEY (`jefaturaID`) REFERENCES `jefaturadeservicio` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of francocompensatorio
-- ----------------------------

-- ----------------------------
-- Table structure for horaextra
-- ----------------------------
DROP TABLE IF EXISTS `horaextra`;
CREATE TABLE `horaextra`  (
  `id` binary(16) NOT NULL,
  `descripcion` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `fechaIngreso` datetime NOT NULL,
  `fechaEgreso` datetime NOT NULL,
  `ponderacion` int(11) NOT NULL,
  `fechaAutorizacion` datetime NULL DEFAULT NULL,
  `estadoTramiteID` binary(16) NOT NULL,
  `autorizadaPorID` binary(16) NULL DEFAULT NULL,
  `empleadoID` binary(16) NOT NULL,
  `jefaturaID` binary(16) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `estadoTramiteID`(`estadoTramiteID` ASC) USING BTREE,
  INDEX `autorizadaPorID`(`autorizadaPorID` ASC) USING BTREE,
  INDEX `empleadoID`(`empleadoID` ASC) USING BTREE,
  INDEX `jefaturaID`(`jefaturaID` ASC) USING BTREE,
  CONSTRAINT `horaextra_ibfk_1` FOREIGN KEY (`estadoTramiteID`) REFERENCES `estadotramite` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `horaextra_ibfk_2` FOREIGN KEY (`autorizadaPorID`) REFERENCES `usuario` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `horaextra_ibfk_3` FOREIGN KEY (`empleadoID`) REFERENCES `empleado` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `horaextra_ibfk_4` FOREIGN KEY (`jefaturaID`) REFERENCES `jefaturadeservicio` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of horaextra
-- ----------------------------

-- ----------------------------
-- Table structure for horario
-- ----------------------------
DROP TABLE IF EXISTS `horario`;
CREATE TABLE `horario`  (
  `id` binary(16) NOT NULL,
  `fechaIngreso` datetime NOT NULL,
  `fechaEgreso` datetime NOT NULL,
  `jornadasPlanificadas` int(11) NULL DEFAULT NULL,
  `reglasHorario` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `horarioBaseID` binary(16) NOT NULL,
  `modalidad` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  CONSTRAINT `horario_ibfk_1` FOREIGN KEY (`id`) REFERENCES `horariobase` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of horario
-- ----------------------------

-- ----------------------------
-- Table structure for horarioabierto
-- ----------------------------
DROP TABLE IF EXISTS `horarioabierto`;
CREATE TABLE `horarioabierto`  (
  `id` binary(16) NOT NULL,
  `flexibilidadHoraria` tinyint(1) NOT NULL,
  `horasSemanales` int(11) NOT NULL,
  `preferenciasHorarias` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  CONSTRAINT `horarioabierto_ibfk_1` FOREIGN KEY (`id`) REFERENCES `horario` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of horarioabierto
-- ----------------------------

-- ----------------------------
-- Table structure for horariobase
-- ----------------------------
DROP TABLE IF EXISTS `horariobase`;
CREATE TABLE `horariobase`  (
  `id` binary(16) NOT NULL,
  `tipo` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of horariobase
-- ----------------------------

-- ----------------------------
-- Table structure for horarioconfranquicia
-- ----------------------------
DROP TABLE IF EXISTS `horarioconfranquicia`;
CREATE TABLE `horarioconfranquicia`  (
  `id` binary(16) NOT NULL,
  `fechaIngreso` datetime NOT NULL,
  `fechaEgreso` datetime NOT NULL,
  `horasFranquicia` int(11) NOT NULL,
  `horarioDecoradoID` binary(16) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `horarioDecoradoID`(`horarioDecoradoID` ASC) USING BTREE,
  CONSTRAINT `horarioconfranquicia_ibfk_1` FOREIGN KEY (`id`) REFERENCES `horariobase` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `horarioconfranquicia_ibfk_2` FOREIGN KEY (`horarioDecoradoID`) REFERENCES `horario` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of horarioconfranquicia
-- ----------------------------

-- ----------------------------
-- Table structure for horariodxi
-- ----------------------------
DROP TABLE IF EXISTS `horariodxi`;
CREATE TABLE `horariodxi`  (
  `id` binary(16) NOT NULL,
  `distribucionHoraria` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `horaInicioPorDia` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `horasSemanales` int(11) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  CONSTRAINT `horariodxi_ibfk_1` FOREIGN KEY (`id`) REFERENCES `horario` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of horariodxi
-- ----------------------------

-- ----------------------------
-- Table structure for horarioestandar
-- ----------------------------
DROP TABLE IF EXISTS `horarioestandar`;
CREATE TABLE `horarioestandar`  (
  `id` binary(16) NOT NULL,
  `diasLaborables` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `horasPorDia` int(11) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  CONSTRAINT `horarioestandar_ibfk_1` FOREIGN KEY (`id`) REFERENCES `horario` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of horarioestandar
-- ----------------------------

-- ----------------------------
-- Table structure for horarioferiante
-- ----------------------------
DROP TABLE IF EXISTS `horarioferiante`;
CREATE TABLE `horarioferiante`  (
  `id` binary(16) NOT NULL,
  `diasNoLaborables` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `duracionGuardiaHoras` int(11) NOT NULL,
  `guardiasProgramadas` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `horasMinimasMensuales` int(11) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  CONSTRAINT `horarioferiante_ibfk_1` FOREIGN KEY (`id`) REFERENCES `horario` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of horarioferiante
-- ----------------------------

-- ----------------------------
-- Table structure for horarioguardiaenfermeria
-- ----------------------------
DROP TABLE IF EXISTS `horarioguardiaenfermeria`;
CREATE TABLE `horarioguardiaenfermeria`  (
  `id` binary(16) NOT NULL,
  `duracionGuardia10Horas` int(11) NOT NULL,
  `duracionGuardia12Horas` int(11) NOT NULL,
  `fechasGuardias` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `numeroGuardias10Horas` int(11) NOT NULL,
  `numeroGuardias12Horas` int(11) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  CONSTRAINT `horarioguardiaenfermeria_ibfk_1` FOREIGN KEY (`id`) REFERENCES `horario` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of horarioguardiaenfermeria
-- ----------------------------

-- ----------------------------
-- Table structure for horarioguardiamedica
-- ----------------------------
DROP TABLE IF EXISTS `horarioguardiamedica`;
CREATE TABLE `horarioguardiamedica`  (
  `id` binary(16) NOT NULL,
  `duracionGuardiaHoras` int(11) NOT NULL,
  `fechasGuardias` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `numeroGuardiasSemanal` int(11) NOT NULL,
  `permitirGuardiasContinuas` tinyint(1) NOT NULL,
  `tiempoDescansoMinimoHoras` int(11) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  CONSTRAINT `horarioguardiamedica_ibfk_1` FOREIGN KEY (`id`) REFERENCES `horario` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of horarioguardiamedica
-- ----------------------------

-- ----------------------------
-- Table structure for horariojefeservicioguardiapasiva
-- ----------------------------
DROP TABLE IF EXISTS `horariojefeservicioguardiapasiva`;
CREATE TABLE `horariojefeservicioguardiapasiva`  (
  `id` binary(16) NOT NULL,
  `diasLaborables` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `horasPorDia` int(11) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  CONSTRAINT `horariojefeservicioguardiapasiva_ibfk_1` FOREIGN KEY (`id`) REFERENCES `horario` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of horariojefeservicioguardiapasiva
-- ----------------------------

-- ----------------------------
-- Table structure for horarionocturno
-- ----------------------------
DROP TABLE IF EXISTS `horarionocturno`;
CREATE TABLE `horarionocturno`  (
  `id` binary(16) NOT NULL,
  `diasProgramados` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `duracionJornadaHoras` int(11) NOT NULL,
  `numeroJornadasMensuales` int(11) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  CONSTRAINT `horarionocturno_ibfk_1` FOREIGN KEY (`id`) REFERENCES `horario` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of horarionocturno
-- ----------------------------

-- ----------------------------
-- Table structure for horariosemanal
-- ----------------------------
DROP TABLE IF EXISTS `horariosemanal`;
CREATE TABLE `horariosemanal`  (
  `id` binary(16) NOT NULL,
  `distribucionSemanal` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `horaInicioPorDia` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  CONSTRAINT `horariosemanal_ibfk_1` FOREIGN KEY (`id`) REFERENCES `horario` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of horariosemanal
-- ----------------------------

-- ----------------------------
-- Table structure for jefaturadeservicio
-- ----------------------------
DROP TABLE IF EXISTS `jefaturadeservicio`;
CREATE TABLE `jefaturadeservicio`  (
  `id` binary(16) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  CONSTRAINT `jefaturadeservicio_ibfk_1` FOREIGN KEY (`id`) REFERENCES `usuario` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of jefaturadeservicio
-- ----------------------------
INSERT INTO `jefaturadeservicio` VALUES (0x81DA7594A4B411EFA1E9D05099803C6D);
INSERT INTO `jefaturadeservicio` VALUES (0x81DA7D31A4B411EFA1E9D05099803C6D);
INSERT INTO `jefaturadeservicio` VALUES (0x81DA8463A4B411EFA1E9D05099803C6D);
INSERT INTO `jefaturadeservicio` VALUES (0x81DA8AA8A4B411EFA1E9D05099803C6D);
INSERT INTO `jefaturadeservicio` VALUES (0x81DA944AA4B411EFA1E9D05099803C6D);
INSERT INTO `jefaturadeservicio` VALUES (0x81DA9AC0A4B411EFA1E9D05099803C6D);
INSERT INTO `jefaturadeservicio` VALUES (0x81DAA286A4B411EFA1E9D05099803C6D);
INSERT INTO `jefaturadeservicio` VALUES (0x81DAA89EA4B411EFA1E9D05099803C6D);
INSERT INTO `jefaturadeservicio` VALUES (0x81DAB0A8A4B411EFA1E9D05099803C6D);
INSERT INTO `jefaturadeservicio` VALUES (0x81DAB7D4A4B411EFA1E9D05099803C6D);
INSERT INTO `jefaturadeservicio` VALUES (0x81DAC47CA4B411EFA1E9D05099803C6D);
INSERT INTO `jefaturadeservicio` VALUES (0x81DACB86A4B411EFA1E9D05099803C6D);
INSERT INTO `jefaturadeservicio` VALUES (0xF9AF8F9AA79011EF9F8FD05099803C6D);

-- ----------------------------
-- Table structure for jornadalaboral
-- ----------------------------
DROP TABLE IF EXISTS `jornadalaboral`;
CREATE TABLE `jornadalaboral`  (
  `id` binary(16) NOT NULL,
  `fechaIngreso` datetime NOT NULL,
  `fechaEgreso` datetime NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of jornadalaboral
-- ----------------------------

-- ----------------------------
-- Table structure for marcacionempleado
-- ----------------------------
DROP TABLE IF EXISTS `marcacionempleado`;
CREATE TABLE `marcacionempleado`  (
  `id` binary(16) NOT NULL,
  `fechaMarcacion` datetime NOT NULL,
  `observaciones` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `tipoMarcacion` enum('INGRESO','EGRESO') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `validada` tinyint(1) NOT NULL,
  `empleadoID` binary(16) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `empleadoID`(`empleadoID` ASC) USING BTREE,
  CONSTRAINT `marcacionempleado_ibfk_1` FOREIGN KEY (`empleadoID`) REFERENCES `empleado` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of marcacionempleado
-- ----------------------------

-- ----------------------------
-- Table structure for memorandum
-- ----------------------------
DROP TABLE IF EXISTS `memorandum`;
CREATE TABLE `memorandum`  (
  `id` binary(16) NOT NULL,
  `asunto` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `contenido` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `fechaEnvio` datetime NULL DEFAULT NULL,
  `fechaRecepcion` datetime NULL DEFAULT NULL,
  `estado` binary(16) NOT NULL,
  `remitenteID` binary(16) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `estado`(`estado` ASC) USING BTREE,
  INDEX `remitenteID`(`remitenteID` ASC) USING BTREE,
  CONSTRAINT `memorandum_ibfk_1` FOREIGN KEY (`estado`) REFERENCES `estadotramite` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `memorandum_ibfk_2` FOREIGN KEY (`remitenteID`) REFERENCES `usuario` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of memorandum
-- ----------------------------

-- ----------------------------
-- Table structure for memorandum_autorizacion
-- ----------------------------
DROP TABLE IF EXISTS `memorandum_autorizacion`;
CREATE TABLE `memorandum_autorizacion`  (
  `id` binary(16) NOT NULL,
  `memorandumID` binary(16) NOT NULL,
  `tipoAutorizacionID` enum('JefeDeServicio','OficinaDePersonal','Direccion','Usuario') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `autorizadoPorID` binary(16) NULL DEFAULT NULL,
  `fechaAutorizacion` datetime NULL DEFAULT NULL,
  `estado` enum('PENDIENTE','AUTORIZADO','RECHAZADO') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `memorandumID`(`memorandumID` ASC) USING BTREE,
  INDEX `autorizadoPorID`(`autorizadoPorID` ASC) USING BTREE,
  CONSTRAINT `memorandum_autorizacion_ibfk_1` FOREIGN KEY (`memorandumID`) REFERENCES `memorandum` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `memorandum_autorizacion_ibfk_2` FOREIGN KEY (`autorizadoPorID`) REFERENCES `usuario` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of memorandum_autorizacion
-- ----------------------------

-- ----------------------------
-- Table structure for memorandum_destinatario
-- ----------------------------
DROP TABLE IF EXISTS `memorandum_destinatario`;
CREATE TABLE `memorandum_destinatario`  (
  `memorandumID` binary(16) NOT NULL,
  `usuarioID` binary(16) NOT NULL,
  `fechaRecepcion` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`memorandumID`, `usuarioID`) USING BTREE,
  INDEX `usuarioID`(`usuarioID` ASC) USING BTREE,
  CONSTRAINT `memorandum_destinatario_ibfk_1` FOREIGN KEY (`memorandumID`) REFERENCES `memorandum` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `memorandum_destinatario_ibfk_2` FOREIGN KEY (`usuarioID`) REFERENCES `usuario` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of memorandum_destinatario
-- ----------------------------

-- ----------------------------
-- Table structure for memorandum_firmante
-- ----------------------------
DROP TABLE IF EXISTS `memorandum_firmante`;
CREATE TABLE `memorandum_firmante`  (
  `memorandumID` binary(16) NOT NULL,
  `usuarioID` binary(16) NOT NULL,
  `fechaFirma` datetime NOT NULL,
  PRIMARY KEY (`memorandumID`, `usuarioID`) USING BTREE,
  INDEX `usuarioID`(`usuarioID` ASC) USING BTREE,
  CONSTRAINT `memorandum_firmante_ibfk_1` FOREIGN KEY (`memorandumID`) REFERENCES `memorandum` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `memorandum_firmante_ibfk_2` FOREIGN KEY (`usuarioID`) REFERENCES `usuario` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of memorandum_firmante
-- ----------------------------

-- ----------------------------
-- Table structure for novedad
-- ----------------------------
DROP TABLE IF EXISTS `novedad`;
CREATE TABLE `novedad`  (
  `id` binary(16) NOT NULL,
  `cod` int(11) NOT NULL,
  `descripcion` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `estado` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `estadoFechaModif` timestamp NULL DEFAULT NULL,
  `fechaInicio` date NULL DEFAULT NULL,
  `fechaFin` date NULL DEFAULT NULL,
  `fechaSolicitud` date NULL DEFAULT NULL,
  `reqAprobDireccion` tinyint(1) NOT NULL,
  `estadoTramiteID` binary(16) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `estadoTramiteID`(`estadoTramiteID` ASC) USING BTREE,
  CONSTRAINT `novedad_ibfk_1` FOREIGN KEY (`estadoTramiteID`) REFERENCES `estadotramite` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of novedad
-- ----------------------------

-- ----------------------------
-- Table structure for oficinadepersonal
-- ----------------------------
DROP TABLE IF EXISTS `oficinadepersonal`;
CREATE TABLE `oficinadepersonal`  (
  `id` binary(16) NOT NULL,
  `reportesGenerados` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  CONSTRAINT `oficinadepersonal_ibfk_1` FOREIGN KEY (`id`) REFERENCES `usuario` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of oficinadepersonal
-- ----------------------------
INSERT INTO `oficinadepersonal` VALUES (0x6BFCC5FAA79211EF9F8FD05099803C6D, 0);
INSERT INTO `oficinadepersonal` VALUES (0x81DABE35A4B411EFA1E9D05099803C6D, 0);

-- ----------------------------
-- Table structure for partediario
-- ----------------------------
DROP TABLE IF EXISTS `partediario`;
CREATE TABLE `partediario`  (
  `id` binary(16) NOT NULL,
  `fechaDeCierre` datetime NULL DEFAULT NULL,
  `periodo` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `modificadoPorID` binary(16) NULL DEFAULT NULL,
  `oficinaID` binary(16) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `modificadoPorID`(`modificadoPorID` ASC) USING BTREE,
  INDEX `oficinaID`(`oficinaID` ASC) USING BTREE,
  CONSTRAINT `partediario_ibfk_1` FOREIGN KEY (`modificadoPorID`) REFERENCES `usuario` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `partediario_ibfk_2` FOREIGN KEY (`oficinaID`) REFERENCES `oficinadepersonal` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of partediario
-- ----------------------------

-- ----------------------------
-- Table structure for partediario_empleado
-- ----------------------------
DROP TABLE IF EXISTS `partediario_empleado`;
CREATE TABLE `partediario_empleado`  (
  `parteDiarioID` binary(16) NOT NULL,
  `empleadoID` binary(16) NOT NULL,
  PRIMARY KEY (`parteDiarioID`, `empleadoID`) USING BTREE,
  INDEX `empleadoID`(`empleadoID` ASC) USING BTREE,
  CONSTRAINT `partediario_empleado_ibfk_1` FOREIGN KEY (`parteDiarioID`) REFERENCES `partediario` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `partediario_empleado_ibfk_2` FOREIGN KEY (`empleadoID`) REFERENCES `empleado` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of partediario_empleado
-- ----------------------------

-- ----------------------------
-- Table structure for planificacion
-- ----------------------------
DROP TABLE IF EXISTS `planificacion`;
CREATE TABLE `planificacion`  (
  `diagramaID` binary(16) NOT NULL,
  `empleadoID` binary(16) NOT NULL,
  `jornadaID` binary(16) NOT NULL,
  PRIMARY KEY (`diagramaID`, `empleadoID`, `jornadaID`) USING BTREE,
  INDEX `empleadoID`(`empleadoID` ASC) USING BTREE,
  INDEX `jornadaID`(`jornadaID` ASC) USING BTREE,
  CONSTRAINT `planificacion_ibfk_1` FOREIGN KEY (`diagramaID`) REFERENCES `diagramadeservicio` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `planificacion_ibfk_2` FOREIGN KEY (`empleadoID`) REFERENCES `empleado` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `planificacion_ibfk_3` FOREIGN KEY (`jornadaID`) REFERENCES `jornadalaboral` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of planificacion
-- ----------------------------

-- ----------------------------
-- Table structure for registrojornadalaboral
-- ----------------------------
DROP TABLE IF EXISTS `registrojornadalaboral`;
CREATE TABLE `registrojornadalaboral`  (
  `id` binary(16) NOT NULL,
  `fecha` date NOT NULL,
  `empleadoID` binary(16) NOT NULL,
  `marcacionIngresoID` binary(16) NOT NULL,
  `marcacionEgresoID` binary(16) NOT NULL,
  `duracionJornada` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `empleadoID`(`empleadoID` ASC) USING BTREE,
  INDEX `marcacionIngresoID`(`marcacionIngresoID` ASC) USING BTREE,
  INDEX `marcacionEgresoID`(`marcacionEgresoID` ASC) USING BTREE,
  CONSTRAINT `registrojornadalaboral_ibfk_1` FOREIGN KEY (`empleadoID`) REFERENCES `empleado` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `registrojornadalaboral_ibfk_2` FOREIGN KEY (`marcacionIngresoID`) REFERENCES `marcacionempleado` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `registrojornadalaboral_ibfk_3` FOREIGN KEY (`marcacionEgresoID`) REFERENCES `marcacionempleado` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of registrojornadalaboral
-- ----------------------------

-- ----------------------------
-- Table structure for rol
-- ----------------------------
DROP TABLE IF EXISTS `rol`;
CREATE TABLE `rol`  (
  `id` binary(16) NOT NULL,
  `nombre` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `descripcion` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `nombre`(`nombre` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of rol
-- ----------------------------
INSERT INTO `rol` VALUES (0x81E2C998A4B411EFA1E9D05099803C6D, 'Empleado', 'Agente');
INSERT INTO `rol` VALUES (0x81E2D54EA4B411EFA1E9D05099803C6D, 'JefaturaDeServicio', 'Jefe de Servicio');
INSERT INTO `rol` VALUES (0x81E2DFFBA4B411EFA1E9D05099803C6D, 'OficinaDePersonal', 'Oficina de Personal');
INSERT INTO `rol` VALUES (0x81E2E98DA4B411EFA1E9D05099803C6D, 'Direccion', 'Directivo');

-- ----------------------------
-- Table structure for servicio
-- ----------------------------
DROP TABLE IF EXISTS `servicio`;
CREATE TABLE `servicio`  (
  `id` binary(16) NOT NULL,
  `nombre` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `agrupacion` enum('ADMINISTRATIVO','SERVICIO','MEDICO','ENFERMERIA','TECNICO','PLANTA POLITICA') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `direccionID` binary(16) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_nombre`(`nombre` ASC) USING BTREE,
  INDEX `idx_servicio_direccion`(`direccionID` ASC) USING BTREE,
  CONSTRAINT `servicio_ibfk_1` FOREIGN KEY (`direccionID`) REFERENCES `direccion` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of servicio
-- ----------------------------
INSERT INTO `servicio` VALUES (0x81D8EA70A4B411EFA1E9D05099803C6D, 'Administración', 'ADMINISTRATIVO', 0x81DC3392A4B411EFA1E9D05099803C6D);
INSERT INTO `servicio` VALUES (0x81D8F426A4B411EFA1E9D05099803C6D, 'Admisión', 'ADMINISTRATIVO', 0x81DC3392A4B411EFA1E9D05099803C6D);
INSERT INTO `servicio` VALUES (0x81D90246A4B411EFA1E9D05099803C6D, 'Anatomía Patológica', 'MEDICO', 0x81DC3392A4B411EFA1E9D05099803C6D);
INSERT INTO `servicio` VALUES (0x81D90C7AA4B411EFA1E9D05099803C6D, 'Anestesia', 'MEDICO', 0x81DC3392A4B411EFA1E9D05099803C6D);
INSERT INTO `servicio` VALUES (0x81D917A2A4B411EFA1E9D05099803C6D, 'Auditoría Médica', 'MEDICO', 0x81DC3392A4B411EFA1E9D05099803C6D);
INSERT INTO `servicio` VALUES (0x81D92554A4B411EFA1E9D05099803C6D, 'Biomédica', 'TECNICO', 0x81DC390BA4B411EFA1E9D05099803C6D);
INSERT INTO `servicio` VALUES (0x81D92D04A4B411EFA1E9D05099803C6D, 'Bioquímica', 'SERVICIO', 0x81DC390BA4B411EFA1E9D05099803C6D);
INSERT INTO `servicio` VALUES (0x81D93584A4B411EFA1E9D05099803C6D, 'Camilleros', 'SERVICIO', 0x81DC390BA4B411EFA1E9D05099803C6D);
INSERT INTO `servicio` VALUES (0x81D940FFA4B411EFA1E9D05099803C6D, 'Capacitación y Docencia', 'MEDICO', 0x81DC3392A4B411EFA1E9D05099803C6D);
INSERT INTO `servicio` VALUES (0x81D94C41A4B411EFA1E9D05099803C6D, 'Cirugía', 'MEDICO', 0x81DC3392A4B411EFA1E9D05099803C6D);
INSERT INTO `servicio` VALUES (0x81D95604A4B411EFA1E9D05099803C6D, 'Cirugía Plástica', 'MEDICO', 0x81DC3392A4B411EFA1E9D05099803C6D);
INSERT INTO `servicio` VALUES (0x81D95FACA4B411EFA1E9D05099803C6D, 'Clínica Médica', 'MEDICO', 0x81DC3392A4B411EFA1E9D05099803C6D);
INSERT INTO `servicio` VALUES (0x81D97056A4B411EFA1E9D05099803C6D, 'Diagnóstico por Imágenes', 'MEDICO', 0x81DC3392A4B411EFA1E9D05099803C6D);
INSERT INTO `servicio` VALUES (0x81D97A8BA4B411EFA1E9D05099803C6D, 'Dirección', 'ADMINISTRATIVO', 0x81DC3392A4B411EFA1E9D05099803C6D);
INSERT INTO `servicio` VALUES (0x81D98A7EA4B411EFA1E9D05099803C6D, 'Enfermería', 'ENFERMERIA', 0x81DC3392A4B411EFA1E9D05099803C6D);
INSERT INTO `servicio` VALUES (0x81D991CCA4B411EFA1E9D05099803C6D, 'Esterilización', 'SERVICIO', 0x81DC390BA4B411EFA1E9D05099803C6D);
INSERT INTO `servicio` VALUES (0x81D999E9A4B411EFA1E9D05099803C6D, 'Facturación', 'ADMINISTRATIVO', 0x81DC3392A4B411EFA1E9D05099803C6D);
INSERT INTO `servicio` VALUES (0x81D9A1ECA4B411EFA1E9D05099803C6D, 'Farmacia', 'SERVICIO', 0x81DC390BA4B411EFA1E9D05099803C6D);
INSERT INTO `servicio` VALUES (0x81D9A908A4B411EFA1E9D05099803C6D, 'Habilitación', 'ADMINISTRATIVO', 0x81DC3392A4B411EFA1E9D05099803C6D);
INSERT INTO `servicio` VALUES (0x81D9B582A4B411EFA1E9D05099803C6D, 'Hemoterapia', 'SERVICIO', 0x81DC390BA4B411EFA1E9D05099803C6D);
INSERT INTO `servicio` VALUES (0x81D9BE7FA4B411EFA1E9D05099803C6D, 'Informática', 'TECNICO', 0x81DC390BA4B411EFA1E9D05099803C6D);
INSERT INTO `servicio` VALUES (0x81D9C803A4B411EFA1E9D05099803C6D, 'Instrumentación Quirúrgica', 'MEDICO', 0x81DC3392A4B411EFA1E9D05099803C6D);
INSERT INTO `servicio` VALUES (0x81D9E0FFA4B411EFA1E9D05099803C6D, 'Kinesiología', 'MEDICO', 0x81DC3392A4B411EFA1E9D05099803C6D);
INSERT INTO `servicio` VALUES (0x81D9E8A4A4B411EFA1E9D05099803C6D, 'Laboratorio', 'SERVICIO', 0x81DC390BA4B411EFA1E9D05099803C6D);
INSERT INTO `servicio` VALUES (0x81D9EEF8A4B411EFA1E9D05099803C6D, 'Lavadero', 'SERVICIO', 0x81DC390BA4B411EFA1E9D05099803C6D);
INSERT INTO `servicio` VALUES (0x81D9F8FFA4B411EFA1E9D05099803C6D, 'Mantenimiento', 'TECNICO', 0x81DC390BA4B411EFA1E9D05099803C6D);
INSERT INTO `servicio` VALUES (0x81DA02EBA4B411EFA1E9D05099803C6D, 'Medicina Legal', 'ADMINISTRATIVO', 0x81DC3392A4B411EFA1E9D05099803C6D);
INSERT INTO `servicio` VALUES (0x81DA0C74A4B411EFA1E9D05099803C6D, 'Neurocirugía', 'MEDICO', 0x81DC3392A4B411EFA1E9D05099803C6D);
INSERT INTO `servicio` VALUES (0x81DA1744A4B411EFA1E9D05099803C6D, 'Nutrición', 'MEDICO', 0x81DC3392A4B411EFA1E9D05099803C6D);
INSERT INTO `servicio` VALUES (0x81DA1DD5A4B411EFA1E9D05099803C6D, 'Personal', 'ADMINISTRATIVO', 0x81DC3392A4B411EFA1E9D05099803C6D);
INSERT INTO `servicio` VALUES (0x81DA2681A4B411EFA1E9D05099803C6D, 'Quirófano', 'MEDICO', 0x81DC3392A4B411EFA1E9D05099803C6D);
INSERT INTO `servicio` VALUES (0x81DA31FFA4B411EFA1E9D05099803C6D, 'Registro Médico (Archivo)', 'ADMINISTRATIVO', 0x81DC3392A4B411EFA1E9D05099803C6D);
INSERT INTO `servicio` VALUES (0x81DA420AA4B411EFA1E9D05099803C6D, 'Salud Mental', 'MEDICO', 0x81DC3392A4B411EFA1E9D05099803C6D);
INSERT INTO `servicio` VALUES (0x81DA4E90A4B411EFA1E9D05099803C6D, 'Secretaría Técnica', 'ADMINISTRATIVO', 0x81DC3392A4B411EFA1E9D05099803C6D);
INSERT INTO `servicio` VALUES (0x81DA5665A4B411EFA1E9D05099803C6D, 'Servicio Social', 'ADMINISTRATIVO', 0x81DC3392A4B411EFA1E9D05099803C6D);
INSERT INTO `servicio` VALUES (0x81DA5EABA4B411EFA1E9D05099803C6D, 'Tecnoeléctrica', 'TECNICO', 0x81DC390BA4B411EFA1E9D05099803C6D);
INSERT INTO `servicio` VALUES (0x81DA6D7EA4B411EFA1E9D05099803C6D, 'Traumatología y Ortopedia', 'MEDICO', 0x81DC3392A4B411EFA1E9D05099803C6D);

-- ----------------------------
-- Table structure for usuario
-- ----------------------------
DROP TABLE IF EXISTS `usuario`;
CREATE TABLE `usuario`  (
  `id` binary(16) NOT NULL,
  `fechaAlta` date NOT NULL,
  `estado` tinyint(1) NOT NULL,
  `cuil` bigint(20) NOT NULL,
  `apellidos` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `nombres` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `sexo` enum('FEMENINO','MASCULINO','OTRO') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `mail` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `tel` bigint(20) NULL DEFAULT NULL,
  `domicilioID` binary(16) NULL DEFAULT NULL,
  `cargoID` binary(16) NULL DEFAULT NULL,
  `servicioID` binary(16) NULL DEFAULT NULL,
  `tipoUsuario` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `passwd` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `profile_image` blob NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `cuil`(`cuil` ASC) USING BTREE,
  UNIQUE INDEX `idx_mail`(`mail` ASC) USING BTREE,
  UNIQUE INDEX `idx_tel`(`tel` ASC) USING BTREE,
  INDEX `domicilioID`(`domicilioID` ASC) USING BTREE,
  INDEX `cargoID`(`cargoID` ASC) USING BTREE,
  INDEX `servicioID`(`servicioID` ASC) USING BTREE,
  INDEX `idx_apellidos`(`apellidos` ASC) USING BTREE,
  INDEX `idx_nombres`(`nombres` ASC) USING BTREE,
  CONSTRAINT `usuario_ibfk_1` FOREIGN KEY (`domicilioID`) REFERENCES `domicilio` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `usuario_ibfk_2` FOREIGN KEY (`cargoID`) REFERENCES `cargo` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `usuario_ibfk_3` FOREIGN KEY (`servicioID`) REFERENCES `servicio` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of usuario
-- ----------------------------
INSERT INTO `usuario` VALUES (0x5F390597A78E11EF9F8FD05099803C6D, '2015-03-01', 1, 20112223331, 'Bonifacio', 'Aguirre', 'MASCULINO', 'bonifacio.a@hmu.com.ar', 3512223344, 0x4F1041F9A52611EFA1E9D05099803C6D, 0x81DADA8CA4B411EFA1E9D05099803C6D, 0x81D97A8BA4B411EFA1E9D05099803C6D, 'Direccion', '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x6BFCC5FAA79211EF9F8FD05099803C6D, '2010-12-15', 1, 27112223333, 'Felicitas', 'Herrera', 'FEMENINO', 'feliherrra@hmu.com.ar', 351223366, 0x81D834A1A4B411EFA1E9D05099803C6D, 0x81DAF048A4B411EFA1E9D05099803C6D, 0x81DA1DD5A4B411EFA1E9D05099803C6D, 'OficinaDePersonal', '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x81DA7594A4B411EFA1E9D05099803C6D, '2024-11-17', 1, 27295554447, 'Balconte', 'Andrea', 'FEMENINO', 'andrea@hmu.com.ar', NULL, 0x81D82E51A4B411EFA1E9D05099803C6D, 0x81DAD1E4A4B411EFA1E9D05099803C6D, 0x81D8F426A4B411EFA1E9D05099803C6D, 'JefaturaDeServicio', '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x81DA7D31A4B411EFA1E9D05099803C6D, '2024-11-17', 1, 27274422442, 'Maestro', 'Silvina', 'FEMENINO', 'smaestro@hmu.com.ar', NULL, 0x81D8334BA4B411EFA1E9D05099803C6D, 0x81DAEA29A4B411EFA1E9D05099803C6D, 0x81D92554A4B411EFA1E9D05099803C6D, 'JefaturaDeServicio', '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x81DA8463A4B411EFA1E9D05099803C6D, '2024-11-17', 1, 20268944448, 'Titarelli', 'Maximiliano', 'MASCULINO', 'drtita@hmu.com.ar', NULL, 0x81D82F1AA4B411EFA1E9D05099803C6D, 0x81DAD1E4A4B411EFA1E9D05099803C6D, 0x81D94C41A4B411EFA1E9D05099803C6D, 'JefaturaDeServicio', '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x81DA8AA8A4B411EFA1E9D05099803C6D, '2024-11-17', 1, 20281324547, 'Morales', 'Juan Ignacio', 'MASCULINO', 'jimorales@hmu.com.ar', NULL, 0x81D826C8A4B411EFA1E9D05099803C6D, 0x81DAD1E4A4B411EFA1E9D05099803C6D, 0x81D97056A4B411EFA1E9D05099803C6D, 'JefaturaDeServicio', '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x81DA944AA4B411EFA1E9D05099803C6D, '2024-11-17', 0, 27224444445, 'Plaza', 'Tania', 'FEMENINO', 'tplaza@hmu.com.ar', NULL, 0x81D82E51A4B411EFA1E9D05099803C6D, 0x81DAD1E4A4B411EFA1E9D05099803C6D, 0x81D98A7EA4B411EFA1E9D05099803C6D, 'JefaturaDeServicio', '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x81DA9AC0A4B411EFA1E9D05099803C6D, '2024-11-17', 1, 20289445441, 'Pérez Cabral', 'Matías', 'MASCULINO', 'mcp@hmu.com.ar', NULL, 0x81D833F3A4B411EFA1E9D05099803C6D, 0x81DAD1E4A4B411EFA1E9D05099803C6D, 0x81D991CCA4B411EFA1E9D05099803C6D, 'JefaturaDeServicio', '$2a$10$.Qr6/kLrQeZFV0UZbztU7.86tSdxZ8tr7iOS9FjDsCvafa3CekXQS', NULL);
INSERT INTO `usuario` VALUES (0x81DAA286A4B411EFA1E9D05099803C6D, '2024-11-17', 1, 27254344447, 'Arancibia', 'María Pía', 'FEMENINO', 'piaarancibia@hmu.com.ar', NULL, 0x81D8230CA4B411EFA1E9D05099803C6D, 0x81DAD1E4A4B411EFA1E9D05099803C6D, 0x81D9A1ECA4B411EFA1E9D05099803C6D, 'JefaturaDeServicio', '$2a$10$xyp9Z4qEIlEnZuX0bgzsCeqM9MiuuVOWOtor8ybnjSZKcorsVqNEu', NULL);
INSERT INTO `usuario` VALUES (0x81DAA89EA4B411EFA1E9D05099803C6D, '2024-11-17', 1, 20276444446, 'Roberts', 'Carlos Fernando', 'MASCULINO', 'ferroberts@hmu.com.ar', NULL, 0x81D834A1A4B411EFA1E9D05099803C6D, 0x81DAD1E4A4B411EFA1E9D05099803C6D, 0x81D9A908A4B411EFA1E9D05099803C6D, 'JefaturaDeServicio', '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x81DAB0A8A4B411EFA1E9D05099803C6D, '2024-11-17', 1, 20220361118, 'Roqué', 'Juan Manuel', 'OTRO', 'jmroque@hmu.com.ar', 3517553799, 0x81D82CEFA4B411EFA1E9D05099803C6D, 0x81DAE2BEA4B411EFA1E9D05099803C6D, 0x81D9BE7FA4B411EFA1E9D05099803C6D, 'JefaturaDeServicio', '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x81DAB7D4A4B411EFA1E9D05099803C6D, '2024-11-17', 0, 27174444446, 'Boqué', 'Alejandra', 'FEMENINO', 'aleboque@hmu.com.ar', NULL, 0x81D82F1AA4B411EFA1E9D05099803C6D, 0x81DAD1E4A4B411EFA1E9D05099803C6D, 0x81DA1744A4B411EFA1E9D05099803C6D, 'JefaturaDeServicio', '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x81DABE35A4B411EFA1E9D05099803C6D, '2024-11-17', 1, 27284644443, 'Maurino', 'Florencia', 'FEMENINO', 'flormaurino@hmu.com.ar', NULL, 0x81D8334BA4B411EFA1E9D05099803C6D, 0x81DAE2BEA4B411EFA1E9D05099803C6D, 0x81DA1DD5A4B411EFA1E9D05099803C6D, 'OficinaDePersonal', '$2a$10$s7YSfGVW3W/IL6vLuElPou3g45SZfoF1djegk/E5BEOjzRB7RRMS6', NULL);
INSERT INTO `usuario` VALUES (0x81DAC47CA4B411EFA1E9D05099803C6D, '2024-11-17', 1, 20124644445, 'Sánchez', 'Omar Wenceslao', 'MASCULINO', 'owsanchez@hmu.com.ar', NULL, 0x81D834A1A4B411EFA1E9D05099803C6D, 0x81DAD1E4A4B411EFA1E9D05099803C6D, 0x81DA6D7EA4B411EFA1E9D05099803C6D, 'JefaturaDeServicio', '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x81DACB86A4B411EFA1E9D05099803C6D, '2024-11-17', 1, 27214434447, 'Vilkelis', 'Andrea', 'FEMENINO', 'avilkelis@hmu.com.ar', NULL, 0x81D8230CA4B411EFA1E9D05099803C6D, 0x81DAD1E4A4B411EFA1E9D05099803C6D, 0x81DA6631A4B411EFA1E9D05099803C6D, 'JefaturaDeServicio', '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x81DC3392A4B411EFA1E9D05099803C6D, '2024-11-17', 1, 20224448885, 'Marino', 'Mariano Gustavo', 'MASCULINO', 'direccion@hmu.com.ar', NULL, 0x81D82CEFA4B411EFA1E9D05099803C6D, 0x81DAD1E4A4B411EFA1E9D05099803C6D, 0x81D97A8BA4B411EFA1E9D05099803C6D, 'Direccion', '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x81DC3656A4B411EFA1E9D05099803C6D, '2024-11-17', 1, 20259993331, 'Huergo Sánchez', 'Federico', 'MASCULINO', 'fedesubdir@hmu.com.ar', NULL, 0x81D826C8A4B411EFA1E9D05099803C6D, 0x81DAD1E4A4B411EFA1E9D05099803C6D, 0x81D97A8BA4B411EFA1E9D05099803C6D, 'Direccion', '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x81DC378FA4B411EFA1E9D05099803C6D, '2024-11-17', 0, 27129997773, 'Longoni', 'Gloria', 'FEMENINO', 'subdirectora@hmu.com.ar', NULL, 0x81D82C3CA4B411EFA1E9D05099803C6D, 0x81DAD1E4A4B411EFA1E9D05099803C6D, 0x81D97A8BA4B411EFA1E9D05099803C6D, 'Direccion', '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x81DC390BA4B411EFA1E9D05099803C6D, '2024-11-17', 1, 20239997772, 'Vitali', 'Fabricio', 'MASCULINO', 'subdireccion@hmu.com.ar', NULL, 0x81D82CEFA4B411EFA1E9D05099803C6D, 0x81DAD1E4A4B411EFA1E9D05099803C6D, 0x81D97A8BA4B411EFA1E9D05099803C6D, 'Direccion', '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x81DC5426A4B411EFA1E9D05099803C6D, '2024-11-17', 1, 20124543421, 'Aniceto', 'Juan', 'MASCULINO', 'janiceto@hmu.com.ar', NULL, 0x81D8329EA4B411EFA1E9D05099803C6D, 0x81DAD1E4A4B411EFA1E9D05099803C6D, 0x81D97056A4B411EFA1E9D05099803C6D, 'Empleado', '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x81DC56BDA4B411EFA1E9D05099803C6D, '2024-11-17', 1, 20554644448, 'Garzón', 'Baltazar', 'MASCULINO', 'baltig@hmu.com.ar', NULL, 0x81D82CEFA4B411EFA1E9D05099803C6D, 0x81DAD1E4A4B411EFA1E9D05099803C6D, 0x81DA1DD5A4B411EFA1E9D05099803C6D, 'Empleado', '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x81DC5A62A4B411EFA1E9D05099803C6D, '2024-11-17', 1, 20154786445, 'Taborda', 'Pedro', 'MASCULINO', 'pltaborda@hmu.com.ar', NULL, 0x81D826C8A4B411EFA1E9D05099803C6D, 0x81DAD1E4A4B411EFA1E9D05099803C6D, 0x81DA1DD5A4B411EFA1E9D05099803C6D, 'Empleado', '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x81DC5D14A4B411EFA1E9D05099803C6D, '2024-11-17', 1, 27554664563, 'Vargas Ruíz', 'María Laura', 'FEMENINO', 'mlvargas@hmu.com.ar', NULL, 0x81D8230CA4B411EFA1E9D05099803C6D, 0x81DAD1E4A4B411EFA1E9D05099803C6D, 0x81D991CCA4B411EFA1E9D05099803C6D, 'Empleado', '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x81DC5F91A4B411EFA1E9D05099803C6D, '2024-11-17', 1, 27554685443, 'Vignetta', 'María Celeste', 'FEMENINO', 'mcv@hmu.com.ar', NULL, 0x81D831E6A4B411EFA1E9D05099803C6D, 0x81DAD1E4A4B411EFA1E9D05099803C6D, 0x81D94C41A4B411EFA1E9D05099803C6D, 'Empleado', '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x81DC6273A4B411EFA1E9D05099803C6D, '2024-11-17', 1, 27554567443, 'Vivas', 'Alicia', 'FEMENINO', 'avivas@hmu.com.ar', NULL, 0x81D83077A4B411EFA1E9D05099803C6D, 0x81DAD1E4A4B411EFA1E9D05099803C6D, 0x81D9A908A4B411EFA1E9D05099803C6D, 'Empleado', '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x81DC64ABA4B411EFA1E9D05099803C6D, '2024-11-17', 1, 20554649743, 'Canga Castellanos', 'Matías Quique', 'MASCULINO', 'lestat@hmu.com.ar', NULL, 0x81D83077A4B411EFA1E9D05099803C6D, 0x81DAEA29A4B411EFA1E9D05099803C6D, 0x81D9BE7FA4B411EFA1E9D05099803C6D, 'Empleado', '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x81DC668DA4B411EFA1E9D05099803C6D, '2024-11-17', 1, 24574741745, 'Garay', 'Mauricio Elio', 'OTRO', 'mgaray@hmu.com.ar', NULL, 0x81D8230CA4B411EFA1E9D05099803C6D, 0x81DAEA29A4B411EFA1E9D05099803C6D, 0x81D9BE7FA4B411EFA1E9D05099803C6D, 'Empleado', '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x81DC68CCA4B411EFA1E9D05099803C6D, '2024-11-17', 1, 27556786453, 'Usandivares', 'Eva Patricia', 'FEMENINO', 'epu@hmu.com.ar', NULL, 0x81D82ADDA4B411EFA1E9D05099803C6D, 0x81DAD1E4A4B411EFA1E9D05099803C6D, 0x81DA1DD5A4B411EFA1E9D05099803C6D, 'Empleado', '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x81DC6B32A4B411EFA1E9D05099803C6D, '2024-11-17', 1, 20454676443, 'Tolay', 'Edith', 'FEMENINO', 'etolay@hmu.com.ar', NULL, 0x81D82F1AA4B411EFA1E9D05099803C6D, 0x81DAD1E4A4B411EFA1E9D05099803C6D, 0x81D98A7EA4B411EFA1E9D05099803C6D, 'Empleado', '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x81DC6D09A4B411EFA1E9D05099803C6D, '2024-11-17', 1, 20554543453, 'Terrieris', 'José Luis', 'MASCULINO', 'jterrieris@hmu.com.ar', NULL, 0x81D8230CA4B411EFA1E9D05099803C6D, 0x81DAD1E4A4B411EFA1E9D05099803C6D, 0x81D98A7EA4B411EFA1E9D05099803C6D, 'Empleado', '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x81DC7318A4B411EFA1E9D05099803C6D, '2024-11-17', 1, 27540678443, 'Tarifa', 'Claudia', 'FEMENINO', 'clautarifa@hmu.com.ar', NULL, 0x81D8284CA4B411EFA1E9D05099803C6D, 0x81DAD1E4A4B411EFA1E9D05099803C6D, 0x81D98A7EA4B411EFA1E9D05099803C6D, 'Empleado', '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x81DC75EFA4B411EFA1E9D05099803C6D, '2024-11-17', 1, 27554560863, 'Tapia', 'Erica', 'FEMENINO', 'erikatapia@hmu.com.ar', NULL, 0x81D82E51A4B411EFA1E9D05099803C6D, 0x81DAD1E4A4B411EFA1E9D05099803C6D, 0x81DA6631A4B411EFA1E9D05099803C6D, 'Empleado', '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x81DC7813A4B411EFA1E9D05099803C6D, '2024-11-17', 1, 24784044443, 'Tampares', 'Demetrio', 'OTRO', 'demetam@hmu.com.ar', NULL, 0x81D8284CA4B411EFA1E9D05099803C6D, 0x81DAD1E4A4B411EFA1E9D05099803C6D, 0x81DA6631A4B411EFA1E9D05099803C6D, 'Empleado', '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x81DC7AA2A4B411EFA1E9D05099803C6D, '2024-11-17', 1, 20526456443, 'Suizer', 'Alejandr', 'MASCULINO', 'amsuizer@hmu.com.ar', NULL, 0x81D82C3CA4B411EFA1E9D05099803C6D, 0x81DAD1E4A4B411EFA1E9D05099803C6D, 0x81D94C41A4B411EFA1E9D05099803C6D, 'Empleado', '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x81DC7D9BA4B411EFA1E9D05099803C6D, '2024-11-17', 1, 20554897343, 'Suárez', 'David', 'MASCULINO', 'dsuarez@hmu.com.ar', NULL, 0x81D8334BA4B411EFA1E9D05099803C6D, 0x81DAEA29A4B411EFA1E9D05099803C6D, 0x81D92554A4B411EFA1E9D05099803C6D, 'Empleado', '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x81DC8235A4B411EFA1E9D05099803C6D, '2024-11-17', 1, 20554674843, 'Rius', 'Pedro', 'MASCULINO', 'pedrorius@hmu.com.ar', NULL, 0x81D8329EA4B411EFA1E9D05099803C6D, 0x81DAD1E4A4B411EFA1E9D05099803C6D, 0x81D97056A4B411EFA1E9D05099803C6D, 'Empleado', '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x81DC840FA4B411EFA1E9D05099803C6D, '2024-11-17', 1, 20558244443, 'Rabbat', 'Damian', 'MASCULINO', 'drabbat@hmu.com.ar', NULL, 0x81D831E6A4B411EFA1E9D05099803C6D, 0x81DAD1E4A4B411EFA1E9D05099803C6D, 0x81DA6D7EA4B411EFA1E9D05099803C6D, 'Empleado', '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x81DC872FA4B411EFA1E9D05099803C6D, '2024-11-17', 1, 20567974443, 'Puig', 'Ismael', 'MASCULINO', 'ipuig@hmu.com.ar', NULL, 0x81D82E51A4B411EFA1E9D05099803C6D, 0x81DAD1E4A4B411EFA1E9D05099803C6D, 0x81DA6D7EA4B411EFA1E9D05099803C6D, 'Empleado', '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x81DC896CA4B411EFA1E9D05099803C6D, '2024-11-17', 1, 20456645843, 'Pascolo', 'Diego', 'MASCULINO', 'dpascolo@hmu.com.ar', NULL, 0x81D8334BA4B411EFA1E9D05099803C6D, 0x81DAD1E4A4B411EFA1E9D05099803C6D, 0x81D94C41A4B411EFA1E9D05099803C6D, 'Empleado', '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x81DC8B2BA4B411EFA1E9D05099803C6D, '2024-11-17', 1, 24554978443, 'Bustos', 'Sebastián', 'OTRO', 'sebustos@hmu.com.ar', NULL, 0x81D834A1A4B411EFA1E9D05099803C6D, 0x81DAEA29A4B411EFA1E9D05099803C6D, 0x81D92554A4B411EFA1E9D05099803C6D, 'Empleado', '$2a$12$x9SDga8sk3DyjvdeIwkhl.2e9wcWHewfHEFQFKkFp1.FiAKcyRZUG', NULL);
INSERT INTO `usuario` VALUES (0xC6CDA736A79611EF9F8FD05099803C6D, '2021-09-01', 1, 20112223334, 'Luis', 'Masson', 'MASCULINO', 'lmasson@hmu.com.ar', 3512223377, 0x81D83077A4B411EFA1E9D05099803C6D, 0x81DAEA29A4B411EFA1E9D05099803C6D, 0x81D9BE7FA4B411EFA1E9D05099803C6D, 'Empleado', '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0xF9AF8F9AA79011EF9F8FD05099803C6D, '2012-07-01', 1, 20112223332, 'Valentino', 'Marquez', 'MASCULINO', 'vale.mar@hmu.com.ar', 351223355, 0x4FFE4ABFA52511EFA1E9D05099803C6D, 0x81DAE2BEA4B411EFA1E9D05099803C6D, 0x81D97A8BA4B411EFA1E9D05099803C6D, 'JefaturaDeServicio', '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);

-- ----------------------------
-- Table structure for usuario_rol
-- ----------------------------
DROP TABLE IF EXISTS `usuario_rol`;
CREATE TABLE `usuario_rol`  (
  `usuario_id` binary(16) NOT NULL,
  `rol_id` binary(16) NOT NULL,
  PRIMARY KEY (`usuario_id`, `rol_id`) USING BTREE,
  INDEX `idx_usuario`(`usuario_id` ASC) USING BTREE,
  INDEX `idx_rol`(`rol_id` ASC) USING BTREE,
  CONSTRAINT `usuario_rol_ibfk_1` FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `usuario_rol_ibfk_2` FOREIGN KEY (`rol_id`) REFERENCES `rol` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of usuario_rol
-- ----------------------------
INSERT INTO `usuario_rol` VALUES (0x5F390597A78E11EF9F8FD05099803C6D, 0x81E2E98DA4B411EFA1E9D05099803C6D);
INSERT INTO `usuario_rol` VALUES (0x6BFCC5FAA79211EF9F8FD05099803C6D, 0x81E2DFFBA4B411EFA1E9D05099803C6D);
INSERT INTO `usuario_rol` VALUES (0x81DAB0A8A4B411EFA1E9D05099803C6D, 0x81E2C998A4B411EFA1E9D05099803C6D);
INSERT INTO `usuario_rol` VALUES (0x81DAB0A8A4B411EFA1E9D05099803C6D, 0x81E2D54EA4B411EFA1E9D05099803C6D);
INSERT INTO `usuario_rol` VALUES (0x81DABE35A4B411EFA1E9D05099803C6D, 0x81E2C998A4B411EFA1E9D05099803C6D);
INSERT INTO `usuario_rol` VALUES (0x81DABE35A4B411EFA1E9D05099803C6D, 0x81E2D54EA4B411EFA1E9D05099803C6D);
INSERT INTO `usuario_rol` VALUES (0x81DABE35A4B411EFA1E9D05099803C6D, 0x81E2DFFBA4B411EFA1E9D05099803C6D);
INSERT INTO `usuario_rol` VALUES (0x81DC3392A4B411EFA1E9D05099803C6D, 0x81E2D54EA4B411EFA1E9D05099803C6D);
INSERT INTO `usuario_rol` VALUES (0x81DC3392A4B411EFA1E9D05099803C6D, 0x81E2E98DA4B411EFA1E9D05099803C6D);
INSERT INTO `usuario_rol` VALUES (0xF9AF8F9AA79011EF9F8FD05099803C6D, 0x81E2D54EA4B411EFA1E9D05099803C6D);

-- ----------------------------
-- Function structure for BIN_TO_UUID
-- ----------------------------
DROP FUNCTION IF EXISTS `BIN_TO_UUID`;
delimiter ;;
CREATE FUNCTION `BIN_TO_UUID`(b BINARY(16))
 RETURNS char(36) CHARSET ascii COLLATE ascii_general_ci
  DETERMINISTIC
BEGIN
   DECLARE hexStr CHAR(32);
   SET hexStr = HEX(b);
   RETURN LOWER(CONCAT(
     SUBSTR(hexStr, 1, 8), '-',      -- aaaaaaaa
     SUBSTR(hexStr, 9, 4), '-',      -- bbbb
     SUBSTR(hexStr, 13, 4), '-',     -- cccc
     SUBSTR(hexStr, 17, 4), '-',     -- dddd
     SUBSTR(hexStr, 21, 12)          -- eeeeeeeeeeee
  ));
END
;;
delimiter ;

-- ----------------------------
-- Function structure for UUID_TO_BIN
-- ----------------------------
DROP FUNCTION IF EXISTS `UUID_TO_BIN`;
delimiter ;;
CREATE FUNCTION `UUID_TO_BIN`(uuid CHAR(36))
 RETURNS binary(16)
  DETERMINISTIC
BEGIN
  RETURN UNHEX(CONCAT(
    SUBSTRING(uuid, 1, 8),      -- aaaaaaaa
    SUBSTRING(uuid, 10, 4),     -- bbbb
    SUBSTRING(uuid, 15, 4),     -- cccc
    SUBSTRING(uuid, 20, 4),     -- dddd
    SUBSTRING(uuid, 25, 12)     -- eeeeeeeeeeee
  ));
END
;;
delimiter ;

-- ----------------------------
-- Triggers structure for table empleado
-- ----------------------------
DROP TRIGGER IF EXISTS `trg_check_servicio_consistente_before_insert`;
delimiter ;;
CREATE TRIGGER `trg_check_servicio_consistente_before_insert` BEFORE INSERT ON `empleado` FOR EACH ROW BEGIN
    DECLARE empleadoServicioID BINARY(16);
    DECLARE jefaturaServicioID BINARY(16);

    -- Obtener el servicioID del Empleado desde la tabla Usuario
    SELECT servicioID INTO empleadoServicioID
    FROM Usuario
    WHERE id = NEW.id;

    -- Verificar si NEW.jefaturaID no es NULL
    IF NEW.jefaturaID IS NOT NULL THEN
        -- Obtener el servicioID de la JefaturaDeServicio desde la tabla Usuario
        SELECT servicioID INTO jefaturaServicioID
        FROM Usuario
        WHERE id = NEW.jefaturaID;

        -- Si no se encuentra el servicioID de la JefaturaDeServicio, generar un error
        IF jefaturaServicioID IS NULL THEN
            SIGNAL SQLSTATE '45000' 
            SET MESSAGE_TEXT = 'La JefaturaDeServicio especificada no tiene un servicio asignado.';
        END IF;

        -- Verificar si los servicioID coinciden
        IF empleadoServicioID != jefaturaServicioID THEN
            SIGNAL SQLSTATE '45000' 
            SET MESSAGE_TEXT = 'El servicioID del Empleado debe coincidir con el servicioID de la JefaturaDeServicio.';
        END IF;
    END IF;
    -- Si NEW.jefaturaID es NULL, no se realiza la validación
END
;;
delimiter ;

-- ----------------------------
-- Triggers structure for table empleado
-- ----------------------------
DROP TRIGGER IF EXISTS `trg_check_servicio_consistente_before_update`;
delimiter ;;
CREATE TRIGGER `trg_check_servicio_consistente_before_update` BEFORE UPDATE ON `empleado` FOR EACH ROW BEGIN
    DECLARE empleadoServicioID BINARY(16);
    DECLARE jefaturaServicioID BINARY(16);

    -- Obtener el servicioID del Empleado desde la tabla Usuario
    SELECT servicioID INTO empleadoServicioID
    FROM Usuario
    WHERE id = NEW.id;

    -- Verificar si NEW.jefaturaID no es NULL
    IF NEW.jefaturaID IS NOT NULL THEN
        -- Obtener el servicioID de la JefaturaDeServicio desde la tabla Usuario
        SELECT servicioID INTO jefaturaServicioID
        FROM Usuario
        WHERE id = NEW.jefaturaID;

        -- Si no se encuentra el servicioID de la JefaturaDeServicio, generar un error
        IF jefaturaServicioID IS NULL THEN
            SIGNAL SQLSTATE '45000' 
            SET MESSAGE_TEXT = 'La JefaturaDeServicio especificada no tiene un servicio asignado.';
        END IF;

        -- Verificar si los servicioID coinciden
        IF empleadoServicioID != jefaturaServicioID THEN
            SIGNAL SQLSTATE '45000' 
            SET MESSAGE_TEXT = 'El servicioID del Empleado debe coincidir con el servicioID de la JefaturaDeServicio.';
        END IF;
    END IF;
    -- Si NEW.jefaturaID es NULL, no se realiza la validación
END
;;
delimiter ;

SET FOREIGN_KEY_CHECKS = 1;
