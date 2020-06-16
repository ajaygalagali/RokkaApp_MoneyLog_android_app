package com.astro.rokka;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class DetailsActivity extends AppCompatActivity {
    SQLiteDatabase db;
    String Name;
    TextView textViewHeading;
    ListView listViewDet;
    String Time;
    String mem_id;
    int transfer_index;
    int date_index;
    static  DetailsAdapter detailsAdapter;

    static ArrayList<DetailsList> detList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        listViewDet = findViewById(R.id.listViewDetails);
        textViewHeading = findViewById(R.id.textView5);

        Intent i = getIntent();
        mem_id = i.getStringExtra("mem_id");
        Name = i.getStringExtra("name");
        textViewHeading.setText(Name);
        detList = new ArrayList<>();
        detailsAdapter = new DetailsAdapter(this, detList);

        db = openOrCreateDatabase("rokk_db",MODE_PRIVATE,null);

        @SuppressLint("Recycle") Cursor c = db.rawQuery(String.format("SELECT * FROM '%s'",mem_id),null);

        int j = c.getCount();
        transfer_index = c.getColumnIndex("transfer");
        date_index = c.getColumnIndex("date");
        Log.i("Index",String.valueOf(date_index)+" "+String.valueOf(transfer_index));
        Log.i("Count",String.valueOf(j));
        c.moveToFirst();
        for(int k=0;k<j;k++){
            int transfer = c.getInt(transfer_index);
            String date = c.getString(date_index);
            detList.add(new DetailsList(transfer,date));
            c.moveToNext();
        }




        listViewDet.setAdapter(detailsAdapter);


    }

    public void refresh(View view) {
        detailsAdapter.notifyDataSetChanged();
    }

    public static class DetailsAdapter extends ArrayAdapter<DetailsList> {

        private Context mContext;
        private List<DetailsList> cablist = new ArrayList<>();

        public DetailsAdapter(@NonNull Context context, @SuppressLint("SupportAnnotationUsage") @LayoutRes ArrayList<DetailsList> list) {
            super(context, 0 , list);

            mContext = context;
            cablist =list;
        }

        @SuppressLint("ViewHolder")
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//            return super.getView(position, convertView, parent);
            View view = convertView;
            view = LayoutInflater.from(mContext).inflate(R.layout.details_row,parent,false);
            DetailsList currentPosition = cablist.get(position);

            TextView textViewNumber = view.findViewById(R.id.textViewDetNumber);
            TextView textViewTime  = view.findViewById(R.id.textViewDetTime);

            textViewNumber.setText(String.valueOf(currentPosition.getNumber()));
            if(currentPosition.getNumber() < 0){
                textViewNumber.setTextColor(Color.RED);
            }else {
                textViewNumber.setTextColor(Color.GREEN);
            }
            textViewTime.setText(String.valueOf(currentPosition.getTime()));

            return view;
        }
    }
}