package daniel.robot.SLAM;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;
import java.util.Random;

import daniel.robot.Direction;

public class State {
	private Float m_position = new Float(); //in degrees
	public Direction m_heading; //in meters
	
	
	public Float getRobotPosition() {
		return m_position;
	}
	
	public Float getHeadPosition() {
		float distance = 7.0f; //Head is 7 cm in front of center
		float x = m_position.x + m_heading.getX() * distance;
		float y = m_position.y + m_heading.getY() * distance;
		return new Point2D.Float(x ,y );
	}
	
	public MatchingError m_error;
	
	private static Random rand = new Random();
	
	public State(Float a_position, Direction a_compassDirection) {
		m_position.x = a_position.x;
		m_position.y = a_position.y;
		m_heading = new Direction(a_compassDirection.getHeadingDegrees());
		
	}
	
	public State(State a_state) {
		m_position.x = a_state.m_position.x;
		m_position.y = a_state.m_position.y;
		m_heading = new Direction(a_state.m_heading.getHeadingDegrees());
		m_error = a_state.m_error;
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
		" x : "  + m_position.x +
		" y : "  + m_position.y +
		" d : "  + m_heading.getHeadingDegrees();
	}

	public void setError(MatchingError error) {
		m_error = error;
	}

	public float getError() {
		if (m_error == null)
			return 0;
		else
			return m_error.getError();
	}

	
}
