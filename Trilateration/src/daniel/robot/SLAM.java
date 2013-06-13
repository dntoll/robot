package daniel.robot;

import java.awt.geom.Point2D.Float;
import java.util.Random;

import daniel.robot.sensors.SensorReading;

public class SLAM {
	
	public World m_world = new World();
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
		//Wake up and sense!
		SensorReading Z = m_robot.SenseAll();
		
		//WE MOVE THE PARTICLES TO REFLECT UNCERTAINTY
		m_particles.move(0.0f, 0.0f, 0.0f, 20.0f, 3.0f);
		m_particles.setWeights(m_world, Z);
		m_particles = m_particles.ResampleParticles();
		
		m_world.append(m_particles, Z);
	}
}
