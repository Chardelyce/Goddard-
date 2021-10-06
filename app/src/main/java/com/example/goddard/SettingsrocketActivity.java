package com.example.goddard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public
class SettingsrocketActivity extends AppCompatActivity {
    public static final  String NOTIFICATION_CHANNEL_ID         = "10001";
    private final static String default_notification_channel_id = "default";


    @Override
    protected
    void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.settingsrocket );
        EditText text = null;
        this.setRequestedOrientation ( ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE );
        findViewById ( R.id.qckcmnd ).setOnClickListener ( new View.OnClickListener ( ) {


            @Override
            public
            void onClick ( View v ) {


                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder (
                        SettingsrocketActivity.this ,
                        default_notification_channel_id
                )
                        .setSmallIcon ( R.drawable.logo__1_ )
                        .setContentTitle ( "Goddard.va" )

                        .setContentText (
                                "\" WHAT TIME IS IT \"\n" +
                                "\" WHAT DAY IS IT \" \n" +
                                "\" OPEN YOUTUBE \"\n" +
                                "\"OPEN SPOTIFY \"\n" +
                                "\" WEATHER \" \n" +
                                "\"SHUT DOWN\n" +
                                "\" OPEN CHROME\n" );
                NotificationManager mNotificationManager = ( NotificationManager ) getSystemService ( Context.NOTIFICATION_SERVICE );
                if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ) {
                    AudioAttributes audioAttributes = new AudioAttributes.Builder ( )
                            .setContentType ( AudioAttributes.CONTENT_TYPE_SONIFICATION )
                            .setUsage ( AudioAttributes.USAGE_ALARM )
                            .build ( );
                    int importance = NotificationManager.IMPORTANCE_HIGH;
                    NotificationChannel notificationChannel = new
                            NotificationChannel ( NOTIFICATION_CHANNEL_ID , "NOTIFICATION_CHANNEL_NAME" , importance );
                    notificationChannel.enableLights ( true );
                    notificationChannel.setLightColor ( Color.RED );
                    notificationChannel.enableVibration ( true );
                    notificationChannel.setVibrationPattern ( new long[] { 100 , 200 , 300 , 400 , 500 , 400 , 300 , 200 , 400 } );

                    mBuilder.setChannelId ( NOTIFICATION_CHANNEL_ID );
                    assert mNotificationManager != null;
                    mNotificationManager.createNotificationChannel ( notificationChannel );

                    assert mNotificationManager != null;
                    mNotificationManager.notify ( ( int ) System.currentTimeMillis ( ) , mBuilder.build ( ) );
                }
            }
        } );
        findViewById ( R.id.cmdman ).setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public
            void onClick ( View v ) {


            }
        } );
    }

}