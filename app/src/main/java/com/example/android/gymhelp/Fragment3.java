package com.example.android.gymhelp;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Fragment3 extends Fragment implements AdapterView.OnItemSelectedListener {
    public static final String ARG_PAGE2 = "ARG_PAGE";
    private int mPage;
    private BarChart chart;
    Spinner spinnerMonth;
    Spinner spinnerYear;
    DatabaseHelperStat databaseHelperStat;

    public static Fragment3 newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE2, page);
        Fragment3 fragment3 = new Fragment3();
        fragment3.setArguments(args);
        return fragment3;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE2);
    }
    public static int getMonthDays(int month, int year) {
        int daysInMonth ;
        if (month == 4 || month == 6 || month == 9 || month == 11) {
            daysInMonth = 30;
        }
        else {
            if (month == 2) {
                daysInMonth = (year % 4 == 0) ? 29 : 28;
            } else {
                daysInMonth = 31;
            }
        }
        return daysInMonth;
    }

    private void fillData(String month, Integer year){
        Map <Integer,Integer > dic = new HashMap<Integer, Integer>();
        dic = databaseHelperStat.getEarningsForThirtyDays(month,year);
        ArrayList<BarEntry> values = new ArrayList<>();
        for(int i=1;i<=getMonthDays(7,2019);i++){
            if (dic.get(i)==null){
                values.add(new BarEntry(i,0));
            }else{
                values.add(new BarEntry(i,dic.get(i)));
            }
        }
        try {
            chart.clearValues();
        }catch (Exception e){
            Log.i("test",e.toString());
        }

        BarDataSet barDataSet= new BarDataSet(values,"Earnings per day");
        barDataSet.setColor(Color.rgb(109,76,65));
        barDataSet.setValueTextSize(10);
        BarData barData= new BarData(barDataSet);
        chart.setData(barData);
        chart.invalidate();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fagment3, container, false);
        chart = view.findViewById(R.id.chart1);
        spinnerMonth=view.findViewById(R.id.spinnerMonth);
        spinnerYear=view.findViewById(R.id.spinnerYear);
        String [] itemsForSpinner = new String[]{"January","February","March","April","May","June","July","August","September","October","November","December"};
        String [] itemsForSpinner2 =new String[]{"2019","2020","2021","2022","2023"};
        ArrayAdapter <String> adapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_dropdown_item,itemsForSpinner);
        ArrayAdapter <String> adapter2 = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_dropdown_item,itemsForSpinner2);
        spinnerMonth.setAdapter(adapter);
        spinnerYear.setAdapter(adapter2);
        spinnerMonth.setOnItemSelectedListener(this);
        spinnerYear.setOnItemSelectedListener(this);
        chart.setDrawBarShadow(false);
        chart.setDrawValueAboveBar(true);
        databaseHelperStat = new DatabaseHelperStat(getContext());
        chart.getDescription().setEnabled(false);
        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        chart.setMaxVisibleValueCount(60);
        // scaling can now only be done on x- and y-axis separately
        chart.setPinchZoom(true);
        chart.setDrawGridBackground(true);
        YAxis yAxis= chart.getAxisRight();
        yAxis.setDrawLabels(false);
        yAxis.setAxisMinimum(0);
        YAxis yAxis1= chart.getAxisLeft();
        yAxis1.setAxisMinimum(0);
        XAxis xAxis=chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelCount(12);
        Legend legend = chart.getLegend();
        legend.setTextColor(Color.WHITE);
        return view;
    }
    public String addPrefix(Integer number){
        return  "0"+number;
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner = (Spinner) parent;
        if(spinner.getId()==R.id.spinnerMonth){
            switch (position) {
                case 0:
                    Log.i("testing",spinnerYear.getItemAtPosition(spinnerYear.getSelectedItemPosition()).toString());
                    fillData("01", Integer.parseInt(spinnerYear.getItemAtPosition(spinnerYear.getSelectedItemPosition()).toString()));
                    break;
                case 1:
                    fillData("02", Integer.parseInt(spinnerYear.getItemAtPosition(spinnerYear.getSelectedItemPosition()).toString()));
                    break;
                case 2:
                    fillData("03", Integer.parseInt(spinnerYear.getItemAtPosition(spinnerYear.getSelectedItemPosition()).toString()));
                    break;
                case 3:
                    fillData("04", Integer.parseInt(spinnerYear.getItemAtPosition(spinnerYear.getSelectedItemPosition()).toString()));
                    break;
                case 4:
                    fillData("05", Integer.parseInt(spinnerYear.getItemAtPosition(spinnerYear.getSelectedItemPosition()).toString()));
                    break;
                case 5:
                    fillData("06", Integer.parseInt(spinnerYear.getItemAtPosition(spinnerYear.getSelectedItemPosition()).toString()));
                    break;
                case 6:
                    fillData("07", Integer.parseInt(spinnerYear.getItemAtPosition(spinnerYear.getSelectedItemPosition()).toString()));
                    break;
                case 7:
                    fillData("08", Integer.parseInt(spinnerYear.getItemAtPosition(spinnerYear.getSelectedItemPosition()).toString()));
                    break;
                case 8:
                    fillData("09", Integer.parseInt(spinnerYear.getItemAtPosition(spinnerYear.getSelectedItemPosition()).toString()));
                    break;
                case 9:
                    fillData("10", Integer.parseInt(spinnerYear.getItemAtPosition(spinnerYear.getSelectedItemPosition()).toString()));
                    break;
                case 10:
                    fillData("11", Integer.parseInt(spinnerYear.getItemAtPosition(spinnerYear.getSelectedItemPosition()).toString()));
                    break;
                case 11:
                    fillData("12", Integer.parseInt(spinnerYear.getItemAtPosition(spinnerYear.getSelectedItemPosition()).toString()));
                    break;
        }
        }else if(spinner.getId()==R.id.spinnerYear){
            switch (position) {
                case 0:
                    if (spinnerMonth.getSelectedItemPosition()+1<10){
                        fillData(addPrefix(spinnerMonth.getSelectedItemPosition()+1),2019);
                    }else{
                        fillData(Integer.toString(spinnerMonth.getSelectedItemPosition()+1),2019);
                    }
                    break;
                case 1:
                    if (spinnerMonth.getSelectedItemPosition()+1<10){
                        fillData(addPrefix(spinnerMonth.getSelectedItemPosition()+1),2020);
                    }else{
                        fillData(Integer.toString(spinnerMonth.getSelectedItemPosition()+1),2020);
                    }
                    break;
                case 2:
                    if (spinnerMonth.getSelectedItemPosition()+1<10){
                        fillData(addPrefix(spinnerMonth.getSelectedItemPosition()+1),2021);
                    }else{
                        fillData(Integer.toString(spinnerMonth.getSelectedItemPosition()+1),2021);
                    }
                    break;
                case 3:
                    if (spinnerMonth.getSelectedItemPosition()+1<10){
                        fillData(addPrefix(spinnerMonth.getSelectedItemPosition()+1),2022);
                    }else{
                        fillData(Integer.toString(spinnerMonth.getSelectedItemPosition()+1),2022);
                    }
                    break;
                case 4:
                    if (spinnerMonth.getSelectedItemPosition()+1<10){
                        fillData(addPrefix(spinnerMonth.getSelectedItemPosition()+1),2023);
                    }else{
                        fillData(Integer.toString(spinnerMonth.getSelectedItemPosition()+1),2023);
                    }
                    break;
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    //Integer [] earningsForThirtyDay=getEarningsForThirtyDays();
}
