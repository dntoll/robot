package daniel.robot.sensors;

public abstract class DistanceBase {

	public float m_distance;
	public float m_servo;
	
	public DistanceBase(float servoPos, float distance) {
		m_distance = distance;
		m_servo = servoPos;
	}

	public abstract float getBeamWidth();

	public abstract boolean okDistance();
}
