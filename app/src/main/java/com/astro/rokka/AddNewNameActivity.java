package com.astro.rokka;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddNewNameActivity extends AppCompatActivity {
    EditText editTextName;
    EditText editTextBalance;
    String Name;
    static Map<String, HomeList> map = new HashMap<>();
    String balance;
    int mem_id_from_db;
    String mem_name_from_db;
    Integer mem_balance_from_db;

    TextView textViewAlertAddMember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_name);

        Window window = this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.mycolor));


        editTextName = findViewById(R.id.editTextTextPersonName);
        editTextBalance = findViewById(R.id.editTextLabBalance);

        textViewAlertAddMember = findViewById(R.id.textViewAlertADdMember);
    }

    public static boolean isValidUsername(String name)
    {

        // Regex to check valid username.
        String regex = "[^\".\']+";
        // Compile the ReGex
        Pattern p = Pattern.compile(regex);

        // If the username is empty
        // return false
        if (name == null) {
            return false;
        }

        // Pattern class contains matcher() method
        // to find matching between given username
        // and regular expression.
        Matcher m = p.matcher(name);

        // Return if the username
        // matched the ReGex
        return m.matches();
    }

    public void addNameClicked(View view) {
        SQLiteDatabase db = openOrCreateDatabase("rokk_db", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS member_info (id INTEGER PRIMARY KEY AUTOINCREMENT,mem_name VARCHAR, mem_balance INT)");

        Name = String.valueOf(editTextName.getText()).trim();
        
        Boolean flagReg = isValidUsername(Name);


        if(Name.isEmpty()){
//            Toast.makeText(this, "ಹೆಸರು ಬರೆಯಿರಿ ", Toast.LENGTH_SHORT).show();
            textViewAlertAddMember.setText(getString(R.string.addMName));
            textViewAlertAddMember.setScaleX(0);
            textViewAlertAddMember.setScaleY(0);
            textViewAlertAddMember.animate().scaleX(1).setDuration(500);
            textViewAlertAddMember.animate().scaleY(1).setDuration(500);
        }else if(!flagReg){
            textViewAlertAddMember.setText(getString(R.string.addMAlertA));
            textViewAlertAddMember.setScaleX(0);
            textViewAlertAddMember.setScaleY(0);
            textViewAlertAddMember.animate().scaleX(1).setDuration(500);
            textViewAlertAddMember.animate().scaleY(1).setDuration(500);

        }else {

            @SuppressLint("Recycle") Cursor cc = db.rawQuery("SELECT * FROM member_info",null);
            cc.moveToFirst();
            int flagUsernameCheck =0;
            int mem_name_index = cc.getColumnIndex("mem_name");
            int size_of_t = cc.getCount();
            for(int i=0;i<size_of_t;i++){
                String db_mem_name = cc.getString(mem_name_index);
//                Log.i("Member Name",db_mem_name);
//                Log.i("User Input",Name);
                if(Name.equalsIgnoreCase(db_mem_name)){
                    textViewAlertAddMember.setText(getString(R.string.addMAlertB));
                    textViewAlertAddMember.setScaleX(0);
                    textViewAlertAddMember.setScaleY(0);
                    textViewAlertAddMember.animate().scaleX(1).setDuration(500);
                    textViewAlertAddMember.animate().scaleY(1).setDuration(500);
                    flagUsernameCheck = 1;
                    break;
                }
                cc.moveToNext();
            }
            if(flagUsernameCheck == 0){
                balance = String.valueOf(editTextBalance.getText());
                if (balance.isEmpty()) {
                    balance = "0";
                }

                db.execSQL(String.format("INSERT INTO member_info (mem_name,mem_balance) VALUES('%s',%s)", Name, balance));

                /*@SuppressLint("Recycle") Cursor c = db.rawQuery(String.format("SELECT * FROM member_info WHERE mem_name IS '%s'", Name), null);
                int mem_idIndex = c.getColumnIndex("id");
                c.moveToFirst();*/
                /*boolean flag = true;
                while (flag) {
                    mem_id_from_db = c.getInt(mem_idIndex);
                    flag = false;
                }*/

                //CREATING TABLE 2
                db.execSQL(String.format("CREATE TABLE IF NOT EXISTS '%s'(id INTEGER PRIMARY KEY AUTOINCREMENT,days INT,total_wages INT,paid_wages INT,rem_wages INT,date VARCHAR,note VARCHAR,halfdays INT)", Name));

                String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                String stringTime = currentDate + " " + currentTime;

                db.execSQL(String.format("INSERT INTO '%s' (date,days,paid_wages,rem_wages,note,total_wages,halfdays) VALUES('%s',%s,%s,%s,'%s',%s,%s)",Name , stringTime, 0, 0, balance, getString(R.string.initialBal), 0, 0));


                MainActivity.homeListAdapter.notifyDataSetChanged();
                Intent goToMain = new Intent(AddNewNameActivity.this, MainActivity.class);
                startActivity(goToMain);
            }


        }
    }

    public void arrowBackClicked(View view) {
        finish();
    }
}