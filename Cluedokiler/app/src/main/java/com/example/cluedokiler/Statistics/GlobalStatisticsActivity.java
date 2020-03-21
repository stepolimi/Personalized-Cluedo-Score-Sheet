package com.example.cluedokiler.Statistics;

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

import com.example.cluedokiler.R;
import com.example.cluedokiler.gameInstance.GameStatus;
import com.example.cluedokiler.parameters.Parameters;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static com.example.cluedokiler.parameters.Parameters.MyPREFERENCES;

public class GlobalStatisticsActivity extends AppCompatActivity {

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

        db = FirebaseDatabase.getInstance().getReference().child("ValidatedGame");
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> playerNames = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.players)));
                playerNames.remove("--Vuoto--");
                playerNames.remove("Guest1");
                playerNames.remove("Guest2");
                playerNames.remove("Guest3");
                playerNames.remove("Guest4");
                playerNames.remove("Guest5");
                playerNames.remove("Guest6");
                personalStats = new ArrayList<>();

                HashMap<String,Integer> players = new HashMap<>();

                for(String name: playerNames){
                    players.put(name,0);
                }

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

                personalStats.add(new PersonalStats("Giocatore migliore: ",player));
                personalStats.add(new PersonalStats("Partite vinte : ",String.valueOf(max)));
                personalStats.add(new PersonalStats("",""));
                personalStats.add(new PersonalStats("Partite vinte da ciascun giocatore:",""));

                for(String name: players.keySet())
                    personalStats.add(new PersonalStats("  " + name,String.valueOf(players.get(name)) + "  "));

                personalStatsRecyclerView = findViewById(R.id.personalStatsRecyclerView);
                //personalStatsRecyclerView.setHasFixedSize(true);    //true if items doesn't change
                layoutManager = new LinearLayoutManager(GlobalStatisticsActivity.super.getApplicationContext());
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
