package com.example.cluedokiler;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class DeclareWinnerDialog extends DialogFragment implements AdapterView.OnItemSelectedListener {
    AlertDialog.Builder builder;
    String winner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        builder = new AlertDialog.Builder(getActivity());

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.declare_winner_dialog, container, false);

        Spinner winnerSpinner = (Spinner) v.findViewById(R.id.winnerSpinner);
        ArrayAdapter arrayAdapter = new ArrayAdapter(super.getContext(), android.R.layout.simple_spinner_item, GameStatus.getInstance().playersNames);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        winnerSpinner.setAdapter(arrayAdapter);
        winnerSpinner.setOnItemSelectedListener(this);


        Button yesButton = (Button) v.findViewById(R.id.yesButtonWinnerDialog);
        Button noButton = (Button) v.findViewById(R.id.noButtonWinnerDialog);

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameStatus.getInstance().winner = winner;
                DeclareWinnerDialog.this.dismiss();
            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeclareWinnerDialog.this.dismiss();
            }
        });

        builder.setView(v);

        return v;

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        winner = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
