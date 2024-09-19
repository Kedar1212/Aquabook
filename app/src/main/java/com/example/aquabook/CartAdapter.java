package com.example.aquabook;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<CartItem> cartItems;
    private DatabaseReference cartReference;

    public CartAdapter(List<CartItem> cartItems, DatabaseReference cartReference) {
        this.cartItems = cartItems;
        this.cartReference = cartReference;
    }

    public interface OnTotalPriceChangeListener {
        void onTotalPriceChange(double totalPrice);
    }

    private OnTotalPriceChangeListener onTotalPriceChangeListener;

    public void setOnTotalPriceChangeListener(OnTotalPriceChangeListener listener) {
        this.onTotalPriceChangeListener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem cartItem = cartItems.get(position);
        holder.serviceNameTextView.setText(cartItem.getServiceName());
        holder.totalPriceTextView.setText("Rs." + cartItem.getTotalPrice());
        holder.checkBox.setChecked(cartItem.isSelected());

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            cartItem.setSelected(isChecked);
            updateTotalPrice();
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public void deleteSelectedItems() {
        for (int i = cartItems.size() - 1; i >= 0; i--) {
            if (cartItems.get(i).isSelected()) {
                String cartItemKey = cartItems.get(i).getKey();
                cartReference.child(cartItemKey).removeValue();
                cartItems.remove(i);
                notifyItemRemoved(i);
                updateTotalPrice();
            }
        }
    }

    public double calculateTotalPrice() {
        double total = 0;
        for (CartItem cartItem : cartItems) {
            if (cartItem.isSelected()) {
                total += cartItem.getTotalPrice();
            }
        }
        return total;
    }

    public List<CartItem> getSelectedItems() {
        List<CartItem> selectedItems = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            if (cartItem.isSelected()) {
                selectedItems.add(cartItem);
            }
        }
        return selectedItems;
    }

    private void updateTotalPrice() {
        double totalPrice = calculateTotalPrice();
        if (onTotalPriceChangeListener != null) {
            onTotalPriceChangeListener.onTotalPriceChange(totalPrice);
        }
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView serviceNameTextView;
        TextView totalPriceTextView;
        CheckBox checkBox;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            serviceNameTextView = itemView.findViewById(R.id.serviceNameTextView);
            totalPriceTextView = itemView.findViewById(R.id.totalPriceTextView);
            checkBox = itemView.findViewById(R.id.checkbox);
        }
    }
}

