
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
    
    switch(inByte) {
      case  'a': hBridge.left();
                 delay (1000);
                 break;
      case  'd': hBridge.right();
                 delay (1000);
                 break;
      case  'w': hBridge.forward();
                 delay (1000);
                 break;
      case  's': hBridge.backward();
                 delay (1000);
                 break;
    }  
    
    
    hBridge.stopAll();
  }
}
