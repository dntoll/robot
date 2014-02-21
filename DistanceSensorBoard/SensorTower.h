#pragma once

#include "HC_SR04.h"
#include "SharpSensor.h"

//SERVO
#define SERVO_PIN     3
#define MIN_DEGREES 7
#define MAX_DEGREES 161
#define SERVO_DELAY_TIME 45

//SONAR
#define SONAR1_TRIGGER_PIN 5 
#define SONAR1_ECHO_PIN 6  


//SONAR 2
#define SONAR2_TRIGGER_PIN 9 
#define SONAR2_ECHO_PIN 10  


//SHARP
#define SHARP_PIN1     0
#define SHARP_PIN2     1


class SensorTower {
  public:
    SensorTower() : m_sharp1(SHARP_PIN1), m_sharp2(SHARP_PIN2), m_sonar1(SONAR1_TRIGGER_PIN, SONAR1_ECHO_PIN), m_sonar2(SONAR2_TRIGGER_PIN, SONAR2_ECHO_PIN)  {
      m_servo.attach(SERVO_PIN);       
      moveServo(MIN_DEGREES+5);
      m_servo.detach(); 
    }
    
    ~SensorTower() {
     
    }
    

    
    void sweep() {
       m_servo.attach(SERVO_PIN); 
       for(int pos = MIN_DEGREES; pos <= MAX_DEGREES; pos += 1)  // goes from 0 degrees to 180 degrees 
       { 
          measure(pos, m_sharp1, m_sharp2, "sh", 40);
       }
       Serial.println("DONE");
       delay(10);
       for(int pos = MAX_DEGREES; pos > MIN_DEGREES; pos -= 1)  // goes from 0 degrees to 180 degrees 
       { 
//          measure(pos, m_sharp1, m_sharp2, "sh", 40);
            moveServo(pos); // move it back slow...
       }
      // Serial.println("DONE");
       m_servo.detach(); 
    }
    
    void measure(int pos, Sensor &front, Sensor &back, const String &code, int delayMilliseconds) {
      moveServo(pos);
          
      for (int i = 0;i< 3; i++) {
        int visiblePos = map(m_servoPos, MIN_DEGREES, MAX_DEGREES, 0, 180);
        Serial.print(code);
        Serial.print(":");   
        Serial.print(visiblePos);
        Serial.print(":");              
        Serial.print(front.measureCM());
        Serial.print(":");              
        Serial.print(back.measureCM());
        Serial.println();
        delay(delayMilliseconds);
      }
    }
  private:
    
    
    void moveServo(int pos) {
      float distanceMoved = sqrt((pos - m_servoPos)*(pos - m_servoPos));
      
      m_servoPos = pos;
      m_servo.write(m_servoPos);
      delay(SERVO_DELAY_TIME * distanceMoved+SERVO_DELAY_TIME);
    }
    
    Servo m_servo;  // create servo object to control a servo 
    SharpSensor m_sharp1;
    SharpSensor m_sharp2;
    HC_SR04 m_sonar1;
    HC_SR04 m_sonar2;
    int m_servoPos;
    
};
