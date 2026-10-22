package Persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class Conexion {
    private static final String URL = "jdbc:mariadb://localhost:3306/";
    private static final String DB = "gp8_flechaflash"; 
    private static final String USUARIO = "root";
    private static final String PASSWORD = "";
    
    private static Connection conexion = null;

    public Conexion() {}
    
    public static Connection getConexion() {
        if (conexion == null) {
            try {
                Class.forName("org.mariadb.jdbc.Driver");
                conexion = DriverManager.getConnection(URL + DB, USUARIO, PASSWORD);
            } catch (ClassNotFoundException e) {
                JOptionPane.showMessageDialog(null, "Error al cargar driver MariaDB: " + e.getMessage());
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error de conexión a la base de datos: " + e.getMessage());
            }
        }
        return conexion;
    }
}
