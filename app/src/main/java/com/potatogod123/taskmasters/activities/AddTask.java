package com.potatogod123.taskmasters.activities;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.FileUtils;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
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

import static com.potatogod123.taskmasters.utilities.ConfigUtilities.configPlugins;


public class AddTask extends AppCompatActivity {
    String TAG = "potatogod123.AddTask";
    Handler handler;
    TaskDatabase taskDatabase;
    Team selectedTeam;
    File fileUpload;
    List<TaskModelAmp> awsTaskList=new ArrayList<>();
    Spinner coolSpinner;
    SharedPreferences pref;
    List<Team> thisAllTeams= new ArrayList<>();
//    private final int GET_IMAGE_CODE=32;
    ActivityResultLauncher<String[]> filePicker= registerForActivityResult(
            new ActivityResultContracts.OpenDocument(),
            result -> {
                if(result!=null) {
                    setUpPreviewImage(result);
                }

            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configPlugins(getApplication(),getApplicationContext());

        handler=new Handler(this.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);

                if(msg.what==1){
                    setUpInstanceVariable(getApplicationContext());
                }else if(msg.what==2){
                    ((TextView) findViewById(R.id.textViewTotalTask)).setText(String.format(Locale.getDefault(),"Total Task: %d",awsTaskList.size()));
                }
            }
        };

        awsTaskList = helperQuery();
        setContentView(R.layout.activity_add_task);
        pref = getApplicationContext().getSharedPreferences("userdetails",MODE_PRIVATE);

        Button selectImage = findViewById(R.id.selectImageButton);

        taskDatabase= Room.databaseBuilder(getApplicationContext(), TaskDatabase.class,"potatogod123_task")
                .allowMainThreadQueries()
                .build();



        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filePicker.launch(new String[]{"image/*"});
            }
        });


    }

    @Override
    protected  void onResume(){
        super.onResume();
//        List<TaskModelAmp> awsTaskList = helperQuery();

        configureDataFromFilter();
        setUpAddTaskButton();

    }


    public List<TaskModelAmp> helperQuery(){


        Amplify.API.query(
                ModelQuery.list(TaskModelAmp.class),
                r->{
                    for(TaskModelAmp task:r.getData()){
                        awsTaskList.add(task);
                    }
                    handler.sendEmptyMessage(2);
                },
                r->{}
        );
        Amplify.API.query(
                ModelQuery.list(Team.class),
                r->{
                    for(Team t: r.getData()){
                        thisAllTeams.add(t);
                    }
                    handler.sendEmptyMessage(1);
                },
                r->{}
        );

        return  awsTaskList;
    }


    public void setUpPreviewImage(Uri result){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            try {
                fileUpload = new File(getApplicationContext().getFilesDir(), "tempFile");
                InputStream inputStream = getContentResolver().openInputStream(result);
                FileUtils.copy(inputStream, new FileOutputStream(fileUpload));
                ImageView i = findViewById(R.id.imageViewSelectImagePreview);
                i.setImageBitmap(BitmapFactory.decodeFile(fileUpload.getPath()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            Toast.makeText(AddTask.this,"Your phone does not support this app!",Toast.LENGTH_LONG).show();
        }
    }

    public void setUpInstanceVariable(Context c){
        ArrayList<String> namesArr= new ArrayList<>();

        for(Team team: thisAllTeams){
            namesArr.add(team.getName());
        }

        coolSpinner = findViewById(R.id.addTaskSpinner);
        ArrayAdapter<String> adapt = new ArrayAdapter<>(c,R.layout.fragment_spinner_setting_fragment,R.id.textViewFragementSpinner,namesArr);
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

    }

    public void setUpAddTaskButton(){
        Button addTaskButton = findViewById(R.id.addTaskButton);
        addTaskButton.setOnClickListener(view->{
            Log.i(TAG, "You are in the button, but can't find current user?");
            if(Amplify.Auth.getCurrentUser()==null){

                Snackbar.make(getApplicationContext(),view,"You MUST best logged in to add a task!, Please Go to settings and sign up/login!",Snackbar.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(),"You MUST best logged in to add a task!, Please Go to settings and sign up/login!",Toast.LENGTH_LONG).show();
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
                Log.i(TAG, "Inside of file upload");
                Snackbar.make(this,view,"Please Select An Image",Snackbar.LENGTH_SHORT).show();
                return;
            }
            for(Team teams: thisAllTeams){
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
                    r-> fileUpload=null,
                    r->{}
            );



//            TaskModel newTask = new TaskModel(title,description);
//            taskDatabase.taskModelDao().insert(newTask);

//            List<TaskModel> allTask = taskDatabase.taskModelDao().findAll();

//            Intent intent = getIntent();
            int size = awsTaskList.size();
            size++;
            ((TextView) findViewById(R.id.textViewTotalTask)).setText(String.format(Locale.getDefault(),"Total Task: %d",size));

            ((EditText) findViewById(R.id.editTextTaskTitle)).setText("");
            ((EditText) findViewById(R.id.editTextDescription)).setText("");
            ((ImageView) findViewById(R.id.imageViewSelectImagePreview)).setImageBitmap(null);
            Snackbar.make(AddTask.this,view,"Your task, "+title+ ", has been made!",Snackbar.LENGTH_LONG).show();
//            taskRecycleAdapter.notifyItemChanged(size-1);


        });
    }
    public void configureDataFromFilter(){
        Intent intent = getIntent();
        String i = intent.getType();
        if(i!=null){
            if(intent.getType().equals("text/plain")){
                String t = intent.getStringExtra(Intent.EXTRA_TEXT);
                ((EditText) findViewById(R.id.editTextTaskTitle)).setText(t);
            }else if(intent.getType().startsWith("image/")){
                Uri uri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
                setUpPreviewImage(uri);
            }
        }
    }

}