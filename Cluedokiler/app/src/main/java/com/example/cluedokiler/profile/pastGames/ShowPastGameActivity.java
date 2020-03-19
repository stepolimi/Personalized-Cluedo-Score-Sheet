package com.example.cluedokiler.profile.pastGames;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.cluedokiler.models.GameNames;
import com.example.cluedokiler.gameInstance.GameStatus;
import com.example.cluedokiler.parameters.Parameters;
import com.example.cluedokiler.R;
import com.example.cluedokiler.dialogs.ImageDialog;

import java.util.ArrayList;

import static com.example.cluedokiler.parameters.Parameters.MyPREFERENCES;
import static com.example.cluedokiler.parameters.Parameters.OUR_CLUEDO;

public class ShowPastGameActivity extends AppCompatActivity {

    private SharedPreferences preferences;
    private PastGame game;
    private GameNames gameNames;
    private int numCol;
    private ArrayList<String> suspects;
    private ArrayList<String> weapons;
    private ArrayList<String> places;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setColors();

        game = (PastGame)getIntent().getSerializableExtra("game");
        gameNames = (GameNames) getIntent().getSerializableExtra("gameNames");
        name = getIntent().getStringExtra("name");

        final ArrayList<TextView> playerTextViews = new ArrayList<>();
        playerTextViews.add(0,(TextView) findViewById(R.id.player1TextView));
        playerTextViews.add(1,(TextView) findViewById(R.id.player2TextView));
        playerTextViews.add(2,(TextView) findViewById(R.id.player3TextView));
        playerTextViews.add(3,(TextView) findViewById(R.id.player4TextView));
        playerTextViews.add(4,(TextView) findViewById(R.id.player5TextView));
        playerTextViews.add(5,(TextView) findViewById(R.id.player6TextView));

