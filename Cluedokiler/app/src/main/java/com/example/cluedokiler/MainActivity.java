package com.example.cluedokiler;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity  {

    ArrayList<PlayerSpinnerListener> spinnerListeners = new ArrayList<>();
    DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final GameStatus gameStatus = GameStatus.getInstance();
        compileSpinners();


        final Button playButton = (Button) findViewById(R.id.playButton);
        Button resetButton = (Button) findViewById(R.id.resetButton);

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameStatus gameStatus = GameStatus.getInstance();
                gameStatus.tableSet = false;
                gameStatus.playersSet = false;
                playButton.setText(getResources().getString(R.string.giocaPlayButton));
            }
        });

        if(gameStatus.playersSet)
            playButton.setText(getResources().getString(R.string.riprendiPlayButton));
        else
            playButton.setText(getResources().getString(R.string.giocaPlayButton));



        db = FirebaseDatabase.getInstance().getReference().child("Game").child("Players");

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                    gameStatus.gameNumber=(dataSnapshot.getChildrenCount());
                else
                    gameStatus.gameNumber= Long.valueOf(0);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameStatus gameStatus = GameStatus.getInstance();


                if(!gameStatus.playersSet) {
                    gameStatus.playersNames.clear();
                    for (int i = 0; i < 6; i++)
                        if (!spinnerListeners.get(i).getPlayerChoice().equals("--Vuoto--") && !spinnerListeners.get(i).getPlayerChoice().equals(""))
                            if (!gameStatus.playersSet)
                                gameStatus.playersNames.add(spinnerListeners.get(i).getPlayerChoice());

                    if( ! (gameStatus.playersNames.size() > 2))
                        gameStatus.playersNames.clear();
                    else
                        gameStatus.playersSet = true;
                }


                if(gameStatus.playersNames.size() > 2){
                    Intent startGameIntent = new Intent(getApplicationContext(), GameActivity.class);
                    startActivity(startGameIntent);


                    /*          */

                    db.child(String.valueOf(gameStatus.gameNumber+1)).setValue(gameStatus.playersNames);

                }

                if(gameStatus.playersSet)
                    playButton.setText(getResources().getString(R.string.riprendiPlayButton));
                else
                    playButton.setText(getResources().getString(R.string.giocaPlayButton));


            }
        });



    }


    private void compileSpinners() {
        ArrayList<String> players = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.players)));
        ArrayList<ArrayAdapter<String>> adapters = new ArrayList<>();

        Spinner playerChoiceSpinner1 = (Spinner) findViewById(R.id.playerCoicheSpinner6);
        Spinner playerChoiceSpinner2 = (Spinner) findViewById(R.id.playerCoicheSpinner5);
        Spinner playerChoiceSpinner3 = (Spinner) findViewById(R.id.playerCoicheSpinner4);
        Spinner playerChoiceSpinner4 = (Spinner) findViewById(R.id.playerCoicheSpinner3);
        Spinner playerChoiceSpinner5 = (Spinner) findViewById(R.id.playerCoicheSpinner2);
        Spinner playerChoiceSpinner6 = (Spinner) findViewById(R.id.playerCoicheSpinner1);

        for(int i=0; i<6; i++){

            adapters.add(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,players));
            adapters.get(i).setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        }

        playerChoiceSpinner1.setAdapter(adapters.get(5));
        playerChoiceSpinner2.setAdapter(adapters.get(4));
        playerChoiceSpinner3.setAdapter(adapters.get(3));
        playerChoiceSpinner4.setAdapter(adapters.get(2));
        playerChoiceSpinner5.setAdapter(adapters.get(1));
        playerChoiceSpinner6.setAdapter(adapters.get(0));

        for(int i=0; i<6; i++){
            spinnerListeners.add(new PlayerSpinnerListener(adapters,i));
        }

        playerChoiceSpinner1.setOnItemSelectedListener(spinnerListeners.get(5));
        playerChoiceSpinner2.setOnItemSelectedListener(spinnerListeners.get(4));
        playerChoiceSpinner3.setOnItemSelectedListener(spinnerListeners.get(3));
        playerChoiceSpinner4.setOnItemSelectedListener(spinnerListeners.get(2));
        playerChoiceSpinner5.setOnItemSelectedListener(spinnerListeners.get(1));
        playerChoiceSpinner6.setOnItemSelectedListener(spinnerListeners.get(0));

    }

}
