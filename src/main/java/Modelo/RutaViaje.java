package Modelo;

import java.time.LocalTime;

public class RutaViaje {
    private int idRuta;
    private String origen;
    private String destino;
    private LocalTime horaSalida;
    private LocalTime horaLlegada;
    private Colectivo colectivo;
    private int precio;
    private int estado;

    public RutaViaje() {
    }

    public RutaViaje(int idRuta, String origen, String destino, LocalTime horaSalida, LocalTime horaLlegada, Colectivo colectivo, int precio, int estado) {
        this.idRuta = idRuta;
        this.origen = origen;
        this.destino = destino;
        this.horaSalida = horaSalida;
        this.horaLlegada = horaLlegada;
        this.colectivo = colectivo;
        this.precio = precio;
        this.estado = estado;
    }

    public RutaViaje(String origen, String destino, LocalTime horaSalida, LocalTime horaLlegada, Colectivo colectivo, int precio, int estado) {
        this.origen = origen;
        this.destino = destino;
        this.horaSalida = horaSalida;
        this.horaLlegada = horaLlegada;
        this.colectivo = colectivo;
        this.precio = precio;
        this.estado = estado;
    }

    public int getIdRuta() {
        return idRuta;
    }

    public void setIdRuta(int idRuta) {
        this.idRuta = idRuta;
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

    public Colectivo getColectivo() {
        return colectivo;
    }

    public void setColectivo(Colectivo colectivo) {
        this.colectivo = colectivo;
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
        return "Pasajero: " + 
                "\n idRuta: " + idRuta 
                + "\n origen: " + origen 
                + "\n destino: " + destino 
                + "\n horaSalida: " + horaSalida 
                + "\n horaLlegada: " + horaLlegada 
                + "\n colectivo: " + colectivo 
                + "\n precio: " + precio
                + "\n estado: " + estado;
    }
}
