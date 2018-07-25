package sistema_fluxing;

import Rutas.Rutas;
import javafx.application.Application;
import javafx.stage.Stage;

/*
 * @author Jorge Manzano
 */
public class Sistema_Fluxing extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        Rutas r = new Rutas();
        r.Login(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }

}
