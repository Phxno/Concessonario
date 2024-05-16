package org.example.homepage;

public class UserSession {

    private static UserSession instance;

    private String nome;
    private String cognome;
    private String username;

    private UserSession(String nome, String cognome, String username) {
        this.nome = nome;
        this.cognome = cognome;
        this.username = username;
    }

    public static UserSession getInstance(String nome, String cognome, String username) {
        if(instance == null) {
            instance = new UserSession(nome, cognome, username);
        }
        return instance;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return nome;
    }

    public String getLastName() {
        return cognome;
    }

    @Override
    public String toString() {
        return "UserSession{" +
                "nome='" + nome + '\'' +
                ", cognome='" + cognome + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}