package com.astro.rokka;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import static android.widget.AdapterView.*;

public class HomeListAdapter extends ArrayAdapter<HomeList> {

    private Context mContext;
    private List<HomeList> labList = new ArrayList<>();

    public HomeListAdapter(@NonNull Context context, @SuppressLint("SupportAnnotationUsage") @LayoutRes ArrayList<HomeList> list) {
        super(context, 0 , list);
        mContext = context;
        labList = list;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//            return super.getView(position, convertView, parent);
        View view = convertView;

        view = LayoutInflater.from(mContext).inflate(R.layout.main_row,parent,false);
        HomeList currentPosition = labList.get(position);

        ImageView imageViewPlus = view.findViewById(R.id.imageViewPlus);
        imageViewPlus.setImageResource(currentPosition.getPlusID());

        ImageView imageViewMinus = view.findViewById(R.id.imageViewMinus);
        imageViewMinus.setImageResource(currentPosition.getMinusID());

        TextView textViewName = view.findViewById(R.id.textViewName);
        textViewName.setText(currentPosition.getLabName());

        TextView textViewBalance = view.findViewById(R.id.textViewBalance);
        int bal = currentPosition.getBalance();

        textViewBalance.setText(String.valueOf(currentPosition.getBalance()));
        if(bal < 0){
            textViewBalance.setTextColor(Color.RED);
        }else{
            textViewBalance.setTextColor(Color.GREEN);
        }

        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.i("Adapter Class",String.valueOf(v.getUniqueDrawingId()));
            }
        });



        return view;


    }
}
