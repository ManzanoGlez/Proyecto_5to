package Controladores;

import Modelo.Empleado;
import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

/*
 * FXML Controller class
 * @author jorge
 */
public class MenuController implements Initializable {

    @FXML
    private JFXButton Btn_Empleados;
    @FXML
    private JFXButton Btn_Proyectos;
    @FXML
    private JFXButton Btn_Viajes;
    @FXML
    private JFXButton Btn_Facturas;
    @FXML
    private JFXButton Btn_Reportes;

    @FXML
    void Btn_Empleados(ActionEvent event) {
        System.out.println("Empleados");
    }

    @FXML
    void Btn_Proyectos(ActionEvent event) {
        System.out.println("Proyectos");
    }

    @FXML
    void Btn_Viajes(ActionEvent event) {
        System.out.println("Viaticos");
    }

    @FXML
    void Btn_Facturas(ActionEvent event) {
        System.out.println("Facturas");
    }

    @FXML
    void Btn_Reportes(ActionEvent event) {
        System.out.println("Reportes");
    }

    @FXML
    void Btn_Salir(ActionEvent event) {

        try {

            Parent Inicio_parent = FXMLLoader.load(getClass().getResource("/Vista/Login.fxml"));
            Scene Inicio_scene = new Scene(Inicio_parent);
            Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            app_stage.setScene(Inicio_scene);
            app_stage.setTitle("Login");
            app_stage.show();

            //Centrar
            Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
            app_stage.setX((primScreenBounds.getWidth() - app_stage.getWidth()) / 2);
            app_stage.setY((primScreenBounds.getHeight() - app_stage.getHeight()) / 4);

        } catch (IOException ex) {
            System.out.println("Error al cambiar de scene Menu : " + ex.getMessage());

        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        Btn_Empleados.setAccessibleText("Empleados");
        Btn_Proyectos.setAccessibleText("Proyectos");
        Btn_Viajes.setAccessibleText("Viaticos_Admin");
        Btn_Facturas.setAccessibleText("Facturas");
        Btn_Reportes.setAccessibleText("Reportes");

        //true => administrador false => normal
        if (!Empleado.Perfil) {
            Btn_Empleados.setText("     Perfil       ");
        }

    }

}
