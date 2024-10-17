package com.erikat.cochesmongodb.Utils;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.ServerAddress;

import java.util.Properties;

public class MongoDBManager {
    public static MongoClient conectar(){
        try {
            Properties dbConf = new Properties(); //Crea un objeto de tipo Properties
            dbConf.load(R.getProperties("database.properties")); //Lee el archivo de propiedades y procede a coger todos los datos que ve necesarios
            String host = dbConf.getProperty("host");
            String user = dbConf.getProperty("user");
            String pass = dbConf.getProperty("password");
            String port = dbConf.getProperty("port");
            String source = dbConf.getProperty("source");

            final MongoClient cliente = new MongoClient(new MongoClientURI("mongodb://"+user+":"+pass+"@"+host+":"+port+"/?authSource="+source)); //Se conecta a la base de datos en función de la url
            System.out.println("Programa conectado a la base de datos de MongoDB correctamente");

            return cliente; //Devuelve la conexión
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null; //Devuelve nulo si hay un error
        }
    }
    public static void closeCon(MongoClient con){
        con.close();
    }
}
