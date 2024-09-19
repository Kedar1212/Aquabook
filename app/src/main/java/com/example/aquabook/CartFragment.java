package com.example.aquabook;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment implements CartAdapter.OnTotalPriceChangeListener {

    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private List<CartItem> cartItemList;
    private DatabaseReference cartReference;
    private TextView totalPriceTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cart, container, false);

        cartReference = FirebaseDatabase.getInstance().getReference().child("cart");

        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        cartItemList = new ArrayList<>();
        cartAdapter = new CartAdapter(cartItemList, cartReference);
        recyclerView.setAdapter(cartAdapter);

        loadCartData();

        totalPriceTextView = rootView.findViewById(R.id.TotalPrice1);

        Button deleteButton = rootView.findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(v -> {
            cartAdapter.deleteSelectedItems();
        });

        Button confirmButton = rootView.findViewById(R.id.Cart_button);
        confirmButton.setOnClickListener(v -> {
            if (cartAdapter.getSelectedItems().isEmpty()) {
                // No items selected, show a toast
                Toast.makeText(getContext(), "Please select an item", Toast.LENGTH_SHORT).show();
            } else {


                List<String> selectedServiceNames = new ArrayList<>();

                for (CartItem selectedCartItem : cartAdapter.getSelectedItems()) {
                    selectedServiceNames.add(selectedCartItem.getServiceName());
                }
                double totalPrice = cartAdapter.calculateTotalPrice();
                // Pass the total price to the CheckoutFragment
                CheckoutFragment checkoutFragment = new CheckoutFragment();
                Bundle bundle = new Bundle();
                bundle.putDouble("totalPrice", totalPrice);
                bundle.putStringArrayList("selectedServiceNames", (ArrayList<String>) selectedServiceNames);
                checkoutFragment.setArguments(bundle);

                FragmentManager fragmentManager = getParentFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, checkoutFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        // Set the listener in the adapter
        cartAdapter.setOnTotalPriceChangeListener(this);

        return rootView;
    }

    private void loadCartData() {
        cartReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                cartItemList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    CartItem cartItem = snapshot.getValue(CartItem.class);
                    if (cartItem != null) {
                        cartItem.setKey(snapshot.getKey());
                        cartItemList.add(cartItem);
                    }
                }

                cartAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
            }
        });
    }

    // Implement the interface method to update the total price TextView
    @Override
    public void onTotalPriceChange(double totalPrice) {
        totalPriceTextView.setText("Rs." + totalPrice);
    }
}
