package com.example.cluedokiler.db;

import androidx.annotation.NonNull;

import com.example.cluedokiler.gameInstance.GameStatus;
import com.example.cluedokiler.models.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DbManager {

    private static DbManager single_instance = null;
    private DatabaseReference db;
    private int numGames=0;

    private DbManager(){
    }

    public static DbManager getInstance() {
        if (single_instance == null)
            single_instance = new DbManager();
        return single_instance;
    }

    public void saveGameRecord(){
        ArrayList<String> players = GameStatus.getInstance().playersNames;
        final String name = GameStatus.getInstance().playerName;
        final long code = GameStatus.getInstance().multiPlayerCode;
        db=FirebaseDatabase.getInstance().getReference().child("GameRecord").child(GameStatus.getInstance().gameTime);
        db.child("Game Table").setValue(GameStatus.getInstance().gameTableHash);
        db.child("GameMode").setValue(GameStatus.getInstance().gameNames.getGameMode());

        DatabaseReference db= FirebaseDatabase.getInstance().getReference().child("MultiPlayerGame").child(String.valueOf(GameStatus.getInstance().multiPlayerCode));
        db.child(GameStatus.getInstance().playerName).child("GameTable").setValue(GameStatus.getInstance().gameTableHash);
        db.child("GameMode").setValue(GameStatus.getInstance().gameNames.getGameMode());

        DatabaseReference db2= FirebaseDatabase.getInstance().getReference().child(GameStatus.getInstance().playerName).child(String.valueOf(GameStatus.getInstance().multiPlayerCode));
        db2.child("Players").setValue(GameStatus.getInstance().playersNames);
        db2.child("GameTable").setValue(GameStatus.getInstance().gameTableHash);
        db2.child("GameNames").setValue(GameStatus.getInstance().gameNames);
        db2.child("GameMode").setValue(GameStatus.getInstance().gameNames.getGameMode());
        db2.child("Winner").setValue(GameStatus.getInstance().winner);
        if (GameStatus.getInstance().confirmationCode.equals("passwordsicurissima"))
            db2.child("Validated").setValue("true");
        else
            db2.child("Validated").setValue("false");

        for (String player: players){
            if(GameStatus.getInstance().confirmationCode.equals("passwordsicurissima")) {
                DatabaseReference dbb = FirebaseDatabase.getInstance().getReference().child(player).child(String.valueOf(code));
                dbb.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists())
                            dataSnapshot.getRef().child("Validated").setValue("true");
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { }
                });
            }else {
                DatabaseReference dbn = FirebaseDatabase.getInstance().getReference().child(player).child(String.valueOf(code)).child("Validated");
                dbn.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue()!=null && dataSnapshot.getValue().equals("true"))
                            FirebaseDatabase.getInstance().getReference().child(name).child(String.valueOf(code)).child("Validated").setValue("true");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }
    }

    public void savePlayersRecord(){
        final Users users = new Users(GameStatus.getInstance().playersNames);
        db = FirebaseDatabase.getInstance().getReference().child("PlayersRecord");
        db.child(GameStatus.getInstance().gameTime).setValue(users);

        db= FirebaseDatabase.getInstance().getReference().child("MultiPlayerGame").child(String.valueOf(GameStatus.getInstance().multiPlayerCode)).child("Players");

        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    if ( !dataSnapshot.exists())
                        db.setValue(users);
                }catch(Exception e){}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    public void saveValidatedGame(){
        db= FirebaseDatabase.getInstance().getReference().child("ValidatedGame");
        db.child(GameStatus.getInstance().gameTime).setValue(GameStatus.getInstance().winner);

        db= FirebaseDatabase.getInstance().getReference().child("MultiPlayerGame").child(String.valueOf(GameStatus.getInstance().multiPlayerCode)).child("Winner");
        db.setValue(GameStatus.getInstance().winner);

        db= FirebaseDatabase.getInstance().getReference().child("MultiPlayerGame").child(String.valueOf(GameStatus.getInstance().multiPlayerCode)).child("Validated");
        db.setValue("true");

    }

    public void saveUnValidatedGame(final String winner){
        db= FirebaseDatabase.getInstance().getReference().child("UnValidatedGame");
        db.child(GameStatus.getInstance().gameTime).setValue(GameStatus.getInstance().winner);

        final DatabaseReference db= FirebaseDatabase.getInstance().getReference().child("MultiPlayerGame").child(String.valueOf(GameStatus.getInstance().multiPlayerCode)).child("Winner");
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    if (!dataSnapshot.exists())
                        db.setValue(winner);
                }catch(Exception e){}
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

        final DatabaseReference db2= FirebaseDatabase.getInstance().getReference().child("MultiPlayerGame").child(String.valueOf(GameStatus.getInstance().multiPlayerCode)).child("Validated");
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    if ( !dataSnapshot.exists())
                        db2.setValue("false");
                }catch(Exception e){}
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    public int getNumGames(){
        db = FirebaseDatabase.getInstance().getReference().child("PlayersRecord");
        db.addListenerForSingleValueEvent(new ValueEventListener() {
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

    public void saveMultiPlayerCode(){
        //DatabaseReference db= FirebaseDatabase.getInstance().getReference().child("MultiPlayerGame").child(String.valueOf(GameStatus.getInstance().multiPlayerCode));
        //db.child(GameStatus.getInstance().playerName).child("code").setValue(GameStatus.getInstance().multiPlayerCode);
/*
        final DatabaseReference db2= FirebaseDatabase.getInstance().getReference().child("MultiPlayerGame").child(String.valueOf(GameStatus.getInstance().multiPlayerCode)).child("Players");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    if ( !dataSnapshot.exists()) {
                        for (String name : dataSnapshot.getValue(Users.class).getPlayers())
                            if (name.equals(GameStatus.getInstance().playerName)) {
                                //ok
                                //return;
                            }
                        //partita non valida
                    }else{

                    }

                }catch(Exception e){}
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });*/

        //todo
        //all of this should go in main activity to control if the game should start based on matching players with an already existing game with the same code

        //check if a game exists and in case join that, but only if the game is not finished yet
    }
}
