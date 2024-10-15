package com.erikat.cochesmongodb.Scenes;

import com.erikat.cochesmongodb.Obj.Coche;
import com.erikat.cochesmongodb.Utils.MongoDBManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.bson.Document;
import com.google.gson.Gson;


import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import org.bson.Document;

public class MainController implements Initializable {
    MongoClient con;
    MongoDatabase database;
    Gson gson;
    MongoCollection<Document> collection;

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

    }

    @FXML
    void onModClick(ActionEvent event) {

    }

    @FXML
    void onNewClick(ActionEvent event) {

    }

    @FXML
    void onSaveClick(ActionEvent event) {

    }

    @FXML
    void onSearchClick(ActionEvent event) {

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
        MongoCursor<Document> cursor = collection.find().iterator();
        ArrayList<Coche> coches = new ArrayList<>();
        try {
            while (cursor.hasNext()){
                String json = cursor.next().toJson();
                Coche coche = gson.fromJson(json, Coche.class);
                coches.add(coche);
            }
        }catch (Exception e){
            System.out.println("Error de programa");
        } finally {
            cursor.close();
        }
        ObservableList<Coche> cocheObservableList = FXCollections.observableArrayList(coches);
        carTView.setItems(cocheObservableList);
        carListView.setItems(cocheObservableList);
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            //MongoDB
            con = MongoDBManager.conectar();
            gson = new Gson();
            database = con.getDatabase("coches");
            collection = database.getCollection("coches");

            //JavaFX
            matrTCol.setCellValueFactory(new PropertyValueFactory<>("matricula"));
            markTCol.setCellValueFactory(new PropertyValueFactory<>("marca"));
            typeTCol.setCellValueFactory(new PropertyValueFactory<>("tipo"));
            modTCol.setCellValueFactory(new PropertyValueFactory<>("modelo"));
            refresh();
        }catch (Exception e){
            System.out.println("Error de programa");
        }
    }
}