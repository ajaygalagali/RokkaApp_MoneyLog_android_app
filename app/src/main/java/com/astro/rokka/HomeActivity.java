package com.astro.rokka;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.astro.rokka.Expense.ExpenseMainActivity;
import com.astro.rokka.OthersFeature.Others_mainActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {

    Spinner spinnerLang;
    String currentLanguage , currentLang;
    Locale myLocale;
    SQLiteDatabase db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        db = openOrCreateDatabase("rokk_db",MODE_PRIVATE,null);
        //Multi Lang Support
        db.execSQL("CREATE TABLE IF NOT EXISTS lang(id INTEGER PRIMARY KEY AUTOINCREMENT,currentlang VARCHAR)");
        Cursor cursor = db.rawQuery("SELECT * FROM lang",null);
        cursor.moveToFirst();
        if(cursor.getCount() == 0){
            db.execSQL("INSERT INTO lang(currentlang) VALUES('en')");
            currentLanguage = "en";
        }else{
            currentLanguage = cursor.getString(1);
//            setLocaleOnCreate(currentLanguage);
//            Log.i("Current Language from DB",currentLanguage);

        }
        setLocaleOnCreate(currentLanguage);
        cursor.close();

        setTheme(R.style.Theme_AppCompat_Light_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        spinnerLang = findViewById(R.id.spinnerLanguage);
        List<String> list = new ArrayList<String>();

        list.add("Select Language");
        list.add("English");
        list.add("ಕನ್ನಡ");
        list.add("हिन्दी");
        list.add("मराठी");
        ArrayAdapter<String> spinner_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLang.setAdapter(spinner_adapter);

        spinnerLang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                switch (position) {
                    case 0:
                        break;
                    case 1:

                        db.execSQL("UPDATE lang SET currentlang = 'en' WHERE id IS 1");
                        setLocale("en");
                        break;

                    case 2:

                        db.execSQL("UPDATE lang SET currentlang = 'kn' WHERE id IS 1");
                        setLocale("kn");
                        break;

                    case 3:
                        db.execSQL("UPDATE lang SET currentlang = 'hi' WHERE id IS 1");
                        setLocale("hi");

                        break;

                    case 4:
                        db.execSQL("UPDATE lang SET currentlang = 'mr' WHERE id IS 1");
                        setLocale("mr");

                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });



    }

    public void setLocale(String localeName) {
        if (!localeName.equals(currentLanguage)) {
            myLocale = new Locale(localeName);
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);
            Intent refresh = new Intent(this, HomeActivity.class);
//            refresh.putExtra(currentLang, localeName);
            startActivity(refresh);
        } else {
            Toast.makeText(HomeActivity.this, "Language already selected!", Toast.LENGTH_SHORT).show();
        }
    }

    public void setLocaleOnCreate(String localeName) {

        myLocale = new Locale(localeName);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);


    }

    public void laboursClicked(View view) {
        Intent goToLabour = new Intent(this,MainActivity.class);
        startActivity(goToLabour);
    }

    public void othersClicked(View view) {
        Intent goToOther = new Intent(this, Others_mainActivity.class);
        startActivity(goToOther);
    }
    public void onBackPressed(){

        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory( Intent.CATEGORY_HOME );
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }

    public void expenseClicked(View view) {
        Intent expIntent = new Intent(HomeActivity.this, ExpenseMainActivity.class);
        startActivity(expIntent);
    }
}

//Todo: Intro Screens
//Todo: Spash Screen
//Todo: About Page