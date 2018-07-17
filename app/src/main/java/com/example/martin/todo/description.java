package com.example.martin.todo;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.IDN;
import java.util.regex.Pattern;

public class description extends AppCompatActivity {
TextView description;
Button editbutton;
public static final int TEXT_EDIT_REQUEST_CODE=3;
public static final int RESULT_CODE_DESCRIPTION_ACTIVITY=4;
ImageView savetomail;
TextView feedback;
TextView datetextview;
TextView timetextview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_description );
      description=findViewById( R.id.descriptiontext );
      savetomail=findViewById( R.id.savetomailimage );
         feedback=findViewById( R.id.feedbackimage );
        datetextview=findViewById( R.id.descriptiondate );
        timetextview=findViewById( R.id.descriptiontime );
         final Intent intent=getIntent();
        final Bundle bundle=intent.getExtras();
        String text= bundle.getString( MainActivity.BUNDLE_TEXT_KEY );
        String date=bundle.getString( MainActivity.BUNDLE_DATE_KEY );
        String time=bundle.getString( MainActivity.BUNDLE_TIME_KEY );
        datetextview.setText( date );
        timetextview.setText( time );
        description.setText( text );
        editbutton=findViewById( R.id.editbutton );
        editbutton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1=new Intent( description.this,editActivity.class );
                intent1.putExtras( bundle );
                startActivityForResult(intent1,TEXT_EDIT_REQUEST_CODE);
                finish();
            }
        } );
        setResult( RESULT_CODE_DESCRIPTION_ACTIVITY, intent);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==TEXT_EDIT_REQUEST_CODE){
            if(resultCode==editActivity.RESULT_CODE){
                Bundle bundle=data.getExtras();
                String setupdatedtext=bundle.getString( MainActivity.BUNDLE_TEXT_KEY );
                description.setText( setupdatedtext );

            }
        }
        super.onActivityResult( requestCode, resultCode, data );
    }

public void feedback(View view){
        Intent intent=new Intent(  );
        intent.setAction( Intent.ACTION_SENDTO );
    Uri uri=Uri.parse( "mailto:himanshusachan806@gmail.com" );
    intent.putExtra( intent.EXTRA_SUBJECT,"Feedback" );
    intent.setData( uri );
    startActivity( intent );

}
    protected void onStart() {
        Log.d( "mainActiviy","onStartdescription" );
        super.onStart();
    }


}
