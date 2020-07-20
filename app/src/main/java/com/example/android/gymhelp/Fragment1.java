package com.example.android.gymhelp;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.sundeepk.compactcalendarview.domain.Event;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Fragment1 extends Fragment {

        public static final String ARG_PAGE = "ARG_PAGE";
    public RecyclerView recyclerView;
        private int mPage;
    public static DatabaseHelper mDatabaseHelper;
    public static MemberAdapter adapter;
    public static List<Member> memberList;
    public static String date;
        public static Fragment1 newInstance(int page) {
            Bundle args = new Bundle();
            args.putInt(ARG_PAGE, page);
            Fragment1 fragment = new Fragment1();
            fragment.setArguments(args);
            return fragment;
        }
    //fillRecyclerView(Calendar.getInstance().getTime());

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mPage = getArguments().getInt(ARG_PAGE);
        }
    public static void fillRecyclerView(Date dateOn,List<Event> bookingsFromMap){

        date = MainActivity.sdf.format(dateOn);
        Log.i("check",date);
        Log.i("check2",bookingsFromMap.toString());
        Cursor dataForDate = mDatabaseHelper.getDataByDate(date);
        if (!bookingsFromMap.isEmpty()) {
            memberList.clear();
            while (dataForDate.moveToNext()) {
                memberList.add(new Member(dataForDate.getString(0), dataForDate.getString(2),dataForDate.getInt(1),
                        dataForDate.getString(3)));
            }
        }else{
            //textViewNo.setVisibility(View.VISIBLE);
            memberList.clear();
        }
        adapter.notifyDataSetChanged();
    }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment1, container, false);
            recyclerView = view.findViewById(R.id._dynamic);
            recyclerView.hasFixedSize();
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            mDatabaseHelper = new DatabaseHelper(getContext());
            memberList = new ArrayList<>();
            adapter = new MemberAdapter(getActivity(),memberList);
            recyclerView.setAdapter(adapter);
            return view;
        }
}

