package com.example.cluedokiler;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity  {

    ArrayList<PlayerSpinnerListener> spinnerListeners = new ArrayList<>();
    ArrayList<ArrayList<String>> gameTable = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        compileSpinners();


        Button playButton = (Button) findViewById(R.id.playButton);
        Button resetButton = (Button) findViewById(R.id.resetButton);

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameStatus gameStatus = GameStatus.getInstance();
                gameStatus.alreadySet = false;
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> playersChoices = new ArrayList<>();

                for(int i=0; i<6; i++){
                    if(!spinnerListeners.get(i).getPlayerChoice().equals(" ") && !spinnerListeners.get(i).getPlayerChoice().equals(""))
                        playersChoices.add(spinnerListeners.get(i).getPlayerChoice());
                }


                if(playersChoices.size() >= 2){
                    Intent startGameIntent = new Intent(getApplicationContext(), GameActivity.class);
                    startGameIntent.putExtra("com.example.cluedokiller.players", playersChoices);
                    startActivity(startGameIntent);
                }


            }
        });
    }


    private void compileSpinners() {
        Spinner playerChoiceSpinner1 = (Spinner) findViewById(R.id.playerCoicheSpinner6);
        Spinner playerChoiceSpinner2 = (Spinner) findViewById(R.id.playerCoicheSpinner5);
        Spinner playerChoiceSpinner3 = (Spinner) findViewById(R.id.playerCoicheSpinner4);
        Spinner playerChoiceSpinner4 = (Spinner) findViewById(R.id.playerCoicheSpinner3);
        Spinner playerChoiceSpinner5 = (Spinner) findViewById(R.id.playerCoicheSpinner2);
        Spinner playerChoiceSpinner6 = (Spinner) findViewById(R.id.playerCoicheSpinner1);

        ArrayAdapter<CharSequence> playerChoiceAdapter = ArrayAdapter.createFromResource(this, R.array.players, android.R.layout.simple_spinner_item);
        playerChoiceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        playerChoiceSpinner1.setAdapter(playerChoiceAdapter);
        playerChoiceSpinner2.setAdapter(playerChoiceAdapter);
        playerChoiceSpinner3.setAdapter(playerChoiceAdapter);
        playerChoiceSpinner4.setAdapter(playerChoiceAdapter);
        playerChoiceSpinner5.setAdapter(playerChoiceAdapter);
        playerChoiceSpinner6.setAdapter(playerChoiceAdapter);

        for(int i=0; i<6; i++){
            spinnerListeners.add(new PlayerSpinnerListener());
        }

        playerChoiceSpinner1.setOnItemSelectedListener(spinnerListeners.get(0));
        playerChoiceSpinner2.setOnItemSelectedListener(spinnerListeners.get(1));
        playerChoiceSpinner3.setOnItemSelectedListener(spinnerListeners.get(2));
        playerChoiceSpinner4.setOnItemSelectedListener(spinnerListeners.get(3));
        playerChoiceSpinner5.setOnItemSelectedListener(spinnerListeners.get(4));
        playerChoiceSpinner6.setOnItemSelectedListener(spinnerListeners.get(5));
    }

}
