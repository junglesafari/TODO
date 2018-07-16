package com.example.martin.todo;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static final String BUNDLE_TITLE_KEY = "title";
    public static final String BUNDLE_TEXT_KEY = "text";
    public static final String BUNDLDE_INDEX = "index";
    public static final String BUNDLE_ID = "bundle_id";
    public static final String BUNDLE_DATE_KEY = "date";
    public static final String BUNDLE_TIME_KEY = "time";
    public static final String MESSAGE_INTENT_KEY = "message";
    //    ListView listView;
    RecyclerView listView;
    public static ArrayList<todo> arraytodo;
    public todorecycleradaptor adaptor;
    public static int EDIT_REQUEST_CODE = 1;
    public static int ADD_REQUEST_CODE = 2;
    LinearLayoutManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        listView = findViewById( R.id.list );
        arraytodo = new ArrayList<>();
        ToDoOpenhelper openhelper = ToDoOpenhelper.getInstance( this );
        SQLiteDatabase database = openhelper.getReadableDatabase();
        Cursor cursor = database.query( Contract.Todo.TABLE_NAME, null, null, null, null, null, null );
        while (cursor.moveToNext()) {
            String title = cursor.getString( cursor.getColumnIndex( Contract.Todo.COLUMN_TITLE ) );
            String text = cursor.getString( cursor.getColumnIndex( Contract.Todo.COLUMN_TEXT ) );
            String date = cursor.getString( cursor.getColumnIndex( Contract.Todo.COLUMN_DATE ) );
            String time = cursor.getString( cursor.getColumnIndex( Contract.Todo.COLUMN_TIME ) );
            todo to = new todo( title, text, date, time );


            //most important work because when activity reloads  it assigns id to
            //id attribute of todo class ;

            long id = cursor.getLong( cursor.getColumnIndex( Contract.Todo.COLUMN_ID ) );
            to.setId( id );
            arraytodo.add( to );

        }
        cursor.close();
        adaptor = new todorecycleradaptor( arraytodo, MainActivity.this, new todoclicklistener() {
            @Override
            public void ontodoclick(View view, int position) {
                todo e = arraytodo.get( position );
                String title = e.getTodotitle();
                String text = e.getTodotext();
                String date = e.getTododate();
                String time = e.getTodotime();
                Bundle bundle = new Bundle();
                bundle.putString( BUNDLE_TITLE_KEY, title );
                bundle.putString( BUNDLE_TEXT_KEY, text );
                bundle.putString( BUNDLE_DATE_KEY, date );
                bundle.putString( BUNDLE_TIME_KEY, time );
                bundle.putInt( BUNDLDE_INDEX, position );
                bundle.putLong( BUNDLE_ID, e.getId() );
                Intent intent = new Intent( MainActivity.this, description.class );
                intent.putExtras( bundle );
                startActivityForResult( intent, EDIT_REQUEST_CODE );

            }
        } );
        manager = new LinearLayoutManager( getApplicationContext(), LinearLayoutManager.VERTICAL, false );
        listView.addItemDecoration( new DividerItemDecoration( MainActivity.this, DividerItemDecoration.VERTICAL ) );
        listView.setLayoutManager( manager );
        listView.setAdapter( adaptor );
        listView.setItemAnimator( new DefaultItemAnimator() );
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper( new ItemTouchHelper.SimpleCallback( ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT ) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder fromVH, @NonNull RecyclerView.ViewHolder toVH) {
                int from = fromVH.getAdapterPosition();
                int to = toVH.getAdapterPosition();

                todo post = arraytodo.get( from );
                arraytodo.remove( from );
                arraytodo.add( to, post );

                adaptor.notifyItemMoved( from, to );
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                todo e1 = arraytodo.get( viewHolder.getAdapterPosition() );
                arraytodo.remove( viewHolder.getAdapterPosition() );
                // adaptor.notifyDataSetChanged();
                ToDoOpenhelper openhelper = ToDoOpenhelper.getInstance( getApplicationContext() );
                SQLiteDatabase database = openhelper.getWritableDatabase();
                long id1 = e1.getId();
                String[] selectArguments = {id1 + ""};
                database.delete( Contract.Todo.TABLE_NAME, Contract.Todo.COLUMN_ID + " = ?", selectArguments );

                Toast.makeText( MainActivity.this, "ITEM DELETED", Toast.LENGTH_SHORT ).show();


                adaptor.notifyItemRemoved( viewHolder.getAdapterPosition() );

            }
        } );

        itemTouchHelper.attachToRecyclerView( listView );


//        listView.setOnItemClickListener( this );
//        listView.setOnItemLongClickListener( this );
//working for permission granting
        if (ActivityCompat.checkSelfPermission( this, Manifest.permission.RECEIVE_SMS ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission( this, Manifest.permission.READ_SMS ) == PackageManager.PERMISSION_GRANTED) {

        } else {
            String[] permissions = {Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS};
            ActivityCompat.requestPermissions( this, permissions, 1 );
        }


    }

