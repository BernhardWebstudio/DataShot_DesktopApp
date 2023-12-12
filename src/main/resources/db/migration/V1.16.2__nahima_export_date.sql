ALTER TABLE Specimen
    ADD `DateLastNahimaUpdated` TIMESTAMP NULL DEFAULT NULL;
INSERT INTO `allowed_version` (`version`)
VALUES ('1.16.2');