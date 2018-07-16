package com.example.martin.todo;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsMessage;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Random;

import static android.content.Context.NOTIFICATION_SERVICE;

public class MyReceiver extends BroadcastReceiver {
    // TODO: This method is called when the BroadcastReceiver is receiving
    String message;
    // an Intent broadcast.
    NotificationManager manager;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {
        manager = (NotificationManager) context.getSystemService( NOTIFICATION_SERVICE );
        // String action=intent.getAction();
        if (intent.getAction() != null && intent.getAction().equals( "android.provider.Telephony.SMS_RECEIVED" )) {
            //message reading from broadcast
            Bundle bundle = intent.getExtras();
            try {

                if (bundle != null) {

                    final Object[] pdusObj = (Object[]) bundle.get( "pdus" );
                    String format = bundle.getString( "format" );

                    for (int i = 0; i < pdusObj.length; i++) {
                        SmsMessage currentMessage = SmsMessage.createFromPdu( (byte[]) pdusObj[i], format );
                        String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                        String senderNum = phoneNumber;
                        message = currentMessage.getDisplayMessageBody();
                        //Log.i("SmsReceiver", "senderNum: "+ senderNum + "; message: " + message);
                        // Show Alert
//                         int duration = Toast.LENGTH_LONG;
//                         Toast toast = Toast.makeText(context,
//                                "senderNum: "+ senderNum + ", message: " + message, duration);
//                        toast.show();
                        Intent open = new Intent( context, MyReceiver.class );
                        open.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
                        context.startActivity( open );

                    } // end for loop
                } // bundle is null

            } catch (Exception e) {
                //Log.e("SmsReceiver", "Exception smsReceiver" +e);

            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel( "mychannelid", "todo channel", NotificationManager.IMPORTANCE_HIGH );
                manager.createNotificationChannel( channel );

            }

            NotificationCompat.Builder builder = new NotificationCompat.Builder( context, "mychannelid" );
            builder.setSmallIcon( R.drawable.ic_launcher_background );
            builder.setContentTitle( "TODO" );
            builder.setContentText( "Convert this to a new task" );
            Intent intent1 = new Intent( context, Addtodo.class );
            intent1.putExtra( MainActivity.MESSAGE_INTENT_KEY, message );
            int c = Integer.parseInt( String.valueOf( SystemClock.currentThreadTimeMillis() ) );
            PendingIntent pendingIntent = PendingIntent.getActivity( context, c, intent1, 0 );

            builder.setContentIntent( pendingIntent );
            Notification notification = builder.build();
            manager.notify( 1, notification );
            builder.setAutoCancel( true );


        } else if (intent.getAction() != null && intent.getAction().equals( "android.intent.action.BOOT_COMPLETED" )) {

            ToDoOpenhelper openhelper = ToDoOpenhelper.getInstance( context );
            SQLiteDatabase database = openhelper.getReadableDatabase();
            Cursor cursor = database.query( Contract.Todo.TABLE_NAME, null, null, null, null, null, null );
            while (cursor.moveToNext()) {
                long currenttime = System.currentTimeMillis();
                long alarm = cursor.getLong( cursor.getColumnIndex( Contract.Todo.COLUMN_ALARM ) );
                int id = cursor.getInt( cursor.getColumnIndex( Contract.Todo.COLUMN_ID ) );
                String title = cursor.getString( cursor.getColumnIndex( Contract.Todo.COLUMN_TITLE ) );
                String description = cursor.getString( cursor.getColumnIndex( Contract.Todo.COLUMN_TEXT ) );
                if (alarm > currenttime) {
                    AlarmManager alarmManager = (AlarmManager) context.getSystemService( Context.ALARM_SERVICE );
                    Intent intent1 = new Intent( context.getApplicationContext(), MyReceiver.class );
                    intent1.putExtra( "title", title );
                    intent1.putExtra( "des", description );
                    PendingIntent pendingIntent = PendingIntent.getBroadcast( context, id, intent1, 0 );
                    alarmManager.set( AlarmManager.RTC_WAKEUP, alarm, pendingIntent );
                }

            }


        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel( "mychannelid2", "todo channel", NotificationManager.IMPORTANCE_HIGH );
                manager.createNotificationChannel( channel );

            }
            NotificationCompat.Builder builder = new NotificationCompat.Builder( context, "mychannelid2" );
            builder.setSmallIcon( R.drawable.ic_launcher_background );
            builder.setContentTitle( intent.getStringExtra( "title" ) );
            builder.setContentText( intent.getStringExtra( "des" ) );
            Intent intent1 = new Intent( context, description.class );
            Bundle bundle=new Bundle(  );
            bundle.putString( MainActivity.BUNDLE_TEXT_KEY,intent.getStringExtra( "des" ) );
           intent1.putExtras( bundle );
            PendingIntent pendingIntent = PendingIntent.getActivity( context, 2, intent1, 0 );
            builder.setContentIntent( pendingIntent );
            Notification notification = builder.build();
            manager.notify( 1, notification );

        }


    }
}
