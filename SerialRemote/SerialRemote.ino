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

long lastUpdateTime =0;
void setup() {
  
  
  
  Serial.begin(57600); // Open serial monitor at 115200 baud to see ping results.
  Serial.println("Robot starting up");
  
  pRobot = new Robot();
  
  
  
  while (!Serial) {
    ; // wait for serial port to connect. Needed for Leonardo only
  }
  
  establishContact();
  
  lastUpdateTime = millis();
}

void establishContact() {
 
  Serial.println("DONE");
}


void loop() {
     
  if (Serial.available() > 0) {
    // get incoming byte:
    char inByte = Serial.read();
    int time = 500;
    

    
    switch(inByte) {
      case  'a': pRobot->left(time);
                 break;
      case  'd': pRobot->right(time);
                 break;
      case  'w': pRobot->forward(time);
                 break;
      case  's': pRobot->backward(time);
                 break;
      case  'g': pRobot->measureGyro();
                 break;
      case  'c': pRobot->measureCompass();
                 break;
      case  't': pRobot->measureTemperature();
                 break;    
      case  'n': pRobot->calibrate();
                 break;   
      case  'z': establishContact();
                 break;           
      default : 
             ;      
                
    }
    
    
  } else if (millis() > lastUpdateTime + 500) {
    pRobot->measureGyro();
    pRobot->measureCompass();
    pRobot->measureTemperature();
    lastUpdateTime = millis();
  }
}
