#include <Servo.h> 
#include <Arduino.h>

#include "SensorTower.h"

SensorTower *pRobot;

void setup() {
  pRobot = new SensorTower();
  
  Serial.begin(57600); 
  
  
  while (!Serial) {
    ; // wait for serial port to connect. Needed for Leonardo only
  }
  
  establishContact();
  
  
}

void establishContact() {
 
  Serial.println("DONE");
}


void loop() {
     
  if (Serial.available() > 0) {
    // get incoming byte:
    char inByte = Serial.read();
    int time = 250;
    

    
    switch(inByte) {
      case  'z': establishContact();
                 break; 
      case  '5': pRobot->sweep();
      default : 
              ;//  Serial.println("unknown character");      
                
    }
    
    
  }
}
