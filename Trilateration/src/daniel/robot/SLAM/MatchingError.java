package daniel.robot.SLAM;

import daniel.robot.Direction;
import daniel.robot.glWindow.model.DirectionalReading;
import daniel.robot.glWindow.model.DirectionalReadingCollection;
import daniel.robot.glWindow.model.State;
import daniel.robot.sensors.SharpMeasurement;
import daniel.robot.Gaussian;

public class MatchingError {
	//Error statistics compared to last measurement
	public float m_directionalError;
	public double m_irError;
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
	
	
	public float getWeight() {
		return (float) Gaussian.gaussian(0, 1.1f, (float)m_irError / (float)m_numMatching);
	}
	
	
	public static MatchingError getMatchingError(Map a_known, State a_newState, DirectionalReadingCollection sense) {
		MatchingError error = new MatchingError();
		
		error.m_directionalError = a_newState.m_heading.GetDifferenceInDegrees(sense.getCompassDirection());
		error.m_numMatching = 0;
		error.m_irError = 1.0f;
		error.m_sonarError = 0.0f;
		
		int numIrReadings = 0;
		for (DirectionalReading dr : sense.getReadings().values()) {
			SharpMeasurement distance = dr.getBestDistance();
			if (distance.okDistance()) 
			{
				float directionalError = matchReading(a_known, a_newState, error, distance, dr.getServoDirection());
			
				error.m_irError += directionalError;
				numIrReadings++;
			}
		}
		if (numIrReadings > 0) {		
			error.m_overlap = (float)error.m_numMatching / 
							  (float)numIrReadings;
		}
		
		return error;
	}

	
	private static float matchReading(Map a_known, State a_newState,
			MatchingError error, SharpMeasurement measurement, Direction direction) {
		float ret = 0;
		float degrees = measurement.getBeamWidth();
		Prediction expectedDistance = a_known.getPrediction(a_newState, direction, degrees);
		if (expectedDistance != null) {
			float distance = measurement.getMedian();
			
			error.m_numMatching++;
			float delta = distance - expectedDistance.getDistance();
			
			return (float) Math.sqrt(delta*delta);
		}
		return 0;
	}
}
