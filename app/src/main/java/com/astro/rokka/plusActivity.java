package com.astro.rokka;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class plusActivity extends AppCompatActivity {
    TextView textViewName;
    EditText editText;

    String name;
    String stringTime;
    String userInput;
    String currentBalance;


    int currentTransaction;
    int updatedBalance;
    String position;
    int posiOne;


    SQLiteDatabase db;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plus);

        Intent i =getIntent();
        name = i.getStringExtra("name");
        currentBalance = i.getStringExtra("currentBalance");
        position = i.getStringExtra("position");
        assert position != null;
        posiOne = Integer.parseInt(position)+1;

        textViewName = findViewById(R.id.textViewPlus);
        editText = findViewById(R.id.editTextPlus);
        textViewName.setText(name+" ಜಮಾ");

    }

    public void plusClicked(View view) {
        userInput = String.valueOf(editText.getText());
        if(userInput.isEmpty()){
            Toast.makeText(this, "ಜಮಾ ಬರೆಯಿರಿ ", Toast.LENGTH_SHORT).show();
        }else {
            currentTransaction = Integer.parseInt(userInput);
            updatedBalance = Integer.parseInt(currentBalance) + currentTransaction;

            String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
            String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
            stringTime = currentDate + " " + currentTime;

            db = openOrCreateDatabase("rokk_db", MODE_PRIVATE, null);
            db.execSQL(String.format("UPDATE member_info SET mem_balance=%s WHERE id IS %s", updatedBalance, posiOne));
            Log.i("Table 1", "Updated");

            db.execSQL(String.format("INSERT INTO '%s' (transfer,date) VALUES(%s,'%s')", posiOne, currentTransaction, stringTime));
            Log.i("Table 2", "Inserted");

            Intent goToMain = new Intent(plusActivity.this, MainActivity.class);

            startActivity(goToMain);


        }
    }
}