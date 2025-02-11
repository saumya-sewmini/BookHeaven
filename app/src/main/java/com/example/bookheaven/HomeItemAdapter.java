package com.example.bookheaven;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class HomeItemAdapter extends RecyclerView.Adapter<HomeItemAdapter.HomeItemViewHolder> {
    private List<HomeItemModel> itemList;
    private OnItemClickListener listener;

    public HomeItemAdapter(List<HomeItemModel> itemList, OnItemClickListener listener) {
        this.itemList = itemList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public HomeItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_item_card, parent, false);
        return new HomeItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeItemViewHolder holder, int position) {
        HomeItemModel item = itemList.get(position);
        holder.title.setText(item.getTitle());
        holder.price.setText("Rs. " + item.getPrice());
        holder.image.setImageResource(item.getImageResId());

        // Handle Add to Cart Button Click
        holder.addToCartButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onAddToCartClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class HomeItemViewHolder extends RecyclerView.ViewHolder {
        TextView title, price;
        ImageView image;
        ImageButton addToCartButton;

        public HomeItemViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textView54);
            price = itemView.findViewById(R.id.textView55);
            image = itemView.findViewById(R.id.imageView22);
            addToCartButton = itemView.findViewById(R.id.imageButton7);
        }
    }

    // Interface for Click Events
    public interface OnItemClickListener {
        void onAddToCartClick(HomeItemModel item);
    }
}

