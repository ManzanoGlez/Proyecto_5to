package Controladores;

import Conexion.ConexionSQLServer;
import Modelo.Empleado;
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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Window;
import javafx.util.Callback;

/* FXML Controller class
 * @author Jorge Manzano
 */
public class EmpleadosController implements Initializable {

    ConexionSQLServer SQL = new ConexionSQLServer();
    FilteredList<Empleado> Lista = new FilteredList<>(Cargar_Tabla(), p -> true);
    Empleado empleado;

    @FXML
    private AnchorPane Panel_Empleados;
    @FXML
    private TableView<Empleado> Tabla;
    @FXML
    private TableColumn<Empleado, String> Col_ID;
    @FXML
    private TableColumn<Empleado, String> Col_Nombre;
    @FXML
    private TableColumn<Empleado, String> Col_Correo;
    @FXML
    private TableColumn<Empleado, String> Col_Telefono;
    @FXML
    private TableColumn<Empleado, String> Col_Puesto;
    @FXML
    private TableColumn<Empleado, String> Col_Area;
    @FXML
    private JFXTextField Txt_Buscar;
    @FXML
    private JFXComboBox<String> Combo_Buscar;

    @FXML
    public void Nuevo() {
        try {
            Empleado.ID_Modificar = 0;
            AnchorPane pane = FXMLLoader.load(getClass().getResource("/Vista/Alta_Modificar_Empleado.fxml"));
            Panel_Empleados.getChildren().setAll(pane);

        } catch (IOException ex) {
            Logger.getLogger(EmpleadosController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //Buscar empleados
    public void Buscar() {

        Txt_Buscar.setPromptText("Buscar por ...");
        Txt_Buscar.textProperty().addListener((prop, old, text) -> {
            Lista.setPredicate(Empleado -> {
                if (text == null || text.isEmpty()) {
                    return true;
                }

                String Buscar = "";

                switch (Combo_Buscar.getValue()) {

                    case "Nombre":
                        Buscar = Empleado.getNombres().toLowerCase();
                        break;
                    case "Correo":
                        Buscar = Empleado.getCorreo().toLowerCase();
                        break;
                    case "Teléfono":
                        Buscar = Empleado.getTelefono().toLowerCase();
                        break;
                    case "Puesto":
                        Buscar = Empleado.getPuesto().toLowerCase();
                        break;
                    case "Area":
                        Buscar = Empleado.getArea().toLowerCase();
                        break;
                }
                return Buscar.contains(text.toLowerCase());
            });
        });

    }

    ///Llenar Combobox
    public void Cargar_Combo_Buscar() {
        Combo_Buscar.getItems().add("Nombre");
        Combo_Buscar.getItems().add("Correo");
        Combo_Buscar.getItems().add("Teléfono");
        Combo_Buscar.getItems().add("Puesto");
        Combo_Buscar.getItems().add("Area");
        Combo_Buscar.setValue("Nombre");
    }

    //Inicializa la tabla
    public void Inicializar_Tabla() {
        Col_ID.setCellValueFactory(new PropertyValueFactory<>("ID_Empleado"));
        Col_Nombre.setCellValueFactory(new PropertyValueFactory<>("Nombres"));
        Col_Correo.setCellValueFactory(new PropertyValueFactory<>("Correo"));
        Col_Telefono.setCellValueFactory(new PropertyValueFactory<>("Telefono"));
        Col_Puesto.setCellValueFactory(new PropertyValueFactory<>("Puesto"));
        Col_Area.setCellValueFactory(new PropertyValueFactory<>("Area"));
        Col_Eliminar();
        Col_Ver();
        Tabla.setItems(Lista);
        Buscar();

    }

    //Cargar Empleados
    public ObservableList<Empleado> Cargar_Tabla() {

        List<Empleado> Empleados = new ArrayList<>();
        PreparedStatement ps;
        ResultSet rs;
        try {
            
            String Query = Empleado.Query_Tabla;

            ps = SQL.Conectar().prepareStatement(Query);

            rs = ps.executeQuery();

            while (rs.next()) {
                Empleados.add(new Empleado(rs.getInt(1),
                        rs.getString(2) + " " + rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getString(6),
                        rs.getString(7)
                ));
            }

        } catch (SQLException e) {
            System.out.println("Error en Cargar Tabla Empleados :" + e.getMessage());
        }

        return observableArrayList(Empleados);

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
        TableColumn<Empleado, Void> colBtn = new TableColumn("Eliminar");

        Callback<TableColumn<Empleado, Void>, TableCell<Empleado, Void>> cellFactory = (final TableColumn<Empleado, Void> param) -> {
            final TableCell<Empleado, Void> cell = new TableCell<Empleado, Void>() {

                private final Button Btn_Columna = new Button("Eliminar");
                {
                    //Eliminar
                    Btn_Columna.setOnAction((ActionEvent event) -> {
                        empleado = getTableView().getItems().get(getIndex());

                        int ID = empleado.getID_Empleado();
                        System.out.println("Seleccionado: " + empleado.getID_Empleado());

                        Alert alert = new Alert(AlertType.CONFIRMATION);
                        alert.setTitle("Confirmar eliminar empleado");
                        alert.setHeaderText("Eliminar empleado : " + empleado.getNombres());
                        alert.setContentText("¿Estas Seguro de hacer esto?");

                        Optional<ButtonType> result = alert.showAndWait();
                        if (result.get() == ButtonType.OK) {
                            try {

                                PreparedStatement ps;
                                String Query = Empleado.Query_Delete;

                                ps = SQL.Conectar().prepareStatement(Query);
                                ps.setInt(1, ID);
                                ps.executeUpdate();

                                //Actualizar Tabla
                                Tabla.getItems().removeAll(Cargar_Tabla());
                                Mostrar_Alerta(Alert.AlertType.INFORMATION, Panel_Empleados.getScene().getWindow(),
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
        TableColumn<Empleado, Void> colBtn = new TableColumn("Ver");

        Callback<TableColumn<Empleado, Void>, TableCell<Empleado, Void>> cellFactory = (final TableColumn<Empleado, Void> param) -> {
            final TableCell<Empleado, Void> cell = new TableCell<Empleado, Void>() {

                private final Button Btn_Columna = new Button("Ver");

                {
                    //Modificar
                    Btn_Columna.setOnAction((ActionEvent e) -> {

                        empleado = getTableView().getItems().get(getIndex());
                        Empleado.ID_Modificar = empleado.getID_Empleado();

                        //Abre
                        try {
                            AnchorPane pane = FXMLLoader.load(getClass().getResource("/Vista/Alta_Modificar_Empleado.fxml"));
                            Panel_Empleados.getChildren().setAll(pane);
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
