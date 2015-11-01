package daniel.robot.slam.map.bm;

import java.awt.geom.Point2D;

import daniel.robot.Direction;
import daniel.robot.glWindow.model.DirectionalReading;
import daniel.robot.glWindow.model.DirectionalReadingCollection;
import daniel.robot.glWindow.model.IMap;
import daniel.robot.glWindow.model.IPrediction;
import daniel.robot.glWindow.model.State;

public class BitmapMap extends IMap {

	MapData freeArea;
	
	
	public BitmapMap(State a_bestGuess, DirectionalReadingCollection sense, IMap parent) {
		
		if (parent != null) {
			freeArea = new MapData(((BitmapMap)parent).freeArea);
		} else {
			freeArea = new MapData(600, 2);
		}
		
		addLandmarks(a_bestGuess, sense);
	}
	
	@Override
	public IPrediction getPrediction(State state,
			Direction servo, float beamWidth) {
		Direction worldDirection = state.m_heading.getHeadDirection(servo);

		Point2D.Float robotPosition = state.getRobotPosition();
		
		float distance;
		try {
			distance = freeArea.getDistance(robotPosition, worldDirection);
			return new SimplePrediction(distance);
		} catch (Exception e) {
			return null;
		}
		
	}

	@Override
	public IMap createChild(State state, DirectionalReadingCollection sense) {
		return new BitmapMap(state, sense, this);
	}

	@Override
	protected void addWithPrediction(State a_bestGuess, DirectionalReading distanceReading, IPrediction prediction) {
		freeArea.draw(distanceReading.getBestDistance(), a_bestGuess.getRobotPosition(), a_bestGuess.m_heading.getHeadDirection(distanceReading.getServoDirection()));
	}
	
	

	@Override
	protected void addWithoutPrediction(State a_bestGuess, DirectionalReading distanceReading) {
		freeArea.draw(distanceReading.getBestDistance(), a_bestGuess.getRobotPosition(), a_bestGuess.m_heading.getHeadDirection(distanceReading.getServoDirection()));
		
	}

	public MapData getMap() {
		return freeArea;
	}


}
