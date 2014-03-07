package daniel.robot.SLAM.ParticleFilter;

import java.util.Random;

import daniel.robot.SLAM.Movement;
import daniel.robot.glWindow.model.DistanceSensorReadings;
import daniel.robot.glWindow.model.PoseCollection;
import daniel.robot.glWindow.model.State;

public class ParticleFilter {
	
	private static Random RANDOM = new Random();
	private static int NUMBER_OF_PARTICLES = 25;
	private Particle[] m_particles;
	
	public ParticleFilter(State a_startState, DistanceSensorReadings sense) {
		m_particles = new Particle[NUMBER_OF_PARTICLES];
		for (int i = 0; i< NUMBER_OF_PARTICLES; i++) {
			State state = new State(a_startState.getRobotPosition(), a_startState.m_heading);
			float weight = 1.0f / (float)NUMBER_OF_PARTICLES;
			m_particles[i] = new Particle(state, weight, sense);
			
		}
	}
	
	public ParticleFilter(ParticleFilter a_parent, Movement move, DistanceSensorReadings sense, PoseCollection world) throws Exception {
		
		m_particles = new Particle[NUMBER_OF_PARTICLES];
		for (int i = 0; i< NUMBER_OF_PARTICLES; i++) {
			m_particles[i] = new Particle(a_parent.m_particles[i]);
		}
		
		move(move);
		
		AddMaps(sense);
		
		setWeights( sense, world);
		
		if (shouldResample()) {
			
			printWeights();
			ResampleParticles();
			
			printWeights();
		}
		
		
		
	}
	
	private void AddMaps(DistanceSensorReadings sense) {
		for (int i= 0; i < NUMBER_OF_PARTICLES; i++) {
			m_particles[i].addMap(sense);
		}
		
	}

	int shouldResample = 0;
	private boolean shouldResample() {
		//http://www.informatik.uni-freiburg.de/~stachnis/rbpf-tutorial/iros05tutorial-gridrbpf-handout.pdf
		
		
		return true;
	}

	private void printWeights() {
		float totalWeight = 0.0f;
		for (int i = 0; i< NUMBER_OF_PARTICLES; i++) {
			System.out.print(":" + m_particles[i].getWeight() );
			totalWeight += m_particles[i].getWeight();
		}
		System.out.println();
		System.out.println("totalWeight:" + totalWeight);
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
	
	private void ResampleParticles() {
		int N = m_particles.length + 0;
		Particle[] newParticles = new Particle[N];
   
		int index = RANDOM.nextInt(m_particles.length);
        float beta = 0.0f;
        float totalWeight = totalWeight();
        float weight;
	    for (int i = 0; i < N; i++) {
	    	
	    	index = 0;
	    	weight = m_particles[index].getWeight();
	       
	    	beta = RANDOM.nextFloat() * totalWeight;
	       
	    	while (beta > weight) {
	    		index++;
	    		weight += m_particles[index].getWeight();
	    	}
	    	newParticles[i] = m_particles[index];
	    }
	    
	    m_particles = newParticles;
	    
	}
	


	
	
	private float totalWeight() {
		float totalWeight = 0.0f;
		for (int i = 0; i< NUMBER_OF_PARTICLES; i++) {
			totalWeight += m_particles[i].getWeight();
		}
		return totalWeight;
	}

	private void move(Movement a_move) {
		for (int i = 0; i< NUMBER_OF_PARTICLES; i++) {
			m_particles[i].getState().move(a_move);
		}
	}

	private void setWeights(DistanceSensorReadings sense, PoseCollection world) throws Exception {
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
