package com.example.cluedokiler;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class PlayerSpinnerListener implements AdapterView.OnItemSelectedListener {

   String playerChoice;
   int index;
   int num;
   ArrayList<ArrayAdapter<String>> adapters;

    public PlayerSpinnerListener(ArrayList<ArrayAdapter<String>> arrayAdapters, int n){
        playerChoice = "--Vuoto--";
        adapters = arrayAdapters;
        num = n;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
       /* if(!playerChoice.equals("--Vuoto--"))
            for(int i=0; i<6; i++)
                if(i!=num)
                    adapters.get(i).add(playerChoice);*/

        playerChoice = parent.getItemAtPosition(position).toString();

        if(num==0)
            GameStatus.getInstance().playerName=playerChoice;

       /* if(!playerChoice.equals("--Vuoto--"))
            for(int i=0; i<6; i++)
                if(i!=num) {
                    index = position;
                    adapters.get(i).remove(playerChoice);
                }*/
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public String getPlayerChoice(){
        return playerChoice;
    }
}
