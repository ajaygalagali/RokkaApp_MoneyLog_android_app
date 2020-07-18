package com.astro.rokka.OthersFeature;

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
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.astro.rokka.Expense.ExpenseMainActivity;
import com.astro.rokka.HomeActivity;
import com.astro.rokka.HomeList;
import com.astro.rokka.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Others_mainActivity extends AppCompatActivity {

    ListView listViewOtherMain;
    ArrayList<otherMainList> arrayList;
    SQLiteDatabase db;

    EditText editTextPlus,editTextPlusNote;
    Button buttonPlus, buttonMinus;
    
    static OtherMainAdapter other_main_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_others_main);

        listViewOtherMain = findViewById(R.id.listViewOtherMain);

        arrayList = new ArrayList<otherMainList>();

        other_main_adapter = new OtherMainAdapter(getApplicationContext(),arrayList);

        db = openOrCreateDatabase("others_db",MODE_PRIVATE,null);
        db.execSQL("CREATE TABLE IF NOT EXISTS oMemInfo (id INTEGER PRIMARY KEY AUTOINCREMENT,oMemName VARCHAR, oMemBalance INT)");
        Cursor c = db.rawQuery("SELECT * FROM oMemInfo",null);

        int mem_NameIndex = c.getColumnIndex("oMemName");
        int mem_balanceIndex = c.getColumnIndex("oMemBalance");


        c.moveToFirst();
        int j= c.getCount();
        for(int i=0;i<j;i++){
            String mem_name_from_db = String.valueOf(c.getString(mem_NameIndex));
            int mem_balance_from_db = c.getInt(mem_balanceIndex);

            arrayList.add(new otherMainList(mem_name_from_db,mem_balance_from_db));

            c.moveToNext();

        }
        c.close();

        listViewOtherMain.setAdapter(other_main_adapter);



    }

    public void arrowBackClicked(View view) {
        Intent gotoHomeMain = new Intent(Others_mainActivity.this, HomeActivity.class);
        startActivity(gotoHomeMain);

    }


    public class OtherMainAdapter extends ArrayAdapter<otherMainList>{

        private Context mContext;
        private List<otherMainList> otherMemList = new ArrayList<>();


        public OtherMainAdapter(@NonNull Context context, @NonNull ArrayList<otherMainList> list) {
            super(context, 0,list);

            mContext = context;
            otherMemList = list;

        }


        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//            return super.getView(position, convertView, parent);

            View view = convertView;
            view = LayoutInflater.from(mContext).inflate(R.layout.others_main_row,parent,false);

            final otherMainList currentPosition = otherMemList.get(position);
            final TextView textViewName = view.findViewById(R.id.textViewOtherName);
            final TextView textViewBalance = view.findViewById(R.id.textViewOtherBalance);

            textViewBalance.setText(String.valueOf(currentPosition.getOtherMemBalance()));
            if(Integer.valueOf(currentPosition.getOtherMemBalance()) < 0){
                textViewBalance.setTextColor(Color.GREEN);
            }else{
                textViewBalance.setTextColor(Color.RED);
            }
            textViewName.setText(currentPosition.getOtherMemName());

            final ImageButton imageButtonPlus = view.findViewById(R.id.imageButtonOtherPlus);

            imageButtonPlus.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onClick(View v) {
                    final Dialog dialog = new Dialog(Others_mainActivity.this);


                    dialog.setContentView(R.layout.o_plus);
                    editTextPlus = dialog.findViewById(R.id.editTextOtherPlus);
                    buttonPlus = dialog.findViewById(R.id.buttonOtherPlus);
                    editTextPlusNote = dialog.findViewById(R.id.editTextOtherPlusNote);
                    TextView textViewPopUpTitle = dialog.findViewById(R.id.textViewPopUpTitle);
                    textViewPopUpTitle.setText(String.valueOf(textViewName.getText())+" "+getString(R.string.oPlusTitle));
                    dialog.show();

                    buttonPlus.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            TextView textViewPopUpAlert = dialog.findViewById(R.id.textViewPopupAlert);
                            if (String.valueOf(editTextPlus.getText()).isEmpty()){
                                textViewPopUpAlert.setText(getString(R.string.oAlert));
                                textViewPopUpAlert.setScaleX(0);
                                textViewPopUpAlert.setScaleY(0);
                                textViewPopUpAlert.animate().scaleX(1).setDuration(500);
                                textViewPopUpAlert.animate().scaleY(1).setDuration(500);
                            }else {
//                            Toast.makeText(Others_mainActivity.this, String.valueOf(textViewName.getText()), Toast.LENGTH_SHORT).show();
                                String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                                String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                                String stringTime = currentDate + " " + currentTime;
                                int updatedBal = Integer.parseInt((String) textViewBalance.getText()) + Integer.parseInt(String.valueOf(editTextPlus.getText()));

                                //Updating balance in oMemInfo Table
                                db.execSQL(String.format("UPDATE oMemInfo SET oMemBalance=%s WHERE oMemName IS '%s'", updatedBal, String.valueOf(textViewName.getText())));

                                Log.i("Updated Balance Inserted", String.valueOf(updatedBal));

                                //amount INT,current_balance INT,date VARCHAR,note VARCHAR
                                db.execSQL(String.format("INSERT INTO '%s'(amount,current_balance,date,note) VALUES(%s,%s,'%s','%s')"
                                        , String.valueOf(textViewName.getText())
                                        , Integer.parseInt(String.valueOf(editTextPlus.getText()))
                                        , updatedBal
                                        , stringTime
                                        , String.valueOf(editTextPlusNote.getText())));
//                            Log.i("Insertion","TRUE");
//                            other_main_adapter.notifyDataSetChanged();
                                textViewBalance.setText(String.valueOf(updatedBal));
                                Toast.makeText(Others_mainActivity.this, getString(R.string.oPlusToastTrue), Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        }
                    });
                }
            });

            ImageButton imageButtonMinus = view.findViewById(R.id.imageButtonOtherMinus);

            imageButtonMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog dialog = new Dialog(Others_mainActivity.this);


                    dialog.setContentView(R.layout.o_plus);
                    editTextPlus = dialog.findViewById(R.id.editTextOtherPlus);
                    buttonPlus = dialog.findViewById(R.id.buttonOtherPlus);
                    editTextPlusNote = dialog.findViewById(R.id.editTextOtherPlusNote);
                    TextView textViewPopUpTitle = dialog.findViewById(R.id.textViewPopUpTitle);
                    textViewPopUpTitle.setText(String.valueOf(textViewName.getText())+" "+getString(R.string.oMinusTitle));
                    dialog.show();

                    buttonPlus.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            TextView textViewPopUpAlert = dialog.findViewById(R.id.textViewPopupAlert);

                            if (String.valueOf(editTextPlus.getText()).isEmpty()) {
                                textViewPopUpAlert.setText(getString(R.string.oAlert));
                                textViewPopUpAlert.setScaleX(0);
                                textViewPopUpAlert.setScaleY(0);
                                textViewPopUpAlert.animate().scaleX(1).setDuration(500);
                                textViewPopUpAlert.animate().scaleY(1).setDuration(500);
                            } else {
                                String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                                String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                                String stringTime = currentDate + " " + currentTime;
                                int updatedBal = Integer.parseInt((String) textViewBalance.getText()) - Integer.parseInt(String.valueOf(editTextPlus.getText()));

                                //Updating balance in oMemInfo Table
                                db.execSQL(String.format("UPDATE oMemInfo SET oMemBalance=%s WHERE oMemName IS '%s'", updatedBal, String.valueOf(textViewName.getText())));

//                            Log.i("Updated Balance Inserted",String.valueOf(updatedBal));

                                //amount INT,current_balance INT,date VARCHAR,note VARCHAR
                                db.execSQL(String.format("INSERT INTO '%s'(amount,current_balance,date,note) VALUES(%s,%s,'%s','%s')"
                                        , String.valueOf(textViewName.getText())
                                        , (Integer.parseInt(String.valueOf(editTextPlus.getText())) * -1)
                                        , updatedBal
                                        , stringTime
                                        , String.valueOf(editTextPlusNote.getText())));
//                            Log.i("Insertion","TRUE");
//                            other_main_adapter.notifyDataSetChanged();
                                textViewBalance.setText(String.valueOf(updatedBal));
                                Toast.makeText(Others_mainActivity.this, getString(R.string.oPlusToastFalse), Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        }
                    });
                }
            });


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent gotoOdet = new Intent(Others_mainActivity.this,otherDetailsActivity.class);
                    gotoOdet.putExtra("name",String.valueOf(textViewName.getText()));
                    startActivity(gotoOdet);
                }
            });

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
//                    Toast.makeText(mContext, String.valueOf(textViewName.getText()), Toast.LENGTH_SHORT).show();

                    new AlertDialog.Builder(Others_mainActivity.this)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle(getString(R.string.eAlertTitle))
                            .setMessage(getString(R.string.alertboxLine))
                            .setPositiveButton(getString(R.string.alertboxYes), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    db.execSQL(String.format("DELETE FROM oMEmInfo WHERE oMemNAme IS '%s'",currentPosition.getOtherMemName()));
                                    db.execSQL(String.format("DROP TABLE '%s'",currentPosition.getOtherMemName()));
                                    Toast.makeText(mContext, getString(R.string.alertboxToast), Toast.LENGTH_SHORT).show();
                                    arrayList.remove(position);
                                    other_main_adapter.notifyDataSetChanged();
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

    public void AddOtherMemberClicked(View view) {
        Intent gotoAdd = new Intent(Others_mainActivity.this,otherAddNewMemberActivity.class);
        startActivity(gotoAdd);
    }

    public void onBackPressed() {
//        super.onBackPressed();

        Intent gotoHomeMain = new Intent(Others_mainActivity.this, HomeActivity.class);
        startActivity(gotoHomeMain);


    }
}