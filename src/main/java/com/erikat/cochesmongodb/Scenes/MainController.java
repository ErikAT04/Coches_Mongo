package com.erikat.cochesmongodb.Scenes;

import com.erikat.cochesmongodb.Obj.CRUDCoche;
import com.erikat.cochesmongodb.Obj.Coche;
import com.erikat.cochesmongodb.Utils.AlertUtils;
import com.erikat.cochesmongodb.Utils.Validator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;


import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

public class MainController implements Initializable {

    CRUDCoche cocheCRUD;

    @FXML
    public Button changeViewBtt;
    @FXML
    public ListView<Coche> carListView;

    @FXML
    private TableView<Coche> carTView;

    @FXML
    private TableColumn<Coche, String> markTCol;

    @FXML
    private TextField markTField;

    @FXML
    private TableColumn<Coche, String> matrTCol;

    @FXML
    private TextField matrTField;

    @FXML
    private TableColumn<Coche, String> modTCol;

    @FXML
    private TextField modelTField;

    @FXML
    private ComboBox<String> typeCB;

    @FXML
    private TableColumn<Coche, String> typeTCol;

    @FXML
    void onDeleteClick(ActionEvent event) {
        try {
            //Para borrar, tiene que tener en cuenta que la matrícula existe y que aparece dentro de la base de datos.
            if (matrTField.getText().isEmpty()) { //Si el campo de la matrícula está vacío:
                AlertUtils.showAlert(Alert.AlertType.ERROR, "El campo de matrícula tiene que tener datos", "Error de campos");
            } else {
                Optional<ButtonType> bt = AlertUtils.showAlert(Alert.AlertType.CONFIRMATION, "¿Quieres borrar este vehículo?", "Borrar coche");
                if (bt.isPresent() && bt.get() == ButtonType.OK) { //Pide confirmación al usuario, y si este la acepta, sigue con el borrado
                    if (cocheCRUD.borrarCoche(matrTField.getText())) { //Si la operación ha salido bien
                        AlertUtils.showAlert(Alert.AlertType.INFORMATION, "Coche borrado correctamente", "Información de eliminación de coches");
                        refresh(); //Refresca la tabla y de paso borra la información de los campos (se presupone que no los va a necesitar)
                        flush();
                    } else { //Si la operación no sale bien: (Puede ser por un error con la conexión o porque la matrícula no coincide con ninguna de la base)
                        AlertUtils.showAlert(Alert.AlertType.ERROR, "Ha habido un error borrando el coche, comprueba que la matrícula es correcta y que la base de datos está conectada correctamente", "Error de eliminación");
                    }
                }
            }
        }catch (Exception e){
            AlertUtils.showAlert(Alert.AlertType.ERROR, "La base de datos tiene un problema", "Error de base de datos");
        }
    }

    @FXML
    void onModClick(ActionEvent event) {
        /*
        Criterios a tener en cuenta:
            1. Todos los campos están rellenados correctamente
            2. La matrícula cumple los requisitos oportunos (NNNNXXX ó Y[Y]NNNNXX, donde N son numeros, X son consonantes e Y son cualquier tipo de letra)
            3. La matrícula tiene que aparecer en la base de datos
            4. Para que se edite, el coche tiene que haber cambiado, como mínimo, un parámetro
         */
        try{
            if (markTField.getText().isEmpty() || matrTField.getText().isEmpty() || modelTField.getText().isEmpty() || typeCB.getValue().isEmpty()){ //Si alguno de los campos está vacío:
                AlertUtils.showAlert(Alert.AlertType.ERROR, "Alguno de los campos está vacío", "Error de campos");
            } else{
                if (Validator.validatePlate(matrTField.getText())){ //Si la matrícula no cumple los criterios de validación
                    Coche oldCar = cocheCRUD.getCoche(matrTField.getText()); //Coge el coche de la base de datos para comprobar si es o no nulo
                    if (oldCar == null){ //Si es nulo (No existe en la base de datos)
                        AlertUtils.showAlert(Alert.AlertType.ERROR, "No existe ningún coche con esta matrícula en la base de datos", "Error de búsqueda");
                    } else { //Si existe en la base de datos
                        Coche car = new Coche(matrTField.getText(), markTField.getText(), modelTField.getText(), typeCB.getValue()); //Crea un objeto con los datos introducidos
                        if (oldCar.equals(car)){ //Si todos los atributos de ambos objetos son iguales:
                            AlertUtils.showAlert(Alert.AlertType.ERROR, "¿Por qué querrías actualizar un coche al que no has cambiado nada?", "Error de datos");
                        }else { //Si alguno de los atributos es distinto, procede con la modificación
                            if (cocheCRUD.actualizarCoche(car)) { //Si la operación ha salido correctamente:
                                AlertUtils.showAlert(Alert.AlertType.INFORMATION, "Coche actualizado correctamente", "Información de coche");
                                refresh(); //Refresca la tabla
                            } else { //Si no ha salido bien (Por un error de conexión a la base de datos)
                                AlertUtils.showAlert(Alert.AlertType.ERROR, "No se pudo actualizar el vehículo, comprueba que la base de datos funciona correctamente", "Error de actualización");
                            }
                        }
                    }
                } else { //Si la matrícula no cumple el criterio
                    AlertUtils.showAlert(Alert.AlertType.ERROR, "La matrícula no cumple el criterio de validación", "Error de matrícula");
                }
            }
        }catch (Exception e){
            AlertUtils.showAlert(Alert.AlertType.ERROR, "La base de datos tiene un problema", "Error de base de datos");
        }
    }

