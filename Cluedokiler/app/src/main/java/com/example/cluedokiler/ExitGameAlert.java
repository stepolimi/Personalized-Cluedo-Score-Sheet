package com.example.cluedokiler;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class ExitGameAlert extends DialogFragment {
    AlertDialog.Builder builder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        builder = new AlertDialog.Builder(getActivity());

//        Bundle args = getArguments();
//        String name = args.getString("player");

        final Context context = super.getContext();


        CharSequence text;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.exit_alert, container, false);


        Button yesButton = (Button) v.findViewById(R.id.yesButton);
        Button noButton = (Button) v.findViewById(R.id.noButton);

        TextView confirmRequestTextView = (TextView) v.findViewById(R.id.confirmRequestTextView);

        confirmRequestTextView.setText("Sei sicuro di voler uscire?");

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameStatus.getInstance().resetGame();

                getActivity().finish();
                ExitGameAlert.this.dismiss();
            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExitGameAlert.this.dismiss();
            }
        });

        builder.setView(v);

        return v;
    }
}
