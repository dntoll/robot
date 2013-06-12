package daniel.robot;

import java.util.ArrayList;
import java.util.List;

public class World {
	
	public class Reading {
		public Reading(ParticleFilter particles, SensorReading reading) {
			m_particles = particles;
			m_reading = reading;
		}
		ParticleFilter m_particles;
		public SensorReading m_reading;
		
		public State getBestGuess() {
			return m_particles.getBestGuess();
		}
	}

	private static final float compassNoise = 3.0f;
	private static final float irDistanceNoise = 5.5f;
	private static final float mapSizeMeters = 5.0f;
	private static final int mapPixelsPerMeter = 30;
	
	public List<Reading> m_world = new ArrayList<Reading>();
	public Map m_map;
	
	World() {
		m_map = new Map(mapSizeMeters, mapPixelsPerMeter);
	}

	public void append(ParticleFilter particles, SensorReading reading) {
		m_world.add( new Reading(particles, reading) );
		
		AddToMap(particles.getBestGuess(), reading);
	}

	

	private void AddToMap(State bestGuess, SensorReading reading) {
		
		m_map.Add(bestGuess, reading);
	}



	public float measurementProbability(State state, SensorReading reading) throws Exception {
		float prob = 1.0f;
		
		prob *= gaussian(state.m_heading, compassNoise, reading.m_compassDirection);
		
		int numMatching = 0;
		for (IRReading ir : reading.m_ir) {
			try {
				float expectedDistance = m_map.getDistance(state, ir.m_servo, 3.0f);
				
				float distance = ir.m_distance;
				
				prob *= gaussian(expectedDistance, irDistanceNoise, distance);
				numMatching++;
			} catch (Exception e) {
				prob *= gaussian(2, irDistanceNoise, 5.0f);
			}
		}
		if (numMatching == 0)
			return 0.0f;
		
		return prob;
		//throw new Exception("Not implemented");
		
		/*# calculates how likely a measurement should be
        
        prob = 1.0;
        for i in range(len(landmarks)):
            dist = sqrt((self.x - landmarks[i][0]) ** 2 + (self.y - landmarks[i][1]) ** 2)
            prob *= self.Gaussian(dist, self.sense_noise, measurement[i])
        return prob*/
	}

	private float gaussian(float mu, float sigma, float x) {
		return phi((x - mu) / sigma) / sigma;
	}
	
	public static float phi(float x) {
        return (float) (Math.exp(-x*x / 2) / Math.sqrt(2.0f * Math.PI));
    }

	

	    

	
	
}
