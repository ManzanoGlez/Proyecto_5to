package Modelo;

import Conexion.ConexionSQLServer;
import Controladores.LoginController;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
 * @author jorge
 */
public class Empleado extends Usuario {

    private String NSS, Nombres, Apellidos, Direccion,
            Telefono, RFC, Fecha_Nacimiento,
            CURP, Correo, Fecha_Ingreso,
            Puesto, Area;
    private double Sueldo;

    public int ID_Empleado, Usuario_Login;

    public static int ID_Modificar;

    public static int ID_Usuario_Login;
    public static boolean Perfil = false; //true => administrador false => normal

    ConexionSQLServer SQL = new ConexionSQLServer();
    //Querys
    public static String Query_Tabla = "SELECT u.ID_Usuario,e.Nombres,e.Apellidos,e.Correo,e.telefono,e.Puesto,e.Area FROM empleado e JOIN usuario u ON e.ID_Empleado = u.ID_Empleado WHERE u.Estatus = 'Activo' and e.Estatus = 'Activo';";

    public static String Query_Delete = "exec [dbo].[sp_Baja_Empleado] ?";

    public static String Query_Insert = "exec [dbo].[sp_Alta_Empleado] ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?;";

    public static String Query_Update = "exec [dbo].[sp_Modificar_Empleado] ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?;";

    public static String Query_Select_Where = "SELECT e.NSS,e.Nombres,e.Apellidos,e.Direccion,e.Telefono,e.RFC,convert(varchar, e.Fecha_Nacimiento, 101) as Fecha_Nacimiento,e.CURP,e.Correo,convert(varchar, e.Fecha_Ingreso, 101),e.Puesto,e.Area,e.Sueldo,CONVERT(VARCHAR(50),DECRYPTBYPASSPHRASE('Secreto',u.Contraseña)),u.Tipo FROM empleado e JOIN usuario u ON e.ID_Empleado = u.ID_Empleado WHERE e.ID_Empleado = ?";

    public static String Query_Combo_Vendedores = "SELECT CONCAT(Nombres, ' ', Apellidos) As Empleado FROM Empleado WHERE Estatus != 'Eliminado';";

    public Empleado() {

        this.setNombres(null);
        this.setApellidos(null);
        this.setNSS(null);
        this.setDireccion(null);
        this.setTelefono(null);
        this.setRFC(null);
        this.setFecha_Ingreso(null);
        this.setCURP(null);
        this.setCorreo(null);
        this.setFecha_Ingreso(null);
        this.setPuesto(null);
        this.setArea(null);
        this.setSueldo(0f);
    }

// Completo
    public Empleado(String NSS, String Nombres, String Apellidos, String Direccion, String Telefono, String RFC, String Fecha_Nacimiento, String CURP, String Correo, String Fecha_Ingreso, String Puesto, String Area, double Sueldo) {

        this.NSS = NSS;
        this.Nombres = Nombres;
        this.Apellidos = Apellidos;
        this.Direccion = Direccion;
        this.Telefono = Telefono;
        this.RFC = RFC;
        this.Fecha_Nacimiento = Fecha_Nacimiento;
        this.CURP = CURP;
        this.Correo = Correo;
        this.Fecha_Ingreso = Fecha_Ingreso;
        this.Puesto = Puesto;
        this.Area = Area;
        this.Sueldo = Sueldo;
    }

    //Tabla CRUD
    public Empleado(int ID_Empleado, String Nombre, String Correo, String Telefono, String Puesto, String Area) {
        this.ID_Empleado = ID_Empleado;
        this.Nombres = Nombre;
        this.Correo = Correo;
        this.Telefono = Telefono;
        this.Puesto = Puesto;
        this.Area = Area;
    }

    public boolean Inicio_Sesion(String user, String Password) {

        //bandera
        boolean Existe = false;

        Limpiar_Usuario();// Limpeamos registro de otro login

        try {
            PreparedStatement ps;
            ResultSet rs;
            String Query = "exec [dbo].[sp_Login_Usuarios] ?,?;";

            ps = SQL.Conectar().prepareStatement(Query);

            ps.setString(1, user);
            ps.setString(2, Password);

            rs = ps.executeQuery();
            while (rs.next()) {

                if ("FALSE".equals(rs.getObject(1))) {
                    Existe = false;
                } else {

                    this.setID_Usuario(rs.getInt(1));
                    ID_Usuario_Login = rs.getInt(1);
                    this.setNombres(rs.getString(2));
                    this.setApellidos(rs.getString(3));
                    this.setNSS(rs.getString(4));
                    this.setTelefono(rs.getString(5));
                    this.setRFC(rs.getString(6));
                    this.setFecha_Nacimiento(rs.getString(7));
                    this.setCURP(rs.getString(8));
                    this.setDireccion(rs.getString(9));
                    this.setPuesto(rs.getString(10));
                    this.setSueldo(rs.getDouble(11));
                    this.setFecha_Ingreso(rs.getString(12));
                    this.setArea(rs.getString(13));
                    this.setCorreo(rs.getString(14));
                    this.setUsuario(rs.getString(15));
                    this.setContraseña(rs.getString(16));
                    this.setTipo(rs.getString(17));
                    this.setStatus(rs.getString(18));

             Perfil = "Administrador".equals(rs.getString(17));

                    Existe = true;
                }

            }

        } catch (SQLException e) {
            System.out.println("Error en login bueno :" + e.getMessage());
        }

        LoginController.ID_Usuario = this.getID_Usuario();

        return Existe;
    }

