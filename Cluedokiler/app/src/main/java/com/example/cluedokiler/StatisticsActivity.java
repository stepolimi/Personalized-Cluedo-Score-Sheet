package com.example.cluedokiler;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.cluedokiler.dialogs.CodeAlert;
import com.example.cluedokiler.gameInstance.GameStatus;
import com.example.cluedokiler.models.GameTable;
import com.example.cluedokiler.models.Users;
import com.example.cluedokiler.parameters.Parameters;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import static com.example.cluedokiler.parameters.Parameters.MyPREFERENCES;

public class StatisticsActivity extends AppCompatActivity {

    TextView[] playerTextView = new TextView[21];
    DatabaseReference db;
    GameStatus gameStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        gameStatus = GameStatus.getInstance();
        Toolbar toolbar = findViewById(R.id.toolbar);

        setColors();

        setSupportActionBar(toolbar);

        setPlayer();

        setNumGames();

        setNumTicks();

        setNumCross();

        setNumEmpty();

        setUnValidatedVictories();

        setValidatedVictories();

        setMostPlayedPlayerWith();

        setRefreshButton();

        setBestPlayer();

        setBackButton();
    }

    private void setColors(){
        ConstraintLayout title = findViewById(R.id.titleStatisticsActivity);
        LinearLayout background = findViewById(R.id.layoutStatisticsActivity);
        if(GameStatus.getInstance().theme.equals(Parameters.PURPLE)){
            title.setBackgroundColor(Parameters.PURPLE_MAIN_COLOR);
            background.setBackgroundResource(R.drawable.screen_background);
        } else if(GameStatus.getInstance().theme.equals(Parameters.GREEN)){
            title.setBackgroundColor(Parameters.GREEN_MAIN_COLOR);
            background.setBackgroundResource(R.drawable.screen_background_green);
        } else if(GameStatus.getInstance().theme.equals(Parameters.ORANGE)){
            title.setBackgroundColor(Parameters.ORANGE_MAIN_COLOR);
            background.setBackgroundResource(R.drawable.screen_background_orange);
        } else if(GameStatus.getInstance().theme.equals(Parameters.BW)){
            title.setBackgroundColor(Parameters.BW_MAIN_COLOR);
            background.setBackgroundResource(R.drawable.screen_background_bw);
        }
        SharedPreferences preferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("color", GameStatus.getInstance().theme);
        editor.apply();
    }

    private void setBackButton() {
        ImageView backButton = (ImageView) findViewById(R.id.backArrowImageView);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setRefreshButton(){
        ImageView refreshImageView = (ImageView) findViewById(R.id.refreshImageView);
        refreshImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPlayer();
                setNumGames();
                setNumTicks();
                setNumCross();
                setNumEmpty();
                setUnValidatedVictories();
                setValidatedVictories();
                setMostPlayedPlayerWith();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.statistics_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settingsMenu1:
                CodeAlert codeAlert = new CodeAlert();
                codeAlert.show(getSupportFragmentManager(), "codeAlert");
                return true;
            case R.id.settingsMenu21:
                GameStatus.getInstance().theme = Parameters.GREEN;
                setColors();
                return true;
            case R.id.settingsMenu22:
                GameStatus.getInstance().theme = Parameters.PURPLE;
                setColors();
                return true;
            case R.id.settingsMenu23:
                GameStatus.getInstance().theme = Parameters.ORANGE;
                setColors();
                return true;
            case R.id.settingsMenu24:
                GameStatus.getInstance().theme = Parameters.BW;
                setColors();
                return true;
            default:super.onOptionsItemSelected(item);

        }
        return super.onOptionsItemSelected(item);
    }

    private void setPlayer(){
        playerTextView[1] = (TextView) findViewById(R.id.statTextView1);
        playerTextView[2] = (TextView) findViewById(R.id.statValueTextView1);
        playerTextView[1].setText("Giocatore: " );
        playerTextView[2].setText(gameStatus.playerName);
    }

    private void setNumGames(){
        playerTextView[3]= (TextView) findViewById(R.id.statTextView2);
        playerTextView[4] = (TextView) findViewById(R.id.statValueTextView2);
        playerTextView[3].setText("Numero di partite giocate: ");
        playerTextView[4].setText("");
        db = FirebaseDatabase.getInstance().getReference().child("PlayersRecord");
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int numGames=0;
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    if(data.getValue(Users.class).getPlayers().get(0).equals(gameStatus.playerName))
                        numGames = numGames+1;
                }
                playerTextView[3].setText("Numero di partite giocate: ");
                playerTextView[4].setText(String.valueOf(numGames));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void setNumTicks(){
        playerTextView[5]= (TextView) findViewById(R.id.statTextView3);
        playerTextView[6] = (TextView) findViewById(R.id.statValueTextView3);
        playerTextView[5].setText("Totale spunte inserite: ");
        playerTextView[6].setText("");
        db = FirebaseDatabase.getInstance().getReference().child(gameStatus.playerName);
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int numTicks=0;
                for (DataSnapshot data: dataSnapshot.getChildren()){
                    numTicks += data.child("GameTable").getValue(GameTable.class).getNumTick();
                }
                playerTextView[5].setText("Totale spunte inserite: ");
                playerTextView[6].setText(String.valueOf(numTicks));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setNumCross(){
        playerTextView[7]= (TextView) findViewById(R.id.statTextView5);
        playerTextView[8] = (TextView) findViewById(R.id.statValueTextView5);
        playerTextView[7].setText("Totale croci inserite: ");
        playerTextView[8].setText("");
        db = FirebaseDatabase.getInstance().getReference().child(gameStatus.playerName);
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int numCrosses=0;
                for (DataSnapshot data: dataSnapshot.getChildren()){
                    numCrosses += data.child("GameTable").getValue(GameTable.class).getNumCross();
                }
                playerTextView[7].setText("Totale croci inserite: ");
                playerTextView[8].setText(String.valueOf(numCrosses));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setNumEmpty(){
        playerTextView[9]= (TextView) findViewById(R.id.statTextView4);
        playerTextView[10] = (TextView) findViewById(R.id.statValueTextView4);
        playerTextView[9].setText("Totale spazi rimasti incerti: ");
        playerTextView[10].setText("");
        db = FirebaseDatabase.getInstance().getReference().child(gameStatus.playerName);
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int numEmpty=0;
                for (DataSnapshot data: dataSnapshot.getChildren()){
                     numEmpty += data.child("GameTable").getValue(GameTable.class).getNumIncerts();
                }
                playerTextView[9].setText("Totale spazi rimasti incerti: ");
                playerTextView[10].setText(String.valueOf(numEmpty));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setUnValidatedVictories(){
        playerTextView[11]= (TextView) findViewById(R.id.statTextView6);
        playerTextView[12] = (TextView) findViewById(R.id.statValueTextView6);
        playerTextView[11].setText("Totale vittorie non ufficiali: ");
        playerTextView[12].setText("");
        db = FirebaseDatabase.getInstance().getReference().child("UnValidatedGame");
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int numVictories=0;
                for (DataSnapshot data: dataSnapshot.getChildren()){
                    if(data.getValue(String.class).equals(gameStatus.playerName))
                        numVictories ++;
                }
                playerTextView[11].setText("Totale vittorie non ufficiali: ");
                playerTextView[12].setText(String.valueOf(numVictories));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setValidatedVictories(){
        playerTextView[13]= (TextView) findViewById(R.id.statTextView7);
        playerTextView[14] = (TextView) findViewById(R.id.statValueTextView7);
        playerTextView[13].setText("Totale vittorie ufficiali: ");
        playerTextView[14].setText("");
        db = FirebaseDatabase.getInstance().getReference().child("ValidatedGame");
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int numVictories=0;
                for (DataSnapshot data: dataSnapshot.getChildren()){
                    if(data.getValue(String.class).equals(gameStatus.playerName))
                        numVictories ++;
                }
                playerTextView[13].setText("Totale vittorie ufficiali: ");
                playerTextView[14].setText(String.valueOf(numVictories));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setMostPlayedPlayerWith(){
        playerTextView[15]= (TextView) findViewById(R.id.statTextView8);
        playerTextView[16] = (TextView) findViewById(R.id.statValueTextView8);
        playerTextView[17]= (TextView) findViewById(R.id.statTextView9);
        playerTextView[18] = (TextView) findViewById(R.id.statValueTextView9);
        playerTextView[15].setText("Compagno più fidato: ");
        playerTextView[16].setText("");
        playerTextView[17].setText("Partite con il compagno più fidato: ");
        playerTextView[18].setText("");
        db = FirebaseDatabase.getInstance().getReference().child("PlayersRecord");
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String,Integer> players = new HashMap<>();
                int max = 0;
                String player="";
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    if(data.getValue(Users.class).getPlayers().get(0).equals(gameStatus.playerName)){
                        for(String name: data.getValue(Users.class).getPlayers()){
                            if(players.containsKey(name))
                                players.put(name,players.get(name)+1);
                            else
                                players.put(name,1);
                        }

                    }
                }
                for(String name: players.keySet())
                    if(players.get(name) > max && !name.equals(gameStatus.playerName)){
                        max = players.get(name);
                        player = name;
                    }
                if(max!=0) {
                    playerTextView[15].setText("Compagno più fidato: ");
                    playerTextView[16].setText(player);
                }else {
                    playerTextView[15].setText("Compagno più fidato: ");
                    playerTextView[16].setText("NA");
                }
                playerTextView[17].setText("Partite con il compagno più fidato: ");
                playerTextView[18].setText(String.valueOf(max));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setBestPlayer(){
        playerTextView[19]= (TextView) findViewById(R.id.statTextView10);
        playerTextView[20] = (TextView) findViewById(R.id.statValueTextView10);
        playerTextView[19].setText("Miglior giocatore: ");
        playerTextView[20].setText("");
        db = FirebaseDatabase.getInstance().getReference().child("ValidatedGame");
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String,Integer> players = new HashMap<>();
                int max = 0;
                String player="";
                for (DataSnapshot data: dataSnapshot.getChildren()){
                    if(players.containsKey(data.getValue(String.class)))
                        players.put(data.getValue(String.class), players.get(data.getValue(String.class))+1);
                    else
                        players.put(data.getValue(String.class),1);
                }
                for(String name: players.keySet())
                    if(players.get(name) > max){
                        max = players.get(name);
                        player = name;
                    }
                playerTextView[19].setText("Miglior giocatore: ");
                playerTextView[20].setText(player);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
