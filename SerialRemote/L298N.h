#pragma once
#include <Arduino.h>

class L298N  {
  public:
    L298N() {
      
     ENA=5;
     ENB=3;
     IN1=10;
     IN2=9;

     IN3=6;
     IN4=17;
      pinMode(ENA,OUTPUT);//output
      pinMode(ENB,OUTPUT);
      pinMode(IN1,OUTPUT);
      pinMode(IN2,OUTPUT);
      pinMode(IN3,OUTPUT);
      pinMode(IN4,OUTPUT);
      analogWrite(ENA,0);//stop driving motorA
      analogWrite(ENB,0);//stop driving motorB
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
      digitalWrite(IN3, leftForward ? HIGH : LOW);
      digitalWrite(IN4, leftForward ? LOW : HIGH);//setting motorB's directon 
      
      analogWrite(ENA,255);
      analogWrite(ENB,255);
 //     digitalWrite(ENA, HIGH);
 //     digitalWrite(ENB,HIGH);//start driving motorB
    }

    void stopAll() {
      analogWrite(ENA,0);//stop driving motorA
      analogWrite(ENB,0);//stop driving motorB
      
      digitalWrite(IN1,LOW);
      digitalWrite(IN2,HIGH);//setting motorA's directon
      digitalWrite(IN3,LOW);
      digitalWrite(IN4,HIGH);//setting motorB's directon
    }
  private:
    int ENA;
    int IN1;
    int IN2;
    int ENB;
    int IN3;
    int IN4;

};
