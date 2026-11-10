package Persistencia;

import Modelo.Asiento;
import Modelo.Colectivo;
import Modelo.RutaViaje;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
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
        String sql = "INSERT INTO rutas (origen, destino, horaSalida, horaLlegada, ID_colectivo, precio, estado) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, r.getOrigen());
            ps.setString(2, r.getDestino());
            ps.setTime(3, Time.valueOf(r.getHoraSalida()));
            ps.setTime(4, Time.valueOf(r.getHoraLlegada()));
            ps.setInt(5, r.getID_colectivo());
            ps.setDouble(6, r.getPrecio());
            ps.setInt(7, r.getEstado());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int idGenerado = rs.getInt(1);
                    r.setID_ruta(idGenerado);
                      ColectivoData colData = new ColectivoData();
                    Colectivo colectivo = colData.buscarColectivo(r.getID_colectivo());

                    if (colectivo != null) {
                        AsientoData asientoData = new AsientoData();
                        for (int i = 1; i <= colectivo.getCapacidad(); i++) {
                           
                            Asiento asiento = new Asiento();
                            asiento.setID_ruta(idGenerado); 
                            asiento.setNumeroAsiento(i);
                            asiento.setEstado(1); // 1 = Libre
                            
                            asientoData.guardarAsiento(asiento);
                        }
                    }

                    JOptionPane.showMessageDialog(null, "Se guardo la ruta con exito y se generaron sus asientos ID:" + idGenerado);
                }
                ps.close();

            } catch (SQLException ex) {
                  Logger.getLogger(RutaViajeData.class.getName()).log(Level.SEVERE, "Error al guardar ruta de viaje", ex);
            JOptionPane.showMessageDialog(null, "Error al insertar en la base de datos: " + ex.getMessage());

            }
        } catch (SQLException ex) {
            System.getLogger(RutaViajeData.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
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
            }
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al dar de baja a la ruta: " + ex.getMessage());
        }
    }
    
    public void altaLogicaRuta(int id) {
        String sql = "UPDATE rutas SET estado=1 WHERE ID_ruta = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            int filas = ps.executeUpdate();
            if (filas > 0) {
                JOptionPane.showMessageDialog(null, "Ruta activada con éxito.");
            }
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al activar la ruta: " + ex.getMessage());
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
                ruta.setID_colectivo(col.getID_colectivo());
                ruta.setPrecio(rs.getDouble("precio"));
                ruta.setEstado(rs.getInt("estado"));
            }
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al buscar ruta: " + ex.getMessage());
        }
        return ruta;
    }

    public List<RutaViaje> listarRutas() {
        String sql = "SELECT * FROM rutas ";
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
                r.setID_colectivo(col.getID_colectivo());
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
    
    public void modificarRutaViaje(RutaViaje ruta) {
        String sql = "UPDATE rutas SET origen = ?, destino = ?, horaSalida = ?, horaLlegada = ?, ID_colectivo = ?, precio = ?, estado = ? WHERE ID_ruta = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, ruta.getOrigen());
            ps.setString(2, ruta.getDestino());
            ps.setTime(3, Time.valueOf(ruta.getHoraSalida()));
            ps.setTime(4, Time.valueOf(ruta.getHoraLlegada()));
            ps.setInt(5, ruta.getID_colectivo());
            ps.setDouble(6, ruta.getPrecio());
            ps.setInt(7, ruta.getEstado());
            ps.setInt(8, ruta.getID_ruta());

            if (ps.executeUpdate() == 1) {
                JOptionPane.showMessageDialog(null, "Ruta de viaje modificada con éxito.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al modificar la ruta de viaje: " + ex.getMessage());
        }
    }
    
    public List<RutaViaje> buscarRutasPorDestino(String destinoBuscado) {
        List<RutaViaje> rutas = new ArrayList<>();
        String sql = "SELECT * FROM rutas WHERE destino LIKE ?"; 

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, "%" + destinoBuscado + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                RutaViaje r = new RutaViaje();
                r.setID_ruta(rs.getInt("ID_ruta")); 
                r.setOrigen(rs.getString("origen"));
                r.setDestino(rs.getString("destino"));
                r.setHoraSalida(rs.getTime("horaSalida").toLocalTime());
                r.setHoraLlegada(rs.getTime("horaLlegada").toLocalTime());
                r.setID_colectivo(rs.getInt("ID_colectivo"));
                r.setPrecio(rs.getDouble("precio"));
                r.setEstado(rs.getInt("estado"));

                rutas.add(r);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al buscar rutas por destino: " + ex.getMessage());
        }
        return rutas;
    }
}