    public void Enviar_Info_Usuario_Loggeado(int ID_Usuario) {

        // Limpeamos registro de otro login
        try {

            PreparedStatement ps;
            ResultSet rs;

            String Query = "exec [dbo].[sp_Info_Usuario] ?;";
            ps = SQL.Conectar().prepareStatement(Query);
            ps.setInt(1, ID_Usuario);
            rs = ps.executeQuery();

            while (rs.next()) {

                this.setID_Usuario(rs.getInt(1));
                this.setNombres(rs.getString(2));
                this.setApellidos(rs.getString(3));
                this.setNSS(rs.getString(4));
                this.setTelefono(rs.getString(5));
                this.setRFC(rs.getString(6));
                this.setFecha_Nacimiento(rs.getString(7));
                this.setCURP(rs.getString(8));
                this.setDireccion(rs.getString(9));
                this.setPuesto(rs.getString(10));
                this.setSueldo(rs.getDouble(11));
                this.setFecha_Ingreso(rs.getString(12));
                this.setArea(rs.getString(13));
                this.setCorreo(rs.getString(14));
                this.setUsuario(rs.getString(13));
                this.setContraseña(rs.getString(15));
                this.setTipo(rs.getString(15));
                this.setStatus(rs.getString(16));

            }

        } catch (SQLException e) {
            System.out.println("Error en login bueno :" + e.getMessage());
        }
    }

    private void Limpiar_Usuario() {

        this.setID_Usuario(0);
        this.setUsuario(null);
        this.setContraseña(null);
        this.setTipo(null);
        this.setStatus(null);
        this.setNombres(null);
        this.setApellidos(null);
        this.setDireccion(null);
        this.setTelefono(null);
        this.setRFC(null);
        this.setFecha_Ingreso(null);
        this.setCURP(null);
        this.setCorreo(null);
        this.setFecha_Ingreso(null);
        this.setPuesto(null);
        this.setArea(null);
        this.setSueldo(0f);

    }

    @Override
    public int getID_Empleado() {
        return ID_Empleado;
    }

    @Override
    public void setID_Empleado(int ID_Empleado) {
        this.ID_Empleado = ID_Empleado;
    }

    public String getNSS() {
        return NSS;
    }

    public void setNSS(String NSS) {
        this.NSS = NSS;
    }

    public String getNombres() {
        return Nombres;
    }

    public void setNombres(String Nombres) {
        this.Nombres = Nombres;
    }

    public String getApellidos() {
        return Apellidos;
    }

    public void setApellidos(String Apellidos) {
        this.Apellidos = Apellidos;
    }

    public String getDireccion() {
        return Direccion;
    }

    public void setDireccion(String Direccion) {
        this.Direccion = Direccion;
    }

    public String getTelefono() {
        return Telefono;
    }

    public void setTelefono(String Telefono) {
        this.Telefono = Telefono;
    }

    public String getRFC() {
        return RFC;
    }

    public void setRFC(String RFC) {
        this.RFC = RFC;
    }

    public String getFecha_Nacimiento() {
        return Fecha_Nacimiento;
    }

    public void setFecha_Nacimiento(String Fecha_Nacimiento) {
        this.Fecha_Nacimiento = Fecha_Nacimiento;
    }

    public String getCURP() {
        return CURP;
    }

    public void setCURP(String CURP) {
        this.CURP = CURP;
    }

    public String getCorreo() {
        return Correo;
    }

    public void setCorreo(String Correo) {
        this.Correo = Correo;
    }

    public String getFecha_Ingreso() {
        return Fecha_Ingreso;
    }

    public void setFecha_Ingreso(String Fecha_Ingreso) {
        this.Fecha_Ingreso = Fecha_Ingreso;
    }

    public String getPuesto() {
        return Puesto;
    }

    public void setPuesto(String Puesto) {
        this.Puesto = Puesto;
    }

    public String getArea() {
        return Area;
    }

    public void setArea(String Area) {
        this.Area = Area;
    }

    public double getSueldo() {
        return Sueldo;
    }

    public void setSueldo(double Sueldo) {
        this.Sueldo = Sueldo;
    }

}
