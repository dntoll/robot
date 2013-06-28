package daniel.robot.SLAM.ParticleFilter;

import java.util.Random;

import daniel.robot.SLAM.Movement;
import daniel.robot.SLAM.PoseCollection;
import daniel.robot.SLAM.State;
import daniel.robot.sensors.SensorReading;

public class ParticleFilter {
	
	private static Random RANDOM = new Random();
	private static int NUMBER_OF_PARTICLES = 3000;
	private Particle[] m_particles;
	
	public ParticleFilter(State a_startState) {
		m_particles = new Particle[NUMBER_OF_PARTICLES];
		for (int i = 0; i< NUMBER_OF_PARTICLES; i++) {
			State state = new State(a_startState.getRobotPosition(), a_startState.m_heading);
			float weight = 1.0f / (float)NUMBER_OF_PARTICLES;
			m_particles[i] = new Particle(state, weight);
			
		}
	}
	
	private ParticleFilter(Particle[] a_newParticles) {
		m_particles = a_newParticles;
	}
	
	
	
	public ParticleFilter ResampleParticles() {
		
		
		int newParticleIndex = 0;
		int N = m_particles.length;
		Particle[] newParticles = new Particle[N];
   
		int index = RANDOM.nextInt(N);
        float beta = 0.0f;
        float mw = max();
	    for (int i = 0; i < N; i++) {
	       beta += RANDOM.nextFloat() * 2.0f * mw;
	       
	       while (beta > m_particles[index].getWeight()) {
	           beta -= m_particles[index].getWeight();
	           index = (index + 1) % N;
	       }
	       newParticles[newParticleIndex] = new Particle(m_particles[index]);
	       newParticleIndex++;
	    }
	    
	    ParticleFilter ret = new ParticleFilter(newParticles);
	    
		return ret;
	}
	
	public State getBestGuess() {
		
		int bestIndex = 0;
		for (int i= 1; i < NUMBER_OF_PARTICLES; i++) {
			if (m_particles[i].getWeight() > m_particles[bestIndex].getWeight()) {
				bestIndex = i;
			}
		}
		return m_particles[bestIndex].getState();
	}

	
	
	public void move(Movement a_move, float a_headingVariance, float a_positionVariance) {
		for (int i = 0; i< NUMBER_OF_PARTICLES; i++) {
			m_particles[i].getState().move(a_move, a_headingVariance, a_positionVariance);
		}
	}

	public void setWeights(PoseCollection a_world, SensorReading a_reading) throws Exception {
		for (int i = 0; i< NUMBER_OF_PARTICLES; i++) {
			m_particles[i].setWeight(a_world.measurementProbability( m_particles[i].getState(), a_reading ).getError());
			
			if (Float.isNaN(m_particles[i].getWeight())) {
				m_particles[i].setWeight(0.0f);
			}
		}
	}


	public int getSize() {
		return NUMBER_OF_PARTICLES;
	}

	
	public State getState(int a_index) {
		return m_particles[a_index].getState();
	}
	
	private float max() {
		int bestIndex = 0;
		for (int i= 1; i < NUMBER_OF_PARTICLES; i++) {
			if (m_particles[i].getWeight() > m_particles[bestIndex].getWeight()) {
				bestIndex = i;
			}
		}
		return m_particles[bestIndex].getWeight();
	}

	
}
