package Persistencia;

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
import java.util.logging.Level;
import java.util.logging.Logger;

public class RutaViajeData {

    private Connection con = null;
    private static final Logger LOGGER = Logger.getLogger(RutaViajeData.class.getName());

    public RutaViajeData() {
        con = Conexion.getConexion();
    }

    // Guarda una ruta y genera todos sus asientos.
    public boolean guardarRutaViaje(RutaViaje r) {
        String sqlRuta = "INSERT INTO rutaViaje (origen, destino, fechaSalida, fechaLlegada, horaSalida, horaLlegada, ID_colectivo, precio, estado) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String sqlCapacidad = "SELECT capacidad FROM colectivo WHERE ID_colectivo = ?";
        String sqlAsientos = "INSERT INTO lugar (ID_ruta, numeroAsiento, estado) VALUES (?, ?, ?)";

        boolean autoCommitOriginal = true;
        boolean transaccionIniciada = false;

        try {
            if (con == null) {
                return false;
            }

            autoCommitOriginal = con.getAutoCommit();
            con.setAutoCommit(false);
            transaccionIniciada = true;

            // Obtener la capacidad del colectivo.
            int capacidad;

            try (PreparedStatement ps = con.prepareStatement(sqlCapacidad)) {
                ps.setInt(1, r.getID_colectivo());

                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        throw new SQLException("No se encontró el colectivo.");
                    }
                    capacidad = rs.getInt("capacidad");
                }
            }

            if (capacidad <= 0) {
                throw new SQLException("La capacidad del colectivo no es válida.");
            }

            // Insertar la ruta.
            int idGenerado;

            try (PreparedStatement ps = con.prepareStatement(sqlRuta, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, r.getOrigen());
                ps.setString(2, r.getDestino());
                ps.setDate(3, Date.valueOf(r.getFechaSalida()));
                ps.setDate(4, Date.valueOf(r.getFechaLlegada()));
                ps.setTime(5, Time.valueOf(r.getHoraSalida()));
                ps.setTime(6, Time.valueOf(r.getHoraLlegada()));
                ps.setInt(7, r.getID_colectivo());
                ps.setBigDecimal(8, r.getPrecio());
                ps.setInt(9, r.getEstado());

                if (ps.executeUpdate() != 1) {
                    throw new SQLException("No se pudo guardar la ruta.");
                }

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (!rs.next()) {
                        throw new SQLException("No se pudo obtener el ID de la ruta.");
                    }
                    idGenerado = rs.getInt(1);
                }
            }

            // Generar todos los asientos de la ruta.
            try (PreparedStatement ps = con.prepareStatement(sqlAsientos)) {

                for (int i = 1; i <= capacidad; i++) {
                    ps.setInt(1, idGenerado);
                    ps.setInt(2, i);
                    ps.setInt(3, LugarData.LIBRE);

                    if (ps.executeUpdate() != 1) {
                        throw new SQLException("No se pudo generar el asiento " + i);
                    }
                }
            }

            // Confirmar la operación completa.
            con.commit();

            r.setID_ruta(idGenerado);
            return true;

        } catch (SQLException ex) {
            if (transaccionIniciada) {
                try {
                    con.rollback();
                } catch (SQLException e) {
                    LOGGER.log(Level.SEVERE, "Error al revertir la transacción al guardar una ruta.", e);
                }
            }
            return false;

        } finally {
            if (transaccionIniciada) {
                try {
                    con.setAutoCommit(autoCommitOriginal);
                } catch (SQLException ex) {
                    LOGGER.log(Level.SEVERE, "Error al restaurar el autocommit de la conexión.", ex);
                }
            }
        }
    }

    // Modifica los datos de una ruta.
    public boolean modificarRutaViaje(RutaViaje ruta) {
        String sql = "UPDATE rutaViaje SET origen = ?, destino = ?, fechaSalida = ?, fechaLlegada = ?, horaSalida = ?, horaLlegada = ?, ID_colectivo = ?, precio = ?, estado = ? WHERE ID_ruta = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, ruta.getOrigen());
            ps.setString(2, ruta.getDestino());
            ps.setDate(3, Date.valueOf(ruta.getFechaSalida()));
            ps.setDate(4, Date.valueOf(ruta.getFechaLlegada()));
            ps.setTime(5, Time.valueOf(ruta.getHoraSalida()));
            ps.setTime(6, Time.valueOf(ruta.getHoraLlegada()));
            ps.setInt(7, ruta.getID_colectivo());
            ps.setBigDecimal(8, ruta.getPrecio());
            ps.setInt(9, ruta.getEstado());
            ps.setInt(10, ruta.getID_ruta());

            return ps.executeUpdate() == 1;

        } catch (SQLException ex) {
            return false;
        }
    }

    // Da de baja lógica una ruta.
    public boolean bajaLogicaRuta(int id) {
        return cambiarEstadoRuta(id, 0);
    }

    // Da de alta lógica una ruta.
    public boolean altaLogicaRuta(int id) {
        return cambiarEstadoRuta(id, 1);
    }

    // Cambia el estado de una ruta.
    private boolean cambiarEstadoRuta(int id, int estado) {
        String sql = "UPDATE rutaViaje SET estado = ? WHERE ID_ruta = ? AND estado <> ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, estado);
            ps.setInt(2, id);
            ps.setInt(3, estado);

            return ps.executeUpdate() == 1;

        } catch (SQLException ex) {
            return false;
        }
    }

    // Busca una ruta por su ID.
    public RutaViaje buscarRuta(int id) {
        String sql = "SELECT * FROM rutaViaje WHERE ID_ruta = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return armarRuta(rs);
                }
            }

        } catch (SQLException ex) {
            return null;
        }
        return null;
    }

    // Busca rutas por destino.
    public List<RutaViaje> buscarRutasPorDestino(String destinoBuscado) {
        String sql = "SELECT * FROM rutaViaje WHERE destino LIKE ?";
        List<RutaViaje> rutas = new ArrayList<>();

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + destinoBuscado + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rutas.add(armarRuta(rs));
                }
            }

        } catch (SQLException ex) {
            return rutas;
        }
        return rutas;
    }

    // Lista todas las rutas.
    public List<RutaViaje> listarRutas() {
        String sql = "SELECT * FROM rutaViaje";
        List<RutaViaje> rutas = new ArrayList<>();

        try (PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                rutas.add(armarRuta(rs));
            }

        } catch (SQLException ex) {
            return rutas;
        }
        return rutas;
    }

    // Convierte una fila del ResultSet en un objeto RutaViaje.
    private RutaViaje armarRuta(ResultSet rs) throws SQLException {
        RutaViaje ruta = new RutaViaje();

        ruta.setID_ruta(rs.getInt("ID_ruta"));
        ruta.setOrigen(rs.getString("origen"));
        ruta.setDestino(rs.getString("destino"));
        ruta.setFechaSalida(rs.getDate("fechaSalida").toLocalDate());
        ruta.setFechaLlegada(rs.getDate("fechaLlegada").toLocalDate());
        ruta.setHoraSalida(rs.getTime("horaSalida").toLocalTime());
        ruta.setHoraLlegada(rs.getTime("horaLlegada").toLocalTime());
        ruta.setID_colectivo(rs.getInt("ID_colectivo"));
        ruta.setPrecio(rs.getBigDecimal("precio"));
        ruta.setEstado(rs.getInt("estado"));

        return ruta;
    }
}
