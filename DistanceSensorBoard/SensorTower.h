#pragma once

#include "HC_SR04.h"
#include "SharpSensor.h"

//SERVO
#define SERVO_PIN     3
#define MIN_DEGREES 20
#define MAX_DEGREES 160
#define SERVO_DELAY_TIME 80

//SONAR
#define SONAR1_ENABLE_PIN 4
#define SONAR1_TRIGGER_PIN 5 
#define SONAR1_ECHO_PIN 6  
#define SONAR1_GND_PIN 7

//SONAR 2
#define SONAR2_ENABLE_PIN 8
#define SONAR2_TRIGGER_PIN 9 
#define SONAR2_ECHO_PIN 10  
#define SONAR2_GND_PIN 11

//SHARP
#define SHARP_PIN1     0
#define SHARP_PIN2     1

//ENABLERS
#define SHARP1_ENABLE_PIN 16
#define SHARP2_ENABLE_PIN 17


class SensorTower {
  public:
    SensorTower() : m_sharp1(SHARP_PIN1), m_sharp2(SHARP_PIN2), m_sonar1(SONAR1_TRIGGER_PIN, SONAR1_ECHO_PIN), m_sonar2(SONAR2_TRIGGER_PIN, SONAR2_ECHO_PIN)  {
     
     pinMode(SONAR1_GND_PIN, OUTPUT); 
     pinMode(SONAR2_GND_PIN, OUTPUT); 
     pinMode(SHARP1_ENABLE_PIN, OUTPUT); 
     pinMode(SHARP2_ENABLE_PIN, OUTPUT); 
     pinMode(SONAR1_ENABLE_PIN, OUTPUT); 
     pinMode(SONAR2_ENABLE_PIN, OUTPUT);  
     
     
     digitalWrite(SONAR1_GND_PIN, LOW);
     digitalWrite(SONAR2_GND_PIN, LOW);
      
      m_servoPos = 90;
      m_servo.attach(SERVO_PIN);  
      
   //   m_servo.write(m_servoPos);
     
     
     
      
    shutdownAll();
      
     
    }
    
    ~SensorTower() {
     
    }
    
    void sonarSweep() {
       Serial.print("Servo:Sonar");

       int minAngle = MIN_DEGREES;
       int maxAngle = MAX_DEGREES;
       float sonarMeasurement;
       float sonarcm;
       
       shutdownAll();
       startUpSensor(SONAR2_ENABLE_PIN);
       for(int pos = minAngle; pos <= maxAngle; pos += 5)  // goes from 0 degrees to 180 degrees 
       { 
          moveServo(pos);
          sonarMeasurement = m_sonar2.getMedian(5, 30);
          sonarcm = m_sonar2.transformToCM(sonarMeasurement);
          
          Serial.print(":");
          Serial.print((m_servoPos+180)%360);
          Serial.print(":");
          Serial.print(sonarcm);
       }
       shutdownAll();
       startUpSensor(SONAR1_ENABLE_PIN);
       for(int pos = minAngle; pos <= maxAngle; pos += 5)  // goes from 0 degrees to 180 degrees 
       { 
          moveServo(pos);
          sonarMeasurement = m_sonar1.getMedian(5, 30);
          sonarcm = m_sonar1.transformToCM(sonarMeasurement);
          
          Serial.print(":");
          Serial.print(m_servoPos);
          Serial.print(":");
          Serial.print(sonarcm);
       }
       Serial.println("");
       
       moveServo(90);
    }
    
    
    
    void calibrateIR() {
      Serial.print("Calibration");
      while (Serial.available() <= 0) {
        measureDistance(90);
        delay(300);
      }
    }
    
    void irSweep() {
       Serial.print("Servo:Sharp");
       int minAngle = MIN_DEGREES;
       int maxAngle = MAX_DEGREES;
       float sharpMeasurement, sharpcm, sonarMeasurement = 0, sonarcm = 0;
       
       shutdownAll();
       startUpSensor(SHARP1_ENABLE_PIN);
       for(int pos = maxAngle; pos >= minAngle; pos -= 1)  // goes from 0 degrees to 180 degrees 
       { 
          moveServo(pos);
          sharpMeasurement = m_sharp1.getMedian(10,1);
          sharpcm = m_sharp1.transformToCM(sharpMeasurement);
          
          Serial.print(":");
          Serial.print(m_servoPos);
          Serial.print(":");
          Serial.print(sharpcm);
       }  
       
       shutdownAll();
       startUpSensor(SHARP2_ENABLE_PIN);
       
       for(int pos = maxAngle; pos >= minAngle; pos -= 1)  // goes from 0 degrees to 180 degrees 
       { 
          moveServo(pos);
          
          sharpMeasurement = m_sharp2.getMedian(10,1);
          sharpcm = m_sharp2.transformToCM(sharpMeasurement);
          
          Serial.print(":");
          Serial.print((m_servoPos+180)%360);
          Serial.print(":");
          Serial.print(sharpcm);
       }
       Serial.println("");
       
       moveServo(90);
    }
    
    void measureDistance(int a_pos) {
      float sharpMeasurement, sharpcm, sonarMeasurement = 0, sonarcm = 0;
      
      Serial.println("Sharp:Sonar");
      moveServo(a_pos);
      
      
      shutdownAll();
      startUpSensor(SHARP1_ENABLE_PIN);
      
      sharpMeasurement = m_sharp1.getMedian(5,100);
      sharpcm = m_sharp1.transformToCM(sharpMeasurement);
      Serial.print("s1:");
      Serial.println(sharpcm, 1);
      
      shutdownAll();
      startUpSensor(SONAR1_ENABLE_PIN);
      sonarMeasurement = m_sonar1.getMedian(5,30);
      sonarcm = m_sonar1.transformToCM(sonarMeasurement);
      Serial.print("u1:");
      Serial.println(sonarcm, 1);
      
      shutdownAll();
      startUpSensor(SHARP2_ENABLE_PIN);
      sharpMeasurement = m_sharp2.getMedian(5,100);
      sharpcm = m_sharp2.transformToCM(sharpMeasurement);
      Serial.print("s2:");
      Serial.println(sharpcm, 1);
      
      
      
      shutdownAll();
      startUpSensor(SONAR2_ENABLE_PIN);
      sonarMeasurement = m_sonar2.getMedian(5,30);
      sonarcm = m_sonar2.transformToCM(sonarMeasurement);
      Serial.print("u2:");
      Serial.println(sonarcm, 1);
      
      Serial.println("");
      
    }
    
   
    
    
  private:
    
    
    void moveServo(int pos) {
      m_servoPos = pos;
      m_servo.write(m_servoPos);
      delay(SERVO_DELAY_TIME);
    }

    
    
    void startUpSensor(int sensor) {
      digitalWrite(sensor, HIGH);
      
      delay(100);
      
      if (SHARP1_ENABLE_PIN == sensor || SHARP2_ENABLE_PIN == sensor) {
         delay(38+10+6);
         
         analogRead(sensor);
      } else {
        delay(55);
      }

    }
    
    void shutdownAll() {
      digitalWrite(SHARP1_ENABLE_PIN, LOW);
      digitalWrite(SHARP2_ENABLE_PIN, LOW);
      digitalWrite(SONAR1_ENABLE_PIN, LOW);
      digitalWrite(SONAR2_ENABLE_PIN, LOW);
      delay(3);
    }
    
    Servo m_servo;  // create servo object to control a servo 
    SharpSensor m_sharp1;
    SharpSensor m_sharp2;
    HC_SR04 m_sonar1;
    HC_SR04 m_sonar2;
    int m_servoPos;
    
};
