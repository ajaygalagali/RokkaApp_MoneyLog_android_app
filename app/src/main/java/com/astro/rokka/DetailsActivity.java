package com.astro.rokka;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.Dialog;
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
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class DetailsActivity extends AppCompatActivity {


    String Name, mem_id;

    TextView textViewHeading;
    ListView listViewDet;

    ConstraintLayout clDetails;

    int date_index, days_index, paid_wages_index, rem_wages_index, total_wages_index, note_index, halfdays_index,id_index;
    int total_wages,paid_wages, rem_wages,days, halfdays, rem_from_db,id_from_db;
    String date;
    String note;
    int bal_from_db;

    //PopUp
    EditText editTextAmount;
    EditText editTextNote;
    TextView textViewTitle;
    TextView textViewAlert;
    Button buttonPopUp;

    SQLiteDatabase db;

    static  DetailsAdapter detailsAdapter;

    static ArrayList<DetailsList> detList;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        db = openOrCreateDatabase("rokk_db",MODE_PRIVATE,null);


        Window window = this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.mycolor));

        listViewDet = findViewById(R.id.listViewDetails);
        textViewHeading = findViewById(R.id.textView5);

        clDetails = findViewById(R.id.clDetails);

        Intent i = getIntent();
        mem_id = i.getStringExtra("mem_id");
        Name = i.getStringExtra("name");

        textViewHeading.setText(getString(R.string.detTitle)+Name);

        detList = new ArrayList<>();
        detailsAdapter = new DetailsAdapter(this, detList);


        @SuppressLint("Recycle") Cursor c = db.rawQuery(String.format("SELECT * FROM '%s'",Name),null);

        int j = c.getCount();

        date_index = c.getColumnIndex("date");
        days_index = c.getColumnIndex("days");
        note_index = c.getColumnIndex("note");
        total_wages_index = c.getColumnIndex("total_wages");
        rem_wages_index = c.getColumnIndex("rem_wages");
        paid_wages_index = c.getColumnIndex("paid_wages");
        halfdays_index = c.getColumnIndex("halfdays");
        id_index = c.getColumnIndex("id");



        c.moveToLast();
        for(int k=0;k<j;k++){
            total_wages = c.getInt(total_wages_index);
            paid_wages = c.getInt(paid_wages_index);
            rem_wages = c.getInt(rem_wages_index);
            days = c.getInt(days_index);
            halfdays = c.getInt(halfdays_index);
            id_from_db = c.getInt(id_index);

            date = c.getString(date_index);
            note = c.getString(note_index);
            //int days, int total_wages,int rem_wages,int paid_wages,String note,String time
            detList.add(new DetailsList(days,total_wages,rem_wages,paid_wages,note,date,halfdays,id_from_db));
            c.moveToPrevious();
        }

        c.close();

        if(detList.isEmpty()){
            clDetails.setVisibility(View.VISIBLE);
        }
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

        @SuppressLint({"ViewHolder", "SetTextI18n"})
        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//            return super.getView(position, convertView, parent);
            View view = convertView;
            view = LayoutInflater.from(mContext).inflate(R.layout.details_row,parent,false);
            final DetailsList currentPosition = cablist.get(position);

            final TextView textViewDays, textViewTotalWage, textViewPaidWage, textViewRemWage,textViewDate,textViewNote,textViewStatus;

            textViewDate = view.findViewById(R.id.textViewDtDate);
            textViewDays = view.findViewById(R.id.textViewDtDays);
            textViewNote = view.findViewById(R.id.textViewDtNote);
            textViewPaidWage = view.findViewById(R.id.textViewDtGiven);
            textViewRemWage = view.findViewById(R.id.textViewDtRem);
            textViewTotalWage = view.findViewById(R.id.textViewDtTotalWage);
            textViewStatus = view.findViewById(R.id.textViewDetStatus);

            FloatingActionButton floatingActionButton = view.findViewById(R.id.floatingActionButton);

            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(mContext, String.valueOf(position), Toast.LENGTH_SHORT).show();
                    final Dialog dialog = new Dialog(mContext);
                    dialog.setContentView(R.layout.o_plus);
                    dialog.show();

//                    editTextAmount =dialog.findViewById(R.id.editTextOtherPlus);
                    editTextAmount =dialog.findViewById(R.id.editTextOtherPlus);
                    editTextNote = dialog.findViewById(R.id.editTextOtherPlusNote);
                    textViewTitle = dialog.findViewById(R.id.textViewPopUpTitle);
                    textViewAlert = dialog.findViewById(R.id.textViewPopupAlert);
                    buttonPopUp = dialog.findViewById(R.id.buttonOtherPlus);



                    buttonPopUp.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            Toast.makeText(DetailsActivity.this, pAmount, Toast.LENGTH_SHORT).show();
//                            Log.i("Amount", String.valueOf(editTextAmount.getText()));
                            String pAmount = editTextAmount.getText().toString();
                            String pNote = editTextNote.getText().toString();
