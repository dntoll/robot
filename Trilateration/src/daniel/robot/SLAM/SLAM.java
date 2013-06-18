package daniel.robot.SLAM;

import java.awt.geom.Point2D.Float;
import daniel.robot.Direction;
import daniel.robot.Robot;
import daniel.robot.sensors.SensorReading;

public class SLAM {
	
	public MeasurementCollection m_world = new MeasurementCollection();
	Robot m_robot;
	ParticleFilter m_particles;
	
	public SLAM(Robot robot) throws Exception {
		m_robot = robot;
		
	}

	public void startUp() throws Exception {
		Direction compassDirection = m_robot.SenseSome().m_compassDirection;
		State startState = new State(new Float(0.0f, 0.0f), compassDirection);
		
		m_particles = new ParticleFilter(startState);
		
		SensorReading reading = m_robot.SenseAll();
		m_world.append(m_particles, reading);
	}
	
	public void updateAfterMovement() throws Exception {
		float distance = 3.0f;
		m_robot.move(distance);
		//Wake up and sense!
		SensorReading Z = m_robot.SenseAll();
		
		
		
		
		m_particles.move(distance, 0.0f, 2.0f, 3.0f);
		
		m_particles.setWeights(m_world, Z);
		
		m_particles = m_particles.ResampleParticles();
		
		m_world.append(m_particles, Z);
		
		
	}
}
