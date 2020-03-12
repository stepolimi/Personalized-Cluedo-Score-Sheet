package com.example.cluedokiler;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DbManager {

    private static DbManager single_instance = null;
    DatabaseReference db;
    int numGames=0;

    private DbManager(){
    }

    public static DbManager getInstance() {
        if (single_instance == null)
            single_instance = new DbManager();
        return single_instance;
    }

    public void saveGameRecord(){
        db=FirebaseDatabase.getInstance().getReference().child("GameRecord");
        db.child(GameStatus.getInstance().gameTime).setValue(GameStatus.getInstance().gameTableHash);
    }

    public void savePlayersRecord(){
        Users users = new Users(GameStatus.getInstance().playersNames);
        db = FirebaseDatabase.getInstance().getReference().child("PlayersRecord");
        db.child(GameStatus.getInstance().gameTime).setValue(users);
    }

    public void saveValidatedGame(){
        db= FirebaseDatabase.getInstance().getReference().child("ValidatedGame");
        db.child(GameStatus.getInstance().gameTime).setValue(GameStatus.getInstance().winner);
    }

    public void saveUnValidatedGame(){
        db= FirebaseDatabase.getInstance().getReference().child("UnValidatedGame");
        db.child(GameStatus.getInstance().gameTime).setValue(GameStatus.getInstance().winner);
    }

    public int getNumGames(){
        db = FirebaseDatabase.getInstance().getReference().child("PlayersRecord");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                numGames=0;
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    if(data.getValue(Users.class).getPlayers().get(0).equals(GameStatus.getInstance().playerName)) {
                        numGames= numGames + 1;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
        return numGames;
    }
}
