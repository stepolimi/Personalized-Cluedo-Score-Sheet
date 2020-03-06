package com.example.cluedokiler;

import java.util.HashMap;

public class GameStatus {

    private static GameStatus single_instance = null;

    public HashMap<Integer,String> gameTableHash;
    public boolean alreadySet = false;

    private GameStatus() {
        gameTableHash = new HashMap<>();
    }

    public static GameStatus getInstance()
    {
        if (single_instance == null)
            single_instance = new GameStatus();

        return single_instance;
    }
}
