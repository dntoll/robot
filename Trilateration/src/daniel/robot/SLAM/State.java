package daniel.robot.SLAM;

import java.awt.geom.Point2D.Float;
import java.util.Random;

import daniel.robot.Direction;

public class State {
	public Float m_position; //in degrees
	public Direction m_heading; //in meters
	
	//Error statistics compared to last measurement
	public float m_directionalError;
	public float m_distanceError;
	public float m_overlap;
	
	private static Random rand = new Random();
	
	public State(Float a_position, Direction a_compassDirection) {
		m_position = new Float();
		m_position.x = a_position.x;
		m_position.y = a_position.y;
		m_heading = new Direction(a_compassDirection.getHeadingDegrees());
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

	
	
	public void move(Movement a_move, float a_headingVariance,
			float a_positionVariance) {
		
		m_heading.Turn(a_move.m_turnRight + rand.nextFloat() * a_headingVariance*2.0f-a_headingVariance);
		
		float x = m_heading.getX() * a_move.m_distance; 
		float y = m_heading.getY() * a_move.m_distance;
		
		//m_position = new Float();
		m_position.x = m_position.x + x + rand.nextFloat() * (a_positionVariance*2.0f)-a_positionVariance;
		m_position.y = m_position.y + y + rand.nextFloat() * (a_positionVariance*2.0f)-a_positionVariance;
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
