package com.example.loanmanagementapp;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class StartScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initGui();
    }
    private void initGui(){
        //Initiating our buttons
        Button userBtn = findViewById(R.id.userBtn);
        Button adminBtn = findViewById(R.id.adminBtn);

        //Set a on click listener to our user button
        userBtn.setOnClickListener(view->{
            Intent intent = new Intent(StartScreen.this,UserScreen.class );
            startActivity(intent);
        });

        //Set a on click listener to our admin button
        adminBtn.setOnClickListener(view->{
            //Create a dialog for password input
            AlertDialog.Builder builder = new AlertDialog.Builder(StartScreen.this);
            builder.setTitle("Enter Admin Password");

            //Create an EditText for password input
            final EditText input = new EditText(StartScreen.this);
            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            builder.setView(input);

            //Set up the dialog buttons
            builder.setPositiveButton("OK",((dialog, which) -> {
                String enteredPassword = input.getText().toString();
                String adminPassword = "Passw0rd";

                if(enteredPassword.equals(adminPassword)){
                    //Correct password, navigate to adminScreen
                    Intent intent = new Intent(StartScreen.this,AdminScreen.class);
                    startActivity(intent);
                }else{
                    //Incorrect password, show a Toast message
                    Toast.makeText(StartScreen.this, "Incorrect password!", Toast.LENGTH_SHORT).show();
                }
            }));
            builder.setNegativeButton("Cancel",(dialog, which) -> {
                dialog.cancel();
            });
            builder.show();
        });
    }
}