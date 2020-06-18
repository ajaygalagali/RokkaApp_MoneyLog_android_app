package com.astro.rokka;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class plusActivity extends AppCompatActivity {
    TextView textViewName, textViewPagar,textViewRem;
    EditText editTextAal, editTextGiven, editTextNote;

    Spinner spinner;

    String name, stringTime, currentBalance,position,given, note;
    int currentTransaction,updatedBalance, posiOne,aal, totalPagar, spinnerValue, remPagar;

    ArrayList<Integer> spinnerList;
    ArrayAdapter<Integer> spinnerAdapter;

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
        textViewPagar = findViewById(R.id.textViewPagar);
        textViewRem = findViewById(R.id.textViewUlliddidu);

        editTextAal = findViewById(R.id.editTextaal);
        editTextGiven = findViewById(R.id.editTextKottiddu);
        editTextNote = findViewById(R.id.editTextNote);

        spinner = findViewById(R.id.spinner);
        spinnerList = new ArrayList<Integer>();
        spinnerAdapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_item,spinnerList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerList.add(300);
        spinnerList.add(150);

        spinner.setAdapter(spinnerAdapter);



        textViewName.setText(name+" ಜಮಾ");


    }

    public void plusClicked(View view) {

        if(editTextAal.getText().toString().isEmpty() || editTextGiven.getText().toString().isEmpty()){
            Toast.makeText(this, "ಬರೆಯಿರಿ", Toast.LENGTH_SHORT).show();

        }else{
            String aalString =String.valueOf(editTextAal.getText());
            aal = Integer.parseInt(aalString);
            spinnerValue = Integer.parseInt(spinner.getSelectedItem().toString());
            totalPagar = aal * spinnerValue;
            textViewPagar.setText("ಪಗಾರ: "+String.valueOf(totalPagar));
            given = String.valueOf(editTextGiven.getText());
            remPagar = totalPagar - Integer.valueOf(given);
            textViewRem.setText("ಉಳುದ್ದಿದ್ದು : "+String.valueOf(remPagar));

            note = editTextNote.getText().toString();


            updatedBalance = Integer.parseInt(currentBalance) + remPagar;
            String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
            String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
            stringTime = currentDate + " " + currentTime;

            db = openOrCreateDatabase("rokk_db", MODE_PRIVATE, null);
            db.execSQL(String.format("UPDATE member_info SET mem_balance=%s WHERE id IS %s", updatedBalance, posiOne));
            Log.i("Table 1", "Updated");

            db.execSQL(String.format("INSERT INTO '%s' (date,days,paid_wages,rem_wages,note,total_wages) VALUES('%s',%s,%s,%s,'%s',%s)", posiOne, stringTime, aal, Integer.valueOf(given), remPagar,note,totalPagar));
            Log.i("Table 2", "Inserted");
            Intent goToMain = new Intent(plusActivity.this, MainActivity.class);

            startActivity(goToMain);


        }

    }

    public void aalClicked(View view) {
        String aalString =String.valueOf(editTextAal.getText());

        if(aalString.isEmpty()){
            Toast.makeText(this, "ಆಳ ಬರೆಯಿರ", Toast.LENGTH_SHORT).show();
        }else {
            aal = Integer.parseInt(aalString);

            spinnerValue = Integer.parseInt(spinner.getSelectedItem().toString());
            totalPagar = aal * spinnerValue;
            textViewPagar.setText("ಪಗಾರ: "+String.valueOf(totalPagar));

        }
    }

    public void givenClicked(View view) {
        String aalString =String.valueOf(editTextAal.getText());
        given = String.valueOf(editTextGiven.getText());
        if(aalString.isEmpty()){
            Toast.makeText(this, "ಆಳ ಬರೆಯಿರ", Toast.LENGTH_SHORT).show();
        }else if(given.isEmpty()){
            Toast.makeText(this, "ಕೊಟ್ಟುದ್ದು ಬರೆಯಿರಿ ", Toast.LENGTH_SHORT).show();
        }else {
            aal = Integer.parseInt(aalString);

            spinnerValue = Integer.parseInt(spinner.getSelectedItem().toString());
            totalPagar = aal * spinnerValue;
            textViewPagar.setText("ಪಗಾರ: "+String.valueOf(totalPagar));

            remPagar = totalPagar - Integer.valueOf(given);
            textViewRem.setText("ಉಳುದ್ದಿದ್ದು : "+String.valueOf(remPagar));



        }
    }
}