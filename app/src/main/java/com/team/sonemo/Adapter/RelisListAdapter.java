package com.team.sonemo.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.team.sonemo.R;
import com.team.sonemo.Model.RelisList;

import java.util.ArrayList;
import java.util.List;

public class RelisListAdapter extends RecyclerView.Adapter<RecipeViewHolder> {

    private List<RelisList> relisLists = new ArrayList<>();

    public RelisListAdapter(OnItemTypeClickListener listener) {
        this.listener = listener;
    }

    private OnItemTypeClickListener listener;

    public void setList(List<RelisList> relisLists) {
        this.relisLists = relisLists;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.relis_item, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        holder.onBind(relisLists.get(position), listener);

    }

    @Override
    public int getItemCount() { return relisLists.size();}

    public interface OnItemTypeClickListener{
        void onClick(RelisList item);

    }

}
