package daniel.robot.SLAM;

import java.util.Random;

import daniel.robot.sensors.SensorReading;

public class ParticleFilter {
	
	private static Random RANDOM = new Random();
	private static int NUMBER_OF_PARTICLES = 1000;
	float[] m_weights;
	State[] m_particles;
	
	public ParticleFilter(State a_startState) {
		m_particles = new State[NUMBER_OF_PARTICLES];
		m_weights = new float[NUMBER_OF_PARTICLES];
		for (int i = 0; i< NUMBER_OF_PARTICLES; i++) {
			m_particles[i] = new State(a_startState.m_position, a_startState.m_heading);
			m_weights[i] = 1.0f / (float)NUMBER_OF_PARTICLES;
		}
	}
	
	public ParticleFilter ResampleParticles() {
		
		
		int newParticleIndex = 0;
		int N = m_particles.length;
		State[] newParticles = new State[N];
		float[] newWeights = new float[N];
   
		int index = RANDOM.nextInt(N);
        float beta = 0.0f;
        float mw = max();
	    for (int i = 0; i < N; i++) {
	       beta += RANDOM.nextFloat() * 2.0f * mw;
	       
	       while (beta > m_weights[index]) {
	           beta -= m_weights[index];
	           index = (index + 1) % N;
	       }
	       newParticles[newParticleIndex] = new State(m_particles[index]);
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
		return m_particles[bestIndex];
	}

	
	
	public void move(Movement a_move, float a_headingVariance, float a_positionVariance) {
		for (int i = 0; i< NUMBER_OF_PARTICLES; i++) {
			m_particles[i].move(a_move, a_headingVariance, a_positionVariance);
		}
	}

	public void setWeights(PoseCollection a_world, SensorReading a_reading) throws Exception {
		for (int i = 0; i< NUMBER_OF_PARTICLES; i++) {
			m_weights[i] = a_world.measurementProbability( m_particles[i], a_reading );
			
			if (Float.isNaN(m_weights[i])) {
				m_weights[i] = 0.0f;
			}
		}
	}


	public int getSize() {
		return NUMBER_OF_PARTICLES;
	}

	
	public State getState(int a_index) {
		return m_particles[a_index];
	}
	
	private ParticleFilter(State[] a_newParticles, float[] a_weights) {
		m_particles = a_newParticles;
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

	
}
