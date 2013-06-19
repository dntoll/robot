package daniel.robot.sensors;


//TODO: baseclass with ir and distance reading
public class SonarReading {
	public float m_distance;
	public float m_servo;
	
	public SonarReading(float servoPos, float distance) {
		
		if (distance == 0) {
			distance = 200;
		}
		
		m_distance = distance;
		m_servo = servoPos;
	}
	
}
