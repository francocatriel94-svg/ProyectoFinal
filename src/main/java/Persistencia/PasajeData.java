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
import javax.swing.JOptionPane;

public class PasajeData {

    private Connection con;

    public PasajeData() {
        con = Conexion.getConexion();
    }

    // Crea un pasaje.
    public void guardarPasaje(Pasaje pasaje) {
        String sql = "INSERT INTO pasaje (ID_pasajero, ID_lugar, ID_ruta, fechaVenta, horaVenta, precio, estado) " + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        String sqlOcuparAsiento = "UPDATE lugar SET estado = 0 " + "WHERE ID_lugar = ? AND ID_ruta = ? AND estado = 1";

        boolean autoCommitAnterior = true;

        try {
            autoCommitAnterior = con.getAutoCommit();
            con.setAutoCommit(false);

            //Primero intentamos reservar el asiento.
            try (PreparedStatement ps = con.prepareStatement(sqlOcuparAsiento)) {
                ps.setInt(1, pasaje.getID_lugar());
                ps.setInt(2, pasaje.getID_ruta());

                if (ps.executeUpdate() != 1) {
                    throw new SQLException("El asiento no existe, no pertenece a esta ruta o ya está ocupado.");
                }
            }

            //Si el asiento se reservó, guardamos el pasaje.
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
                    } else {
                        throw new SQLException("No se pudo obtener el ID del pasaje.");
                    }
                }
            }

            con.commit();
            JOptionPane.showMessageDialog(null, "Pasaje guardado y asiento reservado con éxito.");

        } catch (SQLException ex) {
            try {
                con.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            JOptionPane.showMessageDialog(null, "Error al guardar el pasaje: " + ex.getMessage());

        } finally {
            try {
                con.setAutoCommit(autoCommitAnterior);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    //Busca pasaje por su id.
    public Pasaje buscarPasaje(int id) {
        String sql = "SELECT * FROM pasaje WHERE ID_pasaje = ?";
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

    //Lista todos los pasajes.
    public List<Pasaje> listarPasajes() {
        String sql = "SELECT * FROM pasaje";
        List<Pasaje> pasajes = new ArrayList<>();

        try (PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                pasajes.add(armarPasaje(rs));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al listar los pasajes: " + ex.getMessage());
        }
        return pasajes;
    }

    //Lista los pasajes de un pasajero.
    public List<Pasaje> listarPasajesPorPasajero(int idPasajero) {
        String sql = "SELECT * FROM pasaje WHERE ID_pasajero = ?";
        return listarPorId(sql, idPasajero, "del pasajero");
    }

    //Pasajes de una ruta.
    public List<Pasaje> listarPasajesPorRuta(int idRuta) {
        String sql = "SELECT * FROM pasaje WHERE ID_ruta = ?";
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

    //Modificamos los valores de los atributos de un pasaje
    public void modificarPasaje(Pasaje pasaje) {
        String sqlBuscar = "SELECT ID_lugar, ID_ruta, estado FROM pasaje " + "WHERE ID_pasaje = ? FOR UPDATE";
        String sqlModificar = "UPDATE pasaje SET ID_pasajero = ?, ID_lugar = ?, " + "ID_ruta = ?, fechaVenta = ?, horaVenta = ?, " + "precio = ?, estado = ? WHERE ID_pasaje = ?";

        boolean autoCommitAnterior = true;

        try {
            autoCommitAnterior = con.getAutoCommit();
            con.setAutoCommit(false);

            int lugarAnterior;
            int rutaAnterior;
            int estadoAnterior;

            //Obtenemos los datos actuales del pasaje.
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

            boolean asientoCambio = lugarAnterior != pasaje.getID_lugar()
                    || rutaAnterior != pasaje.getID_ruta();

            //Si el pasaje queda activo y cambia el asiento, primero reservamos el nuevo.
            if (pasaje.getEstado() == 1
                    && (estadoAnterior == 0 || asientoCambio)) {

                ocuparAsiento(pasaje.getID_lugar(), pasaje.getID_ruta());
            }

            //Si el pasaje anterior estaba activo y cambia de asiento o se da de baja, liberamos el anterior.
            if (estadoAnterior == 1
                    && (pasaje.getEstado() == 0 || asientoCambio)) {

                liberarAsiento(lugarAnterior, rutaAnterior);
            }

            try (PreparedStatement ps = con.prepareStatement(sqlModificar)) {
                ps.setInt(1, pasaje.getID_pasajero());
                ps.setInt(2, pasaje.getID_lugar());
                ps.setInt(3, pasaje.getID_ruta());
                ps.setDate(4, Date.valueOf(pasaje.getFechaVenta()));
                ps.setTime(5, Time.valueOf(pasaje.getHoraVenta()));
                ps.setDouble(6, pasaje.getPrecio());
                ps.setInt(7, pasaje.getEstado());
                ps.setInt(8, pasaje.getID_pasaje());

                if (ps.executeUpdate() != 1) {
                    throw new SQLException("No se pudo modificar el pasaje.");
                }
            }

            con.commit();
            JOptionPane.showMessageDialog(null, "Pasaje modificado.");

        } catch (SQLException ex) {
            try {
                con.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            JOptionPane.showMessageDialog(null, "Error al modificar el pasaje: " + ex.getMessage());

        } finally {
            try {
                con.setAutoCommit(autoCommitAnterior);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    //Baja lógica (estado = 0).
    public void darDeBaja(int id) {
        cambiarEstado(id, 0, "Pasaje dado de baja.");
    }

    //Alta lógica (estado = 1).
    public void darDeAlta(int id) {
        cambiarEstado(id, 1, "Pasaje dado de alta.");
    }

    private void cambiarEstado(int id, int estado, String mensaje) {
        String sqlBuscar = "SELECT ID_lugar, ID_ruta, estado FROM pasaje " + "WHERE ID_pasaje = ? FOR UPDATE";
        String sqlModificar = "UPDATE pasaje SET estado = ? WHERE ID_pasaje = ?";

        boolean autoCommitAnterior = true;

        try {
            autoCommitAnterior = con.getAutoCommit();
            con.setAutoCommit(false);

            int idLugar;
            int idRuta;
            int estadoAnterior;

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

            if (estadoAnterior != estado) {
                if (estado == 0) {
                    liberarAsiento(idLugar, idRuta);
                } else {
                    ocuparAsiento(idLugar, idRuta);
                }
            }

            try (PreparedStatement ps = con.prepareStatement(sqlModificar)) {
                ps.setInt(1, estado);
                ps.setInt(2, id);

                if (ps.executeUpdate() != 1) {
                    throw new SQLException("No se pudo cambiar el estado del pasaje.");
                }
            }

            con.commit();
            JOptionPane.showMessageDialog(null, mensaje);

        } catch (SQLException ex) {
            try {
                con.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            JOptionPane.showMessageDialog(null, "Error al cambiar el estado: " + ex.getMessage());

        } finally {
            try {
                con.setAutoCommit(autoCommitAnterior);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    //Borra un pasaje.
    public void eliminarPasaje(int id) {
        String sqlBuscar = "SELECT ID_lugar, ID_ruta, estado FROM pasaje " + "WHERE ID_pasaje = ? FOR UPDATE";
        String sqlEliminar = "DELETE FROM pasaje WHERE ID_pasaje = ?";

        boolean autoCommitAnterior = true;

        try {
            autoCommitAnterior = con.getAutoCommit();
            con.setAutoCommit(false);

            int idLugar;
            int idRuta;
            int estado;

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

            if (estado == 1) {
                liberarAsiento(idLugar, idRuta);
            }

            try (PreparedStatement ps = con.prepareStatement(sqlEliminar)) {
                ps.setInt(1, id);

                if (ps.executeUpdate() != 1) {
                    throw new SQLException("No se pudo eliminar el pasaje.");
                }
            }

            con.commit();
            JOptionPane.showMessageDialog(null, "Pasaje eliminado.");

        } catch (SQLException ex) {
            try {
                con.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            JOptionPane.showMessageDialog(null, "Error al eliminar el pasaje: " + ex.getMessage());

        } finally {
            try {
                con.setAutoCommit(autoCommitAnterior);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    //Convierte una fila del ResultSet en un Pasaje (trae los objetos con sus Data).
    private Pasaje armarPasaje(ResultSet rs) throws SQLException {
        Pasaje pasaje = new Pasaje();

        pasaje.setID_pasaje(rs.getInt("ID_pasaje"));
        pasaje.setID_pasajero(rs.getInt("ID_pasajero"));
        pasaje.setID_lugar(rs.getInt("ID_lugar"));
        pasaje.setID_ruta(rs.getInt("ID_ruta"));
        pasaje.setFechaVenta(rs.getDate("fechaVenta").toLocalDate());
        pasaje.setHoraVenta(rs.getTime("horaVenta").toLocalTime());
        pasaje.setPrecio(rs.getDouble("precio"));
        pasaje.setEstado(rs.getInt("estado"));

        return pasaje;
    }

    private void ocuparAsiento(int idLugar, int idRuta) throws SQLException {
        String sql = "UPDATE lugar SET estado = 0 " + "WHERE ID_lugar = ? AND ID_ruta = ? AND estado = 1";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idLugar);
            ps.setInt(2, idRuta);

            if (ps.executeUpdate() != 1) {
                throw new SQLException("El nuevo asiento no está disponible.");
            }
        }
    }

    private void liberarAsiento(int idLugar, int idRuta) throws SQLException {
        String sql = "UPDATE lugar SET estado = 1 " + "WHERE ID_lugar = ? AND ID_ruta = ? AND estado = 0";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idLugar);
            ps.setInt(2, idRuta);

            if (ps.executeUpdate() != 1) {
                throw new SQLException("No se pudo liberar el asiento anterior.");
            }
        }
    }
}
