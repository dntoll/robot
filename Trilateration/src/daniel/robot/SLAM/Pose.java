package daniel.robot.SLAM;

import daniel.robot.SLAM.ParticleFilter.ParticleFilter;
import daniel.robot.glWindow.model.DistanceSensorReadings;
import daniel.robot.glWindow.model.IPose;
import daniel.robot.glWindow.model.State;


/**
 * A Pose is one position (probability distribution) 
 * A reading at that position
 * A Pose can create a map 
 * @author Daniel
 */
public class Pose implements IPose {
	public ParticleFilter m_position;
	public DistanceSensorReadings  m_sensorReading;
	public Movement		  m_movement;
	
	
	public Pose(ParticleFilter a_particles, DistanceSensorReadings a_reading, Movement a_movement) {
		m_position = a_particles;
		m_sensorReading = a_reading;
		m_movement = a_movement;
		
	}
	
	
	public State getBestGuessPosition() {
		return m_position.getBestGuess().getState();
	}

	public Map getBestMap() {
		return m_position.getBestGuess().getMap();
	}


	@Override
	public DistanceSensorReadings getDistanceSensorReadings() {
		return m_sensorReading;
	}


	@Override
	public float getError() {
		return m_position.getBestGuess().getWeight();
	}
	
}