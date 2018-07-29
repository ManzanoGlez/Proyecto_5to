
package Controladores;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author jorge
 */
public class Viaticos_AdminController implements Initializable {

    
            @FXML
    private Label lbl_Titulo;

    @FXML
    private AnchorPane Panel_Base;

    @FXML
    private Button Btn_Nuevo;

    
    
        @FXML
    public void Agregar() {
        try {
            AnchorPane pane = FXMLLoader.load(getClass().getResource("/Vista/Alta_Modificar_Viaticos.fxml"));
            Panel_Base.getChildren().setAll(pane);
        } catch (IOException ex) {
            Logger.getLogger(EmpleadosController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
