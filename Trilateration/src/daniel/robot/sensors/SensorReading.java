package daniel.robot.sensors;

import daniel.robot.Direction;

/*
public class SensorReading {
	public Direction m_compassDirection;
	public float m_temperature;
	
	public SonarReading[] m_sonar;
	public IRReading[] m_ir;
	public GyroAccelerometerReading m_gyroAccelerator;
	
	
	public SensorReading(Direction compassDirection,
			GyroAccelerometerReading gyroAccelerator, SonarReading[] sonar,
			IRReading[] ir, float temperature) throws Exception {
		m_compassDirection = compassDirection;
		m_temperature = temperature;
		m_sonar = sonar;
		m_ir = ir;
		m_gyroAccelerator = gyroAccelerator;
	}

	public String toString() {
		return "SensorReading Compass: " + m_compassDirection + " IR: " + m_ir[0].m_distance;
	}


	

	private float distanceDegrees(float a_degrees1, float a_degrees2) {
		return (float) Math.sqrt((a_degrees1 - a_degrees2) * (a_degrees1 - a_degrees2));
	}

	public boolean hasCloseSonar(IRReading distanceReading) {
		
		for (int i = 0; i< m_sonar.length; i++) {
			float newDistance = distanceDegrees(m_sonar[i].m_servo, distanceReading.m_servo);
			if (newDistance <= SonarReading.SONAR_BEAM_WIDTH/2.0f) {
				
				float difference = m_sonar[i].m_distance - distanceReading.m_distance;
				if (difference*difference < 5*5) {
					return true;
				}
			}
		}
		return false;
		
	}





	
}*/
