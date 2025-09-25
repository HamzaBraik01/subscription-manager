package dao;

import java.sql.Connection;

public class TestConnection {
    public static void main(String[] args) {
        try (Connection conn = JDBCUtil.getConnection()) {
            if (conn != null) {
                System.out.println("Connexion PostgreSQL réussie !");
            } else {
                System.out.println("Connexion échouée !");
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de l'ouverture de la connexion : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
