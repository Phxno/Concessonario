package org.uni.progetto.homepage;

public class userClass {
    private String name;
    private String surname;
    private String phone;
    private String email;
    public userClass(String nome, String cognome, String cell, String mail){
        this.name = nome;
        this.surname = cognome;
        this.phone = cell;
        this.email = mail;
    }
    public String getName(){
        return this.name;
    }
    public String getSurname(){
        return this.surname;
    }
    public String getPhone(){
        return this.phone;
    }
    public String getEmail(){
        return this.email;
    }
}
