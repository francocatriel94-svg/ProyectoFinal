package Modelo;

import java.time.LocalTime;

public class RutaViaje {
    private int ID_ruta;
    private String origen;
    private String destino;
    private LocalTime horaSalida;
    private LocalTime horaLlegada;
    private Colectivo ID_colectivo;
    private double precio;
    private int estado;

    public RutaViaje() {
    }

    public RutaViaje(int idRuta, String origen, String destino, LocalTime horaSalida, LocalTime horaLlegada, Colectivo colectivo, double precio, int estado) {
        this.ID_ruta = idRuta;
        this.origen = origen;
        this.destino = destino;
        this.horaSalida = horaSalida;
        this.horaLlegada = horaLlegada;
        this.ID_colectivo = colectivo;
        this.precio = precio;
        this.estado = estado;
    }

    public RutaViaje(String origen, String destino, LocalTime horaSalida, LocalTime horaLlegada, Colectivo colectivo, double precio, int estado) {
        this.origen = origen;
        this.destino = destino;
        this.horaSalida = horaSalida;
        this.horaLlegada = horaLlegada;
        this.ID_colectivo = colectivo;
        this.precio = precio;
        this.estado = estado;
    }

    public int getID_ruta() {
        return ID_ruta;
    }

    public void setID_ruta(int ID_ruta) {
        this.ID_ruta = ID_ruta;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public LocalTime getHoraSalida() {
        return horaSalida;
    }

    public void setHoraSalida(LocalTime horaSalida) {
        this.horaSalida = horaSalida;
    }

    public LocalTime getHoraLlegada() {
        return horaLlegada;
    }

    public void setHoraLlegada(LocalTime horaLlegada) {
        this.horaLlegada = horaLlegada;
    }

    public Colectivo getID_colectivo() {
        return ID_colectivo;
    }

    public void setID_colectivo(Colectivo ID_colectivo) {
        this.ID_colectivo = ID_colectivo;
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
        return "Pasajero: " + 
                "\n idRuta: " + ID_ruta 
                + "\n origen: " + origen 
                + "\n destino: " + destino 
                + "\n horaSalida: " + horaSalida 
                + "\n horaLlegada: " + horaLlegada 
                + "\n colectivo: " + ID_colectivo 
                + "\n precio: " + precio
                + "\n estado: " + estado;
    }
}
