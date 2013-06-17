package daniel.robot.sensors;

import daniel.robot.Direction;


public class SensorReading {
	public Direction m_compassDirection;
	public float m_temperature;
	
	public SonarReading[] m_sonar;
	public IRReading[] m_ir;
	public GyroAccelerometerReading m_gyroAccelerator;
	public DistanceReading[] m_distances;
	
	public SensorReading(Direction compassDirection,
			GyroAccelerometerReading gyroAccelerator, SonarReading[] sonar,
			IRReading[] ir, float temperature) throws Exception {
		m_compassDirection = compassDirection;
		m_temperature = temperature;
		m_sonar = sonar;
		m_ir = ir;
		m_gyroAccelerator = gyroAccelerator;
		
		setupDistances();
	}
	
	
	
	

	private void setupDistances() throws Exception {
		m_distances = new DistanceReading[m_ir.length];
		
		for (int i = 0;i < m_ir.length; i++) {
			SonarReading sonar = getFurthestSonar(m_ir[i].m_servo);
			m_distances[i] = new DistanceReading(m_ir[i], sonar, distanceDegrees(m_ir[i].m_servo, sonar.m_servo));
		}
	}





	private SonarReading getFurthestSonar(float a_servo) {
		SonarReading ret = m_sonar[0];
		
		float distance = 0.0f;//distanceDegrees(a_servo, m_sonar[0].m_servo);
		
		for (int i = 0; i< m_sonar.length; i++) {
			float newDistance = distanceDegrees(m_sonar[i].m_servo,a_servo);
			if (newDistance <= 6.5f && m_sonar[i].m_distance > distance) {
				
				ret = m_sonar[i];
				distance = m_sonar[i].m_distance;
			}
		}
		return ret;
	}





	private float distanceDegrees(float a_degrees1, float a_degrees2) {
		return (float) Math.sqrt((a_degrees1 - a_degrees2) * (a_degrees1 - a_degrees2));
	}





	public String toString() {
		return "SensorReading Compass: " + m_compassDirection + " IR: " + m_ir[0].m_distance;
	}
}
