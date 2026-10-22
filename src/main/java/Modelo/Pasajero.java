package Modelo;

public class Pasajero {
    private int idPasajero;
    private int dni;
    private String nombre;
    private String apellido;
    private String correo;
    private int telefono;
    private int estado;

    public Pasajero(){}

    public Pasajero(int idPasajero, int dni, String nombre, String apellido, String correo, int telefono, int estado) {
        this.idPasajero = idPasajero;
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.telefono = telefono;
        this.estado = estado;
    }
    
    public Pasajero(int dni, String nombre, String apellido, String correo, int telefono, int estado) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.telefono = telefono;
        this.estado = estado;
    }
    
    public int getIdPasajero() {
        return idPasajero;
    }

    public void setIdPasajero(int idPasajero) {
        this.idPasajero = idPasajero;
    }

    public int getDni() {
        return dni;
    }

    public void setDni(int dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
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
                "\n idPasajero: " + idPasajero 
                + "\n dni: " + dni 
                + "\n nombre: " + nombre 
                + "\n apellido: " + apellido 
                + "\n correo: " + correo 
                + "\n telefono: " + telefono 
                + "\n estado: " + estado;
    }
}
