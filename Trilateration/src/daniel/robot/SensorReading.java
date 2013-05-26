package daniel.robot;

public class SensorReading {
	public float m_compassDirection;
	public float m_temperature;
	
	public SonarReading[] m_sonar;
	public IRReading[] m_ir;
	public GyroAccelerometerReading m_gyroAccelerator;
	
	
	
	
	
	public SensorReading(float compassDirection,
			GyroAccelerometerReading gyroAccelerator, SonarReading[] sonar,
			IRReading[] ir, float temperature) {
		m_compassDirection = compassDirection;
		m_temperature = temperature;
		m_sonar = sonar;
		m_ir = ir;
		m_gyroAccelerator = gyroAccelerator;
		
	
	}

	public String toString() {
		return "SensorReading " + m_compassDirection;
	}
}
