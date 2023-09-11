package com.example.schedule;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class UpdateActivity extends AppCompatActivity {

    EditText name_input;
    TextView date_input, time_input;
    Button update_button;
    Calendar selDate, selTime;
    MaterialDatePicker<Long> datePicker;
    MaterialTimePicker timePicker;

    // DATABASE INFO
    String id, name, date, time;
    int time_long;
    Date dateP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);


        name_input = findViewById(R.id.et_update_name);
        date_input = findViewById(R.id.tv_date_update);
        time_input = findViewById(R.id.tv_time_update);
        update_button = findViewById(R.id.btn_update);
        getAndSetIntentData();

        update_button.setOnClickListener((view) -> {
            DataBase myDB = new DataBase(UpdateActivity.this);
            name = name_input.getText().toString().trim();
            date = date_input.getText().toString().trim();
            time = time_input.getText().toString().trim();
            myDB.updateTask(id, name, date, time);
        });

    }

    void getAndSetIntentData() {
        if (getIntent().hasExtra("id") && getIntent().hasExtra("name") &&
                getIntent().hasExtra("date") && getIntent().hasExtra("time")) {

            id = getIntent().getStringExtra("id");
            name = getIntent().getStringExtra("name");
            date = getIntent().getStringExtra("date");
            time = getIntent().getStringExtra("time");

            name_input.setText(name);
            date_input.setText(date);
            time_input.setText(time);

        } else {
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        }
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

                date_input.setText(simpleFormat.format(dateP));
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
            dateP = new Date(selectedTime);

            time_input.setText(simpleDateFormat.format(dateP));
        });
        timePicker.show(getSupportFragmentManager(), timePicker.toString());
    }


    // установка обработчика выбора времени
//    TimePickerDialog.OnTimeSetListener t = new TimePickerDialog.OnTimeSetListener() {
//        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//            dateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
//            dateAndTime.set(Calendar.MINUTE, minute);
//            setInitialTime();
//        }
};

// установка обработчика выбора даты
//    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
//        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//            dateAndTime.set(Calendar.YEAR, year);
//            dateAndTime.set(Calendar.MONTH, monthOfYear);
//            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//            setInitialDate();
//        }
//    };
//}