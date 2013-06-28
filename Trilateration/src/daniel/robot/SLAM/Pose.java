package daniel.robot.SLAM;

import daniel.robot.SLAM.ParticleFilter.ParticleFilter;
import daniel.robot.sensors.SensorReading;


/**
 * A Pose is one position (probability distribution) 
 * A reading at that position
 * A Pose can create a map 
 * @author Daniel
 */
public class Pose {
	public ParticleFilter m_position;
	public SensorReading  m_sensorReading;
	public Map 			  m_bestGuessMap;
	public Movement		  m_movement;
	
	
	public Pose(ParticleFilter a_particles, SensorReading a_reading, Movement a_movement) {
		m_position = a_particles;
		m_sensorReading = a_reading;
		m_movement = a_movement;
		m_bestGuessMap = new Map(a_particles.getBestGuess(), a_reading);
	}
	
	
	public State getBestGuess() {
		return m_position.getBestGuess();
	}

	public float getDistance(State a_state, float a_servoDirection, float a_beamWidth) throws Exception {
		return m_bestGuessMap.getDistance(a_state, a_servoDirection, a_beamWidth);
	}


	
}