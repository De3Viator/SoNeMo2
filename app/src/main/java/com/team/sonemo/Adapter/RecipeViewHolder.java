package com.team.sonemo.Adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.team.sonemo.R;
import com.team.sonemo.Model.RelisList;

public class RecipeViewHolder extends RecyclerView.ViewHolder {

    private TextView filmName;
    private TextView dataRelis;
    private ImageView poster;

    public RecipeViewHolder(@NonNull View itemView) {
        super(itemView);

        filmName = itemView.findViewById(R.id.idFilmName);
        dataRelis = itemView.findViewById(R.id.idDataRelis);
        poster = itemView.findViewById(R.id.idPoster);

    }
    public void onBind(RelisList recipe, RelisListAdapter.OnItemTypeClickListener listener){
Picasso.get().load(recipe.getPosterId()).into(poster);
  filmName.setText(recipe.getFilmName()) ;
  dataRelis.setText(recipe.getFilmData());
  itemView.setOnClickListener(v -> listener.onClick(recipe));
    }
}
