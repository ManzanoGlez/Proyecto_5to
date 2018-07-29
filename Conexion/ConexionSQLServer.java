package Conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/*
 * @author jorge
 */

public class ConexionSQLServer {

    private final String db = "Fluxing_Viaticos";
    private final String url = "jdbc:sqlserver://;database=" + db;
    private final String user = "Manzano";
    private final String pass = "12345";


    public Connection Conectar() {
        Connection Conexion = null;
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Conexion = DriverManager.getConnection(this.url, this.user, this.pass);
        } catch (ClassNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return Conexion;
    }

    public Boolean ComprobarConexion() {
        Boolean Connected;

        if (Conectar() != null) {
            Connected = true;
            JOptionPane.showMessageDialog(null, "Conexión con exito a SQL Server", "Exito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            Connected = false;
            JOptionPane.showMessageDialog(null, "Conexión Fallida", "Exito", JOptionPane.ERROR_MESSAGE);
        }
        return Connected;
    }

    public void CerrarConexion() {

        try {
            Conectar().close();
        } catch (SQLException ex) {

            System.out.println("Error al desconectar " + ex);
        }
    }

}
