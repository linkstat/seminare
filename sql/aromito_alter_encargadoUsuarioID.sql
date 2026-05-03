-- ----------------------------------------------------------------------------
-- Migración: agregar Servicio.encargadoUsuarioID
--
-- Aplicar sobre BDs existentes que se crearon con el DDL previo a la
-- introducción del campo. Para BDs nuevas, no es necesario: el DDL ya lo
-- incluye y el seed CRUD lo pobla automáticamente.
--
-- El campo apunta al Usuario que actualmente concentra autorizaciones,
-- rechazos y observaciones de memos del servicio. Normalmente coincide con
-- la jefatura de servicio; en ausencia de la jefatura puede asignarse a
-- otro empleado del servicio.
-- ----------------------------------------------------------------------------

ALTER TABLE Servicio
    ADD COLUMN encargadoUsuarioID UUID NULL;

ALTER TABLE Servicio
    ADD CONSTRAINT fk_servicio_encargado
    FOREIGN KEY (encargadoUsuarioID) REFERENCES Usuario(id);

-- Inicializar con el jefe actual de cada servicio (si lo hay).
UPDATE Servicio s
   SET encargadoUsuarioID = sjs.empleadoID
  FROM Servicio_JefaturaDeServicio sjs
 WHERE s.id = sjs.servicioID;
