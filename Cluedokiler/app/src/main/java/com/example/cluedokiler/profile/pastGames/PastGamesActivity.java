package com.example.cluedokiler.profile.pastGames;

import android.content.Context;
import android.content.Intent;
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

import com.example.cluedokiler.models.GameNames;
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

import static com.example.cluedokiler.parameters.Parameters.MyPREFERENCES;

public class PastGamesActivity extends AppCompatActivity {

    private DatabaseReference db;
    private ArrayList<String> dates;
    private ArrayList<ArrayList<String>> players;
    private ArrayList<GameTable> gameTables;
    private ArrayList<PastGame> pastGames;
    private ArrayList<String> winners;
    private ArrayList<String> gameMode;
    private ArrayList<GameNames> gameNames;
    private RecyclerView pastGamesRecyclerView;
    private PastGamesAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private SharedPreferences preferences;
    private ImageView backArrow;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_games);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        name = getIntent().getStringExtra("name");

        setColors();

        db = FirebaseDatabase.getInstance().getReference().child(name);
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pastGames = new ArrayList<>();
                players = new ArrayList<>();
                gameTables = new ArrayList<>();
                dates = new ArrayList<>();
                winners = new ArrayList<>();
                gameMode = new ArrayList<>();
                gameNames = new ArrayList<>();
                long numGames=dataSnapshot.getChildrenCount();
                int i=0;
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    if( !(i< ( numGames-20))) {
                        dates.add(data.getKey());
                        players.add((ArrayList<String>)data.child("Players").getValue());
                        gameTables.add(data.child("GameTable").getValue(GameTable.class));
                        if(!data.child("Winner").getValue(String.class).equals(""))
                            winners.add(data.child("Winner").getValue(String.class));
                        else
                            winners.add("N.A.");
                        gameMode.add(data.child("GameMode").getValue(String.class));
                        gameNames.add(data.child("GameNames").getValue(GameNames.class));
                    }
                    i++;
                }

                for(int j=0; j<players.size(); j++){
                    pastGames.add(new PastGame(dates.get(j), players.get(j),winners.get(j),gameMode.get(j), gameTables.get(j)));
                }

                pastGamesRecyclerView = findViewById(R.id.pastGamesRecyclerView);
                //pastGamesRecyclerView.setHasFixedSize(true);    //true if items doesent change
                layoutManager = new LinearLayoutManager(PastGamesActivity.super.getApplicationContext());
                pastGamesRecyclerView.setLayoutManager(layoutManager);
                adapter = new PastGamesAdapter(pastGames);
                pastGamesRecyclerView.setAdapter(adapter);
                adapter.setOnItemClickListener(new PastGamesAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Intent showPastGame = new Intent(getApplicationContext(), ShowPastGameActivity.class);
                        showPastGame.putExtra("game", pastGames.get(position));
                        showPastGame.putExtra("gameNames", gameNames.get(position));
                        showPastGame.putExtra("name", name);
                        startActivity(showPastGame);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

        backArrow = findViewById(R.id.pastGamesBackArrowImageView);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setColors(){
        ConstraintLayout title = findViewById(R.id.titlePastGamesActivity);
        LinearLayout background = findViewById(R.id.layoutPastGamesActivity);

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
        inflater.inflate(R.menu.past_games_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.pastGamesMenu11:
                GameStatus.getInstance().theme = Parameters.GREEN;
                setColors();
                return true;
            case R.id.pastGamesMenu12:
                GameStatus.getInstance().theme = Parameters.PURPLE;
                setColors();
                return true;
            case R.id.pastGamesMenu13:
                GameStatus.getInstance().theme = Parameters.ORANGE;
                setColors();
                return true;
            case R.id.pastGamesMenu14:
                GameStatus.getInstance().theme = Parameters.BW;
                setColors();
                return true;
            default:super.onOptionsItemSelected(item);
        }

        return super.onOptionsItemSelected(item);
    }
}
