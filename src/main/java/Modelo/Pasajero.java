package Modelo;

public class Pasajero {
    private int ID_pasajero;
    private String dni;
    private String nombre;
    private String apellido;
    private String correo;
    private String telefono;
    private int estado;

    public Pasajero(){}

    public Pasajero(int idPasajero, String dni, String nombre, String apellido, String correo, String telefono, int estado) {
        this.ID_pasajero = idPasajero;
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.telefono = telefono;
        this.estado = estado;
    }
    
    public Pasajero(String dni, String nombre, String apellido, String correo, String telefono, int estado) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.telefono = telefono;
        this.estado = estado;
    }
    
    public int getID_pasajero() {
        return ID_pasajero;
    }

    public void setID_pasajero(int ID_pasajero) {
        this.ID_pasajero = ID_pasajero;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
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

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
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
                "\n idPasajero: " + ID_pasajero 
                + "\n dni: " + dni 
                + "\n nombre: " + nombre 
                + "\n apellido: " + apellido 
                + "\n correo: " + correo 
                + "\n telefono: " + telefono 
                + "\n estado: " + estado;
    }
}
