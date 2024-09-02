package compasso.com.br.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseSetup {
    private static final String URL = "jdbc:mysql://localhost:3306/your_database";
    private static final String USER = "your_username";
    private static final String PASSWORD = "your_password";

    public static void setupDatabase() {
        String createTablesSQL =
                "CREATE TABLE IF NOT EXISTS parking_spot (" +
                        "    id INT AUTO_INCREMENT PRIMARY KEY," +
                        "    number INT NOT NULL," +
                        "    occupied BOOLEAN NOT NULL DEFAULT FALSE," +
                        "    reserved BOOLEAN NOT NULL DEFAULT FALSE," +
                        "    UNIQUE (number)" +
                        ");" +
                        "CREATE TABLE IF NOT EXISTS vehicle (" +
                        "    id INT AUTO_INCREMENT PRIMARY KEY," +
                        "    plate VARCHAR(10) NOT NULL," +
                        "    model VARCHAR(50) NOT NULL," +
                        "    category VARCHAR(20) NOT NULL," +
                        "    UNIQUE (plate)" +
                        ");" +
                        "CREATE TABLE IF NOT EXISTS monthly_payers (" +
                        "    id INT AUTO_INCREMENT PRIMARY KEY," +
                        "    payment_month DATE NOT NULL," +
                        "    plate VARCHAR(10)," +
                        "    FOREIGN KEY (plate) REFERENCES vehicle(plate)" +
                        ");" +
                        "CREATE TABLE IF NOT EXISTS ticket (" +
                        "    id INT AUTO_INCREMENT PRIMARY KEY," +
                        "    plate VARCHAR(10) NOT NULL," +
                        "    entry_hour DATETIME NOT NULL," +
                        "    exit_hour DATETIME," +
                        "    entry_gate INT NOT NULL," +
                        "    exit_gate INT," +
                        "    amount DECIMAL(10,2)," +
                        "    parking_spot VARCHAR(100)," +
                        "    FOREIGN KEY (plate) REFERENCES vehicle(plate)" +
                        ");";

        String insertInitialSpotsSQL =
                "INSERT IGNORE INTO parking_spot (number, reserved) " +
                        "SELECT number, " +
                        "       CASE " +
                        "           WHEN number <= 200 THEN TRUE " +
                        "           ELSE FALSE " +
                        "       END AS reserved " +
                        "FROM ( " +
                        "    SELECT @rownum := @rownum + 1 AS number " +
                        "    FROM (SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 UNION SELECT 10) AS t1, " +
                        "         (SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 UNION SELECT 10) AS t2, " +
                        "         (SELECT @rownum := 0) AS t0 " +
                        ") AS numbers " +
                        "WHERE number <= 500;";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {

            // Criação das tabelas
            stmt.executeUpdate(createTablesSQL);
            System.out.println("Tables created successfully.");

            // Inserção inicial de vagas, ignorando duplicatas
            stmt.executeUpdate(insertInitialSpotsSQL);
            System.out.println("Initial spots inserted successfully.");

        } catch (SQLException e) {
            System.out.println("Error setting up the database: " + e.getMessage());
        }
    }
}
