package com.astro.rokka.OthersFeature;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.astro.rokka.AddNewNameActivity;
import com.astro.rokka.MainActivity;
import com.astro.rokka.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class otherAddNewMemberActivity extends AppCompatActivity {


    EditText editTextName;
    EditText editTextBalance;
    SQLiteDatabase db;

    TextView textViewAlert;

    String balance,name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_add_new_member);

        // notification bar color
        Window window = this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            window.setStatusBarColor(ContextCompat.getColor(otherAddNewMemberActivity.this,R.color.mycolor));

        }

        editTextName = findViewById(R.id.editTextOtherMemName);
        editTextBalance = findViewById(R.id.editTextOtherBal);

        textViewAlert = findViewById(R.id.textViewOaddAlert);

    }

    public void oAddNameClicked(View view) {
        name = String.valueOf(editTextName.getText()).trim();

        db = openOrCreateDatabase("others_db",MODE_PRIVATE,null);
        db.execSQL("CREATE TABLE IF NOT EXISTS oMemInfo(id INTEGER PRIMARY KEY AUTOINCREMENT,oMemName VARCHAR, oMemBalance INT)");
//        Log.i("Table","Member Info Created!");


        Boolean flagReg = AddNewNameActivity.isValidUsername(name);

        if(name.isEmpty()){
//            Toast.makeText(this,"Enter Name",Toast.LENGTH_SHORT).show();
            textViewAlert.setText(getString(R.string.addMName));
            textViewAlert.setScaleX(0);
            textViewAlert.setScaleY(0);
            textViewAlert.animate().scaleX(1).setDuration(500);
            textViewAlert.animate().scaleY(1).setDuration(500);
        }else if(!flagReg){
//            Toast.makeText(this, "Name shouldn't contain so and so", Toast.LENGTH_SHORT).show();
            textViewAlert.setText(getString(R.string.addMAlertA));
            textViewAlert.setScaleX(0);
            textViewAlert.setScaleY(0);
            textViewAlert.animate().scaleX(1).setDuration(500);
            textViewAlert.animate().scaleY(1).setDuration(500);
        }else {

            Cursor cc = db.rawQuery("SELECT * FROM oMemInfo",null);
            cc.moveToFirst();
            int flagUsernameCheck =0;
            int mem_name_index = cc.getColumnIndex("oMemName");
            int size_of_t = cc.getCount();
            for(int i=0;i<size_of_t;i++){
                String db_mem_name = cc.getString(mem_name_index);
//                Log.i("Member Name",db_mem_name);
//                Log.i("User Input",Name);
                if(name.equalsIgnoreCase(db_mem_name)){
                    /*textViewAlertAddMember.setText(getString(R.string.addMAlertB));
                    textViewAlertAddMember.setScaleX(0);
                    textViewAlertAddMember.setScaleY(0);
                    textViewAlertAddMember.animate().scaleX(1).setDuration(500);
                    textViewAlertAddMember.animate().scaleY(1).setDuration(500);*/
//                    Toast.makeText(this, "UserName Taken", Toast.LENGTH_SHORT).show();
                    textViewAlert.setText(getString(R.string.addMAlertB));
                    textViewAlert.setScaleX(0);
                    textViewAlert.setScaleY(0);
                    textViewAlert.animate().scaleX(1).setDuration(500);
                    textViewAlert.animate().scaleY(1).setDuration(500);
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

                db.execSQL(String.format("INSERT INTO oMemInfo (oMemName,oMemBalance) VALUES('%s',%s)", name, balance));

                //CREATING TABLE 2
                String tabName = String.valueOf(editTextName.getText());
                tabName = tabName.replace(" ", "_");

//                db.execSQL(String.format("CREATE TABLE IF NOT EXISTS '%s'(id INTEGER PRIMARY KEY AUTOINCREMENT,amount INT,current_balance INT,date VARCHAR,note VARCHAR)", mem_id_from_db));
                db.execSQL(String.format("CREATE TABLE IF NOT EXISTS '%s'(id INTEGER PRIMARY KEY AUTOINCREMENT,amount INT,date VARCHAR,note VARCHAR)", tabName));

                String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                String stringTime = currentDate + " " + currentTime;
                db.execSQL(String.format("INSERT INTO '%s'(amount,date,note) VALUES(%s,'%s','%s')"
                        , tabName
                        , balance
                        , stringTime
                        , getString(R.string.initialBal)));
//                Log.i("Table 2",tabName);

                Others_mainActivity.other_main_adapter.notifyDataSetChanged();
                Intent goToMain = new Intent(otherAddNewMemberActivity.this, Others_mainActivity.class);
                startActivity(goToMain);
            }


        }



    }

    public void arrowBackClicked(View view) {

            finish();
    }
}