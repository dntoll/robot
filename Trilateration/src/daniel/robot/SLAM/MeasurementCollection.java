package daniel.robot.SLAM;

import java.util.ArrayList;
import java.util.List;

import daniel.robot.sensors.DistanceReading;
import daniel.robot.sensors.IRReading;
import daniel.robot.sensors.SensorReading;
import daniel.robot.statistics.Gaussian;

public class MeasurementCollection {
	
	public ArrayList<Reading> m_world = new ArrayList<Reading>();
	
	
	MeasurementCollection() {
		
	}

	public void append(ParticleFilter particles, SensorReading reading) {
		
		State s = particles.getBestGuess();
		m_world.add( new Reading(particles, reading) );
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
				float expectedDistance = getLastReading().getDistance(state, ir.m_servo, IRReading.BEAM_WIDTH);
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

	private Reading getLastReading() {
		// TODO Auto-generated method stub
		return m_world.get(m_world.size()-1);
	}

	

	

	    

	
	
}
