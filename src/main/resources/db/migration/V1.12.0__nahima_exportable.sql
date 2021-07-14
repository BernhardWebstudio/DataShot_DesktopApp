#-- the ISO code for primary divison
ALTER TABLE Specimen
    ADD `ISO_3166_2_primary_division` varchar(11) NULL DEFAULT NULL;
#-- the mention whether the Specimen is already exported
ALTER TABLE Specimen
    ADD COLUMN `nahimaExported` TINYINT NULL DEFAULT 0;