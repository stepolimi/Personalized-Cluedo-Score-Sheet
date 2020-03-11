package com.example.cluedokiler;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class CodeAlert extends DialogFragment {
    AlertDialog.Builder builder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        builder = new AlertDialog.Builder(getActivity());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.code_alert, container, false);

        final EditText editText = v.findViewById(R.id.codeEditText);

        Button yesButton = (Button) v.findViewById(R.id.yesButtonCodeAlert);
        Button noButton = (Button) v.findViewById(R.id.noButtonCodeAlert);

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameStatus.getInstance().confirmationCode = editText.getText().toString();
                if(editText.getText().toString().equals(getResources().getString(R.string.code)))
                    Toast.makeText(CodeAlert.super.getContext(), "codice accettato",Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(CodeAlert.super.getContext(), "codice rifiutato",Toast.LENGTH_SHORT).show();
                CodeAlert.this.dismiss();
            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CodeAlert.this.dismiss();
            }
        });

        builder.setView(v);

        return v;

    }
}
