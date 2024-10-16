package com.erikat.cochesmongodb.Obj;

import com.erikat.cochesmongodb.Utils.MongoDBManager;
import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;

import java.util.ArrayList;


public class CRUDCoche {
    MongoClient con;
    MongoDatabase database;
    Gson gson;
    MongoCollection<Document> collection;
    public CRUDCoche(){
        //MongoDB
        con = MongoDBManager.conectar();
        gson = new Gson();
        database = con.getDatabase("coches");
        collection = database.getCollection("coches");
    }
    public ArrayList<Coche> listCars(){
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
        return coches;
    }
    public Coche getCar(String plate){
        Coche car = null;
        try {
            Document document = collection.find(Filters.eq("matricula", plate)).first();
            if (document != null) {
                car = gson.fromJson(document.toJson(), Coche.class);
            }
        }catch (Exception e){
            System.out.println("Error de base de datos");
        }
        return car;
    }

    public boolean insertCar(Coche car) {
        try {
            long formerSize = collection.countDocuments();
            Document newCar = new Document("matricula", car.getMatricula()).append("marca", car.getMarca()).append("modelo", car.getModelo()).append("tipo", car.getTipo());
            collection.insertOne(newCar);
            return collection.countDocuments() != formerSize;
        }catch (Exception e){ //Si falla la base de datos
            return false;
        }
    }

    public boolean updateCar(Coche car) {
        try{
            Document formerCar = collection.find(Filters.eq("matricula", car.matricula)).first();
            UpdateResult result = collection.updateOne(new Document("matricula", car.getMatricula()), new Document("$set", new Document("marca", car.getMarca()).append("modelo", car.getModelo()).append("tipo", car.getTipo())));
            return result.wasAcknowledged(); //Devuelve si el update funcionó correctamente
        }catch (Exception e){ //Si falla la base de datos
            return false;
        }
    }

    public boolean deleteCar(String plate) {
        try{
            long formerSize = collection.countDocuments();
            collection.deleteOne(Filters.eq("matricula", plate));
            return formerSize != collection.countDocuments();
        } catch (Exception e){
            return false;
        }
    }
}
