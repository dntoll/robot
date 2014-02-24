package daniel.robot.SLAM;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import daniel.robot.Direction;
import daniel.robot.glWindow.model.DirectionalReading;
import daniel.robot.glWindow.model.DistanceSensorReadings;
import daniel.robot.glWindow.model.State;
import daniel.robot.sensors.IRReading;

/**
 * Bitmap map of robot world, starts with 0.0 in the middle
 * @author dntoll
 *
 */ 
public class Map {
	LandmarkCollection m_landmarks = new LandmarkCollection();
	MapData freeArea;
	
	
	public Map(State a_bestGuess, DistanceSensorReadings sense, Map parentMap) {
		
		if (parentMap != null) {
			m_landmarks.copy(parentMap.m_landmarks);
			freeArea = new MapData(parentMap.freeArea);
		} else {
			freeArea = new MapData(100, 10);
		}
		
		for (DirectionalReading distanceReading : sense.getReadings().values()) {
			freeArea.draw(distanceReading.getSharp1Distance(), a_bestGuess.getRobotPosition(), a_bestGuess.m_heading.getHeadDirection(distanceReading.getServoDirection()));
			
			
			//if (distanceReading.getSharp1Distance().wit())
			{
				Point2D.Float position = getIRPosition(a_bestGuess, distanceReading);
				float distanceMeasured= distanceReading.getSharp1Distance().getMedian();
				
				float deviation = distanceReading.getSharp1Distance().getStdev();
				
				Landmark lm = new Landmark(position, deviation, distanceReading.getSharp1Distance().getMin());
				
				Prediction prediction = getDistance(a_bestGuess, distanceReading.getServoDirection(), distanceReading.getSharp1Distance().getBeamWidth());
				//no prediction is found
				if (prediction == null) {
					//no prediction in this direction just add...
					m_landmarks.add(lm);
				} else {
					
					//the new prediction should be added if its closer
//					boolean newLandMarkIsCloser;
					boolean newLandMarkIsFurtherAway = lm.getDifference(prediction.landmark) > 100;
//					boolean newLandMarkIsHasBetterSTDEV;
					
					if (distanceMeasured < 150 && 
						distanceMeasured < prediction.getDistance() + 30 && lm.deviation < 10) {
						m_landmarks.add(lm);
					} else {
					
						//Improvements
						if (lm.isBetter(prediction) && distanceMeasured < 150) 
						{
							//replace
							m_landmarks.remove(prediction.landmark);
							m_landmarks.add(lm);
						} else  if(newLandMarkIsFurtherAway && distanceMeasured < 150) {
							//remove false landmarks
							m_landmarks.remove(prediction.landmark);
						}
						
					}
				}
			}
		}
	}


	private Point2D.Float getIRPosition(State a_bestGuess,
			DirectionalReading distanceReading) {
		float distance = distanceReading.getSharp1Distance().getMedian();
		Direction direction = a_bestGuess.m_heading.getHeadDirection(distanceReading.getServoDirection());
		Point2D.Float headPosition = a_bestGuess.getRobotPosition();
		
		float x = headPosition.x + direction.getX() * distance;
		float y = headPosition.y + direction.getY() * distance;
		Point2D.Float position = new Point2D.Float(x ,y );
		return position;
	}
	
	public Prediction getDistance(State state, Direction servo, float a_beamWidth) {
		Direction direction = state.m_heading.getHeadDirection(servo);

		Point2D.Float robotPosition = state.getRobotPosition();
		
		Prediction ret = m_landmarks.getClosestLandMark(a_beamWidth, direction, robotPosition);
				
		return ret;
	}


	


	public boolean isFree(int x, int y) {
		return freeArea.isFree(x, y);
	}


	public boolean isBlocked(int x, int y) {
		return freeArea.isBlocked(x, y);
	}


	public MapData getMap() {
		// TODO Auto-generated method stub
		return freeArea;
	}


	public Landmark[] getAllLandmarks() {
		return m_landmarks.getAllLandmarks();
	}

	
	

}
