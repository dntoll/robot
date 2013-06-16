package daniel.robot;

import java.util.Random;

import daniel.robot.sensors.SensorReading;

public class ParticleFilter {
	
	private static Random rand = new Random();
	private static int NUMBER_OF_PARTICLES = 1000;
	float[] m_weights;
	State[] m_states;
	
	public ParticleFilter(State a_startState) {
		m_states = new State[NUMBER_OF_PARTICLES];
		m_weights = new float[NUMBER_OF_PARTICLES];
		for (int i = 0; i< NUMBER_OF_PARTICLES; i++) {
			m_states[i] = new State(a_startState.m_position, a_startState.m_heading);
			m_weights[i] = 1.0f / (float)NUMBER_OF_PARTICLES;
		}
	}
	

	private ParticleFilter(State[] newParticles, float[] a_weights) {
		m_states = newParticles;
		m_weights = a_weights;
	}

	private float max() {
		int bestIndex = 0;
		for (int i= 1; i < m_weights.length; i++) {
			if (m_weights[i] > m_weights[bestIndex]) {
				bestIndex = i;
			}
		}
		return m_weights[bestIndex];
	}
	
	public ParticleFilter ResampleParticles() {
		
		
		int newParticleIndex = 0;
		int N = m_states.length;
		State[] newParticles = new State[N];
		float[] newWeights = new float[N];
   
		int index = rand.nextInt(N);
        float beta = 0.0f;
        float mw = max();
	    for (int i = 0; i < N; i++) {
	       beta += rand.nextFloat() * 2.0f * mw;
	       
	       while (beta > m_weights[index]) {
	           beta -= m_weights[index];
	           index = (index + 1) % N;
	       }
	       newParticles[newParticleIndex] = new State(m_states[index]);
	       newWeights[newParticleIndex] = m_weights[index];
	       newParticleIndex++;
	    }
	    
	    ParticleFilter ret = new ParticleFilter(newParticles, newWeights);
	    
		return ret;
	}
	
	public State getBestGuess() {
		
		int bestIndex = 0;
		for (int i= 1; i < m_weights.length; i++) {
			if (m_weights[i] > m_weights[bestIndex]) {
				bestIndex = i;
			}
		}
		return m_states[bestIndex];
	}

	public void move(float forward, float headingChange, float headingVariance, float positionVariance) {
		for (int i = 0; i< NUMBER_OF_PARTICLES; i++) {
			m_states[i].move(forward, headingChange, headingVariance, positionVariance);
		}	
	}

	public void setWeights(World world, SensorReading Z) throws Exception {
		for (int i = 0; i< NUMBER_OF_PARTICLES; i++) {
			m_weights[i] = world.measurementProbability( m_states[i], Z );
			
			if (Float.isNaN(m_weights[i])) {
				m_weights[i] = 0.0f;
			}
		}
	}


	public int getSize() {
		return NUMBER_OF_PARTICLES;
	}

	
	public State getState(int p) {
		return m_states[p];
	}
}
