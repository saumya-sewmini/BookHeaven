package com.example.bookheaven;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder>{

    private Context context;
    private List<CartItem> cartItems;

    public CartAdapter(Context context, List<CartItem> cartItems) {
        this.context = context;
        this.cartItems = cartItems;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_item, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = cartItems.get(position);
        holder.name.setText(item.getName());
        holder.price.setText("Rs." + (item.getPrice() * item.getQuantity()));
        holder.image.setImageResource(item.getImageResId());
        holder.qty.setText(String.valueOf(item.getQuantity()));

        // Increase Quantity
        holder.btnIncrease.setOnClickListener(v -> {
            item.setQuantity(item.getQuantity() + 1);
            holder.qty.setText(String.valueOf(item.getQuantity()));
            holder.price.setText("Rs." + (item.getPrice() * item.getQuantity()));
        });

        // Decrease Quantity (Minimum is 1)
        holder.btnDecrease.setOnClickListener(v -> {
            if (item.getQuantity() > 1) {
                item.setQuantity(item.getQuantity() - 1);
                holder.qty.setText(String.valueOf(item.getQuantity()));
                holder.price.setText("Rs." + (item.getPrice() * item.getQuantity()));
            }
        });

        // Fade-in animation
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.item_fade_in);
        holder.itemView.startAnimation(animation);

        // Handle Remove Click
        holder.remove.setOnClickListener(v -> {
            cartItems.remove(position);
            notifyItemRemoved(position);
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView name, price, qty;
        ImageView image, remove, btnIncrease, btnDecrease;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.cart_item_name);
            price = itemView.findViewById(R.id.cart_item_price);
            qty = itemView.findViewById(R.id.cart_item_qty);
            image = itemView.findViewById(R.id.cart_item_image);
            remove = itemView.findViewById(R.id.cart_item_remove);
            btnIncrease = itemView.findViewById(R.id.btn_increase);
            btnDecrease = itemView.findViewById(R.id.btn_decrease);
        }
    }


}
