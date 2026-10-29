/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Persistencia;
public class ControladorDatos {
    public static boolean ValidarDNINumeros(String DNI)
    {           
        int dni;
        try{
            dni=Integer.parseInt(DNI);            
        }catch(NumberFormatException e)
        {
           return false; 
        }
        return true;
    }
    public static boolean ValidarDNILongitud(String DNI)
    {
        if(DNI.length()!=8)
        {
           return false; 
        }
        else{
            return true;
        }
    }
    public static boolean ValidarTelefonoLongitud(String telefono)
    {
      if(telefono.length()!=11)
      {
          return false;
      }
      return true;
    }
    public static boolean ValidarTelefonoNumeros(String telefono)
    {   
        
        int telefon;
        try
        {
          telefon = Integer.parseInt(telefono);   
        }catch(NumberFormatException e)
        {
          return false;  
        }
        return false;
    }
}
