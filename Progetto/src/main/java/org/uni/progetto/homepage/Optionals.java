package org.uni.progetto.homepage;

public enum Optionals {
    ENGINE(3500),
    TEL_POS(900),
    MIRRORS(500),
    ADAS(1800),
    HEATS_SEATS(1500);

    private final int optionals;

    Optionals (int optionals){
        this.optionals = optionals;
    }
    public int getOptionals(){
        return optionals;
    }
}
