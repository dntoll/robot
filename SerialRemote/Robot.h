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


//SERVO
#define SERVO_PIN     11
#define MIN_DEGREES 20
#define MAX_DEGREES 160
#define SERVO_DELAY_TIME 80

//SONAR
#define TRIGGER_PIN   4 
#define ECHO_PIN      16  

//SHARP
#define SHARP_PIN1     1
#define SHARP_PIN2     0

//ENABLERS
#define SHARP1_ENABLE_PIN 8
#define SHARP2_ENABLE_PIN 12
#define SONAR1_ENABLE_PIN 2
#define SONAR2_ENABLE_PIN 7




class Robot {
  public:
    Robot() : m_sharp1(SHARP_PIN1), m_sharp2(SHARP_PIN2), m_sonar1(TRIGGER_PIN, ECHO_PIN), m_sonar2(TRIGGER_PIN, ECHO_PIN)  {
      
      //m_hBridge.stopAll();
      
      m_servoPos = 90;
      m_servo.attach(SERVO_PIN);  
      
   //   m_servo.write(m_servoPos);
     pinMode(SHARP1_ENABLE_PIN, OUTPUT); 
     pinMode(SHARP2_ENABLE_PIN, OUTPUT); 
     pinMode(SONAR1_ENABLE_PIN, OUTPUT); 
     pinMode(SONAR2_ENABLE_PIN, OUTPUT); 
      
      shutdownAll();
      
     // digitalWrite(SONAR2_ENABLE_PIN, HIGH);
      
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

       int minAngle = MIN_DEGREES;
       int maxAngle = MAX_DEGREES;
       float sonarMeasurement;
       float sonarcm;
       
       shutdownAll();
       startUpSensor(SONAR2_ENABLE_PIN);
       for(int pos = minAngle; pos <= maxAngle; pos += 5)  // goes from 0 degrees to 180 degrees 
       { 
          moveServo(pos);
          sonarMeasurement = m_sonar2.getMedian(5, 30);
          sonarcm = m_sonar2.transformToCM(sonarMeasurement);
          
          Serial.print(":");
          Serial.print((m_servoPos+180)%360);
          Serial.print(":");
          Serial.print(sonarcm);
       }
       shutdownAll();
       startUpSensor(SONAR1_ENABLE_PIN);
       for(int pos = minAngle; pos <= maxAngle; pos += 5)  // goes from 0 degrees to 180 degrees 
       { 
          moveServo(pos);
          sonarMeasurement = m_sonar1.getMedian(5, 30);
          sonarcm = m_sonar1.transformToCM(sonarMeasurement);
          
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
       int minAngle = MIN_DEGREES;
       int maxAngle = MAX_DEGREES;
       float sharpMeasurement, sharpcm, sonarMeasurement = 0, sonarcm = 0;
       
       shutdownAll();
       startUpSensor(SHARP1_ENABLE_PIN);
       for(int pos = maxAngle; pos >= minAngle; pos -= 1)  // goes from 0 degrees to 180 degrees 
       { 
          moveServo(pos);
          sharpMeasurement = m_sharp1.getMedian(10,1);
          sharpcm = m_sharp1.transformToCM(sharpMeasurement);
          
          Serial.print(":");
          Serial.print(m_servoPos);
          Serial.print(":");
          Serial.print(sharpcm);
       }  
       
       shutdownAll();
       startUpSensor(SHARP2_ENABLE_PIN);
       
       for(int pos = maxAngle; pos >= minAngle; pos -= 1)  // goes from 0 degrees to 180 degrees 
       { 
          moveServo(pos);
          
          sharpMeasurement = m_sharp2.getMedian(10,1);
          sharpcm = m_sharp2.transformToCM(sharpMeasurement);
          
          Serial.print(":");
          Serial.print((m_servoPos+180)%360);
          Serial.print(":");
          Serial.print(sharpcm);
       }
       Serial.println("");
       
       moveServo(90);
    }
    
    void measureDistance(int a_pos) {
      float sharpMeasurement, sharpcm, sonarMeasurement = 0, sonarcm = 0;
      
      Serial.println("Sharp:Sonar");
      moveServo(a_pos);
      
      
      shutdownAll();
      startUpSensor(SHARP1_ENABLE_PIN);
      
      sharpMeasurement = m_sharp1.getMedian(5,40);
      sharpcm = m_sharp1.transformToCM(sharpMeasurement);
      Serial.print("s1:");
      Serial.println(sharpcm, 1);
      
      shutdownAll();
      startUpSensor(SONAR1_ENABLE_PIN);
      sonarMeasurement = m_sonar1.getMedian(5,30);
      sonarcm = m_sonar1.transformToCM(sonarMeasurement);
      Serial.print("u1:");
      Serial.println(sonarcm, 1);
      
      shutdownAll();
      startUpSensor(SHARP2_ENABLE_PIN);
      sharpMeasurement = m_sharp2.getMedian(5,40);
      sharpcm = m_sharp2.transformToCM(sharpMeasurement);
      Serial.print("s2:");
      Serial.println(sharpcm, 1);
      
      
      
      shutdownAll();
      startUpSensor(SONAR2_ENABLE_PIN);
      sonarMeasurement = m_sonar2.getMedian(5,30);
      sonarcm = m_sonar2.transformToCM(sonarMeasurement);
      Serial.print("u2:");
      Serial.println(sonarcm, 1);
      
      Serial.println("");
      
    }
    
    void measureGyro() {
      #ifdef GYRO
      int gx,gy,gz, ax, ay, az;
      gx = gy = gz = 0;
      
      int samples = 5;
      for (int i = 0; i< samples; i++) {
        accel_t_gyro_union accel_t_gyro = m_pGyro->getAll();
        gx += accel_t_gyro.value.x_gyro;
        gy += accel_t_gyro.value.y_gyro;
        gz += accel_t_gyro.value.z_gyro;
        ax += accel_t_gyro.value.x_accel;
        ay += accel_t_gyro.value.y_accel;
        az += accel_t_gyro.value.z_accel;
      }
      Serial.print("Accel:Gyro");

      
      Serial.print(":");
      Serial.print(ax/samples, DEC);
      Serial.print(":");
      Serial.print(ay/samples, DEC);
      Serial.print(":");
      Serial.print(az/samples, DEC);
      Serial.print(":");
      Serial.print(gx/samples, DEC);
      Serial.print(":");
      Serial.print(gy/samples, DEC);
      Serial.print(":");
      Serial.print(gz/samples, DEC);
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
     //measureGyro();
  //   for (int i = 0; i < a_time/10; i++) {
    //   measureGyro();
   //    delay(a_time/10);
   //  }
      delay(a_time);
      m_hBridge.stopAll();
   //   measureGyro();
      Serial.println("");
    }
    
    void moveServo(int pos) {
      m_servoPos = pos;
      m_servo.write(m_servoPos);
      delay(SERVO_DELAY_TIME);
    }

    
    
    void startUpSensor(int sensor) {
      digitalWrite(sensor, HIGH);
      
      if (SHARP1_ENABLE_PIN == sensor || SHARP2_ENABLE_PIN == sensor) {
         delay(38+10+6);
         delay(0);
         analogRead(sensor);
      } else {
        delay(55);
      }

    }
    
    void shutdownAll() {
      digitalWrite(SHARP1_ENABLE_PIN, LOW);
      digitalWrite(SHARP2_ENABLE_PIN, LOW);
      digitalWrite(SONAR1_ENABLE_PIN, LOW);
      digitalWrite(SONAR2_ENABLE_PIN, LOW);
      delay(3);
    }
    
    
    
    Servo m_servo;  // create servo object to control a servo 
    SharpSensor m_sharp1;
    SharpSensor m_sharp2;
    HC_SR04 m_sonar1;
    HC_SR04 m_sonar2;
    L298N m_hBridge;
    #ifdef GYRO
      MPU6050 *m_pGyro;
    #endif
    Compass *m_pCompass;
    int m_servoPos;
    
};
