/**
 * Remove necessity of non-null specimen ids.
 * Gives us more possibilities to configure Hibernate delete/cleanup/cascade etc.
 */
ALTER TABLE Determination Modify SpecimenId bigint null;
ALTER TABLE LAT_LONG Modify SpecimenId bigint null;
ALTER TABLE Collector Modify SpecimenId bigint null;
ALTER TABLE Specimen_Part Modify SpecimenId bigint null;
ALTER TABLE Image Modify SpecimenId bigint null;
ALTER TABLE Tracking Modify SpecimenId bigint null;
ALTER TABLE external_history Modify SpecimenId bigint null;
-- tell DataShot's own system what our current version is
INSERT INTO allowed_version (version) VALUES ("1.10");