package com.example.cluedokiler;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.cluedokiler.db.DbManager;
import com.example.cluedokiler.dialogs.CodeAlert;
import com.example.cluedokiler.dialogs.ExitGameAlert;
import com.example.cluedokiler.gameInstance.GameStatus;
import com.example.cluedokiler.gameTypes.GameSelectorActivity;
import com.example.cluedokiler.models.GameNames;
import com.example.cluedokiler.parameters.Parameters;
import com.example.cluedokiler.profile.ProfileActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

import static com.example.cluedokiler.parameters.Parameters.MyPREFERENCES;

public class MainActivity extends AppCompatActivity {
    Button playButton;
    Button resetButton;
    Button statisticsButton;
    Button multiPlayerCodeButton;
    SharedPreferences preferences;
    GameStatus gameStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setElevation(Float.valueOf(100));

        setColors();

        compileSpinners();

        setProfile();

        resetButton = (Button) findViewById(R.id.resetButton);
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

        playButton = (Button) findViewById(R.id.playButton);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameStatus = GameStatus.getInstance();
                gameStatus.tentativePlayers.set(0,GameStatus.getInstance().playerName);

                if(!gameStatus.playersSet) {

                    for (int i = 0; i < 6; i++)
                        if(!gameStatus.tentativePlayers.get(i).equals("--Vuoto--") && !gameStatus.tentativePlayers.get(i).equals(""))
                            if(!gameStatus.playersSet)
                                gameStatus.playersNames.add(gameStatus.tentativePlayers.get(i));
                    if( ! (gameStatus.playersNames.size() > 2))
                        gameStatus.playersNames.clear();
                    else
                        gameStatus.playersSet = true;
                }

