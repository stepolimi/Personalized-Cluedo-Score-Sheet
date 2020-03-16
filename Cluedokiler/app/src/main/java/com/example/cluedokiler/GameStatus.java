package com.example.cluedokiler;

import java.util.ArrayList;

public class GameStatus {

    private static GameStatus single_instance = null;

    public GameTable gameTableHash;
    public boolean tableSet;
    public ArrayList<String> playersNames;
    public boolean playersSet;
    public boolean hideTable;
    public String playerName;
    public int numCol;
    public ArrayList<Integer> highlightedRows;
    public String confirmationCode;
    public String winner;
    public String gameTime;
    public ArrayList<String> tentativePlayers;
    public String theme;
    public GameNames gameNames;
    public long multiPlayerCode;

    private GameStatus() {
        tentativePlayers=new ArrayList<>();
        for(int i=0; i<6; i++)
            tentativePlayers.add("");
        gameTableHash = new GameTable();
        tableSet = false;
        playersSet = false;
        hideTable = false;
        playersNames = new ArrayList<>();
        playerName = "--Vuoto--";
        highlightedRows = new ArrayList<>();
        winner ="";
        confirmationCode="";
        theme = Parameters.PURPLE;
        multiPlayerCode = 0;
    }

    public static GameStatus getInstance() {
        if (single_instance == null)
            single_instance = new GameStatus();
        return single_instance;
    }

    public void newGame(){
        tentativePlayers=new ArrayList<>();
        for(int i=0; i<6; i++)
            tentativePlayers.add("");
        gameTableHash = new GameTable();
        playersNames.clear();
        tableSet = false;
        hideTable = false;
        playersSet = false;
        highlightedRows = new ArrayList<>();
        winner ="";
        gameTime="";
        multiPlayerCode = 0;
    }

}
