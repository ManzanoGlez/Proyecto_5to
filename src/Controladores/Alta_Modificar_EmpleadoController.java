package Controladores;

import Conexion.ConexionSQLServer;
import Modelo.Empleado;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Window;

/*
 * FXML Controller class
 * @author jorge
 */
public class Alta_Modificar_EmpleadoController implements Initializable {

    ConexionSQLServer SQL = new ConexionSQLServer();
    Empleado empleado;

    @FXML
    private AnchorPane Pane_Ver_Modificar;
    @FXML
    private JFXTextField Txt_NSS;
    @FXML
    private JFXTextField Txt_Nombre;
    @FXML
    private JFXTextField Txt_Apellidos;
    @FXML
    private JFXTextField Txt_Direccion;
    @FXML
    private JFXTextField Txt_Celular;
    @FXML
    private JFXTextField TxtRFC;
    @FXML
    private DatePicker Date_Fecha_Nacimiento;
    @FXML
    private JFXTextField TxtCurp;
    @FXML
    private JFXTextField Txt_Correo;
    @FXML
    private DatePicker Date_Fecha_Ingreso;
    @FXML
    private JFXTextField Txt_Puesto;
    @FXML
    private JFXTextField Txt_Area;
    @FXML
    private JFXTextField Txt_Salario;
    @FXML
    private JFXPasswordField Txt_Contraseña;
    @FXML
    private JFXComboBox<String> Combo_Tipo_Cuenta;
    @FXML
    private Label Lbl_Alerta;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        Cargar_Combo_Buscar();

