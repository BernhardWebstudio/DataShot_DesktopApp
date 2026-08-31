-- Indexes on child tables for foreign key lookups and batch loading
CREATE INDEX idx_other_numbers_specimenid ON OTHER_NUMBERS(SpecimenId);
CREATE INDEX idx_other_numbers_type ON OTHER_NUMBERS(NumberType, OTHER_NUMBER);
CREATE INDEX idx_collector_specimenid ON Collector(SpecimenId);
CREATE INDEX idx_tracking_specimenid ON Tracking(SpecimenId);
CREATE INDEX idx_specimen_part_specimenid ON Specimen_Part(SpecimenId);
CREATE INDEX idx_external_history_specimenid ON external_history(specimenId);

-- Indexes on Specimen for common search filter fields
CREATE INDEX idx_specimen_taxonomy ON Specimen(Family, Genus, SpecificEpithet);
CREATE INDEX idx_specimen_workflow ON Specimen(WorkFlowStatus);
CREATE INDEX idx_specimen_date_updated ON Specimen(DateLastUpdated);
CREATE INDEX idx_specimen_higher_order ON Specimen(HigherOrder);
CREATE INDEX idx_specimen_country ON Specimen(Country);

-- Register version in allowed_version table
INSERT INTO `allowed_version` (`version`)
VALUES ('2.0.3');
