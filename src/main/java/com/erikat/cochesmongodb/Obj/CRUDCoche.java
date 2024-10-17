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


public class CRUDCoche { //DAO de Coche en MongoDB
    MongoClient con;
    MongoDatabase database;
    Gson gson;
    MongoCollection<Document> collection;

    public CRUDCoche(){
        //MongoDB
        try {
            con = MongoDBManager.conectar(); //Se conecta a la base de datos
            gson = new Gson(); //Crea un gson
            database = con.getDatabase("ErikAmo_Coches"); //Accede a la base de datos con este nombre
            collection = database.getCollection("coches"); //Accede a la colección de la base de datos
        }catch (NullPointerException e){ //Por si la base de datos es nula
            System.out.println(e.getMessage());
        }
    }
    public ArrayList<Coche> listarCoches(){ //Función que busca en la base de datos
        ArrayList<Coche> coches = new ArrayList<>();
        MongoCursor<Document> cursor = collection.find().iterator(); //Crea un iterador que recorre toda la colección de la base de datos
        try {
            while (cursor.hasNext()){ //Mientras haya datos a recorrer en el cursor
                String json = cursor.next().toJson(); //Convierte el dato del cursor en json
                Coche coche = gson.fromJson(json, Coche.class); //Convierte el json a clase con la librería Gson
                coches.add(coche); //Añade el coche
            }
        }catch (Exception e){
            System.out.println("Error de programa");
        } finally {
            cursor.close(); //Cierra el cursor
        }
        return coches; //Devuelve la lista
    }
    public Coche getCoche(String matricula){
        Coche coche = null; //Crea un objeto de tipo coche iniciado en nulo
        try {
            Document document = collection.find(Filters.eq("matricula", matricula)).first(); //Busca el primer objeto de la lista que tenga la matrícula pasada
            if (document != null) { //Si el documento no es nulo, lo transforma en clase con Gson y lo guarda en el objeto de tipo coche
                coche = gson.fromJson(document.toJson(), Coche.class);
            }
        }catch (Exception e){
            System.out.println("Error de base de datos");
        }
        return coche; //Devuelve el coche, tanto si es nulo como si no
    }

    public boolean insertarCoche(Coche coche) {
        try {
            long formerSize = collection.countDocuments(); //Tamaño antes de añadir
            Document newCar = new Document("matricula", coche.getMatricula()).append("marca", coche.getMarca()).append("modelo", coche.getModelo()).append("tipo", coche.getTipo()); //Crea el documento con los datos del coche
            collection.insertOne(newCar); //Inserta el documento
            return collection.countDocuments() != formerSize; //Devuelve si ha cambiado el tamaño
        }catch (Exception e){ //Si falla la base de datos devuelve falso
            return false;
        }
    }

    public boolean actualizarCoche(Coche coche) {
        try{
            UpdateResult resultado = collection.updateOne(new Document("matricula", coche.getMatricula()), new Document("$set", new Document("marca", coche.getMarca()).append("modelo", coche.getModelo()).append("tipo", coche.getTipo())));
            return resultado.wasAcknowledged(); //Devuelve si el update funcionó correctamente
        }catch (Exception e){ //Si falla la base de datos devuelve falso
            return false;
        }
    }

    public boolean borrarCoche(String coche) {
        try{
            long formerSize = collection.countDocuments(); //Tamaño antes de borrar el objeto
            collection.deleteOne(Filters.eq("matricula", coche));
            return formerSize != collection.countDocuments(); //Devuelve si ha cambiado el tamaño
        } catch (Exception e){
            return false;
        }
    }
}
