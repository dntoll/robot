package daniel.robot.slam;

import daniel.robot.glWindow.model.DirectionalReadingCollection;
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
	public DirectionalReadingCollection  m_sensorReading;
	public Movement		  m_movement;
	
	
	public Pose(ParticleFilter a_particles, DirectionalReadingCollection a_reading, Movement a_movement) {
		m_position = a_particles;
		m_sensorReading = a_reading;
		m_movement = a_movement;
		
	}
	@Override
	public ParticleFilter getParticleFilter() {
		return m_position;
	}
	
	public State getBestGuessPosition() {
		return m_position.getBestGuess().getState();
	}

	public Map getBestMap() {
		return m_position.getBestGuess().getMap();
	}


	@Override
	public DirectionalReadingCollection getDistanceSensorReadings() {
		return m_sensorReading;
	}


	@Override
	public float getError() {
		return m_position.getBestGuess().getWeight();
	}
	
}