package com.potatogod123.taskmaster.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.potatogod123.taskmaster.R;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewPracticeAdapter extends RecyclerView.Adapter<RecyclerViewPracticeAdapter.SkateBoardViewHolder> {

    static List<String> stuff;

    static {
        stuff= new ArrayList<>();
        stuff.add("Palm Tree");
        stuff.add("Hi");
        stuff.add("No");
        stuff.add("Epic");
        stuff.add("Yes");
    }

public ClickOnSkateBoardAble clickOnSkateBoardAble;

    @NonNull
    @Override
    public SkateBoardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //4. choose the fragment and inflate it

        View fragment = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_skate_board, parent, false);

        //5. define a view holder
        //6. attach this fragment to a ViewHolder which will manage it'ss data
        SkateBoardViewHolder skateBoardViewHolder = new SkateBoardViewHolder(fragment);

        return skateBoardViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SkateBoardViewHolder holder, int position) {
        //to do later, change the data when fragment cycle


        String skateBoardDesign =  stuff.get(position);
        holder.design= skateBoardDesign;// this line passes the data, essentially

        ((TextView)holder.itemView.findViewById(R.id.textViewSkateBoardDesign)).setText(skateBoardDesign);

        holder.itemView.setOnClickListener(v->{
            Log.i("CLicked on the fragment", holder.toString() );
            clickOnSkateBoardAble.handleClickOnSkateBoard(holder);

        });

    }

    @Override
    public int getItemCount() {
        return stuff.size();
    }

    public static class SkateBoardViewHolder extends RecyclerView.ViewHolder {
        public String design;
        //later. store some data
        public SkateBoardViewHolder(@NonNull View itemView) {
            super(itemView);

        }
    }


    public RecyclerViewPracticeAdapter(ClickOnSkateBoardAble clickOnSkateBoardAble){
        this.clickOnSkateBoardAble=clickOnSkateBoardAble;
    }

    // this gets implement wherever else uses this
    public interface ClickOnSkateBoardAble{

        public void handleClickOnSkateBoard(SkateBoardViewHolder skateBoardViewHolder);
    }
}
