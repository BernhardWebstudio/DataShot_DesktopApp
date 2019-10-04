CREATE TABLE `flyway_schema_history` (
                                         `installed_rank` int(11) NOT NULL,
                                         `version` varchar(50) DEFAULT NULL,
                                         `description` varchar(200) NOT NULL,
                                         `type` varchar(20) NOT NULL,
                                         `script` varchar(1000) NOT NULL,
                                         `checksum` int(11) DEFAULT NULL,
                                         `installed_by` varchar(100) NOT NULL,
                                         `installed_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                         `execution_time` int(11) NOT NULL,
                                         `success` tinyint(1) NOT NULL,
                                         PRIMARY KEY (`installed_rank`),
                                         KEY `flyway_schema_history_s_idx` (`success`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `flyway_schema_history`
(`installed_rank`,
 `version`,
 `description`,
 `type`,
 `script`,
 `checksum`,
 `installed_by`,
 `installed_on`,
 `execution_time`,
 `success`)
VALUES
('1', '1', '<< Flyway Baseline >>', 'BASELINE', '<< Flyway Baseline >>', NULL, 'root', '2019-09-01 18:32:57', '0', '1');

INSERT INTO `flyway_schema_history` (installed_rank, version, description, type, script, installed_by, execution_time, success)
    VALUE (
           3, "1.9.0", "flyway baseline", "SQL", "V1.9.0__flyway_baseline.sql", "root", 0, 1
    );
