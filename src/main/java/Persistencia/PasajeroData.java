package Persistencia;

import Modelo.Pasajero;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class PasajeroData {

    private Connection con = null;

    public PasajeroData() {
        con = Conexion.getConexion();
    }

    public void guardarPasajero(Pasajero p) {
        String sql = "INSERT INTO pasajeros (dni, nombre, apellido, correo, telefono, estado) VALUES (?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, p.getDni());
            ps.setString(2, p.getNombre());
            ps.setString(3, p.getApellido());
            ps.setString(4, p.getCorreo());
            ps.setString(5, p.getTelefono());
            ps.setInt(6, p.getEstado());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                p.setID_pasajero(rs.getInt(1));
                JOptionPane.showMessageDialog(null, "Se guardo al Pasajero con éxito. ID: " + p.getID_pasajero());
            } else {
                JOptionPane.showMessageDialog(null, "No se pudo obtener el ID del pasajero.");
            }
            ps.close();

        } catch (SQLException ex) {
            Logger.getLogger(PasajeroData.class.getName()).log(Level.SEVERE, "Error al guardar pasajero", ex);
            JOptionPane.showMessageDialog(null, "Error al insertar en la base de datos: " + ex.getMessage());
        }
    }

    public void bajaLogicaPasajero(int id) {
        String sql = "UPDATE pasajeros SET estado = 0 WHERE ID_pasajero = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);

            int filas = ps.executeUpdate();
            if (filas > 0) {
                JOptionPane.showMessageDialog(null, "Pasajero dado de baja lógicamente.");
            }
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al dar de baja al pasajero: " + ex.getMessage());
        }
    }

    public void modificarPasajero(Pasajero p) {
        String sql = "UPDATE pasajeros SET dni = ?, nombre = ?, apellido = ?, correo = ?, telefono = ? WHERE ID_pasajero = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, p.getDni());
            ps.setString(2, p.getNombre());
            ps.setString(3, p.getApellido());
            ps.setString(4, p.getCorreo());
            ps.setString(5, p.getTelefono());
            ps.setInt(6, p.getID_pasajero());

            int filas = ps.executeUpdate();
            if (filas > 0) {
                JOptionPane.showMessageDialog(null, "Pasajero modificado con éxito.");
            }
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al modificar pasajero: " + ex.getMessage());
        }
    }

    public Pasajero buscarPasajero(int id) {
        String sql = "SELECT dni, nombre, apellido, correo, telefono, estado FROM pasajeros WHERE ID_pasajero = ?";
        Pasajero pasajero = null;

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                pasajero = new Pasajero();
                pasajero.setID_pasajero(id);
                pasajero.setDni(rs.getString("dni"));
                pasajero.setNombre(rs.getString("nombre"));
                pasajero.setApellido(rs.getString("apellido"));
                pasajero.setCorreo(rs.getString("correo"));
                pasajero.setTelefono(rs.getString("telefono"));
                pasajero.setEstado(rs.getInt("estado"));
            } else {
                JOptionPane.showMessageDialog(null, "No existe un pasajero con ese ID.");
            }
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al buscar pasajero: " + ex.getMessage());
        }
        return pasajero;
    }

    public List<Pasajero> listarPasajeros() {
        String sql = "SELECT ID_pasajero, dni, nombre, apellido, correo, telefono, estado FROM pasajeros WHERE estado = 1";
        List<Pasajero> pasajeros = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Pasajero p = new Pasajero();
                p.setID_pasajero(rs.getInt("ID_pasajero"));
                p.setDni(rs.getString("dni"));
                p.setNombre(rs.getString("nombre"));
                p.setApellido(rs.getString("apellido"));
                p.setCorreo(rs.getString("correo"));
                p.setTelefono(rs.getString("telefono"));
                p.setEstado(rs.getInt("estado"));

                pasajeros.add(p);
            }
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al listar pasajeros: " + ex.getMessage());
        }
        return pasajeros;
    }
}
