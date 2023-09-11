package com.example.schedule;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.ContactsContract;

import androidx.core.app.NotificationCompat;

public class MyAlarmBroadcastManager extends BroadcastReceiver {
    // Идентификатор уведомления
    private int NOTIFY_ID = 101;
    int reqCode = 0;

    // Идентификатор канала
    private static String CHANNEL_ID = "322";
    final public static String ONE_TIME = "onetime";

    String name_alarm;

    public void setName_alarm(String name_alarm) {
        this.name_alarm = name_alarm;
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "YOUR TAG");
//Осуществляем блокировку
        wl.acquire();

//Здесь можно делать обработку.
        Bundle extras = intent.getExtras();
        StringBuilder msgStr = new StringBuilder();

        if (extras != null & extras.getBoolean(ONE_TIME, Boolean.FALSE)) {
//проверяем параметр ONE_TIME, если это одиночный будильник,
//выводим соответствующее сообщение.
            msgStr.append("Одноразовый будильник: ");
        }
        @SuppressLint("SimpleDateFormat") Format formatter = new SimpleDateFormat("HH:mm:ss");
        msgStr.append(formatter.format(new Date()));

//        Toast.makeText(context, msgStr, Toast.LENGTH_LONG).show();


        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context,
                reqCode, notificationIntent,
                0);
        reqCode++;
        if (intent.getIntExtra("type", 0) == 1) {
            NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(context, CHANNEL_ID)
                            .setSmallIcon(R.drawable.ic_launcher_foreground)
                            .setContentTitle(intent.getStringExtra("name"))
                            .setContentText("Прямо сейчас")
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .setContentIntent(contentIntent)
                            .setWhen(System.currentTimeMillis())
                            .setAutoCancel(true);

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            createChannelIfNeeded(notificationManager);
            notificationManager.notify(NOTIFY_ID, builder.build());


        } else {
            NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(context, CHANNEL_ID)
                            .setSmallIcon(R.drawable.ic_launcher_foreground)
                            .setContentTitle("Напоминание")
                            .setContentText(intent.getStringExtra("name") + " произойдет через" + intent.getStringExtra("notif_text"))
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .setContentIntent(contentIntent)
                            .setWhen(System.currentTimeMillis())
                            .setAutoCancel(true);

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            createChannelIfNeeded(notificationManager);
            notificationManager.notify(NOTIFY_ID, builder.build());


        }

        wl.release();

       if (intent.getIntExtra("type", 0) == 1) {
            DataBase myDB = new DataBase(context.getApplicationContext());
            myDB.moveToArchive(intent.getStringExtra("name"),
                    intent.getStringExtra("date_db"),
                    intent.getStringExtra("time_db"));
        }
    }

    public void setAlarm(Context context, String name, Long time_alarm, String date_db, String time_db) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, MyAlarmBroadcastManager.class);
        intent.putExtra("name", name);
        intent.putExtra("type", 1);
        intent.putExtra("date_db", date_db);
        intent.putExtra("time_db", time_db);
        PendingIntent pi = PendingIntent.getBroadcast(context, reqCode, intent, 0);
        reqCode++;
        am.set(AlarmManager.RTC_WAKEUP, (time_alarm - 3 * 60 * 60 * 1000), pi);
    }

    public void setReminder(Context context, String name, Long time_alarm, Long time_reminder) {
        if (time_reminder != 0L) {
            AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, MyAlarmBroadcastManager.class);
            intent.putExtra("name", name);
            intent.putExtra("type", 0);
            if (time_reminder.compareTo(600000L) == 0) {
                intent.putExtra("notif_text", " 10 минут");
            } else if (time_reminder.compareTo(1800000L) == 0) {
                intent.putExtra("notif_text", " 30 минут");
            } else if (time_reminder.compareTo(3600000L) == 0) {
                intent.putExtra("notif_text", " час");
            } else if (time_reminder.compareTo(21600000L) == 0) {
                intent.putExtra("notif_text", " 6 часов");
            } else if (time_reminder.compareTo(43200000L) == 0) {
                intent.putExtra("notif_text", " 12 часов");
            } else if (time_reminder.compareTo(86400000L) == 0) {
                intent.putExtra("notif_text", " день");
            } else if (time_reminder.compareTo(604800000L) == 0) {
                intent.putExtra("notif_text", " неделю");
            }
            PendingIntent pi = PendingIntent.getBroadcast(context, reqCode, intent, 0);
            reqCode++;
            am.set(AlarmManager.RTC_WAKEUP, (time_alarm - 3 * 60 * 60 * 1000) - time_reminder, pi);
        }
    }

    public void cancelAlarm(Context context) {
        Intent intent = new Intent(context, MyAlarmBroadcastManager.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);//Отменяем будильник, связанный с интентом данного класса
    }

    public void setOnetimeTimer(Context context) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, MyAlarmBroadcastManager.class);
        intent.putExtra(ONE_TIME, Boolean.TRUE);//Задаем параметр интента
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pi);
    }

    public static void createChannelIfNeeded(NotificationManager manager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_ID, NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(notificationChannel);
        }
    }
}