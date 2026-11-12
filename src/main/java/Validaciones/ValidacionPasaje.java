package Validaciones;

import Modelo.Pasaje;
import Modelo.Pasajero;
import Modelo.RutaViaje;
import Modelo.Lugar;
import Persistencia.PasajeroData;
import Persistencia.RutaViajeData;
import Persistencia.LugarData;

public class ValidacionPasaje {

    public static String validarPasaje(Pasaje pasaje,PasajeroData pasajeroData,RutaViajeData rutaData,LugarData lugarData) {
        if (pasaje == null) {
            return "No se recibieron los datos del pasaje.";
        }

        // Comprobar pasajero.
        Pasajero pasajero = pasajeroData.buscarPasajero(
                pasaje.getID_pasajero());

        if (pasajero == null) {
            return "El pasajero seleccionado no existe.";
        }

        if (pasajero.getEstado() != 1) {
            return "El pasajero seleccionado está dado de baja.";
        }

        // Comprobar ruta.
        RutaViaje ruta = rutaData.buscarRuta(
                pasaje.getID_ruta());

        if (ruta == null) {
            return "La ruta seleccionada no existe.";
        }

        if (ruta.getEstado() != 1) {
            return "La ruta seleccionada está dada de baja.";
        }

        // Comprobar asiento.
        Lugar lugar = lugarData.buscarLugar(pasaje.getID_lugar());

        if (lugar == null) {
            return "El asiento seleccionado no existe.";
        }

        if (lugar.getID_ruta() != pasaje.getID_ruta()) {
            return "El asiento no pertenece a la ruta seleccionada.";
        }

        if (lugar.getEstado() != LugarData.LIBRE) {
            return "El asiento seleccionado ya está ocupado.";
        }

        // Comprobar precio.
        if (!Double.isFinite(pasaje.getPrecio())
                || pasaje.getPrecio() <= 0) {
            return "El precio del pasaje debe ser mayor que cero.";
        }

        return null;
    }
}
