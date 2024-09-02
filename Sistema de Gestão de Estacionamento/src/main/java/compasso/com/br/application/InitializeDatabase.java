package compasso.com.br.application;

import compasso.com.br.db.DatabaseSetup;

import static compasso.com.br.db.DatabaseSetup.setupDatabase;

public class InitializeDatabase {
    public static void main(String[] args) {

        // Configura o banco de dados
        setupDatabase();
    }
}
