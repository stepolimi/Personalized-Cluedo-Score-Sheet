package com.example.cluedokiler;

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

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;

import static com.example.cluedokiler.Parameters.MyPREFERENCES;

public class GameActivity extends AppCompatActivity {

    String[] suspects;
    String[] weapons;
    String[] places;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setColors();

        suspects = GameStatus.getInstance().gameNames.getSuspects();
        weapons =  GameStatus.getInstance().gameNames.getWeapons();
        places =  GameStatus.getInstance().gameNames.getPlaces();

        final ArrayList <TextView> playerTextViews = new ArrayList<>();
        playerTextViews.add(0,(TextView) findViewById(R.id.player1TextView));
        playerTextViews.add(1,(TextView) findViewById(R.id.player2TextView));
        playerTextViews.add(2,(TextView) findViewById(R.id.player3TextView));
        playerTextViews.add(3,(TextView) findViewById(R.id.player4TextView));
        playerTextViews.add(4,(TextView) findViewById(R.id.player5TextView));
        playerTextViews.add(5,(TextView) findViewById(R.id.player6TextView));

        final GameStatus gameStatus = GameStatus.getInstance();
        for (int i = 0; i<7;i++) {
            if(i<gameStatus.playersNames.size()) {
                playerTextViews.get(i).setText(gameStatus.playersNames.get(i));
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
                if(i<6 || gameStatus.playersNames.size()==6) {
                    TableLayout gameTableLayout = (TableLayout) findViewById(R.id.gameTableLayout);
                    gameTableLayout.setColumnCollapsed(i + 1, true);
                }
            }
        }
        if(!gameStatus.tableSet)
                gameStatus.numCol = gameStatus.playersNames.size() + 1;

        setImages();

        nameViews();

        setHideSwitcher();

        restoreHighlights();

        setEndGameButton();

        setDeclareWinnerButton();

