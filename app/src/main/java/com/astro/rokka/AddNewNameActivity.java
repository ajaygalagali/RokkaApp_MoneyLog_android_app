package com.astro.rokka;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class AddNewNameActivity extends AppCompatActivity {
    EditText editTextName;
    EditText editTextBalance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_name);

        editTextName = findViewById(R.id.editTextTextPersonName);
        editTextBalance = findViewById(R.id.editTextNumberSigned);
    }



    public void addNameClicked(View view) {

        String Name = String.valueOf(editTextName.getText());
        String balance = String.valueOf(editTextBalance.getText());
        if(balance.isEmpty()){
            balance="0";
        }

        MainActivity.arrayList.add(new HomeList(Integer.valueOf(balance),Name,R.drawable.plus,R.drawable.minus));
        MainActivity.homeListAdapter.notifyDataSetChanged();
        finish();

    }
}