#include <NewPing.h>

#include "Sensor.h"
#include "HC_SR04.h"
#include "SharpSensor.h"
#include <Servo.h> 
#include "L298N.h"
#include <Wire.h>
#include <HMC5883L.h>
#include "Compass.h"
#include "MPU6050.h"
#include "Robot.h"




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
  while (Serial.available() <= 0) {
    Serial.print('A');   // send a capital A
    delay(300);
  }
}


void loop() {
      
  if (Serial.available() > 0) {
    // get incoming byte:
    char inByte = Serial.read();
    int time = 200;
    
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
                 
                
    }
    
    
  }
}
