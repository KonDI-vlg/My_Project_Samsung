package com.example.schedule;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textview.MaterialTextView;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Add_schedule_activity extends AppCompatActivity {

    EditText et_name;
    MaterialTextView tv_time, tv_time_to;

    Button btn_add;

    MyAlarmBroadcastManager broadcastManager = new MyAlarmBroadcastManager();
    MaterialTimePicker timePicker;

    Date dateP;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);

        et_name = findViewById(R.id.et_add_sch_name);
        tv_time = findViewById(R.id.tv_time_add_sch);
        tv_time_to = findViewById(R.id.tv_time_to_add_sch);
        btn_add = findViewById(R.id.btn_add_sch);

        tv_time.setOnClickListener(v -> {
            timePicker =
                    new MaterialTimePicker.Builder()
                            .setTimeFormat(TimeFormat.CLOCK_24H)
                            .setHour((int) (((System.currentTimeMillis() + 10800000) / 3600000) % 24))
                            .setMinute((int) (System.currentTimeMillis() % 3600000 / 60000))
                            .build();

            timePicker.addOnPositiveButtonClickListener(v1 -> {
                @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");

                long selectedTime = timePicker.getHour() * 3600000 + timePicker.getMinute() * 60000;
                simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+0:00"));
                dateP = new Date(selectedTime);

                tv_time.setText(simpleDateFormat.format(dateP));
            });
            timePicker.show(getSupportFragmentManager(), timePicker.toString());
        });
        tv_time_to.setOnClickListener(v -> {
            timePicker =
                    new MaterialTimePicker.Builder()
                            .setTimeFormat(TimeFormat.CLOCK_24H)
                            .setHour((int) (((System.currentTimeMillis() + 10800000) / 3600000) % 24))
                            .setMinute((int) (System.currentTimeMillis() % 3600000 / 60000))
                            .build();

            timePicker.addOnPositiveButtonClickListener(v1 -> {
                @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");

                long selectedTime = timePicker.getHour() * 3600000 + timePicker.getMinute() * 60000;
                simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+0:00"));
                dateP = new Date(selectedTime);

                tv_time_to.setText(simpleDateFormat.format(dateP));
            });
            timePicker.show(getSupportFragmentManager(), timePicker.toString());
        });
        btn_add.setOnClickListener(v -> {
            DataBase db = new DataBase(Add_schedule_activity.this);
            if (et_name.getText().toString().trim().length() == 0 ||
                    tv_time.getText().toString().length() == 7 ||
                    tv_time_to.getText().toString().length() == 8) {
                Toast.makeText(Add_schedule_activity.this, "Заполните поля", Toast.LENGTH_SHORT).show();
            } else {

                db.addSchedule(et_name.getText().toString().trim(),
                        tv_time.getText().toString().trim(),
                        tv_time_to.getText().toString().trim(),
                        "schedule");

                String title_notif = et_name.getText().toString().trim();

                et_name.setText("");
                tv_time.setText("Время с");
                tv_time_to.setText("Время до");
                Toast.makeText(Add_schedule_activity.this, "Успешно добавлено", Toast.LENGTH_SHORT).show();

                Intent data = new Intent();
                setResult(Activity.RESULT_OK);
                finish();
            }
        });


    }

}
