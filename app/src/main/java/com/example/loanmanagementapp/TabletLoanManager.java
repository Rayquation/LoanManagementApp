package com.example.loanmanagementapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class TabletLoanManager {

    private SharedPreferences sharedPreferences;
    private Gson gson;

    public TabletLoanManager(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        gson = new Gson();
    }

    // Save a TabletLoan object
    public void saveTabletLoan(TabletLoan tabletLoan) {
        List<TabletLoan> allLoans = getAllTabletLoans(); // Get existing loans
        allLoans.add(tabletLoan); // Add new loan to the list

        String tabletLoanJson = gson.toJson(allLoans); // Convert the list to JSON
        sharedPreferences.edit()
                .putString("tablet_loans", tabletLoanJson) // Save the list as a string
                .apply(); // Apply changes
    }

    // Retrieve a TabletLoan object
    public TabletLoan getTabletLoan() {
        String tabletLoanJson = sharedPreferences.getString("tablet_loan", null);
        if (tabletLoanJson != null) {
            return gson.fromJson(tabletLoanJson, TabletLoan.class); // Convert JSON back to TabletLoan
        }
        return null; // Return null if no data is saved
    }

    // Clear the saved TabletLoan
    public void clearTabletLoan() {
        sharedPreferences.edit()
                .remove("tablet_loan")
                .apply();
    }

    // Get all saved TabletLoan objects
    public List<TabletLoan> getAllTabletLoans() {
        String tabletLoanJson = sharedPreferences.getString("tablet_loans", null);
        if (tabletLoanJson != null) {
            // Define the type for List<TabletLoan> (generic)
            Type type = new TypeToken<List<TabletLoan>>(){}.getType();
            return gson.fromJson(tabletLoanJson, type); // Convert JSON back to List<TabletLoan>
        }
        return new ArrayList<>(); // Return empty list if no data is saved
    }

    // Clear all saved TabletLoans
    public void clearAllTabletLoans() {
        sharedPreferences.edit()
                .remove("tablet_loans")
                .apply();
    }
}
