package com.example.schedule;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ActionMenuView;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class Schedule_activity extends AppCompatActivity {

    MaterialToolbar toolbar;
    FloatingActionButton fab;
    RecyclerView rv;
    TasksAdapter adapter;
    DataBase myDB;
    ArrayList<String> sch_name, sch_time, sch_time_to, sch_day, type;
    ArrayList<Integer> sch_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        myDB = new DataBase(this);
        sch_id = new ArrayList<>();
        sch_name = new ArrayList<>();
        sch_time = new ArrayList<>();
        sch_time_to = new ArrayList<>();
        sch_day = new ArrayList<>();
        storeDataInArrays();


        fab = findViewById(R.id.fab_schedule);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(this, Add_schedule_activity.class);
            startActivityForResult(intent, 1);
        });


        adapter = new TasksAdapter(this, this, sch_id, sch_name, null, sch_time, sch_time_to, "schedule");

        rv = findViewById(R.id.rv_schedule);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);


    }


    void storeDataInArrays() {
        Cursor cursor = myDB.readAllDataSchedule();
        if (cursor.getCount() == 0) {

        } else {
            while (cursor.moveToNext()) {
                sch_id.add(cursor.getInt(0));
                sch_name.add(cursor.getString(1));
                sch_time.add(cursor.getString(2));
                sch_time_to.add(cursor.getString(3));
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
}