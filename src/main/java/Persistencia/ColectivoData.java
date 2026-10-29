package Persistencia;

import Modelo.Colectivo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class ColectivoData {

    private Connection con;

    public ColectivoData() {
        con = Conexion.getConexion();
    }

    // CREATE
    public void guardarColectivo(Colectivo colectivo) {
        String sql = "INSERT INTO colectivos (matricula, marca, modelo, capacidad, chofer, estado) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            // La columna matricula es varchar(50) y en la clase es int
            ps.setString(1, String.valueOf(colectivo.getMatricula()));
            ps.setString(2, colectivo.getMarca());
            ps.setString(3, colectivo.getModelo());
            ps.setInt(4, colectivo.getCapacidad());
            ps.setString(5, colectivo.getChofer());
            ps.setInt(6, colectivo.getEstado());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    colectivo.setID_colectivo(rs.getInt(1));
                    JOptionPane.showMessageDialog(null, "Colectivo guardado.");
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al guardar el colectivo: " + ex.getMessage());
        }
    }

    // READ: uno por id
    public Colectivo buscarColectivo(int id) {
        String sql = "SELECT * FROM colectivos WHERE ID_colectivo = ?";
        Colectivo colectivo = null;
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    colectivo = armarColectivo(rs);
                } else {
                    JOptionPane.showMessageDialog(null, "No existe el colectivo.");
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al buscar el colectivo: " + ex.getMessage());
        }
        return colectivo;
    }

    // READ: uno por matrícula
    public Colectivo buscarColectivoPorMatricula(int matricula) {
        String sql = "SELECT * FROM colectivos WHERE matricula = ?";
        Colectivo colectivo = null;
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, String.valueOf(matricula));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    colectivo = armarColectivo(rs);
                } else {
                    JOptionPane.showMessageDialog(null, "No existe un colectivo con esa matrícula.");
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al buscar el colectivo: " + ex.getMessage());
        }
        return colectivo;
    }

    // READ: todos
    public List<Colectivo> listarColectivos() {
        String sql = "SELECT * FROM colectivos";
        return listar(sql);
    }

    // READ: solo los activos (estado = 1)
    public List<Colectivo> listarColectivosActivos() {
        String sql = "SELECT * FROM colectivos WHERE estado = 1";
        return listar(sql);
    }

    private List<Colectivo> listar(String sql) {
        List<Colectivo> colectivos = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                colectivos.add(armarColectivo(rs));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al listar los colectivos: " + ex.getMessage());
        }
        return colectivos;
    }

    // UPDATE
    public void modificarColectivo(Colectivo colectivo) {
        String sql = "UPDATE colectivos SET matricula = ?, marca = ?, modelo = ?, capacidad = ?, "
                + "chofer = ?, estado = ? WHERE ID_colectivo = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, String.valueOf(colectivo.getMatricula()));
            ps.setString(2, colectivo.getMarca());
            ps.setString(3, colectivo.getModelo());
            ps.setInt(4, colectivo.getCapacidad());
            ps.setString(5, colectivo.getChofer());
            ps.setInt(6, colectivo.getEstado());
            ps.setInt(7, colectivo.getID_colectivo());
            if (ps.executeUpdate() == 1) {
                JOptionPane.showMessageDialog(null, "Colectivo modificado.");
            } else {
                JOptionPane.showMessageDialog(null, "No existe el colectivo.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al modificar el colectivo: " + ex.getMessage());
        }
    }

    // Baja lógica (estado = 0)
    public void darDeBaja(int id) {
        cambiarEstado(id, 0, "Colectivo dado de baja.");
    }

    // Alta lógica (estado = 1)
    public void darDeAlta(int id) {
        cambiarEstado(id, 1, "Colectivo dado de alta.");
    }

    private void cambiarEstado(int id, int estado, String mensaje) {
        String sql = "UPDATE colectivos SET estado = ? WHERE ID_colectivo = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, estado);
            ps.setInt(2, id);
            if (ps.executeUpdate() == 1) {
                JOptionPane.showMessageDialog(null, mensaje);
            } else {
                JOptionPane.showMessageDialog(null, "No existe el colectivo.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al cambiar el estado del colectivo: " + ex.getMessage());
        }
    }

    // DELETE BD
    public void eliminarColectivo(int id) {
        String sql = "DELETE FROM colectivos WHERE ID_colectivo = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            if (ps.executeUpdate() == 1) {
                JOptionPane.showMessageDialog(null, "Colectivo eliminado.");
            } else {
                JOptionPane.showMessageDialog(null, "No existe el colectivo.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al eliminar el colectivo: " + ex.getMessage());
        }
    }

    // Convierte una fila del ResultSet en un Colectivo
    private Colectivo armarColectivo(ResultSet rs) throws SQLException {
        Colectivo colectivo = new Colectivo();
        colectivo.setID_colectivo(rs.getInt("ID_colectivo"));
        colectivo.setMatricula(rs.getInt("matricula"));
        colectivo.setMarca(rs.getString("marca"));
        colectivo.setModelo(rs.getString("modelo"));
        colectivo.setCapacidad(rs.getInt("capacidad"));
        colectivo.setChofer(rs.getString("chofer"));
        colectivo.setEstado(rs.getInt("estado"));
        return colectivo;
    }
}