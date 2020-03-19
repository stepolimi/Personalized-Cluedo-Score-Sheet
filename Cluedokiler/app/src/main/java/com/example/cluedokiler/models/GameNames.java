package com.example.cluedokiler.models;

import java.io.Serializable;
import java.util.ArrayList;

import static com.example.cluedokiler.parameters.Parameters.OUR_CLUEDO;

public class GameNames implements Serializable {

    private ArrayList<String> suspects;
    private ArrayList<String> weapons;
    private ArrayList<String> places;
    private String gameMode;
    private boolean modified;

    public GameNames(ArrayList<String> suspects, ArrayList<String> weapons, ArrayList<String> places) {
        this.suspects = suspects;
        this.weapons = weapons;
        this.places = places;
        modified = false;
        gameMode = OUR_CLUEDO;
    }

    public GameNames(){
        suspects = new ArrayList<>();
        weapons =  new ArrayList<>();
        places = new ArrayList<>();
        modified = false;
    }

    public ArrayList<String> getSuspects() {
        return suspects;
    }

    public void setSuspects(ArrayList<String> suspects) {
        this.suspects = suspects;
    }

    public ArrayList<String> getWeapons() {
        return weapons;
    }

    public void setWeapons(ArrayList<String> weapons) {
        this.weapons = weapons;
    }

    public ArrayList<String> getPlaces() {
        return places;
    }

    public void setPlaces(ArrayList<String> places) {
        this.places = places;
    }

    public void addSuspect(String suspect){
        suspects.add( suspect);
    }

    public void addWeapon(String weapon){
        weapons.add(weapon);
    }

    public void addPlace(String place){
        places.add(place);
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
