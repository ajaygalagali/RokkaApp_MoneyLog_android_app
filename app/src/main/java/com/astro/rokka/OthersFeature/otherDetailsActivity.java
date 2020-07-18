package com.astro.rokka.OthersFeature;

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
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.astro.rokka.DetailsList;
import com.astro.rokka.Expense.ExpenseMainActivity;
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
        int idIndex = c.getColumnIndex("id");
        int curTotal = 0;
        while(!c.isAfterLast()){
            curTotal+=c.getInt(amountIndex);
            arrayList.add(new otherDetailsList(c.getInt(amountIndex)
                    ,curTotal
                    ,c.getString(noteIndex)
                    ,c.getString(dateIndex)
                    ,c.getInt(idIndex)));
            c.moveToNext();
        }

        c.close();


        otherDetailsAdapter = new OtherDetailsAdapter(this,arrayList);
        listViewOtherDetails.setAdapter(otherDetailsAdapter);


    }

    public void arrowBackClicked(View view) {
        Intent goBack = new Intent(otherDetailsActivity.this,Others_mainActivity.class);
        startActivity(goBack);
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
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
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

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
//                    Toast.makeText(mContext, String.valueOf(textViewName.getText()), Toast.LENGTH_SHORT).show();

                    new AlertDialog.Builder(otherDetailsActivity.this)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle(getString(R.string.eAlertTitle))
                            .setMessage(getString(R.string.alertboxLine))
                            .setPositiveButton(getString(R.string.alertboxYes), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    Cursor  c = db.rawQuery(String.format("SELECT * FROM oMemInfo WHERE oMemName IS '%s'",title),null);
                                    c.moveToFirst();
                                    int balanceIndex = c.getColumnIndex("oMemBalance");
                                    int bal_db = c.getInt(balanceIndex);

                                    int updatedBal = bal_db - currentPosition.getAmount();
                                    c.close();
                                    db.execSQL(String.format("UPDATE oMemInfo SET oMemBalance=%s WHERE oMemName IS '%s'", updatedBal, title));
                                    db.execSQL(String.format("DELETE FROM '%s' WHERE id IS %s",title,currentPosition.getId()));
                                    Toast.makeText(mContext, getString(R.string.alertboxToast), Toast.LENGTH_SHORT).show();
                                    finish();
                                    overridePendingTransition(0, 0);
                                    startActivity(getIntent());
                                    overridePendingTransition(0, 0);
//                                    arrayList.remove(position);
//                                    Others_mainActivity.other_main_adapter.notifyDataSetChanged();
//                                    otherDetailsAdapter.notifyDataSetChanged();
//                                    finish();
//                                    startActivity(getIntent());
                                }

                            })
                            .setNegativeButton(getString(R.string.alertboxNo),null).show();




                    return true;
                }
            });


            return view;
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Intent gotoOtherMain = new Intent(otherDetailsActivity.this, Others_mainActivity.class);
//        overridePendingTransition(0, 0);

        startActivity(gotoOtherMain);
//        overridePendingTransition(0, 0);



    }
}