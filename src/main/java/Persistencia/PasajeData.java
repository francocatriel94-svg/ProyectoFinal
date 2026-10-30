package Persistencia;

import Modelo.Asiento;
import Modelo.Pasaje;
import Modelo.Pasajero;
import Modelo.RutaViaje;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class PasajeData {

    private Connection con;
    public PasajeData() {
        con = Conexion.getConexion();
    }

    // CREATE
    public void guardarPasaje(Pasaje pasaje) {
        String sql = "INSERT INTO pasajes (ID_pasajero, ID_lugar, ID_ruta, fechaVenta, horaVenta, precio, estado) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, pasaje.getID_pasajero());
            ps.setInt(2, pasaje.getID_lugar());
            ps.setInt(3, pasaje.getID_ruta());
            ps.setDate(4, Date.valueOf(pasaje.getFechaVenta()));
            ps.setTime(5, Time.valueOf(pasaje.getHoraVenta()));
            ps.setDouble(6, pasaje.getPrecio());
            ps.setInt(7, pasaje.getEstado());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    pasaje.setID_pasaje(rs.getInt(1));
                    JOptionPane.showMessageDialog(null, "Pasaje guardado.");
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al guardar el pasaje: " + ex.getMessage());
        }
    }

    // READ: uno por id
    public Pasaje buscarPasaje(int id) {
        String sql = "SELECT * FROM pasajes WHERE ID_pasaje = ?";
        Pasaje pasaje = null;
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    pasaje = armarPasaje(rs);
                } 
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al buscar el pasaje: " + ex.getMessage());
        }
        return pasaje;
    }

    // READ: todos
    public List<Pasaje> listarPasajes() {
        String sql = "SELECT * FROM pasajes";
        List<Pasaje> pasajes = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                pasajes.add(armarPasaje(rs));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al listar los pasajes: " + ex.getMessage());
        }
        return pasajes;
    }

    // READ: pasajes de un pasajero
    public List<Pasaje> listarPasajesPorPasajero(int idPasajero) {
        String sql = "SELECT * FROM pasajes WHERE ID_pasajero = ?";
        return listarPorId(sql, idPasajero, "del pasajero");
    }

    // READ: pasajes de una ruta
    public List<Pasaje> listarPasajesPorRuta(int idRuta) {
        String sql = "SELECT * FROM pasajes WHERE ID_ruta = ?";
        return listarPorId(sql, idRuta, "de la ruta");
    }

    private List<Pasaje> listarPorId(String sql, int id, String descripcion) {
        List<Pasaje> pasajes = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    pasajes.add(armarPasaje(rs));
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al listar los pasajes " + descripcion + ": " + ex.getMessage());
        }
        return pasajes;
    }

    // UPDATE
    public void modificarPasaje(Pasaje pasaje) {
        String sql = "UPDATE pasajes SET ID_pasajero = ?, ID_lugar = ?, ID_ruta = ?, fechaVenta = ?, "
                + "horaVenta = ?, precio = ?, estado = ? WHERE ID_pasaje = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, pasaje.getID_pasajero());
            ps.setInt(2, pasaje.getID_lugar());
            ps.setInt(3, pasaje.getID_ruta());
            ps.setDate(4, Date.valueOf(pasaje.getFechaVenta()));
            ps.setTime(5, Time.valueOf(pasaje.getHoraVenta()));
            ps.setDouble(6, pasaje.getPrecio());
            ps.setInt(7, pasaje.getEstado());
            ps.setInt(8, pasaje.getID_pasaje());
            if (ps.executeUpdate() == 1) {
                JOptionPane.showMessageDialog(null, "Pasaje modificado.");
            } 
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al modificar el pasaje: " + ex.getMessage());
        }
    }

    // Baja lógica (estado = 0)
    public void darDeBaja(int id) {
        cambiarEstado(id, 0, "Pasaje dado de baja.");
    }

    // Alta lógica (estado = 1)
    public void darDeAlta(int id) {
        cambiarEstado(id, 1, "Pasaje dado de alta.");
    }

    private void cambiarEstado(int id, int estado, String mensaje) {
        String sql = "UPDATE pasajes SET estado = ? WHERE ID_pasaje = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, estado);
            ps.setInt(2, id);
            if (ps.executeUpdate() == 1) {
                JOptionPane.showMessageDialog(null, mensaje);
            } 
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al cambiar el estado del pasaje: " + ex.getMessage());
        }
    }

    // DELETE físico
    public void eliminarPasaje(int id) {
        String sql = "DELETE FROM pasajes WHERE ID_pasaje = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            if (ps.executeUpdate() == 1) {
                JOptionPane.showMessageDialog(null, "Pasaje eliminado.");
            } 
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al eliminar el pasaje: " + ex.getMessage());
        }
    }

    // Convierte una fila del ResultSet en un Pasaje (trae los objetos con sus Data)
    private Pasaje armarPasaje(ResultSet rs) throws SQLException {
        Pasaje pasaje = new Pasaje();
        PasajeroData p = new PasajeroData();
        RutaViajeData z = new RutaViajeData();
        AsientoData a = new AsientoData();
        pasaje.setID_pasaje(rs.getInt("ID_pasaje"));
        Pasajero pasajero = p.buscarPasajero(rs.getInt("ID_pasajero"));
        Asiento lugar = a.buscarAsiento(rs.getInt("ID_lugar"));
        RutaViaje ruta = z.buscarRuta(rs.getInt("ID_ruta"));
        pasaje.setID_pasajero(pasajero);
        pasaje.setID_lugar(lugar);
        pasaje.setID_ruta(ruta);
        pasaje.setFechaVenta(rs.getDate("fechaVenta").toLocalDate());
        pasaje.setHoraVenta(rs.getTime("horaVenta").toLocalTime());
        pasaje.setPrecio(rs.getDouble("precio"));
        pasaje.setEstado(rs.getInt("estado"));
        return pasaje;
    }
}