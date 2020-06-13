package com.astro.rokka;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ListView listViewMain;
    static ArrayList<HomeList> arrayList;
    static HomeListAdapter homeListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listViewMain = findViewById(R.id.listViewMain);

        arrayList = new ArrayList<>();

        arrayList.add(new HomeList(-89,"ಅಜಯ"));
        arrayList.add(new HomeList(-892,"Astro"));
        arrayList.add(new HomeList(-90,"Boi"));
        arrayList.add(new HomeList(-67,"Hello"));
        arrayList.add(new HomeList(89,"ಅಜ"));

        homeListAdapter = new HomeListAdapter(this,arrayList);
        listViewMain.setAdapter(homeListAdapter);

        /*listViewMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.i("Position",String.valueOf(position));
            }
        });*/


    }


    public void NewNameClicked(View view) {
        Intent goToAdd = new Intent(MainActivity.this,AddNewNameActivity.class);
        startActivity(goToAdd);
    }
}