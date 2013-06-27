package daniel.robot.SLAM;

import daniel.robot.sensors.DistanceBase;
import daniel.robot.sensors.IRReading;
import daniel.robot.sensors.SensorReading;
import daniel.robot.sensors.SonarReading;
import daniel.robot.statistics.Gaussian;

public class MatchingError {
	//Error statistics compared to last measurement
	public float m_directionalError;
	public float m_irError;
	public float m_sonarError;
	public float m_overlap;
	public int m_numMatching;
	
	
	private MatchingError() {
		m_directionalError = 0.0f;
		m_irError = 0.0f;
		m_sonarError = 0.0f;
		m_overlap = 0.0f;
		m_numMatching = 0;
	}
	
	public String toString() {
		return 
		" de: " + m_directionalError + 
	    " so: " + m_overlap + 
		" di: " + (m_irError + m_sonarError); 
	}
	
	
	public float getError() {
		float match = Gaussian.gaussian(0, SonarReading.SONAR_DISTANCE_ERROR, (float)m_sonarError / (float)m_numMatching);
		float match2 = 1.0f;//Gaussian.gaussian(0, IRReading.IR_DISTANCE_NOISE, (float)m_irError / (float)m_numMatching);
		float directionalProb = 1.0f;// Gaussian.gaussian(0, compassNoise, state.m_directionalError);
		return directionalProb * (match) *(match2) * m_overlap;
	}
	
	
	public static MatchingError getMatchingError(Pose a_known, State a_newState, SensorReading a_reading) {
		MatchingError error = new MatchingError();
		
		error.m_directionalError = a_newState.m_heading.GetDifferenceInDegrees(a_reading.m_compassDirection);
		error.m_numMatching = 0;
		error.m_irError = 0.0f;
		error.m_sonarError = 0.0f;
		
		for (IRReading ir : a_reading.m_ir) {
			error.m_irError += MatchReading(a_known, a_newState, error, ir);
		}
		/*for (SonarReading sonar : a_reading.m_sonar) {
			error.m_sonarError += MatchReading(a_known, a_newState, error, sonar);
		}*/
		
		error.m_overlap = (float)error.m_numMatching/(float)a_reading.m_ir.length;
		
		return error;
	}

	private static float MatchReading(Pose a_known, State a_newState,
			MatchingError error, DistanceBase ir) {
		float ret = 0;
		try {
			float degrees = ir.getBeamWidth();
			float expectedDistance = a_known.getDistance(a_newState, ir.m_servo, degrees);
			float distance = ir.m_distance;
			if (ir.okDistance()) {
				error.m_numMatching++;
				ret += Math.sqrt((distance - expectedDistance)*(distance - expectedDistance));
			}
		} catch (Exception e) {
			
		}
		return ret;
	}
}
