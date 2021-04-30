package com.potatogod123.taskmaster.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.potatogod123.taskmaster.models.TaskModel;

import java.util.List;

@Dao
public interface TaskModelDao {

    @Insert
    public void insert(TaskModel taskModel);

    @Query("SELECT * FROM TaskModel")
    public List<TaskModel> findAll();

    @Query("SELECT * FROM TaskModel ORDER BY id DESC")
    public List<TaskModel> findAllReversed();


}
