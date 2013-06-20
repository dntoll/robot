#pragma once

class Odometry {
  
  public:
  
  Odometry() {
    pinMode(LEFT_PIN, INPUT);
    pinMode(RIGHT_PIN, INPUT);
    
    m_leftOn = digitalRead(LEFT_PIN) == HIGH ? true:  false;
    m_rightOn = digitalRead(RIGHT_PIN) == HIGH ? true:  false;;
  };
  
  void measureForTime(int a_time) {
   for (int i = 0; i < a_time * 10; i++) {
      delayMicroseconds(100);
      if ((digitalRead(LEFT_PIN) == HIGH) && !m_leftOn) {
        m_leftOn = true;
        m_left++;
      }
      if ((digitalRead(LEFT_PIN) == LOW) && m_leftOn) {
        m_leftOn = false;
        m_left++;
      }
      
      if ((digitalRead(RIGHT_PIN) == HIGH) && !m_rightOn) {
        m_rightOn = true;
        m_right++;
      }
      if ((digitalRead(RIGHT_PIN) == LOW) && m_rightOn) {
        m_rightOn = false;
        m_right++;
      }
      
      
      
      
    }
  }
  
  String toString() {
    return String("L") + m_left + String("R") + m_right;
  }
  
  private:
    boolean m_leftOn, m_rightOn;
    int m_left = 0;
    int m_right = 0;
    static const int LEFT_PIN = 4;
    static const int RIGHT_PIN = 7;
  
};
