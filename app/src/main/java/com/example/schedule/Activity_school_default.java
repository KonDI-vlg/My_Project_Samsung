package com.example.schedule;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.ActionBar;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toolbar;


public class Activity_school_default extends AppCompatActivity {

    ViewPagerAdapter adapter;
    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    FloatingActionButton fab;
    EditText editText;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_default);
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), getApplicationContext());


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(adapter);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        editText = findViewById(R.id.et_add_lesson);

        button = findViewById(R.id.btn_school);
        button.setOnClickListener(v -> {
            DataBase myDB = new DataBase(getApplicationContext());
            int btnpos = adapter.getBtn_pos();
            if (btnpos == 0) {
                myDB.setSchoolLessons("school","monday", editText.getText().toString());
                editText.setText("");
                adapter.notifyDataSetChanged();
            } else if (btnpos == 1 ){
                myDB.setSchoolLessons("school","tuesday", editText.getText().toString());
                editText.setText("");
                adapter.notifyDataSetChanged();
            } else if (btnpos == 2) {
                myDB.setSchoolLessons("school","wednesday", editText.getText().toString());
                editText.setText("");
                adapter.notifyDataSetChanged();
            } else if (btnpos == 3) {
                myDB.setSchoolLessons("school","thursday", editText.getText().toString());
                editText.setText("");
                adapter.notifyDataSetChanged();
            } else if (btnpos == 4) {
                myDB.setSchoolLessons("school","friday", editText.getText().toString());
                editText.setText("");
                adapter.notifyDataSetChanged();
            } else if (btnpos == 5) {
                myDB.setSchoolLessons("school","saturday", editText.getText().toString());
                editText.setText("");
                adapter.notifyDataSetChanged();
            }

        });

    }

}