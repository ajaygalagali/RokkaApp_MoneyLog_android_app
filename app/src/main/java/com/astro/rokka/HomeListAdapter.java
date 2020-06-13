package com.astro.rokka;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import static android.widget.AdapterView.*;

public class HomeListAdapter extends ArrayAdapter<HomeList>{

    private Context mContext;
    private List<HomeList> labList = new ArrayList<>();

    public HomeListAdapter(@NonNull Context context, @SuppressLint("SupportAnnotationUsage") @LayoutRes ArrayList<HomeList> list) {
        super(context, 0 , list);
        mContext = context;
        labList = list;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent) {
//            return super.getView(position, convertView, parent);
        View view = convertView;

        view = LayoutInflater.from(mContext).inflate(R.layout.main_row,parent,false);
        HomeList currentPosition = labList.get(position);



        /*ImageView imageViewPlus = view.findViewById(R.id.imageViewPlus);
        imageViewPlus.setImageResource(currentPosition.getPlusID());*/

        /*ImageView imageViewMinus = view.findViewById(R.id.imageViewMinus);
        imageViewMinus.setImageResource(currentPosition.getMinusID());*/

        final TextView textViewName = view.findViewById(R.id.textViewName);
        textViewName.setText(currentPosition.getLabName());

        textViewName.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.i("Name",String.valueOf(textViewName.getText()));
                Toast.makeText(mContext, String.valueOf(textViewName.getText()), Toast.LENGTH_SHORT).show();

            }
        });

        TextView textViewBalance = view.findViewById(R.id.textViewBalance);
        int bal = currentPosition.getBalance();

        textViewBalance.setText(String.valueOf(currentPosition.getBalance()));
        if(bal < 0){
            textViewBalance.setTextColor(Color.RED);
        }else{
            textViewBalance.setTextColor(Color.GREEN);
        }
        ImageButton imageButtonMinus = view.findViewById(R.id.imageButtonMinus);
        imageButtonMinus.setTag(String.valueOf(position));
//        Log.i("posi",String.valueOf(position));

        imageButtonMinus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.i("Minus",String.valueOf(v.getTag()));
                Intent goToMinus = new Intent(mContext,minusActivity.class);



            }
        });


        ImageButton imageButtonPlus = view.findViewById(R.id.imageButtonPlus);
        imageButtonPlus.setTag(String.valueOf(position));

        imageButtonPlus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.i("Plus",String.valueOf(v.getTag()));
                Intent goToAdd = new Intent(mContext,plusActivity.class);

            }
        });







        return view;


    }
}
