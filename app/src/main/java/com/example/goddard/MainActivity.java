package com.example.goddard;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
/* GoddardAI/Goddard.va © 2021 Charde'Lyce Edwards  */
import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.AlarmClock;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;


public
class MainActivity extends AppCompatActivity {


    private static final String                   PREFS    = "prefs";
    private static final String                   NEW      = "new";
    private static final String                   NAME     = "name";
    private static final String                   AGE      = "age";
    private static final String                   AS_NAME  = "as_name";
    private static final String                   build    = "built";
    private static final String                   daisy    = "day";
    private static final String                   device   = "devicever";
    private static final String                   maybuild = "may";
    public               String                   msg;
    EditText  editText;
    ImageView imageView;
    String    model = Build.MODEL;
    private TextToSpeech         tts;
    private ArrayList < String > questions;
    private String               name, surname, age, asName;
    private              SharedPreferences        preferences;
    private              SharedPreferences.Editor editor;

    @Override
    protected
    void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_main );
        this.setRequestedOrientation ( ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE );


        findViewById ( R.id.settingsbutton ).setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public
            void onClick ( View v ) {

                Intent intent = new Intent ( MainActivity.this , SettingsrocketActivity.class );
                startActivity ( intent );

            }
        } );


        preferences = getSharedPreferences ( PREFS , 0 );
        editor      = preferences.edit ( );

        findViewById ( R.id.microphoneButton ).setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public
            void onClick ( View v ) {

                listen ( );
                loadQuestions ( );
            }
        } );



        tts = new TextToSpeech ( this , new TextToSpeech.OnInitListener ( ) {

            @Override
            public
            void onInit ( int status ) {
                if ( status == TextToSpeech.SUCCESS ) {
                    int result     = tts.setLanguage ( Locale.US);
                    int greenbeans = tts.setPitch ( - 8.0f );
                    Voice voiceobj = new Voice("en-uk-x-sfg#male_2-local", new Locale("en","UK"), 1, 1, false, null);
                    tts.setVoice(voiceobj);
                    if ( result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED ) {
                        Log.e ( "TTS" , "This Language is not supported" );
                    }
                    MediaPlayer player = MediaPlayer.create ( MainActivity.this , R.raw.barkbark );
                    player.start ( );
                }
                else {
                    Log.e ( "TTS" , "Initialization Failed!" );
                }

            }
        } );

    }


    private
    void loadQuestions ( ) {
        questions = new ArrayList <> ( );
        questions.clear ( );
        questions.add ("Hello, and Welcome to Goddard vee A, some quick commands you can say are Open Youtube, weather, Open chrome, what time is it, now what is your name");
        Toast.makeText ( MainActivity.this , "Quick cmds:Open Youtube, weather, Open chrome, what time is it? " ,
                Toast.LENGTH_LONG
        ).show ( );
        questions.add("How old are you?");




        listen ( );


    }

    private
    void listen ( ) {
        Intent i = new Intent ( RecognizerIntent.ACTION_RECOGNIZE_SPEECH );
        i.putExtra ( RecognizerIntent.EXTRA_LANGUAGE_MODEL , RecognizerIntent.LANGUAGE_MODEL_FREE_FORM );
        i.putExtra ( RecognizerIntent.EXTRA_LANGUAGE , Locale.getDefault ( ) );
        i.putExtra ( RecognizerIntent.EXTRA_PROMPT , "Waiting on input" );

        try {
            startActivityForResult ( i , 100 );
        }
        catch ( ActivityNotFoundException a ) {
            Toast.makeText ( MainActivity.this , "Your device doesn't support Speech Recognition" , Toast.LENGTH_SHORT ).show ( );
        }
    }

    @Override
    public
    void onDestroy ( ) {
        if ( tts != null ) {
            tts.stop ( );
            tts.shutdown ( );
        }
        super.onDestroy ( );
    }

    private
    void speak ( String text ) {
        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ) {
            tts.speak ( text , TextToSpeech.QUEUE_FLUSH , null , null );

        }
        else {
            tts.speak ( text , TextToSpeech.QUEUE_FLUSH , null );
        }
    }

    @Override
    protected
    void onActivityResult ( int requestCode , int resultCode , Intent data ) {
        super.onActivityResult ( requestCode , resultCode , data );
        if ( requestCode == 100 ) {
            if ( resultCode == RESULT_OK && null != data ) {
                ArrayList < String > res      = data.getStringArrayListExtra ( RecognizerIntent.EXTRA_RESULTS );
                String               inSpeech = res.get ( 0 );
                recognition ( inSpeech );
            }
        }
    }

    private
    void recognition ( String text ) {
        Log.e ( "Speech" , "" + text );
        String[] speech = text.split ( " " );
        if ( text.contains ( "hello" ) ) {
            speak ( questions.get ( 0 ) );
        }


        if ( text.contains ( "my name is" ) ) {
            name = speech[ speech.length - 1 ];
            Log.e ( "THIS" , "" + name );
            editor.putString ( NAME , name ).apply ( );

        }

        if ( text.contains ( "years" ) && text.contains ( "old" ) ) {
            String age = speech[ speech.length - 3 ];
            Log.e ( "THIS" , "" + age );
            editor.putString ( AGE , age ).apply ( );
        }



        if ( text.contains ( "your name" ) ) {
            String as_name = preferences.getString ( AS_NAME , "" );
            if ( as_name.equals ( "" ) )
                speak ( "What do you want to name me?" );
            else
                speak ( "My name is " + as_name+"What about you" );
        }
        if ( text.contains ( "call you" ) ) {
            String name = speech[ speech.length - 1 ];
            editor.putString ( AS_NAME , name ).apply ( );
            speak ( "if that what you desire to call me" + preferences.getString ( NAME , null ) );
        }
        //region basic commands
        if ( text.contains ( "what is my name" ) ) {
            speak (  preferences.getString ( NAME , null));
            Toast.makeText ( MainActivity.this , "Did i get your name correct?, you can say correct to confirm or no to try again " ,
                    Toast.LENGTH_LONG
            ).show ( );
            if ( text.equals ( "correct") )
                speak ( "great i'll save this so i can better assist you" );//for use with the name command

            if (text.equals("retry"))
                speak ( "i apologize if you can repeat it again you can say the command my name is to try again" +
                        "" );//retries a version of the name again

            if ( text.contains ( "my name is" ) ) {
                name = speech[ speech.length - 1 ];
                Log.e ( "THIS" , "" + name );
                editor.putString ( NAME , name ).apply ( );


            }
        }


        //==============
        //extra memory commands
        //==============
        if ( text.contains ( "build version" ) ) {
            String building = preferences.getString ( build , "" );
            if ( building.equals ( "" ) )
                speak ( "When was i built?" );
            else
                speak ( "current build version " + building );

        }
        //
        if ( text.contains ( "implemented" ) ) {
            String ver = speech[ speech.length - 1 ];
            editor.putString ( build , ver ).apply ( );
            speak ( "saving " + preferences.getString ( build , null ) );

        }
        if ( text.contains ( "Hardware" ) ) {
            speak ( "Current device " + model );

        }
        if ( text.contains ( "progress" ) ) {
            speak ( "Progress  of my project is done While i am a limited first test build my succesor will have more features enabled" );

        }
        if ( text.contains ( "thank you" ) ) {
            speak ( "you're welcome" + preferences.getString ( NAME , null ) );
            listen ( );
        }
        //age command
        if ( text.contains ( "how old am I" ) ) {
            speak ( "You are " + preferences.getString ( AGE , null ) + " years old." );
            listen ( );
        }
//=================== daisy daisy
//
//
//


        if ( text.contains ( "hello world" ) ) {
            String bi = preferences.getString ( daisy , "" );
            if ( bi.equals ( "" ) )
                speak ( "Testing memory" );
            else
                speak ( "Daisy,Daisy,give me your answer " + bi );

        }


        if ( text.contains ( "yes" ) ) {
            String world = speech[ speech.length - 1 ];
            editor.putString ( daisy , world ).apply ( );
            speak ( "saving test " + preferences.getString ( daisy , null ) );//do
        }

        //good morning command
        if ( text.contains ( "good morning" ) ) {
            speak ( "good morning " + preferences.getString ( NAME , null ) );
            listen ( );
        }
        if ( text.contains ( "good night" ) ) {
            speak ( "good night " + preferences.getString ( NAME , null ) );
            finish ( );
        }

        if ( text.contains ( "how do you do" ) ) {
            speak ( "its very nice to make your acquaintance  " + preferences.getString ( NAME , null ) );
            listen ( );
        }



//
// ================================================

        // time and day command
        if ( text.contains ( "what time is it" ) ) {
            SimpleDateFormat sdfDate = new SimpleDateFormat ( "H:mm" );//dd/MM/yyyy
            Date             now     = new Date ( );


            String[] strDate = sdfDate.format ( now ).split ( ":" );
            if ( strDate[ 1 ].contains ( "00" ) )
                strDate[ 1 ] = "o'clock";
            speak ( "The time is " + sdfDate.format ( now ) );
            listen ( );

        }
        if ( text.contains ( "what day is it" ) ) {
            SimpleDateFormat sdfDates = new SimpleDateFormat ( "EEEE" );
            Date             nows     = new Date ( );
            String[]         strDate  = sdfDates.format ( nows ).split ( ":" );
            speak (    sdfDates.format ( nows ) );

            listen ( );
        }


        //endregion


        //region show off commands
        if ( text.contains ( "introduce yourself" ) ) {
            speak ( "Hello My name is Goddard and I am a voice assistance  project based on the cartoon robotic dog of the same name I was first  conceptualised " +
                    "in c sharp as a windows forms project then from inspiration I was then migrated to  java for ease of use with mobile devices" +
                    "especially with the device that will be used with the body of the robot"  +
                    " My use is to be both the personality as well as a general Voice assistant. With that said I am in your care thankyou." );
        }
        if ( text.contains ( "speak" ) ) {
            MediaPlayer player = MediaPlayer.create ( MainActivity.this , R.raw.barkbark );
            player.start ( );
        }
        if ( text.contains ( "sing" ) ) {
            MediaPlayer music = MediaPlayer.create ( MainActivity.this , R.raw.song );
            music.start ( );
        }

        if ( text.contains ( "thank you boy" ) ) {
            speak ( "you're welcome char-dee-lease" );
            listen ( );
        }

        if ( text.contains ( "who's a good boy" ) ) {
            MediaPlayer player = MediaPlayer.create ( MainActivity.this , R.raw.barkbark );
            player.start ( );
            listen ( );
        }

        String[] jokey = new String[ 10 ];
        jokey[ 0 ] = "What was the exercising avocado worried about... his core!";
        jokey[ 1 ] = "Alright whats a drink for alligators?........Gatorade ";
        jokey[ 2 ] = "Did you hear about the guy who walked into a bar?......he lost the limbo contest";
        jokey[ 4 ] = "people can’t tell the difference between entomology and etymology. I can’t find the words for how much this bugs me.";
        jokey[ 5 ] = "I had a date with a magician last night i tried to text him back today be he disappeared";
        jokey[ 6 ] = "Did you hear about the toddler who got arrested? he was not wanting to take a nap so he was brought up on resisting a rest";
        jokey[ 7 ] = "what do you call an atom on its first day of work ?......a neutron";
        jokey[ 8 ] = "what do you call gelatin in a swamp? a marshmallow";
        Random randoemo = new Random ( );
        int    len      = randoemo.nextInt ( jokey.length );
        if ( text.contains ( "tell me a joke" ) ) {
            speak ( "" + jokey[ len ] );
        }


        //emotions block
        String[] emotions = new String[ 10 ];
        emotions[ 0 ] = "Happy";
        emotions[ 1 ] = "Sad";
        emotions[ 2 ] = "Tired";
        emotions[ 3 ] = "Vibing";
        emotions[ 4 ] = "Hungry";
        emotions[ 5 ] = "alright";
        emotions[ 6 ] = "Good";
        emotions[ 7 ] = "sus";
        emotions[ 8 ] = "irritated";
        emotions[ 9 ] = "null, and i mean that i am null";

        Random randoemoo = new Random ( );
        int    rin       = randoemoo.nextInt ( emotions.length );
        if ( text.contains ( "how are you" ) ) {
            speak ( "I'm feeling like I am " + emotions[ rin ] );

        }


        //endregion


        //region open app activities
        if ( text.contains ( "open YouTube" ) ) {
            speak ( "opening youtube" );
            Intent launchIntent = getPackageManager ( ).getLaunchIntentForPackage ( "com.google.android.youtube" );
            if ( launchIntent != null ) {
                startActivity ( launchIntent );
            }
            else {
                Toast.makeText ( MainActivity.this ,
                                 "There is no package available in android" , Toast.LENGTH_LONG
                               ).show ( );
            }
        }
        if ( text.contains ( "open Chrome" ) ) {
            speak ( "opening chrome" );
            Intent a = getPackageManager ( ).getLaunchIntentForPackage ( "com.android.chrome" );
            if ( a != null ) {
                startActivity ( a );
            }
            else {
                Toast.makeText ( MainActivity.this ,
                                 "There is no package available in android" , Toast.LENGTH_LONG
                               ).show ( );
            }
        }
        if ( text.contains ( "open Spotify" ) ) {
            speak ( "opening Spotify" );
            Intent b = getPackageManager ( ).getLaunchIntentForPackage ( "com.spotify.music" );
            if ( b != null ) {
                startActivity ( b );
            }
            else {
                Toast.makeText ( MainActivity.this ,
                                 "There is no package available in android" , Toast.LENGTH_LONG
                               ).show ( );
            }
        }
        if ( text.contains ( "open maps" ) ) {
            speak ( "showing local area" );
            Intent c = getPackageManager ( ).getLaunchIntentForPackage ( "com.google.android.apps.maps" );
            if ( c != null ) {
                startActivity ( c );
            }
            else {
                Toast.makeText ( MainActivity.this ,
                                 "There is no package available in android" , Toast.LENGTH_LONG
                               ).show ( );
            }

        }
        //endregion


        //region website activities
        if ( text.contains ( "open Cartoon" ) ) {
            speak ( "launching, i recommend jimmy neutron" );
            Intent searching = new Intent ( );
            searching.setAction ( Intent.ACTION_VIEW );
            searching.addCategory ( Intent.CATEGORY_BROWSABLE );
            searching.setData ( Uri.parse ( "https://www.thewatchcartoononline.tv/anime/jimmy-neutron" ) );
            startActivity ( searching );

        }
        if ( text.contains ( "weather" ) ) {
            speak ( "local area weather" );
            Intent s = new Intent ( );
            s.setAction ( Intent.ACTION_VIEW );
            s.addCategory ( Intent.CATEGORY_BROWSABLE );
            s.setData ( Uri.parse ( "https://weather.com/" ) );
            startActivity ( s );

        }
        if ( text.contains ( "idea" ) ) {

            Intent intenti = new Intent ( MainActivity.this , ideatron.class );
            startActivity ( intenti );

        }
        //starts the object detection class
        if ( text.contains ( "eyes" ) ) {

            Intent intentl = new Intent ( MainActivity.this , eyes.class );
            startActivity ( intentl );

        }

        if ( text.contains ( "season 4" ) ) {
            speak ( "loading petition for season 4" );
            Intent t = new Intent ( );
            t.setAction ( Intent.ACTION_VIEW );
            t.addCategory ( Intent.CATEGORY_BROWSABLE );
            t.setData ( Uri.parse ( "http://chng.it/2pZdVbMPj5" ) );
            startActivity ( t );

        }

        if ( text.contains ( "phone" ) ) {
            speak ( "launching activity" );
            Intent p = new Intent ( );
            p.setAction ( Intent.ACTION_VIEW );
            p.addCategory ( Intent.CATEGORY_BROWSABLE );
            p.setData ( Uri.parse ( "https://makefreecallsonline.com/free-call/sent.php" ) );
            startActivity ( p );

        }
        //endregion


        //========================================================================================
//commands that close the application

        if ( text.contains ( "shut down" ) ) {

            MediaPlayer z = MediaPlayer.create ( MainActivity.this , R.raw.hip );
            z.start ( );
            finish ( );
            System.exit(0);

        }


        //bonus JimmyNeutron commands


        if ( text.contains ( "options" ) ) {

            String[] emo = new String[ 10 ];
            emo[ 0 ] = "drink water it is important to stay hydrated ";
            emo[ 1 ] = "lets learn something today say open chrome ";
            emo[ 2 ] = "lets watch something together simply say watch cartoon";
            emo[ 3 ] = "Build goddard a female poodle";
            emo[ 4 ] = "there are no options at this time";
            emo[ 5 ] = "do a page out of math book";
            emo[ 6 ] = "make new home brew wallpaper";
            emo[ 7 ] = "lets watch a cat video  say youtube ";
            emo[ 8 ] = " command option eight to be added";
            emo[ 9 ] = "command nine out of 10 to be added ";

            Random ran = new Random ( );
            int    en  = ran.nextInt ( emo.length );
            if ( text.contains ( "options" ) ) {
                speak ( " " + emo[ en ] );
            }


        }

        if ( text.contains ( "play dead" ) ) {
            speak ( "Danger Danger, You have initiated self destruct sequence alpha," +
                    "Self destruct sequence is now engaged " +
                    "This unit will yield a 50 megaton nuclear blast in exactly 10 seconds. " +
                    "Please clear a 30 square mile area. " +
                    "thank you and have a nice day" );
            Intent glitchscreen = new Intent ( this , glitchscreen.class );
            startActivity ( glitchscreen );
            System.exit ( 0 );

        }

    }
}

