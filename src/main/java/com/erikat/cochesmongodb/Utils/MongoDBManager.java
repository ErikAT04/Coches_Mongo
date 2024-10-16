package com.erikat.cochesmongodb.Utils;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.ServerAddress;

import java.util.Properties;

public class MongoDBManager {
    public static MongoClient conectar(){
        try {
            Properties dbConf = new Properties();
            dbConf.load(R.getProperties("database.properties"));
            String host = dbConf.getProperty("host");
            String user = dbConf.getProperty("user");
            String pass = dbConf.getProperty("password");
            String port = dbConf.getProperty("port");
            String source = dbConf.getProperty("source");

            final MongoClient cliente = new MongoClient(new MongoClientURI("mongodb://"+user+":"+pass+"@"+host+":"+port+"/?authSource="+source));
            System.out.println("Programa conectado a la base de datos de MongoDB correctamente");

            return cliente;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }
    public static void closeCon(MongoClient con){
        con.close();
    }
}
