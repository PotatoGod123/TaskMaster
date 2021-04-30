package com.potatogod123.taskmaster;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.potatogod123.taskmaster.daos.TaskModelDao;
import com.potatogod123.taskmaster.models.TaskModel;

@Database(entities = {TaskModel.class},version = 1)
public abstract class TaskDatabase extends RoomDatabase {
    public abstract TaskModelDao taskModelDao();
}
