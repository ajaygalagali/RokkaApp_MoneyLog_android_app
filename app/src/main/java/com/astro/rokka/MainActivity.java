package com.astro.rokka;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ListView listViewMain;
    static ArrayList<HomeList> arrayList;
    @SuppressLint("StaticFieldLeak")
    static HomeListAdapter homeListAdapter;
    SQLiteDatabase db;
    String mem_name_from_db;
    Integer mem_balance_from_db;
    String mem_id_from_db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listViewMain = findViewById(R.id.listViewMain);

        arrayList = new ArrayList<>();


        homeListAdapter = new HomeListAdapter(this,arrayList);
        db = openOrCreateDatabase("rokk_db",MODE_PRIVATE,null);
        db.execSQL("CREATE TABLE IF NOT EXISTS member_info (id INTEGER PRIMARY KEY AUTOINCREMENT,mem_name VARCHAR, mem_balance INT)");
        Cursor c = db.rawQuery("SELECT * FROM member_info",null);

        int mem_NameIndex = c.getColumnIndex("mem_name");
        int mem_balanceIndex = c.getColumnIndex("mem_balance");
        Log.i("Name Bal", mem_NameIndex +String.valueOf(mem_balanceIndex));

        c.moveToFirst();
        int j= c.getCount();
        for(int i=0;i<j;i++){
            mem_name_from_db = String.valueOf(c.getString(mem_NameIndex));
            mem_balance_from_db = c.getInt(mem_balanceIndex);

            arrayList.add(new HomeList(mem_balance_from_db,mem_name_from_db));

            c.moveToNext();

        }
        c.close();

        listViewMain.setAdapter(homeListAdapter);

    }


    public void NewNameClicked(View view) {
        Intent goToAdd = new Intent(MainActivity.this,AddNewNameActivity.class);
        startActivity(goToAdd);
    }
    public class HomeListAdapter extends ArrayAdapter<HomeList>{

        private Context mContext;
        private List<HomeList> labList = new ArrayList<>();

        public HomeListAdapter(@NonNull Context context, @SuppressLint("SupportAnnotationUsage") @LayoutRes ArrayList<HomeList> list) {
            super(context, 0 , list);
            mContext = context;
            labList = list;
        }

        @SuppressLint("ViewHolder")
        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent) {
//            return super.getView(position, convertView, parent);
            View view = convertView;

            view = LayoutInflater.from(mContext).inflate(R.layout.main_row,parent,false);
            HomeList currentPosition = labList.get(position);


            final TextView textViewName = view.findViewById(R.id.textViewName);
            textViewName.setText(currentPosition.getLabName());

            textViewName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                Log.i("Name",String.valueOf(textViewName.getText()));
//                    Toast.makeText(mContext, String.valueOf(textViewName.getText()), Toast.LENGTH_SHORT).show();
                    Intent goToDet = new Intent(MainActivity.this,DetailsActivity.class);


                    db = openOrCreateDatabase("rokk_db",MODE_PRIVATE,null);
                    db.execSQL("CREATE TABLE IF NOT EXISTS member_info (id INTEGER PRIMARY KEY AUTOINCREMENT,mem_name VARCHAR, mem_balance INT)");
                    Cursor c = db.rawQuery(String.format("SELECT * FROM member_info WHERE mem_name IS '%s'", textViewName.getText()),null);
                    Log.i("C","Done");
                    int mem_idIndex = c.getColumnIndex("id");

                    c.moveToFirst();
                    int j= c.getCount();
                    for(int i=0;i<j;i++){
                        mem_id_from_db = String.valueOf(c.getString(mem_idIndex));
                        c.moveToNext();
                    }
                    c.close();
                    Log.i("ID of Main",mem_id_from_db);
                    goToDet.putExtra("mem_id",String.valueOf(mem_id_from_db));
                    goToDet.putExtra("name",textViewName.getText());




                    startActivity(goToDet);

                }
            });

            final TextView textViewBalance = view.findViewById(R.id.textViewBalance);
            int bal = currentPosition.getBalance();

            textViewBalance.setText(String.valueOf(currentPosition.getBalance()));
            if(bal < 0){
                textViewBalance.setTextColor(Color.RED);
            }else{
                textViewBalance.setTextColor(Color.GREEN);
            }




            ImageButton imageButtonPlus = view.findViewById(R.id.imageButtonPlus);
            imageButtonPlus.setTag(String.valueOf(position));

            imageButtonPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                Log.i("Plus",String.valueOf(v.getTag()));
                    Intent goToAdd = new Intent(mContext,plusActivity.class);
                    goToAdd.putExtra("name",String.valueOf(textViewName.getText()));
                    goToAdd.putExtra("currentBalance",String.valueOf(textViewBalance.getText()));
                    goToAdd.putExtra("position",String.valueOf(v.getTag()));

                    startActivity(goToAdd);

                }
            });

            return view;


        }
    }

    public void onBackPressed(){

        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory( Intent.CATEGORY_HOME );
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }
}