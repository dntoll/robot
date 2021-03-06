package daniel.robot.slam;

import daniel.robot.Gaussian;

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
	
	
	float randomness = 1.0f;
	private float getHeadingVariance() {
		return  randomness * (m_turnRight/10.0f + 1.0f);
	}

	private float getPositionVariance() {
		return randomness * (m_distance /5.0f+1.0f);
	}

	public float getAngle() {
		return m_turnRight;
	}
	
	public float getDistance() {
		return m_distance;
	}

	//https://docs.google.com/spreadsheet/ccc?key=0AssbuCblTdssdGU4TFVSZlBINWptMS1ZbmRNUUVoNHc#gid=0
	

}
