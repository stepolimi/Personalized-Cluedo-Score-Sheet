package com.example.cluedokiler;

import java.util.ArrayList;
import java.util.HashMap;

public class GameStatus {

    private static GameStatus single_instance = null;

    public HashMap<Integer,String> gameTableHash;
    public boolean tableSet;
    public ArrayList<String> playersNames;
    public boolean playersSet;
    public boolean hideTable;
    public Long gameNumber;

    private GameStatus() {
        gameTableHash = new HashMap<>();
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
