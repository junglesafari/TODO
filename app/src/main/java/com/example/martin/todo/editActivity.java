package com.example.martin.todo;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class editActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener ,TimePickerDialog.OnTimeSetListener{
EditText edittitle;
EditText editText;
TextView editdate;
TextView edittime;
Button savechange;
ImageView calender_edit;
ImageView time_edit;
Calendar alarm;
    int year ;
    int month ;
    int day ;
    int hour ;
    int minutes ;
    long lastalarmtime;
public static int RESULT_CODE=5;
int databaseprimaryid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_edit );
        alarm=Calendar.getInstance();
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
        ToDoOpenhelper openhelper=ToDoOpenhelper.getInstance(getApplicationContext() );
        SQLiteDatabase database=openhelper.getReadableDatabase();
        String[] selectionargs={bundle.getString( MainActivity.BUNDLE_TITLE_KEY )
                ,bundle.getString( MainActivity.BUNDLE_TEXT_KEY )
                ,bundle.getString( MainActivity.BUNDLE_DATE_KEY )
                ,bundle.getString( MainActivity.BUNDLE_TIME_KEY )};
        Cursor cursor=database.query( Contract.Todo.TABLE_NAME, null,
                Contract.Todo.COLUMN_TITLE+" = ? AND "
                +Contract.Todo.COLUMN_TEXT +"= ? AND "+Contract.Todo.COLUMN_DATE+" = ? AND "+
        Contract.Todo.COLUMN_TIME+" = ? ",selectionargs,null,null,null);
     while (cursor.moveToNext()){
        lastalarmtime =cursor.getLong( cursor.getColumnIndex( Contract.Todo.COLUMN_ALARM ) );
         databaseprimaryid=cursor.getInt( cursor.getColumnIndex( Contract.Todo.COLUMN_ID ) );
     }

        Log.d( "lastalarmtime",lastalarmtime +" one");
          alarm.setTimeInMillis(  lastalarmtime);

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
        contentValues.put( Contract.Todo.COLUMN_ALARM,alarm.getTimeInMillis() );
        database.update( Contract.Todo.TABLE_NAME,contentValues,Contract.Todo.COLUMN_ID+" = ?",selectionArguments );

        //alarm updation
        AlarmManager alarmManager = (AlarmManager) getSystemService( ALARM_SERVICE );
        Intent intent1 = new Intent( getApplicationContext(), MyReceiver.class );
        intent1.putExtra( "title", bundle.getString( MainActivity.BUNDLE_TITLE_KEY ));
        intent1.putExtra( "des", bundle.getString( MainActivity.BUNDLE_TEXT_KEY ) );
        intent1.putExtra( "date", bundle.getString( MainActivity.BUNDLE_DATE_KEY ));
        intent1.putExtra( "time",bundle.getString( MainActivity.BUNDLE_TIME_KEY ) );



        int alarmid = databaseprimaryid;
        long alarmtoset=alarm.getTimeInMillis();
        Log.d( "lastalarmtime",alarmtoset+" three" );
        PendingIntent pendingIntent = PendingIntent.getBroadcast( getApplicationContext(), alarmid, intent1, PendingIntent.FLAG_UPDATE_CURRENT );
        alarmManager.set( AlarmManager.RTC_WAKEUP, alarmtoset, pendingIntent );

        setResult( RESULT_CODE, intent);
         finish();

    }


    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        String new_date=i2+"/"+i1+"/"+i;
        editdate.setText( new_date );
        year=i;
        month=i1;
        day=i2;
        Intent intent=getIntent();
        Calendar c=Calendar.getInstance();
        c.setTimeInMillis(  intent.getLongExtra( "alarmtime",0 ));
        int hour=c.get( Calendar.HOUR );
        int minutes=c.get( Calendar.MINUTE );
        alarm.set(year,month,day,hour,minutes);

    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        hour=i1;
        minutes=i;
        if(DateFormat.is24HourFormat( this )){
            String time = i + ":" + i1;
            edittime.setText( time );
        }else {
            if(i>12){
                String time = i-12 + ":" + i1+":"+"PM";Log.d( "lastalarmtime",alarm.getTimeInMillis()+" two" );
                edittime.setText( time );
            }else {
                String time = i + ":" + i1+":"+"AM";
                edittime.setText( time );
            }
        }
  alarm.set(year,month,day,hour,minutes);


    }



}
