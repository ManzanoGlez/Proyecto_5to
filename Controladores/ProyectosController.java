package Controladores;

import Conexion.ConexionSQLServer;
import Modelo.Proyecto;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javafx.collections.FXCollections.observableArrayList;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Window;
import javafx.util.Callback;

/*
 * FXML Controller class
 * @author jorge
 */
public class ProyectosController implements Initializable {

    ConexionSQLServer SQL = new ConexionSQLServer();
    FilteredList<Proyecto> Lista = new FilteredList<>(Cargar_Tabla(), p -> true);
    Proyecto objeto;

    @FXML
    private TableColumn<Proyecto, String> Col_0;
    @FXML
    private TableColumn<Proyecto, String> Col_1;
    @FXML
    private TableColumn<Proyecto, String> Col_2;
    @FXML
    private TableColumn<Proyecto, String> Col_3;
    @FXML
    private TableColumn<Proyecto, String> Col_4;
    @FXML
    private TableColumn<Proyecto, String> Col_5;
    @FXML
    private TableColumn<Proyecto, String> Col_6;
    @FXML
    private TableView<Proyecto> Tabla;
    @FXML
    private JFXTextField Txt_Buscar;
    @FXML
    private JFXComboBox<String> Combo_Buscar;
    @FXML
    private AnchorPane Pane_Proyectos;

