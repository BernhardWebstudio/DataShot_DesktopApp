-- Add version column for Hibernate optimistic locking on Specimen
ALTER TABLE Specimen ADD COLUMN version INT NOT NULL DEFAULT 0;

-- Register version in allowed_version table
INSERT INTO `allowed_version` (`version`)
VALUES ('2.0.4');
