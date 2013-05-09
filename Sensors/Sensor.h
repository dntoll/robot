#ifndef SENSOR_H
#define SENSOR_H

class Sensor {
  public:
    float getMedian(int a_samples, int a_delay) {
        float samples[a_samples];
        for (int i = 0; i< a_samples; i++) {
          samples[i] = getRawMeasurement();
          delay(a_delay);
        }
            
  
        // sort all
        for (uint8_t i=0; i< a_samples-1; i++) 
        {
                uint8_t m = i;
                for (uint8_t j=i+1; j< a_samples; j++)
                {
                    if (samples[j] < samples[m]) 
                        m = j;
                }
                if (m != i)
                {
                    float t = samples[m];
                    samples[m] = samples[i];
                    samples[i] = t;
                }
        }
  
        
        int samplesSampled = 0;
        float accum = 0;
        for (int i = 2; i< a_samples-2; i++) {
           accum += samples[i];
           samplesSampled++;
        }
   
        return accum / (float)samplesSampled;
  }
  
  virtual float getRawMeasurement() = 0;
  virtual float transformToCM(float a_raw) = 0;
};

#endif
