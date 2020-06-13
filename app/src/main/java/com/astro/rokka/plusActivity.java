package com.astro.rokka;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

public class plusActivity extends AppCompatActivity {
    TextView textViewName;
    EditText editText;
    String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plus);

        textViewName = findViewById(R.id.textViewPlus);
        editText = findViewById(R.id.editTextPlus);

        Intent i =getIntent();
        name = i.getStringExtra("name");
        String position = i.getStringExtra("position");

        textViewName.setText(name+" ಕೊಡು");

//        Log.i("Position",position);
//        Log.i("Name",name)
//        Log.i("Obj",String.valueOf(HomeList.class.getFields()));
    }

    public void plusClicked(View view) {
        String userInput = String.valueOf(editText.getText());
        int currentBalance = AddNewNameActivity.map.get(name).getBalance();
        int updatedBalance = Integer.valueOf(userInput)+currentBalance;
        AddNewNameActivity.map.get(name).setBalance(Integer.valueOf(updatedBalance));
        MainActivity.homeListAdapter.notifyDataSetChanged();
        finish();


    }
}