//    @Override
//    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//        todo e = arraytodo.get( i );
//        int position = i;
//        String title = e.getTodotitle();
//        String text = e.getTodotext();
//        String date = e.getTododate();
//        String time = e.getTodotime();
//        Bundle bundle = new Bundle();
//        bundle.putString( BUNDLE_TITLE_KEY, title );
//        bundle.putString( BUNDLE_TEXT_KEY, text );
//        bundle.putString( BUNDLE_DATE_KEY, date );
//        bundle.putString( BUNDLE_TIME_KEY, time );
//        bundle.putInt( BUNDLDE_INDEX, position );
//        bundle.putLong( BUNDLE_ID, e.getId() );
//        Intent intent = new Intent( MainActivity.this, description.class );
//        intent.putExtras( bundle );
//        startActivityForResult( intent, EDIT_REQUEST_CODE );
//
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.menu, menu );
        return super.onCreateOptionsMenu( menu );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent( MainActivity.this, Addtodo.class );
        startActivityForResult( intent, ADD_REQUEST_CODE );
        return super.onOptionsItemSelected( item );
    }


//    @Override
//    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
//        Toast.makeText( MainActivity.this, "ok press", Toast.LENGTH_SHORT ).show();
//        final todo e1 = arraytodo.get( i );
//        final int position = i;
//        AlertDialog.Builder builder = new AlertDialog.Builder( this );
//        builder.setTitle( "Delete the task" );
//        builder.setPositiveButton( "delete", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                arraytodo.remove( position );
//                adaptor.notifyDataSetChanged();
//                ToDoOpenhelper openhelper = ToDoOpenhelper.getInstance( getApplicationContext() );
//                SQLiteDatabase database = openhelper.getWritableDatabase();
//                long id1 = e1.getId();
//                String[] selectArguments = {id1 + ""};
//                database.delete( Contract.Todo.TABLE_NAME, Contract.Todo.COLUMN_ID + " = ?", selectArguments );
//
//                Toast.makeText( MainActivity.this, "ITEM DELETED", Toast.LENGTH_SHORT ).show();
//
//            }
//        } );
//        builder.setNegativeButton( "NOPE", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                //do nothing here
//            }
//        } );
//
//        AlertDialog dialog = builder.create();
//        dialog.show();
//
//
//        return true;
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == EDIT_REQUEST_CODE) {
            if (resultCode == description.RESULT_CODE_DESCRIPTION_ACTIVITY) {
                Bundle bundle = data.getExtras();
                assert bundle != null;
                int position = bundle.getInt( BUNDLDE_INDEX );
                ToDoOpenhelper openhelper = ToDoOpenhelper.getInstance( getApplicationContext() );
                SQLiteDatabase database = openhelper.getWritableDatabase();
                long id = bundle.getLong( BUNDLE_ID );
                String[] selectionArguments = {id + ""};
                Cursor cursor = database.query( Contract.Todo.TABLE_NAME, null, Contract.Todo.COLUMN_ID + " = ?", selectionArguments, null, null, null );
                while (cursor.moveToNext()) {
                    arraytodo.get( position ).setTodotitle( cursor.getString( cursor.getColumnIndex( Contract.Todo.COLUMN_TITLE ) ) );
                    arraytodo.get( position ).setTodotext( cursor.getString( cursor.getColumnIndex( Contract.Todo.COLUMN_TEXT ) ) );
                    arraytodo.get( position ).setTododate( cursor.getString( cursor.getColumnIndex( Contract.Todo.COLUMN_DATE ) ) );
                    arraytodo.get( position ).setTodotime( cursor.getString( cursor.getColumnIndex( Contract.Todo.COLUMN_TIME ) ) );
                }
                cursor.close();
                adaptor.notifyDataSetChanged();

            }
            //todo::phle bundle bhej ke main activity me hi database me add kr rhe the pr
            //todo::pr intent receiver ke case me ye activity open nhi hoti to bundle yha phuchta hi
            //todo::nhi tha to todo add nhi hota tha tbhi add activity me hi data to database me add krke
            //todo:::mainctivity ke adaptor ko update kiya aur notify bhi kiya tbhi inhe static bnanan pda h
        } else if (resultCode == Addtodo.RESULT_CODE_ADD_ACTIVITY) {
//                Bundle bundle=data.getExtras();

            ToDoOpenhelper openhelper = ToDoOpenhelper.getInstance( getApplicationContext() );
            SQLiteDatabase database = openhelper.getWritableDatabase();
            long id = data.getLongExtra( "id", 0 );
            String[] selectionArguments = {id + ""};
            Cursor cursor = database.query( Contract.Todo.TABLE_NAME, null, Contract.Todo.COLUMN_ID + " = ?", selectionArguments, null, null, null );
            while (cursor.moveToNext()) {
                todo to = new todo( cursor.getString( cursor.getColumnIndex( Contract.Todo.COLUMN_TITLE ) ), cursor.getString( cursor.getColumnIndex( Contract.Todo.COLUMN_TEXT ) ), cursor.getString( cursor.getColumnIndex( Contract.Todo.COLUMN_DATE ) ), cursor.getString( cursor.getColumnIndex( Contract.Todo.COLUMN_TIME ) ) );
                arraytodo.add( to );
            }

            cursor.close();
            adaptor.notifyDataSetChanged();
            // manager=new LinearLayoutManager( getApplicationContext(), LinearLayoutManager.VERTICAL,false);
            //  listView.addItemDecoration(new DividerItemDecoration(MainActivity.this,DividerItemDecoration.VERTICAL));
            // listView.setLayoutManager( manager );
            listView.setAdapter( adaptor );

        }


        super.onActivityResult( requestCode, resultCode, data );
    }


}


