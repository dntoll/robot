//#include <NewPing.h>
#include <Servo.h> 
#include <Arduino.h>

#include "SensorTower.h"

SensorTower *pRobot;

void setup() {
  Serial.begin(57600); // Open serial monitor at 115200 baud to see ping results.
  //Serial.println("Sensor Tower starting up");
  
  pRobot = new SensorTower();
  
  
  
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
      case  'm': pRobot->measureDistance(90);
                 break;
      case  'q': pRobot->sonarSweep();
                 break;
      case  'i': pRobot->irSweep();
                 break;  
      case  'p': pRobot->calibrateIR();
                 break;  
      case  'z': establishContact();
                 break; 
      case  '1': Serial.println(pRobot->sensor(0));
                 break;           
      case  '2': Serial.println(pRobot->sensor(1));
                 break;           
      case  '3': Serial.println(pRobot->sensor(2));
                 break;           
      case  '4': Serial.println(pRobot->sensor(3));
                 break;  
      case  '5': pRobot->sweep();
      default : 
              ;//  Serial.println("unknown character");      
                
    }
    
    
  }
}