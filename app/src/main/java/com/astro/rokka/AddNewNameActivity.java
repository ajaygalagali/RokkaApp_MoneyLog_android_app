package com.astro.rokka;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddNewNameActivity extends AppCompatActivity {
    EditText editTextName;
    EditText editTextBalance;
    String Name;
    static Map<String, HomeList> map = new HashMap<>();
    String balance;
    int mem_id_from_db;
    String mem_name_from_db;
    Integer mem_balance_from_db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_name);

        editTextName = findViewById(R.id.editTextTextPersonName);
        editTextBalance = findViewById(R.id.editTextNumberSigned);
    }



    public void addNameClicked(View view) {

        Name = String.valueOf(editTextName.getText());
        if(Name.isEmpty()){
            Toast.makeText(this, "ಹೆಸರು ಬರೆಯಿರಿ ", Toast.LENGTH_SHORT).show();
        }else {
            balance = String.valueOf(editTextBalance.getText());
            if (balance.isEmpty()) {
                balance = "0";
            }

            SQLiteDatabase db = openOrCreateDatabase("rokk_db", MODE_PRIVATE, null);

            db.execSQL("CREATE TABLE IF NOT EXISTS member_info (id INTEGER PRIMARY KEY AUTOINCREMENT,mem_name VARCHAR, mem_balance INT)");
            db.execSQL(String.format("INSERT INTO member_info (mem_name,mem_balance) VALUES('%s',%s)", Name, balance));


            @SuppressLint("Recycle") Cursor c = db.rawQuery(String.format("SELECT * FROM member_info WHERE mem_name IS '%s'", Name), null);
            int mem_idIndex = c.getColumnIndex("id");
            c.moveToFirst();
            boolean flag = true;
            while (flag) {
                mem_id_from_db = c.getInt(mem_idIndex);
                flag = false;
            }

            //CREATING TABLE 2
            db.execSQL(String.format("CREATE TABLE IF NOT EXISTS '%s'(id INTEGER PRIMARY KEY AUTOINCREMENT,days INT,total_wages INT,paid_wages INT,rem_wages INT,date VARCHAR,note VARCHAR)", mem_id_from_db));
            Log.i("Table 2 ", "Created");

            MainActivity.homeListAdapter.notifyDataSetChanged();
            Intent goToMain = new Intent(AddNewNameActivity.this, MainActivity.class);
            startActivity(goToMain);


        }
    }
}