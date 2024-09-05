package org.uni.progetto.homepage;

public class UserSession {

    private static UserSession instance;

    private String nome;
    private String cognome;
    private String username;
    private boolean isLoggedOut = true;

    private UserSession(String nome, String cognome, String username) {
        this.nome = nome;
        this.cognome = cognome;
        this.username = username;
        this.isLoggedOut = false;

    }

    public static UserSession getInstance(String nome, String cognome, String username) {
        if(instance == null || instance.isLoggedOut) {
            instance = new UserSession(nome, cognome, username);
        }
        return instance;
    }

    public static UserSession getInstance() {
        if(instance == null) {
            return null;
        }
        return instance;
    }

    public void logout(){
        nome = null;
        cognome = null;
        username = null;
        instance = null;
        isLoggedOut = true;
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