    @FXML
    public void Nuevo() {
        try {
            Proyecto.ID_Modificar = 0;
            AnchorPane pane = FXMLLoader.load(getClass().getResource("/Vista/Alta_Modificar_Proyecto.fxml"));
            Pane_Proyectos.getChildren().setAll(pane);

        } catch (IOException ex) {
            Logger.getLogger(EmpleadosController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //Buscar empleados
    public void Buscar() {

        Txt_Buscar.setPromptText("Buscar por ...");
        Txt_Buscar.textProperty().addListener((prop, old, text) -> {
            Lista.setPredicate(obj -> {
                if (text == null || text.isEmpty()) {
                    return true;
                }

                String Buscar = "";

                switch (Combo_Buscar.getValue()) {

                    case "Codigo":
                        Buscar = obj.getCodigo().toLowerCase();
                        break;
                    case "Proyecto":
                        Buscar = obj.getNombre_Proyecto().toLowerCase();
                        break;
                    case "Cliente":
                        Buscar = obj.getCliente().toLowerCase();
                        break;
                    case "Vendedor":
                        Buscar = obj.getVendedor().toLowerCase();
                        break;
                    case "Fecha inicio":
                        Buscar = obj.getFecha_Inicio().toLowerCase();
                        break;
                    case "Estatus":
                        Buscar = obj.getEstatus().toLowerCase();
                        break;
                }
                return Buscar.contains(text.toLowerCase());
            });
        });

    }

    ///Llenar Combobox
    public void Cargar_Combo_Buscar() {
        Combo_Buscar.getItems().add("Codigo");
        Combo_Buscar.getItems().add("Proyecto");
        Combo_Buscar.getItems().add("Cliente");
        Combo_Buscar.getItems().add("Vendedor");
        Combo_Buscar.getItems().add("Fecha inicio");
        Combo_Buscar.getItems().add("Estatus");
        Combo_Buscar.setValue("Codigo");
    }

    //Inicializa la tabla
    public void Inicializar_Tabla() {
        Col_0.setCellValueFactory(new PropertyValueFactory<>("ID_Proyecto"));
        Col_1.setCellValueFactory(new PropertyValueFactory<>("Codigo"));
        Col_2.setCellValueFactory(new PropertyValueFactory<>("Nombre_Proyecto"));
        Col_3.setCellValueFactory(new PropertyValueFactory<>("Cliente"));
        Col_4.setCellValueFactory(new PropertyValueFactory<>("Vendedor"));
        Col_5.setCellValueFactory(new PropertyValueFactory<>("Fecha_Inicio"));
        Col_6.setCellValueFactory(new PropertyValueFactory<>("Estatus"));
        Col_Eliminar();
        Col_Ver();
        Tabla.setItems(Lista);
        Buscar();

    }

    //Cargar Empleados
    public ObservableList<Proyecto> Cargar_Tabla() {
        String Query = Proyecto.Query_Tabla;
        List<Proyecto> Proyecto = new ArrayList<>();
        PreparedStatement ps;
        ResultSet rs;
        try {
            ps = SQL.Conectar().prepareStatement(Query);
            rs = ps.executeQuery();

            while (rs.next()) {
                Proyecto.add(new Proyecto(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7))
                );
            }

        } catch (SQLException e) {
            System.out.println("Error en Cargar Tabla Empleados :" + e.getMessage());
        }

        return observableArrayList(Proyecto);

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

    //Columna Eliminar
    private void Col_Eliminar() {

        TableColumn<Proyecto, Void> colBtn = new TableColumn("Eliminar");

        Callback<TableColumn<Proyecto, Void>, TableCell<Proyecto, Void>> cellFactory = (final TableColumn<Proyecto, Void> param) -> {
            final TableCell<Proyecto, Void> cell = new TableCell<Proyecto, Void>() {

                private final Button Btn_Columna = new Button("Eliminar");

                {
                    //Eliminar
                    Btn_Columna.setOnAction((ActionEvent event) -> {
                        objeto = getTableView().getItems().get(getIndex());

                        int ID = objeto.getID_Proyecto();
                        System.out.println("Seleccionado: " + objeto.getID_Proyecto());

                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Confirmar eliminar Proyecto");
                        alert.setHeaderText("Eliminar Proyecto : " + objeto.getNombre_Proyecto());
                        alert.setContentText("¿Estas Seguro de hacer esto?");

                        Optional<ButtonType> result = alert.showAndWait();
                        if (result.get() == ButtonType.OK) {
                            try {

                                PreparedStatement ps;
                                String Query = Proyecto.Query_Delete;

                                ps = SQL.Conectar().prepareStatement(Query);
                                ps.setInt(1, ID);
                                ps.executeUpdate();

                                //Actualizar Tabla
                                Tabla.getItems().removeAll(Cargar_Tabla());
                                Mostrar_Alerta(Alert.AlertType.INFORMATION, Pane_Proyectos.getScene().getWindow(),
                                        "Exito!", "Registro guardado exitosa mente");
                                Tabla.setItems(Cargar_Tabla());

                            } catch (SQLException e) {
                                System.out.println("Error al eliminar :" + e.getMessage());
                            }

                        } else {
                            // CANCEL
                        }

                    });
                }

                @Override
                public void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {

                        Btn_Columna.setStyle("-fx-font-weight: bold;-fx-font: 13 System; -fx-base: #FA5858;-fx-text-fill :#ffffff;");
                        setGraphic(Btn_Columna);
                    }
                }
            };
            return cell;
        };

        colBtn.setCellFactory(cellFactory);
        Tabla.getColumns().add(colBtn);
    }

    //Columna Modificar
    private void Col_Ver() {
        TableColumn<Proyecto, Void> colBtn = new TableColumn("Ver");

        Callback<TableColumn<Proyecto, Void>, TableCell<Proyecto, Void>> cellFactory = (final TableColumn<Proyecto, Void> param) -> {
            final TableCell<Proyecto, Void> cell = new TableCell<Proyecto, Void>() {

                private final Button Btn_Columna = new Button("Ver");

                {
                    //Modificar
                    Btn_Columna.setOnAction((ActionEvent e) -> {

                        objeto = getTableView().getItems().get(getIndex());
                        Proyecto.ID_Modificar = objeto.getID_Proyecto();

                        //Abre
                        try {
                            AnchorPane pane = FXMLLoader.load(getClass().getResource("/Vista/Alta_Modificar_Proyecto.fxml"));
                            Pane_Proyectos.getChildren().setAll(pane);
                        } catch (IOException ex) {
                            Logger.getLogger(EmpleadosController.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    });
                }

                @Override
                public void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {//#4675BD
                        Btn_Columna.setStyle("-fx-font-weight: bold;-fx-font: 13 System; -fx-base: #4675BD;-fx-text-fill :#ffffff;");
                        setGraphic(Btn_Columna);
                    }
                }
            };
            return cell;
        };

        colBtn.setCellFactory(cellFactory);
        Tabla.getColumns().add(colBtn);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Inicializar_Tabla();
        Cargar_Combo_Buscar();
    }

}
