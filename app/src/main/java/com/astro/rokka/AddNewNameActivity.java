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
        balance = String.valueOf(editTextBalance.getText());
        if(balance.isEmpty()){
            balance="0";
        }

        SQLiteDatabase db = openOrCreateDatabase("rokk_db",MODE_PRIVATE,null);
//        String create = "CREATE TABLE IF NOT EXI";
        db.execSQL("CREATE TABLE IF NOT EXISTS member_info (id INTEGER PRIMARY KEY AUTOINCREMENT,mem_name VARCHAR, mem_balance INT)");
        db.execSQL(String.format("INSERT INTO member_info (mem_name,mem_balance) VALUES('%s',%s)",Name,balance));


        @SuppressLint("Recycle") Cursor c = db.rawQuery(String.format("SELECT * FROM member_info WHERE mem_name IS '%s'", Name),null);
        int mem_idIndex = c.getColumnIndex("id");
        c.moveToFirst();
        boolean flag = true;
        while(flag){
            mem_id_from_db = c.getInt(mem_idIndex);
            flag = false;
        }

        //CREATING TABLE 2
        db.execSQL(String.format("CREATE TABLE IF NOT EXISTS '%s'(id INTEGER PRIMARY KEY AUTOINCREMENT,transfer INT,date VARCHAR)",mem_id_from_db));
        Log.i("Table 2 ","Created");
        /*db = openOrCreateDatabase("rokk_db",MODE_PRIVATE,null);
        @SuppressLint("Recycle") Cursor cd = db.rawQuery("SELECT * FROM member_info",null);
        int mem_NameIndex = cd.getColumnIndex("mem_name");
        int mem_balanceIndex = cd.getColumnIndex("mem_balance");
        cd.moveToFirst();

        while(cd.isAfterLast()){
            mem_name_from_db = cd.getString(mem_NameIndex);
            mem_balance_from_db = cd.getInt(mem_balanceIndex);
            Log.i("Name from db",mem_name_from_db);
            Log.i("bal from db",String.valueOf(mem_balance_from_db));
            MainActivity.arrayList.add(new HomeList(mem_balance_from_db,mem_name_from_db));
            Log.i("ArrayList","Created");
        }*/
        MainActivity.homeListAdapter.notifyDataSetChanged();
        Intent goToMain = new Intent(AddNewNameActivity.this,MainActivity.class);
        startActivity(goToMain);
//        db.execSQL("CREATE TABLE IF NOT EXISTS "+ Name + "(id INTEGER PRIMARY KEY AUTOINCREMENT,newTransfer INT,balance INT,createdAt VARCHAR)");
//        Log.i("Table",Name);
        /*String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        String stringTime = currentDate+" "+currentTime;
        String Insert = "INSERT INTO " + Name + "(balance,createdAt) VALUES("+ Integer.valueOf(balance) +","+"'"+ stringTime +"')";

//        Log.i("Insert",Insert);
        db.execSQL(Insert);
        Cursor c =db.rawQuery("SELECT * FROM "+Name,null);
        int balanceIndex = c.getColumnIndex("balance");

        int TimeIndex = c.getColumnIndex("createdAt");
        c.moveToFirst();
        while(!c.isAfterLast()){
            Log.i("Balance",c.getString(balanceIndex));


            Log.i("Time",c.getString(TimeIndex));
            c.moveToNext();
        }*/
//        String[] arr = new String[]{"a", "b", "c", "d", "e", "f"};
//        for(int i = 0; i < arr.length; i++) {
//            map.put("v" + i, new HomeList(arr[i]));
//        }
        /*map.put(Name,new HomeList(Integer.parseInt(balance),Name));
        MainActivity.arrayList.add(map.get(Name));

        MainActivity.arrayList.add(new HomeList(Integer.valueOf(balance),Name));
        MainActivity.homeListAdapter.notifyDataSetChanged();
        finish();*/


    }
}