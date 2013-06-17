package daniel.robot.SLAM;

import java.util.ArrayList;
import java.util.List;

import daniel.robot.sensors.DistanceReading;
import daniel.robot.sensors.IRReading;
import daniel.robot.sensors.SensorReading;
import daniel.robot.statistics.Gaussian;

public class MeasurementCollection {
	
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
	
	public List<Reading> m_world = new ArrayList<Reading>();
	public Map m_map;
	
	MeasurementCollection() {
		m_map = new Map();
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
		for (DistanceReading ir : reading.m_distances) {
			try {
				float expectedDistance = m_map.getDistance(state, ir.m_servo, IRReading.BEAM_WIDTH);
				float distance = ir.m_distance;
				numMatching++;
				
				state.m_distanceError += Math.sqrt((distance - expectedDistance)*(distance - expectedDistance));
			} catch (Exception e) {
				
			}
		}
		state.m_overlap = (float)numMatching/(float)reading.m_distances.length;
		
		float match = Gaussian.gaussian(0, IRReading.IR_DISTANCE_NOISE, (float)state.m_distanceError / (float)numMatching);
		
		return directionalProb * (match) * state.m_overlap;
		
	}

	

	

	    

	
	
}
