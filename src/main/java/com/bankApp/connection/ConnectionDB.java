package com.bankApp.connection;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.client.*;
import org.bson.Document;
import org.bson.types.ObjectId;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.*;

public class ConnectionDB {

    //Le pasamos la url de nuestra base de datos en este caso es una local
    private String url = "mongodb://127.0.0.1:27017";
    private MongoClient mongoClient;
    private MongoDatabase database;

    public ConnectionDB() {
        try {
            //Establecemos la conexion a nuestra base de datos
            mongoClient = MongoClients.create(
                    MongoClientSettings.builder()
                            .applyConnectionString(new ConnectionString(url))
                            .build());

            //Seleccionamos la base de datos
            database = mongoClient.getDatabase("Bank");

            System.out.println("Conexion exitosa a MongoDB");

        }catch(MongoException e){
            System.out.println("Error al conectarse a la base de datos: " + e.getMessage());
        }
    }

    //Getters
    public MongoClient getMongoClient() {return mongoClient;}
    public MongoDatabase getDatabase() {return database;}

    //Obtenemos la coleccion de nuestra base de datos
    public MongoCollection<Document> getCollection(String nameCollection) {
        return database.getCollection(nameCollection);
    }

    //Cerramos la conexion a nuestra base de datos
    public void ConnectionClose(){
        if(mongoClient != null){
            mongoClient.close();
            System.out.println("Conexion cerarda exitosamente");
        }
    }
}
