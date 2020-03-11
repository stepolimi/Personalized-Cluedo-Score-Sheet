package com.example.cluedokiler;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    ArrayList<PlayerSpinnerListener> spinnerListeners = new ArrayList<>();
    DatabaseReference db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final GameStatus gameStatus = GameStatus.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        compileSpinners();


        final Button playButton = (Button) findViewById(R.id.playButton);
        Button resetButton = (Button) findViewById(R.id.resetButton);

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameStatus.getInstance().newGame();
                onResume();
            }
        });

        if(gameStatus.playersSet)
            playButton.setText(getResources().getString(R.string.riprendiPlayButton));
        else
            playButton.setText(getResources().getString(R.string.giocaPlayButton));



        db = FirebaseDatabase.getInstance().getReference().child("PlayersRecord");

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


                if(gameStatus.playersNames.size() > 2 && !gameStatus.playerName.equals("--Vuoto--")){
                    Intent startGameIntent = new Intent(getApplicationContext(), GameActivity.class);
                    startActivity(startGameIntent);


                    if(!gameStatus.tableSet) {
                        gameStatus.gameTableHash.setPlayer(gameStatus.playerName);
                        Users users = new Users(gameStatus.playersNames);
                        db.child(String.valueOf(gameStatus.gameNumber + 1)).setValue(users);
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
        ArrayList<ArrayAdapter<String>> adapters = new ArrayList<>();

        Spinner playerChoiceSpinner1 = (Spinner) findViewById(R.id.playerCoicheSpinner1);
        Spinner playerChoiceSpinner2 = (Spinner) findViewById(R.id.playerCoicheSpinner2);
        Spinner playerChoiceSpinner3 = (Spinner) findViewById(R.id.playerCoicheSpinner3);
        Spinner playerChoiceSpinner4 = (Spinner) findViewById(R.id.playerCoicheSpinner4);
        Spinner playerChoiceSpinner5 = (Spinner) findViewById(R.id.playerCoicheSpinner5);
        Spinner playerChoiceSpinner6 = (Spinner) findViewById(R.id.playerCoicheSpinner6);

        for(int i=0; i<6; i++){

            adapters.add(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,players));
            if(i!=0 || GameStatus.getInstance().playerName.equals("--Vuoto--") || GameStatus.getInstance().playerName.equals(""))
                adapters.get(i).setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        }

        if(GameStatus.getInstance().playerName.equals("--Vuoto--"))
            playerChoiceSpinner1.setAdapter(adapters.get(0));
        playerChoiceSpinner2.setAdapter(adapters.get(1));
        playerChoiceSpinner3.setAdapter(adapters.get(2));
        playerChoiceSpinner4.setAdapter(adapters.get(3));
        playerChoiceSpinner5.setAdapter(adapters.get(4));
        playerChoiceSpinner6.setAdapter(adapters.get(5));

        for(int i=0; i<6; i++){
            spinnerListeners.add(new PlayerSpinnerListener(adapters,i));
        }

        if(GameStatus.getInstance().playerName.equals("--Vuoto--"))
            playerChoiceSpinner1.setOnItemSelectedListener(spinnerListeners.get(0));
        playerChoiceSpinner2.setOnItemSelectedListener(spinnerListeners.get(1));
        playerChoiceSpinner3.setOnItemSelectedListener(spinnerListeners.get(2));
        playerChoiceSpinner4.setOnItemSelectedListener(spinnerListeners.get(3));
        playerChoiceSpinner5.setOnItemSelectedListener(spinnerListeners.get(4));
        playerChoiceSpinner6.setOnItemSelectedListener(spinnerListeners.get(5));

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


        db = FirebaseDatabase.getInstance().getReference().child("PlayersRecord");

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

    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        final GameStatus gameStatus = GameStatus.getInstance();

        if(gameStatus.tableSet) {
            db = FirebaseDatabase.getInstance().getReference().child("PlayersRecord");

            db.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists())
                        gameStatus.gameNumber = (dataSnapshot.getChildrenCount());
                    else
                        gameStatus.gameNumber = Long.valueOf(0);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            db = FirebaseDatabase.getInstance().getReference().child("GameRecord");
            db.child(String.valueOf(gameStatus.gameNumber)).setValue(gameStatus.gameTableHash);
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
            case R.id.mainMenu2:
                Toast.makeText(this,"item3", Toast.LENGTH_SHORT).show();
                return true;
            default:super.onOptionsItemSelected(item);
        }

        return super.onOptionsItemSelected(item);
    }
}
