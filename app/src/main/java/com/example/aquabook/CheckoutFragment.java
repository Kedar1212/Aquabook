package com.example.aquabook;

import android.Manifest;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CheckoutFragment extends Fragment implements PaymentResultListener {

    private DatabaseReference orderReference;
    private TextView totalPriceTextView;
    private View rootView;
    private EditText editTextName, editTextPhone, editTextAddress;
    private Button buttonProceedToPay;

    private static final int SMS_PERMISSION_REQUEST_CODE = 123;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_checkout, container, false);

        orderReference = FirebaseDatabase.getInstance().getReference().child("orders");

        Bundle bundle = getArguments();
        if (bundle != null) {
            double totalPrice = bundle.getDouble("totalPrice", 0.0);
            ArrayList<String> selectedServiceNames = bundle.getStringArrayList("selectedServiceNames");
            updateTotalPrice(totalPrice);
        }

        editTextName = rootView.findViewById(R.id.editTextName);
        editTextPhone = rootView.findViewById(R.id.editTextPhone);
        editTextAddress = rootView.findViewById(R.id.editTextAddress);
        buttonProceedToPay = rootView.findViewById(R.id.buttonProceedToPay);

        buttonProceedToPay.setEnabled(false);

        editTextName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkInputsAndEnableButton();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        editTextPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkInputsAndEnableButton();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        editTextAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkInputsAndEnableButton();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        buttonProceedToPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.SEND_SMS)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(requireActivity(),
                            new String[]{Manifest.permission.SEND_SMS},
                            SMS_PERMISSION_REQUEST_CODE);
                } else {
                    String name = editTextName.getText().toString();
                    String phone = editTextPhone.getText().toString().trim();

                    // Validate the phone number
                    if (isValidPhoneNumber(phone)) {
                        String address = editTextAddress.getText().toString();
                        double totalPrice = getArguments().getDouble("totalPrice", 0.0);
                        ArrayList<String> selectedServiceNames = getArguments().getStringArrayList("selectedServiceNames");
                        String serviceName = TextUtils.join(", ", selectedServiceNames);

                        Order order = new Order(name, phone, address, serviceName, totalPrice);
                        orderReference.push().setValue(order);
                        sendOrderConfirmationSMS();
                        startPayment();
                    } else {
                        Toast.makeText(requireContext(), "Invalid phone number", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        return rootView;
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        // Validate that the phone number is 10 digits and starts with the country code for India
        return phoneNumber.matches("^[7-9][0-9]{9}$");
    }

    private void updateTotalPrice(double totalPrice) {
        totalPriceTextView = rootView.findViewById(R.id.TotalPrice2);
        totalPriceTextView.setText("Rs." + totalPrice);
    }

    private void checkInputsAndEnableButton() {
        boolean allInputsValid = !TextUtils.isEmpty(editTextName.getText())
                && !TextUtils.isEmpty(editTextPhone.getText())
                && !TextUtils.isEmpty(editTextAddress.getText());

        buttonProceedToPay.setEnabled(allInputsValid);
    }

    private void sendOrderConfirmationSMS() {
        try {
            String message = "Thank you for placing your order! Your order has been received.";
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(editTextPhone.getText().toString(), null, message, null, null);
            Toast.makeText(requireContext(), "Order confirmation SMS sent!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(requireContext(), "Failed to send SMS: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void startPayment() {
        double totalPrice = getArguments().getDouble("totalPrice", 0.0);

        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_0Ai9LGCMxjKMa7");  // Replace with your Razorpay key
        checkout.setImage(R.drawable.logo2);

        JSONObject object = new JSONObject();
        try {
            object.put("name", "Aquabook");
            object.put("description", "Test Payment");
            object.put("theme", "#0093DD");
            object.put("currency", "INR");
            object.put("amount", String.valueOf((int) (totalPrice * 100)));  // Convert to paisa

            // Get the phone number entered by the user
            String phoneNumber = editTextPhone.getText().toString();
            object.put("prefill.contact", phoneNumber);
            object.put("prefill.email", "aquabook0@gmail.com");

            checkout.open(requireActivity(), object);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Payment Success");
        builder.setMessage("Payment ID: " + s);
        builder.show();
        Log.d("Razorpay", "Payment Success: " + s);
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(requireContext(), "Payment Error: " + s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == SMS_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sendOrderConfirmationSMS();
                startPayment();
            } else {
                Toast.makeText(requireContext(), "SMS permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
