package com.example.martin.todo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class DialogJava extends AppCompatDialogFragment {
   EditText title;
   EditText text;
   Dialogjavalistener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder  builder=new AlertDialog.Builder( getActivity() );

        LayoutInflater inflater=getActivity().getLayoutInflater();
        View view=inflater.inflate( R.layout.dialoglayout,null );
        builder.setView( view );
        builder.setTitle("Take a new one" );

        title=view.findViewById( R.id.title );
        text=view.findViewById( R.id.datatoinsert );


        builder.setPositiveButton( "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

             String titlename=title.getText().toString();
             String textname=text.getText().toString();
            listener.applyText( titlename,textname );

            }
        } );
        builder.setNegativeButton( "CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        } );


        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
       try {
           listener=(Dialogjavalistener)context;
       }catch (ClassCastException e){
           throw new ClassCastException( context.toString()+"must impliment dialogjavalistener" );
       }


        super.onAttach( context );
    }

    public interface Dialogjavalistener{
        void applyText(String title,String text);
    }
}
