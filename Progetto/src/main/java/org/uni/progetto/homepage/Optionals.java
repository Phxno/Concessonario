package org.uni.progetto.homepage;

public enum Optionals {
    ENGINE(4000),
    TEL_POS(1000),
    MIRRORS(500),
    ADAS(2500),
    HEATS_SEATS(1500);

    private final int optionals;

    Optionals (int optionals){
        this.optionals = optionals;
    }
    public int getOptionals(){
        return optionals;
    }
}
