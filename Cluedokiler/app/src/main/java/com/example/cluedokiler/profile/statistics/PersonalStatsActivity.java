package com.example.cluedokiler.profile.statistics;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cluedokiler.gameInstance.GameStatus;
import com.example.cluedokiler.models.GameTable;
import com.example.cluedokiler.parameters.Parameters;
import com.example.cluedokiler.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.cluedokiler.parameters.Parameters.MyPREFERENCES;

public class PersonalStatsActivity extends AppCompatActivity {


    private DatabaseReference db;
    private ArrayList<PersonalStats> personalStats;
    private RecyclerView personalStatsRecyclerView;
    private PersonalStatsAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private SharedPreferences preferences;
    private ImageView backArrow;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_stats);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        name = getIntent().getStringExtra("name");

        setColors();

        db = FirebaseDatabase.getInstance().getReference().child(name);
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                personalStats = new ArrayList<>();

                int tickCount = 0;
                int crossCount = 0;
                int emptyCount = 0;
                int gameCount = 0;
                int validatedWinsCount = 0;
                int unvalidatedWinsCount = 0;
                int leftGamesCount = 0;

                HashMap<String,Integer> players = new HashMap<>();
                int max = 0;
                String player="";

                for(DataSnapshot data: dataSnapshot.getChildren()){
                    gameCount ++;
                    tickCount += data.child("GameTable").getValue(GameTable.class).getNumTick();
                    crossCount += data.child("GameTable").getValue(GameTable.class).getNumCross();
                    emptyCount += data.child("GameTable").getValue(GameTable.class).getNumIncerts();
                    //
                    if(data.child("Winner").getValue(String.class).equals(name)) {
                        if(data.child("Validated").getValue(String.class).equals("true"))
                            validatedWinsCount++;
                        else
                            unvalidatedWinsCount ++;
                    } else if(data.child("Winner").getValue(String.class).equals("")) {
                        leftGamesCount ++;
                    }
                    //
                    for (String playerName : (ArrayList<String>)data.child("Players").getValue()) {
                        if (players.containsKey(playerName))
                            players.put(playerName, players.get(playerName) + 1);
                        else
                            players.put(playerName, 1);
                    }

                }
                for(String playerName: players.keySet())
                    if(players.get(playerName) > max && !playerName.equals(name)){
                        max = players.get(playerName);
                        player = playerName;
                    }

                personalStats.add(new PersonalStats("Partite giocate: ",String.valueOf(gameCount)));
                personalStats.add(new PersonalStats("Totale spunte inserite: ",String.valueOf(tickCount)));
                personalStats.add(new PersonalStats("Totale croci inserite: ",String.valueOf(crossCount)));
                personalStats.add(new PersonalStats("Totale spazi rimasti incerti: ",String.valueOf(emptyCount)));
                personalStats.add(new PersonalStats("Numero vittorie ufficiali: ",String.valueOf(validatedWinsCount)));
                personalStats.add(new PersonalStats("Numero vittorie non ufficiali: ",String.valueOf(unvalidatedWinsCount)));
                personalStats.add(new PersonalStats("Numero partite abbandonate: ",String.valueOf(leftGamesCount)));
                personalStats.add(new PersonalStats("Compagno fidato:",player));
                personalStats.add(new PersonalStats("Partite con il compagno fidato: ",String.valueOf(max)));

                personalStatsRecyclerView = findViewById(R.id.personalStatsRecyclerView);
                //personalStatsRecyclerView.setHasFixedSize(true);    //true if items doesn't change
                layoutManager = new LinearLayoutManager(PersonalStatsActivity.super.getApplicationContext());
                personalStatsRecyclerView.setLayoutManager(layoutManager);
                adapter = new PersonalStatsAdapter(personalStats);
                personalStatsRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

        backArrow = findViewById(R.id.personalStatsBackArrowImageView);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setColors(){
        ConstraintLayout title = findViewById(R.id.titlePersonalStatsActivity);
        LinearLayout background = findViewById(R.id.layoutPersonalStatsActivity);

        if(GameStatus.getInstance().theme.equals(Parameters.PURPLE)){
            title.setBackgroundColor(Parameters.PURPLE_MAIN_COLOR);
            background.setBackgroundResource(R.drawable.screen_background);
        }else if(GameStatus.getInstance().theme.equals(Parameters.GREEN)){
            title.setBackgroundColor(Parameters.GREEN_MAIN_COLOR);
            background.setBackgroundResource(R.drawable.screen_background_green);
        }else if(GameStatus.getInstance().theme.equals(Parameters.ORANGE)){
            title.setBackgroundColor(Parameters.ORANGE_MAIN_COLOR);
            background.setBackgroundResource(R.drawable.screen_background_orange);
        }else if(GameStatus.getInstance().theme.equals(Parameters.BW)){
            title.setBackgroundColor(Parameters.BW_MAIN_COLOR);
            background.setBackgroundResource(R.drawable.screen_background_bw);
        }else if(GameStatus.getInstance().theme.equals(Parameters.WB)){
            title.setBackgroundColor(Parameters.WB_MAIN_COLOR);
            background.setBackgroundResource(R.drawable.screen_background_wb);
        }
        preferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("color", GameStatus.getInstance().theme);
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setColors();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.personal_stats_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.personalStatsMenu11:
                GameStatus.getInstance().theme = Parameters.GREEN;
                setColors();
                return true;
            case R.id.personalStatsMenu12:
                GameStatus.getInstance().theme = Parameters.PURPLE;
                setColors();
                return true;
            case R.id.personalStatsMenu13:
                GameStatus.getInstance().theme = Parameters.ORANGE;
                setColors();
                return true;
            case R.id.personalStatsMenu14:
                GameStatus.getInstance().theme = Parameters.BW;
                setColors();
                return true;
            default:super.onOptionsItemSelected(item);
        }

        return super.onOptionsItemSelected(item);
    }

}
