package daniel.robot.SLAM;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;
import java.util.ArrayList;
import daniel.robot.Direction;
import daniel.robot.glWindow.model.DirectionalReading;
import daniel.robot.glWindow.model.DistanceSensorReadings;
import daniel.robot.glWindow.model.State;
import daniel.robot.sensors.SharpMeasurement;

/**
 * Bitmap map of robot world, starts with 0.0 in the middle
 * @author dntoll
 *
 */ 
public class Map {
	ILandmarkCollection m_landmarks = new LandMarkCollectionTree();
	//MapData freeArea;
	
	
	public Map(State a_bestGuess, DistanceSensorReadings sense, Map parentMap) {
		
		if (parentMap != null) {
			m_landmarks.copy(parentMap.m_landmarks);
	//		freeArea = new MapData(parentMap.freeArea);
		} else {
	//		freeArea = new MapData(300, 5);
		}
		
		for (DirectionalReading distanceReading : sense.getReadings().values()) {
	//		freeArea.draw(distanceReading.getSharp1Distance(), a_bestGuess.getRobotPosition(), a_bestGuess.m_heading.getHeadDirection(distanceReading.getServoDirection()));

			SharpMeasurement sharp1Distance = distanceReading.getBestDistance();
			Point2D.Float position = getIRPosition(a_bestGuess, sharp1Distance, distanceReading.getServoDirection());
			
			float deviation = sharp1Distance.getStdev();
			
			Landmark lm = new Landmark(position, deviation, sharp1Distance.getMin());
			
			Prediction prediction = getDistance(a_bestGuess, distanceReading.getServoDirection(), sharp1Distance.getBeamWidth());
			//no prediction is found
			if (prediction == null) {
				//no prediction in this direction just add...
				m_landmarks.add(lm);
			} else {
				addWithExisting(sharp1Distance, lm, prediction);
			}

		}
	}


	


	private void addWithExisting(SharpMeasurement sharp1Distance, Landmark lm, Prediction prediction) {
		
		float distanceMeasured= sharp1Distance.getMedian();
		//add closer landmark
		if (distanceMeasured < sharp1Distance.getReliableDistance() && 
			distanceMeasured < prediction.getDistance() - 15 && lm.deviation < 10) {
			m_landmarks.add(lm);
		} else {

			//the new prediction should be removed if its much further away
			//to remove false readings
			
			
			//Improvements
			if (lm.isBetter(prediction) && distanceMeasured < sharp1Distance.getReliableDistance()) 
			{
				//replace
				if (distanceMeasured > prediction.getDistance()) { 
					m_landmarks.remove(prediction.landmark);
					m_landmarks.add(lm);
				}
			} else  if(shouldRemoveMoreDistantLandmark(sharp1Distance, prediction,
					distanceMeasured, lm) ) {
				//remove false landmarks
				m_landmarks.remove(prediction.landmark);
			}
			
		}
	}


	private boolean shouldRemoveMoreDistantLandmark(
			SharpMeasurement sharp1Distance, Prediction prediction,
			float distanceMeasured, Landmark lm) {
		
		boolean newLandMarkIsMuchFurtherAway = lm.getDifference(prediction.landmark) > 100;
		
		return newLandMarkIsMuchFurtherAway && 
				  distanceMeasured > sharp1Distance.getReliableDistance() && 
				  prediction.getDistance() < sharp1Distance.getReliableDistance();
	}

	
	private Float getIRPosition(State a_bestGuess,
			SharpMeasurement sharp1Distance, Direction servoDirection) {
		
		float distance = sharp1Distance.getMedian();
		Direction direction = a_bestGuess.m_heading.getHeadDirection(servoDirection);
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
		return false;//freeArea.isFree(x, y);
	}


	public boolean isBlocked(int x, int y) {
		return false;//freeArea.isBlocked(x, y);
	}


	public MapData getMap() {
		// TODO Auto-generated method stub
		return null;//freeArea;
	}


	public Landmark[] getAllLandmarks() {
		return m_landmarks.getAllLandmarks();
	}

	
	

}
