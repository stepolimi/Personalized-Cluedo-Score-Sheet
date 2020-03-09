package com.example.cluedokiler;

import java.util.Collection;
import java.util.HashMap;

public class GameTable {

    public String player;
    public HashMap<String,String> gameTableHash;

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public GameTable(HashMap<String, String> gameTableHash,String player) {
        this.gameTableHash = gameTableHash;
        this.player = player;
    }
    public GameTable(){
        this.gameTableHash = new HashMap<>();
    }

    public HashMap<String, String> getGameTableHash() {
        return gameTableHash;
    }

    public void setGameTableHash(HashMap<String, String> gameTableHash) {
        this.gameTableHash = gameTableHash;
    }

    public String get (Integer key){
        return gameTableHash.get(key.toString());
    }

    public void put (String integer, String value){
        gameTableHash.put(integer,value);
    }

    public void remove(Integer key){
        gameTableHash.remove(key.toString());
    }

    public Collection values(){
        return gameTableHash.values();
    }

    public int getNumTick(){
        int sum=0;
        for(String place: gameTableHash.values()) {
            if (place.equals("tick"))
                sum++;
        }
        return sum;
    }

    public int getNumCross(){
        int sum=0;
        for(String place: gameTableHash.values()) {
            if (place.equals("cross"))
                sum++;
        }
        return sum;
    }

    public int getNumIncerts(){
        int sum=0;
        for(String place: gameTableHash.values()) {
            if (place.equals("empty") || place.equals("question") || place.equals("question2"))
                sum++;
        }
        return sum;
    }
}
