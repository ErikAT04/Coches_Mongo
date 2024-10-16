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
        if (matrTField.getText().isEmpty()){
            AlertUtils.showAlert(Alert.AlertType.ERROR, "El campo de matrícula tiene que tener datos", "Error de campos");
        }else {
            Optional<ButtonType> bt = AlertUtils.showAlert(Alert.AlertType.CONFIRMATION, "¿Quieres borrar este vehículo?", "Borrar coche");
            if (bt.isPresent() && bt.get() == ButtonType.OK) {
                if (cocheCRUD.deleteCar(matrTField.getText())) {
                    AlertUtils.showAlert(Alert.AlertType.INFORMATION, "Coche borrado correctamente", "Información de eliminación de coches");
                    refresh();
                    flush();
                } else {
                    AlertUtils.showAlert(Alert.AlertType.ERROR, "Ha habido un error borrando el coche, comprueba que la matrícula es correcta y que la base de datos está conectada correctamente", "Error de eliminación");
                }
            }
        }
    }

    @FXML
    void onModClick(ActionEvent event) {
        if (markTField.getText().isEmpty() || matrTField.getText().isEmpty() || modelTField.getText().isEmpty() || typeCB.getValue().isEmpty()){
            AlertUtils.showAlert(Alert.AlertType.ERROR, "Alguno de los campos está vacío", "Error de campos");
        } else{
            if (Validator.validatePlate(matrTField.getText())){
                Coche oldCar = cocheCRUD.getCar(matrTField.getText());
                if (oldCar == null){
                    AlertUtils.showAlert(Alert.AlertType.ERROR, "No existe ningún coche con esta matrícula en la base de datos", "Error de búsqueda");
                } else {
                    Coche car = new Coche(matrTField.getText(), markTField.getText(), modelTField.getText(), typeCB.getValue());
                    if (oldCar.equals(car)){
                        AlertUtils.showAlert(Alert.AlertType.ERROR, "¿Por qué querrías actualizar un coche al que no has cambiado nada?", "Error de datos");
                    }else {
                        if (cocheCRUD.updateCar(car)) {
                            AlertUtils.showAlert(Alert.AlertType.INFORMATION, "Coche actualizado correctamente", "Información de coche");
                            refresh();
                        } else {
                            AlertUtils.showAlert(Alert.AlertType.ERROR, "No se pudo actualizar el vehículo, comprueba que la base de datos funciona correctamente", "Error de actualización");
                        }
                    }
                }
            } else {
                AlertUtils.showAlert(Alert.AlertType.ERROR, "La matrícula no cumple el criterio de evaluación", "Error de matrícula");
            }
        }
    }

    @FXML
    void onNewClick(ActionEvent event) {
        if (markTField.getText().isEmpty() || matrTField.getText().isEmpty() || modelTField.getText().isEmpty() || typeCB.getValue().isEmpty()){
            AlertUtils.showAlert(Alert.AlertType.ERROR, "Alguno de los campos está vacío", "Error de campos");
        } else{
            if (Validator.validatePlate(matrTField.getText())){
                if (cocheCRUD.getCar(matrTField.getText())!=null){
                    AlertUtils.showAlert(Alert.AlertType.ERROR, "Ya hay un coche en la base de datos con esa matrícula", "Error de adición");
                } else {
                    Coche car = new Coche(matrTField.getText(), markTField.getText(), modelTField.getText(), typeCB.getValue());
                    if (cocheCRUD.insertCar(car)){
                        AlertUtils.showAlert(Alert.AlertType.INFORMATION, "Coche añadido correctamente", "Información de coche");
                        refresh();
                    } else {
                        AlertUtils.showAlert(Alert.AlertType.ERROR, "No se pudo añadir el vehículo, comprueba que la base de datos funciona correctamente", "Error de adición");
                    }
                }
            } else {
                AlertUtils.showAlert(Alert.AlertType.ERROR, "La matrícula no cumple el criterio de evaluación", "Error de matrícula");
            }
        }
    }

    @FXML
    void onClearClick(ActionEvent event) {
        flush();
    }

    @FXML
    void onContentClickInTable(MouseEvent event) {
        showItems(carTView.getSelectionModel().getSelectedItem());
    }

    public void onContentClickInList(MouseEvent mouseEvent) {
        showItems(carListView.getSelectionModel().getSelectedItem());
    }

    public void showItems(Coche coche){
        if (coche!=null){
            markTField.setText(coche.getMarca());
            matrTField.setText(coche.getMatricula());
            modelTField.setText(coche.getModelo());
            typeCB.setValue(coche.getTipo());
        }
    }
    public void onChangeClick(ActionEvent actionEvent) {
        if(carListView.isVisible()){
            carListView.setVisible(false);
            carTView.setVisible(true);
            changeViewBtt.setText("Cambiar tabla a lista");
        } else {
            carListView.setVisible(true);
            carTView.setVisible(false);
            changeViewBtt.setText("Cambiar lista a tabla");
        }
    }
    public void refresh() {
        ArrayList<Coche> coches = cocheCRUD.listCars();
        ObservableList<Coche> cocheObservableList = FXCollections.observableArrayList(coches);
        carTView.setItems(cocheObservableList);
        carListView.setItems(cocheObservableList);
    }
    public void flush(){
        modelTField.setText("");
        matrTField.setText("");
        markTField.setText("");
        typeCB.setValue(null);
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            cocheCRUD = new CRUDCoche();
            //JavaFX: Tabla y Lista
            matrTCol.setCellValueFactory(new PropertyValueFactory<>("matricula"));
            markTCol.setCellValueFactory(new PropertyValueFactory<>("marca"));
            typeTCol.setCellValueFactory(new PropertyValueFactory<>("tipo"));
            modTCol.setCellValueFactory(new PropertyValueFactory<>("modelo"));
            refresh();

            //JavaFX: ComboBox
            ArrayList<String> listTypes = new ArrayList<>(List.of(new String[]{"Diesel", "Gasolina", "Hibrido", "Electrico"}));
            typeCB.setItems(FXCollections.observableArrayList(listTypes));

        }catch (Exception e){
            System.out.println("Error de programa");
        }
    }
}