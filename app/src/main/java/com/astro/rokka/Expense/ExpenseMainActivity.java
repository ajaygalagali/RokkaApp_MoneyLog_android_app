package com.astro.rokka.Expense;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.util.Calendar;
import android.icu.util.TimeZone;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.astro.rokka.HomeActivity;
import com.astro.rokka.HomeList;
import com.astro.rokka.MainActivity;
import com.astro.rokka.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ExpenseMainActivity extends AppCompatActivity {

    SQLiteDatabase db;
    ListView listView;
    ArrayList<ExpenseList> arrayList;
    ExpenseAdpater expenseAdpater;

    ArrayList<String> spinnerList;

    Spinner spinnerFilter;
    TextView textViewTotal;
    Calendar calendar;
    int date;
    int month;
    int year;
    int plusMonth;

    int total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_main);

        //Calender
        calendar = Calendar.getInstance(TimeZone.getDefault());

        //db
        db = openOrCreateDatabase("expense_db",MODE_PRIVATE,null);
        db.execSQL("CREATE TABLE IF NOT EXISTS expenseTable(id INTEGER PRIMARY KEY AUTOINCREMENT,amount INTEGER,note VARCHAR,dateDD INTEGER,dateMM INTEGER,dateYYYY INTEGER,date VARCHAR)");

        // Spinner Filter
        spinnerFilter = findViewById(R.id.spinnerExpFilter);
        textViewTotal = findViewById(R.id.textViewExpTotal);
        spinnerList = new ArrayList<String>();
        spinnerList.add(getString(R.string.fToday));
        spinnerList.add(getString(R.string.fThisMonth));
        spinnerList.add(getString(R.string.fThisYear));
        spinnerList.add(getString(R.string.fAll));
        ArrayAdapter<String> spinner_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerList);
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilter.setAdapter(spinner_adapter);


        spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i("Spinner",String.valueOf(position));

                // DD-MM-YYYY

                date = calendar.get(Calendar.DATE);
                month = calendar.get(Calendar.MONTH);
                year = calendar.get(Calendar.YEAR);
                plusMonth = month+1;

                if(position == 0){
                    String query = String.format(String.format("SELECT * FROM expenseTable WHERE dateDD IS %s AND dateMM is %s",date,plusMonth));
                    updateAdapter(query);
                }

                if(position == 1){

                    String query = String.format("SELECT * FROM expenseTable WHERE dateMM IS %s",plusMonth);
                    updateAdapter(query);
                }

                if(position == 2){
                    String query = String.format("SELECT * FROM expenseTable WHERE dateYYYY IS %s",year);
                    updateAdapter(query);
                }
                if(position == 3){
                    String query = String.format("SELECT * FROM expenseTable");
                    updateAdapter(query);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        listView = findViewById(R.id.listViewExpenseMain);
        listView.setDivider(null);
        arrayList = new ArrayList<>();


        expenseAdpater = new ExpenseAdpater(this,arrayList);
        listView.setAdapter(expenseAdpater);


    }

    public void updateAdapter(String query){
        arrayList.clear();


        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();

        int amountIndex = c.getColumnIndex("amount");
        int noteIndex = c.getColumnIndex("note");
        int dateIndex = c.getColumnIndex("date");
        int idIndex = c.getColumnIndex("id");
        total = 0;
        while(!c.isAfterLast()){
            total = total + c.getInt(amountIndex);

            arrayList.add(new ExpenseList(c.getInt(amountIndex),c.getString(noteIndex),c.getString(dateIndex),c.getInt(idIndex)));
            c.moveToNext();
        }
        textViewTotal.setText("₹ "+String.valueOf(total));
        c.close();
    }

    public void newExpenseClicked(View view) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.expense_popup);
        final EditText editTextExpAmount = dialog.findViewById(R.id.editTextExpAmount);
        final EditText editTextExpNote = dialog.findViewById(R.id.editTextTextExpNote);
        final TextView textViewAlert = dialog.findViewById(R.id.textViewExpAlert);
        Button buttonExpAdd = dialog.findViewById(R.id.expAddBtn);
        dialog.show();
        buttonExpAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(ExpenseMainActivity.this, String.valueOf(editTextExpAmount.getText()), Toast.LENGTH_SHORT).show();
                if(editTextExpAmount.getText().toString().isEmpty()){
                    textViewAlert.setText(getString(R.string.oAlert));
                    textViewAlert.setScaleX(0);
                    textViewAlert.setScaleY(0);
                    textViewAlert.animate().scaleX(1).setDuration(500);
                    textViewAlert.animate().scaleY(1).setDuration(500);
                }else{
                    String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                    String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                    String stringTime = currentDate + " " + currentTime;

                    date = calendar.get(Calendar.DATE);
                    month = calendar.get(Calendar.MONTH);
                    int plusmonth = month+1;
                    year = calendar.get(Calendar.YEAR);
                    String note = String.valueOf(editTextExpNote.getText());
                    note = note.replace("'", "''");

                    db.execSQL(String.format("INSERT INTO expenseTable(amount,note,date,dateDD,dateMM,dateYYYY) VALUES(%s,'%s','%s',%s,%s,%s)",editTextExpAmount.getText()
                    ,note
                    ,stringTime
                    ,date
                    ,plusmonth
                    ,year));

                    Cursor c = db.rawQuery("SELECT id FROM expenseTable",null);
                    c.moveToLast();
                    int lastid = c.getInt(0);
                    c.close();
                    // Toast.makeText(ExpenseMainActivity.this, String.valueOf(lastid), Toast.LENGTH_SHORT).show();
                    arrayList.add(new ExpenseList(Integer.parseInt(String.valueOf(editTextExpAmount.getText())),String.valueOf(editTextExpNote.getText()),stringTime,lastid));
                    // String t ="₹ "+String.valueOf(total+Integer.parseInt(String.valueOf(editTextExpAmount.getText()))) ;
                    total = total+Integer.parseInt(editTextExpAmount.getText().toString());
                    textViewTotal.setText(String.valueOf(total));

                    Toast.makeText(ExpenseMainActivity.this, getString(R.string.oPlusToastTrue), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    expenseAdpater.notifyDataSetChanged();
                }
            }
        });

    }

    public void arrowBackClicked(View view) {
        finish();
        /*Intent goBack = new Intent(ExpenseMainActivity.this, HomeActivity.class);
        startActivity(goBack);*/
    }

    public class ExpenseAdpater extends ArrayAdapter<ExpenseList>{

        private Context mContext;
        private List<ExpenseList> expList = new ArrayList<>();

        public ExpenseAdpater(@NonNull Context context, @SuppressLint("SupportAnnotationUsage") @LayoutRes ArrayList<ExpenseList> list) {
            super(context, 0 , list);
            mContext = context;
            expList = list;
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//            return super.getView(position, convertView, parent);
            View view = convertView;
            view = LayoutInflater.from(mContext).inflate(R.layout.expense_row,parent,false);
            final ExpenseList currentPosition = expList.get(position);

            final TextView textViewExpAmount,textViewExpNote,textViewExpDate;
            textViewExpDate = view.findViewById(R.id.textViewExpDate);
            textViewExpAmount = view.findViewById(R.id.textViewExpAmount);
            textViewExpNote = view.findViewById(R.id.textViewExpNote);

            textViewExpAmount.setText("₹ "+String.valueOf(currentPosition.getAmount()));
            textViewExpNote.setText(String.valueOf(currentPosition.getNote()));
            textViewExpDate.setText(String.valueOf(currentPosition.getDate()));

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
//                    Toast.makeText(mContext, String.valueOf(textViewName.getText()), Toast.LENGTH_SHORT).show();

                    new AlertDialog.Builder(ExpenseMainActivity.this)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle(getString(R.string.eAlertTitle))
                            .setMessage(getString(R.string.alertboxLine))
                            .setPositiveButton(getString(R.string.alertboxYes), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    db.execSQL(String.format("DELETE FROM expenseTable WHERE id IS %s",currentPosition.getId()));
                                    Toast.makeText(mContext, getString(R.string.alertboxToast), Toast.LENGTH_SHORT).show();
                                    total = total-currentPosition.getAmount();
                                    textViewTotal.setText("₹ "+String.valueOf(total));
                                    arrayList.remove(position);
                                    expenseAdpater.notifyDataSetChanged();
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
}