package com.potatogod123.taskmaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.potatogod123.taskmaster.adapters.TaskRecycleAdapter;
import com.potatogod123.taskmaster.models.TaskModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TaskRecycleAdapter.ClickOnTaskButtonAble {
    static TaskRecycleAdapter taskRecycleAdapter;

    public static List<TaskModel> allTask = new ArrayList<>();

    static{
        allTask.add(new TaskModel("Buy a dog","Go and buy a dog, steal one whatever"));
        allTask.add(new TaskModel("Sell a dog","Go and sell a dog, give it for free whatever"));
        allTask.add(new TaskModel("Use the Restroom","quick! it's been a long day :("));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button goToAddFormButton = findViewById(R.id.goToAddFormButton);
        Button goToAllTaskButton = findViewById(R.id.goToAllTaskButton);
        Button goSettingsButton = findViewById(R.id.goToSettingsButton);

        RecyclerView recyclerView = findViewById(R.id.taskRecyclerViewMain);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskRecycleAdapter= new TaskRecycleAdapter(this);

        recyclerView.setAdapter(taskRecycleAdapter);

        Button recyclerButton = findViewById(R.id.recycleButton);

        recyclerButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RecyclerViewPractice.class);
            startActivity(intent);
        });



        goToAddFormButton.setOnClickListener(view->{

            Intent  intent = new Intent(MainActivity.this,AddTask.class);
            startActivity(intent);
        });

        goToAllTaskButton.setOnClickListener(view->{
            Intent intent = new Intent(MainActivity.this, AllTask.class);
            startActivity(intent);
        });

        goSettingsButton.setOnClickListener(view->{
            Intent intent = new Intent(MainActivity.this, Settings.class);
            startActivity(intent);
        });

    }

    @Override
    protected  void onResume(){
        super.onResume();
        SharedPreferences pref = getSharedPreferences("userdetails",MODE_PRIVATE);

        String username = pref.getString("username",null);

        if(username!=null){
            ((TextView) findViewById(R.id.textViewMainMyTaskTitle)).setText(String.format(Locale.getDefault(),"Welcome %s, These are your task:",username));
        }
    }


    protected void buttonHelper(TaskRecycleAdapter.TaskViewHolder holder){
        Intent intent = new Intent(MainActivity.this,TaskDetail.class);

        intent.putExtra("taskTitle",holder.getTitle());
        intent.putExtra("description", holder.getDescription());
        startActivity(intent);
    }

    @Override
    public void handleClickOnButton(TaskRecycleAdapter.TaskViewHolder taskViewHolder) {
        buttonHelper(taskViewHolder);
    }
}