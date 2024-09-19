package com.example.aquabook;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class SettingsFragment extends Fragment {

    private EditText location, serviceName, jarPriceEditText, bottlePriceEditText, phoneNumberEditText;
    private ImageView proofImageView;

    private Button submitButton;
    private Spinner spinner;
    private Uri proofImageUri;

    private DatabaseReference servicesReference;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        location = rootView.findViewById(R.id.AddServiceLocation1);
        serviceName = rootView.findViewById(R.id.AddServiceName1);
        proofImageView = rootView.findViewById(R.id.ProofImageView1);
        submitButton = rootView.findViewById(R.id.submit_button1);
        spinner = rootView.findViewById(R.id.item_spinner1);
        jarPriceEditText = rootView.findViewById(R.id.jarPriceEditText);
        bottlePriceEditText = rootView.findViewById(R.id.bottlePriceEditText);
        phoneNumberEditText = rootView.findViewById(R.id.phoneNumberEditText);

        // Initialize services reference
        servicesReference = FirebaseDatabase.getInstance().getReference().child("services");

        // Initialize Firebase Storage reference
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference("proof_images");

        ArrayList<String> arrIds = new ArrayList<>();
        arrIds.add("Bottles");
        arrIds.add("Jars");
        arrIds.add("Both");

        ArrayAdapter<String> stringAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, arrIds);
        spinner.setAdapter(stringAdapter);

        proofImageView.setOnClickListener(view -> {
            openImagePicker();
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedItem = spinner.getSelectedItem().toString();

                switch (selectedItem) {
                    case "Bottles":
                        jarPriceEditText.setVisibility(View.GONE);
                        bottlePriceEditText.setVisibility(View.VISIBLE);
                        break;
                    case "Jars":
                        jarPriceEditText.setVisibility(View.VISIBLE);
                        bottlePriceEditText.setVisibility(View.GONE);
                        break;
                    case "Both":
                        jarPriceEditText.setVisibility(View.VISIBLE);
                        bottlePriceEditText.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle the case where nothing is selected.
            }
        });

        submitButton.setOnClickListener(view -> {
            String serviceLocation = location.getText().toString();
            String serviceTitle = serviceName.getText().toString();
            String selectedItem = spinner.getSelectedItem().toString();
            String phoneNumberText = phoneNumberEditText.getText().toString();
            String jarPriceText = jarPriceEditText.getText().toString();
            String bottlePriceText = bottlePriceEditText.getText().toString();



            if (serviceLocation.isEmpty() || serviceTitle.isEmpty() || proofImageUri == null) {
                Toast.makeText(getActivity(), "Please fill in all the fields and select a proof photo", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!isValidImageFormat(proofImageUri)) {
                Toast.makeText(getActivity(), "Please select a JPEG or PNG image", Toast.LENGTH_SHORT).show();
                return;
            }

            double jarPrice = jarPriceText.isEmpty() ? 0.0 : Double.parseDouble(jarPriceText);
            double bottlePrice = bottlePriceText.isEmpty() ? 0.0 : Double.parseDouble(bottlePriceText);

            StorageReference imageRef = storageReference.child(System.currentTimeMillis() + ".jpg");
            UploadTask uploadTask = imageRef.putFile(proofImageUri);
            uploadTask.addOnSuccessListener(taskSnapshot -> {
                imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    String imageUrl = uri.toString();

                    ServiceModel service = new ServiceModel(serviceTitle, serviceLocation, selectedItem, imageUrl, jarPrice, bottlePrice, phoneNumberText);

                    String serviceKey = servicesReference.push().getKey();
                    servicesReference.child(serviceKey).setValue(service);

                    Toast.makeText(getActivity(), "Service added successfully!", Toast.LENGTH_SHORT).show();
                });
            }).addOnFailureListener(exception -> {
                exception.printStackTrace();
                Toast.makeText(getActivity(), "Failed to upload image.", Toast.LENGTH_SHORT).show();
            });
        });

        return rootView;
    }

    private boolean isValidImageFormat(Uri imageUri) {
        ContentResolver contentResolver = getActivity().getContentResolver();
        String mimeType = contentResolver.getType(imageUri);
        return mimeType != null && (mimeType.equals("image/jpeg") || mimeType.equals("image/png"));
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            proofImageUri = data.getData();
            proofImageView.setImageURI(proofImageUri);
        }
    }
}
