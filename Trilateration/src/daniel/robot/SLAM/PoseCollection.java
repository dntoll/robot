package daniel.robot.SLAM;

import java.util.ArrayList;

import daniel.robot.SLAM.ParticleFilter.ParticleFilter;
import daniel.robot.sensors.SensorReading;

public class PoseCollection {
	
	public ArrayList<Pose> m_sensorScans = new ArrayList<Pose>();
	
	
	PoseCollection() {
		
	}

	public void append(ParticleFilter a_particles, SensorReading a_reading, Movement a_movement) {
		
		State s = a_particles.getBestGuess();
		m_sensorScans.add( new Pose(a_particles, a_reading, a_movement) );
		System.out.println(s.toString());
		System.out.println(a_reading.toString());
	}

	public MatchingError measurementProbability(State a_newState, SensorReading a_reading) throws Exception {
		MatchingError error = MatchingError.getMatchingError(getLastReading(), a_newState, a_reading);
		
		a_newState.setError(error);
		return error;
	}

	private Pose getLastReading() {
		return m_sensorScans.get(m_sensorScans.size()-1);
	}
	
	public float getError() {
		return getLastReading().getBestGuess().getError();
	}

	
}
