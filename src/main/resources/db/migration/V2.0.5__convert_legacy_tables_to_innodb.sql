-- Convert legacy MyISAM tables to InnoDB to support ACID transactions,
-- row-level locking, foreign key integrity, and MySQL GTID consistency.
-- Statement violates GTID consistency (MySQL error 1785) occurs when
-- non-transactional tables (MyISAM) are updated in transactional statements.
ALTER TABLE `LAT_LONG` ENGINE=InnoDB;
ALTER TABLE `Specimen_Part` ENGINE=InnoDB;
ALTER TABLE `Specimen_Part_Attribute` ENGINE=InnoDB;
ALTER TABLE `HIGHER_TAXON` ENGINE=InnoDB;
ALTER TABLE `MCZBASE_AUTH_AGENT_NAME` ENGINE=InnoDB;
ALTER TABLE `MCZBASE_GEOG_AUTH_REC` ENGINE=InnoDB;
ALTER TABLE `UNIT_TRAY_LABEL` ENGINE=InnoDB;
ALTER TABLE `Users` ENGINE=InnoDB;
ALTER TABLE `Template` ENGINE=InnoDB;
ALTER TABLE `Label` ENGINE=InnoDB;
ALTER TABLE `LabelTag` ENGINE=InnoDB;
ALTER TABLE `Tag` ENGINE=InnoDB;

-- Register version in allowed_version table
INSERT INTO `allowed_version` (`version`)
VALUES ('2.0.5');
