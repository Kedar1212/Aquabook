package com.example.aquabook;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;


public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private List<Order> orderList;

    public OrderAdapter(List<Order> orderList) {
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.serviceNameTextView.setText(order.getServiceName());
        holder.totalPriceTextView.setText("Rs." + order.getTotalPrice());
        holder.nameTextView.setText(order.getName());
        holder.addressTextView.setText(order.getAddress());
        // Bind other views for order details
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView serviceNameTextView;
        TextView totalPriceTextView;
        TextView nameTextView;
        TextView addressTextView;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            serviceNameTextView = itemView.findViewById(R.id.orderServiceNameTextView);
            totalPriceTextView = itemView.findViewById(R.id.orderTotalPriceTextView);
            nameTextView = itemView.findViewById(R.id.orderNameTextView);
            addressTextView = itemView.findViewById(R.id.orderAddressTextView);
            // Initialize other views for order details
        }
    }
}

