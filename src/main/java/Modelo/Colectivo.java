package Modelo;

public class Colectivo {
    private int idColectivo;
    private int matricula;
    private String marca;
    private String modelo;
    private int capacidad;
    private String chofer;
    private int estado;

    public Colectivo() {
    }

    public Colectivo(int idColectivo, int matricula, String marca, String modelo, int capacidad, String chofer, int estado) {
        this.idColectivo = idColectivo;
        this.matricula = matricula;
        this.marca = marca;
        this.modelo = modelo;
        this.capacidad = capacidad;
        this.chofer = chofer;
        this.estado = estado;
    }

    public Colectivo(int matricula, String marca, String modelo, int capacidad, String chofer, int estado) {
        this.matricula = matricula;
        this.marca = marca;
        this.modelo = modelo;
        this.capacidad = capacidad;
        this.chofer = chofer;
        this.estado = estado;
    }

    public int getIdColectivo() {
        return idColectivo;
    }

    public void setIdColectivo(int idColectivo) {
        this.idColectivo = idColectivo;
    }

    public int getMatricula() {
        return matricula;
    }

    public void setMatricula(int matricula) {
        this.matricula = matricula;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public String getChofer() {
        return chofer;
    }

    public void setChofer(String chofer) {
        this.chofer = chofer;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }
    
    @Override
    public String toString() {
        return "Colectivo: " + 
                "\n idColectivo: " + idColectivo 
                + "\n matricula: " + matricula 
                + "\n modelo: " + modelo 
                + "\n marca: " + marca 
                + "\n capacidad: " + capacidad 
                + "\n chofer: " + chofer 
                + "\n estado: " + estado;
    }
}
