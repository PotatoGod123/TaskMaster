package com.potatogod123.taskmasters;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.potatogod123.taskmasters.daos.TaskModelDao;
import com.potatogod123.taskmasters.models.TaskModel;

@Database(entities = {TaskModel.class},version = 1)
public abstract class TaskDatabase extends RoomDatabase {
    public abstract TaskModelDao taskModelDao();
}
