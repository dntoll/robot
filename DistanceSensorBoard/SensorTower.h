#pragma once

#include "SharpSensor.h"

//SERVO
#define SERVO_PIN     3
#define MIN_DEGREES 7
#define MAX_DEGREES 161
#define SERVO_DELAY_TIME 45

//SHARP
#define SHARP_PIN1     0
#define SHARP_PIN2     1
#define SHARP_PIN3     2
#define SHARP_PIN4     4


class SensorTower {
  public:
    SensorTower()  {
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
          measure(pos, "sh", 40, 5);
       }
       Serial.println("DONE");
       delay(10);
       for(int pos = MAX_DEGREES; pos > MIN_DEGREES; pos -= 1)  // goes from 0 degrees to 180 degrees 
       { 
            moveServo(pos); // move it back slow...
       }
       m_servo.detach(); 
    }
    
    void calibrate(int measurements) {
      m_servo.attach(SERVO_PIN); 
      measure(MIN_DEGREES+5, "sh", 40, measurements);
      Serial.println("DONE");
      m_servo.detach(); 
    }
  private:
    void measure(int pos, const String &code, int delayMilliseconds, int numMeasurements) {
      moveServo(pos);
          
      for (int i = 0;i< numMeasurements; i++) {
        int visiblePos = map(m_servoPos, MIN_DEGREES, MAX_DEGREES, 0, 180);
        Serial.print(code);
        Serial.print(":");   
        Serial.print(visiblePos);
        Serial.print(":");       
 
  
        Serial.print(m_sensors[0].getRawMeasurement(A0));
        Serial.print(":");              
        Serial.print(m_sensors[1].getRawMeasurement(A1));
        Serial.print(":");              
        Serial.print(m_sensors[2].getRawMeasurement(A2));
        Serial.print(":");              
        Serial.print(m_sensors[3].getRawMeasurement(A3));
        Serial.println();
        delay(delayMilliseconds);
      }
    }
    
    void moveServo(int pos) {
      float distanceMoved = sqrt((pos - m_servoPos)*(pos - m_servoPos));
      
      m_servoPos = pos;
      m_servo.write(m_servoPos);
      delay(SERVO_DELAY_TIME * distanceMoved+SERVO_DELAY_TIME);
    }
    
    Servo m_servo;  // create servo object to control a servo 
    SharpSensor m_sensors[4];
    int m_servoPos;
    
};
