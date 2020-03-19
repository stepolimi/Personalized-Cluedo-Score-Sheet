package com.example.cluedokiler.profile.statistics;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cluedokiler.R;

import java.util.ArrayList;

public class PersonalStatsAdapter extends RecyclerView.Adapter<PersonalStatsAdapter.MyViewHolder> {
    private ArrayList<PersonalStats> personalStats;
    private PersonalStatsAdapter.OnItemClickListener clickListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(PersonalStatsAdapter.OnItemClickListener listener){
        clickListener = listener;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView statistic;
        public TextView value;

        public MyViewHolder(@NonNull View itemView, final PersonalStatsAdapter.OnItemClickListener listener) {
            super(itemView);

            statistic = itemView.findViewById(R.id.statPersonalStats) ;
            value = itemView.findViewById(R.id.valuePersonalStat);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null) {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION)
                            listener.onItemClick(position);
                    }
                }
            });
        }
    }

    public PersonalStatsAdapter(ArrayList<PersonalStats> personalStats) {
        this.personalStats = personalStats;
    }

    @NonNull
    @Override
    public PersonalStatsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.personal_stats_recycler_view_item,parent, false);
        PersonalStatsAdapter.MyViewHolder viewHolder = new PersonalStatsAdapter.MyViewHolder(v,clickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PersonalStatsAdapter.MyViewHolder holder, int position) {
        PersonalStats personalStat  = personalStats.get(position);

        holder.statistic.setText(personalStat.getStatistic());
        holder.value.setText(personalStat.getValue());
    }

    @Override
    public int getItemCount() {
        return personalStats.size();
    }
}