    @FXML
    void onNewClick(ActionEvent event) { //Función programada al dar al botón "nuevo"
        /*
        Criterios a tener en cuenta:
            1. Todos los campos están rellenados correctamente
            2. La matrícula cumple los requisitos oportunos (NNNNXXX ó Y[Y]NNNNXX, donde N son numeros, X son consonantes e Y son cualquier tipo de letra)
            3. No haya un coche ya con esa matrícula en la base de datos
         */
        try {
            if (markTField.getText().isEmpty() || matrTField.getText().isEmpty() || modelTField.getText().isEmpty() || typeCB.getValue().isEmpty()) { //Si alguno de los campos está vacío
                AlertUtils.showAlert(Alert.AlertType.ERROR, "Alguno de los campos está vacío", "Error de campos");
            } else {
                if (Validator.validatePlate(matrTField.getText())) { //Si la matrícula cumple los criterios de validación continúa con el proceso
                    if (cocheCRUD.getCoche(matrTField.getText()) != null) { //Busca y coge un objeto de tipo coche de la base de datos con la matrícula dada. Si no es nulo (Aparece en la base de datos), salta un error
                        AlertUtils.showAlert(Alert.AlertType.ERROR, "Ya hay un coche en la base de datos con esa matrícula", "Error de adición");
                    } else { //Si la búsqueda no encuentra esa matrícula (El coche es nulo)
                        Coche car = new Coche(matrTField.getText(), markTField.getText(), modelTField.getText(), typeCB.getValue()); //Crea un nuevo objeto de tipo coche
                        if (cocheCRUD.insertarCoche(car)) { //Si lo inserta correctamente en la base
                            AlertUtils.showAlert(Alert.AlertType.INFORMATION, "Coche añadido correctamente", "Información de coche");
                            refresh(); //Refresca las tablas
                        } else { //Si no se inserta correctamente (Debido a un error en la base de datos)
                            AlertUtils.showAlert(Alert.AlertType.ERROR, "No se pudo añadir el vehículo, comprueba que la base de datos funciona correctamente", "Error de adición");
                        }
                    }
                } else { //Si la matrícula no cumple la validación:
                    AlertUtils.showAlert(Alert.AlertType.ERROR, "La matrícula no cumple el criterio de validación", "Error de matrícula");
                }
            }
        }catch (Exception e){
            AlertUtils.showAlert(Alert.AlertType.ERROR, "La base de datos tiene un problema", "Error de base de datos");
        }
    }

    @FXML
    void onClearClick(ActionEvent event) {
        flush(); //Limpia los datos al dar click al botón de limpiar
    }

    @FXML
    void onContentClickInTable(MouseEvent event) {
        showItems(carTView.getSelectionModel().getSelectedItem()); //Muestra los datos del objeto seleccionado en la lista
    }

    public void onContentClickInList(MouseEvent mouseEvent) {
        showItems(carListView.getSelectionModel().getSelectedItem()); //Muestra los datos del objeto seleccionado en la lista
    }

    public void showItems(Coche coche){ //Función llamada por la ListView y TableView para cambiar los datos de los campos
        if (coche!=null){ //Si el coche recibido no es nulo (Se ha seleccionado algún campo, se inserta cada dato en su campo correspondiente
            markTField.setText(coche.getMarca());
            matrTField.setText(coche.getMatricula());
            modelTField.setText(coche.getModelo());
            typeCB.setValue(coche.getTipo());
        }
    }
    public void onChangeClick(ActionEvent actionEvent) { //Función que se activa al dar al botón de cambiar tabla/lista
        if(carListView.isVisible()){ //Si la lista es visible (es decir, la tabla no lo es):
            carListView.setVisible(false); //La lista desaparece
            carTView.setVisible(true); //Aparece la tabla
            changeViewBtt.setText("Cambiar tabla a lista"); //Cambia el texto
        } else { //Si la lista no se ve, sucede exactamente lo opuesto
            carListView.setVisible(true);
            carTView.setVisible(false);
            changeViewBtt.setText("Cambiar lista a tabla");
        }
    }
    public void refresh() { //Función que refresca los datos de las tablas
        ArrayList<Coche> coches = cocheCRUD.listarCoches(); //Obtiene la lista de los coches
        ObservableList<Coche> cocheObservableList = FXCollections.observableArrayList(coches); //Se convierte en una lista observable de JavaFX
        //Se inserta en las dos tablas
        carTView.setItems(cocheObservableList);
        carListView.setItems(cocheObservableList);
    }
    public void flush(){ //Función encargada de borrar todos los datos de los campos
        //Cambia el texto de los campos de texto a un string vacío
        modelTField.setText("");
        matrTField.setText("");
        markTField.setText("");
        typeCB.setValue(null); //Quita el valor del combobox
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            cocheCRUD = new CRUDCoche(); //Inicializo el objeto CRUD de coche
            //JavaFX: Tabla y Lista
            //Comienzo dando los tipos de valor que va a recibir la tabla.
            matrTCol.setCellValueFactory(new PropertyValueFactory<>("matricula"));
            markTCol.setCellValueFactory(new PropertyValueFactory<>("marca"));
            typeTCol.setCellValueFactory(new PropertyValueFactory<>("tipo"));
            modTCol.setCellValueFactory(new PropertyValueFactory<>("modelo"));
            refresh(); //Refresco ambas tablas por primera vez

            //JavaFX: ComboBox
            ArrayList<String> listTypes = new ArrayList<>(List.of(new String[]{"Diesel", "Gasolina", "Hibrido", "Electrico"})); //Le doy distintas opciones
            typeCB.setItems(FXCollections.observableArrayList(listTypes)); //Meto las opciones en el combobox

        }catch (Exception e){ //Fallo en la base de datos
            System.out.println("Error de programa");
        }
    }
}