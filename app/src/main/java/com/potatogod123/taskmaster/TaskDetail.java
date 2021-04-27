package com.potatogod123.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class TaskDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
    }


    @Override
    protected  void onResume(){
        super.onResume();
        Intent intent = getIntent();
        if(intent.getStringExtra("taskTitle")!=null){
            ((TextView) findViewById(R.id.textViewTaskDetailPageTitle)).setText(intent.getStringExtra("taskTitle"));
        }

    }
}