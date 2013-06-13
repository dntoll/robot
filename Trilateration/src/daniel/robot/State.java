package daniel.robot;

import java.awt.geom.Point2D.Float;
import java.util.Random;

public class State {
	public Float m_position; //in degrees
	public Direction m_heading; //in meters
	private static Random rand = new Random();
	
	public State(Float position, Direction compassDirection) {
		m_position = new Float();
		m_position.x = position.x;
		m_position.y = position.y;
		m_heading = new Direction(compassDirection.getHeadingDegrees());
	}

	public void move(float x, float y, float headingChange, float headingVariance, float positionVariance) {
		m_heading.Turn(headingChange + rand.nextFloat() * headingVariance*2.0f-headingVariance);
		
		
		m_position = new Float();
		m_position.x = m_position.x + x + rand.nextFloat() * (positionVariance*2.0f)-positionVariance;
		m_position.y = m_position.y + y + rand.nextFloat() * (positionVariance*2.0f)-positionVariance;
	}
}
