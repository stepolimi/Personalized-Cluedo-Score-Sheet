package com.example.cluedokiler.profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.cluedokiler.PlayersAdapter;
import com.example.cluedokiler.R;
import com.example.cluedokiler.gameInstance.GameStatus;
import com.example.cluedokiler.parameters.Parameters;

import java.util.ArrayList;
import java.util.Arrays;

import static com.example.cluedokiler.parameters.Parameters.MyPREFERENCES;

public class SearchProfile extends AppCompatActivity {

    private SharedPreferences preferences;
    private Button searchProfileButton;
    private String name;
    private String profileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_profile);
        searchProfileButton = findViewById(R.id.searchProfileButton);
        name= getIntent().getStringExtra("name");

        setColors();

        ArrayList<String> players = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.players)));
        players.remove(name);
        players.remove("--Vuoto--");

        Spinner profileSpinner = (Spinner) findViewById(R.id.profileSpinner);
        PlayersAdapter playersAdapter = new PlayersAdapter(this,players);
        profileSpinner.setAdapter(playersAdapter);
        profileSpinner.setDropDownVerticalOffset(100);

        profileSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                profileName = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        searchProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profile = new Intent(getApplicationContext(), ProfileActivity.class);
                profile.putExtra("name",profileName);
                startActivity(profile);
                finish();
            }
        });

    }

    private void setColors(){
        ConstraintLayout title = findViewById(R.id.titleSearchProfileActivity);
        ConstraintLayout background = findViewById(R.id.layoutSearchProfileActivity);
        searchProfileButton = findViewById(R.id.searchProfileButton);

        if(GameStatus.getInstance().theme.equals(Parameters.PURPLE)){
            searchProfileButton.setBackgroundResource(R.drawable.button_background);
            title.setBackgroundColor(Parameters.PURPLE_MAIN_COLOR);
            background.setBackgroundResource(R.drawable.screen_background);
        }else if(GameStatus.getInstance().theme.equals(Parameters.GREEN)){
            searchProfileButton.setBackgroundResource(R.drawable.button_background_green);
            title.setBackgroundColor(Parameters.GREEN_MAIN_COLOR);
            background.setBackgroundResource(R.drawable.screen_background_green);
        }else if(GameStatus.getInstance().theme.equals(Parameters.ORANGE)){
            searchProfileButton.setBackgroundResource(R.drawable.button_background_orange);
            title.setBackgroundColor(Parameters.ORANGE_MAIN_COLOR);
            background.setBackgroundResource(R.drawable.screen_background_orange);
        }else if(GameStatus.getInstance().theme.equals(Parameters.BW)){
            searchProfileButton.setBackgroundResource(R.drawable.button_background_bw);
            title.setBackgroundColor(Parameters.BW_MAIN_COLOR);
            background.setBackgroundResource(R.drawable.screen_background_bw);
        }else if(GameStatus.getInstance().theme.equals(Parameters.WB)){
            searchProfileButton.setBackgroundResource(R.drawable.button_background_wb);
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
        inflater.inflate(R.menu.search_profile_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.searchProfileMenu1:
                GameStatus.getInstance().theme = Parameters.GREEN;
                setColors();
                return true;
            case R.id.searchProfileMenu12:
                GameStatus.getInstance().theme = Parameters.PURPLE;
                setColors();
                return true;
            case R.id.searchProfileMenu13:
                GameStatus.getInstance().theme = Parameters.ORANGE;
                setColors();
                return true;
            case R.id.searchProfileMenu14:
                GameStatus.getInstance().theme = Parameters.BW;
                setColors();
                return true;
            default:super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }
}
