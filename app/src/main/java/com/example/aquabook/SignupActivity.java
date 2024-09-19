package com.example.aquabook;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    EditText signupName, signupEmail, signupUsername, signupPassword;
    TextView loginRedirectText;
    Button signupButton;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        signupName = findViewById((R.id.signup_name));
        signupEmail = findViewById((R.id.signup_email));
        signupUsername = findViewById((R.id.signup_username));
        signupPassword = findViewById((R.id.signup_password));
        signupButton = findViewById((R.id.signup_button));
        loginRedirectText = findViewById((R.id.loginRedirectText));

        signupPassword.setTransformationMethod(new PasswordTransformationMethod());

        signupButton.setOnClickListener(view -> {
            if(validateUsername()&validatePassword()&validateName()&Validateemail()){

                firebaseDatabase = FirebaseDatabase.getInstance();
                reference = firebaseDatabase.getReference("users");

                String name = signupName.getText().toString();
                String email = signupEmail.getText().toString();
                String username = signupUsername.getText().toString();
                String password = signupPassword.getText().toString();

                HelperClass helperClass = new HelperClass(name,email,username,password);
                reference.child(username).setValue(helperClass);

                Toast.makeText(SignupActivity.this, "You have signup successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);

            }else{
                Toast.makeText(SignupActivity.this, "Insert all credientials ", Toast.LENGTH_SHORT).show();
            }
        });

        loginRedirectText.setOnClickListener(view -> {
            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(intent);
        });

    }


    public Boolean validateUsername(){
        String val = signupUsername.getText().toString();
        if (val.isEmpty()){
            signupUsername.setError("Username cannot be empty");
            return false;

        }else {
            signupUsername.setError(null);
            return true;
        }

    }

    public Boolean validatePassword(){
        String val = signupPassword.getText().toString().trim();
        String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).{8,}$";

        if (val.isEmpty()) {
            signupPassword.setError("Password cannot be empty");
            return false;
        } else if (!val.matches(passwordPattern)) {
            signupPassword.setError("Password must contain at least 1 uppercase letter, 1 lowercase letter, 1 digit, and 1 special character. Minimum length is 8 characters.");
            return false;
        } else {
            signupPassword.setError(null);
            return true;
        }

    }

    public Boolean validateName(){
        String val = signupName.getText().toString();
        if (val.isEmpty()){
            signupName.setError("Name cannot be empty");
            return false;

        }else {
            signupName.setError(null);
            return true;
        }

    }

    public Boolean Validateemail(){
        String val = signupEmail.getText().toString().trim();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty()) {
            signupEmail.setError("Email cannot be empty");
            return false;
        } else if (!val.matches(emailPattern)) {
            signupEmail.setError("Enter a valid email address");
            return false;
        } else {
            signupEmail.setError(null);
            return true;
        }

    }

}