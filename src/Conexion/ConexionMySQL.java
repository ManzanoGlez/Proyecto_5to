package Conexion;

import java.sql.*;
import javax.swing.JOptionPane;

/*
 * @author Manzano
 */
public class ConexionMySQL {

    public String db = "fluxing_viaticos";
    public String url = "jdbc:mysql://localhost:3306/" + db;
    public String user = "root";
    public String pass = "";

    public Connection Conectar() {
        Connection Conexion = null;
        try {
            Class.forName("org.gjt.mm.mysql.Driver");
            Conexion = DriverManager.getConnection(this.url, this.user, this.pass);
        } catch (ClassNotFoundException | SQLException e) {

            JOptionPane.showMessageDialog(null, "Debes iniciar MySQL");
            System.out.println("Verifica si tienes el driver instalado");
            System.out.println(e.getMessage());
        }
        return Conexion;
    }

    //Comprueba la conexion
    public void ComprobarConexion() {

        if (Conectar() != null) {
            JOptionPane.showMessageDialog(null, "Conectado con MySQL");
            try {
                Conectar().close();
            } catch (SQLException ex) {

                System.out.println("Error al desconectar " + ex);
            }
        }
    }

    public void CerrarConexion() {

        try {
            Conectar().close();
        } catch (SQLException ex) {

            System.out.println("Error al desconectar " + ex);
        }
    }

    public void ReinciarBD() {
        java.sql.Connection conexion = this.Conectar();
        try {

            PreparedStatement pst = conexion.prepareStatement("Truncate table "+this.db+"");

            pst.executeUpdate();

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(null, "Error al Reinciar BD : " + e.getMessage());
        }
    }

}
