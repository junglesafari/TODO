package com.example.martin.todo;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener,DialogJava.Dialogjavalistener,AdapterView.OnItemLongClickListener{

    ListView listView;
ArrayList<todo> arraytodo;
    todoadaptor adaptor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        listView=(ListView)findViewById( R.id.list);
        arraytodo=new ArrayList<>(  );
        listView.setAdapter( adaptor );
        listView.setOnItemClickListener( this );
        listView.setOnItemLongClickListener(this);


    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        todo e=arraytodo.get( i );
        int position=i;

}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.menu, menu );
        return super.onCreateOptionsMenu( menu );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        openDialog();

        return super.onOptionsItemSelected( item );
    }

    private void openDialog() {
      DialogJava dialogJava=new DialogJava();
      dialogJava.show( getSupportFragmentManager(),"Dialogjava" );

    }

    @Override
    public void applyText(String title, String text) {
        todo to=new todo( "title","text" );
        to.setTodotext( text );
        to.setTodotitle( title );
        arraytodo.add( to );
        adaptor=new todoadaptor( this,arraytodo );
        listView.setAdapter( adaptor );
        listView.setOnItemClickListener( this );

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText( MainActivity.this,"ok press",Toast.LENGTH_SHORT ).show();
        final todo e=arraytodo.get( i );
        final int position=i;
        AlertDialog.Builder builder=new AlertDialog.Builder( this );
        builder.setTitle( "Delete the task" );
        builder.setPositiveButton( "delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                arraytodo.remove( position );
                Toast.makeText( MainActivity.this,"ok press",Toast.LENGTH_SHORT ).show();
                adaptor.notifyDataSetChanged();
            }
        } );

      AlertDialog dialog=builder.create();
      dialog.show();


        return false;
    }
}

