package com.example.cluedokiler;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    ArrayList<PlayerSpinnerListener> spinnerListeners = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setElevation(Float.valueOf(100));

        compileSpinners();

        final Button playButton = (Button) findViewById(R.id.playButton);
        Button resetButton = (Button) findViewById(R.id.resetButton);
        playButton.setElevation(Float.valueOf(10));
        playButton.setTranslationZ(Float.valueOf(10));
        resetButton.setElevation(Float.valueOf(100));

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameStatus.getInstance().newGame();
                onResume();
            }
        });

        if(GameStatus.getInstance().playersSet)
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
                       // if (!spinnerListeners.get(i).getPlayerChoice().equals("--Vuoto--") && !spinnerListeners.get(i).getPlayerChoice().equals(""))
                         //   if (!gameStatus.playersSet)
                           //     gameStatus.playersNames.add(spinnerListeners.get(i).getPlayerChoice());
                        if(!gameStatus.tentativePlayers.get(i).equals("--Vuoto--") && !gameStatus.tentativePlayers.get(i).equals(""))
                            if(!gameStatus.playersSet)
                                gameStatus.playersNames.add(gameStatus.tentativePlayers.get(i));
                    }

                    if( ! (gameStatus.playersNames.size() > 2))
                        gameStatus.playersNames.clear();
                    else
                        gameStatus.playersSet = true;
                }

                if(gameStatus.playersNames.size() > 2 && !gameStatus.playerName.equals("--Vuoto--")){
                    Intent startGameIntent = new Intent(getApplicationContext(), GameActivity.class);
                    startActivity(startGameIntent);

                    if(!gameStatus.tableSet) {
                        GameStatus.getInstance().gameTime = java.util.Calendar.getInstance().getTime().toString();
                        gameStatus.gameTableHash.setPlayer(gameStatus.playerName);
                        DbManager.getInstance().savePlayersRecord();
                    }

                }else {
                    if(gameStatus.tableSet){
                        Intent startGameIntent = new Intent(getApplicationContext(), GameActivity.class);
                        startActivity(startGameIntent);

                    } else if(gameStatus.playersNames.size() <=2) {
                        Toast toast = Toast.makeText(getApplicationContext(), "Devono esserci almeno 3 giocatori!", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.BOTTOM|Gravity.FILL_HORIZONTAL, 0, 20);
                        toast.show();
                    }else{
                        Toast toast = Toast.makeText(getApplicationContext(), "Inserisci il tuo nome prima", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.BOTTOM|Gravity.FILL_HORIZONTAL, 0, 20);
                        toast.show();
                    }
                }

                if(gameStatus.playersSet)
                    playButton.setText(getResources().getString(R.string.riprendiPlayButton));
                else
                    playButton.setText(getResources().getString(R.string.giocaPlayButton));
            }
        });

        setStatisticsButton();

        setTopBarButtons();

        OnBackPressedCallback callback = new OnBackPressedCallback(true ) {
            @Override
            public void handleOnBackPressed() {
                ExitGameAlert exitAlert = new ExitGameAlert();
                exitAlert.show(getSupportFragmentManager(), "exitGameAlert");
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }



    private void compileSpinners() {
        ArrayList<String> players = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.players)));
        final ArrayList<ArrayAdapter<String>> adapters = new ArrayList<>();

        final Spinner playerChoiceSpinner1 = (Spinner) findViewById(R.id.playerCoicheSpinner1);
        final Spinner playerChoiceSpinner2 = (Spinner) findViewById(R.id.playerCoicheSpinner2);
        final Spinner playerChoiceSpinner3 = (Spinner) findViewById(R.id.playerCoicheSpinner3);
        final Spinner playerChoiceSpinner4 = (Spinner) findViewById(R.id.playerCoicheSpinner4);
        final Spinner playerChoiceSpinner5 = (Spinner) findViewById(R.id.playerCoicheSpinner5);
        final Spinner playerChoiceSpinner6 = (Spinner) findViewById(R.id.playerCoicheSpinner6);

        for(int i=0; i<6; i++){

            adapters.add(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,players));
           // if(i!=0 || GameStatus.getInstance().playerName.equals("--Vuoto--") || GameStatus.getInstance().playerName.equals(""))
                adapters.get(i).setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        }

        //if(GameStatus.getInstance().playerName.equals("--Vuoto--"))
            playerChoiceSpinner1.setAdapter(adapters.get(0));
        playerChoiceSpinner2.setAdapter(adapters.get(1));
        playerChoiceSpinner3.setAdapter(adapters.get(2));
        playerChoiceSpinner4.setAdapter(adapters.get(3));
        playerChoiceSpinner5.setAdapter(adapters.get(4));
        playerChoiceSpinner6.setAdapter(adapters.get(5));

      /*  for(int i=0; i<6; i++){
            spinnerListeners.add(new PlayerSpinnerListener(adapters,i));
        }

        //if(GameStatus.getInstance().playerName.equals("--Vuoto--"))
            playerChoiceSpinner1.setOnItemSelectedListener(spinnerListeners.get(0));
        playerChoiceSpinner2.setOnItemSelectedListener(spinnerListeners.get(1));
        playerChoiceSpinner3.setOnItemSelectedListener(spinnerListeners.get(2));
        playerChoiceSpinner4.setOnItemSelectedListener(spinnerListeners.get(3));
        playerChoiceSpinner5.setOnItemSelectedListener(spinnerListeners.get(4));
        playerChoiceSpinner6.setOnItemSelectedListener(spinnerListeners.get(5));*/

        playerChoiceSpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String playerChoice;
                playerChoice = parent.getItemAtPosition(position).toString();

                if(!GameStatus.getInstance().tentativePlayers.contains(playerChoice) || playerChoice.equals("--Vuoto--")) {
                    GameStatus.getInstance().playerName = playerChoice;
                    GameStatus.getInstance().tentativePlayers.set(0,playerChoice);
                }
                else {
                    Toast.makeText(MainActivity.super.getApplicationContext(), "Nome già selezionato", Toast.LENGTH_SHORT).show();
                    playerChoiceSpinner1.setAdapter(adapters.get(0));
                }

                /*
                if(!playerChoice.equals("--Vuoto--")) {
                    if (!GameStatus.getInstance().tentativePlayers.get(0).equals("") && !GameStatus.getInstance().tentativePlayers.get(0).equals("--Vuoto--"))
                        for (ArrayAdapter arrayAdapter : adapters)
                            if(!arrayAdapter.equals(adapters.get(0)))
                                arrayAdapter.add(GameStatus.getInstance().tentativePlayers.get(0));

                    for (ArrayAdapter arrayAdapter : adapters) {
                        try {
                            if(!arrayAdapter.equals(adapters.get(0)))
                                arrayAdapter.remove(playerChoice);
                        } catch (Exception e) {
                        }
                    }
                }*/


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        playerChoiceSpinner1.setElevation(Float.valueOf(10));
        playerChoiceSpinner1.setTranslationZ(Float.valueOf(10));

        playerChoiceSpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String playerChoice;
                playerChoice = parent.getItemAtPosition(position).toString();

                if(!GameStatus.getInstance().tentativePlayers.contains(playerChoice) || playerChoice.equals("--Vuoto--")) {
                    GameStatus.getInstance().tentativePlayers.set(1,playerChoice);
                }
                else {
                    Toast.makeText(MainActivity.super.getApplicationContext(), "Nome già selezionato", Toast.LENGTH_SHORT).show();
                    playerChoiceSpinner2.setAdapter(adapters.get(1));
                }
                /*
                if(!playerChoice.equals("--Vuoto--")) {
                    if (!GameStatus.getInstance().tentativePlayers.get(1).equals("") && !GameStatus.getInstance().tentativePlayers.get(1).equals("--Vuoto--"))
                        for (ArrayAdapter arrayAdapter : adapters)
                            if(!arrayAdapter.equals(adapters.get(1)))
                                arrayAdapter.add(GameStatus.getInstance().tentativePlayers.get(1));

                        for (ArrayAdapter arrayAdapter : adapters) {
                            try {
                                if(!arrayAdapter.equals(adapters.get(1)))
                                    arrayAdapter.remove(playerChoice);
                            } catch (Exception e) {
                            }
                        }
                }

                GameStatus.getInstance().tentativePlayers.set(1,playerChoice);*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
        playerChoiceSpinner2.setElevation(Float.valueOf(10));
        playerChoiceSpinner2.setTranslationZ(Float.valueOf(10));

        playerChoiceSpinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String playerChoice;
                playerChoice = parent.getItemAtPosition(position).toString();

                if(!GameStatus.getInstance().tentativePlayers.contains(playerChoice) || playerChoice.equals("--Vuoto--")) {
                    GameStatus.getInstance().tentativePlayers.set(2,playerChoice);
                }
                else {
                    Toast.makeText(MainActivity.super.getApplicationContext(), "Nome già selezionato", Toast.LENGTH_SHORT).show();
                    playerChoiceSpinner3.setAdapter(adapters.get(2));
                }
                /*if(!playerChoice.equals("--Vuoto--")) {
                    if (!GameStatus.getInstance().tentativePlayers.get(2).equals("") && !GameStatus.getInstance().tentativePlayers.get(2).equals("--Vuoto--"))
                        for (ArrayAdapter arrayAdapter : adapters)
                            if(!arrayAdapter.equals(adapters.get(2)))
                                arrayAdapter.add(GameStatus.getInstance().tentativePlayers.get(2));

                    for (ArrayAdapter arrayAdapter : adapters) {
                        try {
                            if(!arrayAdapter.equals(adapters.get(2)))
                                arrayAdapter.remove(playerChoice);
                        } catch (Exception e) {
                        }
                    }
                }

                GameStatus.getInstance().tentativePlayers.set(1,playerChoice);*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
        playerChoiceSpinner3.setElevation(Float.valueOf(10));
        playerChoiceSpinner3.setTranslationZ(Float.valueOf(10));

        playerChoiceSpinner4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String playerChoice;
                playerChoice = parent.getItemAtPosition(position).toString();


                if(!GameStatus.getInstance().tentativePlayers.contains(playerChoice) || playerChoice.equals("--Vuoto--")) {
                    GameStatus.getInstance().tentativePlayers.set(3,playerChoice);
                }
                else {
                    Toast.makeText(MainActivity.super.getApplicationContext(), "Nome già selezionato", Toast.LENGTH_SHORT).show();
                    playerChoiceSpinner4.setAdapter(adapters.get(3));
                }
                /*
                if(!playerChoice.equals("--Vuoto--")) {
                    if (!GameStatus.getInstance().tentativePlayers.get(3).equals("") && !GameStatus.getInstance().tentativePlayers.get(3).equals("--Vuoto--"))
                        for (ArrayAdapter arrayAdapter : adapters)
                            if(!arrayAdapter.equals(adapters.get(3)))
                                arrayAdapter.add(GameStatus.getInstance().tentativePlayers.get(3));

                    for (ArrayAdapter arrayAdapter : adapters) {
                        try {
                            if(!arrayAdapter.equals(adapters.get(3)))
                                arrayAdapter.remove(playerChoice);
                        } catch (Exception e) {
                        }
                    }
                }

                GameStatus.getInstance().tentativePlayers.set(3,playerChoice);*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
        playerChoiceSpinner4.setElevation(Float.valueOf(10));
        playerChoiceSpinner4.setTranslationZ(Float.valueOf(10));

        playerChoiceSpinner5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String playerChoice;
                playerChoice = parent.getItemAtPosition(position).toString();

                if(!GameStatus.getInstance().tentativePlayers.contains(playerChoice) || playerChoice.equals("--Vuoto--")) {
                    GameStatus.getInstance().tentativePlayers.set(4,playerChoice);
                }
                else {
                    Toast.makeText(MainActivity.super.getApplicationContext(), "Nome già selezionato", Toast.LENGTH_SHORT).show();
                    playerChoiceSpinner5.setAdapter(adapters.get(4));
                }
                /*
                if(!playerChoice.equals("--Vuoto--")) {
                    if (!GameStatus.getInstance().tentativePlayers.get(4).equals("") && !GameStatus.getInstance().tentativePlayers.get(4).equals("--Vuoto--"))
                        for (ArrayAdapter arrayAdapter : adapters)
                            if(!arrayAdapter.equals(adapters.get(4)))
                                arrayAdapter.add(GameStatus.getInstance().tentativePlayers.get(4));

                    for (ArrayAdapter arrayAdapter : adapters) {
                        try {
                            if(!arrayAdapter.equals(adapters.get(4)))
                                arrayAdapter.remove(playerChoice);
                        } catch (Exception e) {
                        }
                    }
                }

                GameStatus.getInstance().tentativePlayers.set(4,playerChoice);*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
        playerChoiceSpinner5.setElevation(Float.valueOf(10));
        playerChoiceSpinner5.setTranslationZ(Float.valueOf(10));

        playerChoiceSpinner6.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String playerChoice;
                playerChoice = parent.getItemAtPosition(position).toString();

                if(!GameStatus.getInstance().tentativePlayers.contains(playerChoice) || playerChoice.equals("--Vuoto--")) {
                    GameStatus.getInstance().tentativePlayers.set(5,playerChoice);
                }
                else {
                    Toast.makeText(MainActivity.super.getApplicationContext(), "Nome già selezionato", Toast.LENGTH_SHORT).show();
                    playerChoiceSpinner6.setAdapter(adapters.get(5));
                }
                /*
                if(!playerChoice.equals("--Vuoto--")) {
                    if (!GameStatus.getInstance().tentativePlayers.get(5).equals("") && !GameStatus.getInstance().tentativePlayers.get(5).equals("--Vuoto--"))
                        for (ArrayAdapter arrayAdapter : adapters)
                            if(!arrayAdapter.equals(adapters.get(5)))
                                arrayAdapter.add(GameStatus.getInstance().tentativePlayers.get(5));

                    for (ArrayAdapter arrayAdapter : adapters) {
                        try {
                            if(!arrayAdapter.equals(adapters.get(5)))
                                arrayAdapter.remove(playerChoice);
                        } catch (Exception e) {
                        }
                    }
                }

                GameStatus.getInstance().tentativePlayers.set(1,playerChoice);*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
        playerChoiceSpinner6.setElevation(Float.valueOf(10));
        playerChoiceSpinner6.setTranslationZ(Float.valueOf(10));
    }



    public void setStatisticsButton(){
        Button statisticsButton = (Button) findViewById(R.id.statisticsButton);

        statisticsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameStatus gameStatus = GameStatus.getInstance();
                if(!gameStatus.playerName.equals("--Vuoto--")) {
                    Intent startGameIntent = new Intent(getApplicationContext(), StatisticsActivity.class);
                    startActivity(startGameIntent);
                }else{
                    Toast toast = Toast.makeText(getApplicationContext(), "Inserisci il tuo nome prima", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });

    }

    private void setTopBarButtons(){
        ImageView exitApp = (ImageView) findViewById(R.id.exitAppImageVIew);

        exitApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExitGameAlert exitAlert = new ExitGameAlert();
                exitAlert.show(getSupportFragmentManager(), "exitGameAlert");
            }
        });
    }



    @Override
    protected void onResume(){
        super.onResume();
        final GameStatus gameStatus = GameStatus.getInstance();
        final Button playButton = (Button) findViewById(R.id.playButton);

        compileSpinners();

        if(gameStatus.playersSet)
            playButton.setText(getResources().getString(R.string.riprendiPlayButton));
        else
            playButton.setText(getResources().getString(R.string.giocaPlayButton));

    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        final GameStatus gameStatus = GameStatus.getInstance();

        if(gameStatus.tableSet) {
            DbManager.getInstance().saveGameRecord();
            gameStatus.newGame();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.mainMenu1:
                CodeAlert codeAlert = new CodeAlert();
                codeAlert.show(getSupportFragmentManager(), "codeAlert");
                return true;
            default:super.onOptionsItemSelected(item);
        }

        return super.onOptionsItemSelected(item);
    }
}
