package Persistencia;

import Modelo.Pasajero;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class PasajeroData {

    private Connection con = null;

    public PasajeroData() {
        con = Conexion.getConexion();
    }

    // Guarda un pasajero.
    public void guardarPasajero(Pasajero p) {
        String sql = "INSERT INTO pasajero " + "(dni, nombre, apellido, correo, telefono, estado) " + "VALUES (?, ?, ?, ?, ?, ?)";

        try {
            if (existeDniEnOtroPasajero(p.getDni(), 0)) {
                JOptionPane.showMessageDialog(null, "Ya existe un pasajero registrado con ese DNI.");
                return;
            }

            try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                ps.setString(1, p.getDni().replace(".", "").trim());
                ps.setString(2, p.getNombre());
                ps.setString(3, p.getApellido());
                ps.setString(4, p.getCorreo());
                ps.setString(5, p.getTelefono());
                ps.setInt(6, p.getEstado());

                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        p.setID_pasajero(rs.getInt(1));
                        JOptionPane.showMessageDialog(null, "Pasajero guardado con éxito. ID: " + p.getID_pasajero());
                    }
                }
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al guardar pasajero: " + ex.getMessage());
        }
    }

    // Da de baja lógica a un pasajero.
    public void bajaLogicaPasajero(int id) {
        cambiarEstado(id, 0, "Pasajero dado de baja.");
    }

    // Reactiva un pasajero.
    public void altaLogicaPasajero(int id) {
        cambiarEstado(id, 1, "Pasajero reactivado con éxito.");
    }

    private void cambiarEstado(int id, int estado, String mensaje) {
        String sql = "UPDATE pasajero SET estado = ? WHERE ID_pasajero = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, estado);
            ps.setInt(2, id);

            if (ps.executeUpdate() == 1) {
                JOptionPane.showMessageDialog(null, mensaje);
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró el pasajero.");
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al cambiar el estado del pasajero: " + ex.getMessage());
        }
    }

    // Modifica los datos de un pasajero.
    public void modificarPasajero(Pasajero p) {
        String sql = "UPDATE pasajero SET dni = ?, nombre = ?, " + "apellido = ?, correo = ?, telefono = ? " + "WHERE ID_pasajero = ?";

        try {
            // Comprueba que el DNI no pertenezca a otro pasajero.
            if (existeDniEnOtroPasajero(p.getDni(), p.getID_pasajero())) {
                JOptionPane.showMessageDialog(null, "Ese DNI ya pertenece a otro pasajero.");
                return;
            }

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, p.getDni().replace(".", "").trim());
                ps.setString(2, p.getNombre());
                ps.setString(3, p.getApellido());
                ps.setString(4, p.getCorreo());
                ps.setString(5, p.getTelefono());
                ps.setInt(6, p.getID_pasajero());

                if (ps.executeUpdate() == 1) {
                    JOptionPane.showMessageDialog(null, "Pasajero modificado con éxito.");
                } else {
                    JOptionPane.showMessageDialog(null, "No se encontró el pasajero.");
                }
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al modificar pasajero: " + ex.getMessage());
        }
    }

    // Busca un pasajero por su ID.
    public Pasajero buscarPasajero(int id) {
        String sql = "SELECT * FROM pasajero WHERE ID_pasajero = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return armarPasajero(rs);
                }
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al buscar pasajero: " + ex.getMessage());
        }
        return null;
    }

    // Busca un pasajero por su DNI.
    public Pasajero buscarPasajeroPorDni(String dni) {
        String sql = "SELECT * FROM pasajero " + "WHERE REPLACE(TRIM(dni), '.', '') " + "= REPLACE(TRIM(?), '.', '')";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dni);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return armarPasajero(rs);
                }
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al buscar pasajero por DNI: " + ex.getMessage());
        }
        return null;
    }

    // Comprueba si un DNI pertenece a otro pasajero.
    // Se utiliza tanto al guardar como al modificar.
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

    // Lista los pasajeros activos.
    public List<Pasajero> listarPasajeros() {
        String sql = "SELECT * FROM pasajero WHERE estado = 1";
        return listarPorEstado(sql);
    }

    // Busca un pasajero inactivo por ID.
    public Pasajero buscarPasajeroInactivo(int id) {
        String sql = "SELECT * FROM pasajero " + "WHERE ID_pasajero = ? AND estado = 0";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return armarPasajero(rs);
                }
            }

            JOptionPane.showMessageDialog(null, "No existe un pasajero inactivo con ese ID.");

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al buscar pasajero inactivo: " + ex.getMessage());
        }
        return null;
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
            JOptionPane.showMessageDialog(null, "Error al listar pasajeros: " + ex.getMessage());
        }
        return pasajeros;
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
