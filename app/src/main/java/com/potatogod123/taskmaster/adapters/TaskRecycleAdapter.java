package com.potatogod123.taskmaster.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.potatogod123.taskmaster.R;

import static com.potatogod123.taskmaster.MainActivity.allTask;

public class TaskRecycleAdapter extends RecyclerView.Adapter<TaskRecycleAdapter.TaskViewHolder> {

    public ClickOnTaskButtonAble clickOnTaskButtonAble;

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View fragment = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_task,parent,false);

        TaskViewHolder taskViewHolder = new TaskViewHolder(fragment);

        return taskViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        String title = allTask.get(position).getTitle();
        String description = allTask.get(position).getDescription();

        holder.title=title;
        holder.description=description;
        holder.index=position;
        ((TextView) holder.itemView.findViewById(R.id.recyclerTaskName)).setText(title);

        Button button = holder.itemView.findViewById(R.id.recyclerGoToTakButton);

        button.setOnClickListener(v -> {
            Log.i("TaskRecycleAdapter",holder.toString());
            clickOnTaskButtonAble.handleClickOnButton(holder);
        });


    }

    @Override
    public int getItemCount() {
        return allTask.size();
    }

    public TaskRecycleAdapter(ClickOnTaskButtonAble clickOnTaskButtonAble){
        this.clickOnTaskButtonAble = clickOnTaskButtonAble;
    }

    public interface ClickOnTaskButtonAble{
        public void handleClickOnButton(TaskViewHolder taskViewHolder);
    }

    public static class TaskViewHolder  extends RecyclerView.ViewHolder{
        String title;
        String description;
        int index;

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
    }
}
