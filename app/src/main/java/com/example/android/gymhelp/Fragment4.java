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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.android.gymhelp.Fragment3.getMonthDays;

public class Fragment4 extends Fragment implements AdapterView.OnItemSelectedListener {

    public static final String ARG_PAGE2 = "ARG_PAGE";

    private int mPage;
    private Spinner spinner;
    private BarChart chart;
    DatabaseHelperStat databaseHelperStat;
    public static Fragment4 newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE2, page);
        Fragment4 fragment4 = new Fragment4();
        fragment4.setArguments(args);
        return fragment4;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE2);
    }
    private void fillData(Integer year){
        Map<Integer,Integer > dic = new HashMap<Integer, Integer>();
        dic = databaseHelperStat.getEarningsForYear(year);
        ArrayList<BarEntry> values = new ArrayList<>();
        for(int i=1;i<=12;i++){
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

        BarDataSet barDataSet= new BarDataSet(values,"Earnings per month");
        barDataSet.setColor(Color.rgb(109,76,65));
        barDataSet.setValueTextSize(10);
        BarData barData= new BarData(barDataSet);
        chart.setData(barData);
        chart.invalidate();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment4, container, false);
        spinner = view.findViewById(R.id.spinner);
        chart = view.findViewById(R.id.chart2);
        String [] itemsForSpinner =new String[]{"2019","2020","2021","2022","2023"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_dropdown_item,itemsForSpinner);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position){
            case 0:
                fillData(2019);
                break;
            case 1:
                fillData(2020);
                break;
            case 2:
                fillData(2021);
                break;
            case 3:
                fillData(2022);
                break;
            case 4:
                fillData(2023);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
