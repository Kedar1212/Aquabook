package com.example.aquabook;

import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class SearchFragment extends Fragment {

    private LinearLayout searchResultsContainer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        searchResultsContainer = rootView.findViewById(R.id.searchResultsContainer);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SearchView searchView = view.findViewById(R.id.SearchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Check if the parent fragment is an instance of HomeFragment
                if (getParentFragment() instanceof HomeFragment) {
                    // Call filterServices method in HomeFragment to get filtered services
                    ArrayList<ServiceModel> filteredServices = ((HomeFragment) getParentFragment()).filterServices(query);

                    // Update the UI with filtered services
                    updateSearchResultsUI(filteredServices);
                }

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Handle text changes if needed
                return false;
            }
        });
    }

    private void updateSearchResultsUI(ArrayList<ServiceModel> filteredServices) {
        searchResultsContainer.removeAllViews();

        for (ServiceModel service : filteredServices) {
            // Create a new TextView for each service
            TextView serviceNameTextView = new TextView(requireContext());
            serviceNameTextView.setText(service.getServiceName());
            serviceNameTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18); // Adjust text size if needed
            serviceNameTextView.setTextColor(Color.BLACK); // Adjust text color if needed
            serviceNameTextView.setPadding(16, 16, 16, 16); // Add padding if needed

            // Add a click listener to handle interactions with the service
            serviceNameTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Handle click on the service (e.g., open details, navigate, etc.)
                    // You can customize this based on your app's requirements
                    // For example:
                    Toast.makeText(requireContext(), "Service clicked: " + service.getServiceName(), Toast.LENGTH_SHORT).show();
                }
            });

            // Add the TextView to the searchResultsContainer
            searchResultsContainer.addView(serviceNameTextView);

            // Add a horizontal line as a separator between services
            View separator = new View(requireContext());
            separator.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    1 // Height of the separator (adjust as needed)
            ));
            separator.setBackgroundColor(Color.LTGRAY); // Color of the separator
            searchResultsContainer.addView(separator);
        }
    }
}
