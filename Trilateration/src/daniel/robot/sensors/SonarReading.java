package daniel.robot.sensors;


public class SonarReading extends DistanceBase {
	public static final float SONAR_DISTANCE_ERROR = 2;
	public static float SONAR_BEAM_WIDTH = 15.0f;
	
	public SonarReading(float servoPos, float distance) {
		super(servoPos, distance);
		if (m_distance == 0) {
			m_distance = 200;
		}
		
	}

	public float getBeamWidth() {
		return SONAR_BEAM_WIDTH;
	}

	public boolean okDistance() {
		return m_distance > 2.0f && m_distance < 200;
	}
	
}
