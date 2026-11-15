package Persistencia;

import Modelo.Pasaje;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PasajeData {

    private Connection con;
    private LugarData lugarData;
    private static final Logger LOGGER = Logger.getLogger(PasajeData.class.getName());

    public PasajeData() {
        con = Conexion.getConexion();
        lugarData = new LugarData();
    }

    // Guarda un pasaje y reserva el asiento correspondiente.
    public boolean guardarPasaje(Pasaje pasaje) {
        String sql = "INSERT INTO pasaje (ID_pasajero, ID_lugar, ID_ruta, fechaVenta, horaVenta, precio, estado) VALUES (?, ?, ?, ?, ?, ?, ?)";

        boolean autoCommitAnterior = true;

        try {
            autoCommitAnterior = con.getAutoCommit();
            con.setAutoCommit(false);

            // Primero reservamos el asiento si el pasaje está activo.
            if (pasaje.getEstado() == 1) {
                lugarData.ocuparLugar(con, pasaje.getID_lugar(), pasaje.getID_ruta());
            }

            // Después guardamos el pasaje.
            try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, pasaje.getID_pasajero());
                ps.setInt(2, pasaje.getID_lugar());
                ps.setInt(3, pasaje.getID_ruta());
                ps.setDate(4, Date.valueOf(pasaje.getFechaVenta()));
                ps.setTime(5, Time.valueOf(pasaje.getHoraVenta()));
                ps.setBigDecimal(6, pasaje.getPrecio());
                ps.setInt(7, pasaje.getEstado());

                if (ps.executeUpdate() != 1) {
                    throw new SQLException("No se pudo guardar el pasaje.");
                }

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        pasaje.setID_pasaje(rs.getInt(1));
                    } else {
                        throw new SQLException("No se pudo obtener el ID del pasaje.");
                    }
                }
            }
            con.commit();
            return true;

        } catch (SQLException ex) {
            try {
                con.rollback();
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error al revertir la transacción al guardar el pasaje.", e);
            }
            LOGGER.log(Level.SEVERE, "Error al guardar el pasaje.", ex);
            return false;

        } finally {
            try {
                con.setAutoCommit(autoCommitAnterior);
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error al restaurar el autocommit al guardar el pasaje.", e);
            }
        }
    }

    // Modifica los datos de un pasaje.
    public boolean modificarPasaje(Pasaje pasaje) {
        String sqlBuscar = "SELECT ID_lugar, ID_ruta, estado FROM pasaje WHERE ID_pasaje = ? FOR UPDATE";
        String sqlModificar = "UPDATE pasaje SET ID_pasajero = ?, ID_lugar = ?, ID_ruta = ?, fechaVenta = ?, horaVenta = ?, precio = ?, estado = ? WHERE ID_pasaje = ?";

        boolean autoCommitAnterior = true;

        try {
            autoCommitAnterior = con.getAutoCommit();
            con.setAutoCommit(false);

            int lugarAnterior;
            int rutaAnterior;
            int estadoAnterior;

            // Obtenemos los datos actuales del pasaje.
            try (PreparedStatement ps = con.prepareStatement(sqlBuscar)) {
                ps.setInt(1, pasaje.getID_pasaje());

                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        throw new SQLException("No se encontró el pasaje.");
                    }

                    lugarAnterior = rs.getInt("ID_lugar");
                    rutaAnterior = rs.getInt("ID_ruta");
                    estadoAnterior = rs.getInt("estado");
                }
            }

            boolean asientoCambio = lugarAnterior != pasaje.getID_lugar() || rutaAnterior != pasaje.getID_ruta();

            // Si el pasaje queda activo y cambia el asiento, reservamos el nuevo.
            if (pasaje.getEstado() == 1 && (estadoAnterior == 0 || asientoCambio)) {
                lugarData.ocuparLugar(con, pasaje.getID_lugar(), pasaje.getID_ruta());
            }

            // Si el pasaje anterior estaba activo y cambia el asiento o se da de baja, liberamos el anterior.
            if (estadoAnterior == 1 && (pasaje.getEstado() == 0 || asientoCambio)) {
                lugarData.desocuparLugar(con, lugarAnterior, rutaAnterior);
            }

            // Actualizamos los datos del pasaje.
            try (PreparedStatement ps = con.prepareStatement(sqlModificar)) {
                ps.setInt(1, pasaje.getID_pasajero());
                ps.setInt(2, pasaje.getID_lugar());
                ps.setInt(3, pasaje.getID_ruta());
                ps.setDate(4, Date.valueOf(pasaje.getFechaVenta()));
                ps.setTime(5, Time.valueOf(pasaje.getHoraVenta()));
                ps.setBigDecimal(6, pasaje.getPrecio());
                ps.setInt(7, pasaje.getEstado());
                ps.setInt(8, pasaje.getID_pasaje());

                if (ps.executeUpdate() != 1) {
                    throw new SQLException("No se pudo modificar el pasaje.");
                }
            }
            con.commit();
            return true;

        } catch (SQLException ex) {
            try {
                con.rollback();
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error al revertir la transacción al modificar el pasaje.", e);
            }
            LOGGER.log(Level.SEVERE, "Error al modificar el pasaje.", ex);
            return false;

        } finally {
            try {
                con.setAutoCommit(autoCommitAnterior);
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error al restaurar el autocommit al modificar el pasaje.", e);
            }
        }
    }

    // Baja lógica (estado = 0).
    public boolean darDeBaja(int id) {
        return cambiarEstado(id, 0);
    }

    // Alta lógica (estado = 1).
    public boolean darDeAlta(int id) {
        return cambiarEstado(id, 1);
    }

    // Busca un pasaje por su ID.
    public Pasaje buscarPasaje(int id) {
        String sql = "SELECT * FROM pasaje WHERE ID_pasaje = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return armarPasaje(rs);
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al buscar el pasaje.", ex);
        }
        return null;
    }

    // Lista todos los pasajes.
    public List<Pasaje> listarPasajes() {
        String sql = "SELECT * FROM pasaje";
        List<Pasaje> pasajes = new ArrayList<>();

        try (PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                pasajes.add(armarPasaje(rs));
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al listar los pasajes.", ex);
        }
        return pasajes;
    }

    // Lista los pasajes de un pasajero.
    public List<Pasaje> listarPasajesPorPasajero(int idPasajero) {
        String sql = "SELECT * FROM pasaje WHERE ID_pasajero = ?";
        return listarPasajesPorId(sql, idPasajero);
    }

    // Lista los pasajes de una ruta.
    public List<Pasaje> listarPasajesPorRuta(int idRuta) {
        String sql = "SELECT * FROM pasaje WHERE ID_ruta = ?";
        return listarPasajesPorId(sql, idRuta);
    }

    // Elimina un pasaje.
    public boolean eliminarPasaje(int id) {
        String sqlBuscar = "SELECT ID_lugar, ID_ruta, estado FROM pasaje WHERE ID_pasaje = ? FOR UPDATE";
        String sqlEliminar = "DELETE FROM pasaje WHERE ID_pasaje = ?";

        boolean autoCommitAnterior = true;

        try {
            autoCommitAnterior = con.getAutoCommit();
            con.setAutoCommit(false);

            int idLugar;
            int idRuta;
            int estado;

            // Buscamos los datos del pasaje.
            try (PreparedStatement ps = con.prepareStatement(sqlBuscar)) {
                ps.setInt(1, id);

                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        throw new SQLException("No se encontró el pasaje.");
                    }

                    idLugar = rs.getInt("ID_lugar");
                    idRuta = rs.getInt("ID_ruta");
                    estado = rs.getInt("estado");
                }
            }

            // Si el pasaje estaba activo, liberamos su asiento.
            if (estado == 1) {
                lugarData.desocuparLugar(con, idLugar, idRuta);
            }

            // Eliminamos el pasaje.
            try (PreparedStatement ps = con.prepareStatement(sqlEliminar)) {
                ps.setInt(1, id);

                if (ps.executeUpdate() != 1) {
                    throw new SQLException("No se pudo eliminar el pasaje.");
                }
            }
            con.commit();
            return true;

        } catch (SQLException ex) {
            try {
                con.rollback();
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error al revertir la transacción al eliminar el pasaje.", e);
            }
            LOGGER.log(Level.SEVERE, "Error al eliminar el pasaje.", ex);
            return false;

        } finally {
            try {
                con.setAutoCommit(autoCommitAnterior);
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error al restaurar el autocommit al eliminar el pasaje.", e);
            }
        }
    }

    // Comprueba si un asiento tiene algún pasaje activo.
    public boolean tienePasajeActivo(int idLugar) {
        String sql = "SELECT 1 FROM pasaje WHERE ID_lugar = ? AND estado = 1 LIMIT 1";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idLugar);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al comprobar los pasajes activos del asiento.", ex);
            return true;
        }
    }

    // Método auxiliar para listar pasajes según un ID.
    private List<Pasaje> listarPasajesPorId(String sql, int id) {
        List<Pasaje> pasajes = new ArrayList<>();

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    pasajes.add(armarPasaje(rs));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al listar los pasajes por ID.", ex);
        }
        return pasajes;
    }

    // Cambia el estado de un pasaje y actualiza la disponibilidad del asiento.
    private boolean cambiarEstado(int id, int estado) {
        if (estado != 0 && estado != 1) {
            return false;
        }

        String sqlBuscar = "SELECT ID_lugar, ID_ruta, estado FROM pasaje WHERE ID_pasaje = ? FOR UPDATE";
        String sqlModificar = "UPDATE pasaje SET estado = ? WHERE ID_pasaje = ?";

        boolean autoCommitAnterior = true;

        try {
            autoCommitAnterior = con.getAutoCommit();
            con.setAutoCommit(false);

            int idLugar;
            int idRuta;
            int estadoAnterior;

            // Buscamos los datos actuales del pasaje.
            try (PreparedStatement ps = con.prepareStatement(sqlBuscar)) {
                ps.setInt(1, id);

                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        throw new SQLException("No se encontró el pasaje.");
                    }

                    idLugar = rs.getInt("ID_lugar");
                    idRuta = rs.getInt("ID_ruta");
                    estadoAnterior = rs.getInt("estado");
                }
            }

            // Si el estado ya es el solicitado, no hacemos cambios.
            if (estadoAnterior == estado) {
                con.commit();
                return true;
            }

            // Actualizamos la disponibilidad del asiento.
            if (estado == 0) {
                lugarData.desocuparLugar(con, idLugar, idRuta);
            } else {
                lugarData.ocuparLugar(con, idLugar, idRuta);
            }

            // Actualizamos el estado del pasaje.
            try (PreparedStatement ps = con.prepareStatement(sqlModificar)) {
                ps.setInt(1, estado);
                ps.setInt(2, id);

                if (ps.executeUpdate() != 1) {
                    throw new SQLException("No se pudo cambiar el estado del pasaje.");
                }
            }
            con.commit();
            return true;

        } catch (SQLException ex) {
            try {
                con.rollback();
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error al revertir la transacción al cambiar el estado del pasaje.", e);
            }
            LOGGER.log(Level.SEVERE, "Error al cambiar el estado del pasaje.", ex);
            return false;

        } finally {
            try {
                con.setAutoCommit(autoCommitAnterior);
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error al restaurar el autocommit al cambiar el estado del pasaje.", e);
            }
        }
    }

    // Convierte una fila del ResultSet en un objeto Pasaje.
    private Pasaje armarPasaje(ResultSet rs) throws SQLException {
        Pasaje pasaje = new Pasaje();

        pasaje.setID_pasaje(rs.getInt("ID_pasaje"));
        pasaje.setID_pasajero(rs.getInt("ID_pasajero"));
        pasaje.setID_lugar(rs.getInt("ID_lugar"));
        pasaje.setID_ruta(rs.getInt("ID_ruta"));
        pasaje.setFechaVenta(rs.getDate("fechaVenta").toLocalDate());
        pasaje.setHoraVenta(rs.getTime("horaVenta").toLocalTime());
        pasaje.setPrecio(rs.getBigDecimal("precio"));
        pasaje.setEstado(rs.getInt("estado"));

        return pasaje;
    }
}
