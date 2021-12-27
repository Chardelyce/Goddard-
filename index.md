![image](https://user-images.githubusercontent.com/63970461/147498762-0fcfd903-ba16-4472-884a-0696157b3cc1.png)
![image](https://user-images.githubusercontent.com/63970461/147498768-57c43921-a903-46d2-b9b1-70c184476216.png)

<p>&nbsp; &nbsp;&nbsp;</p>
<p><br></p>
<p>&quot;<strong><em>Because a super powered mind will always need a mechanical canine</em></strong>&quot; :)</p>



<p><br></p>
<p>Presenting Goddard.va , and ofc Goddard himself; a project started in March 2021 rapidly approaching his finish date (for the physical body that is ) on new years! This mechanical good boy has underwent countless iterations whether that be physically from the prototype to the final body or even the AI itself which is what this application is.</p>
<p><br></p>
<p>While not quite able to do &quot;101 million things &quot; like his fantasy counter part he is able to do 30 - 40 things as of current and more is being added every day ^O^.<p>


    Here is some of the code that is used to recong speech :) with this its easy to make a voice assitant ofc you'll need a few libaries some of theextra imports you see are from things i used haha 
    
 ![image](https://user-images.githubusercontent.com/63970461/147501295-d310a18d-407a-4b03-8488-202cfea73ccc.png)

    
    
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
                    int result     = tts.setLanguage ( Locale.UK );

                    int greenbeans = tts.setPitch ( - 8.0f );

                    Voice voiceobj = new Voice("en-us-x-sfg#male_2-local",//
                            Locale.getDefault(), 1, 1, false, null);
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
   

 

    
  


<p><br></p>
    
    
<p><br></p>
<p id="isPasted" style="text-align: center;">The Adventures of Jimmy Neutron,</p>
<p style="text-align: center;">&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;Boy Genius and all related logos,titles and characters are trademarks of Nickeloden,</p>
<p style="text-align: center;">&nbsp; &nbsp; &nbsp; &nbsp; a programming service of Viacom International Inc</p>
<p><br></p>
