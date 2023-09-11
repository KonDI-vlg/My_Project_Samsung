package com.example.schedule;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class Archive_activity extends AppCompatActivity {

    RecyclerView recyclerView;
    TasksAdapter tasksAdapter;
    FloatingActionButton fab;
    ArrayList<String> task_name, task_date, task_time;
    ArrayList<Integer> task_id;
    DataBase myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archive);
        myDB = new DataBase(this);
        task_id = new ArrayList<>();
        task_name = new ArrayList<>();
        task_date = new ArrayList<>();
        task_time = new ArrayList<>();
        storeDataInArrays();

//        toolbar = findViewById(R.id.toolbar_actionbar);
//        setSupportActionBar(toolbar);

        tasksAdapter = new TasksAdapter(this, this, task_id, task_name, task_date, task_time, null, "archive");

        recyclerView = findViewById(R.id.rv_archive);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(tasksAdapter);


    }

    void storeDataInArrays() {
        Cursor cursor = myDB.readAllDataEvent();
        if (cursor.getCount() == 0) {

        } else {
            while (cursor.moveToNext()) {
                task_id.add(cursor.getInt(0));
                task_name.add(cursor.getString(1));
                task_date.add(cursor.getString(2));
                task_time.add(cursor.getString(3));
            }
        }
    }
}