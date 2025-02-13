package org.uni.progetto.homepage;
import com.google.gson.*;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class PrevClass {
    private UserClass utente;
    private String macchina;
    private JsonArray configurazione;
    private String dataCreazione;
    private String usato;
    private String prezzo;
    private String sconto;
    private String negozioConsegna;
    private String dataConsegna;
    private String id;
    private String prezzoUsato;
    public PrevClass(JsonObject PrevObject){
        this.utente = recoverUser(PrevObject.get("utente").getAsString());
        this.macchina = PrevObject.get("macchina").getAsString();
        this.configurazione = verboseConfigurazione(PrevObject.get("configurazione").getAsJsonArray());
        this.dataCreazione = PrevObject.get("dataCreazione").getAsString();
        this.usato = PrevObject.get("usato").getAsString();
        this.prezzo = PrevObject.get("prezzo").getAsString();
        this.sconto = PrevObject.get("sconto").getAsString();
        this.negozioConsegna = PrevObject.get("negozioConsegna").getAsString();
        this.dataConsegna = PrevObject.get("dataConsegna").getAsString();
        this.id = PrevObject.get("id").getAsString();
        this.prezzoUsato = PrevObject.get("prezzoUsato").getAsString();
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
                if (userObject.get("username").getAsString().equals(utente)){
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
    //
    private JsonArray verboseConfigurazione(JsonArray configurazione){
        configurazione.set(2, new JsonPrimitive(configurazione.get(2).getAsBoolean() ? "Telecamera Posteriore" : "" ));
        configurazione.set(3, new JsonPrimitive(configurazione.get(3).getAsBoolean() ? "Finestrini Oscurati" : ""));
        configurazione.set(4, new JsonPrimitive(configurazione.get(4).getAsBoolean() ? "Adas Avanzati" : "" ));
        configurazione.set(5, new JsonPrimitive(configurazione.get(5).getAsBoolean() ? "Sedili Riscaldati": ""));
        return configurazione;
    }
    public UserClass getUtente(){return this.utente;}
    public String getMacchina(){return this.macchina;}
    public String getUsato() {return this.usato;}
    public JsonArray getConfigurazione(){return this.configurazione;}
    public String getDataCreazione(){return this.dataCreazione;}
    public String getPrezzo(){return this.prezzo;}
    public String getSconto(){return this.sconto;}
    public String getNegozioConsegna(){return this.negozioConsegna;}
    public String getDataConsegna(){return this.dataConsegna;}
    public String getId(){return this.id;}
    public String getPrezzoUsato(){return this.prezzoUsato;}
}
