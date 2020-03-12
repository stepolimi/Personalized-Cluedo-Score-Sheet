package com.example.cluedokiler;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class ExitAlert extends DialogFragment {
    AlertDialog.Builder builder;

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
                gameStatus.gameTableHash.values();

                DbManager.getInstance().saveGameRecord();

                if(!GameStatus.getInstance().winner.equals("")) {
                    if (GameStatus.getInstance().confirmationCode.equals(getResources().getString(R.string.code)))
                        DbManager.getInstance().saveValidatedGame();
                    else
                        DbManager.getInstance().saveUnValidatedGame();
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