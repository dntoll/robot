package daniel.robot.SLAM.ParticleFilter;

import daniel.robot.SLAM.State;

public class Particle {
	
	private float m_weight;
	private State m_state;
	
	Particle(State a_state, float a_weight) {
		m_state = a_state;
		m_weight = a_weight;
	}
		
	public Particle(Particle a_particle) {
		m_state = new State(a_particle.getState());
		m_weight = a_particle.getWeight();
	}

	float getWeight() {
		return m_weight;
	}

	public State getState() {
		return m_state;
	}

	public void setWeight(float a_weight) {
		m_weight = a_weight;
	}
}
