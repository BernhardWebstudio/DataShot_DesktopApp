ALTER TABLE Image
    ADD `RawQR` TEXT(65535) NULL DEFAULT NULL;
ALTER TABLE Specimen
    ADD `HigherOrder` VARCHAR(40) NULL DEFAULT '';
