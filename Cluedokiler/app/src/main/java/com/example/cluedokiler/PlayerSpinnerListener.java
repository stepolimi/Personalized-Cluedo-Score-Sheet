package com.example.cluedokiler;

import android.view.View;
import android.widget.AdapterView;

public class PlayerSpinnerListener implements AdapterView.OnItemSelectedListener {

   String playerChoice;

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        playerChoice = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public String getPlayerChoice(){
        return playerChoice;
    }
}
