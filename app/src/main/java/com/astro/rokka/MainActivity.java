package com.astro.rokka;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.textclassifier.TextLanguage;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    ListView listViewMain;
    static ArrayList<HomeList> arrayList;
    @SuppressLint("StaticFieldLeak")
    static HomeListAdapter homeListAdapter;
    SQLiteDatabase db;
    String mem_name_from_db;
    Integer mem_balance_from_db;
    String mem_id_from_db;

    //Multi Language Support

//    Spinner spinnerLang;
//    String currentLanguage , currentLang;
//    Locale myLocale;

    ConstraintLayout clMain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = openOrCreateDatabase("rokk_db",MODE_PRIVATE,null);



        Window window = this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            window.setStatusBarColor(ContextCompat.getColor(MainActivity.this,R.color.mycolor));

        }

        listViewMain = findViewById(R.id.listViewMain);
        clMain  = findViewById(R.id.constraintLayoutMain);

        arrayList = new ArrayList<>();
        arrayList.clear();


        homeListAdapter = new HomeListAdapter(this,arrayList);

        db.execSQL("CREATE TABLE IF NOT EXISTS member_info (id INTEGER PRIMARY KEY AUTOINCREMENT,mem_name VARCHAR, mem_balance INT)");
        Cursor c = db.rawQuery("SELECT * FROM member_info",null);

        int mem_NameIndex = c.getColumnIndex("mem_name");
        int mem_balanceIndex = c.getColumnIndex("mem_balance");


        c.moveToFirst();
        int j= c.getCount();
        for(int i=0;i<j;i++){
            mem_name_from_db = String.valueOf(c.getString(mem_NameIndex));
            mem_balance_from_db = c.getInt(mem_balanceIndex);

            arrayList.add(new HomeList(mem_balance_from_db,mem_name_from_db));

            c.moveToNext();

        }
        c.close();
        if(arrayList.isEmpty()){
            clMain.setVisibility(View.VISIBLE);
        }
        listViewMain.setAdapter(homeListAdapter);

    }


    public void NewNameClickedd(View view) {
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
            final TextView textViewBalance = view.findViewById(R.id.textViewBalance);

            textViewName.setText(currentPosition.getLabName());

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                Log.i("Name",String.valueOf(textViewName.getText()));
//                    Toast.makeText(mContext, String.valueOf(textViewName.getText()), Toast.LENGTH_SHORT).show();
                    Intent goToDet = new Intent(MainActivity.this,DetailsActivity.class);


//                    db.execSQL("CREATE TABLE IF NOT EXISTS member_info (id INTEGER PRIMARY KEY AUTOINCREMENT,mem_name VARCHAR, mem_balance INT)");
                    Cursor c = db.rawQuery(String.format("SELECT * FROM member_info WHERE mem_name IS '%s'", textViewName.getText()),null);

                    int mem_idIndex = c.getColumnIndex("id");

                    c.moveToFirst();
                    int j= c.getCount();
                    for(int i=0;i<j;i++){
                        mem_id_from_db = String.valueOf(c.getString(mem_idIndex));
                        c.moveToNext();
                    }
                    c.close();

                    goToDet.putExtra("mem_id",String.valueOf(mem_id_from_db));
                    goToDet.putExtra("name",textViewName.getText());
                    goToDet.putExtra("balance",String.valueOf(textViewBalance.getText()));

                    startActivity(goToDet);

                }
            });


            int bal = currentPosition.getBalance();

            textViewBalance.setText(String.valueOf(currentPosition.getBalance()));
            if(bal < 0){
                textViewBalance.setTextColor(Color.GREEN);
            }else{
                textViewBalance.setTextColor(Color.RED);
            }




            ImageButton imageButtonPlus = view.findViewById(R.id.imageButtonPlus);
            imageButtonPlus.setTag(String.valueOf(position)); //not used

            imageButtonPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                Log.i("Plus",String.valueOf(v.getTag()));
                    Intent goToAdd = new Intent(mContext,plusActivity.class);
                    goToAdd.putExtra("name",String.valueOf(textViewName.getText()));
                    goToAdd.putExtra("currentBalance",String.valueOf(textViewBalance.getText())); //not used
                    Cursor c = db.rawQuery(String.format("SELECT * FROM member_info WHERE mem_name IS '%s'", textViewName.getText()),null);

                    int mem_idIndex = c.getColumnIndex("id");

                    c.moveToFirst();
                    int j= c.getCount();
                    for(int i=0;i<j;i++){
                        mem_id_from_db = String.valueOf(c.getString(mem_idIndex));
                        c.moveToNext();
                    }
                    c.close();
                    goToAdd.putExtra("position",String.valueOf(mem_id_from_db));

                    startActivity(goToAdd);

                }
            });

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
//                    Toast.makeText(mContext, String.valueOf(textViewName.getText()), Toast.LENGTH_SHORT).show();

                    new AlertDialog.Builder(MainActivity.this)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle(String.valueOf(textViewName.getText()+getString(R.string.alertboxTitle)))
                            .setMessage(getString(R.string.alertboxLine))
                            .setPositiveButton(getString(R.string.alertboxYes), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    db.execSQL(String.format("DELETE FROM member_info WHERE mem_name IS '%s'",textViewName.getText().toString()));
                                    db.execSQL(String.format("DROP TABLE %s",textViewName.getText().toString().replace(" ","_")));
                                    Toast.makeText(mContext, getString(R.string.alertboxToast), Toast.LENGTH_SHORT).show();
                                    arrayList.remove(position);
                                    homeListAdapter.notifyDataSetChanged();
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

    public void onBackPressed(){

        Intent homeIntent = new Intent(MainActivity.this,HomeActivity.class);
        startActivity(homeIntent);
    }
    public void arrowBackClicked(View view) {
        Intent homeIntent = new Intent(MainActivity.this,HomeActivity.class);
        startActivity(homeIntent);
    }
}