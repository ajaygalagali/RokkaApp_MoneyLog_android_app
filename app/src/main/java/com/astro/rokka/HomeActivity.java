package com.astro.rokka;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.astro.rokka.Expense.ExpenseMainActivity;
import com.astro.rokka.OthersFeature.Others_mainActivity;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
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