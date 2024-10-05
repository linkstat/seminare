-- Borrado total de las tablas de la BD
USE aromito;

-- Desactiva la verificación de claves foráneas
SET FOREIGN_KEY_CHECKS = 0;

-- DROP todas las tablas
DROP TABLE IF EXISTS Servicio, Horario, Empleado, JefaturaDeServicio, OficinaDePersonal, HorasExtras, Novedad, DiagramaDeServicio, ParteDiario;

 -- Vuelve a activar la verificación de claves foráneas
SET FOREIGN_KEY_CHECKS = 1;
