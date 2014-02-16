package daniel.robot.SLAM.ParticleFilter;

import java.util.Random;

import daniel.robot.SLAM.Movement;
import daniel.robot.glWindow.model.DistanceSensorReadings;
import daniel.robot.glWindow.model.State;

public class ParticleFilter {
	
	private static Random RANDOM = new Random();
	private static int NUMBER_OF_PARTICLES = 1000;
	private Particle[] m_particles;
	
	public ParticleFilter(State a_startState, DistanceSensorReadings sense) {
		m_particles = new Particle[NUMBER_OF_PARTICLES];
		for (int i = 0; i< NUMBER_OF_PARTICLES; i++) {
			State state = new State(a_startState.getRobotPosition(), a_startState.m_heading);
			float weight = 1.0f / (float)NUMBER_OF_PARTICLES;
			m_particles[i] = new Particle(state, weight, sense);
			
		}
	}
	
	public ParticleFilter(ParticleFilter a_parent, Movement move, DistanceSensorReadings sense) throws Exception {
		
		m_particles = new Particle[NUMBER_OF_PARTICLES];
		for (int i = 0; i< NUMBER_OF_PARTICLES; i++) {
			m_particles[i] = new Particle(a_parent.m_particles[i]);
		}
		
		move(move);
		setWeights( sense);
		
		ResampleParticles(sense);
	}
	
	public Particle getBestGuess() {
		
		int bestIndex = 0;
		for (int i= 1; i < NUMBER_OF_PARTICLES; i++) {
			if (m_particles[i].getWeight() > m_particles[bestIndex].getWeight()) {
				bestIndex = i;
			}
		}
		return m_particles[bestIndex];
	}		
	
	public int getSize() {
		return NUMBER_OF_PARTICLES;
	}

	
	public State getState(int a_index) {
		return m_particles[a_index].getState();
	}
	
	private void ResampleParticles(DistanceSensorReadings sense) {
		
		
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
	       
	       m_particles[index].addMap(sense);
	       newParticles[newParticleIndex] = m_particles[index];
	       newParticleIndex++;
	    }
	    m_particles = newParticles;
	    
	}
	


	
	
	private void move(Movement a_move) {
		for (int i = 0; i< NUMBER_OF_PARTICLES; i++) {
			m_particles[i].getState().move(a_move);
		}
	}

	private void setWeights(DistanceSensorReadings sense) throws Exception {
		for (int i = 0; i< NUMBER_OF_PARTICLES; i++) {
			m_particles[i].calculateWeight(sense );
		}
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
