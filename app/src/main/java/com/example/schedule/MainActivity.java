package com.example.schedule;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;

import androidx.navigation.ui.AppBarConfiguration;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    MaterialToolbar toolbar;
    DrawerLayout drawerLayout;
    MaterialCardView cv_events, cv_school, cv_schedule, cv_archive;
    MaterialTextView tv_name, tv_date, tv_time;
    String rawUpTask;
    DataBase myDB;

//    DataBase myDB;
//    ArrayList<String> task_name, task_date, task_time;
//    ArrayList<Integer> task_id;
//    TasksAdapter tasksAdapter;


    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        toolbar = findViewById(R.id.toolbar_actionbar);
//        setSupportActionBar(toolbar);

        cv_events = findViewById(R.id.cv_event);
        cv_events.setOnClickListener((v -> {
            Intent intent = new Intent(this, Events_activity.class);
            startActivityForResult(intent, 1);
        }));

        cv_schedule = findViewById(R.id.cv_day_schedule);
        cv_schedule.setOnClickListener((v -> {
            Intent intent = new Intent(this, Schedule_activity.class);
            startActivityForResult(intent, 1);
        }));

        cv_school = findViewById(R.id.cv_school);
        cv_school.setOnClickListener((v -> {
            Intent intent = new Intent(this, Activity_school_default.class);
            startActivity(intent);
        }));

        cv_archive = findViewById(R.id.cv_archive);
        cv_archive.setOnClickListener((v -> {
            Intent intent = new Intent(this, Archive_activity.class);
            startActivity(intent);
        }));

        tv_name = findViewById(R.id.tv_name_upcoming);
        tv_date = findViewById(R.id.tv_date_upcoming);
        tv_time = findViewById(R.id.tv_time_upcoming);

        myDB = new DataBase(this);
//        myDB.onUpgrade(myDB.getWritableDatabase(),1,2);
        rawUpTask = myDB.getUpcomingTask();
        String[] upTask = rawUpTask.split(",");
        tv_name.setText(upTask[0]);
        tv_date.setText(upTask[1]);
        tv_time.setText(upTask[2]);

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            myDB = new DataBase(this);
            rawUpTask = myDB.getUpcomingTask();
            String[] upTask = rawUpTask.split(",");
            tv_name.setText(upTask[0]);
            tv_date.setText(upTask[1]);
            tv_time.setText(upTask[2]);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        myDB = new DataBase(this);
        rawUpTask = myDB.getUpcomingTask();
        String[] upTask = rawUpTask.split(",");
        tv_name.setText(upTask[0]);
        tv_date.setText(upTask[1]);
        tv_time.setText(upTask[2]);
    }
}
