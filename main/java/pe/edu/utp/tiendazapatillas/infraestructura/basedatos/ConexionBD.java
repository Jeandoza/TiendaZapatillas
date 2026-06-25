package pe.edu.utp.tiendazapatillas.infraestructura.basedatos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConexionBD {

    private static final String HOST = "localhost";
    private static final String PORT = "3306";
    private static final String DATABASE = "bd_tienda_zapatillas";
    private static final String URL = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE;
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private Connection conexion;

    // El constructor ahora es vacío y no lanza excepciones.
    // La inicialización del Singleton es segura y no realiza operaciones de red.
    private ConexionBD() {}

    private static class SingletonHelper {
        private static final ConexionBD INSTANCE = new ConexionBD();
    }

    /**
     * Devuelve la única instancia de la clase. Esta operación es segura y no
     * intenta conectarse a la base de datos.
     * @return La instancia Singleton de ConexionBD.
     */
    public static ConexionBD getInstance() {
        return SingletonHelper.INSTANCE;
    }

    /**
     * Obtiene una conexión a la base de datos. Es en este método donde se
     * intenta la conexión real. Si falla, lanzará una RuntimeException.
     * @return Un objeto Connection funcional.
     */
    public Connection getConexion() {
        try {
            if (this.conexion == null || this.conexion.isClosed()) {
                // El driver se carga aquí, solo cuando se necesita una conexión real.
                Class.forName("com.mysql.cj.jdbc.Driver");
                this.conexion = DriverManager.getConnection(URL, USER, PASSWORD);
            }
        } catch (SQLException | ClassNotFoundException e) {
            // Lanzar una excepción de runtime para que el código que la llama sepa que falló.
            throw new RuntimeException("Error al conectar o restablecer la conexión con la BD: " + e.getMessage(), e);
        }
        return this.conexion;
    }
}
