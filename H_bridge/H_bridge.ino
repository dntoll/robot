
#include "L298N.h"


L298N hBridge = L298N();
void setup()
{
  Serial.begin(57600);
  Serial.println("Two motors");

  
  while (!Serial) {
    ; // wait for serial port to connect. Needed for Leonardo only
  }

  pinMode(2, INPUT);   // digital sensor is on digital pin 2
  establishContact(); 
}


void establishContact() {
  while (Serial.available() <= 0) {
    Serial.print('A');   // send a capital A
    delay(300);
  }
}

void loop()
{
  
 
 
  if (Serial.available() > 0) {
    // get incoming byte:
    char inByte = Serial.read();
    int time = 200;
    
    switch(inByte) {
      case  'a': hBridge.left();
                 delay (time);
                 break;
      case  'd': hBridge.right();
                 delay (time);
                 break;
      case  'w': hBridge.forward();
                 delay (time);
                 break;
      case  's': hBridge.backward();
                 delay (time);
                 break;
    }  
    
    
    hBridge.stopAll();
  }
}
