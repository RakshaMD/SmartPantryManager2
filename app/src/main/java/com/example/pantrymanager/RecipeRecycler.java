package com.example.pantrymanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecipeRecycler extends RecyclerView.Adapter<RecipeRecycler.ViewHolder> {

    public interface Listener {
        void open(Recipe recipe);
    }

    private List<Recipe> data;
    private Listener listener;

    public RecipeRecycler(List<Recipe> data, Listener listener) {
        this.data = data;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_match, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Recipe recipe = data.get(position);
        holder.name.setText(recipe.name);
        holder.info.setText("All required ingredients available");
        holder.itemView.setOnClickListener(view -> listener.open(recipe));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView info;

        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.tvRecipeName);
            info = view.findViewById(R.id.tvRecipeInfo);
        }
    }
}
