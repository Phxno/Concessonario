package org.uni.progetto.homepage;

public class AutoConf extends Auto{
    private String[] listaMotori;
    private int[] finestrini;
    private int[] listaPezziAutoColorati;
    private double[] posizioneCamera;

    public AutoConf(String marca, String modello, double prezzo, String[] listaMotori, int[] finestrini, int[] listaPezziAutoColorati,
                    double[] posizioneCamera) {
        super(marca, modello, prezzo);
        this.listaMotori = listaMotori;
        this.finestrini = finestrini;
        this.listaPezziAutoColorati = listaPezziAutoColorati;
        this.posizioneCamera = posizioneCamera;
    }

    public String[] getListaMotori() { return listaMotori; }
    public void setListaMotori(String[] listaMotori) { this.listaMotori = listaMotori; }

    public int[] getFinestrini() { return finestrini; }
    public void setFinestrini(int[] finestrini) { this.finestrini = finestrini; }

    public int[] getListaPezziAutoColorati() { return listaPezziAutoColorati; }
    public void setListaPezziAutoColorati(int[] listaPezziAutoColorati) { this.listaPezziAutoColorati = listaPezziAutoColorati; }

    public double[] getPosizioneCamera() { return posizioneCamera; }
    public void setPosizioneCamera(double[] posizioneCamera) { this.posizioneCamera = posizioneCamera; }
}

