package com.astro.rokka;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
    TextView textViewName, textViewPagar,textViewRem, textViewAlertPlus;
    EditText editTextAal, editTextGiven, editTextNote, editTextHalfdayAll,editTextHDP,editTextFDP;


    String tFulldays,tHalfdays,tFullPagar,tHalfPagar;
    String name, stringTime, currentBalance,position,given, note;
    int updatedBalance, posiOne,aal, totalPagar, spinnerValue, remPagar, halfdays, hd_rate,fd_rate;


    SQLiteDatabase db;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plus);

        Window window = this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.mycolor));

        Intent i =getIntent();
        name = i.getStringExtra("name");
        currentBalance = i.getStringExtra("currentBalance");
        position = i.getStringExtra("position");
        assert position != null;
        posiOne = Integer.parseInt(position);

        textViewName = findViewById(R.id.textViewPlus);
        textViewPagar = findViewById(R.id.textViewPagar);
        textViewRem = findViewById(R.id.textViewUlliddidu);
        textViewAlertPlus = findViewById(R.id.textViewAlertPlus);

        editTextAal = findViewById(R.id.editTextaal);
        editTextGiven = findViewById(R.id.editTextKottiddu);
        editTextNote = findViewById(R.id.editTextNote);
        editTextHalfdayAll = findViewById(R.id.editTextHalfdayAll);
        editTextFDP = findViewById(R.id.editTextPagarFD);
        editTextHDP = findViewById(R.id.editTextPagarHD);

        textViewName.setText(name+getString(R.string.plusTitle));



    }

    public int calculateTotalWage(String tFulldays,String tHalfdays,String tFullPagar,String tHalfPagar){
        if(tHalfdays.isEmpty() && tHalfPagar.isEmpty()){

            if(tFulldays.isEmpty() || tFullPagar.isEmpty()){
//                Toast.makeText(this, "ಸಂಜೆವರೆಗೆ ಆಳ/ಪಗಾರ ಬರೆಯಿರಿ", Toast.LENGTH_SHORT).show();
                textViewAlertPlus.setScaleX(0);
                textViewAlertPlus.setScaleY(0);
                textViewAlertPlus.setText(getString(R.string.plusAlertA));
                textViewAlertPlus.animate().scaleX(1).setDuration(500);
                textViewAlertPlus.animate().scaleY(1).setDuration(500);
                

            }else{
                aal = Integer.parseInt(editTextAal.getText().toString());

                halfdays=0;
                hd_rate = 0;
                fd_rate = Integer.parseInt(editTextFDP.getText().toString());

                totalPagar = (aal*fd_rate)+(halfdays*hd_rate);
                return totalPagar;
            }
        }else if(tFulldays.isEmpty() && tFullPagar.isEmpty()){
            if(tHalfdays.isEmpty() || tHalfPagar.isEmpty()){
//                Toast.makeText(this, "ಮಧ್ಯಾಹ್ನವರೆಗ ಆಳ/ಪಗಾರ ಬರೆಯಿರಿ", Toast.LENGTH_SHORT).show();
                textViewAlertPlus.setScaleX(0);
                textViewAlertPlus.setScaleY(0);
                textViewAlertPlus.setText(getString(R.string.plusAlertB));
                textViewAlertPlus.animate().scaleX(1).setDuration(500);
                textViewAlertPlus.animate().scaleY(1).setDuration(500);
            }else{

                halfdays = Integer.parseInt(editTextHalfdayAll.getText().toString());
                hd_rate = Integer.parseInt(editTextHDP.getText().toString());

                aal = 0;
                fd_rate=0;
                totalPagar = (aal*fd_rate)+(halfdays*hd_rate);
                return totalPagar;
            }
        } else if(tFullPagar.isEmpty() || tFulldays.isEmpty() || tHalfPagar.isEmpty() || tHalfdays.isEmpty() ) {
//            Toast.makeText(this, "ಬರೆಯಿರಿ", Toast.LENGTH_SHORT).show();
            return 0;

        }else{

            aal = Integer.parseInt(editTextAal.getText().toString());
            halfdays = Integer.parseInt(editTextHalfdayAll.getText().toString());
            hd_rate = Integer.parseInt(editTextHDP.getText().toString());
            fd_rate = Integer.parseInt(editTextFDP.getText().toString());

            totalPagar = (aal*fd_rate)+(halfdays*hd_rate);
            return totalPagar;

        }
        return 0;
    }

    public void plusClicked(View view) {

        tFulldays = editTextAal.getText().toString();
        tHalfdays = editTextHalfdayAll.getText().toString();
        tFullPagar = editTextFDP.getText().toString();
        tHalfPagar = editTextHDP.getText().toString();

        given = editTextGiven.getText().toString();
        totalPagar = calculateTotalWage(tFulldays,tHalfdays,tFullPagar,tHalfPagar);



         if(totalPagar == 0){
            textViewAlertPlus.setScaleX(0);
            textViewAlertPlus.setScaleY(0);
            textViewAlertPlus.setText(getString(R.string.plusAlertC));
            textViewAlertPlus.animate().scaleX(1).setDuration(500);
            textViewAlertPlus.animate().scaleY(1).setDuration(500);
        }else if(given.isEmpty()){
//            Toast.makeText(this, "ಕೊಟ್ಟುದ್ದು ಬರೆಯಿರಿ", Toast.LENGTH_SHORT).show();
             textViewAlertPlus.setScaleX(0);
             textViewAlertPlus.setScaleY(0);
             textViewAlertPlus.setText(getString(R.string.plusAlertD));
             textViewAlertPlus.animate().scaleX(1).setDuration(500);
             textViewAlertPlus.animate().scaleY(1).setDuration(500);
         }else {

            remPagar = totalPagar - Integer.valueOf(given);
            note = editTextNote.getText().toString();
            updatedBalance = Integer.parseInt(currentBalance) + remPagar;

            String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
            String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
            stringTime = currentDate + " " + currentTime;

            db = openOrCreateDatabase("rokk_db", MODE_PRIVATE, null);
            db.execSQL(String.format("UPDATE member_info SET mem_balance=%s WHERE id IS %s", updatedBalance, posiOne));


            db.execSQL(String.format("INSERT INTO '%s' (date,days,paid_wages,rem_wages,note,total_wages,halfdays) VALUES('%s',%s,%s,%s,'%s',%s,%s)", posiOne, stringTime, aal, Integer.valueOf(given), remPagar, note, totalPagar, halfdays));

            Intent goToMain = new Intent(plusActivity.this, MainActivity.class);

            startActivity(goToMain);


        }

    }

    public void aalClicked(View view) {
        tFulldays = editTextAal.getText().toString();
        tHalfdays = editTextHalfdayAll.getText().toString();
        tFullPagar = editTextFDP.getText().toString();
        tHalfPagar = editTextHDP.getText().toString();

        textViewPagar.setText(getString(R.string.detTotal)+String.valueOf(calculateTotalWage(tFulldays,tHalfdays,tFullPagar,tHalfPagar)));

    }

    public void givenClicked(View view) {

        tFulldays = editTextAal.getText().toString();
        tHalfdays = editTextHalfdayAll.getText().toString();
        tFullPagar = editTextFDP.getText().toString();
        tHalfPagar = editTextHDP.getText().toString();

        given = editTextGiven.getText().toString();
            totalPagar = calculateTotalWage(tFulldays,tHalfdays,tFullPagar,tHalfPagar);
//            textViewPagar.setText("ಒಟ್ಟು ಪಗಾರ : + "+String.valueOf(totalPagar));
            if(given.isEmpty()){
//                Toast.makeText(this, "ಕೊಟ್ಟುದ್ದು ಬರೆಯಿರಿ", Toast.LENGTH_SHORT).show();
                textViewAlertPlus.setScaleX(0);
                textViewAlertPlus.setScaleY(0);
                textViewAlertPlus.setText(getString(R.string.plusAlertD));
                textViewAlertPlus.animate().scaleX(1).setDuration(500);
                textViewAlertPlus.animate().scaleY(1).setDuration(500);
            }else {

                remPagar = totalPagar - Integer.valueOf(given);
                textViewRem.setText(getString(R.string.plusTextGiven) + String.valueOf(remPagar));
            }

    }
}
