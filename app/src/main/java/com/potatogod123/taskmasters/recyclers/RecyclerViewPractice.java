package com.potatogod123.taskmasters.recyclers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;
import com.potatogod123.taskmasters.R;
import com.potatogod123.taskmasters.adapters.RecyclerViewPracticeAdapter;

public class RecyclerViewPractice extends AppCompatActivity implements RecyclerViewPracticeAdapter.ClickOnSkateBoardAble {
    RecyclerViewPracticeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view_practice);

        // 1. reference the recycler view and set the layout manager
        RecyclerView recyclerView = findViewById(R.id.skateBoardDesignRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // create and set an recyler view adapter
        adapter= new RecyclerViewPracticeAdapter(this);
        recyclerView.setAdapter(adapter);

        //design the recycler fragment

    }

    @Override
    public void handleClickOnSkateBoard(RecyclerViewPracticeAdapter.SkateBoardViewHolder skateBoardViewHolder) {
        Snackbar.make(skateBoardViewHolder.itemView,skateBoardViewHolder.design,Snackbar.LENGTH_SHORT).show();
    }
}