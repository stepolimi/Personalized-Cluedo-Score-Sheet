package com.example.cluedokiler;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;
import java.util.Arrays;

import static com.example.cluedokiler.Parameters.MyPREFERENCES;

public class LoginActivity extends AppCompatActivity {
    SharedPreferences preferences;
    Spinner loginSpinner;
    Button loginButton;
    String name = "--Vuoto--";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setElevation(Float.valueOf(100));


        TextView loginTextView = findViewById(R.id.loginTextView);

        Intent myIntent = getIntent();
        boolean firstLogin = myIntent.getBooleanExtra("firstLogin",true);
        if(firstLogin)
            loginTextView.setText("Per iniziare, scegli il tuo nome!");
        else
            loginTextView.setText("Scegli il tuo nuovo nome!");

        setColors();

        loginSpinner = findViewById(R.id.mainPlayerCoicheSpinner);
        ArrayList<String> players = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.players)));
        ArrayAdapter arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,players);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        loginSpinner.setAdapter(arrayAdapter);
        loginSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                name = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        preferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        loginButton = findViewById(R.id.loginConfirmButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!name.equals("--Vuoto--")){
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("name", name);
                    editor.apply();
                    GameStatus.getInstance().playerName = name;
                    finish();
                }
                else
                    Toast.makeText(LoginActivity.super.getApplicationContext(),"Seleziona il tuo nome", Toast.LENGTH_SHORT).show();
            }
        });

        ImageView backArrow = findViewById(R.id.backArrowImageView);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!GameStatus.getInstance().playerName.equals("--Vuoto--") && !GameStatus.getInstance().playerName.equals(""))
                    finish();
                else
                    Toast.makeText(LoginActivity.super.getApplicationContext(),"Seleziona il tuo nome e schiaccia 'conferma'", Toast.LENGTH_SHORT).show();
            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true ) {
            @Override
            public void handleOnBackPressed() {
                if(!GameStatus.getInstance().playerName.equals("--Vuoto--") && !GameStatus.getInstance().playerName.equals("")) {
                    ExitGameAlert exitAlert = new ExitGameAlert();
                    exitAlert.show(getSupportFragmentManager(), "exitGameAlert");
                }else
                    finish();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);


    }

    private void setColors(){
        //loginSpinner = findViewById(R.id.mainPlayerCoicheSpinner);
        loginButton = findViewById(R.id.loginConfirmButton);
        ConstraintLayout title = findViewById(R.id.titleLoginActivity);
        LinearLayout background = findViewById(R.id.layoutLoginActivity);

        if(GameStatus.getInstance().theme.equals(Parameters.PURPLE)){
            loginButton.setBackgroundResource(R.drawable.button_background);
            title.setBackgroundColor(Parameters.PURPLE_MAIN_COLOR);
            background.setBackgroundResource(R.drawable.screen_background);
        }else if(GameStatus.getInstance().theme.equals(Parameters.GREEN)){
            loginButton.setBackgroundResource(R.drawable.button_background_green);
            title.setBackgroundColor(Parameters.GREEN_MAIN_COLOR);
            background.setBackgroundResource(R.drawable.screen_background_green);
        }else if(GameStatus.getInstance().theme.equals(Parameters.ORANGE)){
            loginButton.setBackgroundResource(R.drawable.button_background_orange);
            title.setBackgroundColor(Parameters.ORANGE_MAIN_COLOR);
            background.setBackgroundResource(R.drawable.screen_background_orange);
        }else if(GameStatus.getInstance().theme.equals(Parameters.BW)){
            loginButton.setBackgroundResource(R.drawable.button_background_bw);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.login_menu,menu);
        return true;
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
