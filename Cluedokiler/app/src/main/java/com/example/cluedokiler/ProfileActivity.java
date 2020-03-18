package com.example.cluedokiler;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import static com.example.cluedokiler.Parameters.MyPREFERENCES;

public class ProfileActivity extends AppCompatActivity {

    SharedPreferences preferences;
    ImageView profileImage;
    TextView profilePhrase;
    ImageView backArrow;
    Button pastGamesButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setElevation(Float.valueOf(100));

        setColors();

        setProfileImage();

        setBackArrow();

        setPastGamesButton();
    }

    private void setProfileImage(){
        profileImage = findViewById(R.id.profileImageView);
        profilePhrase = findViewById(R.id.profilePhraseTextView);
        String name = GameStatus.getInstance().playerName;
        int resource;
        String text;

        if(name.equals("Luchino")) {
            resource = R.drawable.luchino;
            text = "Sono una signorina!";
        }else if(name.equals("Virgi")) {
            resource = R.drawable.virgi;
            text = "Aspetta, hai detto Lucius o Draco?";
        }else if(name.equals("Baga")) {
            resource = R.drawable.baga;
            text = "Da grandi poteri derivano grandi weiss medie";
        }else if(name.equals("Teo")) {
            resource = R.drawable.teo;
            text = "La vita è un pendolo che oscilla tra draghi e viverne";
        }else if(name.equals("Bean")) {
            resource = R.drawable.bean;
            text = "Sono troppo vecchio per questa merda";
        }else if(name.equals("Ale")) {
            resource = R.drawable.ale;
            text = "Ehi, mi piace questa canzone!";
        }else if(name.equals("Greg")) {
            resource = R.drawable.greg;
            text = "Guro ce sno capae di scrvere";
        }else if(name.equals("Simo")) {
            resource = R.drawable.simo;
            text = "(ehi, sono lo sviluppatore: se fai tornare giovane il bean ti faccio vincere)";
        }else if(name.equals("Noe")) {
            resource = R.drawable.noe;
            text = "Da grandi poteri derivano grandi weiss medie";
        }else if(name.equals("Chiari")) {
            resource = R.drawable.chiari;
            text = "*suoni dei cartoni disney*";
        }else if(name.equals("Frii")) {
            resource = R.drawable.frii;
            text = "Amo ma sei seria? Io morta";
        }else if(name.equals("Vitto")) {
            resource = R.drawable.vitto;
            text = "A me piace veramente la Heineken";
        }else if(name.equals("Friggi")) {
            resource = R.drawable.ale;
            text = "LMAO";
        }else if(name.equals("Luca")) {
            resource = R.drawable.ale;
            text = "t.b.d.";
        } else {
            resource = R.drawable.question;
            text = "Profilo fintissimo";
        }
        profileImage.setImageResource(resource);
        profilePhrase.setText(text);
    }

    private void setBackArrow(){
        backArrow = findViewById(R.id.profileBackArrowImageView);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setPastGamesButton(){
        pastGamesButton = findViewById(R.id.pastGamesButton);
        pastGamesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pastGames = new Intent(getApplicationContext(), PastGamesActivity.class);
                startActivity(pastGames);
            }
        });
    }

    private void setColors(){
        ConstraintLayout title = findViewById(R.id.titleProfileActivity);
        LinearLayout background = findViewById(R.id.layoutProfileActivity);
        pastGamesButton = findViewById(R.id.pastGamesButton);

        if(GameStatus.getInstance().theme.equals(Parameters.PURPLE)){
            pastGamesButton.setBackgroundResource(R.drawable.button_background);
            title.setBackgroundColor(Parameters.PURPLE_MAIN_COLOR);
            background.setBackgroundResource(R.drawable.screen_background);
        }else if(GameStatus.getInstance().theme.equals(Parameters.GREEN)){
            pastGamesButton.setBackgroundResource(R.drawable.button_background_green);
            title.setBackgroundColor(Parameters.GREEN_MAIN_COLOR);
            background.setBackgroundResource(R.drawable.screen_background_green);
        }else if(GameStatus.getInstance().theme.equals(Parameters.ORANGE)){
            pastGamesButton.setBackgroundResource(R.drawable.button_background_orange);
            title.setBackgroundColor(Parameters.ORANGE_MAIN_COLOR);
            background.setBackgroundResource(R.drawable.screen_background_orange);
        }else if(GameStatus.getInstance().theme.equals(Parameters.BW)){
            pastGamesButton.setBackgroundResource(R.drawable.button_background_bw);
            title.setBackgroundColor(Parameters.BW_MAIN_COLOR);
            background.setBackgroundResource(R.drawable.screen_background_bw);
        }else if(GameStatus.getInstance().theme.equals(Parameters.WB)){
            pastGamesButton.setBackgroundResource(R.drawable.button_background_wb);
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
        inflater.inflate(R.menu.profile_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profileMenu11:
                GameStatus.getInstance().theme = Parameters.GREEN;
                setColors();
                return true;
            case R.id.profileMenu12:
                GameStatus.getInstance().theme = Parameters.PURPLE;
                setColors();
                return true;
            case R.id.profileMenu13:
                GameStatus.getInstance().theme = Parameters.ORANGE;
                setColors();
                return true;
            case R.id.profileMenu14:
                GameStatus.getInstance().theme = Parameters.BW;
                setColors();
                return true;
            default:super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }
}
