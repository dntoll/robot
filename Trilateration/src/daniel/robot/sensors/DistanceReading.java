package daniel.robot.sensors;



public class DistanceReading {
	
	private float m_beamWidth;
	public float m_distance = 0;
	public float m_servo = 0;

	
	public DistanceReading(IRReading a_irReading, 
						   SonarReading a_closestSonar, 
						   float a_differenceDegrees) throws Exception {
		m_servo = a_irReading.m_servo;
		
		if (a_differenceDegrees > 10.0f)
			throw new Exception("No close sonar measurement " + a_differenceDegrees);
		m_beamWidth = 15.0f - a_differenceDegrees;
		if (a_closestSonar.m_distance < 17.0f) {
			m_distance = a_closestSonar.m_distance;
		} /*else if (a_closestSonar.m_distance > a_irReading.m_distance * 1.2f) {
			m_distance = a_closestSonar.m_distance;
		}*/ else {
			m_distance = a_irReading.m_distance;
			m_beamWidth = 2.0f;
		}
	}

	public float getBeamWidth() {
		return m_beamWidth;
	}

}
