#pragma once

#include "odometry.h"
#include "HC_SR04.h"
#include "SharpSensor.h"
#include "L298N.h"

#ifdef COMPASS
  #include "Compass.h"
#endif

#ifdef GYRO 
 #include "MPU6050.h"
#endif

#include "odometry.h"


#define TRIGGER_PIN   16 
#define ECHO_PIN      15  
#define SERVO_PIN     11
#define SHARP_PIN     0
#define SHARP_ENABLE_PIN 2
#define MIN_DEGREES 0
#define MAX_DEGREES 170
#define SERVO_DELAY_TIME 80

class Robot {
  public:
    Robot() : m_sharp(SHARP_PIN), m_sonar(TRIGGER_PIN, ECHO_PIN) {
      
      //m_hBridge.stopAll();
      
      m_servoPos = 90;
      m_servo.attach(SERVO_PIN);  
      
   //   m_servo.write(m_servoPos);
      
      pinMode(SHARP_ENABLE_PIN, OUTPUT);
      digitalWrite(SHARP_ENABLE_PIN, LOW);
      
      m_pCompass = new Compass();
      #ifdef GYRO 
        m_pGyro = new MPU6050();
      #endif
    }
    
    ~Robot() {
      #ifdef COMPASS
        delete m_pCompass;
      #endif
      #ifdef GYRO 
        delete m_pGyro;
      #endif
    }
    
    void sonarSweep() {
       Serial.print("Servo:Sonar");
       shutdownSharp();
       int minAngle = MIN_DEGREES;
       int maxAngle = MAX_DEGREES;
       for(int pos = minAngle; pos <= maxAngle; pos += 5)  // goes from 0 degrees to 180 degrees 
       { 
          moveServo(pos);
          float sonarMeasurement = m_sonar.getMedian(5, 30);
          float sonarcm = m_sonar.transformToCM(sonarMeasurement);
          
          Serial.print(":");
          Serial.print(m_servoPos);
          Serial.print(":");
          Serial.print(sonarcm);
       }
       Serial.println("");
       
       moveServo(90);
    }
    
    void calibrate() {
      #ifdef COMPASS
      //Serial.print("Calibration");
      while (true) {
        if (Serial.available() > 0) {
          char inByte = Serial.read();
          if (inByte == 'n') {
            break;
          } 
        } else {
          m_pCompass->calibrate();
          delay(300);
        }
      }
      
      m_pCompass->printCalibration();
      Serial.println("DONE");
      #endif
    }
    
    
    void calibrateIR() {
      Serial.print("Calibration");
      while (Serial.available() <= 0) {
        measureDistance(90);
        delay(300);
      }
    }
    
    void irSweep() {
       Serial.print("Servo:Sharp");
       startupSharp();
       int minAngle = MIN_DEGREES;
       int maxAngle = MAX_DEGREES;
       for(int pos = maxAngle; pos >= minAngle; pos -= 1)  // goes from 0 degrees to 180 degrees 
       { 
          moveServo(pos);
          float sharpMeasurement, sharpcm, sonarMeasurement = 0, sonarcm = 0;
          sharpMeasurement = m_sharp.getMedian(10,1);
          sharpcm = m_sharp.transformToCM(sharpMeasurement);
          
          Serial.print(":");
          Serial.print(m_servoPos);
          Serial.print(":");
          Serial.print(sharpcm);
       }
       Serial.println("");
       
       moveServo(90);
    }
    
    void measureDistance(int a_pos) {
      Serial.print("Sharp:Sonar");
      moveServo(a_pos);
      startupSharp();
      float sharpMeasurement, sharpcm, sonarMeasurement = 0, sonarcm = 0;
      sharpMeasurement = m_sharp.getMedian(5,5);
      sharpcm = m_sharp.transformToCM(sharpMeasurement);
      shutdownSharp();
     
      sonarMeasurement = m_sonar.getMedian(5,30);
      sonarcm = m_sonar.transformToCM(sonarMeasurement);
      Serial.print(":");
      Serial.print(sharpcm, 1);
      Serial.print(":");
      Serial.print(sonarcm, 1);
      Serial.print(":");
      Serial.print(sharpMeasurement);
      Serial.print(":");
      Serial.print(sonarMeasurement, 1);
      Serial.println("");
    }
    
    void measureGyro() {
      #ifdef GYRO
      accel_t_gyro_union accel_t_gyro = m_pGyro->getAll();
      Serial.print("Accel:Gyro");

      
      Serial.print(":");
      Serial.print(accel_t_gyro.value.x_accel, DEC);
      Serial.print(":");
      Serial.print(accel_t_gyro.value.y_accel, DEC);
      Serial.print(":");
      Serial.print(accel_t_gyro.value.z_accel, DEC);
      Serial.print(":");
      Serial.print(accel_t_gyro.value.x_gyro, DEC);
      Serial.print(":");
      Serial.print(accel_t_gyro.value.y_gyro, DEC);
      Serial.print(":");
      Serial.print(accel_t_gyro.value.z_gyro, DEC);
      Serial.println("");
      #endif
    }
    
    void measureTemperature() {
      #ifdef GYRO
       float deg = m_pGyro->getTemperature();
       Serial.print("Temperature:");
       Serial.print(deg);
       Serial.println("");
      #endif
    }
    
    void measureCompass() {
       float deg = m_pCompass->measure();
       Serial.print("Compass:");
       Serial.print(deg);
       Serial.println("");
    }
    
    void left(int a_time) {
      Serial.print("Move:Left");
      m_hBridge.left();
      driveAndMeasure(a_time);
      
    }
    
    void right(int a_time) {
      Serial.print("Move:Right");
      m_hBridge.right();
      driveAndMeasure(a_time);
    }
    
    void forward(int a_time) {
      Serial.print("Move:forward:");
      m_hBridge.forward();
      driveAndMeasure(a_time);
    }
    
    void backward(int a_time) {
      Serial.print("Move:backward");
      m_hBridge.backward();
      driveAndMeasure(a_time);
    }
  private:
    void driveAndMeasure(int a_time) {
    //  Odometry odo;
   //   odo.measureForTime(a_time);
   //   Serial.print(":");
     delay(a_time);
      m_hBridge.stopAll();
  //    Serial.print(odo.toString());
      Serial.println("");
      
      
    //  measureCompass();
    }
    
    void moveServo(int pos) {
      m_servoPos = pos;
      m_servo.write(m_servoPos);
      delay(SERVO_DELAY_TIME);
    }

    
    
    void startupSharp() {
      digitalWrite(SHARP_ENABLE_PIN, HIGH);
      delay(38+10+6);
      analogRead(SHARP_PIN);
      delay(10);
    }
    
    void shutdownSharp() {
      digitalWrite(SHARP_ENABLE_PIN, LOW);
      delay(30);
    }
    
    
    
    Servo m_servo;  // create servo object to control a servo 
    SharpSensor m_sharp;
    HC_SR04 m_sonar;
    L298N m_hBridge;
    #ifdef GYRO
      MPU6050 *m_pGyro;
    #endif
    Compass *m_pCompass;
    int m_servoPos;
    
};
