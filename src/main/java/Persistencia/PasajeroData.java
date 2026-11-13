package Persistencia;

import Modelo.Pasajero;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PasajeroData {

    private Connection con = null;

    public PasajeroData() {
        con = Conexion.getConexion();
    }

    public static final int TODOS = -1;

    // Guarda un pasajero creado.
    public boolean guardarPasajero(Pasajero p) {
        String sql = "INSERT INTO pasajero (dni, nombre, apellido, correo, telefono, estado) VALUES (?, ?, ?, ?, ?, ?)";

        try {
            if (existeDniEnOtroPasajero(p.getDni(), 0)) {
                return false;
            }

            try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, p.getDni().replace(".", "").trim());
                ps.setString(2, p.getNombre());
                ps.setString(3, p.getApellido());
                ps.setString(4, p.getCorreo());
                ps.setString(5, p.getTelefono());
                ps.setInt(6, p.getEstado());

                if (ps.executeUpdate() != 1) {
                    return false;
                }

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        p.setID_pasajero(rs.getInt(1));
                    }
                }

                return true;
            }
        } catch (SQLException ex) {
            return false;
        }
    }

    // Modifica los datos de un pasajero.
    public boolean modificarPasajero(Pasajero p) {
        String sql = "UPDATE pasajero SET dni = ?, nombre = ?, apellido = ?, correo = ?, telefono = ? WHERE ID_pasajero = ?";

        try {
            if (existeDniEnOtroPasajero(p.getDni(), p.getID_pasajero())) {
                return false;
            }

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, p.getDni().replace(".", "").trim());
                ps.setString(2, p.getNombre());
                ps.setString(3, p.getApellido());
                ps.setString(4, p.getCorreo());
                ps.setString(5, p.getTelefono());
                ps.setInt(6, p.getID_pasajero());

                return ps.executeUpdate() == 1;
            }
        } catch (SQLException ex) {
            return false;
        }
    }

    // Cambia el estado de un pasajero.
    private boolean cambiarEstado(int id, int estado) {
        String sql = "UPDATE pasajero SET estado = ? WHERE ID_pasajero = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, estado);
            ps.setInt(2, id);

            return ps.executeUpdate() == 1;

        } catch (SQLException ex) {
            return false;
        }
    }

    // Da de baja a un pasajero.  
    public boolean bajaLogicaPasajero(int id) {
        return cambiarEstado(id, 0);
    }

    // Da de alta a un pasajero.
    public boolean altaLogicaPasajero(int id) {
        return cambiarEstado(id, 1);
    }

    // Busca un pasajero por ID, independientemente de su estado.
    public Pasajero buscarPasajero(int id) {
        return buscarPasajero(id, TODOS);
    }

    // Busca un pasajero por ID y estado.
    public Pasajero buscarPasajero(int id, int estado) {
        String sql = "SELECT * FROM pasajero WHERE ID_pasajero = ?";

        if (estado != TODOS) {
            sql += " AND estado = ?";
        }

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);

            if (estado != TODOS) {
                ps.setInt(2, estado);
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return armarPasajero(rs);
                }
            }
        } catch (SQLException ex) {
            return null;
        }
        return null;
    }

    // Busca un pasajero por DNI, independientemente de su estado.
    public Pasajero buscarPasajeroPorDni(String dni) {
        return buscarPasajeroPorDni(dni, TODOS);
    }

    // Busca un pasajero por DNI y estado.
    public Pasajero buscarPasajeroPorDni(String dni, int estado) {
        String sql = "SELECT * FROM pasajero WHERE REPLACE(TRIM(dni), '.', '') = REPLACE(TRIM(?), '.', '')";

        if (estado != TODOS) {
            sql += " AND estado = ?";
        }

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dni);

            if (estado != TODOS) {
                ps.setInt(2, estado);
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return armarPasajero(rs);
                }
            }
        } catch (SQLException ex) {
            return null;
        }
        return null;
    }

    // Lista todos los pasajeros.
    public List<Pasajero> listarTodosLosPasajeros() {
        List<Pasajero> pasajeros = new ArrayList<>();

        String sql = "SELECT * FROM pasajero";

        try (PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                pasajeros.add(armarPasajero(rs));
            }
        } catch (SQLException ex) {
            return pasajeros;
        }
        return pasajeros;
    }

    // Lista los pasajeros activos.
    public List<Pasajero> listarPasajerosActivos() {
        String sql = "SELECT * FROM pasajero WHERE estado = 1";
        return listarPorEstado(sql);
    }

    // Lista los pasajeros inactivos.
    public List<Pasajero> listarPasajerosInactivos() {
        String sql = "SELECT * FROM pasajero WHERE estado = 0";
        return listarPorEstado(sql);
    }

    // Método auxiliar para listar según el estado.
    private List<Pasajero> listarPorEstado(String sql) {
        List<Pasajero> pasajeros = new ArrayList<>();

        try (PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                pasajeros.add(armarPasajero(rs));
            }
        } catch (SQLException ex) {
            return pasajeros;
        }
        return pasajeros;
    }

    // Comprueba si un DNI pertenece a otro pasajero. Se utiliza tanto al guardar como al modificar.
    private boolean existeDniEnOtroPasajero(String dni, int idPasajero) throws SQLException {
        String sql = "SELECT 1 FROM pasajero " + "WHERE REPLACE(TRIM(dni), '.', '') " + "= REPLACE(TRIM(?), '.', '') " + "AND ID_pasajero != ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dni);
            ps.setInt(2, idPasajero);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    // Convierte una fila del ResultSet en un objeto Pasajero.
    private Pasajero armarPasajero(ResultSet rs) throws SQLException {
        Pasajero p = new Pasajero();

        p.setID_pasajero(rs.getInt("ID_pasajero"));
        p.setDni(rs.getString("dni"));
        p.setNombre(rs.getString("nombre"));
        p.setApellido(rs.getString("apellido"));
        p.setCorreo(rs.getString("correo"));
        p.setTelefono(rs.getString("telefono"));
        p.setEstado(rs.getInt("estado"));

        return p;
    }
}
