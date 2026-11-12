package Validaciones;

import Modelo.Colectivo;
import Modelo.RutaViaje;
import Persistencia.ColectivoData;
import java.time.LocalDate;
import java.time.LocalTime;

public class ValidacionRutaViaje {

    public static String validarRutaViaje(RutaViaje ruta, ColectivoData colectivoData) {
        if (ruta == null) {
            return "No se recibieron los datos del viaje.";
        }

        // Comprobar campos obligatorios.
        if (ruta.getOrigen() == null || ruta.getOrigen().trim().isEmpty()
                || ruta.getDestino() == null || ruta.getDestino().trim().isEmpty()
                || ruta.getFechaViaje() == null
                || ruta.getHoraSalida() == null
                || ruta.getHoraLlegada() == null) {
            return "Todos los campos son obligatorios.";
        }

        // Origen y destino deben ser diferentes.
        if (ruta.getOrigen().trim().equalsIgnoreCase(ruta.getDestino().trim())) {
            return "El origen y el destino no pueden ser iguales.";
        }

        // La fecha no puede ser anterior a hoy.
        LocalDate hoy = LocalDate.now();

        if (ruta.getFechaViaje().isBefore(hoy)) {
            return "La fecha del viaje no puede ser anterior a hoy.";
        }

        // Si el viaje es hoy, la salida debe ser posterior a la hora actual.
        if (ruta.getFechaViaje().isEqual(hoy)
                && !ruta.getHoraSalida().isAfter(LocalTime.now())) {

            return "La hora de salida debe ser posterior a la hora actual.";
        }

        // La llegada debe ser posterior a la salida.
        if (!ruta.getHoraLlegada().isAfter(ruta.getHoraSalida())) {
            return "La hora de llegada debe ser posterior a la hora de salida.";
        }

        // El precio debe ser mayor que cero.
        if (!Double.isFinite(ruta.getPrecio()) || ruta.getPrecio() <= 0) {
            return "El precio debe ser mayor que cero.";
        }

        // Comprobar que el colectivo exista y esté activo.
        Colectivo colectivo = colectivoData.buscarColectivo(ruta.getID_colectivo());

        if (colectivo == null) {
            return "El colectivo seleccionado no existe.";
        }

        if (colectivo.getEstado() != 1) {
            return "El colectivo seleccionado está dado de baja.";
        }
        
        return null;
    }
}
