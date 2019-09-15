package edu.harvard.mcz.imagecapture;

import org.flywaydb.core.Flyway;

public class DatabaseMigratorApp {
    public static void main(String[] args) {
        // Create the Flyway instance and point it to the database
        Flyway flyway = Flyway.configure().dataSource("jdbc:mysql://localhost:3306/lepidoptera?serverTimezone=Europe/Zurich", "root", "HVIfYzsp").load();

        // Start the migration
        flyway.migrate();
    }
}
