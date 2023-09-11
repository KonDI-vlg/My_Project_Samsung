package com.example.schedule;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.provider.ContactsContract;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerTitleStrip;


import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private String tabTitles[] = new String[]{"ПН", "ВТ", "СР", "ЧТ", "ПТ", "СБ"};
    private String day[] = new String[]{"monday", "tuesday", "wednesday", "thursday", "friday", "saturday"};


    public int getBtn_pos() {
        return btn_pos;
    }

    int btn_pos;
    Context context;


    ViewPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

//        Bundle arguments = new Bundle();
        DataBase myDB = new DataBase(context);
//        if (myDB.getSchoolLessons(day[position]).length != 0) {
//            arguments.putStringArray("main", myDB.getSchoolLessons(day[position]));
//            Fragment_school fragment_school = new Fragment_school();
//            fragment_school.setArguments(arguments);
//
//            return fragment_school;
//        } else {
//            return new Fragment_school();
//        }

        if (position == 0) {
            btn_pos = position;
            if (myDB.getSchoolLessons("monday").length != 0) {
                Bundle arguments = new Bundle();
                arguments.putStringArray("main", myDB.getSchoolLessons("monday"));
                Fragment_school fragment = new Fragment_school();
                fragment.setArguments(arguments);

                return fragment;
            } else {
                Bundle arguments = new Bundle();
                arguments.putStringArray("main", new String[]{"Математика", "Математика", "ОБЖ", "Физ-ра", "Английский", "Информатика", "Информатика"});
                Fragment_school fragment = new Fragment_school();
                fragment.setArguments(arguments);
                return fragment;
            }

        } else if (position == 1) {
            btn_pos = position;
            if (myDB.getSchoolLessons("tuesday").length != 0) {
                Bundle arguments = new Bundle();
                arguments.putStringArray("main", myDB.getSchoolLessons("tuesday"));
                Fragment_school fragment = new Fragment_school();
                fragment.setArguments(arguments);

                return fragment;
            } else {
                Bundle arguments = new Bundle();
                arguments.putStringArray("main", new String[]{"Физика", "Физика", "Английский", "Литература", "Литература", "Физ-ра"});
                Fragment_school fragment = new Fragment_school();
                fragment.setArguments(arguments);
                return fragment;
            }



        } else if (position == 2) {
            btn_pos = position;
            if (myDB.getSchoolLessons("wednesday").length != 0) {
                Bundle arguments = new Bundle();
                arguments.putStringArray("main", myDB.getSchoolLessons("wednesday"));
                Fragment_school fragment = new Fragment_school();
                fragment.setArguments(arguments);

                return fragment;

            } else {
                Bundle arguments = new Bundle();
                arguments.putStringArray("main", new String[]{"  ", "   ", "Индивидуальный проект", "Английский", "Русский язык", "Русский язык", "Физика", "Физика"});
                Fragment_school fragment = new Fragment_school();
                fragment.setArguments(arguments);
                return fragment;
            }


        } else if (position == 3) {
            btn_pos = position;
            if (myDB.getSchoolLessons("thursday").length != 0) {
                Bundle arguments = new Bundle();
                arguments.putStringArray("main", myDB.getSchoolLessons("thursday"));
                Fragment_school fragment = new Fragment_school();
                fragment.setArguments(arguments);

                return fragment;
            } else {
                Bundle arguments = new Bundle();
                arguments.putStringArray("main", new String[]{"Математика", "Математика", "История", "История"});
                Fragment_school fragment = new Fragment_school();
                fragment.setArguments(arguments);
                return fragment;            }


        } else if (position == 4) {
            btn_pos = position;
            if (myDB.getSchoolLessons("friday").length != 0) {
                Bundle arguments = new Bundle();
                arguments.putStringArray("main", myDB.getSchoolLessons("friday"));
                Fragment_school fragment = new Fragment_school();
                fragment.setArguments(arguments);

                return fragment;
            }else {
                Bundle arguments = new Bundle();
                arguments.putStringArray("main", new String[]{"Физика", "Математика", "Математика", "Русский язык", "Литература", "Информатика", "Информатика"});
                Fragment_school fragment = new Fragment_school();
                fragment.setArguments(arguments);
                return fragment;
            }

        } else if (position == 5) {
            btn_pos = position;
            if (myDB.getSchoolLessons("saturday").length != 0) {
                Bundle arguments = new Bundle();
                arguments.putStringArray("main", myDB.getSchoolLessons("saturday"));
                Fragment_school fragment = new Fragment_school();
                fragment.setArguments(arguments);

                return fragment;
            }else {
                Bundle arguments = new Bundle();
                arguments.putStringArray("main", new String[]{"   ", "   ", "Астрономия", "Физ-ра"});
                Fragment_school fragment = new Fragment_school();
                fragment.setArguments(arguments);

                return fragment;
            }

        }
        return null;
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }


}
