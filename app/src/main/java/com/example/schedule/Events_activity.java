package com.example.schedule;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class Events_activity extends AppCompatActivity {

    RecyclerView recyclerView;
    TasksAdapter tasksAdapter;
    FloatingActionButton fab;
    ArrayList<String> task_name, task_date, task_time;
    ArrayList<Integer> task_id;
    DataBase myDB;
    MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        myDB = new DataBase(this);
        task_id = new ArrayList<>();
        task_name = new ArrayList<>();
        task_date = new ArrayList<>();
        task_time = new ArrayList<>();
        storeDataInArrays();

//        toolbar = findViewById(R.id.toolbar_actionbar);
//        setSupportActionBar(toolbar);

        tasksAdapter = new TasksAdapter(this, this, task_id, task_name, task_date, task_time, null, "event");

        recyclerView = findViewById(R.id.rv_events);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(tasksAdapter);

        fab = findViewById(R.id.fab_events);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(this, Add_activity.class);
            startActivityForResult(intent, 1);
        });


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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            setResult(Activity.RESULT_OK);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent data = new Intent();
        setResult(Activity.RESULT_OK);
    }
}