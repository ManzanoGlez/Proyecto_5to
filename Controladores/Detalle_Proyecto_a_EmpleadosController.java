package Controladores;

import Conexion.ConexionSQLServer;
import Modelo.Empleado;
import Modelo.Empleado_Proyecto;
import Modelo.Proyecto;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Window;
import javafx.util.Callback;

/*
 * FXML Controller class
 * @author jorge
 */
public class Detalle_Proyecto_a_EmpleadosController implements Initializable {
    
    @FXML
    private AnchorPane Pane;
    
    @FXML
    private JFXTextField Txt_Buscar;
    @FXML
    private JFXButton Btn_Agregar;
    @FXML
    private TextArea Txt_Descripcion_puesto;
    
    @FXML
    private JFXComboBox<String> combo_Empleados;
    @FXML
    private DatePicker Date_Asignacion;
    @FXML
    private JFXComboBox<String> Combo_Buscar;
    @FXML
    private Label Txt_Nombre_Proyecto;
    @FXML
    private Label txt_Codigo;
    @FXML
    private Label txt_Fecha_inicio;
    @FXML
    private Label txt_Fecha_Termino;
    @FXML
    private TableView<Empleado_Proyecto> Tabla;
    @FXML
    private TableColumn<Empleado_Proyecto, String> Col_1;
    @FXML
    private TableColumn<Empleado_Proyecto, String> Col_2;
    @FXML
    private TableColumn<Empleado_Proyecto, String> Col_3;
    
    ConexionSQLServer SQL = new ConexionSQLServer();
    FilteredList<Empleado_Proyecto> Lista = new FilteredList<>(Cargar_Tabla(), p -> true);
    Empleado_Proyecto objeto;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Cargar_Combox();
        Cargar_Datos(Proyecto.ID_Modificar);
        Inicializar_Tabla();
    }

