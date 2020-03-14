package com.example.cluedokiler;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;

import static com.example.cluedokiler.Parameters.MyPREFERENCES;

public class PersonalizeGameActivity extends AppCompatActivity {
    SharedPreferences preferences;
    GameNames gameNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personalize_game);

        gameNames = new GameNames();

        final TextView[] suspects = new TextView[6];
        suspects[0] = findViewById(R.id.suspects1);
        suspects[1] = findViewById(R.id.suspects2);
        suspects[2] = findViewById(R.id.suspects3);
        suspects[3] = findViewById(R.id.suspects4);
        suspects[4] = findViewById(R.id.suspects5);
        suspects[5] = findViewById(R.id.suspects6);

        final TextView[] weapons = new TextView[6];
        weapons[0] = findViewById(R.id.weapons1);
        weapons[1] = findViewById(R.id.weapons2);
        weapons[2] = findViewById(R.id.weapons3);
        weapons[3] = findViewById(R.id.weapons4);
        weapons[4] = findViewById(R.id.weapons5);
        weapons[5] = findViewById(R.id.weapons6);

        final TextView[] places = new TextView[9];
        places[0] = findViewById(R.id.places1);
        places[1] = findViewById(R.id.places2);
        places[2] = findViewById(R.id.places3);
        places[3] = findViewById(R.id.places4);
        places[4] = findViewById(R.id.places5);
        places[5] = findViewById(R.id.places6);
        places[6] = findViewById(R.id.places7);
        places[7] = findViewById(R.id.places8);
        places[8] = findViewById(R.id.places9);

        Button resetNamesButton = findViewById(R.id.resetNamesButton);
        Button saveNamesButton = findViewById(R.id.saveNamesButton);
        ImageView exitActivity = findViewById(R.id.exitPersonalizeGameImageVIew);
        TextView playerName = findViewById(R.id.playerNameTextView);
        playerName.setText(GameStatus.getInstance().playerName);

        resetNamesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameNames.setSuspects(getResources().getStringArray(R.array.suspects));
                gameNames.setWeapons(getResources().getStringArray(R.array.weapons));
                gameNames.setPlaces(getResources().getStringArray(R.array.places));
                gameNames.setModified(false);
                GameStatus.getInstance().gameNames = gameNames;
                Toast.makeText(PersonalizeGameActivity.super.getApplicationContext(), "Nomi reimpostati corettamente", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        saveNamesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0; i<6; i++) {
                    if (suspects[i].getText().toString().equals("")) {
                        Toast.makeText(PersonalizeGameActivity.super.getApplicationContext(), "Devi riempire tutti i campi!", Toast.LENGTH_SHORT).show();
                        return;
                    }else
                        gameNames.addSuspect(suspects[i].getText().toString(),i);
                }
                for(int i=0; i<6; i++) {
                    if (weapons[i].getText().toString().equals("")) {
                        Toast.makeText(PersonalizeGameActivity.super.getApplicationContext(), "Devi riempire tutti i campi!", Toast.LENGTH_SHORT).show();
                        return;
                    }else
                        gameNames.addWeapon(weapons[i].getText().toString(),i);
                }
                for(int i=0; i<9; i++) {
                    if (places[i].getText().toString().equals("")) {
                        Toast.makeText(PersonalizeGameActivity.super.getApplicationContext(), "Devi riempire tutti i campi!", Toast.LENGTH_SHORT).show();
                        return;
                    }else
                        gameNames.addPlace(places[i].getText().toString(),i);
                }
                gameNames.setModified(true);
                GameStatus.getInstance().gameNames = gameNames;
                finish();
            }
        });

        exitActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void setColors(){
        Button reset = findViewById(R.id.resetNamesButton);
        Button save = findViewById(R.id.saveNamesButton);
        ConstraintLayout title = findViewById(R.id.titlePersonalizeActivity);
        LinearLayout background = findViewById(R.id.layoutPersonalizeActivity);

        if(GameStatus.getInstance().theme.equals(Parameters.PURPLE)){
            reset.setBackgroundResource(R.drawable.button_background);
            save.setBackgroundResource(R.drawable.button_background);
            title.setBackgroundColor(Parameters.PURPLE_MAIN_COLOR);
            background.setBackgroundResource(R.drawable.screen_background);
        }else if(GameStatus.getInstance().theme.equals(Parameters.GREEN)){
            reset.setBackgroundResource(R.drawable.button_background_green);
            save.setBackgroundResource(R.drawable.button_background_green);
            title.setBackgroundColor(Parameters.GREEN_MAIN_COLOR);
            background.setBackgroundResource(R.drawable.screen_background_green);
        }else if(GameStatus.getInstance().theme.equals(Parameters.ORANGE)){
            reset.setBackgroundResource(R.drawable.button_background_orange);
            save.setBackgroundResource(R.drawable.button_background_orange);
            title.setBackgroundColor(Parameters.ORANGE_MAIN_COLOR);
            background.setBackgroundResource(R.drawable.screen_background_orange);
        }else if(GameStatus.getInstance().theme.equals(Parameters.BW)){
            reset.setBackgroundResource(R.drawable.button_background_bw);
            save.setBackgroundResource(R.drawable.button_background_bw);
            title.setBackgroundColor(Parameters.BW_MAIN_COLOR);
            background.setBackgroundResource(R.drawable.screen_background_bw);
        }else if(GameStatus.getInstance().theme.equals(Parameters.WB)){
            reset.setBackgroundResource(R.drawable.button_background_wb);
            save.setBackgroundResource(R.drawable.button_background_wb);
            title.setBackgroundColor(Parameters.WB_MAIN_COLOR);
            background.setBackgroundResource(R.drawable.screen_background_wb);
        }
        preferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("color", GameStatus.getInstance().theme);
        editor.apply();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.loginMenu11:
                GameStatus.getInstance().theme = Parameters.GREEN;
                setColors();
                return true;
            case R.id.loginMenu12:
                GameStatus.getInstance().theme = Parameters.PURPLE;
                setColors();
                return true;
            case R.id.loginMenu13:
                GameStatus.getInstance().theme = Parameters.ORANGE;
                setColors();
                return true;
            case R.id.loginMenu14:
                GameStatus.getInstance().theme = Parameters.BW;
                setColors();
                return true;
            default:super.onOptionsItemSelected(item);
        }

        return super.onOptionsItemSelected(item);
    }
}
