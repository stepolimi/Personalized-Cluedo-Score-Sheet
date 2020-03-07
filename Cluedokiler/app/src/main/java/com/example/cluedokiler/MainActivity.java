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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GameStatus gameStatus = GameStatus.getInstance();
        compileSpinners();


        // todo: upgrade a bit the quality of imgs

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

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameStatus gameStatus = GameStatus.getInstance();


                if(!gameStatus.playersSet) {
                    gameStatus.playersNames.clear();
                    for (int i = 0; i < 6; i++) {
                        if (!spinnerListeners.get(i).getPlayerChoice().equals("--Vuoto--") && !spinnerListeners.get(i).getPlayerChoice().equals(""))
                            if (!gameStatus.playersSet) {
                                gameStatus.playersNames.add(spinnerListeners.get(i).getPlayerChoice());
                            }
                    }

                    if( ! (gameStatus.playersNames.size() > 2)){
                        gameStatus.playersNames.clear();
                    }else{
                        gameStatus.playersSet = true;
                    }
                }


                if(gameStatus.playersNames.size() > 2){
                    Intent startGameIntent = new Intent(getApplicationContext(), GameActivity.class);
                    startActivity(startGameIntent);
                }

                if(gameStatus.playersSet)
                    playButton.setText(getResources().getString(R.string.riprendiPlayButton));
                else
                    playButton.setText(getResources().getString(R.string.giocaPlayButton));


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
