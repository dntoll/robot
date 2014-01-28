package daniel.robot.SLAM;

import java.awt.geom.Point2D.Float;
import daniel.robot.Direction;
import daniel.robot.SLAM.ParticleFilter.ParticleFilter;
import daniel.robot.sensors.SensorReading;

public class SLAM {
	
	public PoseCollection m_world;
	//Robot 		   m_robot;
	
	/*public SLAM(Robot a_robot) throws Exception {
		m_robot = a_robot;
		
	}*/

	public void startUp() throws Exception {
		//Direction compassDirection = m_robot.SenseSome().m_compassDirection;
		//State startState = new State(new Float(0.0f, 0.0f), compassDirection);
		
		//SensorReading reading = m_robot.SenseAll();
		
		//ParticleFilter startPosition = new ParticleFilter(startState, reading);
		
		
		
		//m_world = new PoseCollection(startPosition, reading);
		
		//MatchingError error = MatchingError.getMatchingError( m_world.getLastPose().getBestMap(), startState, reading);
		//System.out.println(error);
		
	}
	
	public void updateAfterMovement() throws Exception {
		
		
		float distance = -13.0f;
		float turn = 0.0f;
		
		Movement move = new Movement(distance, turn);
		//m_robot.move(move);
		//Wake up and sense!
		//SensorReading Z = m_robot.SenseAll();
		
		//m_world.moveAndSense(move, Z);
		
		
		
		//MatchingError error = MatchingError.getMatchingError( m_world.getLastPose().getBestMap(), m_world.getLastPose().getBestGuess().getState(), Z);
		//System.out.println(error);
	}
}
