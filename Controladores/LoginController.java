package Controladores;

import Modelo.Empleado;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Screen;
import javafx.stage.Stage;

/*
 * FXML Controller class
 * @author Jorge Manzano
 */
public class LoginController implements Initializable {
    
    public static int ID_Usuario;
    
    @FXML
    private TextField Txt_Usuario;
    @FXML
    private PasswordField Txt_Password;
    @FXML
    private Label Lbl_Alerta;
    
    @FXML
    private void Inicio_Sesion(ActionEvent event) {
        
        Empleado e = new Empleado();
        
        if (e.Inicio_Sesion(Txt_Usuario.getText(), Txt_Password.getText())) {
            try {
                
                Parent Inicio_parent = FXMLLoader.load(getClass().getResource("/Vista/Dashboard.fxml"));
                Scene Inicio_scene = new Scene(Inicio_parent);
                Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                app_stage.setScene(Inicio_scene);
                app_stage.setTitle("Menu Principal");
                app_stage.show();

                //Centrar
                Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
                app_stage.setX((primScreenBounds.getWidth() - app_stage.getWidth()) / 2);
                app_stage.setY((primScreenBounds.getHeight() - app_stage.getHeight()) / 4);
                
            } catch (IOException ex) {
                System.out.println("Error al cambiar de scene Menu : " + ex.getMessage());
                
            }
            
        } else {
            Lbl_Alerta.setVisible(true);
        }
        
    }
    
    @FXML
    private void Salir(ActionEvent event) {     
        System.exit(0);
    }
    
    private void Validar_Campos() {
        Txt_Usuario.textProperty().addListener((observable, Valor_Viejo, Nuevo_val) -> {
            if (!Valor_Viejo.equals(Nuevo_val)) {
                Lbl_Alerta.setVisible(false);
            }
        });
        
        Txt_Password.textProperty().addListener((observable, Valor_Viejo, Nuevo_val) -> {
            if (!Valor_Viejo.equals(Nuevo_val)) {
                Lbl_Alerta.setVisible(false);
            }
        });
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Validar_Campos();
        
    }
    
}
