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
		public Landmark(Float position, float stdv) {
			pos = position;
			deviation = stdv;
		}

		public Point2D.Float pos;
		public float deviation;
	}
	public ArrayList<Landmark> m_landmarks = new ArrayList<Landmark>();
	
	public Map(State a_bestGuess, DistanceSensorReadings sense, Map a_parentMap) {
		
		if (a_parentMap != null) {
			m_landmarks.addAll(a_parentMap.m_landmarks);
		}
		
		
		
		for (DirectionalReading distanceReading : sense.getReadings().values()) {
			
			Point2D.Float position = getIRPosition(a_bestGuess, distanceReading);
			
			//only points within range and that is consistant with last reading
			if (distanceReading.getSharp1Distance().okDistance())
			{
				
					Pair oldDistance = getDistance(a_bestGuess, distanceReading.getServoDirection(), distanceReading.getSharp1Distance().getBeamWidth());
					if (oldDistance == null) {
						m_landmarks.add(new Landmark(position, distanceReading.getSharp1Distance().getStdev()));
					} else {
					
						float difference = distanceReading.getSharp1Distance().getMedian() - oldDistance.getDistance();
						
						
						boolean farAwayFromOldLandmark = difference * difference > 4.0f*IRReading.IR_DISTANCE_NOISE * IRReading.IR_DISTANCE_NOISE;
						
						if (farAwayFromOldLandmark) {
							m_landmarks.add(new Landmark(position, distanceReading.getSharp1Distance().getStdev()));	
						} else {
							if (oldDistance.landmark.deviation > distanceReading.getSharp1Distance().getStdev() ) {
								m_landmarks.remove(oldDistance.landmark);
								m_landmarks.add(new Landmark(position, distanceReading.getSharp1Distance().getStdev()));
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
		
		for (Landmark end : m_landmarks ) {
			float dx = (end.pos.x - robotPosition.x);
			float dy = (end.pos.y - robotPosition.y);
			
			Direction toObstacle = Direction.RadiansToDegrees((float) Math.atan2(dy, dx));
			
			float degreesDifference = toObstacle.GetDifferenceInDegrees(direction);
			
			if (degreesDifference < a_beamWidth / 2.0f) {
				float distanceSquare = dx * dx + dy * dy;
				if (distanceSquare  < minLenSquare) {
					minLenSquare = distanceSquare;
					ret = new Pair(minLenSquare, end);
				}
			}
			//Point2D.Float end = state.m_position.x + Math.cos(direction) * ;
		}
				
		//raster.getPixels(x, y, w, h, iArray);
		return ret;
	}

}
