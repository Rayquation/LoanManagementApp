package com.example.loanmanagementapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AdminScreen extends AppCompatActivity {
    private TabletLoanManager tabletLoanManager;
    private List<TabletLoan> allLoans;
    private List<TabletLoan> filteredLoans;

    private Spinner brandFilterSpinner;
    private Spinner cabelFilterSpinner;
    private ListView loanListView;
    private ArrayAdapter<String> loanAdapter;
    private Button clearFiltersButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_screen);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //Initialize elements from view
        brandFilterSpinner = findViewById(R.id.brandFilterSpinner);
        cabelFilterSpinner = findViewById(R.id.cabelFilterSpinner);
        loanListView = findViewById(R.id.loanListView);

        // Initialize TabletLoanManager and load loans
        tabletLoanManager = new TabletLoanManager(this);
        allLoans = tabletLoanManager.getAllTabletLoans(); // Fetch data
        filteredLoans = new ArrayList<>(allLoans);

        setupFilters(brandFilterSpinner,cabelFilterSpinner);
        setupLoanListView();
    }

    private void setupFilters(Spinner brandSpinner,Spinner cabelSpinner) {
        // Create lists for filters
        List<Brand> brands = Arrays.asList(Brand.values());
        List<Cabel> cabels = Arrays.asList(Cabel.values());

        //Create adapters for Brand and Cabel
        ArrayAdapter<Brand> brandAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, brands);
        brandAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        brandSpinner.setAdapter(brandAdapter);

        ArrayAdapter<Cabel> cabelAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cabels);
        cabelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cabelSpinner.setAdapter(cabelAdapter);

        // Add listeners to filter spinners
        brandFilterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterLoans();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        cabelFilterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterLoans();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setupLoanListView() {
        // Create adapter for ListView
        loanAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, formatLoanList(filteredLoans));
        loanListView.setAdapter(loanAdapter);

        // Set item click listener for deletion
        loanListView.setOnItemClickListener((parent, view, position, id) -> {
            TabletLoan selectedLoan = filteredLoans.get(position);
            allLoans.remove(selectedLoan); // Remove from main list
            filterLoans(); // Refresh filtered list
            Toast.makeText(this, "Loan deleted", Toast.LENGTH_SHORT).show();
        });
    }

    private void filterLoans() {
        // Get selected filters
        String selectedBrand = brandFilterSpinner.getSelectedItem().toString();
        String selectedCabel = cabelFilterSpinner.getSelectedItem().toString();

        // Apply filters
        filteredLoans = allLoans.stream()
                .filter(loan -> ("ALL".equals(selectedBrand) || selectedBrand.equals(loan.getBrand().toString())) &&
                        ("ALL".equals(selectedCabel) || selectedCabel.equals(loan.getCabel().toString())))
                .collect(Collectors.toList());

        // Update ListView
        loanAdapter.clear();
        loanAdapter.addAll(formatLoanList(filteredLoans));
        loanAdapter.notifyDataSetChanged();
    }

    private List<String> formatLoanList(List<TabletLoan> loans) {

        List<String> formatted = new ArrayList<>();
        for (TabletLoan loan : loans) {
            // Format each detail in a separate line (column-style)
            String formattedLoan = "Loan:\n"
                    + "Brand: " + loan.getBrand() + "\n"
                    + "Cable: " + loan.getCabel() + "\n"
                    + "Lender's Name: " + loan.getLendersName() + "\n"
                    + "Lender's Email: " + loan.getLendersEmail()+ "\n"
                    + "Date for Loan: " + loan.getDateForLoan();
            formatted.add(formattedLoan);
        }
        return formatted;
    }
}
