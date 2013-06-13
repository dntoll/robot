package daniel.robot.sensors;

public class IRReading {
	public static final float irDistanceNoise = 5.5f;
	
	public IRReading(float servoPos, float distance) {
		m_distance = distance;
		m_servo = servoPos;
	}
	public float m_distance;
	public float m_servo;
}
