package com.example.martin.todo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class todorecycleradaptor extends RecyclerView.Adapter<TodoViewHolder>  {
    ArrayList<todo> item;
    Context context;
    todoclicklistener listener;
    LayoutInflater inflater;
    public todorecycleradaptor(ArrayList<todo> item, Context context, todoclicklistener listener) {
        this.item = item;
        this.context = context;
        this.listener = listener;
    }



    @NonNull
    @Override
    public TodoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        inflater=(LayoutInflater) context.getSystemService( context.LAYOUT_INFLATER_SERVICE );
        View rowlayout=inflater.inflate( R.layout.todolayout,viewGroup,false );
        return new TodoViewHolder(rowlayout);
    }

    @Override
    public void onBindViewHolder(@NonNull final TodoViewHolder todoViewHolder, int i) {
       todo todo=item.get( i );
       todoViewHolder.title.setText( todo.getTodotitle() );
       todoViewHolder.textView.setText( todo.getTodotext() );
       todoViewHolder.dateTextView.setText( todo.getTododate() );
       todoViewHolder.timeTextView.setText( todo.getTodotime() );
        todoViewHolder.view.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.ontodoclick( view,todoViewHolder.getAdapterPosition() );
            }
        } );

    }

    @Override
    public int getItemCount() {
        return item.size();
    }
}
