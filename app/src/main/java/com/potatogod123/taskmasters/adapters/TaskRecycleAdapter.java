package com.potatogod123.taskmasters.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.datastore.generated.model.TaskModelAmp;
import com.potatogod123.taskmasters.R;

import java.util.List;
import java.util.Random;


public class TaskRecycleAdapter extends RecyclerView.Adapter<TaskRecycleAdapter.TaskViewHolder> {

    Random r = new Random();
    public int fragmentChose;
    public ClickOnTaskButtonAble clickOnTaskButtonAble;
    public List<TaskModelAmp> allTask;
    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        int fragmentChosen;

        switch (fragmentChose){
            case 1: fragmentChosen=R.layout.fragment_all_task;
            break;
            default:
                fragmentChosen=R.layout.fragment_task;
        }

        View fragment = LayoutInflater.from(parent.getContext())
                .inflate(fragmentChosen,parent,false);


        TaskViewHolder taskViewHolder = new TaskViewHolder(fragment);

        return taskViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        String title = allTask.get(position).getTitle();
        String description = allTask.get(position).getDescription();
        holder.task=allTask.get(position);
        holder.title=title;
        holder.description=description;
        holder.index=position;
        ((TextView) holder.itemView.findViewById(R.id.recyclerTaskName)).setText(title);

        Button button = holder.itemView.findViewById(R.id.recyclerGoToTakButton);
        button.setBackgroundColor(randomColor());
        button.setOnClickListener(v -> {
//            Log.i("TaskRecycleAdapter",holder.toString());
            clickOnTaskButtonAble.handleClickOnButton(holder);
        });


    }

    @Override
    public int getItemCount() {
        return allTask.size();
    }

    public TaskRecycleAdapter(ClickOnTaskButtonAble clickOnTaskButtonAble, List<TaskModelAmp> allTask, int fragmentPicked){
        this.allTask=allTask;
        this.clickOnTaskButtonAble = clickOnTaskButtonAble;
        this.fragmentChose=fragmentPicked;
    }

    public interface ClickOnTaskButtonAble{
        public void handleClickOnButton(TaskViewHolder taskViewHolder);
    }

    public static class TaskViewHolder  extends RecyclerView.ViewHolder  {
        String title;
        String description;
        int index;
        TaskModelAmp task;
        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public TaskModelAmp getTask() {
            return task;
        }

        public void setTask(TaskModelAmp task) {
            this.task = task;
        }
    }

    public int randomColor() {
        int red = r.nextInt(256);
        int green = r.nextInt(256);
        int blue = r.nextInt(256);
        return Color.rgb(red, green, blue);
    }
}
