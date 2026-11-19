package Validaciones;

import Modelo.Pasaje;
import Modelo.Pasajero;
import Modelo.RutaViaje;
import Modelo.Lugar;
import Persistencia.PasajeroData;
import Persistencia.RutaViajeData;
import Persistencia.LugarData;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ValidacionPasaje {

    public static String validarPasaje(Pasaje pasaje, int idPasajeActual, PasajeroData pasajeroData, RutaViajeData rutaData, LugarData lugarData) {
        if (pasaje == null) {
            return "No se recibieron los datos del pasaje.";
        }

        // Comprobar estado.
        if (pasaje.getEstado() != 0 && pasaje.getEstado() != 1) {
            return "El estado del pasaje no es válido.";
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

        // Comprobar que el viaje todavía no haya salido.
        if (pasaje.getEstado() == 1) {
            if (ruta.getFechaSalida() == null || ruta.getHoraSalida() == null) {
                return "La ruta no tiene una fecha y hora de salida válida.";
            }

            LocalDateTime salida = LocalDateTime.of(ruta.getFechaSalida(), ruta.getHoraSalida());

            if (!salida.isAfter(LocalDateTime.now())) {
                return "No se pueden emitir pasajes para un viaje que ya salió.";
            }
        }

        // Comprobar asiento.
        Lugar lugar = lugarData.buscarLugar(pasaje.getID_lugar());

        if (lugar == null) {
            return "El asiento seleccionado no existe.";
        }

        if (lugar.getID_ruta() != pasaje.getID_ruta()) {
            return "El asiento no pertenece a la ruta seleccionada.";
        }

        if (pasaje.getEstado() == 1) {
            if (!lugarData.estaDisponibleParaPasaje(
                    pasaje.getID_lugar(),
                    pasaje.getID_ruta(),
                    idPasajeActual)) {

                return "El asiento seleccionado ya está ocupado.";
            }
        }

        // Comprobar fecha y hora de venta.
        if (pasaje.getFechaVenta() == null) {
            return "La fecha de venta es obligatoria.";
        }

        if (pasaje.getHoraVenta() == null) {
            return "La hora de venta es obligatoria.";
        }

        // Comprobar precio.
        if (pasaje.getPrecio() == null
                || pasaje.getPrecio().compareTo(BigDecimal.ZERO) <= 0) {
            return "El precio del pasaje debe ser mayor que cero.";
        }

        return null;
    }
}
