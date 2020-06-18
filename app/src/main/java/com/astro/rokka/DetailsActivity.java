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
    String Name, mem_id;

    TextView textViewHeading;
    ListView listViewDet;



    int date_index, days_index, paid_wages_index, rem_wages_index, total_wages_index, note_index;
    int total_wages,paid_wages, rem_wages,days;
    String date, note;

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

        date_index = c.getColumnIndex("date");
        days_index = c.getColumnIndex("days");
        note_index = c.getColumnIndex("note");
        total_wages_index = c.getColumnIndex("total_wages");
        rem_wages_index = c.getColumnIndex("rem_wages");
        paid_wages_index = c.getColumnIndex("paid_wages");



        c.moveToLast();
        for(int k=0;k<j;k++){
            total_wages = c.getInt(total_wages_index);
            paid_wages = c.getInt(paid_wages_index);
            rem_wages = c.getInt(rem_wages_index);
            days = c.getInt(days_index);

            date = c.getString(date_index);
            note = c.getString(note_index);
            //int days, int total_wages,int rem_wages,int paid_wages,String note,String time
            detList.add(new DetailsList(days,total_wages,rem_wages,paid_wages,note,date));
            c.moveToPrevious();
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

            TextView textViewDays, textViewTotalWage, textViewPaidWage, textViewRemWage,textViewDate,textViewNote;

            textViewDate = view.findViewById(R.id.textViewDtDate);
            textViewDays = view.findViewById(R.id.textViewDtDays);
            textViewNote = view.findViewById(R.id.textViewDtNote);
            textViewPaidWage = view.findViewById(R.id.textViewDtGiven);
            textViewRemWage = view.findViewById(R.id.textViewDtRem);
            textViewTotalWage = view.findViewById(R.id.textViewDtTotalWage);

            textViewDate.setText(currentPosition.getTime());
            textViewDays.setText("ಆಳ : "+String.valueOf(currentPosition.getDays()));
            textViewNote.setText(currentPosition.getNote());
            textViewPaidWage.setText("ಕೊಟ್ಟುದ್ದು : "+String.valueOf(currentPosition.getPaid_wages()));
            textViewRemWage.setText("ಉಳುದ್ದಿದ್ದು : "+String.valueOf(currentPosition.getRem_wages()));
            textViewTotalWage.setText("ಪಗಾರ : "+String.valueOf(currentPosition.getTotal_wages()));

            return view;
        }
    }
}