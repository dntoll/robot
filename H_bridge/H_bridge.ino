
#include "L298N.h"


L298N hBridge = L298N();
void setup()
{
  Serial.begin(57600);
  Serial.println("Two motors");

  
}




void loop()
{
  hBridge.left();
  delay (1000);
  hBridge.stopAll();
  delay (1000);  
  hBridge.right();
  delay (1000);
  hBridge.stopAll();
  delay (1000);  
  hBridge.right();
  delay (1000);
  hBridge.stopAll();
  delay (1000);
  hBridge.left();
  delay (1000);
  hBridge.stopAll();
 
  
  int ledPin = 13;
  while (1)
  {
    digitalWrite(ledPin, HIGH);   // sets the LED on
    delay(1000);                  // waits for a second
    digitalWrite(ledPin, LOW);    // sets the LED off
    delay(1000);
  }
}
