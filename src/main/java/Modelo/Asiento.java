package Modelo;

public class Asiento {
    private int idLugar;
    private Colectivo colectivo;
    private int numeroAsiento;
    private int estado;

    public Asiento() {
    }

    public Asiento(int idLugar, Colectivo colectivo, int numeroAsiento, int estado) {
        this.idLugar = idLugar;
        this.colectivo = colectivo;
        this.numeroAsiento = numeroAsiento;
        this.estado = estado;
    }

    public Asiento(Colectivo colectivo, int numeroAsiento, int estado) {
        this.colectivo = colectivo;
        this.numeroAsiento = numeroAsiento;
        this.estado = estado;
    }

    public int getIdLugar() {
        return idLugar;
    }

    public void setIdLugar(int idLugar) {
        this.idLugar = idLugar;
    }

    public Colectivo getColectivo() {
        return colectivo;
    }

    public void setColectivo(Colectivo colectivo) {
        this.colectivo = colectivo;
    }

    public int getNumeroAsiento() {
        return numeroAsiento;
    }

    public void setNumeroAsiento(int numeroAsiento) {
        this.numeroAsiento = numeroAsiento;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }
    
    @Override
    public String toString() {
        return "Asiento: " + 
                "\n idPasajero: " + idLugar
                + "\n Colectivo: " + colectivo 
                + "\n numeroAsiento: " + numeroAsiento 
                + "\n estado: " + estado;
    }
}
