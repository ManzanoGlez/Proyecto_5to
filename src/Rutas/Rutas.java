package Rutas;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;

/**
 *
 * @author jorge
 */
public class Rutas {

    public void Login(Stage stage) {

        try {

            Parent root = FXMLLoader.load(getClass().getResource("/Vista/Login.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Inicio de sesión");
            stage.setResizable(false);
            stage.show();
            stage.getIcons().add(new Image("/Imagenes/velocimetro.png"));

        } catch (IOException e) {
            System.out.println("Error al mostrar Login : " + e.getMessage());
        }

    }

    public void Dashboard(ActionEvent event) {

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
    }
    
  
}
