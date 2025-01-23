package com.example.loanmanagementapp;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class UserScreen extends AppCompatActivity {
    private TabletLoan tabletLoan = new TabletLoan();
    private TabletLoanManager tabletLoanManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_screen);

        // Initialize TabletLoanManager
        tabletLoanManager = new TabletLoanManager(this);

        // Initialize GUI elements
        initGui();
    }

    private void initGui() {
        // Get references to UI elements
        Spinner brandSpinner = findViewById(R.id.brandSpinner);
        Spinner cabelSpinner = findViewById(R.id.cabelSpinner);
        EditText loanName = findViewById(R.id.loanNameEditText);
        EditText loanEmail = findViewById(R.id.loanEmailEditText);
        Button createButton = findViewById(R.id.createLoan);

        // Ensure UI elements are properly connected
        if (brandSpinner == null || cabelSpinner == null || loanName == null ||
                loanEmail == null || createButton == null) {
            Log.e("UserScreen", "One or more UI elements are not properly connected.");
            return;
        }

        setupSpinners(brandSpinner, cabelSpinner);

        // Set Spinner listeners
        brandSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Brand selectedBrand = (Brand) parent.getItemAtPosition(position);
                tabletLoan.setBrand(selectedBrand);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        cabelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Cabel selectedCabel = (Cabel) parent.getItemAtPosition(position);
                tabletLoan.setCabel(selectedCabel);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        // Set Create button listener
        createButton.setOnClickListener(view -> {
            String name = loanName.getText().toString().trim();
            String email = loanEmail.getText().toString().trim();
            String selectedBrand = brandSpinner.getSelectedItem().toString();
            String selectedCable = cabelSpinner.getSelectedItem().toString();

            // Validate input fields
            if (name.isEmpty()) {
                loanName.setError("Name is required");
                loanName.requestFocus();
            } else if (email.isEmpty()) {
                loanEmail.setError("Email is required");
                loanEmail.requestFocus();
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                loanEmail.setError("Enter a valid email");
                loanEmail.requestFocus();
            } else if (selectedBrand.equals("All Brands")) {
                // Show error for brand selection
                Toast.makeText(this, "Please select a brand", Toast.LENGTH_SHORT).show();
            } else if (selectedCable.equals("All Cables")) {
                // Show error for cable selection
                Toast.makeText(this, "Please select a cable", Toast.LENGTH_SHORT).show();
            } else {
                // Create and save loan
                createLoan(name, email);
                showReciptDialog(tabletLoan);
                tabletLoanManager.saveTabletLoan(tabletLoan);
            }
        });
    }

    private void createLoan(String name, String email) {
        tabletLoan.setLendersName(name);
        tabletLoan.setLendersEmail(email);
        tabletLoan.setDateForLoan(LocalDate.now().toString());
    }

    private void setupSpinners(Spinner brandSpinner, Spinner cabelSpinner) {
        List<Brand> brands = Arrays.asList(Brand.values());
        List<Cabel> cabels = Arrays.asList(Cabel.values());

        ArrayAdapter<Brand> brandAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, brands);
        brandAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        brandSpinner.setAdapter(brandAdapter);

        ArrayAdapter<Cabel> cabelAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cabels);
        cabelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cabelSpinner.setAdapter(cabelAdapter);
    }

    private void showReciptDialog(TabletLoan loan) {
        try {

            // Build receipt content
            String receiptContent = "Receipt:\n\n" +
                    "Brand: " + loan.getBrand() + "\n" +
                    "Cable: " + loan.getCabel() + "\n" +
                    "Lender's Name: " + loan.getLendersName() + "\n" +
                    "Lender's Email: " + loan.getLendersEmail() + "\n" +
                    "Date for Loan: " + loan.getDateForLoan();

            // Show dialog
            new AlertDialog.Builder(this)
                    .setTitle("Confirm Receipt")
                    .setMessage(receiptContent)
                    .setPositiveButton("Accept", (dialog, which) -> {
                        Toast.makeText(this, "Receipt Accepted", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> {
                        Toast.makeText(this, "Receipt Cancelled", Toast.LENGTH_SHORT).show();
                    })
                    .show();
        } catch (Exception e) {
            Log.e("UserScreen", "Error showing receipt dialog", e);
            Toast.makeText(this, "An error occurred while creating the receipt.", Toast.LENGTH_SHORT).show();
        }
    }
}
