package com.example.martin.todo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ToDoOpenhelper extends SQLiteOpenHelper {
public static final String DATABASE_NAME="tododatabase";
public static final int DATABASE_VERSION=1;

//make its constructor private
private ToDoOpenhelper(Context context) {
        super( context, DATABASE_NAME, null, DATABASE_VERSION );
    }
    public static ToDoOpenhelper instance;
    public static ToDoOpenhelper getInstance(Context context) {
       if(instance==null){
           instance=new ToDoOpenhelper( context.getApplicationContext() );
           return instance;
       }
        return instance;
    }

    //make public instance



    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String todoSql="CREATE TABLE "+ Contract.Todo.TABLE_NAME + " ( "+
                Contract.Todo.COLUMN_ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT , "+
                Contract.Todo.COLUMN_TITLE+ " TEXT , " +
                Contract.Todo.COLUMN_TEXT + " TEXT ,"+
                Contract.Todo.COLUMN_DATE+" TEXT ,"+
                Contract.Todo.COLUMN_ALARM+" LONG ,"+
                Contract.Todo.COLUMN_TIME+" TEXT ) ";
        sqLiteDatabase.execSQL( todoSql );

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
