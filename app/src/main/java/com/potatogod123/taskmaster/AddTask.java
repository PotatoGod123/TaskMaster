package com.potatogod123.taskmaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.potatogod123.taskmaster.models.TaskModel;

import java.util.List;
import java.util.Locale;

import static com.potatogod123.taskmaster.MainActivity.taskRecycleAdapter;

public class AddTask extends AppCompatActivity {
    TaskDatabase taskDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
//        if(counter>0){
//
//        }
        Button addTaskButton = findViewById(R.id.addTaskButton);

        taskDatabase= Room.databaseBuilder(getApplicationContext(), TaskDatabase.class,"potatogod123_task")
                .allowMainThreadQueries()
                .build();

        addTaskButton.setOnClickListener(view->{

            String title = ((EditText) findViewById(R.id.editTextTaskTitle)).getText().toString();
            String description = ((EditText) findViewById(R.id.editTextDescription)).getText().toString();

            TaskModel newTask = new TaskModel(title,description);
            taskDatabase.taskModelDao().insert(newTask);

            List<TaskModel> allTask = taskDatabase.taskModelDao().findAll();

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
        ((TextView) findViewById(R.id.textViewTotalTask)).setText(String.format(Locale.getDefault(),"Total Task: %d",taskDatabase.taskModelDao().findAll().size()));
    }
}