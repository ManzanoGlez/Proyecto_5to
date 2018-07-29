package Modelo;

/*
 * @author jorge
 */
public class Usuario {

    private int ID_Usuario, ID_Empleado;
    private String Usuario, Contraseña, Tipo, Status;

    public Usuario() {

        this.setID_Usuario(0);
        this.setUsuario(null);
        this.setContraseña(null);
        this.setTipo(null);
        this.setStatus(null);
    }

    public Usuario(int ID_Usuario, int ID_Empleado, String Usuario, String Contraseña, String Tipo, String Status) {
        this.ID_Usuario = ID_Usuario;
        this.ID_Empleado = ID_Empleado;
        this.Usuario = Usuario;
        this.Contraseña = Contraseña;
        this.Tipo = Tipo;
        this.Status = Status;
    }  
    
    public int getID_Usuario() {
        return ID_Usuario;
    }

    public void setID_Usuario(int ID_Usuario) {
        this.ID_Usuario = ID_Usuario;
    }

    public int getID_Empleado() {
        return ID_Empleado;
    }

    public void setID_Empleado(int ID_Empleado) {
        this.ID_Empleado = ID_Empleado;
    }

    public String getUsuario() {
        return Usuario;
    }

    public void setUsuario(String Usuario) {
        this.Usuario = Usuario;
    }

    public String getContraseña() {
        return Contraseña;
    }

    public void setContraseña(String Contraseña) {
        this.Contraseña = Contraseña;
    }

    public String getTipo() {
        return Tipo;
    }

    public void setTipo(String Tipo) {
        this.Tipo = Tipo;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

}
