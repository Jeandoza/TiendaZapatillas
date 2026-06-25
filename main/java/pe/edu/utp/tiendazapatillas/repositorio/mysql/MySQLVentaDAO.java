package pe.edu.utp.tiendazapatillas.repositorio.mysql;

import pe.edu.utp.tiendazapatillas.infraestructura.basedatos.ConexionBD;
import pe.edu.utp.tiendazapatillas.repositorio.interfaces.IRepositorioVenta;

import java.sql.Connection;

public class MySQLVentaDAO implements IRepositorioVenta {

    private final Connection conexion;

    public MySQLVentaDAO() {
        this.conexion = ConexionBD.getInstance().getConexion();
    }

    // Los métodos para manejar la persistencia de las ventas se implementarán aquí
    // una vez que el modelo de Venta y sus detalles estén definidos.
    //
    // Ejemplo:
    // @Override
    // public void guardarVenta(Venta venta) {
    //     String sql = "INSERT INTO ventas (...) VALUES (...)";
    //     // ... lógica con PreparedStatement
    // }
}