//Carga datos en caso de recibir un id 
    public void Cargar_Datos(int id) {
        
        try {
            String Query;
            PreparedStatement ps;
            ResultSet rs;
            Query = Proyecto.Query_Select_Detalle_Where;
            ps = SQL.Conectar().prepareStatement(Query);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            while (rs.next()) {
                Txt_Nombre_Proyecto.setText(rs.getString(1));
                txt_Codigo.setText(rs.getString(2));
                txt_Fecha_inicio.setText(rs.getString(3));
                txt_Fecha_Termino.setText(rs.getString(4));
            }
            
        } catch (SQLException e) {
            
            System.out.println("Error en Cargar Datos empleado :" + e.getMessage());
        }
        
    }

    //Inicializa la tabla
    public void Inicializar_Tabla() {
        
        Col_1.setCellValueFactory(new PropertyValueFactory<>("Empleado"));
        Col_2.setCellValueFactory(new PropertyValueFactory<>("Fecha_asignacion"));
        Col_3.setCellValueFactory(new PropertyValueFactory<>("Descripcion"));
        
        Col_Eliminar();
        Col_Editar();
        Col_Ver();
        Tabla.setItems(Lista);
        Buscar();
        
    }
    
    @FXML
    void Guardar(ActionEvent event) {
        
        if (!Validar_Campos()) {
            
            try {
                
                PreparedStatement ps;
                
                String Query = Empleado_Proyecto.Query_Insert_Update;
                
                System.out.println("Nombre empleado " + combo_Empleados.getValue());
                
                ps = SQL.Conectar().prepareStatement(Query);
                ps.setInt(1, Proyecto.ID_Modificar);
                ps.setInt(2, ObtenerID_Empleado(combo_Empleados.getValue()));
                ps.setString(3, Date_Asignacion.getValue().toString());
                ps.setString(4, Txt_Descripcion_puesto.getText());
                ps.executeUpdate();

                //Actualizar Tabla
                Tabla.getItems().removeAll(Cargar_Tabla());
                Mostrar_Alerta(Alert.AlertType.INFORMATION, Pane.getScene().getWindow(),
                        "Exito!", "Actualización guardado exitosa mente");
                Tabla.setItems(Cargar_Tabla());
                Txt_Descripcion_puesto.setText("");
                combo_Empleados.setValue("Seleccióna..");
                Date_Asignacion.setValue(null);
                
            } catch (SQLException ex) {
                System.out.println("Error al guardar empleado proyecto :" + ex.getMessage());
            }
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
                    
                    case "Empleado":
                        Buscar = obj.getEmpleado().toLowerCase();
                        break;
                    case "Fecha asignación":
                        Buscar = obj.getFecha_asignacion().toLowerCase();
                        break;
                    case "Descripción":
                        Buscar = obj.getDescripcion().toLowerCase();
                        break;
                }
                return Buscar.contains(text.toLowerCase());
            });
        });
        
    }

    //Cargar Empleado_Proyecto
    public ObservableList<Empleado_Proyecto> Cargar_Tabla() {
        String Query = Empleado_Proyecto.Query_Tabla;
        List<Empleado_Proyecto> List = new ArrayList<>();
        PreparedStatement ps;
        ResultSet rs;
        try {
            ps = SQL.Conectar().prepareStatement(Query);
            ps.setInt(1, Proyecto.ID_Modificar);
            
            rs = ps.executeQuery();
            
            while (rs.next()) {
                List.add(new Empleado_Proyecto(rs.getString(1), rs.getString(2), rs.getString(3)));
            }
            
        } catch (SQLException e) {
            System.out.println("Error en Cargar Tabla Empleados :" + e.getMessage());
        }
        
        return observableArrayList(List);
        
    }

    //Columna Eliminar
    private void Col_Eliminar() {
        
        TableColumn<Empleado_Proyecto, Void> colBtn = new TableColumn("Eliminar");
        
        Callback<TableColumn<Empleado_Proyecto, Void>, TableCell<Empleado_Proyecto, Void>> cellFactory = (final TableColumn<Empleado_Proyecto, Void> param) -> {
            final TableCell<Empleado_Proyecto, Void> cell = new TableCell<Empleado_Proyecto, Void>() {
                
                private final Button Btn_Columna = new Button("Eliminar");
                
                {
                    //Eliminar
                    Btn_Columna.setOnAction((ActionEvent event) -> {
                        objeto = getTableView().getItems().get(getIndex());
                        
                        String nombreEmpleado = objeto.getEmpleado();
                        System.out.println("Seleccionado: " + nombreEmpleado);
                        
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Confirmar Quitar Asignación");
                        alert.setHeaderText("Quitar asignacion a proyecto");
                        alert.setContentText("¿Estas Seguro de hacer esto?");
                        
                        Optional<ButtonType> result = alert.showAndWait();
                        if (result.get() == ButtonType.OK) {
                            try {
                                
                                PreparedStatement ps;
                                String Query = Empleado_Proyecto.Query_Delete;
                                
                                ps = SQL.Conectar().prepareStatement(Query);
                                ps.setInt(1, Proyecto.ID_Modificar);
                                ps.setInt(2, ObtenerID_Empleado(nombreEmpleado));
                                ps.executeUpdate();

                                //Actualizar Tabla
                                Tabla.getItems().removeAll(Cargar_Tabla());
                                Mostrar_Alerta(Alert.AlertType.INFORMATION, Pane.getScene().getWindow(),
                                        "Exito!", "Eliminado exitosa mente");
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

    //Columna Ver mas
    private void Col_Ver() {
        TableColumn<Empleado_Proyecto, Void> colBtn = new TableColumn("Ver");
        
        Callback<TableColumn<Empleado_Proyecto, Void>, TableCell<Empleado_Proyecto, Void>> cellFactory = (final TableColumn<Empleado_Proyecto, Void> param) -> {
            final TableCell<Empleado_Proyecto, Void> cell = new TableCell<Empleado_Proyecto, Void>() {
                
                private final Button Btn_Columna = new Button("Ver");
                
                {
                    //Modificar
                    Btn_Columna.setOnAction((ActionEvent e) -> {
                        
                        objeto = getTableView().getItems().get(getIndex());
                        String nombreEmpleado = objeto.getEmpleado();
                        
                        Empleado.ID_Modificar = ObtenerID_Empleado(nombreEmpleado);

                        //Abre
                        try {
                            AnchorPane pane = FXMLLoader.load(getClass().getResource("/Vista/Alta_Modificar_Empleado.fxml"));
                            Pane.getChildren().setAll(pane);
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

    //Columna Editrar
    private void Col_Editar() {
        TableColumn<Empleado_Proyecto, Void> colBtn = new TableColumn("Editar");
        
        Callback<TableColumn<Empleado_Proyecto, Void>, TableCell<Empleado_Proyecto, Void>> cellFactory = (final TableColumn<Empleado_Proyecto, Void> param) -> {
            final TableCell<Empleado_Proyecto, Void> cell = new TableCell<Empleado_Proyecto, Void>() {
                
                private final Button Btn_Columna = new Button("Editar");
                
                {
                    //Modificar
                    Btn_Columna.setOnAction((ActionEvent e) -> {
                        
                        objeto = getTableView().getItems().get(getIndex());
                        String nombreEmpleado = objeto.getEmpleado();
                        
                        try {
                            DateTimeFormatter Formato = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                            LocalDate fecha;
                            PreparedStatement ps;
                            ResultSet rs;
                            String Query = Empleado_Proyecto.Query_Select_Where;
                            
                            ps = SQL.Conectar().prepareStatement(Query);
                            ps.setInt(1, ObtenerID_Empleado(nombreEmpleado));
                            rs = ps.executeQuery();
                            
                            while (rs.next()) {
                                combo_Empleados.setValue(nombreEmpleado);
                                fecha = LocalDate.parse(rs.getString(1), Formato);
                                Date_Asignacion.setValue(fecha);
                                Txt_Descripcion_puesto.setText(rs.getString(2));
                            }
                            
                        } catch (SQLException ex) {
                            System.out.println("Error al eliminar :" + ex.getMessage());
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

    ///Llenar Comboboxs
    public void Cargar_Combox() {
        
        Combo_Buscar.getItems().add("Empleado");
        Combo_Buscar.getItems().add("Fecha asignación");
        Combo_Buscar.getItems().add("Descripción");
        Combo_Buscar.setValue("Empleado");
        
        String Query;
        PreparedStatement ps;
        ResultSet rs;
        
        try {
            Query = Empleado.Query_Combo_Vendedores;
            ps = SQL.Conectar().prepareStatement(Query);
            rs = ps.executeQuery();
            combo_Empleados.getItems().add("Seleccióna..");
            while (rs.next()) {
                combo_Empleados.getItems().add(rs.getString(1));
            }
            combo_Empleados.setValue("Seleccióna..");
        } catch (SQLException e) {
            System.out.println("Error al llenar combobox :" + e.getMessage());
        }
    }
    
    @FXML
    public void Regresar() {
        try {
            AnchorPane pane = FXMLLoader.load(getClass().getResource("/Vista/Alta_Modificar_Proyecto.fxml"));
            Pane.getChildren().setAll(pane);
        } catch (IOException ex) {
            Logger.getLogger(EmpleadosController.class.getName()).log(Level.SEVERE, null, ex);
        }
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

    // obtiene el id  empleado
    public int ObtenerID_Empleado(String Empleado) {
        int id = 0;
        try {
            
            String Query;
            PreparedStatement ps;
            ResultSet rs;
            
            String partes[] = Empleado.split(" ");
            String nombres = partes[0];
            String Apellidos = partes[1];
            
            Query = "SELECT ID_Empleado FROM EMPLEADO P WHERE Nombres = ? AND Apellidos = ?";
            ps = SQL.Conectar().prepareStatement(Query);
            ps.setString(1, nombres);
            ps.setString(2, Apellidos);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                id = rs.getInt(1);
            }
            
            System.out.println("id vendedor :" + id);
            
        } catch (SQLException e) {
            System.out.println("Error en ObtenerVenderdor:" + e.getMessage());
            
        }
        return id;
    }

    // obtiene el id  empleado
    public int ObtenerID_Proyecto(String Proyecto) {
        int id = 0;
        try {
            
            String Query;
            PreparedStatement ps;
            ResultSet rs;
            
            String partes[] = Proyecto.split(" ");
            
            String Nom_Proyecto = partes[0];
            String Codigo = partes[1];
            
            Query = "SELECT ID_Proyecto FROM PROYECTO P WHERE Nombre_Proyecto =  ? AND Codigo = ?";
            ps = SQL.Conectar().prepareStatement(Query);
            ps.setString(1, Nom_Proyecto);
            ps.setString(2, Codigo);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                id = rs.getInt(1);
            }
            
            System.out.println("id vendedor :" + id);
            
        } catch (SQLException e) {
            System.out.println("Error en ObtenerVenderdor:" + e.getMessage());
            
        }
        return id;
    }

    //Valida campos 
    public boolean Validar_Campos() {
        
        boolean Error = false;
        
        if (Date_Asignacion.getValue() == null) {
            Mostrar_Alerta(Alert.AlertType.ERROR, Pane.getScene().getWindow(),
                    "Error!", "Por favor inserta la fecha");
            Error = true;
            return Error;
        }
        
        if (Txt_Descripcion_puesto.getText().isEmpty()) {
            Mostrar_Alerta(Alert.AlertType.ERROR, Pane.getScene().getWindow(),
                    "Error!", "Por favor inserta una descripción");
            Error = true;
            return Error;
        }
        
        if (combo_Empleados.getValue().equals("Seleccióna..")) {
            Mostrar_Alerta(Alert.AlertType.ERROR, Pane.getScene().getWindow(),
                    "Error!", "Por favor seleccióna un empleado");
            Error = true;
            return Error;
        }
        
        return Error;
        
    }
    
}
