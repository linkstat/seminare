-- Borrado total de las tablas de la BD
USE aromito;

-- Desactiva la verificación de claves foráneas
SET FOREIGN_KEY_CHECKS = 0;

-- Inicializar la variable de tablas como NULL
SET @tables = NULL;

-- Obtiene la lista de todas las tablas
SELECT GROUP_CONCAT('`', table_name, '`') INTO @tables
FROM information_schema.tables
WHERE table_schema = (SELECT DATABASE());

-- Si existen tablas, ejecuta el DROP
SET @tables = CONCAT('DROP TABLE IF EXISTS ', @tables);
PREPARE stmt FROM @tables;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

 -- Vuelve a activar la verificación de claves foráneas
SET FOREIGN_KEY_CHECKS = 1;
