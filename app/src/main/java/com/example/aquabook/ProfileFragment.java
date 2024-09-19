package com.example.aquabook;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {

    private TextView profileName, profileEmail, proflieUsername, profilePassword;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        profileName = view.findViewById(R.id.profileName2);
        profileEmail = view.findViewById(R.id.profileemail);
        proflieUsername = view.findViewById(R.id.profileusername);
        profilePassword = view.findViewById(R.id.profilepassword);


        showUserData();

        return view;
    }

    private void showUserData() {
        Intent intent = requireActivity().getIntent();

        String nameUser = intent.getStringExtra("name");
        String emailUser = intent.getStringExtra("email");
        String usernameUser = intent.getStringExtra("username");
        String passwordUser = intent.getStringExtra("password");


        profileName.setText(nameUser);
        profileEmail.setText(emailUser);
        proflieUsername.setText(usernameUser);
        profilePassword.setText(passwordUser);
    }
}
