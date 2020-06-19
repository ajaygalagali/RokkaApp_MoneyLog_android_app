package com.astro.rokka;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DetailsActivity extends AppCompatActivity {


    String Name, mem_id;

    TextView textViewHeading;
    ListView listViewDet;



    int date_index, days_index, paid_wages_index, rem_wages_index, total_wages_index, note_index, halfdays_index;
    int total_wages,paid_wages, rem_wages,days,halfdays;
    String date, note,bal_from_main;

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
        bal_from_main = i.getStringExtra("balance");
        Log.i("name",Name);
        textViewHeading.setText(Name);

        detList = new ArrayList<>();
        detailsAdapter = new DetailsAdapter(this, detList);

        SQLiteDatabase db = openOrCreateDatabase("rokk_db",MODE_PRIVATE,null);
        @SuppressLint("Recycle") Cursor c = db.rawQuery(String.format("SELECT * FROM '%s'",mem_id),null);

        int j = c.getCount();

        date_index = c.getColumnIndex("date");
        days_index = c.getColumnIndex("days");
        note_index = c.getColumnIndex("note");
        total_wages_index = c.getColumnIndex("total_wages");
        rem_wages_index = c.getColumnIndex("rem_wages");
        paid_wages_index = c.getColumnIndex("paid_wages");
        halfdays_index = c.getColumnIndex("halfdays");



        c.moveToLast();
        for(int k=0;k<j;k++){
            total_wages = c.getInt(total_wages_index);
            paid_wages = c.getInt(paid_wages_index);
            rem_wages = c.getInt(rem_wages_index);
            days = c.getInt(days_index);
            halfdays = c.getInt(halfdays_index);

            date = c.getString(date_index);
            note = c.getString(note_index);
            //int days, int total_wages,int rem_wages,int paid_wages,String note,String time
            detList.add(new DetailsList(days,total_wages,rem_wages,paid_wages,note,date,halfdays));
            c.moveToPrevious();
        }

        c.close();


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

        @SuppressLint("ViewHolder")
        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//            return super.getView(position, convertView, parent);
            View view = convertView;
            view = LayoutInflater.from(mContext).inflate(R.layout.details_row,parent,false);
            final DetailsList currentPosition = cablist.get(position);

            final TextView textViewDays, textViewTotalWage, textViewPaidWage, textViewRemWage,textViewDate,textViewNote;

            textViewDate = view.findViewById(R.id.textViewDtDate);
            textViewDays = view.findViewById(R.id.textViewDtDays);
            textViewNote = view.findViewById(R.id.textViewDtNote);
            textViewPaidWage = view.findViewById(R.id.textViewDtGiven);
            textViewRemWage = view.findViewById(R.id.textViewDtRem);
            textViewTotalWage = view.findViewById(R.id.textViewDtTotalWage);

            textViewDate.setText(currentPosition.getTime());
            textViewDays.setText("ಆಳ : "+String.valueOf(currentPosition.getDays())+" / "+String.valueOf(currentPosition.getHalfdays()));
            textViewNote.setText(currentPosition.getNote());
            textViewPaidWage.setText("ಕೊಟ್ಟುದ್ದು : "+String.valueOf(currentPosition.getPaid_wages()));
            textViewRemWage.setText("ಉಳುದ್ದಿದ್ದು : "+String.valueOf(currentPosition.getRem_wages()));
            textViewTotalWage.setText("ಪಗಾರ : "+String.valueOf(currentPosition.getTotal_wages()));

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    new AlertDialog.Builder(DetailsActivity.this)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle(String.valueOf("ಡಿಲಿಟ ಮಾಡಿ?"))
                            .setMessage("ಈ ಟಿಪ್ಪಣಿಯನ್ನು ಅಳಿಸಲು ನೀವು ಖಚಿತವಾಗಿ ಬಯಸುವಿರಾ??")
                            .setPositiveButton("ಹೌದು", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    SQLiteDatabase dbb = openOrCreateDatabase("rokk_db",MODE_PRIVATE,null);

                                    String toBeDeletedString = textViewRemWage.getText().toString();
                                    int toBeDeleted = Integer.valueOf(toBeDeletedString.substring(14));
//                                    Cursor cc = dbb.rawQuery("SELECT * FROM member_info",null);
                                    int toBeUpdated = Integer.valueOf(bal_from_main) - toBeDeleted;
                                    dbb.execSQL(String.format("UPDATE member_info SET mem_balance = %s WHERE mem_name IS '%s'",toBeUpdated,Name));
                                    dbb.execSQL(String.format("DELETE FROM '%s' WHERE date IS '%s'",mem_id,String.valueOf(textViewDate.getText())));
                                    Toast.makeText(mContext, "ಅಳಿಸಲಾಗಿದೆ", Toast.LENGTH_SHORT).show();
                                    detList.remove(position);
                                    detailsAdapter.notifyDataSetChanged();

                                }

                            })
                            .setNegativeButton("ಬೇಡ",null).show();


                    return true;
                }
            });

            return view;
        }


    }
    public void onBackPressed(){

        Intent goToMain = new Intent(DetailsActivity.this,MainActivity.class);
        startActivity(goToMain);
    }
}