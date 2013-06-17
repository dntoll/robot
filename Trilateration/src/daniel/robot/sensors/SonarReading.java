package daniel.robot.sensors;

public class SonarReading {
	public SonarReading(float servoPos, float distance) {
		
		if (distance == 0) {
			distance = 200;
		}
		
		m_distance = distance;
		m_servo = servoPos;
	}
	public float m_distance;
	public float m_servo;
}
