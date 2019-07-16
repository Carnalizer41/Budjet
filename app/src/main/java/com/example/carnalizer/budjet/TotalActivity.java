package com.example.carnalizer.budjet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class TotalActivity extends AppCompatActivity {

    DB dbHelper;
    private ListView allTasks;
    private ExpenseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total);

        dbHelper = new DB(this);
        allTasks = findViewById(R.id.history_list);

        loadAllTasks();
    }

    private void loadAllTasks()
    {
        ArrayList<Expense> taskList = dbHelper.getHistory();
        Collections.sort(taskList, new Comparator<Expense>() {
            @Override
            public int compare(Expense o1, Expense o2) {
                return o2.getDate().compareTo(o1.getDate());
            }
        });
        if(adapter==null)
        {
            adapter = new ExpenseAdapter(this,R.layout.total_row, taskList);
            allTasks.setAdapter(adapter);
        }
        else
        {
            adapter.clear();
            adapter.addAll(taskList);
            adapter.notifyDataSetChanged();
        }
    }


    public void onClickMainMenu(View view)
    {
        Intent intent = new Intent(TotalActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
