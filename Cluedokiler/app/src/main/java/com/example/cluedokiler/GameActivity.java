package com.example.cluedokiler;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity {

    String[] players;
    String[] suspects;
    String[] weapons;
    String[] places;
    DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Resources res = getResources();
        players = res.getStringArray(R.array.players);
        suspects = res.getStringArray(R.array.suspects);
        weapons = res.getStringArray(R.array.weapons);
        places = res.getStringArray(R.array.places);

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

        setAutocompleteButton();

        setEndGameButton();


    }


    private void setHideSwitcher(){
        final Switch hideAnswersSwitch = (Switch) findViewById(R.id.switch1);
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
            final TextView textView = susTextViews.get(i);
            susTextViews.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageDialog imageDialog = new ImageDialog();
                    Bundle args = new Bundle();
                    args.putString("player", textView.getText().toString());
                    imageDialog.setArguments(args);

                    imageDialog.show(getSupportFragmentManager(), "imageDialog");

                }
            });
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
            final TextView textView = weapTextViews.get(i);
            weapTextViews.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageDialog imageDialog = new ImageDialog();
                    Bundle args = new Bundle();
                    args.putString("player", textView.getText().toString());
                    imageDialog.setArguments(args);

                    imageDialog.show(getSupportFragmentManager(), "imageDialog");

                }
            });
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
            final TextView textView = placeTextViews.get(i);
            placeTextViews.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageDialog imageDialog = new ImageDialog();
                    Bundle args = new Bundle();
                    args.putString("player", textView.getText().toString());
                    imageDialog.setArguments(args);

                    imageDialog.show(getSupportFragmentManager(), "imageDialog");

                }
            });
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


    private void setAutocompleteButton(){
        Button autocomplete = (Button) findViewById(R.id.autocompleteButton);

        autocomplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });
    }

    public void setEndGameButton(){
        Button endGameButton = (Button) findViewById(R.id.endGameButton);
        final GameStatus gameStatus = GameStatus.getInstance();


        db = FirebaseDatabase.getInstance().getReference().child("GameRecord");

        endGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameStatus.gameTableHash.values();
                db.child(String.valueOf(gameStatus.gameNumber)).setValue(gameStatus.gameTableHash);

                ExitAlert exitAlert = new ExitAlert();

                exitAlert.show(getSupportFragmentManager(), "exitAlert");
            }
        });
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        final GameStatus gameStatus = GameStatus.getInstance();

        if(gameStatus.tableSet) {
            db = FirebaseDatabase.getInstance().getReference().child("PlayersRecord");

            db.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists())
                        gameStatus.gameNumber = (dataSnapshot.getChildrenCount());
                    else
                        gameStatus.gameNumber = Long.valueOf(0);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            db = FirebaseDatabase.getInstance().getReference().child("GameRecord");
            db.child(String.valueOf(gameStatus.gameNumber)).setValue(gameStatus.gameTableHash);
        }
    }
}
