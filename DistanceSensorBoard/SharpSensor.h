#ifndef SHARP_SENSOR
#define SHARP_SENSOR


class SharpSensor {
  public:
     SharpSensor() {

     }
     
     static const int minDistance = 16;
  
  
  float getRawMeasurement(int a_analogPin) {
    
      analogRead(a_analogPin);
      int sensorValue = analogRead(a_analogPin);
      return sensorValue;
  }
  
  
 
  
  protected:
   
    
    

};

#endif
