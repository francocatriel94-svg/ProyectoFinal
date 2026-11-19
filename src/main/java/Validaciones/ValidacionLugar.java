package Validaciones;

import Modelo.Lugar;
import Modelo.RutaViaje;
import Persistencia.LugarData;
import Persistencia.RutaViajeData;
import Modelo.Colectivo;
import Persistencia.ColectivoData;

public class ValidacionLugar {

    public static String validarLugar(Lugar lugar, int idLugarActual, RutaViajeData rutaData, LugarData lugarData, ColectivoData colectivoData) {
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

        // Comprobar la capacidad del colectivo asignado a la ruta.
        Colectivo colectivo = colectivoData.buscarColectivo(ruta.getID_colectivo());

        if (colectivo == null) {
            return "No se encontró el colectivo asignado a esta ruta.";
        }

        int capacidad = colectivo.getCapacidad();

        if (lugar.getNumeroAsiento() > capacidad) {
            return "El número de asiento no puede superar la capacidad del colectivo (" + capacidad + ").";
        }

        // Comprobar si estamos agregando un asiento a la ruta.
        boolean nuevoAsientoEnRuta = idLugarActual == 0;

        if (idLugarActual != 0) {
            Lugar asientoActual = lugarData.buscarLugar(idLugarActual);

            if (asientoActual == null) {
                return "No se encontró el asiento que desea modificar.";
            }
            nuevoAsientoEnRuta = asientoActual.getID_ruta() != lugar.getID_ruta();
        }

        // Impedir que la ruta tenga más asientos que la capacidad disponible.
        if (nuevoAsientoEnRuta && lugarData.listarLugaresPorRuta(ruta.getID_ruta()).size() >= capacidad) {
            return "La ruta ya alcanzó la capacidad máxima de " + capacidad + " asientos.";
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
