package com.example.cluedokiler.profile.pastGames;

import com.example.cluedokiler.models.GameTable;

import java.io.Serializable;
import java.util.ArrayList;

public class PastGame implements Serializable {
    private String date;
    private ArrayList<String> players;
    private String winner;
    private String gameMode;
    private GameTable table;

    public PastGame(String text1, ArrayList<String> players, String winner, String gameMode, GameTable table){
        this.date = text1;
        if(players!=null)
            this.players = players;
        else
            players= new ArrayList<String>();
        this.winner = winner;
        this.gameMode = gameMode;
        this.table = table;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<String> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<String> players) {
        this.players = players;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public String getGameMode() {
        return gameMode;
    }

    public void setGameMode(String gameMode) {
        this.gameMode = gameMode;
    }

    public GameTable getTable() { return table; }

    public void setTable(GameTable table) { this.table = table; }
}
