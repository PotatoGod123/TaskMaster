package com.potatogod123.taskmaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.TaskModelAmp;
import com.potatogod123.taskmaster.models.TaskModel;

import java.util.ArrayList;
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

            TaskModelAmp newAmpTask = TaskModelAmp.builder()
                    .title(title)
                    .description(description)
                    .build();

            Amplify.API.mutate(
                    ModelMutation.create(newAmpTask),
                    response-> Log.i("addTask.potatogod","new task has been made and added to database"),
                    r -> Log.i("addTask.potatogod","Failed to add new task "+r.toString())
            );



            TaskModel newTask = new TaskModel(title,description);
//            taskDatabase.taskModelDao().insert(newTask);

//            List<TaskModel> allTask = taskDatabase.taskModelDao().findAll();
            List<TaskModelAmp> awsTaskList = helperQuery();
            Intent intent = getIntent();
            int size = intent.getIntExtra("size",0);
            size++;
            ((TextView) findViewById(R.id.textViewTotalTask)).setText(String.format(Locale.getDefault(),"Total Task: %d",size));

            Toast.makeText(this,"Your task, "+title+ ", has been made!",Toast.LENGTH_LONG).show();
            ((EditText) findViewById(R.id.editTextTaskTitle)).setText("");
            ((EditText) findViewById(R.id.editTextDescription)).setText("");
            taskRecycleAdapter.notifyItemChanged(size-1);

        });
    }

    @Override
    protected  void onResume(){
        super.onResume();
        List<TaskModelAmp> awsTaskList = helperQuery();
        Intent intent = getIntent();
        int size = intent.getIntExtra("size",0);


        ((TextView) findViewById(R.id.textViewTotalTask)).setText(String.format(Locale.getDefault(),"Total Task: %d",size));

    }


    public List<TaskModelAmp> helperQuery(){
        List<TaskModelAmp> awsTaskList = new ArrayList<>();
        List<Boolean> flagArr = new ArrayList<>();

        Amplify.API.query(
                ModelQuery.list(TaskModelAmp.class),
                r->{
                    for(TaskModelAmp task:r.getData()){
                        awsTaskList.add(task);
                    }

                },
                r->{Log.i("addTask.potatogod123","failed to retrieve db"); flagArr.add(true);}
        );


        return  awsTaskList;
    }
}