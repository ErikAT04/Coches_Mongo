package com.erikat.cochesmongodb.Scenes;

import com.erikat.cochesmongodb.Obj.Coche;
import com.erikat.cochesmongodb.Utils.MongoDBManager;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.PerspectiveCamera;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import org.bson.Document;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.print.Doc;

public class HelloController implements Initializable {
    MongoClient con;
    public static final ObjectMapper JSON_MAPPER = new ObjectMapper(); //Mapper de JSON
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
    void onContentClick(MouseEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            con = MongoDBManager.conectar();
            MongoDatabase database = con.getDatabase("coches");
            MongoCollection<Document> collection = database.getCollection("coches");
            MongoCursor<Document> cursor = collection.find().iterator();
            ArrayList<Coche> coches = new ArrayList<>();
            try {
                while (cursor.hasNext()){
                    String json = cursor.next().toJson();
                    Coche coche = JSON_MAPPER.readValue(json,
                            JSON_MAPPER.getTypeFactory().constructType(Coche.class));
                    coches.add(coche);
                }
            } finally {
                cursor.close();
            }
            ObservableList<Coche> cocheObservableList = FXCollections.observableArrayList(coches);

            matrTCol.setCellValueFactory(new PropertyValueFactory<>("matricula"));
            markTCol.setCellValueFactory(new PropertyValueFactory<>("marca"));
            
        }catch (Exception e){
            System.out.println("Error de programa");
        }
    }
}