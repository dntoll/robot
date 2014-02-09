#ifndef SHARP_SENSOR
#define SHARP_SENSOR

#include "Sensor.h"

class SharpSensor : public Sensor {
  public:
     SharpSensor(int a_analogPin) {
        m_analogPin = a_analogPin;
     }
     
     static const int minDistance = 16;
  
  
  float getRawMeasurement() {
    
      analogRead(m_analogPin);
      int sensorValue = analogRead(m_analogPin);
      return sensorValue;
      //float value= (float)sensorValue * (4.85f / 1023.0f);
     // return value;
  }
  
  
  float transformToCM(float a_voltage) {
    return a_voltage;
      static const int numDots = 13;
      static const float voltages[numDots]= {0.52,  0.58, 0.60f, 0.65f,  0.71f, 0.77f, 0.85, 0.95f, 1.09, 1.31, 1.63f, 2.11f, 2.9f};
      static const float distance[numDots]= {140,    130,    120,    110,    100,    90,    80,   70,  60,   50,      40,    30,   16};
      float dist = 0.0f;
     
      for (int i = 0; i < numDots-1; i++) {
        if (a_voltage < voltages[i+1]) {
           return floatMap(a_voltage, voltages[i], voltages[i+1], distance[i], distance[i+1]);
        }
      }
      
      
      
      return dist;
    }
  
  protected:
    int m_analogPin;
    
    float floatMap(float sourceValue, float sourceMin, float sourceMax, float destMin, float destMax) {
      float sourceRange = sourceMax - sourceMin;
      float percent = ((sourceValue - sourceMin ) / sourceRange);
      float destRange = destMax - destMin;
      return destMin + percent * destRange;
    }
    
    

};

#endif
