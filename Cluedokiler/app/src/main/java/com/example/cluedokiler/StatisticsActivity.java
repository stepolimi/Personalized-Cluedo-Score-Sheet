package com.example.cluedokiler;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


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
        ImageView backButton = (ImageView) findViewById(R.id.backArrowImageView);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings_menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.settingsMenu1:
                Toast.makeText(this,"item2", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.settingsMenu2:
                Toast.makeText(this,"item3", Toast.LENGTH_SHORT).show();
                return true;
            default:super.onOptionsItemSelected(item);
        }



        return super.onOptionsItemSelected(item);
    }
}
