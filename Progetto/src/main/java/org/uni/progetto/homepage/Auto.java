package org.uni.progetto.homepage;

public class Auto {
    public String marca;
    public String modello;
    public String immagine;
    public String prezzo;
    public String descrizione;


    public String getMarca() {
        return marca;
    }

    public String getModello() {
        return modello;
    }

    public String getImmagine() {
        return immagine;
    }

    public String getPrezzo() {
        return prezzo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    // Setters
    public void setMarca(String marca) {
        this.marca = marca;
    }

    public void setModello(String modello) {
        this.modello = modello;
    }

    public void setImmagine(String immagine) {
        this.immagine = immagine;
    }

    public void setPrezzo(String prezzo) {
        this.prezzo = prezzo;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }
}