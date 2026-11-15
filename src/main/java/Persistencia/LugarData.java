package Persistencia;

import Modelo.Lugar;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LugarData {

    // Convención asumida del campo estado: 1 = libre, 0 = ocupado.
    public static final int LIBRE = 1;
    public static final int OCUPADO = 0;
    private static final Logger LOGGER = Logger.getLogger(LugarData.class.getName());

    private Connection con;

    public LugarData() {
        con = Conexion.getConexion();
    }

    public boolean guardarLugar(Lugar asiento) {
        String sql = "INSERT INTO lugar (ID_ruta, numeroAsiento, estado) VALUES (?, ?, ?)";

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
            return false;
        }
        return false;
    }

    // Modificamos los valores de un lugar.
    public boolean modificarLugar(Lugar asiento) {
        String sql = "UPDATE lugar SET ID_ruta = ?, numeroAsiento = ?, estado = ? WHERE ID_lugar = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, asiento.getID_ruta());
            ps.setInt(2, asiento.getNumeroAsiento());
            ps.setInt(3, asiento.getEstado());
            ps.setInt(4, asiento.getID_lugar());

            return ps.executeUpdate() == 1;

        } catch (SQLException ex) {
            return false;
        }
    }

    // Marcar como ocupado (al vender un pasaje).
    public boolean ocuparLugar(int id) {
        return cambiarEstado(id, OCUPADO);
    }

    // Marcar como libre (al anular un pasaje).
    public boolean desocuparLugar(int id) {
        return cambiarEstado(id, LIBRE);
    }

    // Borra un lugar.
    public boolean eliminarLugar(int id) {
        String sql = "DELETE FROM lugar WHERE ID_lugar = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);

            return ps.executeUpdate() == 1;

        } catch (SQLException ex) {
            return false;
        }
    }

    // Comprueba si un asiento está disponible para un pasaje.
    public boolean estaDisponibleParaPasaje(int idLugar, int idRuta, int idPasajeActual) {
        String sql = "SELECT l.estado, EXISTS (SELECT 1 FROM pasaje p WHERE p.ID_lugar = l.ID_lugar AND p.ID_ruta = l.ID_ruta AND p.estado = 1 AND p.ID_pasaje <> ?) AS ocupadoPorOtro, EXISTS (SELECT 1 FROM pasaje p "
                + "WHERE p.ID_lugar = l.ID_lugar AND p.ID_ruta = l.ID_ruta AND p.estado = 1 AND p.ID_pasaje = ?) AS ocupadoPorActual FROM lugar l WHERE l.ID_lugar = ? AND l.ID_ruta = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPasajeActual);
            ps.setInt(2, idPasajeActual);
            ps.setInt(3, idLugar);
            ps.setInt(4, idRuta);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return false;
                }

                int estado = rs.getInt("estado");
                boolean ocupadoPorOtro = rs.getBoolean("ocupadoPorOtro");
                boolean ocupadoPorActual = rs.getBoolean("ocupadoPorActual");

                if (ocupadoPorOtro) {
                    return false;
                }

                if (estado == LIBRE) {
                    return !ocupadoPorActual;
                }

                if (estado == OCUPADO) {
                    return ocupadoPorActual;
                }

                return false;
            }

        } catch (SQLException ex) {
            return false;
        }
    }

    // Busca un lugar por su id.
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
            return null;
        }
        return asiento;
    }

    // Busca un asiento por su ruta y id(para evitar duplicados).
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
            return null;
        }
        return asiento;
    }

    // Lista todos los lugares.
    public List<Lugar> listarLugares() {
        String sql = "SELECT * FROM lugar";
        List<Lugar> asientos = new ArrayList<>();

        try (PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                asientos.add(armarLugar(rs));
            }
        } catch (SQLException ex) {
            return asientos;
        }
        return asientos;
    }

    // Lista todos los lugares de una ruta.
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
            LOGGER.log(Level.SEVERE, "Error al listar los asientos por ruta.", ex);
        }

        return asientos;
    }

    // Lista los lugares libres de un ruta por su estado.
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
            return asientos;
        }
        return asientos;
    }

    public List<Lugar> listarLugaresPorRutaYEstado(int idRuta, int estado) {
        if (estado != LIBRE && estado != OCUPADO) {
            return new ArrayList<>();
        }

        String sql = "SELECT * FROM lugar WHERE ID_ruta = ? AND estado = ? ORDER BY numeroAsiento";
        List<Lugar> asientos = new ArrayList<>();

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idRuta);
            ps.setInt(2, estado);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    asientos.add(armarLugar(rs));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al listar los asientos por ruta y estado.", ex);
        }

        return asientos;
    }

    // Ocupa un lugar dentro de una transacción externa.
    void ocuparLugar(Connection conexion, int idLugar, int idRuta) throws SQLException {
        String sql = "UPDATE lugar SET estado = ? WHERE ID_lugar = ? AND ID_ruta = ? AND estado = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, OCUPADO);
            ps.setInt(2, idLugar);
            ps.setInt(3, idRuta);
            ps.setInt(4, LIBRE);

            if (ps.executeUpdate() != 1) {
                throw new SQLException("El asiento no existe, no pertenece a esta ruta o ya está ocupado.");
            }
        }
    }

    // Libera un lugar dentro de una transacción externa.
    void desocuparLugar(Connection conexion, int idLugar, int idRuta) throws SQLException {
        String sql = "UPDATE lugar SET estado = ? WHERE ID_lugar = ? AND ID_ruta = ? AND estado = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, LIBRE);
            ps.setInt(2, idLugar);
            ps.setInt(3, idRuta);
            ps.setInt(4, OCUPADO);

            if (ps.executeUpdate() != 1) {
                throw new SQLException("No se pudo liberar el asiento.");
            }
        }
    }

    // Cambia el estado de un lugar.
    private boolean cambiarEstado(int id, int estado) {
        String sql = "UPDATE lugar SET estado = ? WHERE ID_lugar = ? AND estado <> ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, estado);
            ps.setInt(2, id);
            ps.setInt(3, estado);

            return ps.executeUpdate() == 1;

        } catch (SQLException ex) {
            return false;
        }
    }

    // Convierte una fila del ResultSet en un Lugar.
    private Lugar armarLugar(ResultSet rs) throws SQLException {
        Lugar asiento = new Lugar();

        asiento.setID_lugar(rs.getInt("ID_lugar"));
        asiento.setID_ruta(rs.getInt("ID_ruta"));
        asiento.setNumeroAsiento(rs.getInt("numeroAsiento"));
        asiento.setEstado(rs.getInt("estado"));

        return asiento;
    }
}
