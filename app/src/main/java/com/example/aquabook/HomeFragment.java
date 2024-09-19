package com.example.aquabook;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Random;

public class HomeFragment extends Fragment {

    private DatabaseReference servicesReference;
    private LinearLayout serviceNamesContainers;
    private ArrayList<ServiceModel> allServices;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("HomeFragment", "onCreateView called");
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        serviceNamesContainers = rootView.findViewById(R.id.serviceNamesContainers);
        // Add a null check
        if (serviceNamesContainers != null) {
            // Continue with your code
        } else {
            Log.e("HomeFragment", "serviceNamesContainers is null");
        }

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ImageSlider imageSlider = view.findViewById(R.id.imageSlider);
        ArrayList<SlideModel> slideModels = new ArrayList<>();

        slideModels.add(new SlideModel(R.drawable.img11, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.img2, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.img3, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.img4, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.img6, ScaleTypes.FIT));

        imageSlider.setImageList(slideModels, ScaleTypes.FIT);

        servicesReference = FirebaseDatabase.getInstance().getReference().child("services");

        servicesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<ServiceModel> serviceModels = new ArrayList<>();

                for (DataSnapshot serviceSnapshot : dataSnapshot.getChildren()) {
                    ServiceModel service = serviceSnapshot.getValue(ServiceModel.class);
                    if (service != null) {
                        serviceModels.add(service);
                    }
                }

                allServices = serviceModels; // Store all services data

                // Update the UI with service data
                updateServiceUI(serviceModels);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        });
    }

    public ArrayList<ServiceModel> getAllServices() {
        return allServices;
    }

    // New method to filter services based on the query
    public ArrayList<ServiceModel> filterServices(String query) {
        ArrayList<ServiceModel> filteredServices = new ArrayList<>();
        if (allServices != null) {
            for (ServiceModel service : allServices) {
                // Implement your filter logic here
                // For example, checking if the service name or location contains the query
                if (service.getServiceName().toLowerCase().contains(query.toLowerCase()) ||
                        service.getLocation().toLowerCase().contains(query.toLowerCase())) {
                    filteredServices.add(service);
                }
            }
        }
        return filteredServices;
    }

    private void updateServiceUI(ArrayList<ServiceModel> serviceModels) {
        serviceNamesContainers.removeAllViews();

        LinearLayout.LayoutParams cardLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                getResources().getDimensionPixelSize(R.dimen.card_height)
        );
        cardLayoutParams.setMargins(0, 0, 0, getResources().getDimensionPixelSize(R.dimen.card_spacing));

        for (ServiceModel service : serviceModels) {
            String serviceName = service.getServiceName();
            String serviceLocation = service.getLocation();

            CardView cardView = new CardView(requireContext());

            cardView.setRadius(getResources().getDimensionPixelSize(R.dimen.card_corner_radius));
            cardView.setCardElevation(getResources().getDimensionPixelSize(R.dimen.card_elevation));
            cardView.setLayoutParams(cardLayoutParams);

            LinearLayout verticalLayout = new LinearLayout(requireContext());
            verticalLayout.setOrientation(LinearLayout.VERTICAL);

            ImageView imageView = new ImageView(requireContext());
            imageView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    getResources().getDimensionPixelSize(R.dimen.card_height)
            ));

            int[] imageResources = {
                    R.drawable.pp1, R.drawable.pp3, R.drawable.pp2, R.drawable.pp5
            };
            int randomImageIndex = new Random().nextInt(imageResources.length);
            imageView.setImageResource(imageResources[randomImageIndex]);

            TextView textViewService = new TextView(requireContext());
            LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            textViewParams.setMargins(
                    getResources().getDimensionPixelSize(R.dimen.card_start_margin),
                    13, 0, 0);
            textViewService.setLayoutParams(textViewParams);

            textViewService.setText(serviceName);
            textViewService.setTextSize(25);
            textViewService.setTextColor(getResources().getColor(android.R.color.black));
            textViewService.setTypeface(null, Typeface.BOLD);

            TextView textViewLocation = new TextView(requireContext());
            LinearLayout.LayoutParams locationTextViewParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            locationTextViewParams.setMargins(
                    getResources().getDimensionPixelSize(R.dimen.card_start_margin),
                    0, 0, -15);
            textViewLocation.setLayoutParams(locationTextViewParams);

            textViewLocation.setText(serviceLocation);
            textViewLocation.setTextSize(18);
            textViewLocation.setTextColor(getResources().getColor(android.R.color.black));
            textViewLocation.setTypeface(null, Typeface.ITALIC);

            verticalLayout.addView(textViewService);
            verticalLayout.addView(textViewLocation);
            verticalLayout.addView(imageView);

            cardView.addView(verticalLayout);

            // Add click listener to open InfoFragment
            cardView.setOnClickListener(view -> {
                // Create an instance of InfoFragment
                InfoFragment infoFragment = new InfoFragment();

                // Pass the service data to InfoFragment
                Bundle args = new Bundle();
                args.putString("serviceName", serviceName);
                args.putString("serviceLocation", serviceLocation);
                args.putDouble("bottlePrice", service.getBottlePrice());
                args.putDouble("jarPrice", service.getJarPrice());
                infoFragment.setArguments(args);

                // Navigate to InfoFragment
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, infoFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            });

            serviceNamesContainers.addView(cardView);
        }
    }
}
