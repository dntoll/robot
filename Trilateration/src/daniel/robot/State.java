package daniel.robot;

import java.awt.geom.Point2D.Float;
import java.util.Random;

public class State {
	public Float m_position; //in degrees
	public Direction m_heading; //in meters
	
	public float m_directionalError;
	public float m_distanceError;
	public float m_overlap;
	
	private static Random rand = new Random();
	
	public State(Float position, Direction compassDirection) {
		m_position = new Float();
		m_position.x = position.x;
		m_position.y = position.y;
		m_heading = new Direction(compassDirection.getHeadingDegrees());
		m_directionalError = 0.0f;
		m_distanceError = 0.0f;
		m_overlap = 0.0f;
	}
	
	public State(State a_state) {
		m_position = new Float();
		m_position.x = a_state.m_position.x;
		m_position.y = a_state.m_position.y;
		m_heading = new Direction(a_state.m_heading.getHeadingDegrees());
		m_directionalError = a_state.m_directionalError;
		m_distanceError = a_state.m_distanceError;
		m_overlap = a_state.m_overlap;
	}

	public void move(float forward, float headingChange, float headingVariance, float positionVariance) {
		m_heading.Turn(headingChange + rand.nextFloat() * headingVariance*2.0f-headingVariance);
		
		float x = m_heading.getX() * forward; 
		float y = m_heading.getY() * forward;
		
		//m_position = new Float();
		m_position.x = m_position.x + x + rand.nextFloat() * (positionVariance*2.0f)-positionVariance;
		m_position.y = m_position.y + y + rand.nextFloat() * (positionVariance*2.0f)-positionVariance;
	}
	
	public String toString() {
		return 
		" de: " + m_directionalError + 
	    " so: " + m_overlap + 
		" di: " + m_distanceError + 
		" x : "  + m_position.x +
		" y : "  + m_position.y +
		" d : "  + m_heading.getHeadingDegrees();
	}
}
