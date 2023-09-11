package com.example.schedule;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Add_activity extends AppCompatActivity {

    TextView currentDate, currentTime;
    EditText taskName;
    Button btnAdd, btnTest;
    DataBase dbHelper;
    MaterialTimePicker timePicker;
    MaterialDatePicker<Long> datePicker;
    Date dateP;
    MyAlarmBroadcastManager broadcastManager;
    long date_alarm, sec_alarm, time_reminder;


    String[] data = {"Не напоминать", "Напомнить за 10 минут", "Напомнить за 30 минут",
            "Напомнить за час", "Напомнить за 6 часов", "Напомнить за 12 часов",
            "Напомнить за день", "Напомнить за неделю"};
    Long[] reminder_stats = {0L, 600000L, 1800000L, 3600000L, 21600000L, 43200000L, 86400000L, 604800000L};


    String name, date, time;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_add_activity);

        currentDate = (TextView) findViewById(R.id.tv_time_add);
        currentTime = (TextView) findViewById(R.id.tv_time_to_add);
        taskName = (EditText) findViewById(R.id.et_add_name);

        dbHelper = new DataBase(this);


        btnAdd = (Button) findViewById(R.id.btn_add);                   // КНОПКА ADD
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataBase db = new DataBase(Add_activity.this);
                broadcastManager = new MyAlarmBroadcastManager();

                long time_alarm = date_alarm + sec_alarm;

                if (taskName.getText().toString().length() == 0 ||
                        currentDate.getText().toString().length() == 4 ||
                        currentTime.getText().toString().length() == 6) {
                    Toast.makeText(Add_activity.this, "Заполните поля", Toast.LENGTH_SHORT).show();
                } else {

                    db.addEvent(taskName.getText().toString().trim(),
                            currentDate.getText().toString().trim(),
                            currentTime.getText().toString().trim(),
                            String.valueOf(date_alarm + sec_alarm),
                            "event");

                    String title_notif = taskName.getText().toString().trim();

                    broadcastManager.setAlarm(getApplicationContext(), title_notif, time_alarm, currentDate.getText().toString(), currentTime.getText().toString());
                    broadcastManager.setReminder(getApplicationContext(), title_notif, time_alarm, time_reminder);

                    taskName.setText("");
                    currentDate.setText("Дата");
                    currentTime.setText("Время ");

                    Toast.makeText(Add_activity.this, "Успешно добавлено", Toast.LENGTH_SHORT).show();

                    Intent data = new Intent();
                    setResult(Activity.RESULT_OK);
                    finish();
                }

            }

        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner = (Spinner) findViewById(R.id.spinner_add);
        spinner.setAdapter(adapter);
        spinner.setPrompt("Напоминание");
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                time_reminder = reminder_stats[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

//        btnTest = findViewById(R.id.btn_test);
//        btnTest.setOnClickListener(v -> {
//            DataBase myDB = new DataBase(this);
//            myDB.setSchoolLessons("school","monday","Math");
//        });
    }


    public void setDate(View v) {
        datePicker =
                MaterialDatePicker.Builder.datePicker()
                        .setTitleText("Select date")
                        .setSelection(MaterialDatePicker.todayInUtcMilliseconds() + 10800000)
                        .build();

        datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {

                @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleFormat = new SimpleDateFormat("dd/MMM/yyyy");
                long selectedDate = selection;
                dateP = new Date(selectedDate);
                date_alarm = dateP.getTime();
                currentDate.setText(simpleFormat.format(dateP));
            }
        });
        datePicker.show(getSupportFragmentManager(), datePicker.toString());

    }

    public void setTime(View v) {
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
            sec_alarm = dateP.getTime();

            currentTime.setText(simpleDateFormat.format(dateP));
        });
        timePicker.show(getSupportFragmentManager(), timePicker.toString());
    }

}