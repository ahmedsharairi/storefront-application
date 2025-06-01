package com.storefront.backend.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public class User {
    @Id
    private String id;
    private String username;
    private String password;

    // Constructors
    public User() {}
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }
    public void setUsername(String username){
        this.username = username;
    }

    public void setPassword(String password){
        this.password = password;
    }

    // Getters
    public String getId() {
        return id;
    }
    public String getUsername(){
        return username;
    }

    public String getPassword(){
        return password;
    }

}
