import java.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Conexion {
    private static final Logger logger = LogManager.getLogger(Conexion.class);
    private static Conexion instancia;
    private Connection connection;
    
    private Conexion() {
        conectar();
    }
    
    public static Conexion getInstancia() {
        if (instancia == null) {
            instancia = new Conexion();
        }
        return instancia;
    }
    
    private void conectar() {
        try {
            // CONEXIÓN CON ROOT SIN CONTRASEÑA - VERSIÓN FINAL
            String url = "jdbc:mysql://localhost:3306/edd?" +
                        "useSSL=false" +
                        "&allowPublicKeyRetrieval=true" +
                        "&serverTimezone=UTC";
            String user = "root";
            String password = "";  
            
            connection = DriverManager.getConnection(url, user, password);
            logger.info(" Conectado exitosamente a la base de datos 'edd'");
            
        } catch (SQLException e) {
            logger.error(" Error de conexión: {}", e.getMessage());
            e.printStackTrace();
            
            // Intento alternativo con formato diferente
            try {
                logger.info("Intentando conexión alternativa...");
                String altUrl = "jdbc:mysql://localhost/edd?user=root&password=&useSSL=false";
                connection = DriverManager.getConnection(altUrl);
                logger.info(" Conectado usando URL alternativa");
            } catch (SQLException e2) {
                logger.error(" Conexión alternativa también falló: {}", e2.getMessage());
                connection = null;
            }
        }
    }
    
    public Connection getConnection() {
        return connection;
    }
    
    public void cerrarConexion() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                logger.info("Conexión cerrada correctamente");
            }
        } catch (SQLException e) {
            logger.error("Error al cerrar conexión: {}", e.getMessage());
        }
    }
    
    // Método para verificar si la conexión está activa
    public boolean isConectado() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
}