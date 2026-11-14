package Validaciones;

import Modelo.Colectivo;
import Modelo.RutaViaje;
import Persistencia.ColectivoData;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ValidacionRutaViaje {

    public static String validarRutaViaje(RutaViaje ruta, ColectivoData colectivoData) {
        if (ruta == null) {
            return "No se recibieron los datos del viaje.";
        }

        // Comprobar campos obligatorios.
        if (ruta.getOrigen() == null || ruta.getOrigen().trim().isEmpty()
                || ruta.getDestino() == null || ruta.getDestino().trim().isEmpty()
                || ruta.getFechaSalida() == null
                || ruta.getFechaLlegada() == null
                || ruta.getHoraSalida() == null
                || ruta.getHoraLlegada() == null
                || ruta.getPrecio() == null) {
            return "Todos los campos son obligatorios.";
        }

        // Comprobar que exista la capa de persistencia.
        if (colectivoData == null) {
            return "No se pudo verificar el colectivo seleccionado.";
        }

        // Origen y destino deben ser diferentes.
        if (ruta.getOrigen().trim().equalsIgnoreCase(ruta.getDestino().trim())) {
            return "El origen y el destino no pueden ser iguales.";
        }

        // La fecha de salida no puede ser anterior a hoy.
        LocalDate hoy = LocalDate.now();

        if (ruta.getFechaSalida().isBefore(hoy)) {
            return "La fecha de salida no puede ser anterior a hoy.";
        }

        // Combinar fechas y horas para comparar los momentos completos.
        LocalDateTime salida = LocalDateTime.of(ruta.getFechaSalida(),ruta.getHoraSalida());
        LocalDateTime llegada = LocalDateTime.of(ruta.getFechaLlegada(),ruta.getHoraLlegada());

        // La salida debe ser posterior al momento actual.
        if (!salida.isAfter(LocalDateTime.now())) {
            return "La fecha y hora de salida deben ser posteriores al momento actual.";
        }

        // La llegada debe ser posterior a la salida.
        if (!llegada.isAfter(salida)) {
            return "La fecha y hora de llegada deben ser posteriores a la salida.";
        }

        // El precio debe ser mayor que cero.
        if (ruta.getPrecio().compareTo(BigDecimal.ZERO) <= 0) {
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

