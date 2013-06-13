package daniel.robot.sensors;

public class IRReading {
	public static final float IR_DISTANCE_NOISE = 5.5f;
	public static final float BEAM_WIDTH = 2;
	
	public IRReading(float servoPos, float distance) {
		m_distance = distance;
		m_servo = servoPos;
	}
	public float m_distance;
	public float m_servo;
}
