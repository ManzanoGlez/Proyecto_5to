package Controladores;

import Modelo.Empleado;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/*
 * FXML Controller class
 * @author Jorge Manzano
 */
public class DashboardController implements Initializable {

    Empleado Empleado = new Empleado();
    HamburgerBackArrowBasicTransition Btn_Burger_Menu;
    @FXML
    private AnchorPane Pane_Base;
    @FXML
    private JFXHamburger Menu_Hamburguesa;
    @FXML
    private JFXDrawer Panel_Drawer;
    @FXML
    private Label Lbl_Usuario;
    @FXML
    private Label lbl_Perfil;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Menu_Hamburguesa();

        Empleado.Enviar_Info_Usuario_Loggeado(LoginController.ID_Usuario);
        Lbl_Usuario.setText("Bienvenido : " + Empleado.getNombres() + " " + Empleado.getApellidos());
        lbl_Perfil.setText("Perfil : " + Empleado.getStatus());

        //Ventana por defecto
        //true => administrador false => normal
        try {
            if (!Empleado.Perfil) {

                //normal
                Empleado.ID_Modificar = Empleado.Usuario_Login;
                AnchorPane pane = FXMLLoader.load(getClass().getResource("/Vista/Empleado_Ver_Perfil.fxml"));
                Pane_Base.getChildren().setAll(pane);

            } else {
                //Admin

                AnchorPane pane = FXMLLoader.load(getClass().getResource("/Vista/Empleados.fxml"));
                Pane_Base.getChildren().setAll(pane);

            }

            Btn_Burger_Menu.setRate(Btn_Burger_Menu.getRate() * -1);
            Btn_Burger_Menu.play();
            Panel_Drawer.open();
            Panel_Drawer.setDisable(false);

        } catch (IOException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void Menu_Hamburguesa() {

        try {
            VBox box = FXMLLoader.load(getClass().getResource("/Vista/Menu.fxml"));
            Panel_Drawer.setSidePane(box);
            Panel_Drawer.setOverLayVisible(false);

            box.getChildren().forEach((node) -> {

                node.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {

                    if (node.getAccessibleText() != null) {
                        switch (node.getAccessibleText()) {

                            case "Empleados":
                                try {
                                    //true => administrador false => normal
                                    if (!Empleado.Perfil) {

                                        //normal
                                        Empleado.ID_Modificar = Empleado.Usuario_Login;
                                        AnchorPane pane = FXMLLoader.load(getClass().getResource("/Vista/Empleado_Ver_Perfil.fxml"));
                                        Pane_Base.getChildren().setAll(pane);

                                        Btn_Burger_Menu.setRate(Btn_Burger_Menu.getRate() * -1);
                                        Btn_Burger_Menu.play();
                                        Panel_Drawer.close();
                                        Panel_Drawer.setDisable(true);

                                    } else {
                                        //Admin

                                        AnchorPane pane = FXMLLoader.load(getClass().getResource("/Vista/Empleados.fxml"));
                                        Pane_Base.getChildren().setAll(pane);

                                        Btn_Burger_Menu.setRate(Btn_Burger_Menu.getRate() * -1);
                                        Btn_Burger_Menu.play();
                                        Panel_Drawer.close();
                                        Panel_Drawer.setDisable(true);

                                    }

                                } catch (IOException ex) {
                                    Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
                                }

                                break;

                            case "Proyectos":
                                try {
                                    if (!Empleado.Perfil) {
                                        //Empleado

                                        AnchorPane pane = FXMLLoader.load(getClass().getResource("/Vista/Empleado_Ver_Proyectos.fxml"));
                                        Pane_Base.getChildren().setAll(pane);
                                        Btn_Burger_Menu.setRate(Btn_Burger_Menu.getRate() * -1);
                                        Btn_Burger_Menu.play();
                                        Panel_Drawer.close();
                                        Panel_Drawer.setDisable(true);

                                    } else {
                                        //Admin

                                        AnchorPane pane = FXMLLoader.load(getClass().getResource("/Vista/Proyectos.fxml"));
                                        Pane_Base.getChildren().setAll(pane);
                                        Btn_Burger_Menu.setRate(Btn_Burger_Menu.getRate() * -1);
                                        Btn_Burger_Menu.play();
                                        Panel_Drawer.close();
                                        Panel_Drawer.setDisable(true);

                                    }

                                } catch (IOException ex) {
                                    Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
                                }

                                break;
                            case "Viaticos_Admin":
                                try {
                                    AnchorPane pane = FXMLLoader.load(getClass().getResource("/Vista/Viaticos_Admin.fxml"));
                                    Pane_Base.getChildren().setAll(pane);
                                    Btn_Burger_Menu.setRate(Btn_Burger_Menu.getRate() * -1);
                                    Btn_Burger_Menu.play();
                                    Panel_Drawer.close();
                                    Panel_Drawer.setDisable(true);

                                } catch (IOException ex) {
                                    Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                break;
                            case "Facturas":
                                try {
                                    AnchorPane pane = FXMLLoader.load(getClass().getResource("/Vista/Facturas.fxml"));
                                    Pane_Base.getChildren().setAll(pane);
                                    Btn_Burger_Menu.setRate(Btn_Burger_Menu.getRate() * -1);
                                    Btn_Burger_Menu.play();
                                    Panel_Drawer.close();
                                    Panel_Drawer.setDisable(true);

                                } catch (IOException ex) {
                                    Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                break;
                            case "Reportes":
                                try {
                                    AnchorPane pane = FXMLLoader.load(getClass().getResource("/Vista/Reportes.fxml"));
                                    Pane_Base.getChildren().setAll(pane);
                                    Btn_Burger_Menu.setRate(Btn_Burger_Menu.getRate() * -1);
                                    Btn_Burger_Menu.play();
                                    Panel_Drawer.close();
                                    Panel_Drawer.setDisable(true);

                                } catch (IOException ex) {
                                    Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                break;
                        }
                    }
                });
            });

            Btn_Burger_Menu = new HamburgerBackArrowBasicTransition(Menu_Hamburguesa);
            Btn_Burger_Menu.setRate(-1);
            Menu_Hamburguesa.addEventHandler(MouseEvent.MOUSE_PRESSED, (event) -> {

                Btn_Burger_Menu.setRate(Btn_Burger_Menu.getRate() * -1);
                Btn_Burger_Menu.play();
                if (Panel_Drawer.isClosed()) {
                    Panel_Drawer.setDisable(false);
                    Panel_Drawer.open();
                } else {
                    Panel_Drawer.close();
                    Panel_Drawer.setDisable(true);
                }

            });

        } catch (IOException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
