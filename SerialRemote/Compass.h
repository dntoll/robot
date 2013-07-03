/*
HMC5883L_Example.pde - Example sketch for integration with an HMC5883L triple axis magnetomerwe.
Copyright (C) 2011 Love Electronics (loveelectronics.co.uk)

This program is free software: you can redistribute it and/or modify
it under the terms of the version 3 GNU General Public License as
published by the Free Software Foundation.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.

*/
#pragma once



class Compass {
  public:
  Compass() {
    Serial.println("Compass");
    Wire.begin(); // Start the I2C interface.
    compass = HMC5883L(); // Construct a new HMC5883 compass.
    int error = compass.SetScale(1.3); // Set the scale of the compass.
    if(error != 0) // If there is an error, print it out.
      Serial.println(compass.GetErrorText(error));

    error = compass.SetMeasurementMode(Measurement_Continuous); // Set the measurement mode to Continuous
    if(error != 0) // If there is an error, print it out.
      Serial.println(compass.GetErrorText(error));
      
      minX = -241;
      maxX = 233;
      minY = -540;
      maxY = 193;
  }
  
  
void calibrate() {
  // Retrive the raw values from the compass (not scaled).
  MagnetometerRaw raw = compass.ReadRawAxis();

  if (raw.XAxis < minX && raw.XAxis > -1000) {
      minX = raw.XAxis;
  }  
  if (raw.YAxis < minY && raw.YAxis > -1000) {
      minY = raw.YAxis;
  }
  if (raw.XAxis > maxX && raw.XAxis < 1000) {
      maxX = raw.XAxis;
  }  
  if (raw.YAxis > maxY && raw.YAxis < 1000) {
      maxY = raw.YAxis;
  }
} 

void printCalibration() {
  Serial.println(minX);
  Serial.println(maxX);
  Serial.println(minY);
  Serial.println(maxY);
}

  // Our main program loop.
float measure()
{
  // Retrive the raw values from the compass (not scaled).
  MagnetometerRaw raw = compass.ReadRawAxis();
  // Retrived the scaled values from the compass (scaled to the configured scale).
  MagnetometerScaled scaled = compass.ReadScaledAxis();

  //recenter
  float rX = map(raw.XAxis, minX, maxX, -255, 255);
  float rY = map(raw.YAxis, minY, maxY, -255, 255);
  
  // Calculate heading when the magnetometer is level, then correct for signs of axis.
  //  float heading = atan2(raw.YAxis, raw.XAxis);
  float heading = atan2(rY, rX);
  //float heading = CalculateHeadingTiltCompensated(scaled);
  
  // Once you have your heading, you must then add your 'Declination Angle', which is the 'Error' of the magnetic field in your location.
  // Find yours here: http://www.magnetic-declination.com/
  // Mine is: 2� 37' W, which is 2.617 Degrees, or (which we need) 0.0456752665 radians, I will use 0.0457
  // If you cannot find your Declination, comment out these two lines, your compass will be slightly off.
  float declinationAngle = 0.07418;
  heading += declinationAngle;
  
  // Correct for when signs are reversed.
  if(heading < 0)
    heading += 2*PI;
    
  // Check for wrap due to addition of declination.
  if(heading > 2*PI)
    heading -= 2*PI;
    
  // Convert radians to degrees for readability.
  float headingDegrees = heading * 180/M_PI; 
  
  headingDegrees -= 125;
  if(headingDegrees < 0)
    headingDegrees += 360;
  
//  static float match[] =      {0,  36, 114, 175, 227, 265, 293, 327, 360 };
  static float match[] = {0,  45,  90, 135, 180, 225, 270, 315, 360 };
  static float correction[] = {0,  45,  90, 135, 180, 225, 270, 315, 360 };
  
  for (int i = 0; i < 8; i++) {
    if (headingDegrees > match[i] && headingDegrees <= match[i+1]) {
      return map(headingDegrees, match[i], match[i+1], correction[i], correction[i+1]);
    }
  }
  // Output the data via the serial port.
  //  Output(raw, scaled, heading, headingDegrees);
  return headingDegrees;
}
  
  
  
 private:
  
  float RadiansToDegrees(float rads)
  {
    // Correct for when signs are reversed.
    if(rads < 0)
      rads += 2*PI;
        
    // Check for wrap due to addition of declination.
    if(rads > 2*PI)
      rads -= 2*PI;
     
    // Convert radians to degrees for readability.
    float heading = rads * 180/PI;
         
    return heading;
  }
  
  
 
    HMC5883L compass;
     int minX;
     int maxX;
     int minY;
     int maxY;
};
