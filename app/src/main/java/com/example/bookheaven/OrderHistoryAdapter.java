package com.example.bookheaven;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder> {


    private List<OrderModel> orders;

    public OrderHistoryAdapter(List<OrderModel> orders) {
        this.orders = orders;
    }


    @NonNull
    @Override
    public OrderHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_history_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryAdapter.ViewHolder holder, int position) {
        OrderModel order = orders.get(position);
        holder.orderId.setText("Order ID: " + order.getId());
        holder.orderDate.setText("Date: " + order.getDate_time());
        holder.orderStatus.setText("Status: " + order.getOrder_Status());
        holder.totalPrice.setText("Total: Rs." + order.getTotal_Price());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), OrderTrakingActivity.class);
                intent.putExtra("order_id", order.getId());
                intent.putExtra("order_date", order.getDate_time());
                intent.putExtra("order_status", order.getOrder_Status());
                intent.putExtra("total_price", order.getTotal_Price());
                intent.putExtra("latitude", 6.6235288009441255);
                intent.putExtra("longitude",80.219531785047586);

                Log.i("BookHeaven-log-single", "order id: " + order.getId());
                Log.i("BookHeaven-log-single", "order date: " + order.getDate_time());
                Log.i("BookHeaven-log-single", "order status: " + order.getOrder_Status());
                Log.i("BookHeaven-log-single", "total price: " + order.getTotal_Price());
//                Log.i("BookHeaven-log-single", "latitude: " + BookDTO.getLatitude());
//                Log.i("BookHeaven-log-single", "longitude: " + BookDTO.getLongitude());

                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView orderId, orderDate, orderStatus, totalPrice;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderId = itemView.findViewById(R.id.order_name);
            orderDate = itemView.findViewById(R.id.order_date);
            orderStatus = itemView.findViewById(R.id.textView26);
            totalPrice = itemView.findViewById(R.id.order_price);
        }
    }
}
