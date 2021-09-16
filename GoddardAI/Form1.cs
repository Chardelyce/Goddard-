using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using System.Speech.Recognition;
using System.Speech.Synthesis;
using System.IO;
using System.Diagnostics;
using hellobox;


namespace Tutorial_AI
{
    public partial class Form1 : Form
    {

        //the greenscreen 


        public bool wake = false;
        public Form1()
        {
            InitializeComponent();
            // Setup the timer
           
        }



        SpeechSynthesizer s = new SpeechSynthesizer();
        SpeechRecognitionEngine sr = new SpeechRecognitionEngine();
        PromptBuilder pb = new PromptBuilder();

        private void Form1_Load(object sender, EventArgs e)
        {
            s.SelectVoiceByHints(VoiceGender.Male);
            Choices list = new Choices();
            list.Add(File.ReadAllLines(@"G:\goddardai.txt"));

            Grammar gm = new Grammar(new GrammarBuilder(list));

            try
            {
                sr.RequestRecognizerUpdate();
                sr.LoadGrammar(gm);
                sr.SpeechRecognized += Sr_SpeechRecognized;
                sr.SetInputToDefaultAudioDevice();
                sr.RecognizeAsync(RecognizeMode.Multiple);
            }
            catch
            {
                return;
            }
        }

        public void Say(string phrase)
        {
            s.SpeakAsync(phrase);
            wake = false;
        }



        private void Sr_SpeechRecognized(object sender, SpeechRecognizedEventArgs e)
        {
            string speechSaid = e.Result.Text;

            if (speechSaid == "Goddard")
            {
                System.Media.SoundPlayer player = new System.Media.SoundPlayer(@"G:\barkbark.wav");

                player.Play();
                wake = true;
            }

            if (wake)
            {
                switch (speechSaid)
                {
                    case ("hello"):
                        Say("hello, shar-dee-lease");
                        break;

                    case ("game"):
                        Say("presenting see-m-dee craft");
                        
                        break;

                    case ("how are you doing"):
                        Say("good, build me a poodle ");
                        break;

                    case ("watch"):
                        Say("i suggest jimmy neutron");
                        Process.Start("https://www.thewatchcartoononline.tv/");
                        break;
                    case ("talk"):
                        Say("hello, my name is goddard and i am an ongoing AI project based on a cartoon");
                        break;
                    case ("speak"):
                        System.Media.SoundPlayer player = new System.Media.SoundPlayer(@"G:\barkbark.wav");
                        player.Play();
                        break;
                    case ("wake mode"):
                        Say("good morning shar-dee-lease");
                        break;

                    case ("slept well"):
                        Say(" i only power off for your amusement ");
                        break;
                    case ("intro"):
                        Say(" hello , friends and best-tease on snapchat i hope all of you can meet me and see me in action some day  ");
                        break;

                    case ("outside"):
                        Say(" yes, here is the weather  ");
                        Process.Start("https://www.msn.com/en-us/weather/today/weather-today/we-city?el=nA8ucr5H0d%2BbwGT3wozP%2FQ%3D%3D&ocid=ansmsnweather");
                        break;

                    case ("google"):
                        Say("opening google");
                        Process.Start("https://www.google.com");
                        break;

                    case ("defense"):
                        s.Speak("Danger Danger, You have initated self destruct sequence alpha,Self destruct sequence is now engaged This unit will yield a 50 megaton nuclear blast in exactly 10 seconds. Please clear a 30 square mile area. thank you and have a nice day    ");
                        Application.Exit();
                        break;

                    case ("close"):
                        Say("saving state do not remove memory card");
                        SendKeys.Send("%{F4}");
                        break;

                    
                }

            }


        }

        private void button1_Click(object sender, EventArgs e)
        {

        }
    }
}

