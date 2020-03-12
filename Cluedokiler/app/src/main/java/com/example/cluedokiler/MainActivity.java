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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    Button playButton;
    Button resetButton;
    Button statisticsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        playButton = (Button) findViewById(R.id.playButton);
        resetButton = (Button) findViewById(R.id.resetButton);

        setSupportActionBar(toolbar);
        toolbar.setElevation(Float.valueOf(100));

        setColors();

        compileSpinners();

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

    private void setColors(){
        statisticsButton = (Button) findViewById(R.id.statisticsButton);
        playButton = (Button) findViewById(R.id.playButton);
        resetButton = (Button) findViewById(R.id.resetButton);
        ConstraintLayout title = findViewById(R.id.titleMainActivity);
        LinearLayout background = findViewById(R.id.layoutMainActivity);

        if(GameStatus.getInstance().theme.equals(Parameters.PURPLE)){
            playButton.setBackgroundResource(R.drawable.button_background);
            resetButton.setBackgroundResource(R.drawable.button_background);
            statisticsButton.setBackgroundResource(R.drawable.button_background);
            title.setBackgroundColor(Parameters.PURPLE_MAIN_COLOR);
            background.setBackgroundResource(R.drawable.screen_background);
        }else if(GameStatus.getInstance().theme.equals(Parameters.GREEN)){
            playButton.setBackgroundResource(R.drawable.button_background_green);
            resetButton.setBackgroundResource(R.drawable.button_background_green);
            statisticsButton.setBackgroundResource(R.drawable.button_background_green);
            title.setBackgroundColor(Parameters.GREEN_MAIN_COLOR);
            background.setBackgroundResource(R.drawable.screen_background_green);
        }else if(GameStatus.getInstance().theme.equals(Parameters.ORANGE)){
            playButton.setBackgroundResource(R.drawable.button_background_orange);
            resetButton.setBackgroundResource(R.drawable.button_background_orange);
            statisticsButton.setBackgroundResource(R.drawable.button_background_orange);
            title.setBackgroundColor(Parameters.ORANGE_MAIN_COLOR);
            background.setBackgroundResource(R.drawable.screen_background_orange);
        }
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
            adapters.get(i).setDropDownViewResource(android.R.layout.simple_spinner_item);
            //adapters.get(i).setDropDownViewResource(R.layout.spinner_drop_down_view);
        }

        playerChoiceSpinner1.setAdapter(adapters.get(0));
        playerChoiceSpinner2.setAdapter(adapters.get(1));
        playerChoiceSpinner3.setAdapter(adapters.get(2));
        playerChoiceSpinner4.setAdapter(adapters.get(3));
        playerChoiceSpinner5.setAdapter(adapters.get(4));
        playerChoiceSpinner6.setAdapter(adapters.get(5));

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
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
        playerChoiceSpinner6.setElevation(Float.valueOf(10));
        playerChoiceSpinner6.setTranslationZ(Float.valueOf(10));
    }



    public void setStatisticsButton(){
        statisticsButton = (Button) findViewById(R.id.statisticsButton);

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

        setColors();

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
            case R.id.mainMenu21:
                GameStatus.getInstance().theme = Parameters.GREEN;
                setColors();
                return true;
            case R.id.mainMenu22:
                GameStatus.getInstance().theme = Parameters.PURPLE;
                setColors();
                return true;
            case R.id.mainMenu23:
                GameStatus.getInstance().theme = Parameters.ORANGE;
                setColors();
                return true;
            default:super.onOptionsItemSelected(item);
        }

        return super.onOptionsItemSelected(item);
    }
}

//todo: persistent player
//todo: damn spinners
