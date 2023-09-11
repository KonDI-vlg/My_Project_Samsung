package com.example.schedule;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Fragment_school extends Fragment {

    RecyclerView recyclerView;
    String[] lesson_list;
    ArrayAdapter<String> arrayAdapter;
    Button btn_add;
    private static String EXTRA_STRING = "key";

//    public static Fragment_school newInstance(String[] message) {
//
//        Bundle args = new Bundle();
//        args.putStringArray(EXTRA_STRING, message);
//        Fragment_school fragment = new Fragment_school();
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.school_fragment, container, false);


        Bundle arguments = getArguments();
        if (arguments != null) {
            String[] lessons = arguments.getStringArray("main");
            displayValues(view, lessons);
        }
        return view;

    }

    private void displayValues(View v, String[] lessons) {
        if (lessons.length != 0) {
            ListView listView = v.findViewById(R.id.list_view_school);
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(v.getContext(), R.layout.list_view_layout, lessons);
            listView.setAdapter(arrayAdapter);
        }

    }

}