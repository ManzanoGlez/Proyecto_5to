package sistema_fluxing;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/*
 * @author Jorge Manzano
 */
public class Sistema_Fluxing extends Application {

    @Override
    public void start(Stage stage) throws Exception {

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

    public static void main(String[] args) {
        launch(args);
    }



}
