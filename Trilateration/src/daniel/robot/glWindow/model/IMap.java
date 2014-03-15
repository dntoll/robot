package daniel.robot.glWindow.model;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;

import daniel.robot.Direction;
import daniel.robot.sensors.SharpMeasurement;

public abstract class IMap {

	public abstract IPrediction getPrediction(State a_newState, Direction direction,
			float degrees);

	public abstract IMap createChild(State m_state, DirectionalReadingCollection sense);

	
	public void addLandmarks(State a_bestGuess,
			DirectionalReadingCollection sense) {
		for (DirectionalReading distanceReading : sense.getReadings().values()) {
			

			SharpMeasurement sharp1Distance = distanceReading.getBestDistance();
			IPrediction prediction = getPrediction(a_bestGuess, distanceReading.getServoDirection(), sharp1Distance.getBeamWidth());
			if (prediction == null) {
				addWithoutPrediction(a_bestGuess, distanceReading);
			} else {
				addWithPrediction(a_bestGuess, distanceReading, prediction);
			}

		}
	}
	
	protected abstract void addWithPrediction(State a_bestGuess, DirectionalReading distanceReading, IPrediction prediction);

	protected abstract void addWithoutPrediction(State a_bestGuess, DirectionalReading distanceReading);

	protected Float getIRPosition(State a_bestGuess,
			SharpMeasurement sharp1Distance, Direction servoDirection) {
		
		float distance = sharp1Distance.getMedian();
		Direction direction = a_bestGuess.m_heading.getHeadDirection(servoDirection);
		Point2D.Float headPosition = a_bestGuess.getRobotPosition();
		
		float x = headPosition.x + direction.getX() * distance;
		float y = headPosition.y + direction.getY() * distance;
		Point2D.Float position = new Point2D.Float(x ,y );
		return position;
	}
	
	
}
