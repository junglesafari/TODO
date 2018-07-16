package com.example.martin.todo;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class TodoViewHolder extends RecyclerView.ViewHolder{
    TextView title;
    TextView textView;
    TextView dateTextView;
    TextView timeTextView;
      View view;

    public TodoViewHolder(@NonNull View itemView) {
        super( itemView );
         title=itemView.findViewById( R.id.textView );
         textView=itemView.findViewById( R.id.checkbox );
         dateTextView=itemView.findViewById( R.id.date );
         timeTextView=itemView.findViewById( R.id.time );
        view=itemView;


    }
}
