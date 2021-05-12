package com.potatogod123.taskmasters.activities;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.FileUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.TaskModelAmp;
import com.amplifyframework.datastore.generated.model.Team;
import com.google.android.material.snackbar.Snackbar;
import com.potatogod123.taskmasters.R;
import com.potatogod123.taskmasters.TaskDatabase;
import com.potatogod123.taskmasters.models.TaskModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.potatogod123.taskmasters.activities.MainActivity.allTeams;


public class AddTask extends AppCompatActivity {
    TaskDatabase taskDatabase;
    Team selectedTeam;
    File fileUpload;
    private final int GET_IMAGE_CODE=32;
    ActivityResultLauncher<String[]> filePicker= registerForActivityResult(
            new ActivityResultContracts.OpenDocument(),
            new ActivityResultCallback<Uri>() {
                @RequiresApi(api = Build.VERSION_CODES.Q)
                @Override
                public void onActivityResult(Uri result) {
                    if(result!=null) {
                        fileUpload = new File(getApplicationContext().getFilesDir(), "tempFile");
                        try {
                            InputStream inputStream = getContentResolver().openInputStream(result);

                            FileUtils.copy(inputStream, new FileOutputStream(fileUpload));
                            ImageView i = findViewById(R.id.imageViewSelectImagePreview);
                            i.setImageBitmap(BitmapFactory.decodeFile(fileUpload.getPath()));
                            Toast.makeText(AddTask.this,"Your task, has been made!",Toast.LENGTH_LONG).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("userdetails",MODE_PRIVATE);
//        if(counter>0){
//
//        }
        Button addTaskButton = findViewById(R.id.addTaskButton);
        Button selectImage = findViewById(R.id.selectImageButton);

        taskDatabase= Room.databaseBuilder(getApplicationContext(), TaskDatabase.class,"potatogod123_task")
                .allowMainThreadQueries()
                .build();

        ArrayList<String> namesArr= new ArrayList<>();

        for(Team team: allTeams){
            namesArr.add(team.getName());
        }

        Spinner coolSpinner = findViewById(R.id.addTaskSpinner);
        ArrayAdapter<String> adapt = new ArrayAdapter<>(this,R.layout.fragment_spinner_setting_fragment,R.id.textViewFragementSpinner,namesArr);
        coolSpinner.setAdapter(adapt);
        int count=0;
        String teamStr= pref.getString("team",null);
        if(teamStr!=null){
            for(String names: namesArr){
                if(teamStr.contains(names)){
                    break;
                }
                count++;
            }
            coolSpinner.setSelection(count);
        }

        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filePicker.launch(new String[]{"image/*"});
            }
        });

        addTaskButton.setOnClickListener(view->{
            if(Amplify.Auth.getCurrentUser()==null){
                Snackbar.make(this,view,"You MUST best logged in to add a task!, Please Go to settings and sign up/login!",Snackbar.LENGTH_LONG).show();
                Toast.makeText(this,"You MUST best logged in to add a task!, Please Go to settings and sign up/login!",Toast.LENGTH_LONG).show();
                return;
            }


            String title = ((EditText) findViewById(R.id.editTextTaskTitle)).getText().toString();
            String description = ((EditText) findViewById(R.id.editTextDescription)).getText().toString();
            String teamChoice = ((TextView) findViewById(R.id.textViewFragementSpinner)).getText().toString();

            if(title.length()==0){
                ((EditText) findViewById(R.id.editTextTaskTitle)).setError("No Empty Title");
                return;
            }else if(description.length()==0){
                ((EditText) findViewById(R.id.editTextDescription)).setError("No empty Description");
                return;
            }
            if(fileUpload==null) {
                Snackbar.make(this,view,"Please Select An Image",Snackbar.LENGTH_SHORT).show();
                return;
            }

            for(Team teams: allTeams){
                if(teamChoice.contains(teams.getName())){
                    selectedTeam=teams;
                }
            }
            if(selectedTeam==null)return;

            TaskModelAmp newAmpTask= TaskModelAmp.builder()
                    .teamId(selectedTeam.getId())
                    .title(title)
                    .description(description)
                    .build();

            String sB = "taskPicture" +
                    newAmpTask.getId();
            newAmpTask.setS3StorageId(sB);
            Amplify.API.mutate(
                    ModelMutation.create(newAmpTask),
                    response-> {},
                    r -> {}
            );
            Amplify.Storage.uploadFile(
                    sB,
                    fileUpload,
                    r->{fileUpload=null;},
                    r->{}
            );



            TaskModel newTask = new TaskModel(title,description);
//            taskDatabase.taskModelDao().insert(newTask);

//            List<TaskModel> allTask = taskDatabase.taskModelDao().findAll();
            List<TaskModelAmp> awsTaskList = helperQuery();
            Intent intent = getIntent();
            int size = intent.getIntExtra("size",0);
            size++;
            ((TextView) findViewById(R.id.textViewTotalTask)).setText(String.format(Locale.getDefault(),"Total Task: %d",size));

            ((EditText) findViewById(R.id.editTextTaskTitle)).setText("");
            ((EditText) findViewById(R.id.editTextDescription)).setText("");
            ((ImageView) findViewById(R.id.imageViewSelectImagePreview)).setImageBitmap(null);
            Snackbar.make(AddTask.this,view,"Your task, "+title+ ", has been made!",Snackbar.LENGTH_LONG).show();
//            taskRecycleAdapter.notifyItemChanged(size-1);


        });
    }

    @Override
    protected  void onResume(){
        super.onResume();
//        List<TaskModelAmp> awsTaskList = helperQuery();
        Intent intent = getIntent();
        int size = intent.getIntExtra("size",0);


        ((TextView) findViewById(R.id.textViewTotalTask)).setText(String.format(Locale.getDefault(),"Total Task: %d",size));

    }


    public List<TaskModelAmp> helperQuery(){
        List<TaskModelAmp> awsTaskList = new ArrayList<>();

        Amplify.API.query(
                ModelQuery.list(TaskModelAmp.class),
                r->{
                    for(TaskModelAmp task:r.getData()){
                        awsTaskList.add(task);
                    }

                },
                r->{}
        );


        return  awsTaskList;
    }



}