        //Validar numerico
        Txt_Celular.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (!newValue.matches("\\d*")) {
                Txt_Celular.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        //Validar numerico y decimales
        Txt_Salario.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (!newValue.matches("\\d{0,10}([\\.]\\d{0,10})?")) {
                Txt_Salario.setText(oldValue);
            }
        });

        //Carga Fecha actual por default
        Date_Fecha_Nacimiento.setValue(LocalDate.now());
        Date_Fecha_Ingreso.setValue(LocalDate.now());

        //Validar correo existente
        Txt_Correo.textProperty().addListener((observable, Valor_Viejo, Nuevo_val) -> {
            if (!Valor_Viejo.equals(Nuevo_val)) {
                Lbl_Alerta.setVisible(false);
            }
        });

        //Verifica si tiene un id para modificar
        int id = Empleado.ID_Modificar;
        if (id != 0) {
            Cargar_Datos_Empleado(id);
        }

    }

    @FXML
    void Regresar_Empleados(ActionEvent event) {
        try {
            AnchorPane pane = FXMLLoader.load(getClass().getResource("/Vista/Empleados.fxml"));
            Pane_Ver_Modificar.getChildren().setAll(pane);
        } catch (IOException ex) {
            Logger.getLogger(Alta_Modificar_EmpleadoController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void Guardar_Empleado(ActionEvent event) {

        if (!Validar_Campos()) {

            if (Intertar()) {
                Mostrar_Alerta(Alert.AlertType.INFORMATION, Pane_Ver_Modificar.getScene().getWindow(),
                        "Exito!", "Registro guardado exitosa mente");
                try {
                    AnchorPane pane = FXMLLoader.load(getClass().getResource("/Vista/Empleados.fxml"));
                    Pane_Ver_Modificar.getChildren().setAll(pane);
                } catch (IOException ex) {
                    Logger.getLogger(Alta_Modificar_EmpleadoController.class.getName()).log(Level.SEVERE, null, ex);
                }

            } else {
                if (!Lbl_Alerta.isVisible()) {
                    Mostrar_Alerta(Alert.AlertType.ERROR, Pane_Ver_Modificar.getScene().getWindow(),
                            "Error!", "Error al guardar registro.");
                }

            }
        }
    }

    //Insertar nuevo 
    public boolean Intertar() {

        boolean Exito;

        try {

            PreparedStatement ps;

            String Query = "exec [dbo].[sp_Alta_Empleado] ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?;";

            ps = SQL.Conectar().prepareStatement(Query);

            ps.setString(1, Txt_NSS.getText());
            ps.setString(2, Txt_Nombre.getText());
            ps.setString(3, Txt_Apellidos.getText());
            ps.setString(4, Txt_Direccion.getText());
            ps.setString(5, Txt_Celular.getText());
            ps.setString(6, TxtRFC.getText());
            ps.setString(7, Date_Fecha_Nacimiento.getValue().toString());
            ps.setString(8, TxtCurp.getText());
            ps.setString(9, Txt_Correo.getText());
            ps.setString(10, Date_Fecha_Ingreso.getValue().toString());
            ps.setString(11, Txt_Puesto.getText());
            ps.setString(12, Txt_Area.getText());
            ps.setString(13, Txt_Salario.getText());
            ps.setString(14, Txt_Contraseña.getText());
            ps.setString(15, Combo_Tipo_Cuenta.getValue());

            ps.executeUpdate();

            Exito = true;

        } catch (SQLException e) {
            System.out.println("Error en Guardar Empleados :" + e.getMessage());

            if (e.getMessage().equals("Se ha generado un conjunto de resultados para actualización.")) {
                Lbl_Alerta.setVisible(true);
            }

            Exito = false;
        }

        return Exito;

    }

    //Modificar registro
    public boolean Actualizar() {

        boolean Exito;

        try {

            PreparedStatement ps;

            String Query = "exec [dbo].[sp_Alta_Empleado] ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?;";

            ps = SQL.Conectar().prepareStatement(Query);

            ps.setString(1, Txt_NSS.getText());
            ps.setString(2, Txt_Nombre.getText());
            ps.setString(3, Txt_Apellidos.getText());
            ps.setString(4, Txt_Direccion.getText());
            ps.setString(5, Txt_Celular.getText());
            ps.setString(6, TxtRFC.getText());
            ps.setString(7, Date_Fecha_Nacimiento.getValue().toString());
            ps.setString(8, TxtCurp.getText());
            ps.setString(9, Txt_Correo.getText());
            ps.setString(10, Date_Fecha_Ingreso.getValue().toString());
            ps.setString(11, Txt_Puesto.getText());
            ps.setString(12, Txt_Area.getText());
            ps.setString(13, Txt_Salario.getText());
            ps.setString(14, Txt_Contraseña.getText());
            ps.setString(15, Combo_Tipo_Cuenta.getValue());

            ps.executeUpdate();

            Exito = true;

        } catch (SQLException e) {
            System.out.println("Error en Guardar Empleados :" + e.getMessage());

            if (e.getMessage().equals("Se ha generado un conjunto de resultados para actualización.")) {
                Lbl_Alerta.setVisible(true);
            }

            Exito = false;
        }

        return Exito;

    }

    public void Cargar_Datos_Empleado(int id) {

        try {
            DateTimeFormatter Formato = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            LocalDate fecha;

            PreparedStatement ps;
            ResultSet rs;

            String Query = "SELECT "
                    + "e.NSS,"
                    + "e.Nombres,"
                    + "e.Apellidos,"
                    + "e.Direccion,"
                    + "e.Telefono,"
                    + "e.RFC,"
                    + "convert(varchar, e.Fecha_Nacimiento, 101) as Fecha_Nacimiento,"
                    + "e.CURP,"
                    + "e.Correo,"
                    + "convert(varchar, e.Fecha_Ingreso, 101),"
                    + "e.Puesto,"
                    + "e.Area,"
                    + "e.Sueldo,u.Contraseña,u.Tipo "
                    + "FROM empleado e JOIN usuario u ON e.ID_Empleado = u.ID_Empleado "
                    + "WHERE e.ID_Empleado = ?";

            ps = SQL.Conectar().prepareStatement(Query);

            ps.setInt(1, id);

            rs = ps.executeQuery();

            while (rs.next()) {

                Txt_NSS.setText(rs.getString(1));
                Txt_Nombre.setText(rs.getString(2));
                Txt_Apellidos.setText(rs.getString(3));
                Txt_Direccion.setText(rs.getString(4));
                Txt_Celular.setText(rs.getString(5));
                TxtRFC.setText(rs.getString(6));
                fecha = LocalDate.parse(rs.getString(7), Formato);
                Date_Fecha_Nacimiento.setValue(fecha);
                TxtCurp.setText(rs.getString(8));
                Txt_Correo.setText(rs.getString(9));
                fecha = LocalDate.parse(rs.getString(10), Formato);
                Date_Fecha_Ingreso.setValue(fecha);
                Txt_Puesto.setText(rs.getString(11));
                Txt_Area.setText(rs.getString(12));
                Txt_Salario.setText(rs.getString(13));
                Txt_Contraseña.setText(rs.getString(14));
                Combo_Tipo_Cuenta.setValue(rs.getString(15));
            }

        } catch (SQLException e) {

            System.out.println("Error en Cargar Datos empleado :" + e.getMessage());
        }

    }

    public boolean Validar_Campos() {

        boolean Error = false;

        if (Txt_Nombre.getText().isEmpty()) {
            Mostrar_Alerta(Alert.AlertType.ERROR, Pane_Ver_Modificar.getScene().getWindow(),
                    "Error!", "Por favor inserta el nombre");
            Error = true;
            return Error;
        }
        if (Txt_Apellidos.getText().isEmpty()) {
            Mostrar_Alerta(Alert.AlertType.ERROR, Pane_Ver_Modificar.getScene().getWindow(),
                    "Error!", "Por favor inserta el apellido");
            Error = true;
            return Error;
        }
        if (Txt_Celular.getText().isEmpty()) {
            Mostrar_Alerta(Alert.AlertType.ERROR, Pane_Ver_Modificar.getScene().getWindow(),
                    "Error!", "Por favor inserta el numero celular/teléfono");
            Error = true;
            return Error;
        }
        if (TxtRFC.getText().isEmpty()) {
            Mostrar_Alerta(Alert.AlertType.ERROR, Pane_Ver_Modificar.getScene().getWindow(),
                    "Error!", "Por favor inserta el RFC");
            Error = true;
            return Error;
        }
        if (Txt_Direccion.getText().isEmpty()) {
            Mostrar_Alerta(Alert.AlertType.ERROR, Pane_Ver_Modificar.getScene().getWindow(),
                    "Error!", "Por favor inserta la dirección");
            Error = true;
            return Error;
        }
        if (Txt_NSS.getText().isEmpty()) {
            Mostrar_Alerta(Alert.AlertType.ERROR, Pane_Ver_Modificar.getScene().getWindow(),
                    "Error!", "Por favor inserta el NSS");
            Error = true;
            return Error;
        }
        if (TxtCurp.getText().isEmpty()) {
            Mostrar_Alerta(Alert.AlertType.ERROR, Pane_Ver_Modificar.getScene().getWindow(),
                    "Error!", "Por favor inserta la curp");
            Error = true;
            return Error;
        }

        if (Date_Fecha_Nacimiento.getValue() == null) {
            Mostrar_Alerta(Alert.AlertType.ERROR, Pane_Ver_Modificar.getScene().getWindow(),
                    "Error!", "Por favor inserta la fecha de nacimiento");
            Error = true;
            return Error;
        }

        if (Txt_Puesto.getText().isEmpty()) {
            Mostrar_Alerta(Alert.AlertType.ERROR, Pane_Ver_Modificar.getScene().getWindow(),
                    "Error!", "Por favor inserta el puesto");
            Error = true;
            return Error;
        }

        if (Txt_Area.getText().isEmpty()) {
            Mostrar_Alerta(Alert.AlertType.ERROR, Pane_Ver_Modificar.getScene().getWindow(),
                    "Error!", "Por favor inserta el area de trabajo");
            Error = true;
            return Error;
        }

        if (Txt_Salario.getText().isEmpty()) {
            Mostrar_Alerta(Alert.AlertType.ERROR, Pane_Ver_Modificar.getScene().getWindow(),
                    "Error!", "Por favor inserta el salario");
            Error = true;
            return Error;
        }

        if (Date_Fecha_Ingreso.getValue() == null) {
            Mostrar_Alerta(Alert.AlertType.ERROR, Pane_Ver_Modificar.getScene().getWindow(),
                    "Error!", "Por favor inserta la fecha de ingreso");
            Error = true;
            return Error;
        }

        if (Txt_Correo.getText().isEmpty()) {
            Mostrar_Alerta(Alert.AlertType.ERROR, Pane_Ver_Modificar.getScene().getWindow(),
                    "Error!", "Por favor inserta el correo");
            Error = true;
            return Error;
        }

        if (Txt_Contraseña.getText().isEmpty()) {
            Mostrar_Alerta(Alert.AlertType.ERROR, Pane_Ver_Modificar.getScene().getWindow(),
                    "Error!", "Por favor inserta la contraseña");
            Error = true;

        }

        return Error;

    }

    private void Mostrar_Alerta(Alert.AlertType alertType, Window panel, String Titulo, String Mensaje) {
        Alert alert = new Alert(alertType);
        alert.setTitle(Titulo);
        alert.setHeaderText(null);
        alert.setContentText(Mensaje);
        alert.initOwner(panel);
        alert.show();
    }

    ///Llenar Combobox
    public void Cargar_Combo_Buscar() {
        Combo_Tipo_Cuenta.getItems().add("Administrador");
        Combo_Tipo_Cuenta.getItems().add("Empleado");
        Combo_Tipo_Cuenta.setValue("Empleado");
    }

}
