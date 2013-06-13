package daniel.robot;

import java.util.Random;

import daniel.robot.sensors.SensorReading;

public class ParticleFilter {
	
	private static Random rand = new Random();
	private static int m_numberOfParticles = 1000;
	float[] m_weights;
	State[] m_particles;
	
	public ParticleFilter(State startState) {
		m_particles = new State[m_numberOfParticles];
		m_weights = new float[m_numberOfParticles];
		for (int i = 0; i< m_numberOfParticles; i++) {
			m_particles[i] = new State(startState.m_position, startState.m_heading);
			m_weights[i] = 1.0f / (float)m_numberOfParticles;
		}
	}
	

	public ParticleFilter(State[] newParticles) {
		m_particles = newParticles;
		m_weights = new float[m_numberOfParticles];
		for (int i = 0; i< m_numberOfParticles; i++) {
			m_weights[i] = 1.0f / (float)m_numberOfParticles;
		}
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
		int N = m_particles.length;
		State[] newParticles = new State[N];
   
		int index = rand.nextInt(N);
        float beta = 0.0f;
        float mw = max();
	    for (int i = 0; i < N; i++) {
	       beta += rand.nextFloat() * 2.0f * mw;
	       
	       while (beta > m_weights[index]) {
	           beta -= m_weights[index];
	           index = (index + 1) % N;
	       }
	       newParticles[newParticleIndex] = new State(m_particles[index].m_position, m_particles[index].m_heading);
	       newParticleIndex++;
	    }
	    
	    ParticleFilter ret = new ParticleFilter(newParticles);
	    
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

	public void move(float x, float y, float headingChange, float headingVariance, float positionVariance) {
		for (int i = 0; i< m_numberOfParticles; i++) {
			m_particles[i].move(x, y, headingChange, headingVariance, positionVariance);
		}	
	}

	public void setWeights(World world, SensorReading Z) throws Exception {
		for (int i = 0; i< m_numberOfParticles; i++) {
			m_weights[i] = world.measurementProbability( m_particles[i], Z );
		}
	}
}
