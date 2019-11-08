-- tell DataShot's own system that we are already at v. 1.9.0
insert into
  allowed_version (version)
VALUES
  ("1.9");
-- add keys/indices to speed some things up
CREATE INDEX SpecimenBarcodeIdx ON Specimen (Barcode);
CREATE INDEX SpecimenWorkflowStatusIdx ON Specimen (WorkFlowStatus);
CREATE INDEX SpecimenFamilyIdx ON Specimen (Family, Subfamily);
CREATE INDEX ImagePathIdx ON Image (Path);
CREATE INDEX ImageRawBarcode ON Image (RawBarcode);
