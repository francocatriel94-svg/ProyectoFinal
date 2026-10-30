package Persistencia;

import Modelo.Asiento;
import Modelo.Colectivo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class AsientoData {

    // Convención asumida del campo estado: 1 = libre, 0 = ocupado
    private static final int LIBRE = 1;
    private static final int OCUPADO = 0;
    private Connection con;

    public AsientoData() {
        con = Conexion.getConexion();
    }

    // CREATE
    public void guardarAsiento(Asiento asiento) {
        String sql = "INSERT INTO lugares (ID_colectivo, numeroAsiento, estado) VALUES (?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, asiento.getID_colectivo());
            ps.setInt(2, asiento.getNumeroAsiento());
            ps.setInt(3, asiento.getEstado());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    asiento.setID_lugar(rs.getInt(1));
                    JOptionPane.showMessageDialog(null, "Asiento guardado.");
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al guardar el asiento: " + ex.getMessage());
        }
    }

    // READ: uno por id
    public Asiento buscarAsiento(int id) {
        String sql = "SELECT * FROM lugares WHERE ID_lugar = ?";
        Asiento asiento = null;
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    asiento = armarAsiento(rs);
                } 
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al buscar el asiento: " + ex.getMessage());
        }
        return asiento;
    }

    // READ: un asiento por colectivo y número (útil para evitar duplicados)
    public Asiento buscarAsientoPorNumero(int idColectivo, int numeroAsiento) {
        String sql = "SELECT * FROM lugares WHERE ID_colectivo = ? AND numeroAsiento = ?";
        Asiento asiento = null;
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idColectivo);
            ps.setInt(2, numeroAsiento);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    asiento = armarAsiento(rs);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al buscar el asiento: " + ex.getMessage());
        }
        return asiento;
    }

    // READ: todos
    public List<Asiento> listarAsientos() {
        String sql = "SELECT * FROM lugares";
        List<Asiento> asientos = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                asientos.add(armarAsiento(rs));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al listar los asientos: " + ex.getMessage());
        }
        return asientos;
    }

    // READ: asientos de un colectivo
    public List<Asiento> listarAsientosPorColectivo(int idColectivo) {
        String sql = "SELECT * FROM lugares WHERE ID_colectivo = ? ORDER BY numeroAsiento";
        List<Asiento> asientos = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idColectivo);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    asientos.add(armarAsiento(rs));
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al listar los asientos del colectivo: " + ex.getMessage());
        }
        return asientos;
    }

    // READ: asientos libres de un colectivo
    public List<Asiento> listarAsientosLibresPorColectivo(int idColectivo) {
        String sql = "SELECT * FROM lugares WHERE ID_colectivo = ? AND estado = ? ORDER BY numeroAsiento";
        List<Asiento> asientos = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idColectivo);
            ps.setInt(2, LIBRE);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    asientos.add(armarAsiento(rs));
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al listar los asientos libres: " + ex.getMessage());
        }
        return asientos;
    }

    // UPDATE
    public void modificarAsiento(Asiento asiento) {
        String sql = "UPDATE lugares SET ID_colectivo = ?, numeroAsiento = ?, estado = ? WHERE ID_lugar = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, asiento.getID_colectivo());
            ps.setInt(2, asiento.getNumeroAsiento());
            ps.setInt(3, asiento.getEstado());
            ps.setInt(4, asiento.getID_lugar());
            if (ps.executeUpdate() == 1) {
                JOptionPane.showMessageDialog(null, "Asiento modificado.");
            } 
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al modificar el asiento: " + ex.getMessage());
        }
    }

    // Marcar como ocupado (al vender un pasaje)
    public void ocuparAsiento(int id) {
        cambiarEstado(id, OCUPADO, "Asiento ocupado.");
    }

    // Marcar como libre (al anular un pasaje)
    public void liberarAsiento(int id) {
        cambiarEstado(id, LIBRE, "Asiento liberado.");
    }

    private void cambiarEstado(int id, int estado, String mensaje) {
        String sql = "UPDATE lugares SET estado = ? WHERE ID_lugar = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, estado);
            ps.setInt(2, id);
            if (ps.executeUpdate() == 1) {
                JOptionPane.showMessageDialog(null, mensaje);
            } 
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al cambiar el estado del asiento: " + ex.getMessage());
        }
    }

    // DELETE físico
    public void eliminarAsiento(int id) {
        String sql = "DELETE FROM lugares WHERE ID_lugar = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            if (ps.executeUpdate() == 1) {
                JOptionPane.showMessageDialog(null, "Asiento eliminado.");
            } 
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al eliminar el asiento: " + ex.getMessage());
        }
    }

    // Convierte una fila del ResultSet en un Asiento (trae el Colectivo con su Data)
    private Asiento armarAsiento(ResultSet rs) throws SQLException {
        ColectivoData c = new ColectivoData();
        Asiento asiento = new Asiento();
        asiento.setID_lugar(rs.getInt("ID_lugar"));

        Colectivo colectivo = c.buscarColectivo(rs.getInt("ID_colectivo"));
        asiento.setID_colectivo(colectivo);
        asiento.setNumeroAsiento(rs.getInt("numeroAsiento"));
        asiento.setEstado(rs.getInt("estado"));
        return asiento;
    }
}