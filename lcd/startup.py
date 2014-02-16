import RPi.GPIO as GPIO
import time
import os
import pcd8544.lcd as lcd
import time, os, sys

if not os.geteuid() == 0:
  sys.exit('Script must be run as root')

ON, OFF = [1, 0]

#adjust for where your switch is connected
buttonPin = 17
GPIO.setmode(GPIO.BCM)
GPIO.setup(buttonPin,GPIO.IN)
started = False


try:
  lcd.init()
  lcd.cls()
  lcd.backlight(ON)
  lcd.centre_text(0,"Raspberry Pi")
  while 1:
     lcd.centre_text(2,time.strftime("%d %b %Y", time.localtime()))
     lcd.centre_text(3,time.strftime("%H:%M:%S", time.localtime()))
     time.sleep(0.25)
     if (GPIO.input(buttonPin) == False):
       os.system("java -jar /home/pi/Robot/robot/Trilateration/robot.jar &")
       
       break
except KeyboardInterrupt:
  pass
finally:
  lcd.cls()
  lcd.backlight(OFF)

time.sleep(1.5)

while True:
  if (GPIO.input(buttonPin) == False):
    os.system("shutdown -h now")
    break;
