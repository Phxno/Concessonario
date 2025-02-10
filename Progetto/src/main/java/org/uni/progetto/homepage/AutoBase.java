package org.uni.progetto.homepage;

class AutoBase extends Auto{
    public String immagine;
    public String descrizione;

    public AutoBase(String marca, String modello, double prezzo, String immagine, String descrizione){
        super(marca, modello, prezzo);
        this.immagine = immagine;
        this.descrizione = descrizione;
    }

    public String getImmagine(){
        return immagine;
    }

    public String getDescrizione(){
        return descrizione;
    }

    public void setImmagine(String immagine){
        this.immagine = immagine;
    }


    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

}
