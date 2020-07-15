package com.astro.rokka.OthersFeature;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.astro.rokka.DetailsList;
import com.astro.rokka.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class otherDetailsActivity extends AppCompatActivity {

    ListView listViewOtherDetails;
    TextView textViewTitle;
    String title;
    ArrayList<otherDetailsList> arrayList;
    OtherDetailsAdapter otherDetailsAdapter;
    SQLiteDatabase db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_details);

        listViewOtherDetails = findViewById(R.id.listViewOtherDetials);
        textViewTitle = findViewById(R.id.textViewOdetailsTitle);
        Intent i = getIntent();
        title = i.getStringExtra("name");
        textViewTitle.setText(title);

        arrayList = new ArrayList<>();

        //Fetching from Db
        db = openOrCreateDatabase("others_db",MODE_PRIVATE,null);
        Cursor c = db.rawQuery(String.format("SELECT * FROM '%s'",title),null);

        c.moveToFirst();
        int amountIndex = c.getColumnIndex("amount");
        int current_balanceIndex = c.getColumnIndex("current_balance");
        int dateIndex = c.getColumnIndex("date");
        int noteIndex = c.getColumnIndex("note");

        while(!c.isAfterLast()){
            arrayList.add(new otherDetailsList(c.getInt(amountIndex),c.getInt(current_balanceIndex)
                    ,c.getString(noteIndex)
                    ,c.getString(dateIndex)));
            c.moveToNext();
        }

        c.close();


        otherDetailsAdapter = new OtherDetailsAdapter(this,arrayList);
        listViewOtherDetails.setAdapter(otherDetailsAdapter);


    }

    public class OtherDetailsAdapter extends ArrayAdapter<otherDetailsList>{

        private Context mContext;
        private List<otherDetailsList> oDetlist = new ArrayList<>();

        public OtherDetailsAdapter(@NonNull Context context, @SuppressLint("SupportAnnotationUsage") @LayoutRes ArrayList<otherDetailsList> list) {
            super(context, 0 , list);

            mContext = context;
            oDetlist =list;
        }

        @SuppressLint("SetTextI18n")
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//            return super.getView(position, convertView, parent);

            View view = convertView;
            view = LayoutInflater.from(mContext).inflate(R.layout.other_details_row,parent,false);
            final otherDetailsList currentPosition = oDetlist.get(position);

            TextView textViewAmount,textViewNote,textViewDate,textViewBalance;

            textViewAmount = view.findViewById(R.id.textViewOdetailsAmount);
            textViewBalance = view.findViewById(R.id.textViewOdetailsBalance);
            textViewDate = view.findViewById(R.id.textViewOdetailsDate);
            textViewNote = view.findViewById(R.id.textViewOdetailsNote);

            textViewAmount.setText(getString(R.string.oDetailsAmount)+String.valueOf(currentPosition.getAmount()));
            textViewBalance.setText(getString(R.string.oDetailsBalance)+String.valueOf(currentPosition.getBalance()));
            textViewNote.setText(getString(R.string.oDetailsNote)+String.valueOf(currentPosition.getNote()));
            textViewDate.setText(String.valueOf(currentPosition.getDate()));



            return view;
        }
    }
}