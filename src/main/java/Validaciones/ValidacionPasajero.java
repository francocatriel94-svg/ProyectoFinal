package Validaciones;

import Modelo.Pasajero;
import Persistencia.PasajeroData;

public class ValidacionPasajero {

    // Valida todos los datos de un pasajero.
    // idActual = 0 cuando se registra uno nuevo.
    // Si se modifica, recibe el ID del pasajero actual.
    public static String validarPasajero(
            Pasajero p, PasajeroData data, int idActual) {

        if (p == null) {
            return "No se recibieron los datos del pasajero.";
        }

        // Comprobar campos obligatorios.
        if (p.getDni() == null || p.getDni().trim().isEmpty()
                || p.getNombre() == null || p.getNombre().trim().isEmpty()
                || p.getApellido() == null || p.getApellido().trim().isEmpty()
                || p.getCorreo() == null || p.getCorreo().trim().isEmpty()
                || p.getTelefono() == null || p.getTelefono().trim().isEmpty()) {

            return "Todos los campos son obligatorios.";
        }

        String dni = p.getDni().trim();

        // DNI: entre 7 y 10 números.
        // Se permiten puntos como separadores.
        if (!dni.matches("[0-9]{7,10}|[0-9]{1,3}(\\.[0-9]{3}){2,3}")) {
            return "El DNI debe tener entre 7 y 10 dígitos.";
        }

        // Teléfono: entre 8 y 15 dígitos.
        if (!p.getTelefono().trim().matches("[0-9]{8,15}")) {
            return "El teléfono debe contener entre 8 y 15 números.";
        }

        // Nombre y apellido: letras, espacios y guiones.
        String patronNombre = "[\\p{L}]+(?:[ -][\\p{L}]+)*";

        if (!p.getNombre().trim().matches(patronNombre)) {
            return "El nombre contiene caracteres no permitidos.";
        }

        if (!p.getApellido().trim().matches(patronNombre)) {
            return "El apellido contiene caracteres no permitidos.";
        }

        // Correo electrónico.
        String patronCorreo = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

        if (!p.getCorreo().trim().matches(patronCorreo)) {
            return "El correo electrónico no tiene un formato válido.";
        }

        // Comprobar DNI duplicado.
        Pasajero existente = data.buscarPasajeroPorDni(dni);

        if (existente != null && existente.getID_pasajero() != idActual) {
            return "Ya existe un pasajero registrado con ese DNI.";
        }

        return null;
    }
}
