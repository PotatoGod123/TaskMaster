package com.potatogod123.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

public class AddTask extends AppCompatActivity {

    static int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        if(counter>0){
            ((TextView) findViewById(R.id.textViewTotalTask)).setText(String.format(Locale.getDefault(),"Total Task: %d",counter++));
        }
        Button addTaskButton = findViewById(R.id.addTaskButton);

        addTaskButton.setOnClickListener(view->{
            counter++;
            ((TextView) findViewById(R.id.textViewTotalTask)).setText(String.format(Locale.getDefault(),"Total Task: %d",counter));
        });
    }
}