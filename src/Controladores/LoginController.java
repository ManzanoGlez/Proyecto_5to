package Controladores;

import Modelo.Empleado;
import Rutas.Rutas;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;

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
            Rutas r = new Rutas();
            r.Dashboard(event);

        } else {
            Lbl_Alerta.setVisible(true);
        }

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
