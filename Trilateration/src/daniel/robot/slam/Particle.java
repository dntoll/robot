package daniel.robot.slam;

import daniel.robot.glWindow.model.DirectionalReadingCollection;
import daniel.robot.glWindow.model.State;

public class Particle {
	private Particle m_parent;
	private float m_weight;
	private State m_state;
	private Map m_map = null;
	
	private DirectionalReadingCollection m_reading;
	
	Particle(State a_state, float a_weight, DirectionalReadingCollection sense) {
		m_state = a_state;
		m_weight = a_weight;
		
		m_parent = null;
		m_reading = sense;
		
		addMap(sense);
	}
		
	

	public Particle(Particle a_parent, Movement movement,
			DirectionalReadingCollection sense) {
		m_parent = a_parent;
		m_state = new State(a_parent.getState());
		m_weight = a_parent.getWeight();
		m_reading = sense;
		
		m_state.move(movement);
		addMap(sense);
		calculateWeight(sense);
	}

	public float getWeight() {
		return m_weight;
	}

	public State getState() {
		return m_state;
	}

	
	
	private void calculateWeight(DirectionalReadingCollection sense) {
		
		float accumulatedWeight = 0;
		Particle parent = m_parent;
		while (parent != null && parent.m_reading != null) {
			MatchingError error = MatchingError.getMatchingError(m_map, parent.m_state, parent.m_reading);
			accumulatedWeight += error.getWeight();
			parent = parent.m_parent;	
		} 
		MatchingError error = MatchingError.getMatchingError(m_map, m_state, sense);
		float lastError = error.getWeight();
		m_weight = lastError * accumulatedWeight;
		//m_weight = lastError;
		
		if (Float.isNaN(getWeight())) {
			m_weight = 0.0f;
		}
	}

	private void addMap(DirectionalReadingCollection sense) {
		if (m_map == null) {
			if (m_parent != null) {
				m_map = new Map(m_state, sense, m_parent.m_map);
			} else {
				m_map = new Map(m_state, sense, null);
			}
		}
	}

	public Map getMap() {
		return m_map;
	}

	/*public Particle getParent() {
		return m_parent;
	}
*/
	
}