        for (int i = 0; i<7;i++) {
            if(i<game.getPlayers().size()) {
                playerTextViews.get(i).setText(game.getPlayers().get(i));
                final TextView textView = playerTextViews.get(i);

                playerTextViews.get(i).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ImageDialog imageDialog = new ImageDialog();
                        Bundle args = new Bundle();
                        args.putString("player", textView.getText().toString());
                        imageDialog.setArguments(args);
                        imageDialog.show(getSupportFragmentManager(), "imageDialog");
                    }
                });
            }else {
                if(i<6 || game.getPlayers().size()==6) {
                    TableLayout gameTableLayout = (TableLayout) findViewById(R.id.gameTableLayout);
                    gameTableLayout.setColumnCollapsed(i + 1, true);
                }
            }
        }
        numCol = game.getPlayers().size() + 1;

        setImages();

        setResources();

        nameViews();

        setHideSwitcher();

        setEndGameButton();

        setDeclareWinnerButton();

        setBackArrow();
    }

    private void setImages(){
        TableLayout gameTableLayout = (TableLayout) findViewById(R.id.gameTableLayout);
        for (int i = 0; i < gameTableLayout.getChildCount(); i++) {
            TableRow gameTableRow = (TableRow) gameTableLayout.getChildAt(i);
            for (int x = 0; x < gameTableRow.getChildCount(); x++) {
                final View itemTableLayout = gameTableRow.getChildAt(x);
                if (itemTableLayout instanceof ImageView && (x<numCol || x==gameTableRow.getChildCount()-1)) {
                    ImageView tableImageView = (ImageView) itemTableLayout;
                    if (game.getTable().get(tableImageView.getId()).equals("cross")) {
                        scaleImages((ImageView) itemTableLayout, R.drawable.cross);
                        tableImageView.setTag("cross");
                    } else if (game.getTable().get(tableImageView.getId()).equals("empty")) {
                        scaleImages((ImageView) itemTableLayout, R.drawable.checkbox);
                        tableImageView.setTag("empty");
                    } else if (game.getTable().get(tableImageView.getId()).equals("question")) {
                        scaleImages((ImageView) itemTableLayout, R.drawable.question);
                        tableImageView.setTag("question");
                    } else if (game.getTable().get(tableImageView.getId()).equals("tick")) {
                        scaleImages((ImageView) itemTableLayout, R.drawable.tick);
                        tableImageView.setTag("tick");
                    } else if (game.getTable().get(tableImageView.getId()).equals("question2")) {
                        scaleImages((ImageView) itemTableLayout, R.drawable.question2);
                        tableImageView.setTag("question2");
                    }
                }
            }
        }
    }


    private void scaleImages(ImageView imageView, int pic){
        //1/9 largezza
        Display screen = getWindowManager().getDefaultDisplay();
        BitmapFactory.Options scalingOptions = new BitmapFactory.Options();
        scalingOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), pic,scalingOptions);

        int imgWidth = scalingOptions.outWidth;
        int screenWidth = screen.getWidth();

        if(imgWidth > screenWidth){
            int ratio = Math.round( ((float) screenWidth / (float) imgWidth) * 9);
            scalingOptions.inSampleSize = ratio;
        }
        scalingOptions.inJustDecodeBounds = false;

        Bitmap scaleImg = BitmapFactory.decodeResource(getResources(),pic,scalingOptions);
        imageView.setImageBitmap(scaleImg);

    }

    private void setResources(){
        /*
        if(game.getGameMode().equals(HARRY_CLUEDO)) {
            suspects = (getResources().getStringArray(R.array.suspectsHarry));
            weapons = (getResources().getStringArray(R.array.weaponsHarry));
            places = (getResources().getStringArray(R.array.placesHarry));

        }else if(game.getGameMode().equals(STANDARD_CLUEDO) ) {
            suspects = (getResources().getStringArray(R.array.suspectsStandard));
            weapons = (getResources().getStringArray(R.array.weaponsStandard));
            places = (getResources().getStringArray(R.array.placesStandard));

        }else if(game.getGameMode().equals(OUR_CLUEDO)) {
            suspects = (getResources().getStringArray(R.array.suspects));
            weapons = (getResources().getStringArray(R.array.weapons));
            places = (getResources().getStringArray(R.array.places));

        }else {
            suspects = (getResources().getStringArray(R.array.suspectsStandard));
            weapons = (getResources().getStringArray(R.array.weaponsStandard));
            places = (getResources().getStringArray(R.array.placesStandard));
        }
*/
        suspects = gameNames.getSuspects();
        weapons = gameNames.getWeapons();
        places = gameNames.getPlaces();
    }

    private void nameViews() {
        ArrayList<TextView> susTextViews = new ArrayList<>();
        susTextViews.add(0, (TextView) findViewById(R.id.textView8));
        susTextViews.add(1, (TextView) findViewById(R.id.textView9));
        susTextViews.add(2, (TextView) findViewById(R.id.textView10));
        susTextViews.add(3, (TextView) findViewById(R.id.textView11));
        susTextViews.add(4, (TextView) findViewById(R.id.textView12));
        susTextViews.add(5, (TextView) findViewById(R.id.textView13));

        for (int i = 0; i < 6; i++) {
            susTextViews.get(i).setText(suspects.get(i));
            if(game.getGameMode().equals(OUR_CLUEDO)) {
                final TextView textView = susTextViews.get(i);
                susTextViews.get(i).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ImageDialog imageDialog = new ImageDialog();
                        Bundle args = new Bundle();
                        args.putString("suspect", textView.getText().toString());
                        imageDialog.setArguments(args);
                        imageDialog.show(getSupportFragmentManager(), "imageDialog");
                    }
                });
            }
        }

        ArrayList<TextView> weapTextViews = new ArrayList<>();
        weapTextViews.add(0, (TextView) findViewById(R.id.textView16));
        weapTextViews.add(1, (TextView) findViewById(R.id.textView17));
        weapTextViews.add(2, (TextView) findViewById(R.id.textView18));
        weapTextViews.add(3, (TextView) findViewById(R.id.textView19));
        weapTextViews.add(4, (TextView) findViewById(R.id.textView20));
        weapTextViews.add(5, (TextView) findViewById(R.id.textView21));

        for (int i = 0; i < 6; i++) {
            weapTextViews.get(i).setText(weapons.get(i));
            if(game.getGameMode().equals(OUR_CLUEDO)) {
                final TextView textView = weapTextViews.get(i);
                weapTextViews.get(i).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ImageDialog imageDialog = new ImageDialog();
                        Bundle args = new Bundle();
                        args.putString("weapon", textView.getText().toString());
                        imageDialog.setArguments(args);

                        imageDialog.show(getSupportFragmentManager(), "imageDialog");
                    }
                });
            }
        }

        ArrayList<TextView> placeTextViews = new ArrayList<>();
        placeTextViews.add(0, (TextView) findViewById(R.id.textView23));
        placeTextViews.add(1, (TextView) findViewById(R.id.textView24));
        placeTextViews.add(2, (TextView) findViewById(R.id.textView25));
        placeTextViews.add(3, (TextView) findViewById(R.id.textView26));
        placeTextViews.add(4, (TextView) findViewById(R.id.textView27));
        placeTextViews.add(5, (TextView) findViewById(R.id.textView28));
        placeTextViews.add(6, (TextView) findViewById(R.id.textView29));
        placeTextViews.add(7, (TextView) findViewById(R.id.textView30));
        placeTextViews.add(8, (TextView) findViewById(R.id.textView31));

        for (int i = 0; i < 9; i++) {
            placeTextViews.get(i).setText(places.get(i));
            if(game.getGameMode().equals(OUR_CLUEDO)) {
                final TextView textView = placeTextViews.get(i);
                placeTextViews.get(i).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ImageDialog imageDialog = new ImageDialog();
                        Bundle args = new Bundle();
                        args.putString("place", textView.getText().toString());
                        imageDialog.setArguments(args);

                        imageDialog.show(getSupportFragmentManager(), "imageDialog");
                    }
                });
            }
        }
    }

    private void setHideSwitcher() {
        final SwitchCompat hideAnswersSwitch = findViewById(R.id.switch1);
        hideAnswersSwitch.setVisibility(View.GONE);
    }

        private void setDeclareWinnerButton(){
        final Button declareWinnerButton = (Button) findViewById(R.id.declareWinnerButton);
        declareWinnerButton.setVisibility(View.GONE);
    }

    public void setEndGameButton(){
        Button endGameButton = (Button) findViewById(R.id.endGameButton);
        endGameButton.setVisibility(View.GONE);
    }

    public void setBackArrow(){
        ImageView backArrow = findViewById(R.id.backArrowShowPastGameImageView);
        backArrow.setVisibility(View.VISIBLE);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setColors(){
        TableRow suspectsRow = findViewById(R.id.suspectsRow);
        TableRow weaponsRow = findViewById(R.id.weaponsRow);
        TableRow placesRow = findViewById(R.id.placesRow);
        ConstraintLayout title = findViewById(R.id.titleGameActivity);
        LinearLayout background = findViewById(R.id.layoutGameActivity);
        if(GameStatus.getInstance().theme.equals(Parameters.PURPLE)){
            title.setBackgroundColor(Parameters.PURPLE_MAIN_COLOR);
            background.setBackgroundResource(R.drawable.screen_background);
            suspectsRow.setBackgroundColor(Parameters.PURPLE_MAIN_COLOR);
            ((TextView)suspectsRow.getChildAt(0)).setTextColor(Color.WHITE);
            weaponsRow.setBackgroundColor(Parameters.PURPLE_MAIN_COLOR);
            ((TextView)weaponsRow.getChildAt(0)).setTextColor(Color.WHITE);
            placesRow.setBackgroundColor(Parameters.PURPLE_MAIN_COLOR);
            ((TextView)placesRow.getChildAt(0)).setTextColor(Color.WHITE);
        } else if(GameStatus.getInstance().theme.equals(Parameters.GREEN)){
            title.setBackgroundColor(Parameters.GREEN_MAIN_COLOR);
            background.setBackgroundResource(R.drawable.screen_background_green);
            suspectsRow.setBackgroundColor(Parameters.GREEN_MAIN_COLOR);
            ((TextView)suspectsRow.getChildAt(0)).setTextColor(Color.WHITE);
            weaponsRow.setBackgroundColor(Parameters.GREEN_MAIN_COLOR);
            ((TextView)weaponsRow.getChildAt(0)).setTextColor(Color.WHITE);
            placesRow.setBackgroundColor(Parameters.GREEN_MAIN_COLOR);
            ((TextView)placesRow.getChildAt(0)).setTextColor(Color.WHITE);
        } else if(GameStatus.getInstance().theme.equals(Parameters.ORANGE)){
            title.setBackgroundColor(Parameters.ORANGE_MAIN_COLOR);
            background.setBackgroundResource(R.drawable.screen_background_orange);
            suspectsRow.setBackgroundColor(Parameters.ORANGE_MAIN_COLOR);
            ((TextView)suspectsRow.getChildAt(0)).setTextColor(Color.WHITE);
            weaponsRow.setBackgroundColor(Parameters.ORANGE_MAIN_COLOR);
            ((TextView)weaponsRow.getChildAt(0)).setTextColor(Color.WHITE);
            placesRow.setBackgroundColor(Parameters.ORANGE_MAIN_COLOR);
            ((TextView)placesRow.getChildAt(0)).setTextColor(Color.WHITE);
        } else if(GameStatus.getInstance().theme.equals(Parameters.BW)){
            title.setBackgroundColor(Parameters.BW_MAIN_COLOR);
            background.setBackgroundResource(R.drawable.screen_background_bw);
            suspectsRow.setBackgroundColor(Parameters.BW_MAIN_COLOR);
            ((TextView)suspectsRow.getChildAt(0)).setTextColor(Color.WHITE);
            weaponsRow.setBackgroundColor(Parameters.BW_MAIN_COLOR);
            ((TextView)weaponsRow.getChildAt(0)).setTextColor(Color.WHITE);
            placesRow.setBackgroundColor(Parameters.BW_MAIN_COLOR);
            ((TextView)placesRow.getChildAt(0)).setTextColor(Color.WHITE);
        }
        preferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("color", GameStatus.getInstance().theme);
        editor.apply();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.show_past_game_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.showPastGameMenu11:
                GameStatus.getInstance().theme = Parameters.GREEN;
                setColors();
                return true;
            case R.id.showPastGameMenu12:
                GameStatus.getInstance().theme = Parameters.PURPLE;
                setColors();
                return true;
            case R.id.showPastGameMenu13:
                GameStatus.getInstance().theme = Parameters.ORANGE;
                setColors();
                return true;
            case R.id.showPastGameMenu14:
                GameStatus.getInstance().theme = Parameters.BW;
                setColors();
                return true;
            default:super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }
}
