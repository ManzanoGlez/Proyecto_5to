package Controladores;

import Conexion.ConexionSQLServer;
import Modelo.Empleado;
import Modelo.Proyecto;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Window;

/*
 * FXML Controller class
 * @author jorge
 */
public class Alta_Modificar_ProyectoController implements Initializable {

    @FXML
    private AnchorPane Pane_Ver_Modificar;
    @FXML
    private Label lbl_Titulo;
    @FXML
    private JFXComboBox<String> Combo_Vendedor;
    @FXML
    private JFXTextField Txt_Nombre_Proyecto;
    @FXML
    private JFXTextField Txt_Cliente;
    @FXML
    private JFXTextField Txt_Codigo;
    @FXML
    private JFXTextField Txt_Precio;
    @FXML
    private JFXTextField Txt_Anticipo;
    @FXML
    private JFXTextField Txt_sucursal;
    @FXML
    private DatePicker Date_Fecha_inicio;
    @FXML
    private DatePicker Date_Fecha_termino;
    @FXML
    private TextArea TA_Detalles;
    @FXML
    private JFXComboBox<String> Combo_pais;
    @FXML
    private JFXComboBox<String> Combo_estado;
    @FXML
    private JFXTextField Txt_Ciudad;
    @FXML
    private JFXComboBox<String> Combo_estatus;
    @FXML
    private Button Btn_Detalle;

