package Controladores;

import Conexion.ConexionSQLServer;
import Modelo.Empleado;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import com.jfoenix.controls.JFXComboBox;
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
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
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
    private TextField Txt_NSS;
    @FXML
    private TextField Txt_Nombre;
    @FXML
    private TextField Txt_Apellidos;
    @FXML
    private TextField Txt_Direccion;
    @FXML
    private TextField Txt_Celular;
    @FXML
    private TextField TxtRFC;
    @FXML
    private DatePicker Date_Fecha_Nacimiento;
    @FXML
    private TextField TxtCurp;
    @FXML
    private TextField Txt_Correo;
    @FXML
    private DatePicker Date_Fecha_Ingreso;
    @FXML
    private TextField Txt_Puesto;
    @FXML
    private TextField Txt_Area;
    @FXML
    private TextField Txt_Salario;
    @FXML
    private PasswordField Txt_Contraseña;
    @FXML
    private JFXComboBox<String> Combo_Tipo_Cuenta;
    @FXML
    private Label Lbl_Alerta;
    @FXML
    private Label lbl_Titulo;
    @FXML
    private Button Btn_Detalle;


    @Override
    public void initialize(URL url, ResourceBundle rb) {

        Cargar_Combo_Buscar();
        Atributos_TextFied();
        Determina_Modifica_Insertar(Empleado.ID_Modificar);

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

            if (Empleado.ID_Modificar != 0) {
                //Actualizar

                if (Actualizar()) {
                    Mostrar_Alerta(Alert.AlertType.INFORMATION, Pane_Ver_Modificar.getScene().getWindow(),
                            "Exito!", "Actualización guardado exitosa mente");
                    try {
                        AnchorPane pane = FXMLLoader.load(getClass().getResource("/Vista/Empleados.fxml"));
                        Pane_Ver_Modificar.getChildren().setAll(pane);
                    } catch (IOException ex) {
                        Logger.getLogger(Alta_Modificar_EmpleadoController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            } else {
                //Insertar
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
    }

    //Insertar nuevo 
    public boolean Intertar() {

        boolean Exito;

        try {

            PreparedStatement ps;

            String Query = Empleado.Query_Insert;

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
            String Query = Empleado.Query_Update;

            ps = SQL.Conectar().prepareStatement(Query);

            ps.setInt(1, Empleado.ID_Modificar);
            ps.setString(2, Txt_NSS.getText());
            ps.setString(3, Txt_Nombre.getText());
            ps.setString(4, Txt_Apellidos.getText());
            ps.setString(5, Txt_Direccion.getText());
            ps.setString(6, Txt_Celular.getText());
            ps.setString(7, TxtRFC.getText());
            ps.setString(8, Date_Fecha_Nacimiento.getValue().toString());
            ps.setString(9, TxtCurp.getText());
            ps.setString(10, Txt_Correo.getText());
            ps.setString(11, Date_Fecha_Ingreso.getValue().toString());
            ps.setString(12, Txt_Puesto.getText());
            ps.setString(13, Txt_Area.getText());
            ps.setString(14, Txt_Salario.getText());
            ps.setString(15, Txt_Contraseña.getText());
            ps.setString(16, Combo_Tipo_Cuenta.getValue());

            ps.executeUpdate();

            Exito = true;

        } catch (SQLException e) {
            System.out.println("Error en actualizar Empleados :" + e.getMessage());
            Exito = false;
        }

        return Exito;

    }

    // carga datos en caso de recibir un id
    public void Cargar_Datos(int id) {

        try {
            DateTimeFormatter Formato = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            LocalDate fecha;

            PreparedStatement ps;
            ResultSet rs;

            String Query = Empleado.Query_Select_Where;

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

    //Mostrar Mensajes de alerta 
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

    private void Determina_Modifica_Insertar(int id) {
        if (id != 0) {
            Cargar_Datos(id);
            lbl_Titulo.setText("Ver datos Empleado");
            Txt_Correo.setEditable(false);

        } else {
            Txt_Correo.setEditable(true);
        }
    }

    private void Atributos_TextFied() {
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
    }
  

}