                if(gameStatus.playersNames.size() > 2 && !gameStatus.playerName.equals("--Vuoto--")){
                    GameStatus.getInstance().gameTime = java.util.Calendar.getInstance().getTime().toString();
                    initializeGame();

                }else {
                    if(gameStatus.tableSet){
                        Intent startGameIntent = new Intent(getApplicationContext(), GameActivity.class);
                        startActivity(startGameIntent);

                    } else if(gameStatus.playersNames.size() <=2) {
                        Toast toast = Toast.makeText(getApplicationContext(), "Devono esserci almeno 3 giocatori!", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.BOTTOM, 0, 20);
                        toast.show();
                    }else{
                        Toast toast = Toast.makeText(getApplicationContext(), "Inserisci il tuo nome prima", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.BOTTOM, 0, 20);
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

        setMultiPlayerCode();

        setProfile();

        OnBackPressedCallback callback = new OnBackPressedCallback(true ) {
            @Override
            public void handleOnBackPressed() {
                ExitGameAlert exitAlert = new ExitGameAlert();
                exitAlert.show(getSupportFragmentManager(), "exitGameAlert");
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    private void checkStartGame() {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        final ArrayList<String> players = GameStatus.getInstance().playersNames;
        final long code = GameStatus.getInstance().multiPlayerCode;

        DatabaseReference dbb = FirebaseDatabase.getInstance().getReference();
        dbb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (String player : players) {
                    DataSnapshot snap = dataSnapshot.child(player).child(String.valueOf(code));
                    if (snap.exists()) {
                        if (!(((ArrayList<String>) snap.child("Players").getValue()).size() == players.size())) {
                            startGameError();
                            return;
                        }
                        for (String name : (ArrayList<String>) snap.child("Players").getValue()) {
                            if (!players.contains(name)) {
                                startGameError();
                                return;
                            }
                        }
                    }
                }
                startGame();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    private void initializeGame(){
        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {
                    checkStartGame();
                } else {
                    Toast.makeText(MainActivity.super.getBaseContext(), "Errore di connessione, la partita verrà avviata in locale", Toast.LENGTH_SHORT).show();
                    startGame();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void startGameError(){
        gameStatus.playersNames.clear();
        gameStatus.playersSet = false;
        Toast.makeText(this,"Errore nel codice della partita",Toast.LENGTH_SHORT).show();
    }

    private void startGame(){
        if(!gameStatus.tableSet) {
            if(gameStatus.gameNames == null) {
                GameNames gameNames = new GameNames();
                gameNames.setSuspects(new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.suspects))));
                gameNames.setWeapons(new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.weapons))));
                gameNames.setPlaces(new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.places))));
                GameStatus.getInstance().gameNames = gameNames;
            }
            gameStatus.gameTableHash.setPlayer(gameStatus.playerName);
            DbManager.getInstance().saveMultiPlayerCode();
            DbManager.getInstance().savePlayersRecord();
        }

        Intent startGameIntent = new Intent(getApplicationContext(), GameActivity.class);
        startActivity(startGameIntent);
    }

    private void setColors(){
        multiPlayerCodeButton = findViewById(R.id.insertMultiCodeButton);
        statisticsButton = (Button) findViewById(R.id.statisticsButton);
        playButton = (Button) findViewById(R.id.playButton);
        resetButton = (Button) findViewById(R.id.resetButton);
        ConstraintLayout title = findViewById(R.id.titleMainActivity);
        LinearLayout background = findViewById(R.id.layoutMainActivity);

        if(GameStatus.getInstance().theme.equals(Parameters.PURPLE)){
            multiPlayerCodeButton.setBackgroundResource(R.drawable.button_background);
            playButton.setBackgroundResource(R.drawable.button_background);
            resetButton.setBackgroundResource(R.drawable.button_background);
            statisticsButton.setBackgroundResource(R.drawable.button_background);
            title.setBackgroundColor(Parameters.PURPLE_MAIN_COLOR);
            background.setBackgroundResource(R.drawable.screen_background);
        }else if(GameStatus.getInstance().theme.equals(Parameters.GREEN)){
            multiPlayerCodeButton.setBackgroundResource(R.drawable.button_background_green);
            playButton.setBackgroundResource(R.drawable.button_background_green);
            resetButton.setBackgroundResource(R.drawable.button_background_green);
            statisticsButton.setBackgroundResource(R.drawable.button_background_green);
            title.setBackgroundColor(Parameters.GREEN_MAIN_COLOR);
            background.setBackgroundResource(R.drawable.screen_background_green);
        }else if(GameStatus.getInstance().theme.equals(Parameters.ORANGE)){
            multiPlayerCodeButton.setBackgroundResource(R.drawable.button_background_orange);
            playButton.setBackgroundResource(R.drawable.button_background_orange);
            resetButton.setBackgroundResource(R.drawable.button_background_orange);
            statisticsButton.setBackgroundResource(R.drawable.button_background_orange);
            title.setBackgroundColor(Parameters.ORANGE_MAIN_COLOR);
            background.setBackgroundResource(R.drawable.screen_background_orange);
        }else if(GameStatus.getInstance().theme.equals(Parameters.BW)){
            multiPlayerCodeButton.setBackgroundResource(R.drawable.button_background_bw);
            playButton.setBackgroundResource(R.drawable.button_background_bw);
            resetButton.setBackgroundResource(R.drawable.button_background_bw);
            statisticsButton.setBackgroundResource(R.drawable.button_background_bw);
            title.setBackgroundColor(Parameters.BW_MAIN_COLOR);
            background.setBackgroundResource(R.drawable.screen_background_bw);
        }else if(GameStatus.getInstance().theme.equals(Parameters.WB)){
            multiPlayerCodeButton.setBackgroundResource(R.drawable.button_background_wb);
            multiPlayerCodeButton.setTextColor(Parameters.WB_MAIN_TEXT_COLOR);
            playButton.setBackgroundResource(R.drawable.button_background_wb);
            playButton.setTextColor(Parameters.WB_MAIN_TEXT_COLOR);
            resetButton.setBackgroundResource(R.drawable.button_background_wb);
            resetButton.setTextColor(Parameters.WB_MAIN_TEXT_COLOR);
            statisticsButton.setBackgroundResource(R.drawable.button_background_wb);
            statisticsButton.setTextColor(Parameters.WB_MAIN_TEXT_COLOR);
            title.setBackgroundColor(Parameters.WB_MAIN_COLOR);
            background.setBackgroundResource(R.drawable.screen_background_wb);
        }
        preferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("color", GameStatus.getInstance().theme);
        editor.apply();
    }

    private void compileSpinners() {
        final ArrayList<String> players = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.players)));
        if(players.contains(GameStatus.getInstance().playerName) && !GameStatus.getInstance().playerName.equals("--Vuoto--"))
            players.remove(GameStatus.getInstance().playerName);

        final Spinner playerChoiceSpinner2 = (Spinner) findViewById(R.id.playerCoicheSpinner2);
        final Spinner playerChoiceSpinner3 = (Spinner) findViewById(R.id.playerCoicheSpinner3);
        final Spinner playerChoiceSpinner4 = (Spinner) findViewById(R.id.playerCoicheSpinner4);
        final Spinner playerChoiceSpinner5 = (Spinner) findViewById(R.id.playerCoicheSpinner5);
        final Spinner playerChoiceSpinner6 = (Spinner) findViewById(R.id.playerCoicheSpinner6);

        final PlayersAdapter playersAdapter = new PlayersAdapter(this,players);

        playerChoiceSpinner2.setAdapter(playersAdapter);
        playerChoiceSpinner2.setDropDownVerticalOffset(100);
        playerChoiceSpinner3.setAdapter(playersAdapter);
        playerChoiceSpinner3.setDropDownVerticalOffset(100);
        playerChoiceSpinner4.setAdapter(playersAdapter);
        playerChoiceSpinner4.setDropDownVerticalOffset(100);
        playerChoiceSpinner5.setAdapter(playersAdapter);
        playerChoiceSpinner5.setDropDownVerticalOffset(100);
        playerChoiceSpinner6.setAdapter(playersAdapter);
        playerChoiceSpinner6.setDropDownVerticalOffset(100);

        int pos1 = players.indexOf(GameStatus.getInstance().tentativePlayers.get(1));
        playerChoiceSpinner2.setSelection(pos1);
        int pos2 = players.indexOf(GameStatus.getInstance().tentativePlayers.get(2));
        playerChoiceSpinner3.setSelection(pos2);
        int pos3 = players.indexOf(GameStatus.getInstance().tentativePlayers.get(3));
        playerChoiceSpinner4.setSelection(pos3);
        int pos4 = players.indexOf(GameStatus.getInstance().tentativePlayers.get(4));
        playerChoiceSpinner5.setSelection(pos4);
        int pos5 = players.indexOf(GameStatus.getInstance().tentativePlayers.get(5));
        playerChoiceSpinner6.setSelection(pos5);

        playerChoiceSpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String playerChoice;
                playerChoice = parent.getItemAtPosition(position).toString();

                if((!GameStatus.getInstance().tentativePlayers.contains(playerChoice) || GameStatus.getInstance().tentativePlayers.indexOf(playerChoice) == 1) || playerChoice.equals("--Vuoto--")) {
                    GameStatus.getInstance().tentativePlayers.set(1,playerChoice);

                }
                else {
                    Toast toast = Toast.makeText(MainActivity.super.getApplicationContext(), "Nome già selezionato", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM, 0, 20);
                    toast.show();
                    int pos = players.indexOf(GameStatus.getInstance().tentativePlayers.get(1));
                    playerChoiceSpinner2.setSelection(pos);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        playerChoiceSpinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String playerChoice;
                playerChoice = parent.getItemAtPosition(position).toString();

                if((!GameStatus.getInstance().tentativePlayers.contains(playerChoice) || GameStatus.getInstance().tentativePlayers.indexOf(playerChoice) == 2) || playerChoice.equals("--Vuoto--")) {
                    GameStatus.getInstance().tentativePlayers.set(2,playerChoice);
                }
                else {
                    Toast toast = Toast.makeText(MainActivity.super.getApplicationContext(), "Nome già selezionato", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM, 0, 20);
                    toast.show();
                    int pos = players.indexOf(GameStatus.getInstance().tentativePlayers.get(2));
                    playerChoiceSpinner3.setSelection(pos);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        playerChoiceSpinner4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String playerChoice;
                playerChoice = parent.getItemAtPosition(position).toString();

                if((!GameStatus.getInstance().tentativePlayers.contains(playerChoice) || GameStatus.getInstance().tentativePlayers.indexOf(playerChoice) == 3) || playerChoice.equals("--Vuoto--")) {
                    GameStatus.getInstance().tentativePlayers.set(3,playerChoice);
                }
                else {
                    Toast toast = Toast.makeText(MainActivity.super.getApplicationContext(), "Nome già selezionato", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM, 0, 20);
                    toast.show();
                    int pos = players.indexOf(GameStatus.getInstance().tentativePlayers.get(3));
                    playerChoiceSpinner4.setSelection(pos);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        playerChoiceSpinner5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String playerChoice;
                playerChoice = parent.getItemAtPosition(position).toString();

                if((!GameStatus.getInstance().tentativePlayers.contains(playerChoice) || GameStatus.getInstance().tentativePlayers.indexOf(playerChoice) == 4) || playerChoice.equals("--Vuoto--")) {
                    GameStatus.getInstance().tentativePlayers.set(4,playerChoice);
                }
                else {
                    Toast toast = Toast.makeText(MainActivity.super.getApplicationContext(), "Nome già selezionato", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM, 0, 20);
                    toast.show();
                    int pos = players.indexOf(GameStatus.getInstance().tentativePlayers.get(4));
                    playerChoiceSpinner5.setSelection(pos);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        playerChoiceSpinner6.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String playerChoice;
                playerChoice = parent.getItemAtPosition(position).toString();

                if((!GameStatus.getInstance().tentativePlayers.contains(playerChoice) || GameStatus.getInstance().tentativePlayers.indexOf(playerChoice) == 5) || playerChoice.equals("--Vuoto--")) {
                    GameStatus.getInstance().tentativePlayers.set(5,playerChoice);
                }
                else {
                    Toast toast = Toast.makeText(MainActivity.super.getApplicationContext(), "Nome già selezionato", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM, 0, 20);
                    toast.show();
                    int pos = players.indexOf(GameStatus.getInstance().tentativePlayers.get(5));
                    playerChoiceSpinner6.setSelection(pos);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
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
                    toast.setGravity(Gravity.BOTTOM, 0, 20);
                    toast.show();
                }
            }
        });

    }

    private void setMultiPlayerCode(){
        Button insertCodeButton = findViewById(R.id.insertMultiCodeButton);
        final TextView getCodeText = findViewById(R.id.generateMultiCodeText);
        final EditText setCodeText = findViewById(R.id.insertMultiCodeText);

        setCodeText.setText("");

        if(GameStatus.getInstance().multiPlayerCode == 0) {
            long time = java.util.Calendar.getInstance().getTime().getTime();
            getCodeText.setText("Codice: " + String.valueOf(time));
            GameStatus.getInstance().multiPlayerCode = time;
        }

        insertCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(setCodeText.getText().toString().equals("")){
                    Toast toast = Toast.makeText(MainActivity.super.getApplicationContext(), "Nessun codice inserito", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM, 0, 20);
                    toast.show();
                }else {
                    Toast toast = Toast.makeText(MainActivity.super.getApplicationContext(), "Codice inserito correttamente", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM, 0, 20);
                    toast.show();
                    GameStatus.getInstance().multiPlayerCode = Long.parseLong(setCodeText.getText().toString());
                    getCodeText.setText("Codice: " + String.valueOf(GameStatus.getInstance().multiPlayerCode));
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

    private void setProfile(){
        TextView profileTextView = findViewById(R.id.profileTextView);
        profileTextView.setText(GameStatus.getInstance().playerName);

        profileTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profile = new Intent(getApplicationContext(), ProfileActivity.class);
                profile.putExtra("name",GameStatus.getInstance().playerName);
                startActivity(profile);
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        final Button playButton = (Button) findViewById(R.id.playButton);

        setProfile();

        setColors();

        compileSpinners();

        setMultiPlayerCode();

        if(GameStatus.getInstance().playersSet)
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
            case R.id.mainMenu3:
                Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
                loginIntent.putExtra("firstLogin",false);
                startActivity(loginIntent);
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
            case R.id.mainMenu24:
                GameStatus.getInstance().theme = Parameters.BW;
                setColors();
                return true;
            case R.id.mainMenu4:
                Intent gameSelector = new Intent(getApplicationContext(), GameSelectorActivity.class);
                startActivity(gameSelector);
                GameStatus.getInstance().newGame();
                finish();
                return true;
            default:super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }
}