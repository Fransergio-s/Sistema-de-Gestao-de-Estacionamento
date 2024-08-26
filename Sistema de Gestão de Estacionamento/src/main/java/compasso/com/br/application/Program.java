package compasso.com.br.application;

import compasso.com.br.db.DB;
import compasso.com.br.db.DbException;

import java.sql.Connection;

public class Program {
    public static void main(String[] args) {
        Connection conn = null;
        try {
            conn = DB.getConnection();
            System.out.println("Conectado com sucesso!");
        }
        catch(DbException e){
            System.out.println("Não foi possivel conectar ao banco, Erro: " + e.getMessage());
        }
        finally {
            DB.closeConnection();
        }
    }
}