package Persistencia;

import Modelo.Colectivo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class ColectivoData {

    private Connection con;

    public ColectivoData() {
        con = Conexion.getConexion();
    }

    //Guarda un colectivo creado.
    public boolean guardarColectivo(Colectivo colectivo) {
        String sql = "INSERT INTO colectivo (matricula, marca, modelo, capacidad, chofer, estado)" + "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, colectivo.getMatricula());
            ps.setString(2, colectivo.getMarca());
            ps.setString(3, colectivo.getModelo());
            ps.setInt(4, colectivo.getCapacidad());
            ps.setString(5, colectivo.getChofer());
            ps.setInt(6, colectivo.getEstado());

            if (ps.executeUpdate() != 1) {
                return false;
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    colectivo.setID_colectivo(rs.getInt(1));
                }
            }
            return true;

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al guardar el colectivo: " + ex.getMessage());
            return false;
        }
    }

    //Modificamos los valores de un colectico.
    public boolean modificarColectivo(Colectivo colectivo) {
        String sql = "UPDATE colectivo SET matricula = ?, marca = ?, modelo = ?, capacidad = ?, " + "chofer = ?, estado = ? WHERE ID_colectivo = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, colectivo.getMatricula());
            ps.setString(2, colectivo.getMarca());
            ps.setString(3, colectivo.getModelo());
            ps.setInt(4, colectivo.getCapacidad());
            ps.setString(5, colectivo.getChofer());
            ps.setInt(6, colectivo.getEstado());
            ps.setInt(7, colectivo.getID_colectivo());

            return ps.executeUpdate() == 1;

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al modificar el colectivo: " + ex.getMessage());
            return false;
        }
    }

    //Cambiamos el estado de un colectivo.
    private boolean cambiarEstado(int id, int estado) {
        String sql = "UPDATE colectivo SET estado = ? WHERE ID_colectivo = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, estado);
            ps.setInt(2, id);

            return ps.executeUpdate() == 1;
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al cambiar el estado del colectivo: " + ex.getMessage());
            return false;
        }
    }

    //Baja lógica (estado = 0).
    public boolean darDeBaja(int id) {
        return cambiarEstado(id, 0);
    }

    //Alta lógica (estado = 1).
    public boolean darDeAlta(int id) {
        return cambiarEstado(id, 1);
    }
    
    //Borramos un colectivo.
    public boolean eliminarColectivo(int id) {
         String sql = "DELETE FROM colectivo WHERE ID_colectivo = ? AND estado = 0 " + "AND NOT EXISTS (SELECT 1 FROM rutaViaje WHERE ID_colectivo = ?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setInt(2, id);

            return ps.executeUpdate() == 1;
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al eliminar el colectivo: " + ex.getMessage());
            return false;
        }
    }
    
    //Buscamos un colectivo por su ID.
    public Colectivo buscarColectivo(int id) {
        String sql = "SELECT * FROM colectivo WHERE ID_colectivo = ?";
        Colectivo colectivo = null;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    colectivo = armarColectivo(rs);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al buscar el colectivo: " + ex.getMessage());
        }
        return colectivo;
    }
    
    //Buscamos un colectivo por su Matricula.
    public Colectivo buscarColectivoPorMatricula(String matricula) {
        String sql = "SELECT * FROM colectivo WHERE matricula LIKE ?";
        Colectivo colectivo = null;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, matricula);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    colectivo = armarColectivo(rs);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al buscar el colectivo: " + ex.getMessage());
        }
        return colectivo;
    }
    
    //Lista todos los colectivos.
    public List<Colectivo> listarColectivos() {
        String sql = "SELECT * FROM colectivo";
        return listar(sql);
    }
    
    //Lista colectivos segun una Matricula.
    public List<Colectivo> ListarColectivosPorMatricula(String matricula) {
        String sql = "SELECT * FROM colectivo WHERE matricula LIKE ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + matricula + "%");

            try (ResultSet rs = ps.executeQuery()) {
                List<Colectivo> lista = new ArrayList<>();
                while (rs.next()) {
                    lista.add(armarColectivo(rs));
                }
                return lista;
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al buscar colectivos: " + ex.getMessage());
        }
        return new ArrayList<>();
    }
    
    //Lista los colectivos activos(estado = 1).
    public List<Colectivo> listarColectivosActivos() {
        String sql = "SELECT * FROM colectivo WHERE estado = 1";
        return listar(sql);
    }
    
    private List<Colectivo> listar(String sql) {
        List<Colectivo> colectivos = new ArrayList<>();

        try (PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                colectivos.add(armarColectivo(rs));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al listar los colectivos: " + ex.getMessage());
        }
        return colectivos;
    }
    
    private Colectivo armarColectivo(ResultSet rs) throws SQLException {
        Colectivo colectivo = new Colectivo();
        colectivo.setID_colectivo(rs.getInt("ID_colectivo"));
        colectivo.setMatricula(rs.getString("matricula"));
        colectivo.setMarca(rs.getString("marca"));
        colectivo.setModelo(rs.getString("modelo"));
        colectivo.setCapacidad(rs.getInt("capacidad"));
        colectivo.setChofer(rs.getString("chofer"));
        colectivo.setEstado(rs.getInt("estado"));
        return colectivo;
    }
}
