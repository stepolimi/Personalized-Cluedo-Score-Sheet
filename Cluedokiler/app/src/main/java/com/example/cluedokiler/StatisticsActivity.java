package com.example.cluedokiler;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StatisticsActivity extends AppCompatActivity {

    TextView[] playerTextView = new TextView[10];
    DatabaseReference db,db2,db3,db4;
    GameStatus gameStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        gameStatus = GameStatus.getInstance();

        playerTextView[1] = (TextView) findViewById(R.id.statTextView1);
        playerTextView[1].setText("Giocatore: " + gameStatus.playerName);



        playerTextView[2]= (TextView) findViewById(R.id.statTextView2);
        db = FirebaseDatabase.getInstance().getReference().child("PlayersRecord");

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int numGames=0;
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    if(data.getValue(Users.class).getPlayers().get(0).equals(gameStatus.playerName))
                        numGames = numGames+1;
                }
                playerTextView[2].setText("Numero di partite giocate: " + numGames);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        playerTextView[3]= (TextView) findViewById(R.id.statTextView3);
        db2 = FirebaseDatabase.getInstance().getReference().child("GameRecord");
        db2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int numTicks=0;
                for (DataSnapshot data: dataSnapshot.getChildren()){
                    if(data.getValue(GameTable.class).getPlayer().equals(gameStatus.playerName))
                        numTicks += data.getValue(GameTable.class).getNumTick();
                }
                playerTextView[3].setText("Totale spunte inserite: " + numTicks);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        playerTextView[4]= (TextView) findViewById(R.id.statTextView5);
        db3 = FirebaseDatabase.getInstance().getReference().child("GameRecord");
        db3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int numCrosses=0;
                for (DataSnapshot data: dataSnapshot.getChildren()){
                    if(data.getValue(GameTable.class).getPlayer().equals(gameStatus.playerName))
                        numCrosses += data.getValue(GameTable.class).getNumCross();
                }
                playerTextView[4].setText("Totale croci inserite: " + numCrosses);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        playerTextView[5]= (TextView) findViewById(R.id.statTextView4);
        db4 = FirebaseDatabase.getInstance().getReference().child("GameRecord");
        db4.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int numTicks=0;
                for (DataSnapshot data: dataSnapshot.getChildren()){
                    if(data.getValue(GameTable.class).getPlayer().equals(gameStatus.playerName))
                        numTicks += data.getValue(GameTable.class).getNumIncerts();
                }
                playerTextView[5].setText("Totale spazi rimasti incerti: " + numTicks);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        setBackButton();

    }

    private void setBackButton() {
        Button backButton = (Button) findViewById(R.id.backButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
