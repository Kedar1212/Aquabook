// com.example.aquabook.InfoFragment.java
package com.example.aquabook;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class InfoFragment extends Fragment {

    private String bottt;
    private DatabaseReference cartReference;
    private String jarrr;
    private double bottleDouble;
    private double jarDouble, totalPrice;
    private String serviceName;

    public InfoFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_info, container, false);

        // Initialize the cartReference
        cartReference = FirebaseDatabase.getInstance().getReference().child("cart");

        Bundle args = getArguments();
        double bottlePrice = 0;
        double jarPrice = 0;

        if (args != null) {
            serviceName = args.getString("serviceName");
            String serviceLocation = args.getString("serviceLocation");
            bottlePrice = args.getDouble("bottlePrice");
            jarPrice = args.getDouble("jarPrice");

            TextView serviceNameTextView = rootView.findViewById(R.id.servicenameTextView);
            TextView serviceLocationTextView = rootView.findViewById(R.id.serviceLocationTextView);
            TextView bottlePriceTextView = rootView.findViewById(R.id.bottlePriceTextView);
            TextView jarPriceTextView = rootView.findViewById(R.id.jarPriceTextView);
            TextView totalPriceTextView = rootView.findViewById(R.id.TotalPrice);
            TextView Temp_1 = rootView.findViewById(R.id.Temp_1);
            TextView Temp_2 = rootView.findViewById(R.id.Temp_2);
            EditText quantityBottleEditText = rootView.findViewById(R.id.QuantityBottle);
            EditText quantityJarEditText = rootView.findViewById(R.id.QuantityJar);

            serviceNameTextView.setText(serviceName);
            serviceLocationTextView.setText(serviceLocation);
            bottlePriceTextView.setText("Rs." + bottlePrice);
            jarPriceTextView.setText("Rs." + jarPrice);

            if (bottlePrice > 0) {
                quantityBottleEditText.setVisibility(View.VISIBLE);
                Temp_1.setVisibility(View.VISIBLE);
                bottlePriceTextView.setVisibility(View.VISIBLE);

            } else {
                quantityBottleEditText.setVisibility(View.GONE);
                Temp_1.setVisibility(View.GONE);
                bottlePriceTextView.setVisibility(View.GONE);
            }

            if (jarPrice > 0) {
                quantityJarEditText.setVisibility(View.VISIBLE);
                Temp_2.setVisibility(View.VISIBLE);
                jarPriceTextView.setVisibility(View.VISIBLE);

            } else {
                quantityJarEditText.setVisibility(View.GONE);
                Temp_2.setVisibility(View.GONE);
                jarPriceTextView.setVisibility(View.GONE);
            }

            double finalBottlePrice = bottlePrice;
            double finalJarPrice = jarPrice;
            quantityBottleEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    bottt = charSequence.toString();
                    updateTotalPrice(finalBottlePrice, finalJarPrice, totalPriceTextView);
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    updateTotalPrice(finalBottlePrice, finalJarPrice, totalPriceTextView);
                }
            });

            quantityJarEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    jarrr = charSequence.toString();
                    updateTotalPrice(finalBottlePrice, finalJarPrice, totalPriceTextView);
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    updateTotalPrice(finalBottlePrice, finalJarPrice, totalPriceTextView);
                }
            });

            Button addToCartButton = rootView.findViewById(R.id.profile_button);
            addToCartButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addToCart(serviceName, totalPrice);
                }
            });
        }

        return rootView;
    }

    private void updateTotalPrice(double bottlePrice, double jarPrice, TextView totalPriceTextView) {
        double totalBottlePrice = 0.0;
        double totalJarPrice = 0.0;

        if (bottt != null && !bottt.isEmpty()) {
            bottleDouble = Double.parseDouble(bottt);
            totalBottlePrice = bottlePrice * bottleDouble;
        }

        if (jarrr != null && !jarrr.isEmpty()) {
            jarDouble = Double.parseDouble(jarrr);
            totalJarPrice = jarPrice * jarDouble;
        }

        totalPrice = totalBottlePrice + totalJarPrice;
        totalPriceTextView.setText("Rs." + totalPrice);
    }

    private void addToCart(String serviceName, double totalPrice) {
        // Use the constructor that includes the key parameter
        String cartItemKey = cartReference.push().getKey();
        CartItem cartItem = new CartItem(cartItemKey, serviceName, totalPrice);
        cartReference.child(cartItemKey).setValue(cartItem);
        Toast.makeText(requireContext(), "Item added to cart", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageSlider imageSlider = view.findViewById(R.id.imageSlider);
        ArrayList<SlideModel> slideModels = new ArrayList<>();

        slideModels.add(new SlideModel(R.drawable.img2, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.img3, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.img6, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.img4, ScaleTypes.FIT));

        imageSlider.setImageList(slideModels, ScaleTypes.FIT);
    }
}
