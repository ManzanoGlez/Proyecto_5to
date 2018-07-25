package Controladores;

import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

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
    private JFXButton Btn_Salir;

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
        System.out.println("Viajes");
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
        System.exit(0);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        Btn_Empleados.setAccessibleText("Empleados");
        Btn_Proyectos.setAccessibleText("Proyectos");
        Btn_Viajes.setAccessibleText("Viajes");
        Btn_Facturas.setAccessibleText("Facturas");
        Btn_Reportes.setAccessibleText("Reportes");

    }

}
