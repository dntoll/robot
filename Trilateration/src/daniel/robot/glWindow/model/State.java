package daniel.robot.glWindow.model;

import java.awt.geom.Point2D.Float;
import daniel.robot.Direction;
import daniel.robot.slam.Movement;

public class State {
	private Float m_position = new Float(); //in degrees
	public Direction m_heading;
	
	
	
	
	public Float getRobotPosition() {
		return m_position;
	}
	
		
	public State(Float a_position, Direction a_compassDirection) {
		m_position.x = a_position.x;
		m_position.y = a_position.y;
		m_heading = new Direction(a_compassDirection.getHeadingDegrees());
		
	}
	
	public State(State a_state) {
		m_position.x = a_state.m_position.x;
		m_position.y = a_state.m_position.y;
		m_heading = new Direction(a_state.m_heading.getHeadingDegrees());
	}

	
	
	public void move(Movement a_move) {
		
		
		float angle = a_move.getPossibleAngle();
		float distanceMoved = a_move.getDistanceMoved();
		
		m_heading = m_heading.getTurned(angle);
		
		float x = m_heading.getX() * distanceMoved; 
		float y = m_heading.getY() * distanceMoved;
		
		//m_position = new Float();
		m_position.x = m_position.x + x;// + rand.nextFloat() * (a_positionVariance*2.0f)-a_positionVariance;
		m_position.y = m_position.y + y;// + rand.nextFloat() * (a_positionVariance*2.0f)-a_positionVariance;
	}
	
	public void moveFlawLess(Movement a_move) {
		m_heading = m_heading.getTurned(a_move.getAngle());
		
		float x = m_heading.getX() * a_move.getDistance(); 
		float y = m_heading.getY() * a_move.getDistance();
		
		//m_position = new Float();
		m_position.x = m_position.x + x;// + rand.nextFloat() * (a_positionVariance*2.0f)-a_positionVariance;
		m_position.y = m_position.y + y;// + rand.nextFloat() * (a_positionVariance*2.0f)-a_positionVariance;
	}
	
	public String toString() {
		return 
		" x : "  + m_position.x +
		" y : "  + m_position.y +
		" d : "  + m_heading.getHeadingDegrees();
	}

	

	
}
