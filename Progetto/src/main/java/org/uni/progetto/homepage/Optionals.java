package org.uni.progetto.homepage;

import java.util.Arrays;

public class Optionals {
    private int[] opts = {0,0,0,0,0,0};
    private final Integer[] Pricesss = {500,5000,800,1500,1000,2000};


    public int getColor() {
        return opts[0];
    }

    public int getEngine() {
        return opts[1];
    }

    public int getHeat_seats() {
        return opts[5];
    }

    public int getMirror() {
        return opts[2];
    }

    public int getRoofGlass(){
        return opts[4];
    }

    public int getTel_pos() {
        return opts[3];
    }

    public void setColor(int color) {
        opts[0] = color;
    }

    public void setEngine(int engine) {
        opts[1] = engine;
    }

    public void setHeat_seats(int heat_seats) {
        opts[5] = heat_seats;
    }

    public void setMirror(int mirror) {
        opts[2] = mirror;
    }
    public void setRoofGlass(int RoofGlass){
        opts[4] = RoofGlass;
    }

    public void setTel_pos(int tel_pos) {
        opts[3] = tel_pos;
    }
    public int calcuateTotPrice(){
        int totpr = 0;
        for (int i = 0; i < Pricesss.length; i++){
            totpr += Pricesss[i] * opts[i];
        }
        return totpr;
    }
}
