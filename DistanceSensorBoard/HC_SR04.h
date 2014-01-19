

#ifndef HC_SR04_H
#define HC_SR04_H


#include "Sensor.h"

#define MAX_DISTANCE 200 
#define SAMPLES 3
#define DELAY_BETWEEN_SAMPLES 30

class HC_SR04 : public Sensor  {
  public:
    HC_SR04(int triggerPin, int echoPin) //: 
      //sonar(triggerPin, echoPin, MAX_DISTANCE) 
    {
      trigger = triggerPin;
      echo = echoPin;
      pinMode(trigger, OUTPUT);
      pinMode(echo, INPUT);
      digitalWrite(trigger, LOW);
    }
    
    float getRawMeasurement() {
      return distance();//(float)sonar.ping();
    }
    
    float transformToCM(float a_raw) {
        return a_raw;//a_raw / (float)US_ROUNDTRIP_CM;
    }
    
    
  
  private:
    //NewPing sonar;
    
    long microsecondsToCentimeters(long microseconds)
    {
      return microseconds / 29 / 2;
    }
    
    float distance() {
      long duration, inches, cm;
    
      digitalWrite(trigger, LOW);
      delayMicroseconds(2);
      digitalWrite(trigger, HIGH);
      delayMicroseconds(5);
      digitalWrite(trigger, LOW);
    
      duration = pulseIn(echo, HIGH);
    
      cm = microsecondsToCentimeters(duration);
      
      /*if (cm > MAX_DISTANCE) {
        return MAX_DISTANCE;
      }*/
      return cm;
    }
  
    int trigger;
    int echo;
  
};

#endif
