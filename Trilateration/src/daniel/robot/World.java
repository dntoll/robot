package daniel.robot;

import java.util.ArrayList;
import java.util.List;

import daniel.robot.sensors.IRReading;
import daniel.robot.sensors.SensorReading;
import daniel.robot.statistics.Gaussian;

public class World {
	
	public class Reading {
		public Reading(ParticleFilter particles, SensorReading reading) {
			m_particles = particles;
			m_reading = reading;
		}
		public ParticleFilter m_particles;
		public SensorReading m_reading;
		
		
		public State getBestGuess() {
			return m_particles.getBestGuess();
		}
	}

	private static final float compassNoise = 10.0f;
	
	private static final float mapSizeMeters = 5.0f;
	private static final int mapPixelsPerMeter = 30;
	
	public List<Reading> m_world = new ArrayList<Reading>();
	public Map m_map;
	
	World() {
		m_map = new Map(mapSizeMeters, mapPixelsPerMeter);
	}

	public void append(ParticleFilter particles, SensorReading reading) {
		
		State s = particles.getBestGuess();
		m_world.add( new Reading(particles, reading) );
		m_map.Add(s, reading);
		
		System.out.println(s.toString());
		System.out.println(reading.toString());
	}

	

	



	public float measurementProbability(State state, SensorReading reading) throws Exception {
		state.m_directionalError = state.m_heading.GetDifferenceInDegrees(reading.m_compassDirection);
		float directionalProb = 1.0f;// Gaussian.gaussian(0, compassNoise, state.m_directionalError);
		
		int numMatching = 0;
		
		
		state.m_distanceError = 0.0f;
		for (IRReading ir : reading.m_ir) {
			try {
				float expectedDistance = m_map.getDistance(state, ir.m_servo, IRReading.BEAM_WIDTH);
				
				float distance = ir.m_distance;
				
			//	float thisMatch = Gaussian.gaussian(expectedDistance, IRReading.irDistanceNoise, distance);
				//match *= (double)thisMatch;
				numMatching++;
				
				state.m_distanceError += Math.sqrt((distance - expectedDistance)*(distance - expectedDistance));
			} catch (Exception e) {
				
			}
		}
		state.m_overlap = (float)numMatching/(float)reading.m_ir.length;
		
		float match = Gaussian.gaussian(0, IRReading.IR_DISTANCE_NOISE, (float)state.m_distanceError / (float)numMatching);
		
		return directionalProb * (match) * state.m_overlap;
		//throw new Exception("Not implemented");
		
		/*# calculates how likely a measurement should be
        
        prob = 1.0;
        for i in range(len(landmarks)):
            dist = sqrt((self.x - landmarks[i][0]) ** 2 + (self.y - landmarks[i][1]) ** 2)
            prob *= self.Gaussian(dist, self.sense_noise, measurement[i])
        return prob*/
	}

	

	

	    

	
	
}