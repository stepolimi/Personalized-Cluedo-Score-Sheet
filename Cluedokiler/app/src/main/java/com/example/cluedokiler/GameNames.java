package com.example.cluedokiler;

public class GameNames {

    private String[] suspects;
    private String[] weapons;
    private String[] places;
    boolean modified;

    public GameNames(String[] suspects, String[] weapons, String[] places) {
        this.suspects = suspects;
        this.weapons = weapons;
        this.places = places;
        modified = false;
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
}
