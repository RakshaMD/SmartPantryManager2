package com.example.pantrymanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

public class PantryRecycler extends RecyclerView.Adapter<PantryRecycler.ViewHolder> {

    public interface Listener {
        void edit(PantryItems item);
        void delete(PantryItems item);
    }

    private final List<PantryItems> data;
    private final Listener listener;

    public PantryRecycler(List<PantryItems> data, Listener listener) {
        this.data = data;
        this.listener = listener;
    }

    //“RecyclerView warned that using notifyDataSetChanged() is inefficient because it refreshes the entire list.
    //I replaced it with notifyItemRangeChanged(), which updates only the affected items.
    //This improves performance and follows Android best practices.”
    public void setData(List<PantryItems> newData) {
        this.data.clear();
        this.data.addAll(newData);
        notifyItemRangeChanged(0, data.size());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pantryitem_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PantryItems item = data.get(position);

        holder.name.setText(item.name);
        holder.quantity.setText(
                String.format(Locale.US, "%.2f %s", item.quantity, item.unit)
        );

        if (item.expiry == null || item.expiry.isEmpty()) {
            holder.expiry.setText(R.string.no_expiry);
        } else {
            holder.expiry.setText(
                    holder.itemView.getContext().getString(
                            R.string.expiry_with_date,
                            item.expiry
                    )
            );
        }

        holder.edit.setOnClickListener(view -> listener.edit(item));
        holder.delete.setOnClickListener(view -> listener.delete(item));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView quantity;
        TextView expiry;
        Button edit;
        Button delete;

        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.tvName);
            quantity = view.findViewById(R.id.tvQty);
            expiry = view.findViewById(R.id.tvExpiry);
            edit = view.findViewById(R.id.btnEdit);
            delete = view.findViewById(R.id.btnDelete);
        }
    }
}
