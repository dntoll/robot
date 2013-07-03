#pragma once
#include <Arduino.h>

class L298N  {
  public:
    L298N() {
      
      pinMode(ENA,OUTPUT);//output
      pinMode(ENB,OUTPUT);
      pinMode(IN1,OUTPUT);
      pinMode(IN2,OUTPUT);
      pinMode(IN3,OUTPUT);
      pinMode(IN4,OUTPUT);
      digitalWrite(ENA,LOW);
      digitalWrite(ENB,LOW);//stop driving
      digitalWrite(IN1,LOW);
      digitalWrite(IN2,HIGH);//setting motorA's directon
      digitalWrite(IN3,LOW);
      digitalWrite(IN4,HIGH);//setting motorB's directon
    }
    
    void forward() {
      runMotors(true, true);
    }
    void backward() {
      runMotors(false, false);
    }
    void left() {
      runMotors(false, true);
    }
    void right() {
      runMotors(true, false);
    }
    
    void runMotors(boolean leftForward, boolean rightForward) {
      digitalWrite(IN1,rightForward ? LOW : HIGH);
      digitalWrite(IN2,rightForward ? HIGH : LOW);//setting motorA's directon
      digitalWrite(IN3, leftForward ? LOW : HIGH);
      digitalWrite(IN4, leftForward ? HIGH : LOW);//setting motorB's directon 
      
      analogWrite(ENA,225);//start driving motorA
      analogWrite(ENB,192);//start driving motorB
    }

    void stopAll() {
      analogWrite(ENA,0);//stop driving motorA
      analogWrite(ENB,0);//stop driving motorB
      
      analogWrite (IN1,0);//stop driving motorA
      analogWrite (IN2,0);//stop driving motorB
       
      analogWrite (IN3,0);//stop driving motorA
      analogWrite (IN4,0);//stop driving motorB
    }
  private:
    int ENA=5;//connected to Arduino's port 5(output pwm)
    int IN1=10;//connected to Arduino's port 2
    int IN2=9;//connected to Arduino's port 3
    int ENB=3;//connected to Arduino's port 6(output pwm)
    int IN3=6;//connected to Arduino's port 4
    int IN4=17;//connected to Arduino's port 7

};
