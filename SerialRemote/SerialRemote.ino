#define COMPASS
#define GYRO


#include <NewPing.h>

#include <Servo.h> 
#ifdef COMPASS
  #include <Wire.h>
  #include <HMC5883L.h>
#endif

#include "Robot.h"
#include "L298N.h"


Robot *pRobot;

void setup() {
  
  
  
  Serial.begin(57600); // Open serial monitor at 115200 baud to see ping results.
  Serial.println("Robot starting up");
  
  pRobot = new Robot();
  
  
  
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
      case  'a': pRobot->left(time);
                 break;
      case  'd': pRobot->right(time);
                 break;
      case  'w': pRobot->forward(time);
                 break;
      case  's': pRobot->backward(time);
                 break;
      case  'm': pRobot->measureDistance(90);
                 break;
      case  'g': pRobot->measureGyro();
                 break;
      case  'c': pRobot->measureCompass();
                 break;
      case  'q': pRobot->sonarSweep();
                 break;
      case  'i': pRobot->irSweep();
                 break;  
      case  't': pRobot->measureTemperature();
                 break;    
      case  'n': pRobot->calibrate();
                 break;   
      case  'p': pRobot->calibrateIR();
                 break;  
      case  'z': establishContact();
                 break;           
      default : 
              ;//  Serial.println("unknown character");      
                
    }
    
    
  }
}
