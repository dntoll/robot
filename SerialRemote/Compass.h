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
    Serial.println("Starting the I2C interface.");
    Wire.begin(); // Start the I2C interface.
    compass = HMC5883L(); // Construct a new HMC5883 compass.
    Serial.println("Setting scale to +/- 1.3 Ga");
    int error = compass.SetScale(1.3); // Set the scale of the compass.
    if(error != 0) // If there is an error, print it out.
      Serial.println(compass.GetErrorText(error));
    
    Serial.println("Setting measurement mode to continous.");
    error = compass.SetMeasurementMode(Measurement_Continuous); // Set the measurement mode to Continuous
    if(error != 0) // If there is an error, print it out.
      Serial.println(compass.GetErrorText(error));
      
      minX = 10000;
      maxX = -10000;
      minY = 10000;
      maxY = -10000;
  }
  
  // Our main program loop.
float measure()
{
  // Retrive the raw values from the compass (not scaled).
  MagnetometerRaw raw = compass.ReadRawAxis();
  // Retrived the scaled values from the compass (scaled to the configured scale).
  MagnetometerScaled scaled = compass.ReadScaledAxis();

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

  // Output the data via the serial port.
  //  Output(raw, scaled, heading, headingDegrees);
  return headingDegrees;
}
  /*float CalculateHeadingTiltCompensated(MagnetometerScaled mag)
  {
    // We are swapping the accelerometers axis as they are opposite to the compass the way we have them mounted.
    // We are swapping the signs axis as they are opposite.
    // Configure this for your setup.
    float accX = 0;
    float accY = 1;
    
    float rollRadians = -30.0f *6.28f/360.0f;//asin(accY);
    float pitchRadians = 0;//asin(accX);
    
    // We cannot correct for tilt over 40 degrees with this algorthem, if the board is tilted as such, return 0.
    if(rollRadians > 0.78 || rollRadians < -0.78 || pitchRadians > 0.78 || pitchRadians < -0.78)
    {
      return 0;
    }
    
    // Some of these are used twice, so rather than computing them twice in the algorithem we precompute them before hand.
    float cosRoll = cos(rollRadians);
    float sinRoll = sin(rollRadians);  
    float cosPitch = cos(pitchRadians);
    float sinPitch = sin(pitchRadians);
    
    // The tilt compensation algorithem.
    float Xh = mag.XAxis * cosPitch + mag.ZAxis * sinPitch;
    float Yh = mag.XAxis * sinRoll * sinPitch + mag.YAxis * cosRoll - mag.ZAxis * sinRoll * cosPitch;
    
    float heading = atan2(Yh, Xh);
      
    return heading;
  }
  */
  
  
 private:
  // Output the data down the serial port.
  void Output(MagnetometerRaw raw, MagnetometerScaled scaled, float heading, float headingDegrees)
  {
     Serial.print("Raw:\t");
     Serial.print(minX);
     Serial.print("   ");   
     Serial.print(maxX);
     Serial.print("   ");   
     Serial.print(minY);
     Serial.print("   ");   
     Serial.print(maxY);
     Serial.print("   ");   
     Serial.print("   \tHeading:\t");
     Serial.print(heading);
     Serial.print(" Radians   \t");
     Serial.print(headingDegrees);
     Serial.println(" Degrees   \t");
  }
 
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