//                            Log.i("pAmount", pAmount);

                            if(pAmount.isEmpty()){
//                                Toast.makeText(DetailsActivity.this, String.valueOf(pAmount), Toast.LENGTH_SHORT).show();

                                textViewAlert.setText(getString(R.string.oAlert));
                                textViewAlert.setScaleX(0);
                                textViewAlert.setScaleY(0);
                                textViewAlert.animate().scaleX(1).setDuration(500);
                                textViewAlert.animate().scaleY(1).setDuration(500);
                            }else{
                                // Todo: Update to db

                                // fetch current balance
                                Cursor cc = db.rawQuery(String.format("SELECT * FROM member_info WHERE mem_name IS '%s'",Name),null);
                                int bal_from_db_index = cc.getColumnIndex("mem_balance");

                                cc.moveToFirst();
                                bal_from_db = cc.getInt(bal_from_db_index);
                                cc.close();

                                int updated_bal = bal_from_db - Integer.parseInt(pAmount);
                                int updated_remaining = Integer.parseInt(String.valueOf(currentPosition.getRem_wages())) - Integer.parseInt(pAmount);
                                int updated_given = Integer.parseInt(String.valueOf(currentPosition.getPaid_wages())) + Integer.parseInt(pAmount);

                               /* if(updated_remaining == 0){
                                    textViewStatus.setText(getString(R.string.detStatusPositive));
                                    textViewStatus.setTextColor(Color.GREEN);
                                }*/

                                //  '%s'(id INTEGER PRIMARY KEY AUTOINCREMENT,days INT,total_wages INT,paid_wages INT,rem_wages INT,date VARCHAR,note VARCHAR,halfdays INT)", Name));
                                //  dbb.execSQL(String.format("UPDATE member_info SET mem_balance = %s WHERE mem_name IS '%s'",toBeUpdated,Name));
                                db.execSQL(String.format("UPDATE '%s' SET paid_wages = %s,rem_wages = %s WHERE id = %s",Name,updated_given,updated_remaining,currentPosition.getId()));
                                db.execSQL(String.format("UPDATE member_info SET mem_balance=%s WHERE mem_name IS '%s'", updated_bal, Name));
                                Toast.makeText(DetailsActivity.this, getString(R.string.detUpdate), Toast.LENGTH_SHORT).show();
                                dialog.dismiss();


                                //  DetailsList(int days, int total_wages, int rem_wages, int paid_wages, String note, String time, int halfdays,int id)
                                /*detList.add(position+1,new DetailsList(currentPosition.getDays(),currentPosition.getTotal_wages(),updated_remaining,updated_given
                                        ,currentPosition.getNote(),currentPosition.getNote(),currentPosition.getTime(),currentPosition.getHalfdays(),currentPosition.getId()));*/
                                DetailsList newList = new DetailsList(currentPosition.getDays(),currentPosition.getTotal_wages(),updated_remaining,updated_given,
                                        currentPosition.getNote(),currentPosition.getTime(),currentPosition.getHalfdays(),currentPosition.getId());
                                detList.set(position,newList);


//                                detList.remove(position);
                                detailsAdapter.notifyDataSetChanged();


                            }
                        }
                    });



                }
            });


            textViewDate.setText(currentPosition.getTime());
            textViewDays.setText(getString(R.string.detDays)+String.valueOf(currentPosition.getDays())+" / "+String.valueOf(currentPosition.getHalfdays()));
            textViewNote.setText(currentPosition.getNote());
            textViewPaidWage.setText(getString((R.string.detGiven))+String.valueOf(currentPosition.getPaid_wages()));

            textViewRemWage.setText(getString(R.string.detRem)+String.valueOf(currentPosition.getRem_wages()));
            textViewTotalWage.setText(getString(R.string.detTotal)+String.valueOf(currentPosition.getTotal_wages()));

            if(currentPosition.getRem_wages() == 0){
                textViewStatus.setTextColor(Color.GREEN);
                textViewStatus.setText(getString(R.string.detStatusPositive));
            }else{
                textViewStatus.setTextColor(Color.RED);
                textViewStatus.setText(getString(R.string.detStatusNegative));
            }

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    new AlertDialog.Builder(DetailsActivity.this)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle(getString(R.string.detAlertboxTitle))
                            .setMessage(getString(R.string.alertboxLine))
                            .setPositiveButton(getString(R.string.alertboxYes), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    SQLiteDatabase dbb = openOrCreateDatabase("rokk_db",MODE_PRIVATE,null);

//                                    String toBeDeletedString = textViewRemWage.getText().toString();
                                    Cursor ccc = dbb.rawQuery(String.format("SELECT * FROM '%s' WHERE date IS '%s'",Name,String.valueOf(textViewDate.getText())),null);
                                    int rem_index = ccc.getColumnIndex("rem_wages");
                                    ccc.moveToFirst();
                                    rem_from_db = ccc.getInt(rem_wages_index);
                                    ccc.close();


                                    Cursor cc = dbb.rawQuery(String.format("SELECT * FROM member_info WHERE mem_name IS '%s'",Name),null);
                                    int bal_from_db_index = cc.getColumnIndex("mem_balance");

                                    cc.moveToFirst();
                                    bal_from_db = cc.getInt(bal_from_db_index);

                                    cc.close();
                                    int toBeUpdated = Integer.valueOf(bal_from_db) - rem_from_db;

                                    dbb.execSQL(String.format("UPDATE member_info SET mem_balance = %s WHERE mem_name IS '%s'",toBeUpdated,Name));
                                    dbb.execSQL(String.format("DELETE FROM '%s' WHERE date IS '%s'",Name,String.valueOf(textViewDate.getText())));
                                    Toast.makeText(mContext, getString(R.string.alertboxToast), Toast.LENGTH_SHORT).show();
                                    detList.remove(position);
                                    detailsAdapter.notifyDataSetChanged();

                                }

                            })
                            .setNegativeButton(getString(R.string.alertboxNo),null).show();


                    return true;
                }
            });

            return view;
        }


    }
    public void arrowBackClicked(View view) {
        Intent goToMain = new Intent(DetailsActivity.this,MainActivity.class);
        startActivity(goToMain);
    }
    
    public void onBackPressed(){

        Intent goToMain = new Intent(DetailsActivity.this,MainActivity.class);
        startActivity(goToMain);
    }
}