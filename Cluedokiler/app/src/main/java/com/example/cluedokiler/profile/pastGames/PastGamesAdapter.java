package com.example.cluedokiler.profile.pastGames;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cluedokiler.R;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static com.example.cluedokiler.parameters.Parameters.HARRY_CLUEDO;
import static com.example.cluedokiler.parameters.Parameters.OUR_CLUEDO;
import static com.example.cluedokiler.parameters.Parameters.STANDARD_CLUEDO;

public class PastGamesAdapter extends RecyclerView.Adapter<PastGamesAdapter.MyViewHolder> {

    private ArrayList<PastGame> pastGames;
    private OnItemClickListener clickListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        clickListener = listener;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView date;
        public TextView winner;
        public TextView players;
        public TextView gameMode;

        public MyViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            date = itemView.findViewById(R.id.datePastGame);
            winner = itemView.findViewById(R.id.winnerPastGame);
            players = itemView.findViewById(R.id.playersPastGame) ;
            gameMode = itemView.findViewById(R.id.gameModePastGame);

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

    public PastGamesAdapter(ArrayList<PastGame> pastGames) {
        this.pastGames = pastGames;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.past_games_recycler_view_item,parent, false);
        MyViewHolder viewHolder = new MyViewHolder(v,clickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        PastGame pastGame  = pastGames.get(position);
        Date gameDate = new Date(Long.parseLong(pastGame.getDate()));
        String date = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, Locale.ITALY).format(gameDate);
        String mode;

        holder.date.setText("Data: " + date );
        holder.winner.setText( "Vincitore: " + pastGame.getWinner());
        String players  = "Giocatori: ";
        if(pastGame.getPlayers()!=null)
            for(String name: pastGame.getPlayers())
                players = players + ", " + name;
        holder.players.setText(players);

        if(pastGame.getGameMode().equals(HARRY_CLUEDO))
            mode = "Harry Potter";
        else if(pastGame.getGameMode().equals(STANDARD_CLUEDO) )
            mode = "Classico";
        else if(pastGame.getGameMode().equals(OUR_CLUEDO))
            mode = "Sondrio crime";
        else
            mode = "Personalizzata";

        holder.gameMode.setText("Modalità: " + mode);
    }

    @Override
    public int getItemCount() {
        return pastGames.size();
    }

}
