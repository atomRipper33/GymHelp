package com.example.android.gymhelp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Fragment2 extends Fragment {
    public static final String ARG_PAGE2 = "ARG_PAGE";

    private int mPage;
    TextView textView;
    TextView textView2;
    DatabaseHelperStat databaseHelperStat;
    public static Fragment2 newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE2, page);
        Fragment2 fragment2 = new Fragment2();
        fragment2.setArguments(args);
        return fragment2;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE2);
    }
    public String addPrefix(Integer number){
        return  "0"+number;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment2, container, false);
        databaseHelperStat = new DatabaseHelperStat(getContext());
        textView=view.findViewById(R.id.testing2);
        textView2= view.findViewById(R.id.testing);
        Calendar calendar = Calendar.getInstance();
        Date date = new Date();
        textView.setText(MainActivity.sdf.format(date));
        String month=Integer.toString(calendar.get(Calendar.MONTH)+1);
        String day=Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
        if(calendar.get(Calendar.MONTH)+1<10){
            month= addPrefix(calendar.get(Calendar.MONTH)+1);
        }
        if(calendar.get(Calendar.DAY_OF_MONTH)<10){
            day= addPrefix(calendar.get(Calendar.DAY_OF_MONTH));
        }
        int sum = databaseHelperStat.getEarningsForDay(calendar.get(Calendar.YEAR),month,day);
        textView2.setText(sum+"kn");
        return view;
    }
}