    ConexionSQLServer SQL = new ConexionSQLServer();

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        Cargar_Combox();
        Atributos_Textfield();
        Determina_Modifica_Insertar(Proyecto.ID_Modificar);

    }

    ///Llenar Comboboxs
    public void Cargar_Combox() {

        String Query;
        PreparedStatement ps;
        ResultSet rs;

        try {

            //  vendedores
            Query = Empleado.Query_Combo_Vendedores;
            ps = SQL.Conectar().prepareStatement(Query);
            rs = ps.executeQuery();
            Combo_Vendedor.getItems().add("Seleccióna..");
            while (rs.next()) {
                Combo_Vendedor.getItems().add(rs.getString(1));
            }

            Combo_Vendedor.setValue("Seleccióna..");

            //  pais
            Query = "SELECT paisnombre FROM PAIS;";
            ps = SQL.Conectar().prepareStatement(Query);
            rs = ps.executeQuery();

            while (rs.next()) {
                Combo_pais.getItems().add(rs.getString(1));
            }
            Combo_pais.getItems().add("Seleccióna..");
            Combo_pais.setValue("Seleccióna..");

            //  Estado
            Combo_pais.valueProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                try {

                    String Query2;
                    PreparedStatement ps2;
                    ResultSet rs2;

                    Combo_estado.getItems().clear();

                    Query2 = "SELECT estadonombre FROM PAIS P JOIN ESTADO E ON P.ID = E.ubicacionpaiid WHERE PAISNOMBRE  = ?";
                    ps2 = SQL.Conectar().prepareStatement(Query2);
                    ps2.setString(1, Combo_pais.getValue());

                    rs2 = ps2.executeQuery();
                    Combo_estado.getItems().add("Seleccióna..");
                    while (rs2.next()) {
                        Combo_estado.getItems().add(rs2.getString(1));
                    }
                    Combo_estado.setValue("Seleccióna..");
                } catch (SQLException e) {

                    System.out.println("Error alta/baja proyectos llenar combobox de vendedores :" + e.getMessage());

                }
            });

        } catch (SQLException e) {

            System.out.println("Error al llenar combobox :" + e.getMessage());
        }

        //  status  
        Combo_estatus.getItems().add("Activo");
        Combo_estatus.getItems().add("Cerrado");
        Combo_estatus.getItems().add("Pendiente");
        Combo_estatus.getItems().add("Cancelado");
        Combo_estatus.setValue("Activo");
    }

    @FXML
    void Guardar(ActionEvent event) {

        if (!Validar_Campos()) {

            if (Proyecto.ID_Modificar != 0) {
                //Actualizar

                if (Actualizar()) {
                    Mostrar_Alerta(Alert.AlertType.INFORMATION, Pane_Ver_Modificar.getScene().getWindow(),
                            "Exito!", "Actualización guardado exitosa mente");
                    try {
                        AnchorPane pane = FXMLLoader.load(getClass().getResource("/Vista/Proyectos.fxml"));
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
                        AnchorPane pane = FXMLLoader.load(getClass().getResource("/Vista/Proyectos.fxml"));
                        Pane_Ver_Modificar.getChildren().setAll(pane);
                    } catch (IOException ex) {
                        Logger.getLogger(Alta_Modificar_EmpleadoController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

        }
    }

    //Carga datos en caso de recibir un id 
    public void Cargar_Datos(int id) {

        try {

            String Query;
            PreparedStatement ps;
            ResultSet rs;
            DateTimeFormatter Formato = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            LocalDate fecha;

            Query = Proyecto.Query_Select_Cargar_Info;
            ps = SQL.Conectar().prepareStatement(Query);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            while (rs.next()) {

                Combo_Vendedor.setValue(rs.getString(1));
                Txt_Nombre_Proyecto.setText(rs.getString(2));
                Txt_Codigo.setText(rs.getString(3));
                Txt_Precio.setText(rs.getString(4));
                Txt_Anticipo.setText(rs.getString(5));
                Txt_Cliente.setText(rs.getString(6));
                fecha = LocalDate.parse(rs.getString(7), Formato);
                Date_Fecha_inicio.setValue(fecha);
                fecha = LocalDate.parse(rs.getString(8), Formato);
                Date_Fecha_termino.setValue(fecha);
                TA_Detalles.setText(rs.getString(9));
                Combo_pais.setValue(rs.getString(10));
                Txt_Ciudad.setText(rs.getString(11));
                Combo_estado.setValue(rs.getString(12));
                Txt_sucursal.setText(rs.getString(13));
                Combo_estatus.setValue(rs.getString(14));

            }

        } catch (SQLException e) {

            System.out.println("Error en Cargar Datos empleado :" + e.getMessage());
        }

    }

    // obtiene el id del vendedor segun su nombre
    public int ObtenerVenderdor() {

        int id = 0;
        try {

            String Query;
            PreparedStatement ps;
            ResultSet rs;

            String partes[] = Combo_Vendedor.getValue().split(" ");
            String nombres = partes[0];
            String Apellidos = partes[1];

            Query = "SELECT ID_Empleado FROM EMPLEADO P WHERE Nombres = ? AND Apellidos = ?";
            ps = SQL.Conectar().prepareStatement(Query);
            ps.setString(1, nombres);
            ps.setString(2, Apellidos);
            rs = ps.executeQuery();

            while (rs.next()) {
                id = rs.getInt(1);
            }

            System.out.println("id vendedor :" + id);

        } catch (SQLException e) {
            System.out.println("Error en ObtenerVenderdor:" + e.getMessage());

        }
        return id;
    }

    //Insertar nuevo 
    public boolean Intertar() {

        boolean Exito;

        try {
            PreparedStatement pst;

            String Queryinsert = Proyecto.Query_Insert;

            pst = SQL.Conectar().prepareStatement(Queryinsert);

            pst.setInt(1, ObtenerVenderdor());
            pst.setString(2, Txt_Nombre_Proyecto.getText());
            pst.setString(3, Txt_Codigo.getText());
            pst.setString(4, Txt_Precio.getText());
            pst.setFloat(5, Float.valueOf(Txt_Anticipo.getText()));
            pst.setString(6, Txt_Cliente.getText());
            pst.setString(7, Date_Fecha_inicio.getValue().toString());
            pst.setString(8, Date_Fecha_termino.getValue().toString());
            pst.setString(9, TA_Detalles.getText());
            pst.setString(10, Combo_pais.getValue());
            pst.setString(11, Combo_estado.getValue());
            pst.setString(12, Txt_Ciudad.getText());
            pst.setString(13, Txt_sucursal.getText());
            pst.setString(14, Combo_estatus.getValue());

            pst.executeUpdate();

            Exito = true;

        } catch (SQLException e) {

            if (e.getMessage().equals("Se ha generado un conjunto de resultados para actualización.")) {
                Mostrar_Alerta(Alert.AlertType.ERROR, Pane_Ver_Modificar.getScene().getWindow(),
                        "Error!", "Nombre de proyecto o codigo existente , cambielo porfavor.");

            } else {
                System.out.println("Error en Guardar Proyectos :" + e.getMessage());

            }

            Exito = false;
        }

        return Exito;

    }

    //Modificar registro
    public boolean Actualizar() {

        boolean Exito;

        try {

            String Query;
            PreparedStatement ps;
            Query = Proyecto.Query_Update;

            ps = SQL.Conectar().prepareStatement(Query);

            ps.setInt(1, Proyecto.ID_Modificar);
            ps.setInt(2, ObtenerVenderdor());
            ps.setString(3, Txt_Nombre_Proyecto.getText());
            ps.setString(4, Txt_Codigo.getText());
            ps.setString(5, Txt_Precio.getText());
            ps.setFloat(6, Float.valueOf(Txt_Anticipo.getText()));
            ps.setString(7, Txt_Cliente.getText());
            ps.setString(8, Date_Fecha_inicio.getValue().toString());
            ps.setString(9, Date_Fecha_termino.getValue().toString());
            ps.setString(10, TA_Detalles.getText());
            ps.setString(11, Combo_pais.getValue());
            ps.setString(12, Combo_estado.getValue());
            ps.setString(13, Txt_Ciudad.getText());
            ps.setString(14, Txt_sucursal.getText());
            ps.setString(15, Combo_estatus.getValue());

            ps.executeUpdate();

            Exito = true;

        } catch (SQLException e) {
            System.out.println("Error en actualizar proyecto :" + e.getMessage());
            Exito = false;
        }

        return Exito;

    }

    //Valida campos 
    public boolean Validar_Campos() {

        boolean Error = false;

        if (Combo_Vendedor.getValue().equals("Seleccióna..")) {
            Mostrar_Alerta(Alert.AlertType.ERROR, Pane_Ver_Modificar.getScene().getWindow(),
                    "Error!", "Por favor seleccióna un vendedor");
            Error = true;
            return Error;
        }
        if (Txt_Nombre_Proyecto.getText().isEmpty()) {
            Mostrar_Alerta(Alert.AlertType.ERROR, Pane_Ver_Modificar.getScene().getWindow(),
                    "Error!", "Por favor inserta el nombre del proyecto");
            Error = true;
            return Error;
        }
        if (Txt_Cliente.getText().isEmpty()) {
            Mostrar_Alerta(Alert.AlertType.ERROR, Pane_Ver_Modificar.getScene().getWindow(),
                    "Error!", "Por favor inserta el cliente");
            Error = true;
            return Error;
        }
        if (Txt_Codigo.getText().isEmpty()) {
            Mostrar_Alerta(Alert.AlertType.ERROR, Pane_Ver_Modificar.getScene().getWindow(),
                    "Error!", "Por favor inserta el codigo de proyecto");
            Error = true;
            return Error;
        }
        if (Txt_Precio.getText().isEmpty()) {
            Mostrar_Alerta(Alert.AlertType.ERROR, Pane_Ver_Modificar.getScene().getWindow(),
                    "Error!", "Por favor inserta el precio");
            Error = true;
            return Error;
        }
        if (Txt_Anticipo.getText().isEmpty()) {
            Mostrar_Alerta(Alert.AlertType.ERROR, Pane_Ver_Modificar.getScene().getWindow(),
                    "Error!", "Por favor inserta el anticipo");
            Error = true;
            return Error;
        }
        if (Txt_sucursal.getText().isEmpty()) {
            Mostrar_Alerta(Alert.AlertType.ERROR, Pane_Ver_Modificar.getScene().getWindow(),
                    "Error!", "Por favor inserta la sucursal/planta");
            Error = true;
            return Error;
        }

        if (Date_Fecha_inicio.getValue() == null) {
            Mostrar_Alerta(Alert.AlertType.ERROR, Pane_Ver_Modificar.getScene().getWindow(),
                    "Error!", "Por favor inserta la fecha de inicio");
            Error = true;
            return Error;
        }

        if (Date_Fecha_termino.getValue() == null) {
            Mostrar_Alerta(Alert.AlertType.ERROR, Pane_Ver_Modificar.getScene().getWindow(),
                    "Error!", "Por favor inserta la fecha de termino");
            Error = true;
            return Error;
        }

        if (TA_Detalles.getText().isEmpty()) {
            Mostrar_Alerta(Alert.AlertType.ERROR, Pane_Ver_Modificar.getScene().getWindow(),
                    "Error!", "Por favor inserta detalles del proyecto");
            Error = true;
            return Error;
        }

        if (Combo_pais.getValue().equals("Seleccióna..")) {
            Mostrar_Alerta(Alert.AlertType.ERROR, Pane_Ver_Modificar.getScene().getWindow(),
                    "Error!", "Por favor seleccióna un pais");
            Error = true;
            return Error;
        }

        if (Combo_estado.getValue().equals("Seleccióna..")) {
            Mostrar_Alerta(Alert.AlertType.ERROR, Pane_Ver_Modificar.getScene().getWindow(),
                    "Error!", "Por favor seleccióna un estado");
            Error = true;
            return Error;
        }

        if (Txt_Ciudad.getText().isEmpty()) {
            Mostrar_Alerta(Alert.AlertType.ERROR, Pane_Ver_Modificar.getScene().getWindow(),
                    "Error!", "Por favor inserta una ciudad");
            Error = true;
            return Error;
        }

        if (Combo_estatus.getValue().equals("Seleccióna..")) {
            Mostrar_Alerta(Alert.AlertType.ERROR, Pane_Ver_Modificar.getScene().getWindow(),
                    "Error!", "Por favor seleccióna un estado del proyecto");
            Error = true;
            return Error;
        }

        return Error;

    }

    @FXML
    public void ver_Detalle() {
        try {
            AnchorPane pane = FXMLLoader.load(getClass().getResource("/Vista/Detalle_Proyecto_a_Empleados.fxml"));
            Pane_Ver_Modificar.getChildren().setAll(pane);
        } catch (IOException ex) {
            Logger.getLogger(EmpleadosController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    public void Regresar() {
        try {
            AnchorPane pane = FXMLLoader.load(getClass().getResource("/Vista/Proyectos.fxml"));
            Pane_Ver_Modificar.getChildren().setAll(pane);
        } catch (IOException ex) {
            Logger.getLogger(EmpleadosController.class.getName()).log(Level.SEVERE, null, ex);
        }
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

    //Verifica si tiene un id para modificar
    private void Determina_Modifica_Insertar(int id) {

        if (id != 0) {
            Cargar_Datos(id);
            lbl_Titulo.setText("Ver Proyecto");
            Txt_Nombre_Proyecto.setEditable(false);
            Txt_Codigo.setEditable(false);
            Btn_Detalle.setVisible(true);
        } else {
            Txt_Nombre_Proyecto.setEditable(true);
            Txt_Codigo.setEditable(true);
            Btn_Detalle.setVisible(false);
        }
    }

    //Da atributos a textfield
    private void Atributos_Textfield() {
        //Validar numerico y decimales
        Txt_Precio.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (!newValue.matches("\\d{0,10}([\\.]\\d{0,10})?")) {
                Txt_Precio.setText(oldValue);
            }
        });

        //Validar numerico y decimales
        Txt_Anticipo.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (!newValue.matches("\\d{0,10}([\\.]\\d{0,10})?")) {
                Txt_Anticipo.setText(oldValue);
            }
        });
    }

}