        OnBackPressedCallback callback = new OnBackPressedCallback(true ) {
            @Override
            public void handleOnBackPressed() {
                ExitAlert exitAlert = new ExitAlert();
                exitAlert.show(getSupportFragmentManager(), "exitAlert");
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    private void setColors(){
        Button endGameButton = (Button) findViewById(R.id.endGameButton);
        Button declareWinnerButton = (Button) findViewById(R.id.declareWinnerButton);
        TableRow suspectsRow = findViewById(R.id.suspectsRow);
        TableRow weaponsRow = findViewById(R.id.weaponsRow);
        TableRow placesRow = findViewById(R.id.placesRow);
        ConstraintLayout title = findViewById(R.id.titleGameActivity);
        LinearLayout background = findViewById(R.id.layoutGameActivity);
        if(GameStatus.getInstance().theme.equals(Parameters.PURPLE)){
            endGameButton.setBackgroundResource(R.drawable.button_background);
            declareWinnerButton.setBackgroundResource(R.drawable.button_background);
            title.setBackgroundColor(Parameters.PURPLE_MAIN_COLOR);
            background.setBackgroundResource(R.drawable.screen_background);
            suspectsRow.setBackgroundColor(Parameters.PURPLE_MAIN_COLOR);
            ((TextView)suspectsRow.getChildAt(0)).setTextColor(Color.WHITE);
            weaponsRow.setBackgroundColor(Parameters.PURPLE_MAIN_COLOR);
            ((TextView)weaponsRow.getChildAt(0)).setTextColor(Color.WHITE);
            placesRow.setBackgroundColor(Parameters.PURPLE_MAIN_COLOR);
            ((TextView)placesRow.getChildAt(0)).setTextColor(Color.WHITE);
            restoreHighlights();
        } else if(GameStatus.getInstance().theme.equals(Parameters.GREEN)){
            endGameButton.setBackgroundResource(R.drawable.button_background_green);
            declareWinnerButton.setBackgroundResource(R.drawable.button_background_green);
            title.setBackgroundColor(Parameters.GREEN_MAIN_COLOR);
            background.setBackgroundResource(R.drawable.screen_background_green);
            suspectsRow.setBackgroundColor(Parameters.GREEN_MAIN_COLOR);
            ((TextView)suspectsRow.getChildAt(0)).setTextColor(Color.WHITE);
            weaponsRow.setBackgroundColor(Parameters.GREEN_MAIN_COLOR);
            ((TextView)weaponsRow.getChildAt(0)).setTextColor(Color.WHITE);
            placesRow.setBackgroundColor(Parameters.GREEN_MAIN_COLOR);
            ((TextView)placesRow.getChildAt(0)).setTextColor(Color.WHITE);
            restoreHighlights();
        } else if(GameStatus.getInstance().theme.equals(Parameters.ORANGE)){
            endGameButton.setBackgroundResource(R.drawable.button_background_orange);
            declareWinnerButton.setBackgroundResource(R.drawable.button_background_orange);
            title.setBackgroundColor(Parameters.ORANGE_MAIN_COLOR);
            background.setBackgroundResource(R.drawable.screen_background_orange);
            suspectsRow.setBackgroundColor(Parameters.ORANGE_MAIN_COLOR);
            ((TextView)suspectsRow.getChildAt(0)).setTextColor(Color.WHITE);
            weaponsRow.setBackgroundColor(Parameters.ORANGE_MAIN_COLOR);
            ((TextView)weaponsRow.getChildAt(0)).setTextColor(Color.WHITE);
            placesRow.setBackgroundColor(Parameters.ORANGE_MAIN_COLOR);
            ((TextView)placesRow.getChildAt(0)).setTextColor(Color.WHITE);
            restoreHighlights();
        } else if(GameStatus.getInstance().theme.equals(Parameters.BW)){
            endGameButton.setBackgroundResource(R.drawable.button_background_bw);
            declareWinnerButton.setBackgroundResource(R.drawable.button_background_bw);
            title.setBackgroundColor(Parameters.BW_MAIN_COLOR);
            background.setBackgroundResource(R.drawable.screen_background_bw);
            suspectsRow.setBackgroundColor(Parameters.BW_MAIN_COLOR);
            ((TextView)suspectsRow.getChildAt(0)).setTextColor(Color.WHITE);
            weaponsRow.setBackgroundColor(Parameters.BW_MAIN_COLOR);
            ((TextView)weaponsRow.getChildAt(0)).setTextColor(Color.WHITE);
            placesRow.setBackgroundColor(Parameters.BW_MAIN_COLOR);
            ((TextView)placesRow.getChildAt(0)).setTextColor(Color.WHITE);
            restoreHighlights();
        }
        SharedPreferences preferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("color", GameStatus.getInstance().theme);
        editor.apply();
    }

    private void setHideSwitcher(){
        final SwitchCompat hideAnswersSwitch =  findViewById(R.id.switch1);
        final GameStatus gameStatus = GameStatus.getInstance();

        if(gameStatus.hideTable) {
            hideAnswersSwitch.setChecked(true);
            gameStatus.hideTable = true;

            TableLayout gameTableLayout = (TableLayout) findViewById(R.id.gameTableLayout);
            for (int i = 0; i < gameTableLayout.getChildCount(); i++) {
                TableRow gameTableRow = (TableRow) gameTableLayout.getChildAt(i);
                for (int x = 0; x < gameTableRow.getChildCount(); x++) {
                    final View itemTableLayout = gameTableRow.getChildAt(x);
                    if (itemTableLayout instanceof ImageView && (x<gameStatus.numCol || x==gameTableRow.getChildCount()-1)) {
                        ImageView tableImageView = (ImageView) itemTableLayout;

                        scaleImages((ImageView) itemTableLayout, R.drawable.checkbox);
                        tableImageView.setTag("hide");
                    }
                }
            }
        }
        hideAnswersSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hideAnswersSwitch.isChecked() && !gameStatus.hideTable) {
                    hideAnswersSwitch.setChecked(true);
                    gameStatus.hideTable = true;

                    TableLayout gameTableLayout = (TableLayout) findViewById(R.id.gameTableLayout);
                    for (int i = 0; i < gameTableLayout.getChildCount(); i++) {
                        TableRow gameTableRow = (TableRow) gameTableLayout.getChildAt(i);
                        for (int x = 0; x < gameTableRow.getChildCount(); x++) {
                            final View itemTableLayout = gameTableRow.getChildAt(x);
                            if (itemTableLayout instanceof ImageView && (x<gameStatus.numCol || x==gameTableRow.getChildCount()-1)) {
                                ImageView tableImageView = (ImageView) itemTableLayout;

                                scaleImages((ImageView) itemTableLayout, R.drawable.checkbox);
                                tableImageView.setTag("hide");
                            }
                        }
                    }
                }
                else{
                    hideAnswersSwitch.setChecked(false);
                    gameStatus.hideTable = false;
                    GameStatus gameStatus = GameStatus.getInstance();
                    TableLayout gameTableLayout = (TableLayout) findViewById(R.id.gameTableLayout);
                    for (int i = 0; i < gameTableLayout.getChildCount(); i++) {
                        TableRow gameTableRow = (TableRow) gameTableLayout.getChildAt(i);
                        for (int x = 0; x < gameTableRow.getChildCount(); x++) {
                            final View itemTableLayout = gameTableRow.getChildAt(x);
                            if (itemTableLayout instanceof ImageView && (x<gameStatus.numCol || x==gameTableRow.getChildCount()-1)) {
                                ImageView tableImageView = (ImageView) itemTableLayout;

                                if(gameStatus.tableSet) {
                                    if (gameStatus.gameTableHash.get(tableImageView.getId()).equals("cross")) {
                                        scaleImages((ImageView) itemTableLayout, R.drawable.cross);
                                        tableImageView.setTag("cross");
                                    } else if (gameStatus.gameTableHash.get(tableImageView.getId()).equals("empty")) {
                                        scaleImages((ImageView) itemTableLayout, R.drawable.checkbox);
                                        tableImageView.setTag("empty");
                                    } else if (gameStatus.gameTableHash.get(tableImageView.getId()).equals("question")) {
                                        scaleImages((ImageView) itemTableLayout, R.drawable.question);
                                        tableImageView.setTag("question");
                                    } else if(gameStatus.gameTableHash.get(tableImageView.getId()).equals("tick")){
                                        scaleImages((ImageView) itemTableLayout, R.drawable.tick);
                                        tableImageView.setTag("tick");
                                    } else if(gameStatus.gameTableHash.get(tableImageView.getId()).equals("question2")) {
                                        scaleImages((ImageView) itemTableLayout, R.drawable.question2);
                                        tableImageView.setTag("question2");
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });
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
            susTextViews.get(i).setText(suspects[i]);
            if(!GameStatus.getInstance().gameNames.isModified()) {
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
            weapTextViews.get(i).setText(weapons[i]);
            if(!GameStatus.getInstance().gameNames.isModified()) {
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
            placeTextViews.get(i).setText(places[i]);
            if(!GameStatus.getInstance().gameNames.isModified()) {
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

    private void setImages(){
        final GameStatus gameStatus = GameStatus.getInstance();
        TableLayout gameTableLayout = (TableLayout) findViewById(R.id.gameTableLayout);

        for (int i = 0; i < gameTableLayout.getChildCount(); i++) {
            TableRow gameTableRow = (TableRow) gameTableLayout.getChildAt(i);
            for (int x = 0; x < gameTableRow.getChildCount(); x++) {
                final View itemTableLayout = gameTableRow.getChildAt(x);
                if (itemTableLayout instanceof ImageView && (x<gameStatus.numCol || x==gameTableRow.getChildCount()-1)) {
                    ImageView tableImageView = (ImageView) itemTableLayout;
                    if(gameStatus.tableSet) {
                        if (gameStatus.gameTableHash.get(tableImageView.getId()).equals("cross")) {
                            scaleImages((ImageView) itemTableLayout, R.drawable.cross);
                            tableImageView.setTag("cross");
                        } else if (gameStatus.gameTableHash.get(tableImageView.getId()).equals("empty")) {
                            scaleImages((ImageView) itemTableLayout, R.drawable.checkbox);
                            tableImageView.setTag("empty");
                        } else if (gameStatus.gameTableHash.get(tableImageView.getId()).equals("question")) {
                            scaleImages((ImageView) itemTableLayout, R.drawable.question);
                            tableImageView.setTag("question");
                        } else if(gameStatus.gameTableHash.get(tableImageView.getId()).equals("tick")){
                            scaleImages((ImageView) itemTableLayout, R.drawable.tick);
                            tableImageView.setTag("tick");
                        } else if(gameStatus.gameTableHash.get(tableImageView.getId()).equals("question2")) {
                            scaleImages((ImageView) itemTableLayout, R.drawable.question2);
                            tableImageView.setTag("question2");
                        }
                    }
                    else {
                        scaleImages((ImageView) itemTableLayout, R.drawable.checkbox);
                        tableImageView.setTag("empty");
                        gameStatus.gameTableHash.put(String.valueOf(tableImageView.getId()), "empty");
                    }
                }
            }
        }
        gameStatus.tableSet = true;

        for (int i = 0; i < gameTableLayout.getChildCount(); i++) {
            TableRow gameTableRow = (TableRow) gameTableLayout.getChildAt(i);
            for (int x = 0; x < gameTableRow.getChildCount(); x++) {
                final View itemTableLayout = gameTableRow.getChildAt(x);
                if(itemTableLayout instanceof ImageView && (x<gameStatus.numCol || x==gameTableRow.getChildCount()-1)){
                    final ImageView tableImageView = (ImageView) itemTableLayout;

                    itemTableLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if( tableImageView.getTag().equals("tick")) {
                                scaleImages((ImageView) itemTableLayout, R.drawable.cross);
                                tableImageView.setTag("cross");
                                gameStatus.gameTableHash.remove(tableImageView.getId());
                                gameStatus.gameTableHash.put(String.valueOf(tableImageView.getId()),"cross");

                            }else if(  tableImageView.getTag().equals("question")) {
                                scaleImages((ImageView) itemTableLayout, R.drawable.question2);
                                tableImageView.setTag("question2");
                                gameStatus.gameTableHash.remove(tableImageView.getId());
                                gameStatus.gameTableHash.put(String.valueOf(tableImageView.getId()),"question2");

                            }else if(  tableImageView.getTag().equals("cross")) {
                                scaleImages((ImageView) itemTableLayout, R.drawable.question);
                                tableImageView.setTag("question");
                                gameStatus.gameTableHash.remove(tableImageView.getId());
                                gameStatus.gameTableHash.put(String.valueOf(tableImageView.getId()),"question");

                            }else if(  tableImageView.getTag().equals("empty")){
                                scaleImages((ImageView) itemTableLayout, R.drawable.tick);
                                tableImageView.setTag("tick");
                                gameStatus.gameTableHash.remove(tableImageView.getId());
                                gameStatus.gameTableHash.put(String.valueOf(tableImageView.getId()),"tick");
                            }else if(  tableImageView.getTag().equals("question2")){
                                scaleImages((ImageView) itemTableLayout, R.drawable.checkbox);
                                tableImageView.setTag("empty");
                                gameStatus.gameTableHash.remove(tableImageView.getId());
                                gameStatus.gameTableHash.put(String.valueOf(tableImageView.getId()),"empty");
                            }
                        }
                    });
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

    private void completeRows(){
        final GameStatus gameStatus = GameStatus.getInstance();
        TableLayout gameTableLayout = (TableLayout) findViewById(R.id.gameTableLayout);
        for (int i = 0; i < gameTableLayout.getChildCount(); i++) {
            TableRow gameTableRow = (TableRow) gameTableLayout.getChildAt(i);
            for (int x = 0; x < gameTableRow.getChildCount(); x++) {
                final View itemTableLayout = gameTableRow.getChildAt(x);
                if(itemTableLayout instanceof ImageView && (x<gameStatus.numCol || x==gameTableRow.getChildCount()-1)) {
                    final ImageView tableImageView = (ImageView) itemTableLayout;

                    if (tableImageView.getTag().equals("tick")) {
                        for (int k = 0; k < gameTableRow.getChildCount(); k++) {
                            final View crossItemTableLayout = gameTableRow.getChildAt(k);
                            if (crossItemTableLayout instanceof ImageView && k != x && (k<gameStatus.numCol || k==gameTableRow.getChildCount()-1)) {
                                final ImageView crossTableImageView = (ImageView) crossItemTableLayout;
                                scaleImages(crossTableImageView, R.drawable.cross);
                                crossTableImageView.setTag("cross");
                                gameStatus.gameTableHash.remove(crossTableImageView.getId());
                                gameStatus.gameTableHash.put(String.valueOf(crossTableImageView.getId()), "cross");
                            }
                        }
                    }
                }
            }
        }
    }

    private void restoreHighlights(){
        TableLayout gameTableLayout = (TableLayout) findViewById(R.id.gameTableLayout);
        for(Integer i: GameStatus.getInstance().highlightedRows){
            TableRow tableRow = (TableRow) gameTableLayout.getChildAt(i);
            if(GameStatus.getInstance().theme.equals(Parameters.PURPLE))
                tableRow.getChildAt(0).setBackgroundColor(Parameters.PURPLE_MAIN_COLOR);
            else if(GameStatus.getInstance().theme.equals(Parameters.GREEN))
                tableRow.getChildAt(0).setBackgroundColor(Parameters.GREEN_MAIN_COLOR);
            else if(GameStatus.getInstance().theme.equals(Parameters.ORANGE))
                tableRow.getChildAt(0).setBackgroundColor(Parameters.ORANGE_MAIN_COLOR);
            else if(GameStatus.getInstance().theme.equals(Parameters.BW))
                tableRow.getChildAt(0).setBackgroundColor(Parameters.BW_MAIN_COLOR);
            ((TextView) tableRow.getChildAt(0)).setTextColor(Color.WHITE);
        }
    }

    private void highlightObject(String name){
        TableLayout gameTableLayout = (TableLayout) findViewById(R.id.gameTableLayout);
        for (int i = 0; i < gameTableLayout.getChildCount(); i++) {
            TableRow tableRow = (TableRow) gameTableLayout.getChildAt(i);
            if (tableRow.getChildAt(0) instanceof TextView) {
                if(((TextView) tableRow.getChildAt(0)).getText().equals(name)) {
                    if(!GameStatus.getInstance().highlightedRows.contains(i)) {
                        if(GameStatus.getInstance().theme.equals(Parameters.PURPLE))
                            tableRow.getChildAt(0).setBackgroundColor(Parameters.PURPLE_MAIN_COLOR);
                        else if(GameStatus.getInstance().theme.equals(Parameters.GREEN))
                            tableRow.getChildAt(0).setBackgroundColor(Parameters.GREEN_MAIN_COLOR);
                        else if(GameStatus.getInstance().theme.equals(Parameters.ORANGE))
                            tableRow.getChildAt(0).setBackgroundColor(Parameters.ORANGE_MAIN_COLOR);
                        else if(GameStatus.getInstance().theme.equals(Parameters.BW))
                            tableRow.getChildAt(0).setBackgroundColor(Parameters.BW_MAIN_COLOR);
                        ((TextView) tableRow.getChildAt(0)).setTextColor(Color.WHITE);
                        GameStatus.getInstance().highlightedRows.add(i);
                    }else{
                        tableRow.getChildAt(0).setBackgroundColor(Color.TRANSPARENT);
                        ((TextView) tableRow.getChildAt(0)).setTextColor(Color.BLACK);
                        GameStatus.getInstance().highlightedRows.remove((Integer)i);
                    }
                    return;
                }
            }
        }
    }

    private void setDeclareWinnerButton(){
        final Button declareWinnerButton = (Button) findViewById(R.id.declareWinnerButton);

        declareWinnerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeclareWinnerDialog declareWinnerDialog = new DeclareWinnerDialog();
                declareWinnerDialog.show(getSupportFragmentManager(),"declareWinner");
            }
        });
    }

    public void setEndGameButton(){
        Button endGameButton = (Button) findViewById(R.id.endGameButton);
        endGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExitAlert exitAlert = new ExitAlert();
                exitAlert.show(getSupportFragmentManager(), "exitAlert");
            }
        });
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(GameStatus.getInstance().tableSet) {
            DbManager.getInstance().saveGameRecord();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.game_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.gameMenu1:
                completeRows();
                return true;
            case R.id.gameMenu5:
                CodeAlert codeAlert = new CodeAlert();
                codeAlert.show(getSupportFragmentManager(), "codeAlert");
                return true;
            case R.id.gameMenu61:
                GameStatus.getInstance().theme = Parameters.GREEN;
                setColors();
                return true;
            case R.id.gameMenu62:
                GameStatus.getInstance().theme = Parameters.PURPLE;
                setColors();
                return true;
            case R.id.gameMenu63:
                GameStatus.getInstance().theme = Parameters.ORANGE;
                setColors();
                return true;
            case R.id.gameMenu64:
                GameStatus.getInstance().theme = Parameters.BW;
                setColors();
                return true;
            default:
                highlightObject(item.getTitle().toString());
        }
        return super.onOptionsItemSelected(item);
    }
}
