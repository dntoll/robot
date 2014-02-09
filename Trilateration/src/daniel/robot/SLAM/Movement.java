package daniel.robot.SLAM;

import java.util.Random;

import daniel.robot.statistics.Gaussian;

public class Movement {

	private static Random rand = new Random();

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
		return 10;
	}

	private float getPositionVariance() {
		return 6;
	}

	//https://docs.google.com/spreadsheet/ccc?key=0AssbuCblTdssdGU4TFVSZlBINWptMS1ZbmRNUUVoNHc#gid=0
	

}
