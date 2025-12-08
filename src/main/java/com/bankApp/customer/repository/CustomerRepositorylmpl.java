package com.bankApp.customer.repository;

import com.mongodb.MongoBulkWriteException;
import com.mongodb.MongoException;
import com.mongodb.MongoQueryException;
import com.mongodb.client.*;
import com.mongodb.client.result.DeleteResult;
import org.bson.Document;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Indexes.ascending;

import java.util.ArrayList;
import java.util.List;

import com.bankApp.connection.ConnectionDB;

import com.bankApp.customer.model.Customer;

import java.util.Optional;

public class CustomerRepositorylmpl implements CustomerRepository{

    private Customer customer;
    private ConnectionDB connectionDB;
    private MongoCollection<Document> collection;

    public CustomerRepositorylmpl(){
        this.connectionDB = new ConnectionDB();
        collection = connectionDB.getCollection("Customer"); //Obtenemos la coleccion
    }

    //Metodo para buscar por id del cliente
    public Optional<Customer> findById(int id){
        try{
            //Obtenemos los datos del documento
            Document document = collection.find(eq("id", id)).first();
            //Obtenemos los datos guardados en nuestro documento
            if(document != null){
                Customer customer = new Customer(
                    document.getInteger("id"),
                    document.getInteger("edad"),
                    document.getString("nombre"),
                    document.getString("direccion"),
                    document.getString("email"),
                    document.getString("contraseña"),
                    document.getDouble("saldo"),
                    document.getDouble("credito")
                );

                return Optional.of(customer);

            }else{
                return Optional.empty();
            }

        }catch(MongoQueryException e){
            System.out.println("Error al buscar el cliente: " + e.getMessage());
            return Optional.empty();
        }
    }

    //Metodo para mostrar todos los clientes ordenados por su id
    public List<Customer> findAllOrderedById(){
            List <Customer> customers = new ArrayList<>();
        try {
            //Obtenemos los datos guardados en nuestro documento filtrados por id de menor a mayor
            for (Document document : collection.find().sort(ascending("id"))) {
                customer = new Customer(
                        document.getInteger("id"),
                        document.getInteger("edad"),
                        document.getString("nombre"),
                        document.getString("direccion"),
                        document.getString("email"),
                        document.getString("contraseña"),
                        document.getDouble("saldo"),
                        document.getDouble("credito")
                );
                customers.add(customer);
            }

        }catch(MongoQueryException e){
            System.out.println("Error al ordenar la lista de clientes: " + e.getMessage());

        }
        return customers;
    }

    //Metodo para agregar un cliente
    public  Customer save(Customer customer){
        try{
            //Creamos un documento y lo agregamos a la coleccion
            Document document = new Document("id", customer.getId())
                    .append("edad", customer.getEdad())
                    .append("nombre", customer.getNombreCompleto())
                    .append("direccion", customer.getDireccion())
                    .append("email", customer.getEmail())
                    .append("contraseña", customer.getPassword())
                    .append("saldo", customer.getSaldo())
                    .append("credito", customer.getCredito());

            collection.insertOne(document);
            return customer;

        }catch(MongoBulkWriteException e){
            System.out.println("Error al agregar el cliente: " + e.getMessage());
            return null;
        }
    }

    //Metodo para buscar un cliente por su nombre
    public List<Customer> findByName(String nombreCompleto) {
        List<Customer> customers = new ArrayList<>();
        try {
            // Usamos find() sin .first() para obtener todos los resultados
            for (Document document : collection.find(eq("nombre", nombreCompleto))) {
                Customer customer = new Customer(
                        document.getInteger("id"),
                        document.getInteger("edad"),
                        document.getString("nombre"),
                        document.getString("direccion"),
                        document.getString("email"),
                        document.getString("contraseña"),
                        document.getDouble("saldo"),
                        document.getDouble("credito")
                );
                customers.add(customer);
            }
        } catch(MongoQueryException e) {
            System.out.println("Error al buscar clientes por nombre: " + e.getMessage());
        }
        return customers;
    }


    //Metodo para buscar un cliente por su email
    public Optional <Customer> findByEmail(String email){
        try{
            //Buscamos los datos del cliente mediante su correo
            Document document = collection.find(eq("email", email)).first();

            //Mostramos los datos del cliente
            if(document != null){
                Customer customer = new Customer(
                document.getInteger("id"),
                document.getInteger("edad"),
                document.getString("nombre"),
                document.getString("direccion"),
                document.getString("email"),
                document.getString("contraseña"),
                document.getDouble("saldo"),
                document.getDouble("credito")
                );

                return Optional.of(customer);
            }else{
                return Optional.empty();
            }

        }catch(MongoQueryException e){
            System.out.println("Error al buscar el cliente: " + e.getMessage());
            return Optional.empty();
        }
    }

    //Metodo para actualizar los datos de un cliente
    public Customer update(int id, Customer customer){
        try{
            Document document = new Document("$set", new Document("edad", customer.getEdad())
                    .append("nombre", customer.getNombreCompleto())
                    .append("direccion", customer.getDireccion())
                    .append("email", customer.getEmail())
                    .append("contraseña", customer.getPassword())
                    .append("saldo", customer.getSaldo())
                    .append("credito", customer.getCredito()));

            collection.updateOne(eq("id", id), document);

            return customer;

        }catch(MongoBulkWriteException e){
            System.out.println("Error al actualizar los datos del cliente: " + e.getMessage());
            return null;
        }
    }

    //Metodo para eliminar un cliente
    public  boolean delteById(int id){
        try {
            DeleteResult result = collection.deleteOne(eq("id", id));
            return result.getDeletedCount() > 0; //Devuelve true solo si elimino un cliente
        }catch(MongoException e){
            System.out.println("Error al eliminar el cliente: " + e.getMessage());
            return false;
        }
    }
}