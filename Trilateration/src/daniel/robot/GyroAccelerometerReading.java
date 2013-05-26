package daniel.robot;

public class GyroAccelerometerReading {
	public GyroAccelerometerReading(float ax, float ay,
			float az, float gx, float gy,
			float gz) {
		m_aX = ax;
		m_aY = ay;
		m_aZ = az;
		
		m_gX = gx;
		m_gY = gy;
		m_gZ = gz;
	}
	float m_gX;
	float m_gY;
	float m_gZ;
	
	float m_aX;
	float m_aY;
	float m_aZ;
}
