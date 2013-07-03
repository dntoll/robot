package daniel.robot.SLAM.ParticleFilter;

import daniel.robot.SLAM.Map;
import daniel.robot.SLAM.MatchingError;
import daniel.robot.SLAM.State;
import daniel.robot.sensors.SensorReading;

public class Particle {
	
	private float m_weight;
	private MatchingError m_error;
	private State m_state;
	private Map m_map = null;
	private Particle m_parent;
	
	Particle(State a_state, float a_weight, SensorReading a_reading) {
		m_state = a_state;
		m_weight = a_weight;
		
		m_parent = null;
		
		addMap(a_reading);
	}
		
	public Particle(Particle a_parent) {
		
		m_parent = a_parent;
		m_state = new State(a_parent.getState());
		m_weight = a_parent.getWeight();
		
	}

	public float getWeight() {
		return m_weight;
	}

	public State getState() {
		return m_state;
	}

	public void setWeight(float a_weight) {
		m_weight = a_weight;
	}
	
	public void calculateWeight(SensorReading a_reading) throws Exception {
		m_error = MatchingError.getMatchingError(m_parent.m_map, m_state, a_reading);
		m_weight = m_error.getError();
		
		if (Float.isNaN(getWeight())) {
			setWeight(0.0f);
		}
	}

	public void addMap(SensorReading a_sense) {
		if (m_map == null) {
			if (m_parent != null) {
				m_map = new Map(m_state, a_sense, m_parent.m_map);
			} else {
				m_map = new Map(m_state, a_sense, null);
			}
		}
	}

	public Map getMap() {
		return m_map;
	}

	public Particle getParent() {
		return m_parent;
	}

	
}
