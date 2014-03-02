package daniel.robot.SLAM.ParticleFilter;

import daniel.robot.SLAM.Map;
import daniel.robot.SLAM.MatchingError;
import daniel.robot.glWindow.model.DistanceSensorReadings;
import daniel.robot.glWindow.model.IPose;
import daniel.robot.glWindow.model.PoseCollection;
import daniel.robot.glWindow.model.State;

public class Particle {
	
	private float m_weight;
	private MatchingError m_error;
	private State m_state;
	private Map m_map = null;
	private Particle m_parent;
	private DistanceSensorReadings m_reading;
	
	Particle(State a_state, float a_weight, DistanceSensorReadings a_reading) {
		m_state = a_state;
		m_weight = a_weight;
		
		m_parent = null;
		m_reading = a_reading;
		
		addMap(a_reading);
	}
		
	public Particle(Particle a_parent) {
		
		m_parent = a_parent;
		m_state = new State(a_parent.getState());
		m_weight = a_parent.getWeight();
		m_reading = null;
		
	}

	public float getWeight() {
		return m_weight;
	}

	public State getState() {
		return m_state;
	}

	
	
	public void calculateWeight(DistanceSensorReadings sense) throws Exception {
		
		float accumulatedWeight = 0;
		Particle parent = m_parent;
		while (parent != null && parent.m_reading != null) {
			m_error = MatchingError.getMatchingError(m_map, parent.m_state, parent.m_reading);
			accumulatedWeight += m_error.getWeight();
			parent = parent.m_parent;	
		} 
		m_error = MatchingError.getMatchingError(m_map, m_state, sense);
		float lastError = m_error.getWeight();
		m_weight = lastError * accumulatedWeight;
		
		if (Float.isNaN(getWeight())) {
			m_weight = 0.0f;
		}
	}

	public void addMap(DistanceSensorReadings sense) {
		if (m_map == null) {
			if (m_parent != null) {
				m_map = new Map(m_state, sense, m_parent.m_map);
			} else {
				m_map = new Map(m_state, sense, null);
			}
		}
		m_reading = sense;
	}

	public Map getMap() {
		return m_map;
	}

	public Particle getParent() {
		return m_parent;
	}

	
}
