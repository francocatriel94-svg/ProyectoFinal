/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Persistencia;

import java.sql.Connection;

/**
 *
 * @author NOELIA
 */
public class PasajeroData {
    private Connection con = null;

    public PasajeroData() {
        con = Conexion.getConexion();
    }
    
    
}
