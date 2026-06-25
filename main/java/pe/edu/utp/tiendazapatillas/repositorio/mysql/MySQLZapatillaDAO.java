package pe.edu.utp.tiendazapatillas.repositorio.mysql;

import pe.edu.utp.tiendazapatillas.infraestructura.basedatos.ConexionBD;
import pe.edu.utp.tiendazapatillas.modelo.entidades.Marca;
import pe.edu.utp.tiendazapatillas.modelo.entidades.Talla;
import pe.edu.utp.tiendazapatillas.modelo.entidades.Zapatilla;
import pe.edu.utp.tiendazapatillas.repositorio.interfaces.IRepositorioZapatilla;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MySQLZapatillaDAO implements IRepositorioZapatilla {

    private final Connection conexion;

    public MySQLZapatillaDAO() {
        this.conexion = ConexionBD.getInstance().getConexion();
    }

    @Override
    public List<Zapatilla> listarTodas() {
        List<Zapatilla> zapatillas = new ArrayList<>();
        String sql = "SELECT z.id, z.modelo, z.precio, z.stock, " +
                     "m.id as marca_id, m.nombre as marca_nombre, " +
                     "t.id as talla_id, t.descripcion as talla_descripcion " +
                     "FROM zapatillas z " +
                     "JOIN marcas m ON z.marca_id = m.id " +
                     "JOIN tallas t ON z.talla_id = t.id";
        try (Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                zapatillas.add(crearZapatillaDesdeResultSet(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar las zapatillas", e);
        }
        return zapatillas;
    }

    @Override
    public Optional<Zapatilla> buscarPorId(int id) {
        String sql = "SELECT z.id, z.modelo, z.precio, z.stock, " +
                     "m.id as marca_id, m.nombre as marca_nombre, " +
                     "t.id as talla_id, t.descripcion as talla_descripcion " +
                     "FROM zapatillas z " +
                     "JOIN marcas m ON z.marca_id = m.id " +
                     "JOIN tallas t ON z.talla_id = t.id " +
                     "WHERE z.id = ?";
        try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(crearZapatillaDesdeResultSet(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar la zapatilla con id: " + id, e);
        }
        return Optional.empty();
    }

    @Override
    public void guardar(Zapatilla zapatilla) {
        String sql = "INSERT INTO zapatillas (modelo, marca_id, talla_id, precio, stock) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setString(1, zapatilla.getModelo());
            pstmt.setInt(2, zapatilla.getMarca().getId());
            pstmt.setInt(3, zapatilla.getTalla().getId());
            pstmt.setBigDecimal(4, zapatilla.getPrecio());
            pstmt.setInt(5, zapatilla.getStock());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar la zapatilla", e);
        }
    }

    @Override
    public void actualizar(Zapatilla zapatilla) {
        String sql = "UPDATE zapatillas SET modelo = ?, marca_id = ?, talla_id = ?, precio = ?, stock = ? WHERE id = ?";
        try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setString(1, zapatilla.getModelo());
            pstmt.setInt(2, zapatilla.getMarca().getId());
            pstmt.setInt(3, zapatilla.getTalla().getId());
            pstmt.setBigDecimal(4, zapatilla.getPrecio());
            pstmt.setInt(5, zapatilla.getStock());
            pstmt.setInt(6, zapatilla.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar la zapatilla", e);
        }
    }

    @Override
    public void eliminar(int id) {
        String sql = "DELETE FROM zapatillas WHERE id = ?";
        try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar la zapatilla", e);
        }
    }

    @Override
    public void actualizarStock(int idZapatilla, int nuevoStock) {
        String sql = "UPDATE zapatillas SET stock = ? WHERE id = ?";
        try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setInt(1, nuevoStock);
            pstmt.setInt(2, idZapatilla);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar el stock de la zapatilla", e);
        }
    }

    private Zapatilla crearZapatillaDesdeResultSet(ResultSet rs) throws SQLException {
        Marca marca = new Marca(rs.getInt("marca_id"), rs.getString("marca_nombre"));
        Talla talla = new Talla(rs.getInt("talla_id"), rs.getString("talla_descripcion"));
        return new Zapatilla(
                rs.getInt("id"),
                rs.getString("modelo"),
                marca,
                talla,
                rs.getBigDecimal("precio"),
                rs.getInt("stock")
        );
    }
}
