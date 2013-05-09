#include <NewPing.h>
#include "Sensor.h"
#include "HC_SR04.h"
#include "SharpSensor.h"
#include <Servo.h> 
#include "L298N.h"
#include <Wire.h>
#include <HMC5883L.h>
#include "Compass.h"



#define TRIGGER_PIN   16 
#define ECHO_PIN      15  
#define SERVO_PIN     11
#define SHARP_PIN     0
#define SHARP_PIN     0


Servo myservo;  // create servo object to control a servo 
SharpSensor sharp(SHARP_PIN);
HC_SR04 sonar(TRIGGER_PIN, ECHO_PIN);
L298N hBridge = L298N();
Compass compass;

void setup() {
  Serial.begin(57600); // Open serial monitor at 115200 baud to see ping results.
  Serial.println("Robot starting up");
  
  while (!Serial) {
    ; // wait for serial port to connect. Needed for Leonardo only
  }
  

  Serial.println("Constructing new HMC5883L");
  compass = Compass();
  
  Serial.println("Initiating Servo");
  myservo.attach(SERVO_PIN);  // attaches the servo on pin 9 to the servo object 
  myservo.write(90);
  
  establishContact(); 
  
}

void establishContact() {
  while (Serial.available() <= 0) {
    Serial.print('A');   // send a capital A
    delay(300);
  }
}

void mes() {
  
  float sharpMeasurement, sharpcm, sonarMeasurement = 0, sonarcm = 0;
  sharpMeasurement = sharp.getMedian(5,1);
  sharpcm = sharp.transformToCM(sharpMeasurement);
 
 
  sonarMeasurement = sonar.getMedian(5,30);
  sonarcm = sonar.transformToCM(sonarMeasurement);
  
  Serial.print(sharpMeasurement);
      Serial.print(" IR CM : ");
      Serial.print(sharpcm);
      Serial.print(" Sonar CM : ");
      Serial.print(sonarcm);
      
      Serial.print(" Sharp E : ");
      Serial.print((sonarcm - sharpcm) * (sonarcm - sharpcm));
      Serial.println();
}

void loop() {
      
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
      case  'm': mes();
                 delay (time);
                 break;
    }  
    
    
    hBridge.stopAll();
  }
}
