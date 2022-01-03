//This code is to demonstrate the use of a continuous rotation servo motor with its different functions
//Refer to surtrtech.com to understand further

#include <Servo.h> //Servo library

Servo myservo;  //Servo name is myservo
  

void setup() {
  Serial.begin(9600);
  myservo.attach(6);  // attaches the servo signal pin on pin D6

}

void loop() {
  Serial.println("whats this");// You can display on the serial the signal value
  myservo.write(0); //Turn clockwise at high speed
  delay(1000);
  myservo.detach();//Stop. You can use deatch function or use write(x), as x is the middle of 0-180 which is 90, but some lack of precision may change this value
  delay(1000);
  myservo.attach(6);//Always use attach function after detach to re-connect your servo with the board
  Serial.println("hello there");//Turn left high speed
  myservo.write(360);
  delay(1000);
  myservo.detach();//Stop
  delay(1000);
  myservo.attach(6);


//myservo.write(92); //Used in the tutorial video, 92 was my stop value 
      
}
