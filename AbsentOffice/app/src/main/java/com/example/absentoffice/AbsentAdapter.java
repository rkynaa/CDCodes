package com.example.absentoffice;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class AbsentAdapter extends RecyclerView.Adapter<AbsentAdapter.AbsentViewHolder>{
    Context context;
    ArrayList<SubmitAbsent> absents;

    public AbsentAdapter(Context c, ArrayList<SubmitAbsent> sa){
        context = c;
        absents = sa;
    };

    @NonNull
    @Override
    public AbsentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new AbsentViewHolder(LayoutInflater.from(context).inflate(R.layout.rv_row, viewGroup, false));

    }

    @Override
    public void onBindViewHolder(@NonNull AbsentViewHolder absentViewHolder, int i) {
        absentViewHolder.name.setText(absents.get(i).getName());
        absentViewHolder.date.setText(absents.get(i).getDate());
    }

    @Override
    public int getItemCount() {
        return absents.size();
    }

    class AbsentViewHolder extends RecyclerView.ViewHolder{
        TextView name,date;
        public AbsentViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.tvName);
            date = (TextView) itemView.findViewById(R.id.tvDate);
        }
    }
}
