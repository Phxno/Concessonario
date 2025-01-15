package org.uni.progetto.homepage;
import com.google.gson.*;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class PrevClass {
    private UserClass utente;
    private String macchina;
    private String configurazione;
    private String dataCreazione;
    private String usato;
    private String prezzo;
    private String sconto;
    private String negozioConsegna;
    private String dataConsegna;
    private String id;
    public PrevClass(JsonObject PrevObject){
        this.utente = recoverUser(PrevObject.get("utente").getAsString());
        this.macchina = PrevObject.get("macchina").getAsString();
        this.configurazione = PrevObject.get("configurazione").getAsString();
        this.dataCreazione = PrevObject.get("dataCreazione").getAsString();
        this.usato = PrevObject.get("usato").getAsString();
        this.prezzo = PrevObject.get("prezzo").getAsString();
        this.sconto = PrevObject.get("sconto").getAsString();
        this.negozioConsegna = PrevObject.get("negozioConsegna").getAsString();
        this.dataConsegna = PrevObject.get("dataConsegna").getAsString();
        this.id = PrevObject.get("id").getAsString();
    }
    private UserClass recoverUser(String utente){
        String tempName = "";
        String tempSurname = "";
        String tempCell = "";
        String tempEmail = "";
        String file = "dati_utente.json";
        Gson gson = new Gson();
        try (Reader reader = new FileReader(file)){
            JsonArray usersArray = gson.fromJson(reader, JsonArray.class);
            //Creiamo un oggetto JSON per ogni utente
            for (JsonElement userElement : usersArray) {
                JsonObject userObject = userElement.getAsJsonObject();
                if (userObject.get("name").getAsString().concat(" ").concat(userObject.get("surname").getAsString()).equals(utente)){
                    tempName = userObject.get("name").getAsString();
                    tempSurname = userObject.get("surname").getAsString();
                    tempCell = userObject.get("phone").getAsString();
                    //tempEmail = userObject.get("email").getAsString();
                    tempEmail = "aabb@gmail.com";
                    break;
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        UserClass user = new UserClass(tempName, tempSurname, tempCell, tempEmail);
        return user;
    }
    public UserClass getUtente(){return this.utente;}
    public String getMacchina(){return this.macchina;}
    public String getUsato() {return this.usato;}
    public String getConfigurazione(){return this.configurazione;}
    public String getDataCreazione(){return this.dataCreazione;}
    public String getPrezzo(){return this.prezzo;}
    public String getSconto(){return this.sconto;}
    public String getNegozioConsegna(){return this.negozioConsegna;}
    public String getDataConsegna(){return this.dataConsegna;}
    public String getId(){return this.id;}
}
