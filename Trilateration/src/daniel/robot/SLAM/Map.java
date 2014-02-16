package daniel.robot.SLAM;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;
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
	public class Landmark {
		private float measuredFromDistance;
		public Point2D.Float pos;
		public float deviation;
		
		public Landmark(Float position, float stdv, float fromDistance) {
			pos = position;
			deviation = stdv;
			measuredFromDistance = fromDistance;
		}


		public boolean isBetter(Pair prediction) {
			if (deviation < prediction.landmark.deviation ) {
				return true;
			}
			
			if (measuredFromDistance < prediction.landmark.measuredFromDistance ) {
				return true;
			}
			
			return true;
		}


	/*	public boolean isMuchCloser(Pair prediction) {
			float difference = measuredFromDistance - prediction.getDistance();
			
			return  difference * difference > 4.0f*IRReading.IR_DISTANCE_NOISE * IRReading.IR_DISTANCE_NOISE;
			
			
		}*/

		
	}
	public ArrayList<Landmark> m_landmarks = new ArrayList<Landmark>();
	
	public Map(State a_bestGuess, DistanceSensorReadings sense, Map a_parentMap) {
		
		if (a_parentMap != null) {
			m_landmarks.addAll(a_parentMap.m_landmarks);
		}
		
		for (DirectionalReading distanceReading : sense.getReadings().values()) {
			if (distanceReading.getSharp1Distance().okDistance())
			{
				Point2D.Float position = getIRPosition(a_bestGuess, distanceReading);
				float fromDistance= distanceReading.getSharp1Distance().getMedian();
				float deviation = distanceReading.getSharp1Distance().getStdev();
				Landmark lm = new Landmark(position, deviation, fromDistance);
				
				Pair prediction = getDistance(a_bestGuess, distanceReading.getServoDirection(), distanceReading.getSharp1Distance().getBeamWidth());
				//no prediction is found
				if (prediction == null) {
					m_landmarks.add(lm);
				} else {
					if (lm.isBetter(prediction)) {
						m_landmarks.remove(prediction.landmark);
						m_landmarks.add(lm);
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
	
	class Pair {
		public Pair(float minLenSquare, Landmark end) {
			minLenSquareDistance = minLenSquare;
			landmark = end;
		}
		
		public float getDistance() {
			return (float) Math.sqrt(minLenSquareDistance);
		}
		private float minLenSquareDistance;
		Landmark landmark;
	}

	public Pair getDistance(State state, Direction direction2, float a_beamWidth) {
		Direction direction = state.m_heading.getHeadDirection(direction2);

		Point2D.Float robotPosition = state.getRobotPosition();
		
		float minLenSquare = java.lang.Float.MAX_VALUE;
		Pair ret = null;
		
		for (Landmark obstacle : m_landmarks ) {
			float dx = (obstacle.pos.x - robotPosition.x);
			float dy = (obstacle.pos.y - robotPosition.y);
			
			Direction toObstacle = Direction.RadiansToDegrees((float) Math.atan2(dy, dx));
			
			float degreesDifference = toObstacle.GetDifferenceInDegrees(direction);
			
			if (degreesDifference < a_beamWidth / 2.0f) {
				float distanceSquare = dx * dx + dy * dy;
				if (distanceSquare  < minLenSquare) {
					minLenSquare = distanceSquare;
					ret = new Pair(minLenSquare, obstacle);
				}
			}
			//Point2D.Float end = state.m_position.x + Math.cos(direction) * ;
		}
				
		//raster.getPixels(x, y, w, h, iArray);
		return ret;
	}

}
