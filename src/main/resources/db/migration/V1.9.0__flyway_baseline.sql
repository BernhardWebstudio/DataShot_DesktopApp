-- tell DataShot's own system that we are already at v. 1.9.0
insert into
  allowed_version (version)
VALUES
  ("1.9");
CREATE TABLE "flyway_schema_history" (
    "installed_rank" int (11) NOT NULL,
    "version" varchar (50) DEFAULT NULL,
    "description" varchar (200) NOT NULL,
    "type" varchar (20) NOT NULL,
    "script" varchar (1000) NOT NULL,
    "checksum" int (11) DEFAULT NULL,
    "installed_by" varchar (100) NOT NULL,
    "installed_on" timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "execution_time" int (11) NOT NULL,
    "success" tinyint (1) NOT NULL,
    PRIMARY KEY ("installed_rank"),
    KEY "flyway_schema_history_s_idx" ("success")
  ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;
-- tell flyway that we are already at v. 1.9.0
INSERT INTO
  flyway_schema_history (
    installed_rank,
    version,
    description,
    type,
    script,
    installed_by,
    execution_time,
    success
  )
VALUES
  (
    3,
    "1.9.0",
    "flyway_baseline",
    "SQL",
    "V1.9.0__flyway_baseline.sql",
    "root",
    0,
    1
  );
-- add keys/indices to speed some things up
CREATE INDEX SpecimenBarcodeIdx ON Specimen (Barcode);
CREATE INDEX SpecimenWorkflowStatusIdx ON Specimen (WorkFlowStatus);
CREATE INDEX SpecimenFamilyIdx ON Specimen (Family, Subfamily);
CREATE INDEX ImagePathIdx ON Image (Path);
CREATE INDEX ImageRawBarcode ON Image (RawBarcode);
