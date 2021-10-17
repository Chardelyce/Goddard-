package com.example.goddard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
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
        findViewById ( R.id.googdino).setOnClickListener (new View.OnClickListener ( ) {


            @Override
            public
            void onClick ( View v ) {
                Intent threeee = new Intent ( );
                threeee.setAction ( Intent.ACTION_VIEW );
                threeee.addCategory ( Intent.CATEGORY_BROWSABLE );
                threeee.setData ( Uri.parse ( "chrome://dino" ) );
                startActivity ( threeee );


            }
        } );
        findViewById ( R.id.cmdman ).setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public
            void onClick ( View v ) {

                Intent threee = new Intent ( );
                threee.setAction ( Intent.ACTION_VIEW );
                threee.addCategory ( Intent.CATEGORY_BROWSABLE );
                threee.setData ( Uri.parse ( "https://github.com/Chardelyce/Goddard-/blob/master/handycommands.txt" ) );
                startActivity ( threee );

            }
        } );
    }

}