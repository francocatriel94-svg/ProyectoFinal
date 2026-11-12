
package Validaciones;

import Modelo.Colectivo;
import Persistencia.ColectivoData;

public class ValidacionColectivo {

    public static String validarColectivo(Colectivo colectivo, ColectivoData data, int idActual) {
        if (colectivo == null) {
            return "No se recibieron los datos del colectivo.";
        }

        // Comprobar campos obligatorios.
        if (colectivo.getMatricula() == null || colectivo.getMatricula().trim().isEmpty()
                || colectivo.getMarca() == null || colectivo.getMarca().trim().isEmpty()
                || colectivo.getModelo() == null || colectivo.getModelo().trim().isEmpty()
                || colectivo.getChofer() == null || colectivo.getChofer().trim().isEmpty()) {
            return "Todos los campos son obligatorios.";
        }

        // Comprobar capacidad.
        if (colectivo.getCapacidad() <= 0) {
            return "La capacidad debe ser un número mayor que cero.";
        }

        // Comprobar matrícula duplicada.
        String matriculaNormalizada = colectivo.getMatricula().replaceAll("\\s+", "").toUpperCase();
        Colectivo existente = data.buscarColectivoPorMatricula(matriculaNormalizada);

        if (existente != null && existente.getID_colectivo() != idActual) {
            return "Ya existe un colectivo registrado con esa matrícula.";
        }
        return null;
    }
}

