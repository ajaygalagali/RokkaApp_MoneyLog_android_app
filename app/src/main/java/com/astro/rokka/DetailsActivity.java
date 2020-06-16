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
    int newTransfer;
    ListView listViewDet;
    String Time;
    static  DetailsAdapter detailsAdapter;

    static ArrayList<DetailsList> detList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent j = getIntent();
        Name = j.getStringExtra("name");


        db = openOrCreateDatabase("balance",MODE_PRIVATE,null);
        listViewDet = findViewById(R.id.listViewDetails);
        detList = new ArrayList<>();
        detailsAdapter = new DetailsAdapter(this,detList);
//         detList.add(new DetailsList(11,"aaa"));
//         detList.add(new DetailsList(11,"aaa"));
//         detList.add(new DetailsList(11,"aaa"));
//         detList.add(new DetailsList(11,"aaa"));

        listViewDet.setAdapter(detailsAdapter);


    }

    public void refresh(View view) {
        detailsAdapter.notifyDataSetChanged();
    }

    public class DetailsAdapter extends ArrayAdapter<DetailsList> {

        private Context mContext;
        private List<DetailsList> cablist = new ArrayList<>();

        public DetailsAdapter(@NonNull Context context, @SuppressLint("SupportAnnotationUsage") @LayoutRes ArrayList<DetailsList> list) {
            super(context, 0 , list);

            mContext = context;
            cablist =list;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//            return super.getView(position, convertView, parent);
            View view = convertView;
            view = LayoutInflater.from(mContext).inflate(R.layout.details_row,parent,false);
            DetailsList currentPosition = cablist.get(position);

            TextView textViewNumber = view.findViewById(R.id.textViewDetNumber);
            TextView textViewTime  = view.findViewById(R.id.textViewDetTime);
            int posPlus = position;
            Log.i("Position det",String.valueOf(position));
            Cursor c =db.rawQuery("SELECT * FROM "+Name+" WHERE id IS "+position+" AND newTransfer IS NOT NULL",null);
            int newTransferIndex = c.getColumnIndex("newTransfer");
            int TimeIndex = c.getColumnIndex("createdAt");
            c.moveToFirst();
            while(!c.isAfterLast()){
//            Log.i("Balance",c.getString(balanceIndex));
//            Log.i("Time",c.getString(TimeIndex));
                newTransfer = Integer.valueOf(c.getInt(newTransferIndex));
                Time = String.valueOf(c.getString(TimeIndex));
                Log.i("newTransfer Before",String.valueOf(newTransfer));

                textViewNumber.setText(String.valueOf(newTransfer));

                Log.i("newTransfer After",String.valueOf(newTransfer));
                Log.i("Time Before",Time);

                textViewTime.setText(String.valueOf(Time));

                Log.i("Time After",Time);
                c.moveToNext();
            }


//            detList.add(new DetailsList(newTransfer,Time));



            return view;
        }
    }
}