package daniel.robot;

import java.awt.geom.Point2D.Float;
import java.util.Random;

public class State {
	public Float m_position; //in degrees
	public float m_heading; //in meters
	private static Random rand = new Random();
	
	public State(Float position, float compassDirection) {
		m_position = position;
		m_heading = compassDirection;
	}

	public void move(float x, float y, float headingChange, float headingVariance, float positionVariance) {
		m_heading += headingChange + rand.nextFloat() * headingVariance*2.0f-headingVariance;
		
		if (m_heading > 360) {
			m_heading -= 360;
		} else if (m_heading < 0) {
			m_heading += 360;
		}
		
		m_position = new Float();
		m_position.x = m_position.x + x + rand.nextFloat() * (positionVariance*2.0f)-positionVariance;
		m_position.y = m_position.y + y + rand.nextFloat() * (positionVariance*2.0f)-positionVariance;
	}
}
