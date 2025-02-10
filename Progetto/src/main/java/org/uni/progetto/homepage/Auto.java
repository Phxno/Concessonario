package org.uni.progetto.homepage;

public class Auto {
    public String marca;
    public String modello;
    public double prezzo;

    public Auto(String marca, String modello, double prezzo) {
        this.marca = marca;
        this.modello = modello;
        this.prezzo = prezzo;
    }


    public String getMarca() {
        return marca;
    }

    public String getModello() {
        return modello;
    }
/*
    public String getImmagine() {
        return immagine;
    }

 */

    public double getPrezzo() {
        return prezzo;
    }
/*


    public String getDescrizione() {
        return descrizione;
    }

 */

    // Setters
    public void setMarca(String marca) {
        this.marca = marca;
    }

    public void setModello(String modello) {
        this.modello = modello;
    }

    public void setPrezzo(double prezzo) {
        this.prezzo = prezzo;
    }

}




