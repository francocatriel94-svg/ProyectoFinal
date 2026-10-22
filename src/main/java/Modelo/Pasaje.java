package Modelo;

import java.time.LocalDate;
import java.time.LocalTime;

public class Pasaje {
    private int idPasaje;
    private Pasajero pasajero;
    private Asiento lugar;
    private RutaViaje rutaViaje;
    private LocalDate fechaVenta;
    private LocalTime horaVenta;
    private int precio;
    private int estado;

    public Pasaje() {
    }

    public Pasaje(int idPasaje, Pasajero pasajero, Asiento lugar, RutaViaje rutaViaje, LocalDate fechaVenta, LocalTime horaVenta, int precio, int estado) {
        this.idPasaje = idPasaje;
        this.pasajero = pasajero;
        this.lugar = lugar;
        this.rutaViaje = rutaViaje;
        this.fechaVenta = fechaVenta;
        this.horaVenta = horaVenta;
        this.precio = precio;
        this.estado = estado;
    }

    public Pasaje(Pasajero pasajero, Asiento lugar, RutaViaje rutaViaje, LocalDate fechaVenta, LocalTime horaVenta, int precio, int estado) {
        this.pasajero = pasajero;
        this.lugar = lugar;
        this.rutaViaje = rutaViaje;
        this.fechaVenta = fechaVenta;
        this.horaVenta = horaVenta;
        this.precio = precio;
        this.estado = estado;
    }

    public int getIdPasaje() {
        return idPasaje;
    }

    public void setIdPasaje(int idPasaje) {
        this.idPasaje = idPasaje;
    }

    public Pasajero getPasajero() {
        return pasajero;
    }

    public void setPasajero(Pasajero pasajero) {
        this.pasajero = pasajero;
    }

    public Asiento getLugar() {
        return lugar;
    }

    public void setLugar(Asiento lugar) {
        this.lugar = lugar;
    }

    public RutaViaje getRutaViaje() {
        return rutaViaje;
    }

    public void setRutaViaje(RutaViaje rutaViaje) {
        this.rutaViaje = rutaViaje;
    }

    public LocalDate getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(LocalDate fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public LocalTime getHoraVenta() {
        return horaVenta;
    }

    public void setHoraVenta(LocalTime horaVenta) {
        this.horaVenta = horaVenta;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }
    
    @Override
    public String toString() {
        return "Pasaje: " + 
                "\n idPasaje: " + idPasaje 
                + "\n pasajero: " + pasajero 
                + "\n lugar: " + lugar 
                + "\n rutaViaje: " + rutaViaje 
                + "\n fechaVenta: " + fechaVenta 
                + "\n precio: " + precio 
                + "\n estado: " + estado;
    }
}
