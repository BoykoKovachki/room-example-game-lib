package com.example.architectureexample.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.architectureexample.activities.R;
import com.example.architectureexample.database.Game;

import java.util.ArrayList;
import java.util.List;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameHolder> {

    private Context context;
    private final LayoutInflater inflater;

    private List<Game> games = new ArrayList<>();

    private OnItemClickedListener listener;
    private OnCheckedChangeListener favoriteListener;

    public void setGames(List<Game> games) {
        this.games = games;
        notifyDataSetChanged();
    }

    public GameAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public GameHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.game_item, parent, false);
        return new GameHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull GameHolder holder, int position) {
        Game currentGame = games.get(position);
        holder.title.setText(currentGame.getTitle());
        holder.description.setText(currentGame.getDescription());
        holder.yearReleased.setText(String.valueOf(currentGame.getYearReleased()));
        holder.type.setText(String.valueOf(currentGame.getType()));
        holder.favorite.setChecked(currentGame.isFavorite());
        Glide.with(context)
                .load(currentGame.getLogo())
                .fitCenter()
                .transform(new RoundedCorners(20))
                .into(holder.logo);
    }

    @Override
    public int getItemCount() {
        return games.size();
    }

    public Game getGameAt(int position) {
        return games.get(position);
    }

    class GameHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView description;
        private TextView yearReleased;
        private TextView type;
        private Switch favorite;
        private ImageView logo;

        public GameHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title_textview);
            description = itemView.findViewById(R.id.description_textview);
            yearReleased = itemView.findViewById(R.id.year_released_textview);
            type = itemView.findViewById(R.id.type_textview);
            favorite = itemView.findViewById(R.id.favorite_switch);
            logo = itemView.findViewById(R.id.logo_imageview);

            int position = getAdapterPosition();
            if (position != -1) {
                favorite.setOnCheckedChangeListener(null);
                favorite.setChecked(getGameAt(position).isFavorite());
            }
            favorite.setOnCheckedChangeListener((compoundButton, b) -> {
                if (compoundButton.isPressed()) {
                    int position1 = getAdapterPosition();
                    favoriteListener.onItemCheckedChange(getGameAt(position1), b);
                }
            });

            itemView.setOnClickListener(view -> {
                int position12 = getAdapterPosition();
                if (listener != null && position12 != RecyclerView.NO_POSITION) {
                    listener.onItemClicked(getGameAt(position12));
                }
            });
        }
    }

    public interface OnCheckedChangeListener {
        void onItemCheckedChange(Game game, boolean favorite);
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener favoriteListener) {
        this.favoriteListener = favoriteListener;
    }

    public interface OnItemClickedListener {
        void onItemClicked(Game game);
    }

    public void setOnItemClickedListener(OnItemClickedListener listener) {
        this.listener = listener;
    }

}
