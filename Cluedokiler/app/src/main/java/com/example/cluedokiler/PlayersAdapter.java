package com.example.cluedokiler;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class PlayersAdapter extends ArrayAdapter<String> {

    public PlayersAdapter(Context context, ArrayList<String> players){
        super(context, 0, players);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position,convertView,parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = initView(position,convertView,parent);
        TextView textView = view.findViewById(R.id.spinnerTextView);
        textView.setGravity(Gravity.START);
        textView.setPadding(20,15,20,15);
        
        return view;
    }

    private View initView(int position, View convertView, ViewGroup parent){
        if(convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_drop_down_view,parent,false);

        TextView textView = convertView.findViewById(R.id.spinnerTextView);
        String name = getItem(position);
        if(name != null){
            textView.setText(name);
        }
        return convertView;
    }
}
