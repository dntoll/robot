package daniel.robot.SLAM;

import java.awt.geom.Point2D.Float;
import daniel.robot.Direction;
import daniel.robot.Robot;
import daniel.robot.sensors.SensorReading;

public class SLAM {
	
	public PoseCollection m_world = new PoseCollection();
	Robot 		   m_robot;
	ParticleFilter m_latestRobotPosition;
	
	public SLAM(Robot a_robot) throws Exception {
		m_robot = a_robot;
		
	}

	public void startUp() throws Exception {
		Direction compassDirection = m_robot.SenseSome().m_compassDirection;
		State startState = new State(new Float(0.0f, 0.0f), compassDirection);
		
		m_latestRobotPosition = new ParticleFilter(startState);
		
		SensorReading reading = m_robot.SenseAll();
		m_world.append(m_latestRobotPosition, reading, new Movement(0,0));
	}
	
	public void updateAfterMovement() throws Exception {
		
		
		float distance = 0.0f;
		float turn = 0.0f;
		
		Movement move = new Movement(distance, turn);
		m_robot.move(move);
		//Wake up and sense!
		SensorReading Z = m_robot.SenseAll();
		
		m_latestRobotPosition.move(move, turn/2+10, distance/2 + 4);
		
		m_latestRobotPosition.setWeights(m_world, Z);
		
		m_latestRobotPosition = m_latestRobotPosition.ResampleParticles();
		
		m_world.append(m_latestRobotPosition, Z, move);
		
		
	}
}
