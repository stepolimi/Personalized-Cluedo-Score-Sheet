package com.example.cluedokiler;

import java.util.ArrayList;

public class GameStatus {

    private static GameStatus single_instance = null;

    public GameTable gameTableHash;
    public boolean tableSet;
    public ArrayList<String> playersNames;
    public boolean playersSet;
    public boolean hideTable;
    public Long gameNumber;
    public String playerName;
    public int numCol;

    private GameStatus() {
        gameTableHash = new GameTable();
        tableSet = false;
        playersSet = false;
        hideTable = false;
        playersNames = new ArrayList<>();
    }

    public static GameStatus getInstance()
    {
        if (single_instance == null)
            single_instance = new GameStatus();

        return single_instance;
    }
}
