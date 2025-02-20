package org.uni.progetto.homepage;
import com.google.gson.*;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class OrderClass {
  private UserClass utente;
  private String macchina;
  private JsonArray configurazione;
  private String dataFinalizzazione;
  private String prezzo;
  private String negozioConsegna;
  private String dataConsegna;
  private String id;
  public OrderClass(JsonObject OrderObject){
    this.utente = recoverUser(OrderObject.get("utente").getAsString());
    this.macchina = OrderObject.get("macchina").getAsString();
    this.configurazione = OrderObject.get("configurazione").getAsJsonArray();
    this.dataFinalizzazione = OrderObject.get("dataFinalizzazione").getAsString();
    this.prezzo = OrderObject.get("prezzo").getAsString();
    this.negozioConsegna = OrderObject.get("negozioConsegna").getAsString();
    this.dataConsegna = OrderObject.get("dataConsegna").getAsString();
    this.id = OrderObject.get("id").getAsString();
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
  public UserClass getUtente(){
    return this.utente;
  }
  public String getMacchina(){
    return this.macchina;
  }
  public String getDataFinalizzazione(){
    return this.dataFinalizzazione;
  }
  public String getPrezzo(){
    return this.prezzo;
  }
  public String getNegozioConsegna(){
    return this.negozioConsegna;
  }
  public String getDataConsegna(){
    return this.dataConsegna;
  }
  public String getId(){
    return this.id;
  }
}
