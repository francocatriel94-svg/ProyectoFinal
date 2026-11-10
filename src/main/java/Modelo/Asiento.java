package Modelo;

public class Asiento {
    private int ID_lugar;
    private int ID_ruta;
    private int numeroAsiento;
    private int estado;

    public Asiento() {
    }

    public Asiento(int idLugar, RutaViaje rutaviaje, int numeroAsiento, int estado) {
        this.ID_lugar = idLugar;
        this.ID_ruta = rutaviaje.getID_ruta();
        this.numeroAsiento = numeroAsiento;
        this.estado = estado;
    }

    public Asiento(RutaViaje rutaviaje, int numeroAsiento, int estado) {
        this.ID_ruta = rutaviaje.getID_ruta();
        this.numeroAsiento = numeroAsiento;
        this.estado = estado;
    }

    public int getID_lugar() {
        return ID_lugar;
    }

    public void setID_lugar(int ID_lugar) {
        this.ID_lugar = ID_lugar;
    }

    public int getID_ruta() {
        return ID_ruta;
    }

    public void setID_ruta(int ID_ruta) {
        this.ID_ruta = ID_ruta;
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
                + "\n idRuta: " + ID_ruta 
                + "\n numeroAsiento: " + numeroAsiento 
                + "\n estado: " + estado;
    }
}
