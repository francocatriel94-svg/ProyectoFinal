package Modelo;

public class Asiento {
    private int ID_lugar;
    private Colectivo ID_colectivo;
    private int numeroAsiento;
    private int estado;

    public Asiento() {
    }

    public Asiento(int idLugar, Colectivo colectivo, int numeroAsiento, int estado) {
        this.ID_lugar = idLugar;
        this.ID_colectivo = colectivo;
        this.numeroAsiento = numeroAsiento;
        this.estado = estado;
    }

    public Asiento(Colectivo colectivo, int numeroAsiento, int estado) {
        this.ID_colectivo = colectivo;
        this.numeroAsiento = numeroAsiento;
        this.estado = estado;
    }

    public int getID_lugar() {
        return ID_lugar;
    }

    public void setID_lugar(int ID_lugar) {
        this.ID_lugar = ID_lugar;
    }

    public Colectivo getID_colectivo() {
        return ID_colectivo;
    }

    public void setID_colectivo(Colectivo ID_colectivo) {
        this.ID_colectivo = ID_colectivo;
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
                "\n idPasajero: " + ID_lugar
                + "\n Colectivo: " + ID_colectivo 
                + "\n numeroAsiento: " + numeroAsiento 
                + "\n estado: " + estado;
    }
}
