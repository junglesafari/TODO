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

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View output=inflater.inflate( R.layout.todolayout,parent,false );
        TextView titleTextView=output.findViewById( R.id.textView );
        TextView textTextView=output.findViewById( R.id.checkbox );

        todo td=item.get(position);
        titleTextView.setText( td.getTodotitle());
        textTextView.setText( td.getTodotext() );





         return output;
    }
}

