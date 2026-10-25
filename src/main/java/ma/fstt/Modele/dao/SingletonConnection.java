package ma.fstt.Modele.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SingletonConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/gestion_commandes";
    private static final String USER = "root";
    private static final String PASSWORD = "";   // vide par défaut dans XAMPP

    private static Connection connection;

    private SingletonConnection() { }   // personne ne peut faire "new"

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            }
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Erreur de connexion à la base", e);
        }
        return connection;
    }
}
