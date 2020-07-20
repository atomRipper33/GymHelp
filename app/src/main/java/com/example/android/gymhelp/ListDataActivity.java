package com.example.android.gymhelp;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListDataActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private static final String TAG = "ListDataActivity";
    DatabaseHelper mDatabaseHelper;
    RecyclerView recyclerView;
    List<Member> memberList;
    MemberAdapter adapter;
    private TextView memberCount;
    private int memberCountNum;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_layout);
        mDatabaseHelper = new DatabaseHelper(this);
        Toolbar toolbar = findViewById(R.id.tool_bar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = findViewById(R.id.recyclerView);
        memberCount = findViewById(R.id.textView22);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        memberList = new ArrayList<>();
        populateListView();
        adapter = new MemberAdapter(this, memberList);
        recyclerView.setAdapter(adapter);
        memberCount.setText(Integer.toString(adapter.getItemCount()));
    }

    private void populateListView() {
        Cursor data = mDatabaseHelper.getData();
        while (data.moveToNext()) {
            try {
                memberList.add(new Member(data.getString(0), data.getString(2),data.getInt(1),data.getString(3)));
                Log.i("member test", data.getString(2));
            }catch (Exception e){
                Log.i("error","error");
            }
        }
        data.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.member_search, menu);
        MenuItem search = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        searchView.setOnQueryTextListener(this);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        newText = newText.toLowerCase();
        ArrayList<Member> newList = new ArrayList<>();
        for (Member member : memberList){
            String name = member.getName().toLowerCase();
            if (name.contains(newText)){
                newList.add(member);
            }
        }
        adapter.setFilter(newList);
        return true;
    }


}




