-- ----------------------------------------------------------------------------
-- Migración RFS02: Diagramación de servicios
--
-- Aplicar sobre BDs existentes que se crearon antes de habilitar el módulo
-- de diagramación. Las tablas JornadaLaboral / DiagramaDeServicio /
-- Planificacion existían como andamiaje anémico (sólo se las TRUNCA en el
-- script de CRUD, nunca se las sembró), por lo que esta migración es segura:
-- no hay datos que preservar.
--
-- Cambios:
--   1. ENUMs estado_diagrama y tipo_jornada (convención lower_snake / VALORES).
--   2. Se retira la tabla puente ternaria Planificacion: JornadaLaboral pasa
--      a referenciar directamente empleado + diagrama + fecha + tipo
--      ("una fila por empleado por día").
--   3. DiagramaDeServicio: estado tipado como ENUM + auditoría + version
--      (concurrencia optimista) + comentarios de observación.
--   4. Servicio: placeholder dotacionMinima (cobertura mínima, sin uso en
--      pase 1; el DiagramaValidator la aprovechará más adelante).
-- ----------------------------------------------------------------------------

-- 1. Tipos enumerados ---------------------------------------------------------
DO $$ BEGIN
    CREATE TYPE estado_diagrama AS ENUM
        ('BORRADOR', 'PENDIENTE_APROBACION', 'APROBADO', 'OBSERVADO');
EXCEPTION WHEN duplicate_object THEN NULL;
END $$;

DO $$ BEGIN
    CREATE TYPE tipo_jornada AS ENUM
        ('TURNO_NORMAL', 'GUARDIA_ACTIVA', 'GUARDIA_PASIVA',
         'FRANCO', 'FRANCO_COMPENSATORIO', 'LICENCIA');
EXCEPTION WHEN duplicate_object THEN NULL;
END $$;

-- 2. Retiro de la tabla puente Planificacion ---------------------------------
-- (se retira ANTES de tocar JornadaLaboral/DiagramaDeServicio por sus FKs)
DROP TABLE IF EXISTS Planificacion;

-- 3. JornadaLaboral: absorber empleado/diagrama/fecha/tipo -------------------
ALTER TABLE JornadaLaboral
    ADD COLUMN IF NOT EXISTS diagramaID UUID,
    ADD COLUMN IF NOT EXISTS empleadoID UUID,
    ADD COLUMN IF NOT EXISTS fecha DATE,
    ADD COLUMN IF NOT EXISTS tipo tipo_jornada,
    ADD COLUMN IF NOT EXISTS observaciones TEXT NULL;

-- ingreso/egreso pasan a NULLABLE: un franco o licencia no tienen horario
ALTER TABLE JornadaLaboral ALTER COLUMN fechaIngreso DROP NOT NULL;
ALTER TABLE JornadaLaboral ALTER COLUMN fechaEgreso  DROP NOT NULL;

-- restricciones (tablas vacías: se pueden imponer NOT NULL sin default)
ALTER TABLE JornadaLaboral ALTER COLUMN diagramaID SET NOT NULL;
ALTER TABLE JornadaLaboral ALTER COLUMN empleadoID SET NOT NULL;
ALTER TABLE JornadaLaboral ALTER COLUMN fecha      SET NOT NULL;
ALTER TABLE JornadaLaboral ALTER COLUMN tipo       SET NOT NULL;

ALTER TABLE JornadaLaboral
    ADD CONSTRAINT fk_jornada_diagrama
        FOREIGN KEY (diagramaID) REFERENCES DiagramaDeServicio(id) ON DELETE CASCADE,
    ADD CONSTRAINT fk_jornada_empleado
        FOREIGN KEY (empleadoID) REFERENCES Empleado(id);

CREATE INDEX IF NOT EXISTS idx_jornada_diagrama ON JornadaLaboral(diagramaID);
CREATE INDEX IF NOT EXISTS idx_jornada_empleado_fecha ON JornadaLaboral(empleadoID, fecha);

-- 4. DiagramaDeServicio: estado tipado + auditoría + version -----------------
-- estado VARCHAR(50) -> estado_diagrama (tabla vacía: cast directo seguro)
ALTER TABLE DiagramaDeServicio
    ALTER COLUMN estado TYPE estado_diagrama USING estado::estado_diagrama;
ALTER TABLE DiagramaDeServicio
    ALTER COLUMN estado SET DEFAULT 'BORRADOR';

ALTER TABLE DiagramaDeServicio
    ADD COLUMN IF NOT EXISTS version INTEGER NOT NULL DEFAULT 0,
    ADD COLUMN IF NOT EXISTS creadoPorID UUID,
    ADD COLUMN IF NOT EXISTS createdAt TIMESTAMP NOT NULL DEFAULT now(),
    ADD COLUMN IF NOT EXISTS updatedAt TIMESTAMP NOT NULL DEFAULT now(),
    ADD COLUMN IF NOT EXISTS aprobadoPorID UUID NULL,
    ADD COLUMN IF NOT EXISTS fechaAprobacion TIMESTAMP NULL,
    ADD COLUMN IF NOT EXISTS comentariosObservacion TEXT NULL;

ALTER TABLE DiagramaDeServicio ALTER COLUMN creadoPorID SET NOT NULL;

ALTER TABLE DiagramaDeServicio
    ADD CONSTRAINT fk_diagrama_creadopor
        FOREIGN KEY (creadoPorID) REFERENCES Usuario(id),
    ADD CONSTRAINT fk_diagrama_aprobadopor
        FOREIGN KEY (aprobadoPorID) REFERENCES Usuario(id),
    ADD CONSTRAINT chk_diagrama_rango
        CHECK (fechaFin >= fechaInicio);

CREATE INDEX IF NOT EXISTS idx_diagrama_servicio ON DiagramaDeServicio(servicioID);

-- 5. Servicio: placeholder de cobertura mínima -------------------------------
ALTER TABLE Servicio
    ADD COLUMN IF NOT EXISTS dotacionMinima INTEGER NULL;
