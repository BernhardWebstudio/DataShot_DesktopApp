ALTER TABLE Specimen
ADD `GBIF_taxon_id` VARCHAR(45) NULL DEFAULT '';
INSERT INTO `allowed_version` (`version`)
VALUES ('1.18.1');
