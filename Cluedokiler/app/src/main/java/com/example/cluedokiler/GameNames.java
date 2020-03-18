package com.example.cluedokiler;

import java.io.Serializable;

import static com.example.cluedokiler.Parameters.OUR_CLUEDO;

public class GameNames implements Serializable {

    private String[] suspects;
    private String[] weapons;
    private String[] places;
    private String gameMode;
    boolean modified;

    public GameNames(String[] suspects, String[] weapons, String[] places) {
        this.suspects = suspects;
        this.weapons = weapons;
        this.places = places;
        modified = false;
        gameMode = OUR_CLUEDO;
    }

    public GameNames(){
        suspects = new String[7];
        weapons =  new String[7];
        places = new String[10];
        modified = false;
    }

    public String[] getSuspects() {
        return suspects;
    }

    public void setSuspects(String[] suspects) {
        this.suspects = suspects;
    }

    public String[] getWeapons() {
        return weapons;
    }

    public void setWeapons(String[] weapons) {
        this.weapons = weapons;
    }

    public String[] getPlaces() {
        return places;
    }

    public void setPlaces(String[] places) {
        this.places = places;
    }

    public void addSuspect(String suspect, int index){
        suspects[index] = suspect;
    }

    public void addWeapon(String weapon, int index){
        weapons[index] = weapon;
    }

    public void addPlace(String place, int index){
        places[index] = place;
    }

    public boolean isModified() {
        return modified;
    }

    public void setModified(boolean modified) {
        this.modified = modified;
    }

    public String getGameMode() {
        return gameMode;
    }

    public void setGameMode(String gameMode) {
        this.gameMode = gameMode;
    }
}
