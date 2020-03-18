package com.example.cluedokiler;

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
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import static com.example.cluedokiler.Parameters.HARRY_CLUEDO;
import static com.example.cluedokiler.Parameters.MyPREFERENCES;
import static com.example.cluedokiler.Parameters.OUR_CLUEDO;
import static com.example.cluedokiler.Parameters.STANDARD_CLUEDO;

public class GameSelectorActivity extends AppCompatActivity {

    SharedPreferences preferences;
    ImageView ourCluedoImageView;
    ImageView cluedoImageView;
    ImageView harryPotterCluedoImageView;
    ImageView customCluedoImageView;
    GameNames gameNames;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_selector);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setElevation(Float.valueOf(100));

        setUpPreferences();

        setColors();

        setNameTextView();

        ourCluedoImageView = findViewById(R.id.ourCluedoImageView);
        harryPotterCluedoImageView = findViewById(R.id.harryPotterCluedoImageView);
        cluedoImageView = findViewById(R.id.cluedoImageView);
        customCluedoImageView = findViewById(R.id.customCluedoImageView);

        gameNames = new GameNames();

        ourCluedoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameNames.setSuspects(getResources().getStringArray(R.array.suspects));
                gameNames.setWeapons(getResources().getStringArray(R.array.weapons));
                gameNames.setPlaces(getResources().getStringArray(R.array.places));
                gameNames.setGameMode(OUR_CLUEDO);
                GameStatus.getInstance().gameNames = gameNames;
                Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(mainIntent);
                finish();
            }
        });

        harryPotterCluedoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameNames.setSuspects(getResources().getStringArray(R.array.suspectsHarry));
                gameNames.setWeapons(getResources().getStringArray(R.array.weaponsHarry));
                gameNames.setPlaces(getResources().getStringArray(R.array.placesHarry));
                gameNames.setGameMode(HARRY_CLUEDO);
                GameStatus.getInstance().gameNames = gameNames;
                gameNames.setModified(true);
                Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(mainIntent);
                finish();
            }
        });

        cluedoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameNames.setSuspects(getResources().getStringArray(R.array.suspectsStandard));
                gameNames.setWeapons(getResources().getStringArray(R.array.weaponsStandard));
                gameNames.setPlaces(getResources().getStringArray(R.array.placesStandard));
                gameNames.setGameMode(STANDARD_CLUEDO);
                GameStatus.getInstance().gameNames = gameNames;
                gameNames.setModified(true);
                Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(mainIntent);
                finish();
            }
        });

        customCluedoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent personalizeIntent = new Intent(getApplicationContext(), PersonalizeGameActivity.class);
                personalizeIntent.putExtra("personalizeNames",false);
                startActivity(personalizeIntent);
                finish();
            }
        });

        setExitArrow();

        OnBackPressedCallback callback = new OnBackPressedCallback(true ) {
            @Override
            public void handleOnBackPressed() {
                ExitGameAlert exitAlert = new ExitGameAlert();
                exitAlert.show(getSupportFragmentManager(), "exitGameAlert");
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);

    }


    private void setExitArrow(){
        ImageView exitArrow = findViewById(R.id.exitAppGameSelectorImageVIew);
        exitArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExitGameAlert exitAlert = new ExitGameAlert();
                exitAlert.show(getSupportFragmentManager(), "exitGameAlert");
            }
        });

    }

    private void setNameTextView(){
        TextView textView = findViewById(R.id.nameGameSelectorTextView);
        textView.setText(GameStatus.getInstance().playerName);
    }

    private void setUpPreferences(){
        preferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        if(!preferences.contains("name")){
            Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
            loginIntent.putExtra("firstLogin",true);
            startActivity(loginIntent);
        }else{
            GameStatus.getInstance().playerName = preferences.getString("name", "");
        }
        GameStatus.getInstance().theme = preferences.getString("color",Parameters.PURPLE);

    }


    private void setColors(){
        ConstraintLayout title = findViewById(R.id.titleGameSelectorActivity);
        LinearLayout background = findViewById(R.id.layoutGameSelectorActivity);

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

        setNameTextView();

        setColors();
    }

        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.game_selector_menu,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.gameSelectorMenu11:
                GameStatus.getInstance().theme = Parameters.GREEN;
                setColors();
                return true;
            case R.id.gameSelectorMenu12:
                GameStatus.getInstance().theme = Parameters.PURPLE;
                setColors();
                return true;
            case R.id.gameSelectorMenu13:
                GameStatus.getInstance().theme = Parameters.ORANGE;
                setColors();
                return true;
            case R.id.gameSelectorMenu14:
                GameStatus.getInstance().theme = Parameters.BW;
                setColors();
                return true;
            default:super.onOptionsItemSelected(item);
        }

        return super.onOptionsItemSelected(item);
    }
}
