package com.example.loanmanagementapp;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class AdminScreen extends AppCompatActivity {
    private TabletLoanManager tabletLoanManager;
    private List<TabletLoan> allLoans;
    private List<TabletLoan> filteredLoans;

    private Spinner brandFilterSpinner;
    private Spinner cabelFilterSpinner;
    private ListView loanListView;
    private ArrayAdapter<String> loanAdapter;
    private Button datePickerButton;
    private TextView selectedDateTextView;
    private Button resetFiltersButton;


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
        selectedDateTextView = findViewById(R.id.selectedDateTextView);
        datePickerButton = findViewById(R.id.datePickerButton);
        resetFiltersButton = findViewById(R.id.resetFiltersButton);

        // Initialize TabletLoanManager and load loans
        tabletLoanManager = new TabletLoanManager(this);
        allLoans = tabletLoanManager.getAllTabletLoans(); // Fetch data
        filteredLoans = new ArrayList<>(allLoans);


        setupFilters(brandFilterSpinner,cabelFilterSpinner);
        setupDateFilter(selectedDateTextView, datePickerButton);
        setupLoanListView();

        resetFiltersButton.setOnClickListener(v -> resetFilters());
    }
    private void resetFilters() {
        // Reset spinners to "ALL" or first option
        brandFilterSpinner.setSelection(0);
        cabelFilterSpinner.setSelection(0);

        // Reset date filter
        selectedDateTextView.setText("No Date Selected");

        // Reset filtered loans to all loans
        filteredLoans = new ArrayList<>(allLoans);

        // Update ListView
        loanAdapter.clear();
        loanAdapter.addAll(formatLoanList(filteredLoans));
        loanAdapter.notifyDataSetChanged();

        Toast.makeText(this, "Filters reset", Toast.LENGTH_SHORT).show();
    }
    private void setupDateFilter(TextView selectedDateTextView, Button datePickerButton) {
        final Calendar calendar = Calendar.getInstance();

        datePickerButton.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (view, year, month, dayOfMonth) -> {
                        // Format the selected date as "YYYY-MM-DD" and display it
                        String selectedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth);
                        selectedDateTextView.setText(selectedDate);
                        filterLoans(); // Trigger filtering when a date is selected
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });
    }


    private void setupFilters(Spinner brandSpinner,Spinner cabelSpinner) {
        // Create lists for filters
        List<Brand> brands = Arrays.asList(Brand.values());
        List<Cabel> cabels = Arrays.asList(Cabel.values());

        //Create adapters for Brand and Cabel
        //adapters serve as intermediaries that bridge the gap between a data source and a user
        //interface component, particularly in graphical user interfaces (GUIs)
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

            // Remove loan using TabletLoanManager
            tabletLoanManager.deleteLoan(selectedLoan);
            // Update the lists and UI
            filteredLoans.remove(selectedLoan); // Update filtered list
            allLoans = tabletLoanManager.getAllTabletLoans(); // Refresh all loans from storage
            filterLoans(); // Refresh filtered loans

            Toast.makeText(this, "Loan deleted", Toast.LENGTH_SHORT).show();
        });
    }

    private void filterLoans() {
        // Get selected filters
        String selectedBrand = brandFilterSpinner.getSelectedItem().toString();
        String selectedCabel = cabelFilterSpinner.getSelectedItem().toString();
        String selectedDate = selectedDateTextView.getText().toString();
        filteredLoans = allLoans.stream()
                .filter(loan -> ("ALL".equals(selectedBrand) || selectedBrand.equals(loan.getBrand().toString())) &&
                        ("ALL".equals(selectedCabel) || selectedCabel.equals(loan.getCabel().toString())) &&
                        ("No Date Selected".equals(selectedDate) || selectedDate.equals(loan.getDateForLoan())))
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
