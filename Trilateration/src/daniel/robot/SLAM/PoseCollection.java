package daniel.robot.SLAM;

import java.util.ArrayList;
import daniel.robot.sensors.DistanceReading;
import daniel.robot.sensors.IRReading;
import daniel.robot.sensors.SensorReading;
import daniel.robot.statistics.Gaussian;

public class PoseCollection {
	
	public ArrayList<Pose> m_sensorScans = new ArrayList<Pose>();
	
	
	PoseCollection() {
		
	}

	public void append(ParticleFilter a_particles, SensorReading a_reading, Movement a_movement) {
		
		State s = a_particles.getBestGuess();
		m_sensorScans.add( new Pose(a_particles, a_reading, a_movement) );
		System.out.println(s.toString());
		System.out.println(a_reading.toString());
	}

	public float measurementProbability(State a_state, SensorReading a_reading) throws Exception {
		a_state.m_directionalError = a_state.m_heading.GetDifferenceInDegrees(a_reading.m_compassDirection);
		float directionalProb = 1.0f;// Gaussian.gaussian(0, compassNoise, state.m_directionalError);
		int numMatching = 0;
		
		
		a_state.m_distanceError = 0.0f;
		for (DistanceReading ir : a_reading.m_distances) {
			try {
				float degrees = ir.getBeamWidth();
				float expectedDistance = getLastReading().getDistance(a_state, ir.m_servo, degrees);
				float distance = ir.m_distance;
				numMatching++;
				
				a_state.m_distanceError += Math.sqrt((distance - expectedDistance)*(distance - expectedDistance));
			} catch (Exception e) {
				
			}
		}
		a_state.m_overlap = (float)numMatching/(float)a_reading.m_distances.length;
		
		float match = Gaussian.gaussian(0, IRReading.IR_DISTANCE_NOISE, (float)a_state.m_distanceError / (float)numMatching);
		
		return directionalProb * (match) * a_state.m_overlap;
		
	}

	private Pose getLastReading() {
		return m_sensorScans.get(m_sensorScans.size()-1);
	}

	
}
