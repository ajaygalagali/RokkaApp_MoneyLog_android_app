package com.astro.rokka.Expense;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.astro.rokka.R;

public class ExpenseMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_main);


    }

    public void newExpenseClicked(View view) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.expense_popup);
        final EditText editTextExpAmount = dialog.findViewById(R.id.editTextExpAmount);
        EditText editTextExpNote = dialog.findViewById(R.id.editTextTextExpNote);
        Button buttonExpAdd = dialog.findViewById(R.id.expAddBtn);
        dialog.show();
        buttonExpAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ExpenseMainActivity.this, String.valueOf(editTextExpAmount.getText()), Toast.LENGTH_SHORT).show();
            }
        });

    }
}