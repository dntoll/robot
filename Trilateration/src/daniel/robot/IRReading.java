package daniel.robot;

public class IRReading {
	public IRReading(float servoPos, float distance) {
		m_distance = distance;
		m_servo = servoPos;
	}
	public float m_distance;
	public float m_servo;
}
