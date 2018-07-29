package Modelo;

/*
 * @author jorge
 */
public class Empleado_Proyecto {

    private String Empleado, Proyecto, Fecha_asignacion, Descripcion, Estatus;

    public static String Query_Tabla = "SELECT (SELECT CONCAT(Nombres,' ', Apellidos) As Empleado FROM Empleado where Id_Empleado = EP.Id_Empleado) AS Empleado, "
            + "Fecha_Asignado,Razon "
            + "FROM EMPLEADO_PROYECTO EP WHERE ID_PROYECTO =  ? AND Estatus = 'Activo'";

    public static String Query_Delete = "UPDATE EMPLEADO_PROYECTO SET Estatus = 'Eliminado' WHERE ID_Proyecto = ? and ID_Empleado= ?;";
  
    public static String Query_Insert_Update = "exec [dbo].[sp_Asignar_Empleado_Proyecto] ?,?,?,?";

    public static String Query_Select_Where = "SELECT convert(varchar, Fecha_Asignado, 101),Razon FROM EMPLEADO_PROYECTO WHERE ID_Empleado =?";

    public static String Query_Combo_Vendedores = "";

    public Empleado_Proyecto(String Empleado, String Fecha_asignacion, String Descripcion) {
        this.Empleado = Empleado;
        this.Fecha_asignacion = Fecha_asignacion;
        this.Descripcion = Descripcion;
    }

    public String getEmpleado() {
        return Empleado;
    }

    public void setEmpleado(String Empleado) {
        this.Empleado = Empleado;
    }

    public String getProyecto() {
        return Proyecto;
    }

    public void setProyecto(String Proyecto) {
        this.Proyecto = Proyecto;
    }

    public String getFecha_asignacion() {
        return Fecha_asignacion;
    }

    public void setFecha_asignacion(String Fecha_asignacion) {
        this.Fecha_asignacion = Fecha_asignacion;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String Descripcion) {
        this.Descripcion = Descripcion;
    }

    public String getEstatus() {
        return Estatus;
    }

    public void setEstatus(String Estatus) {
        this.Estatus = Estatus;
    }

}
