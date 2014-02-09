package daniel.robot.SLAM;

import daniel.robot.SLAM.ParticleFilter.Particle;
import daniel.robot.SLAM.ParticleFilter.ParticleFilter;
import daniel.robot.glWindow.model.DistanceSensorReadings;


/**
 * A Pose is one position (probability distribution) 
 * A reading at that position
 * A Pose can create a map 
 * @author Daniel
 */
public class Pose {
	public ParticleFilter m_position;
	public DistanceSensorReadings  m_sensorReading;
	public Movement		  m_movement;
	
	
	public Pose(ParticleFilter a_particles, DistanceSensorReadings a_reading, Movement a_movement) {
		m_position = a_particles;
		m_sensorReading = a_reading;
		m_movement = a_movement;
		
	}
	
	
	public Particle getBestGuess() {
		return m_position.getBestGuess();
	}


	


	public Map getBestMap() {
		return getBestGuess().getMap();
	}
	
}