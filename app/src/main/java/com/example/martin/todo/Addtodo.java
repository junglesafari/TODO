package com.example.martin.todo;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class Addtodo extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {


    EditText title;
    EditText text;
    TextView datetext;
    TextView timetext;
    ImageView datebutton;
    ImageView timebutton;
    Button addtodobutton;
    Calendar alarm;
    int year ;
    int month ;
    int day ;
    int hour ;
    int minutes ;
    public static final int RESULT_CODE_ADD_ACTIVITY = 4;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_addtodo );

        alarm = Calendar.getInstance();
        title = findViewById( R.id.title );
        text = findViewById( R.id.datatoinsert );
        datebutton = findViewById( R.id.datepickerbutton );
        timebutton = findViewById( R.id.timepickerbutton );
        datetext = findViewById( R.id.textView3 );
        timetext = findViewById( R.id.textView4 );
        addtodobutton = findViewById( R.id.addtodo );
        final Intent intent = getIntent();


        String action = intent.getAction();
        if (action != null) {
            if (action.equals( Intent.ACTION_SEND )) {
                String text1 = intent.getStringExtra( Intent.EXTRA_TEXT );
                text.setText( text1 );
                title.setText( String.format( "TODO%d", MainActivity.arraytodo.size() + 1 ) );
               alarm = Calendar.getInstance();
                alarm.setTimeInMillis( System.currentTimeMillis() );
                 year = alarm.get( Calendar.YEAR );
                 month = alarm.get( Calendar.MONTH );
                 day = alarm.get( Calendar.DAY_OF_MONTH );
                 hour = alarm.get( Calendar.HOUR_OF_DAY );
                 minutes = alarm.get( Calendar.MINUTE );
                String dateautoset = day + "/" + month + "/" + year;
                datetext.setText( dateautoset );
                String timeautoset = hour + ":" + minutes;
                timetext.setText( timeautoset );
            }
        } else if (intent.getStringExtra( MainActivity.MESSAGE_INTENT_KEY ) != null) {
            String text12 = intent.getStringExtra( MainActivity.MESSAGE_INTENT_KEY );
            Log.d( "text12", text12 );
            text.setText( text12 );
            title.setText( "message" );
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis( System.currentTimeMillis() );
            year = calendar.get( Calendar.YEAR );
             month = calendar.get( Calendar.MONTH );
             day = calendar.get( Calendar.DAY_OF_MONTH );
             hour = calendar.get( Calendar.HOUR_OF_DAY );
             minutes = calendar.get( Calendar.MINUTE );
            String dateautoset = day + "/" + month + "/" + year;
            datetext.setText( dateautoset );
            String timeautoset = hour + ":" + minutes;
            timetext.setText( timeautoset );
        }


        addtodobutton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String titlename = title.getText().toString().trim();
                String textname = text.getText().toString().trim();
                String textdate = datetext.getText().toString().trim();
                String texttime = timetext.getText().toString().trim();
              // long qwerty=alarm.getTimeInMillis();
              //  alarm.set( year,month,day,hour,minutes );
              //  Log.d( "alarmtoset",qwerty+" one" );
                long alarmtoset =alarm.getTimeInMillis();
              //  Log.d( "alarmtoset",alarm.getTime()+" two" );
                if (titlename.equals( "" ) || textname.equals( "" ) || textdate.equals( "" ) || texttime.equals( "" )) {
                    Toast.makeText( Addtodo.this, "Enter the valid text", Toast.LENGTH_SHORT ).show();
                    return;
                }
                ToDoOpenhelper openhelper = ToDoOpenhelper.getInstance( Addtodo.this );
                SQLiteDatabase database = openhelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put( Contract.Todo.COLUMN_TITLE, titlename );
                values.put( Contract.Todo.COLUMN_TEXT, textname );
                values.put( Contract.Todo.COLUMN_DATE, textdate );
                values.put( Contract.Todo.COLUMN_TIME, texttime );
                values.put( Contract.Todo.COLUMN_ALARM, alarmtoset );
                long id = database.insert( Contract.Todo.TABLE_NAME, null, values );
                alarmset( alarmtoset, id, titlename, textname,textdate,texttime );

                intent.putExtra( "id", id );
                setResult( Addtodo.RESULT_CODE_ADD_ACTIVITY, intent );
                finish();
            }
        } );
        datebutton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datepicker = new DatepickerFragment();
                datepicker.show( getSupportFragmentManager(), "date picker" );
            }
        } );
        timebutton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment timepcker = new TimepickerFragment();
                timepcker.show( getSupportFragmentManager(), "time picker" );
            }
        } );


    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        String date = i2 + "/" + i1 + "/" + i;
//        alarm.setTimeInMillis(  i );
//        alarm.set( Calendar.MONTH, i1 );
//        alarm.set( Calendar.DAY_OF_MONTH, i2 );
//        alarm.set(i,i1,i2);
        datetext.setText( date );
        year=i;
        month=i1;
        day=i2;
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        if (DateFormat.is24HourFormat( this )) {
            String time = i + ":" + i1;
            timetext.setText( time );
        } else {
            if (i > 12) {
                String time = i - 12 + ":" + i1 + ":" + "PM";
                timetext.setText( time );
            } else {
                String time = i + ":" + i1 + ":" + "AM";
                timetext.setText( time );
            }
        }
        hour=i;
        minutes=i1;
//        alarm.set( Calendar.HOUR, i );
//        alarm.set( Calendar.MINUTE, i1 );
        Log.d( "alarmtoset",year+" "+month+" "+day+" "+hour+" "+minutes+"" );

        alarm.set (year,month,day,hour,minutes);
        Log.d( "alarmtoset",alarm.getTime()+" three" );
    }

    private void alarmset(long alarm, long id, String title, String description,String date,String time) {
        AlarmManager alarmManager = (AlarmManager) getSystemService( ALARM_SERVICE );
        Intent intent = new Intent( getApplicationContext(), MyReceiver.class );
        intent.putExtra( "title", title );
        intent.putExtra( "des", description );
        intent.putExtra( "date", date);
        intent.putExtra( "time",time );
        intent.putExtra( "alarmtime",alarm );
        Log.d(  "bundlekeys1","title "+title+" time "+time+" description "+description+" date "+date+" alarmtime "+alarm  );
        int alarmid = (int) id;
        PendingIntent pendingIntent = PendingIntent.getBroadcast( getApplicationContext(), alarmid, intent, PendingIntent.FLAG_UPDATE_CURRENT );
        alarmManager.set( AlarmManager.RTC_WAKEUP, alarm, pendingIntent );
    }


}
