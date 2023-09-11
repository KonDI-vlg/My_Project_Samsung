package com.example.schedule;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;


class DataBase extends SQLiteOpenHelper {

    private Context context;
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "TasksDb.db";

    private static final String TABLE_NAME = "Tasks";
    private static final String KEY_ID = "_id";
    private static final String KEY_NAME = "name";
    private static final String KEY_DATE = "date";
    private static final String KEY_TIME = "time";
    private static final String KEY_TYPE = "type";
    private static final String KEY_TIME_TO = "time_to";
    private static final String KEY_ALL_TIME = "all_time";
    private static final String KEY_MONDAY = "monday";
    private static final String KEY_TUESDAY = "tuesday";
    private static final String KEY_WEDNESDAY = "wednesday";
    private static final String KEY_THURSDAY = "thursday";
    private static final String KEY_FRIDAY = "friday";
    private static final String KEY_SATURDAY = "saturday";


    //public static final String KEY_NOTIFICATION = "notification";

    DataBase(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME
                + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_NAME + " TEXT,"
                + KEY_DATE + " DATETIME,"
                + KEY_TIME + " DATETIME,"
                + KEY_TIME_TO + " DATETIME,"
                + KEY_TYPE + " TEXT,"
                + KEY_ALL_TIME + " TEXT,"
                + KEY_MONDAY + " TEXT,"
                + KEY_TUESDAY + " TEXT,"
                + KEY_WEDNESDAY + " TEXT,"
                + KEY_THURSDAY + " TEXT,"
                + KEY_FRIDAY + " TEXT,"
                + KEY_SATURDAY + " TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }


    void addEvent(String name, String date, String time, String all_time, String type) {
        ContentValues cv = new ContentValues();
        SQLiteDatabase db = this.getWritableDatabase();

        cv.put(KEY_NAME, name);
        cv.put(KEY_DATE, date);
        cv.put(KEY_TIME, time);
        cv.put(KEY_ALL_TIME, all_time);
        cv.put(KEY_TYPE, type);

        db.insert(TABLE_NAME, null, cv);

    }

    public void deleteTasks(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + "	= ?", new String[]{String.valueOf(id)});
    }

    Cursor readAllDataEvent() {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery("select _id, name, date, time from Tasks where type = 'event' ORDER by all_time", null);
        }
        return cursor;
    }

    Cursor readAllDataSchedule() {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.query("Tasks", new String[]{"_id, name, time, time_to"}, "type = 'schedule'", null, null, null, "time, time_to");
        }
        return cursor;
    }

    void deleteAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
    }

    String getUpcomingTask() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT name, date, time FROM Tasks GROUP BY all_time HAVING all_time = MIN(all_time) and type = 'event'", null);
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String date = cursor.getString(cursor.getColumnIndex("date"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            cursor.close();
            String upTask = name + "," + date + "," + time;
            return upTask;
        }
        return " , , ";
    }

    void updateTask(String rowId, String name, String date, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_ID, rowId);
        cv.put(KEY_NAME, name);
        cv.put(KEY_DATE, date);
        cv.put(KEY_TIME, time);

        long result = db.update(TABLE_NAME, cv, "_id=?", new String[]{rowId});
        if (result == -1) {
            Toast.makeText(context, "Хуита какая-то произошла", Toast.LENGTH_SHORT).show();
        } else Toast.makeText(context, "Успешно обновлено.", Toast.LENGTH_SHORT).show();
    }

    void addSchedule(String name, String timeFrom, String timeTo, String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(KEY_NAME, name);
        cv.put(KEY_TIME, timeFrom);
        cv.put(KEY_TIME_TO, timeTo);
        cv.put(KEY_TYPE, type);

        db.insert(TABLE_NAME, null, cv);
    }

    int checkToNull() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.rawQuery("SELECT EXISTS ( SELECT name,date,time FROM Tasks WHERE type = 'event')", null);
        return result.getInt(result.getColumnCount() - 1);
    }

    void moveToArchive(String name, String date, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.rawQuery("UPDATE Tasks set type = 'archive' where name = '" + name + "' and date = '" + date + "' and time = '" + time + "';", null);
    }

    String[] getSchoolLessons(String day) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> arrayList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT " + day + " FROM Tasks WHERE type = 'school'", null);
        String temp;
        cursor.moveToFirst();
        if (cursor.moveToNext()) {
            temp = cursor.getString(cursor.getColumnIndexOrThrow(day));
            arrayList.add(temp);

        }
        cursor.close();
        return   arrayList.toArray(new String[0]);
    }

    void setSchoolLessons(String type, String day, String lesson) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_TYPE, type);
        cv.put(day, lesson);
        db.insert(TABLE_NAME,null,cv);
    }

}
