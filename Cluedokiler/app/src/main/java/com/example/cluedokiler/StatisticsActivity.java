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
    DatabaseReference db;
    GameStatus gameStatus;
    int numGames;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        gameStatus = GameStatus.getInstance();

        playerTextView[1] = (TextView) findViewById(R.id.statTextView1);
        playerTextView[1].setText("Giocatore: " + gameStatus.playerName);



        playerTextView[2]= (TextView) findViewById(R.id.statTextView2);
        db = FirebaseDatabase.getInstance().getReference().child("PlayersRecord");
        numGames=0;

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                numGames=0;
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    if(data.getValue(Users.class).contains(gameStatus.playerName))
                        numGames = numGames+1;
                }
                playerTextView[2].setText("Numero di partite: " + numGames);
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
