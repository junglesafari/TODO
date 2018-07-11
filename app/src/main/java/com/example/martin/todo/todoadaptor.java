package com.example.martin.todo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class todoadaptor extends ArrayAdapter {
ArrayList<todo> item;
LayoutInflater inflater;
    public todoadaptor(@NonNull Context context, ArrayList<todo> items) {
        super( context, 0,items );
        inflater=(LayoutInflater)context.getSystemService( context.LAYOUT_INFLATER_SERVICE );
        this.item=items;
    }

    @Override
    public int getCount() {
        return item.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
       //optimise first by convertView
       View  output=convertView;
         if(output==null){

             output=inflater.inflate( R.layout.todolayout,parent,false );
             TextView titleTextView=output.findViewById( R.id.textView );
             TextView textTextView=output.findViewById( R.id.checkbox );
             TextView dateTextView=output.findViewById( R.id.date );
             TextView timeTextView=output.findViewById( R.id.time );
            TodoViewHolder viewHolder=new TodoViewHolder();
            viewHolder.title=titleTextView;
            viewHolder.textView=textTextView;
            viewHolder.dateTextView=dateTextView;
            viewHolder.timeTextView=timeTextView;
             output.setTag( viewHolder );
         }
         TodoViewHolder viewHolder=(TodoViewHolder)output.getTag();

        todo td=item.get(position);
        viewHolder.title.setText( td.getTodotitle());
        viewHolder.textView.setText( td.getTodotext() );
        viewHolder.dateTextView.setText( td.getTododate() );
        viewHolder.timeTextView.setText( td.getTodotime() );
         return output;
    }
}

