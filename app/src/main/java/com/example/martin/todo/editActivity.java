package com.example.martin.todo;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

public class editActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener ,TimePickerDialog.OnTimeSetListener{
EditText edittitle;
EditText editText;
TextView editdate;
TextView edittime;
Button savechange;
ImageView calender_edit;
ImageView time_edit;
public static int RESULT_CODE=5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_edit );
        edittitle = findViewById( R.id.editText );
        editText=findViewById( R.id.editText2 );
        editdate=findViewById( R.id.edit_date_textview );
        edittime=findViewById( R.id.edit_time_textview );
        calender_edit=findViewById( R.id.edit_date_image );
        time_edit=findViewById( R.id.edit_time_image );
        savechange=findViewById( R.id.button2 );
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        assert bundle != null;
        edittitle.setText(bundle.getString( MainActivity.BUNDLE_TITLE_KEY )  );
        editText.setText( bundle.getString( MainActivity.BUNDLE_TEXT_KEY ) );
        editdate.setText( bundle.getString( MainActivity.BUNDLE_DATE_KEY ) );
        edittime.setText( bundle.getString( MainActivity.BUNDLE_TIME_KEY ) );
        calender_edit.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datepicker=new DatepickerFragment();
                datepicker.show(getSupportFragmentManager(),"date picker");
            }
        } );
        time_edit.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment timepicker=new TimepickerFragment();
                timepicker.show( getSupportFragmentManager(),"time piker" );
            }
        } );

    }

    public  void setSavechange(View view){
       String title=edittitle.getText().toString();
       String text=editText.getText().toString();
       String date=editdate.getText().toString();
       String time=edittime.getText().toString();
        Intent intent=getIntent();

        Bundle bundle=intent.getExtras();
        assert bundle != null;
        bundle.putString( MainActivity.BUNDLE_TITLE_KEY,title );
        bundle.putString( MainActivity.BUNDLE_TEXT_KEY ,text );
        bundle.putString( MainActivity.BUNDLE_DATE_KEY ,date );
        bundle.putString( MainActivity.BUNDLE_TIME_KEY,time );
        intent.putExtras( bundle );
        ToDoOpenhelper openhelper=ToDoOpenhelper.getInstance( getApplicationContext() );
        SQLiteDatabase database=openhelper.getWritableDatabase();
        long id=bundle.getLong( MainActivity.BUNDLE_ID );
        String[] selectionArguments={id+""};
        ContentValues contentValues=new ContentValues(  );
        contentValues.put( Contract.Todo.COLUMN_TITLE,bundle.getString( MainActivity.BUNDLE_TITLE_KEY ) );
        contentValues.put(Contract.Todo.COLUMN_TEXT,bundle.getString( MainActivity.BUNDLE_TEXT_KEY )  );
        contentValues.put(Contract.Todo.COLUMN_DATE,bundle.getString( MainActivity.BUNDLE_DATE_KEY )  );
        contentValues.put(Contract.Todo.COLUMN_TIME,bundle.getString( MainActivity.BUNDLE_TIME_KEY )  );
        database.update( Contract.Todo.TABLE_NAME,contentValues,Contract.Todo.COLUMN_ID+" = ?",selectionArguments );

        setResult( RESULT_CODE, intent);
         finish();

    }


    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        String new_date=i2+"/"+i1+"/"+i;
        editdate.setText( new_date );
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        if(DateFormat.is24HourFormat( this )){
            String time = i + ":" + i1;
            edittime.setText( time );
        }else {
            if(i>12){
                String time = i-12 + ":" + i1+":"+"PM";
                edittime.setText( time );
            }else {
                String time = i + ":" + i1+":"+"AM";
                edittime.setText( time );
            }
        }
    }
}
