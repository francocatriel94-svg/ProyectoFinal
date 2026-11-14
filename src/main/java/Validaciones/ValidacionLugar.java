package Validaciones;

import Modelo.Lugar;
import Modelo.RutaViaje;
import Persistencia.LugarData;
import Persistencia.RutaViajeData;

public class ValidacionLugar {

    public static String validarLugar(Lugar lugar, int idLugarActual, RutaViajeData rutaData, LugarData lugarData) {
        if (lugar == null) {
            return "No se recibieron los datos del asiento.";
        }

        if (rutaData == null || lugarData == null) {
            return "No se pudieron verificar los datos del asiento.";
        }

        // Comprobar ruta.
        RutaViaje ruta = rutaData.buscarRuta(lugar.getID_ruta());

        if (ruta == null) {
            return "La ruta seleccionada no existe.";
        }

        if (ruta.getEstado() != 1) {
            return "La ruta seleccionada está dada de baja.";
        }

        // Comprobar número de asiento.
        if (lugar.getNumeroAsiento() <= 0) {
            return "El número de asiento debe ser mayor que cero.";
        }

        // Comprobar estado.
        if (lugar.getEstado() != LugarData.LIBRE
                && lugar.getEstado() != LugarData.OCUPADO) {
            return "El estado del asiento no es válido.";
        }

        // Comprobar que no exista otro asiento con el mismo número.
        Lugar existente = lugarData.buscarLugarPorNumero(
                lugar.getID_ruta(),
                lugar.getNumeroAsiento());

        if (existente != null && existente.getID_lugar() != idLugarActual) {
            return "Ya existe un asiento con ese número en la ruta seleccionada.";
        }
        return null;
    }
}
