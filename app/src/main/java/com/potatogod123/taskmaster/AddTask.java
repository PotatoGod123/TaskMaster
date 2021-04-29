package com.potatogod123.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.potatogod123.taskmaster.models.TaskModel;

import java.util.Locale;

import static com.potatogod123.taskmaster.MainActivity.allTask;
import static com.potatogod123.taskmaster.MainActivity.taskRecycleAdapter;

public class AddTask extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
//        if(counter>0){
//
//        }
        Button addTaskButton = findViewById(R.id.addTaskButton);

        addTaskButton.setOnClickListener(view->{

            String title = ((EditText) findViewById(R.id.editTextTaskTitle)).getText().toString();
            String description = ((EditText) findViewById(R.id.editTextDescription)).getText().toString();

            allTask.add(new TaskModel(title,description));
            ((TextView) findViewById(R.id.textViewTotalTask)).setText(String.format(Locale.getDefault(),"Total Task: %d",allTask.size()));

            Toast.makeText(this,"Your task, "+title+ ", has been made!",Toast.LENGTH_LONG).show();
            ((EditText) findViewById(R.id.editTextTaskTitle)).setText("");
            ((EditText) findViewById(R.id.editTextDescription)).setText("");
            taskRecycleAdapter.notifyItemChanged(allTask.size()-1);

        });
    }

    @Override
    protected  void onResume(){
        super.onResume();
        ((TextView) findViewById(R.id.textViewTotalTask)).setText(String.format(Locale.getDefault(),"Total Task: %d",allTask.size()));
    }
}