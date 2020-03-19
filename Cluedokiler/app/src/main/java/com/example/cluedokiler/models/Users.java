package com.example.cluedokiler.models;

import java.util.ArrayList;

public class Users {
    ArrayList<String> players;

    public Users(ArrayList<String> players) {
        this.players = players;
    }

    public Users(){}

    public ArrayList<String> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<String> players) {
        this.players = players;
    }

    public boolean contains(String name){
        return players.contains(name);
    }
}
