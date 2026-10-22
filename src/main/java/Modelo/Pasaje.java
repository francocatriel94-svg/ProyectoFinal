package Modelo;

import java.time.LocalDate;
import java.time.LocalTime;

public class Pasaje {
    private int ID_pasaje;
    private Pasajero ID_pasajero;
    private Asiento ID_lugar;
    private RutaViaje ID_ruta;
    private LocalDate fechaVenta;
    private LocalTime horaVenta;
    private double precio;
    private int estado;

    public Pasaje() {
    }

    public Pasaje(int idPasaje, Pasajero pasajero, Asiento lugar, RutaViaje rutaViaje, LocalDate fechaVenta, LocalTime horaVenta, double precio, int estado) {
        this.ID_pasaje = idPasaje;
        this.ID_pasajero = pasajero;
        this.ID_lugar = lugar;
        this.ID_ruta = rutaViaje;
        this.fechaVenta = fechaVenta;
        this.horaVenta = horaVenta;
        this.precio = precio;
        this.estado = estado;
    }

    public Pasaje(Pasajero pasajero, Asiento lugar, RutaViaje rutaViaje, LocalDate fechaVenta, LocalTime horaVenta, double precio, int estado) {
        this.ID_pasajero = pasajero;
        this.ID_lugar = lugar;
        this.ID_ruta = rutaViaje;
        this.fechaVenta = fechaVenta;
        this.horaVenta = horaVenta;
        this.precio = precio;
        this.estado = estado;
    }

    public int getID_pasaje() {
        return ID_pasaje;
    }

    public void setID_pasaje(int ID_pasaje) {
        this.ID_pasaje = ID_pasaje;
    }

    public Pasajero getID_pasajero() {
        return ID_pasajero;
    }

    public void setID_pasajero(Pasajero ID_pasajero) {
        this.ID_pasajero = ID_pasajero;
    }

    public Asiento getID_lugar() {
        return ID_lugar;
    }

    public void setID_lugar(Asiento ID_lugar) {
        this.ID_lugar = ID_lugar;
    }

    public RutaViaje getID_ruta() {
        return ID_ruta;
    }

    public void setID_ruta(RutaViaje ID_ruta) {
        this.ID_ruta = ID_ruta;
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

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
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
                "\n idPasaje: " + ID_pasaje 
                + "\n pasajero: " + ID_pasajero 
                + "\n lugar: " + ID_lugar 
                + "\n rutaViaje: " + ID_ruta 
                + "\n fechaVenta: " + fechaVenta 
                + "\n precio: " + precio 
                + "\n estado: " + estado;
    }
}
