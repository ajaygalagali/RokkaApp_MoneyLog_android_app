package com.astro.rokka;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.HashMap;
import java.util.Map;

public class AddNewNameActivity extends AppCompatActivity {
    EditText editTextName;
    EditText editTextBalance;
    static Map<String, HomeList> map = new HashMap<>();

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
//        String[] arr = new String[]{"a", "b", "c", "d", "e", "f"};
//        for(int i = 0; i < arr.length; i++) {
//            map.put("v" + i, new HomeList(arr[i]));
//        }
        map.put(Name,new HomeList(Integer.valueOf(balance),Name));
        MainActivity.arrayList.add(map.get(Name));

//        MainActivity.arrayList.add(new HomeList(Integer.valueOf(balance),Name));
        MainActivity.homeListAdapter.notifyDataSetChanged();
        finish();

    }
}