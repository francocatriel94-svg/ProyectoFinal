package Persistencia;

import Modelo.Colectivo;
import Modelo.RutaViaje;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class RutaViajeData {

    private Connection con = null;

    public RutaViajeData() {
        con = Conexion.getConexion();
    }

    public void guardarRutaViaje(RutaViaje r) {
        String sql = "INSERT INTO rutas (origen, destino, horaSalida, horaLlegada, ID_colectivo, precio, estado) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, r.getOrigen());
            ps.setString(2, r.getDestino());
            ps.setObject(3, r.getHoraSalida());
            ps.setObject(4, r.getHoraLlegada());
            ps.setInt(5, r.getID_colectivo().getID_colectivo());
            ps.setDouble(6, r.getPrecio());
            ps.setInt(7, r.getEstado());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();

            if (rs.next()) {
                r.setID_ruta(rs.getInt(1));
                JOptionPane.showMessageDialog(null, "Se guardo la ruta con exito ID:" + r.getID_ruta());
            } else {
                JOptionPane.showMessageDialog(null, "No se puedo obtener el id");
            }
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(PasajeroData.class.getName()).log(Level.SEVERE, "Error al guardar ruta de viaje", ex);
            JOptionPane.showMessageDialog(null, "Error al insertar en la base de datos: " + ex.getMessage());
        }
    }

    public void bajaLogicaRuta(int id) {
        String sql = "UPDATE rutas SET estado=0 WHERE ID_ruta = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            int filas = ps.executeUpdate();
            if (filas > 0) {
                JOptionPane.showMessageDialog(null, "Ruta dada de baja lógicamente.");
            } else {
                JOptionPane.showMessageDialog(null, "No se encontro la ruta.");
            }
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al dar de baja a la ruta: " + ex.getMessage());
        }
    }

    public RutaViaje buscarRuta(int id) {
        String sql = "SELECT * FROM rutas WHERE ID_ruta = ?";
        RutaViaje ruta = null;

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ruta = new RutaViaje();
                ruta.setID_ruta(id);
                ruta.setOrigen(rs.getString("origen"));
                ruta.setDestino(rs.getString("destino"));
                ruta.setHoraSalida(rs.getObject("horaSalida", LocalTime.class));
                ruta.setHoraLlegada(rs.getObject("horaLlegada", LocalTime.class));
                int idColect = rs.getInt("ID_colectivo");
                Colectivo col = new Colectivo();
                col.setID_colectivo(idColect);
                ruta.setID_colectivo(col);
                ruta.setPrecio(rs.getDouble("precio"));
                ruta.setEstado(rs.getInt("estado"));
            } else {
                JOptionPane.showMessageDialog(null, "No existe ninguna ruta con ese ID.");
            }
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al buscar ruta: " + ex.getMessage());
        }
        return ruta;
    }

    public List<RutaViaje> listarRutas() {
        String sql = "SELECT * FROM rutas WHERE estado = 1";
        List<RutaViaje> rutas = new ArrayList<>();

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                RutaViaje r = new RutaViaje();
                r.setID_ruta(rs.getInt("ID_ruta"));
                r.setOrigen(rs.getString("origen"));
                r.setDestino(rs.getString("destino"));
                r.setHoraSalida(rs.getObject("horaSalida", LocalTime.class));
                r.setHoraLlegada(rs.getObject("horaLlegada", LocalTime.class));
                int idColect = rs.getInt("ID_colectivo");
                Colectivo col = new Colectivo();
                col.setID_colectivo(idColect);
                r.setID_colectivo(col);
                r.setPrecio(rs.getDouble("precio"));
                r.setEstado(rs.getInt("estado"));
                rutas.add(r);
            }
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al listar las rutas: " + ex.getMessage());
        }
        return rutas;
    }
}
