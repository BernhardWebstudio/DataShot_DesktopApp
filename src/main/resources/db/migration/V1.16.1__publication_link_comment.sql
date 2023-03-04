ALTER TABLE Specimen
    ADD `CitedInPublicationLink` TEXT(1200) NULL DEFAULT NULL;
ALTER TABLE Specimen
    ADD `CitedInPublicationComment` TEXT(1200) NULL DEFAULT NULL;
INSERT INTO `allowed_version` (`version`)
VALUES
    ('1.16.0');
INSERT INTO `allowed_version` (`version`)
VALUES
    ('1.16.1');
