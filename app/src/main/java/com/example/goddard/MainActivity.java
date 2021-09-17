package com.example.goddard;

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
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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


public class MainActivity extends AppCompatActivity{


    private TextToSpeech tts;
    private ArrayList<String> questions;
    private String name, surname, age, asName;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private static final String PREFS = "prefs";
    private static final String NEW = "new";
    private static final String NAME = "name";
    private static final String AGE = "age";
    private static final String AS_NAME = "as_name";
    private static final String build = "built";
    private static final String daisy = "day";
    private static final String device = "devicever";
    private static final String maybuild = "may";
public String msg;


    EditText editText;
    ImageView imageView;
    String model = Build.MODEL;
    //request permission for goddard to use microphone
    public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    private final static String default_notification_channel_id = "default" ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //region notify

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(MainActivity. this,
                default_notification_channel_id )
                .setSmallIcon(R.drawable. logo__1_ )
                .setContentTitle( "Goddard.va" )

                .setContentText( "Goddard.va uses your microphone" );
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context. NOTIFICATION_SERVICE ) ;
        if (Build.VERSION. SDK_INT >= Build.VERSION_CODES. O ) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes. CONTENT_TYPE_SONIFICATION )
                    .setUsage(AudioAttributes. USAGE_ALARM )
                    .build() ;
            int importance = NotificationManager. IMPORTANCE_HIGH ;
            NotificationChannel notificationChannel = new
                    NotificationChannel( NOTIFICATION_CHANNEL_ID , "NOTIFICATION_CHANNEL_NAME" , importance) ;
            notificationChannel.enableLights( true ) ;
            notificationChannel.setLightColor(Color. RED ) ;
            notificationChannel.enableVibration( true ) ;
            notificationChannel.setVibrationPattern( new long []{ 100 , 200 , 300 , 400 , 500 , 400 , 300 , 200 , 400 }) ;

            mBuilder.setChannelId( NOTIFICATION_CHANNEL_ID ) ;
            assert mNotificationManager != null;
            mNotificationManager.createNotificationChannel(notificationChannel) ;

        assert mNotificationManager != null;
        mNotificationManager.notify(( int ) System. currentTimeMillis (), mBuilder.build()) ;
    }
        //endregion

        findViewById(R.id.settingsbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent settingsrocket = new Intent(this, Settingsrocket.class);
//                startActivity(settingsrocket);


            }
        });






        preferences = getSharedPreferences(PREFS,0);
        editor = preferences.edit();

       findViewById(R.id.microphoneButton).setOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View v) {

                listen();

            }
       });
        loadQuestions();


        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = tts.setLanguage(Locale.US);

                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "This Language is not supported");
                    }

                    MediaPlayer player= MediaPlayer.create(MainActivity.this,R.raw.barkbark);
                    player.start();

                } else {
                    Log.e("TTS", "Initialization Failed!");
                }
            }
        });

    }


    private void loadQuestions(){
        questions = new ArrayList<>();
        questions.clear();
        //if hello is said
        questions.add("Hello, what is your name?");

        questions.add("How old are you?");


    }

    private void listen(){
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Waiting on input");

        try {
            startActivityForResult(i, 100);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(MainActivity.this, "Your device doesn't support Speech Recognition", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    private void speak(String text){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);

        }else{
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100){
            if (resultCode == RESULT_OK && null != data) {
                ArrayList<String> res = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                String inSpeech = res.get(0);
                recognition(inSpeech);
            }
        }
    }

    private void recognition(String text){
        Log.e("Speech",""+text);
        String[] speech = text.split(" ");
        if(text.contains("hello")){
            speak(questions.get(0));
        }


        if(text.contains("my name is")){
            name = speech[speech.length-1];
            Log.e("THIS", "" + name);
            editor.putString(NAME,name).apply();
            speak(questions.get(2));
        }

        if(text.contains("years") && text.contains("old")){
            String age = speech[speech.length-3];
            Log.e("THIS", "" + age);
            editor.putString(AGE, age).apply();
        }


        if(text.contains("your name")){
            String as_name = preferences.getString(AS_NAME,"");
            if(as_name.equals(""))
                speak("What do you want to name me?");
            else
                speak("My name is "+as_name);
        }
        if(text.contains("call you")){
            String name = speech[speech.length-1];
            editor.putString(AS_NAME,name).apply();
            speak("if that what you desire to call me"+preferences.getString(NAME,null));
        }
        //region basic commands
        if(text.contains("what is my name")){
            speak("If I recall "+preferences.getString(NAME,null));
        }

        //==============
        //extra memory commands
        //==============
        if(text.contains("build version")){
            String building = preferences.getString(build,"");
            if(building.equals(""))
                speak("When was i built?");
            else
                speak("current build version in contrast to initial prototype "+building);
        }
        //
        if(text.contains("implemented")){
            String ver = speech[speech.length -1];
            editor.putString(build,ver).apply();
            speak("saving "+preferences.getString(build,null));
        }
        if(text.contains("Hardware")){
            speak("Current device "+model);
        }

        if(text.contains("thank you")){
            speak("you're welcome"+ preferences.getString(NAME, null));
        }
        //age command
        if(text.contains("how old am I")){
            speak("You are "+preferences.getString(AGE,null)+" years old.");
        }
//=================== daisy daisy
//
//
//


        if(text.contains("hello world")){
            String bi = preferences.getString(daisy,"");
            if(bi.equals(""))
                speak("Testing memory");
            else
                speak("Daisy,Daisy,give me your answer " +bi);

        }
        //
        if(text.contains("yes")){
            String world = speech[speech.length -1];
            editor.putString(daisy,world).apply();
            speak("saving test "+preferences.getString(daisy,null));//do
        }






//
// ================================================




        //good morning command
        if(text.contains("good morning")){
            speak("good morning char-dee-lease");
        }
        if(text.contains("good night")){
            speak("good night char-dee-lease");
            finish();
        }

        // time command
        if(text.contains("what time is it")){
            SimpleDateFormat sdfDate = new SimpleDateFormat("HH:mm a");//dd/MM/yyyy
            Date now = new Date();


            String[] strDate = sdfDate.format(now).split(":");
            if(strDate[1].contains("00"))
                strDate[1] = "o'clock";
            speak("The time is " + sdfDate.format(now));


        }
        if(text.contains("what day is it")){
            SimpleDateFormat sdfDates = new SimpleDateFormat("EEE");
            Date nows = new Date();
            String[] strDate = sdfDates.format(nows).split(":");
            speak("The day  is " + sdfDates.format(nows) + "day");


        }




        //endregion



        //region show off commands
        if(text.contains("introduce yourself")){
            speak("hello, my name is goddard and i am an ongoing AI project based on a cartoon i was conceptualised " +
                    "in c sharp  i have been migrate to java for ease of use with mobile devices  " +
                    "as well as the tablet that runs my program....like so... i hope to meet everyone one day thankyou  ");
        }
        if(text.contains("speak"))
        {
            MediaPlayer player= MediaPlayer.create(MainActivity.this,R.raw.barkbark);
            player.start();
        }
        if(text.contains("sing")){
            MediaPlayer music= MediaPlayer.create(MainActivity.this,R.raw.song);
            music.start();
        }
        if(text.contains("thank you boy")){
            speak("you're welcome char-dee-lease");
        }

        if(text.contains("who's a good boy")){
            MediaPlayer player= MediaPlayer.create(MainActivity.this,R.raw.barkbark);
            player.start();
        }
        if(text.contains("tell your fans a joke")){
            speak("ok what was the exercising avocado worried about");
        }
        if(text.contains("continue")){
            speak("working on his core.. this is where you laugh");
        }
        //endregion




        //region open app activities
        if(text.contains("open YouTube")){
            speak("opening youtube");
            Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.google.android.youtube");
            if (launchIntent != null) {
                startActivity(launchIntent);
            } else {
                Toast.makeText(MainActivity.this,
                        "There is no package available in android", Toast.LENGTH_LONG).show();
            }
        }
        if(text.contains("open Chrome")){
            speak("opening chrome");
            Intent a = getPackageManager().getLaunchIntentForPackage("com.android.chrome");
            if (a!= null) {
                startActivity(a);
            } else {
                Toast.makeText(MainActivity.this,
                        "There is no package available in android", Toast.LENGTH_LONG).show();
            }
        }
        if(text.contains("open Spotify")){
            speak("opening Spotify");
            Intent b = getPackageManager().getLaunchIntentForPackage("com.spotify.music");
            if (b!= null) {
                startActivity(b);
            } else {
                Toast.makeText(MainActivity.this,
                        "There is no package available in android", Toast.LENGTH_LONG).show();
            }
        }
        if(text.contains("open maps")){
            speak("showing local area");
            Intent c = getPackageManager().getLaunchIntentForPackage("com.google.android.apps.maps");
            if (c!= null) {
                startActivity(c);
            } else {
                Toast.makeText(MainActivity.this,
                        "There is no package available in android", Toast.LENGTH_LONG).show();
            }

        }
        //endregion



        //region website activities
        if(text.contains("open Cartoon")){
            speak("launching, i recommend jimmy neutron");
            Intent searching = new Intent();
            searching.setAction(Intent.ACTION_VIEW);
            searching.addCategory(Intent.CATEGORY_BROWSABLE);
            searching.setData(Uri.parse("https://www.thewatchcartoononline.tv/anime/jimmy-neutron"));
            startActivity(searching);

        }
        if(text.contains("weather")){
            speak("local area weather");
            Intent s = new Intent();
            s.setAction(Intent.ACTION_VIEW);
            s.addCategory(Intent.CATEGORY_BROWSABLE);
            s.setData(Uri.parse("https://www.koco.com/weather"));
            startActivity(s);

        }
        if(text.contains("open Github")){
            Intent v = new Intent();
            v.setAction(Intent.ACTION_VIEW);
            v.addCategory(Intent.CATEGORY_BROWSABLE);
            v.setData(Uri.parse("https://github.com/"));
            startActivity(v);

        }
        if(text.contains("season 4")){
            speak("loading petition for season 4");
            Intent t = new Intent();
            t.setAction(Intent.ACTION_VIEW);
            t.addCategory(Intent.CATEGORY_BROWSABLE);
            t.setData(Uri.parse("http://chng.it/2pZdVbMPj5"));
            startActivity(t);

        }

        if(text.contains("phone")){
            speak("launching activity");
            Intent p = new Intent();
            p.setAction(Intent.ACTION_VIEW);
            p.addCategory(Intent.CATEGORY_BROWSABLE);
            p.setData(Uri.parse("https://makefreecallsonline.com/free-call/sent.php"));
            startActivity(p);

        }
        //endregion



        //========================================================================================
//commands that close the application

        if(text.contains("shut down")){

            MediaPlayer z = MediaPlayer.create(MainActivity.this,R.raw.hip);
            z.start();
            finish();

        }


        //bonus lol
        if(text.contains("play dead")){
            speak("Danger Danger, You have initiated self destruct sequence alpha," +
                    "Self destruct sequence is now engaged " +
                    "This unit will yield a 50 megaton nuclear blast in exactly 10 seconds. " +
                    "Please clear a 30 square mile area. " +
                    "thank you and have a nice day");
            Intent glitchscreen = new Intent(this, glitchscreen.class);
            startActivity(glitchscreen);
System.exit(0);

        }
    }
}

