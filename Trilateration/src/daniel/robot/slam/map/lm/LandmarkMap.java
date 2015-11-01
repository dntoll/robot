package daniel.robot.slam.map.lm;

import java.awt.geom.Point2D;
import daniel.robot.Direction;
import daniel.robot.glWindow.model.DirectionalReading;
import daniel.robot.glWindow.model.DirectionalReadingCollection;
import daniel.robot.glWindow.model.IMap;
import daniel.robot.glWindow.model.IPrediction;
import daniel.robot.glWindow.model.State;
import daniel.robot.sensors.SharpMeasurement;
import daniel.robot.slam.ILandmarkCollection;
import daniel.robot.slam.Prediction;


public class LandmarkMap extends IMap {
	ILandmarkCollection m_landmarks = new LandMarkCollectionTree();
	
	
	public LandmarkMap(State a_bestGuess, DirectionalReadingCollection sense, IMap parent) {
		
		if (parent != null) {
			m_landmarks.copy(((LandmarkMap)parent).m_landmarks);
		}
		
		addLandmarks(a_bestGuess, sense);
	}


	


	@Override
	public IMap createChild(State bestGuess, DirectionalReadingCollection sense) {
		return new LandmarkMap( bestGuess, sense, this);
	}


	private boolean addWithExisting(SharpMeasurement sharp1Distance, Landmark lm, IPrediction prediction) {
		
		float distanceMeasured= sharp1Distance.getMedian();
		//add closer landmark
		if (distanceMeasured < sharp1Distance.getReliableDistance() && 
			distanceMeasured < ((Prediction)prediction).getDistance() - 15 && 
			lm.deviation < 10) {
			m_landmarks.add(lm);
			return true;
			//freeArea.draw(sharp1Distance, lm.pos, a_bestGuess.m_heading.getHeadDirection(distanceReading.getServoDirection()));
		} else {

			
			
			
			//Improvements
			if (lm.isBetter((Prediction)prediction) && 
					distanceMeasured < sharp1Distance.getReliableDistance()) 
			{
				//replace
				if (distanceMeasured > ((Prediction)prediction).getDistance()) { 
					m_landmarks.remove(((Prediction)prediction).getLandmark());
					m_landmarks.add(lm);
					return true;
				}
			} else  if(shouldRemoveMoreDistantLandmark(sharp1Distance, (Prediction)prediction,
					distanceMeasured, lm) ) {
				//the new prediction should be removed if its much further away
				//remove false landmarks
				m_landmarks.remove(((Prediction)prediction).getLandmark());
				return true;
			}
			
		}
		return false;
	}


	private boolean shouldRemoveMoreDistantLandmark(
			SharpMeasurement sharp1Distance, Prediction prediction,
			float distanceMeasured, Landmark lm) {
		
		boolean newLandMarkIsMuchFurtherAway = lm.getDifference(prediction.getLandmark()) > 100;
		
		return newLandMarkIsMuchFurtherAway && 
				  distanceMeasured > sharp1Distance.getReliableDistance() && 
				  prediction.getDistance() < sharp1Distance.getReliableDistance();
	}

	
	
	
	
	public IPrediction getPrediction(State state,
			Direction servo, float beamWidth) {
		Direction worldDirection = state.m_heading.getHeadDirection(servo);

		Point2D.Float robotPosition = state.getRobotPosition();
		
		Prediction ret = m_landmarks.getClosestLandMark(beamWidth, worldDirection, robotPosition);
		return ret;
	}


	public Landmark[] getAllLandmarks() {
		return m_landmarks.getAllLandmarks();
	}





	@Override
	protected void addWithPrediction(State a_bestGuess, DirectionalReading distanceReading, IPrediction prediction) {
		SharpMeasurement sharp1Distance = distanceReading.getBestDistance();
		float deviation = sharp1Distance.getStdev();
		Point2D.Float position = getIRPosition(a_bestGuess, sharp1Distance, distanceReading.getServoDirection());
		Landmark lm = new Landmark(position, deviation, sharp1Distance.getMin());
		addWithExisting(sharp1Distance, lm, prediction);
		
	}





	@Override
	protected void addWithoutPrediction(State a_bestGuess, DirectionalReading distanceReading) {
		SharpMeasurement sharp1Distance = distanceReading.getBestDistance();
		float deviation = sharp1Distance.getStdev();
		Point2D.Float position = getIRPosition(a_bestGuess, sharp1Distance, distanceReading.getServoDirection());
		Landmark lm = new Landmark(position, deviation, sharp1Distance.getMin());
		m_landmarks.add(lm);
	}











	

	
	

}
