package com.example.schedule;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.checkbox.MaterialCheckBox;

import java.util.ArrayList;


public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.MyViewHolder> {
    Activity activity;
    private final Context context;
    private final ArrayList id, name, date, time, time_to;

    public String getType() {
        return type;
    }

    String type;


    MainViewModel mainViewModel;

    boolean isEnable = false;
    boolean isSelectAll = false;
    ArrayList<String> selectList = new ArrayList<>();

    TasksAdapter(Activity activity,
                 Context context,
                 ArrayList id,
                 ArrayList name,
                 ArrayList date,
                 ArrayList time,
                 ArrayList time_to,
                 String type) {

        this.activity = activity;
        this.context = context;
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.time_to = time_to;
        this.type = type;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if (type.equals("event")) {
            View view = inflater.inflate(R.layout.single_item, parent, false);
            mainViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(MainViewModel.class);
            return new MyViewHolder(view);
        } else if (type.equals("schedule")) {
            View view = inflater.inflate(R.layout.single_schedule_event, parent, false);
            mainViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(MainViewModel.class);
            return new MyViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.single_schedule_event, parent, false);
            mainViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(MainViewModel.class);
            return new MyViewHolder(view);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        if (type.equals("event")) {
            holder.rowName.setText(String.valueOf(name.get(position)));
            holder.rowDate.setText(String.valueOf(date.get(position)));
            holder.rowTime.setText(String.valueOf(time.get(position)));
        } else if (type.equals("schedule")) {
            holder.rowNameSch.setText(String.valueOf((name.get(position))));
            holder.rowTimeSch.setText(String.valueOf((time.get(position))));
            holder.rowTimeToSch.setText(String.valueOf((time_to.get(position))));
        }


        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (!isEnable) {
                    ActionMode.Callback callback = new ActionMode.Callback() {
                        @Override
                        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                            MenuInflater menuInflater = mode.getMenuInflater();
                            menuInflater.inflate(R.menu.selection_menu, menu);
                            return true;
                        }

                        @Override
                        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                            isEnable = true;

                            ClickItem(holder);
                            mainViewModel.getText().observe((LifecycleOwner) activity, new Observer<String>() {
                                @Override
                                public void onChanged(String s) {
                                    mode.setTitle(String.format("%s Selected", s));
                                }
                            });

                            return true;
                        }

                        @Override
                        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                            int id = item.getItemId();

                            switch (id) {
                                case R.id.menu_delete:
                                    for (String s : selectList) {
                                        int indexToDelete = name.indexOf(s);
                                        DataBase db = new DataBase(activity.getApplicationContext());
                                        db.deleteTasks((Integer) TasksAdapter.this.id.get(indexToDelete));
                                        TasksAdapter.this.id.remove(indexToDelete);
                                        if (type.equals("event")) {
                                            name.remove(indexToDelete);
                                            date.remove(indexToDelete);
                                            time.remove(indexToDelete);
                                        } else if (type.equals("schedule")) {
                                            name.remove(indexToDelete);
                                            time.remove(indexToDelete);
                                            time_to.remove(indexToDelete);
                                        }
                                    }
                                    if (name.size() == 0) {
                                        Toast.makeText(context, "Список пуст", Toast.LENGTH_SHORT).show();
                                    }
                                    mode.finish();
                                    break;
                                case R.id.menu_select_all:
                                    if (selectList.size() == name.size()) {
                                        isSelectAll = false;
                                        selectList.clear();

                                    } else {
                                        isSelectAll = true;
                                        selectList.clear();
                                        selectList.addAll(name);
                                    }
                                    mainViewModel.setText(String.valueOf(selectList.size()));
                                    notifyDataSetChanged();
                                    break;
                            }
                            return true;
                        }

                        @Override
                        public void onDestroyActionMode(ActionMode mode) {
                            isEnable = false;
                            isSelectAll = false;
                            selectList.clear();
                            notifyDataSetChanged();

                        }
                    };
                    ((AppCompatActivity) v.getContext()).startActionMode(callback);
                } else {
                    ClickItem(holder);

                }
                return true;
            }
        });

        holder.itemView.setOnClickListener((view) -> {
            if (isEnable) {
                ClickItem(holder);

            } else if (type.equals("event")) {
                Intent intent = new Intent(context, UpdateActivity.class);
                intent.putExtra("id", String.valueOf(id.get(position)));
                intent.putExtra("name", String.valueOf(name.get(position)));
                intent.putExtra("date", String.valueOf(date.get(position)));
                intent.putExtra("time", String.valueOf(time.get(position)));
                context.startActivity(intent);

            }
        });

        if (isSelectAll) {
            holder.ivCheckBox.setVisibility(View.VISIBLE);
            holder.itemView.setBackgroundColor(Color.LTGRAY);
        } else {
            holder.ivCheckBox.setVisibility(View.GONE);
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
        }

    }

    private void ClickItem(MyViewHolder holder) {

        String s = (String) name.get(holder.getAdapterPosition());

        if (holder.ivCheckBox.getVisibility() == View.GONE) {
            holder.ivCheckBox.setChecked(true);
            holder.ivCheckBox.setVisibility(View.VISIBLE);
            holder.itemView.setBackgroundColor(Color.LTGRAY);
            selectList.add(s);

        } else {
            holder.ivCheckBox.setVisibility(View.GONE);
            holder.ivCheckBox.setChecked(false);
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
            selectList.remove(s);

        }
        mainViewModel.setText(String.valueOf(selectList.size()));
    }

    @Override
    public int getItemCount() {
        return id.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView rowName, rowDate, rowTime;                     // TV для event
        TextView rowNameSch, rowTimeSch, rowTimeToSch;          // TV для schedule
        MaterialCheckBox ivCheckBox;
        LinearLayout layout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCheckBox = itemView.findViewById(R.id.checkbox_main);
            rowName = itemView.findViewById(R.id.event_name);
            rowDate = itemView.findViewById(R.id.event_date);
            rowTime = itemView.findViewById(R.id.event_time);

            rowNameSch = itemView.findViewById(R.id.schedule_name);
            rowTimeSch = itemView.findViewById(R.id.schedule_time);
            rowTimeToSch = itemView.findViewById(R.id.schedule_time_to);

            layout = itemView.findViewById(R.id.eventItemLayout);
        }
    }


}
