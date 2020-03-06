package com.example.cluedokiler;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity {

    String[] players;
    String[] suspects;
    String[] weapons;
    String[] places;

    ArrayList<ArrayList<String>> gameTable;
    //HashMap<Integer,String> gameTableHash = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Resources res = getResources();
        players = res.getStringArray(R.array.players);
        suspects = res.getStringArray(R.array.suspects);
        weapons = res.getStringArray(R.array.weapons);
        places = res.getStringArray(R.array.places);

        ArrayList<String> playersNames = new ArrayList();


        if(getIntent().hasExtra("com.example.cluedokiller.players")){
            playersNames = getIntent().getExtras().getStringArrayList("com.example.cluedokiller.players");
        }

        ArrayList <TextView> playerTextViews = new ArrayList<>();
        playerTextViews.add(0,(TextView) findViewById(R.id.player1TextView));
        playerTextViews.add(1,(TextView) findViewById(R.id.player2TextView));
        playerTextViews.add(2,(TextView) findViewById(R.id.player3TextView));
        playerTextViews.add(3,(TextView) findViewById(R.id.player4TextView));
        playerTextViews.add(4,(TextView) findViewById(R.id.player5TextView));
        playerTextViews.add(5,(TextView) findViewById(R.id.player6TextView));

        nameViews();

        for (int i = 0; i<6;i++) {
            if(i<playersNames.size())
                playerTextViews.get(i).setText(playersNames.get(i));
            else {
                TableLayout gameTableLayout = (TableLayout) findViewById(R.id.gameTableLayout);
                gameTableLayout.setColumnCollapsed(i+1,true);
            }
        }

/*

        ArrayList <TextView> playerTextViews = new ArrayList<>();
        playerTextViews.add(0,(TextView) findViewById(R.id.player1TextView));

        playerTextViews.get(0).setOnHoverListener(new View.OnHoverListener() {
            @Override
            public boolean onHover(View v, MotionEvent event) {
                Intent imageActivity = new Intent(getApplicationContext(), ImageActivity.class);
                startActivity(imageActivity);
                return false;
            }
        });*/
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
        }

        setImages();


        GameStatus gameStatus = GameStatus.getInstance();
        TableLayout gameTableLayout = (TableLayout) findViewById(R.id.gameTableLayout);
        for (int i = 0; i < gameTableLayout.getChildCount(); i++) {
            TableRow gameTableRow = (TableRow) gameTableLayout.getChildAt(i);
            for (int x = 0; x < gameTableRow.getChildCount(); x++) {
                final View itemTableLayout = gameTableRow.getChildAt(x);
                if (itemTableLayout instanceof ImageView) {
                    ImageView tableImageView = (ImageView) itemTableLayout;

                    if(gameStatus.alreadySet) {
                        if (gameStatus.gameTableHash.get(tableImageView.getId()).equals("cross")) {
                            scaleImages((ImageView) itemTableLayout, R.drawable.cross);
                            tableImageView.setTag("cross");
                        } else if (gameStatus.gameTableHash.get(tableImageView.getId()).equals("empty")) {
                            scaleImages((ImageView) itemTableLayout, R.drawable.checkbox);
                            tableImageView.setTag("empty");
                        } else if (gameStatus.gameTableHash.get(tableImageView.getId()).equals("question")) {
                            scaleImages((ImageView) itemTableLayout, R.drawable.question);
                            tableImageView.setTag("question");
                        } else {
                            scaleImages((ImageView) itemTableLayout, R.drawable.tick);
                            tableImageView.setTag("tick");
                        }
                    }
                    else {
                        scaleImages((ImageView) itemTableLayout, R.drawable.checkbox);
                        tableImageView.setTag("empty");
                        gameStatus.gameTableHash.put(tableImageView.getId(), "empty");
                    }
                }
            }
        }
        gameStatus.alreadySet = true;




    }

    private void setImages(){
        final GameStatus gameStatus = GameStatus.getInstance();
        TableLayout gameTableLayout = (TableLayout) findViewById(R.id.gameTableLayout);
        for (int i = 0; i < gameTableLayout.getChildCount(); i++) {
            TableRow gameTableRow = (TableRow) gameTableLayout.getChildAt(i);
            for (int x = 0; x < gameTableRow.getChildCount(); x++) {
                final View itemTableLayout = gameTableRow.getChildAt(x);
                if(itemTableLayout instanceof ImageView){
                    final ImageView tableImageView = (ImageView) itemTableLayout;

                    itemTableLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if( tableImageView.getTag().equals("tick")) {
                                scaleImages((ImageView) itemTableLayout, R.drawable.cross);
                                tableImageView.setTag("cross");
                                gameStatus.gameTableHash.remove(tableImageView.getId());
                                gameStatus.gameTableHash.put(tableImageView.getId(),"cross");

                            }else if(  tableImageView.getTag().equals("question")) {
                                scaleImages((ImageView) itemTableLayout, R.drawable.checkbox);
                                tableImageView.setTag("empty");
                                gameStatus.gameTableHash.remove(tableImageView.getId());
                                gameStatus.gameTableHash.put(tableImageView.getId(),"empty");

                            }else if(  tableImageView.getTag().equals("cross")) {
                                scaleImages((ImageView) itemTableLayout, R.drawable.question);
                                tableImageView.setTag("question");
                                gameStatus.gameTableHash.remove(tableImageView.getId());
                                gameStatus.gameTableHash.put(tableImageView.getId(),"question");

                            }else {
                                scaleImages((ImageView) itemTableLayout, R.drawable.tick);
                                tableImageView.setTag("tick");
                                gameStatus.gameTableHash.remove(tableImageView.getId());
                                gameStatus.gameTableHash.put(tableImageView.getId(),"tick");
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
            int ratio = Math.round( ((float) screenWidth / (float) imgWidth) * ((float) 0.1111 ));
            scalingOptions.inSampleSize = ratio;
        }
        scalingOptions.inJustDecodeBounds = false;

        Bitmap scaleImg = BitmapFactory.decodeResource(getResources(),pic,scalingOptions);
        imageView.setImageBitmap(scaleImg);

    }

}
