package daniel.robot.SLAM;

import daniel.robot.statistics.Gaussian;

public class Movement {

	public float m_distance;
	public float m_turnRight;

	public Movement(float a_distance, float a_turnRight) {
		m_distance = a_distance;
		m_turnRight = a_turnRight;
	}

	public float getPossibleAngle() {
		return Gaussian.getRandomGaussian(m_turnRight, getHeadingVariance());
		
	}

	public float getDistanceMoved() {
		float distanceMoved = Gaussian.getRandomGaussian(m_distance, getPositionVariance());
		return distanceMoved;
	}
	
	private float getHeadingVariance() {
		return m_turnRight/2.0f + 5.0f;
	}

	private float getPositionVariance() {
		return m_distance / 3.0f;
	}

	public float getAngle() {
		return m_turnRight;
	}
	
	public float getDistance() {
		return m_distance;
	}

	//https://docs.google.com/spreadsheet/ccc?key=0AssbuCblTdssdGU4TFVSZlBINWptMS1ZbmRNUUVoNHc#gid=0
	

}
