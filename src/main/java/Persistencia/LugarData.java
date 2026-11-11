package Persistencia;

import Modelo.Lugar;
import Modelo.Colectivo;
import Modelo.RutaViaje;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class LugarData {

    //Convención asumida del campo estado: 1 = libre, 0 = ocupado.
    private static final int LIBRE = 1;
    private static final int OCUPADO = 0;
    private Connection con;

    public LugarData() {
        con = Conexion.getConexion();
    }

    public boolean guardarLugar(Lugar asiento) {
        String sql = "INSERT INTO lugar " + "(ID_ruta, numeroAsiento, estado) " + "VALUES (?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, asiento.getID_ruta());
            ps.setInt(2, asiento.getNumeroAsiento());
            ps.setInt(3, asiento.getEstado());

            if (ps.executeUpdate() == 1) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        asiento.setID_lugar(rs.getInt(1));
                        return true;
                    }
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al guardar el asiento: " + ex.getMessage());
        }
        return false;
    }

    //Busca un lugar por su id.
    public Lugar buscarLugar(int id) {
        String sql = "SELECT * FROM lugar WHERE ID_lugar = ?";
        Lugar asiento = null;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    asiento = armarLugar(rs);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al buscar el asiento: " + ex.getMessage());
        }
        return asiento;
    }

    //Busca un asiento por su colectivo y id(útil para evitar duplicados).
    public Lugar buscarLugarPorNumero(int idRuta, int numeroAsiento) {
        String sql = "SELECT * FROM lugar WHERE ID_ruta = ? AND numeroAsiento = ?";
        Lugar asiento = null;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idRuta);
            ps.setInt(2, numeroAsiento);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    asiento = armarLugar(rs);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al buscar el asiento: " + ex.getMessage());
        }
        return asiento;
    }

    //Lista todos los lugares.
    public List<Lugar> listarLugares() {
        String sql = "SELECT * FROM lugar";
        List<Lugar> asientos = new ArrayList<>();

        try (PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                asientos.add(armarLugar(rs));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al listar los asientos: " + ex.getMessage());
        }
        return asientos;
    }

    //Lista los lugares de un colectivo por su ruta.
    public List<Lugar> listarLugaresPorRuta(int idRuta) {
        String sql = "SELECT * FROM lugar WHERE ID_ruta = ? ORDER BY numeroAsiento";
        List<Lugar> asientos = new ArrayList<>();

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idRuta);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    asientos.add(armarLugar(rs));
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al listar los asientos del colectivo: " + ex.getMessage());
        }
        return asientos;
    }

    //Lista los lugares lugares libres de un colectivo por su ruta.
    public List<Lugar> listarLugaresLibresPorRuta(int idRuta) {
        String sql = "SELECT * FROM lugar WHERE ID_ruta = ? AND estado = ? ORDER BY numeroAsiento";
        List<Lugar> asientos = new ArrayList<>();

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idRuta);
            ps.setInt(2, LIBRE);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    asientos.add(armarLugar(rs));
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al listar los asientos libres: " + ex.getMessage());
        }
        return asientos;
    }

    //Modificamos los valores de los atributos de un lugar.
    public void modificarLugar(Lugar asiento) {
        String sql = "UPDATE lugar SET ID_ruta = ?, numeroAsiento = ?, estado = ? WHERE ID_lugar = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, asiento.getID_ruta());
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

    //Marcar como ocupado (al vender un pasaje).
    public void ocuparLugar(int id) {
        cambiarEstado(id, OCUPADO, "Asiento ocupado.");
    }

    //Marcar como libre (al anular un pasaje).
    public void desocuparLugar(int id) {
        cambiarEstado(id, LIBRE, "Asiento liberado.");
    }

    //Cambia el estado de un lugar.
    private void cambiarEstado(int id, int estado, String mensaje) {
        String sql = "UPDATE lugar SET estado = ? WHERE ID_lugar = ?";

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

    //Borra un lugar.
    public void eliminarLugar(int id) {
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

    //Convierte una fila del ResultSet en un Lugar (trae el Colectivo con su Data).
    private Lugar armarLugar(ResultSet rs) throws SQLException {
        RutaViajeData rData = new RutaViajeData();
        Lugar asiento = new Lugar();
        asiento.setID_lugar(rs.getInt("ID_lugar"));
        RutaViaje ruta = rData.buscarRuta(rs.getInt("ID_ruta"));
        asiento.setID_ruta(ruta != null ? ruta.getID_ruta() : 0);
        asiento.setNumeroAsiento(rs.getInt("numeroAsiento"));
        asiento.setEstado(rs.getInt("estado"));
        return asiento;
    }
}
