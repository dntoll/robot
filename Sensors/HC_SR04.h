#ifndef HC_SR04_H
#define HC_SR04_H

#include "Sensor.h"

#define MAX_DISTANCE 200 
#define SAMPLES 3
#define DELAY_BETWEEN_SAMPLES 30

class HC_SR04 : public Sensor  {
  public:
    HC_SR04(int triggerPin, int echoPin) : 
      sonar(triggerPin, echoPin, MAX_DISTANCE) 
    {
      
    }
    
    float getRawMeasurement() {
      return (float)sonar.ping();// / (float)US_ROUNDTRIP_CM; // Send ping, get ping time in microseconds (uS).
    }
    
    float transformToCM(float a_raw) {
        return a_raw / (float)US_ROUNDTRIP_CM; // Send ping, get ping time in microseconds (uS).
    }
  
  private:
    NewPing sonar;
  
  
  
};

#endif
