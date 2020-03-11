package com.example.cluedokiler;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ExitAlert extends DialogFragment {
    AlertDialog.Builder builder;
    Long indexValid,indexUnValid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        builder = new AlertDialog.Builder(getActivity());

    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.exit_alert, container, false);


        Button yesButton = (Button) v.findViewById(R.id.yesButton);
        Button noButton = (Button) v.findViewById(R.id.noButton);

        TextView confirmRequestTextView = (TextView) v.findViewById(R.id.confirmRequestTextView);

        confirmRequestTextView.setText("Sei sicuro di voler abbandonare la partita?");

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final GameStatus gameStatus = GameStatus.getInstance();
                DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("GameRecord");
                gameStatus.gameTableHash.values();
                db.child(String.valueOf(gameStatus.gameNumber)).setValue(gameStatus.gameTableHash);


                if(!GameStatus.getInstance().winner.equals("")) {
                    if (GameStatus.getInstance().confirmationCode.equals(getResources().getString(R.string.code))) {

                        db= FirebaseDatabase.getInstance().getReference().child("ValidatedGame");
                        db.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists())
                                    indexValid=(dataSnapshot.getChildrenCount());
                                else
                                    indexValid= Long.valueOf(0);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        db.child(String.valueOf(indexValid)+1).setValue(GameStatus.getInstance().winner);

                    }else {
                        db= FirebaseDatabase.getInstance().getReference().child("UnValidatedGame");
                        db.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists())
                                    indexUnValid=(dataSnapshot.getChildrenCount());
                                else
                                    indexUnValid= Long.valueOf(0);
                            }

//todo: indexing system not working

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        db.child(String.valueOf(indexValid)+1).setValue(GameStatus.getInstance().winner);
                    }
                }
                GameStatus.getInstance().newGame();
                getActivity().finish();
                ExitAlert.this.dismiss();
            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExitAlert.this.dismiss();
            }
        });

        builder.setView(v);

        return v;
    }
}