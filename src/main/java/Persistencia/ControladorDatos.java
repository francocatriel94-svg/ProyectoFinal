package Persistencia;

public class ControladorDatos {

    public static boolean ValidarDNINumeros(String DNI) {
        int dni;
        try {
            dni = Integer.parseInt(DNI);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public static boolean ValidarDNILongitud(String DNI) {
        return DNI.length() == 8;
    }

    public static boolean ValidarTelefonoLongitud(String telefono) {
        if (telefono.length() != 11) {
            return false;
        }
        return true;
    }

    public static boolean ValidarTelefonoNumeros(String telefono) {   
        int telef;
        try {
            telef = Integer.parseInt(telefono);   
        } catch(NumberFormatException e) {
            return false;  
        }
        return true;
    }
}
