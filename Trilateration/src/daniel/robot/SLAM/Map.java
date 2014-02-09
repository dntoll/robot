package daniel.robot.SLAM;

import java.awt.Point;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;
import java.util.ArrayList;
import java.util.List;

import daniel.robot.Direction;
import daniel.robot.glWindow.model.DirectionalReading;
import daniel.robot.glWindow.model.DistanceSensorReadings;
import daniel.robot.sensors.IRReading;

/**
 * Bitmap map of robot world, starts with 0.0 in the middle
 * @author dntoll
 *
 */ 
public class Map {

	public ArrayList<Point2D.Float> m_landmarks = new ArrayList<Point2D.Float>();
	
	public List<Line2D.Float> m_lines = new ArrayList<Line2D.Float>();
	
	public Map(State a_bestGuess, DistanceSensorReadings sense, Map a_parentMap) {
		
		if (a_parentMap != null) {
			m_landmarks.addAll(a_parentMap.m_landmarks);
		}
		
		
		
		Point2D.Float prevPosition = new Point2D.Float();
		Point2D.Float prevprevPosition = new Point2D.Float();
		
		
		for (DirectionalReading distanceReading : sense.getReadings().values()) {

			Point2D.Float position = getIRPosition(a_bestGuess, distanceReading);
			
			//only points within range and that is consistant with last reading
			if (distanceReading.getSharp1Distance().okDistance() && 
				position.distance(prevPosition) < 4.0f &&
				prevprevPosition.distance(prevPosition) < 4.0f)
			{
				
			//	if (sense.hasCloseSonar(distanceReading)) {
					
					try {
						float oldDistance = getDistance(a_bestGuess, distanceReading.getServoDirection(), distanceReading.getSharp1Distance().getBeamWidth());
						
						float difference = distanceReading.getSharp1Distance().getMedian() - oldDistance;
						if (difference * difference > 10.0f*IRReading.IR_DISTANCE_NOISE * IRReading.IR_DISTANCE_NOISE) {
							m_landmarks.add(position);	
						}
					} catch (Exception e) {
						m_landmarks.add(position);	
					}
					
					
			//	}
			}
			prevprevPosition = prevPosition;
			prevPosition = position;
		}

		/*
		for (int startIndex = 0; startIndex < a_reading.m_ir.length; startIndex++) {
			Point2D.Float startPosition = getIRPosition(a_bestGuess, a_reading.m_ir[startIndex]);
			
			for (int endIndex = a_reading.m_ir.length-1; endIndex > startIndex + 1; endIndex--) {
				Point2D.Float endPosition = getIRPosition(a_bestGuess, a_reading.m_ir[endIndex]);
				
				Line2D.Float lineCandidate = new Line2D.Float(startPosition, endPosition);
				Point2D.Float points[] = new Point2D.Float[endIndex-startIndex+1]; 
				boolean ok = true;
				int numSonarClose = 0;
				for (int middleIndex = endIndex; middleIndex >= startIndex; middleIndex--) {
					
					Point2D.Float middlePos = getIRPosition(a_bestGuess, a_reading.m_ir[middleIndex]);
					
					if (middleIndex < endIndex) {
						Point2D.Float lastMiddlePos = getIRPosition(a_bestGuess, a_reading.m_ir[middleIndex+1]);
						if (middlePos.distance(lastMiddlePos) > 4.0f ) {
							ok = false;
							break;
						}
					}
					
					
					points[middleIndex-startIndex] = middlePos;
					
					if (a_reading.hasCloseSonar(a_reading.m_ir[middleIndex])) {
						numSonarClose++;
					}
					
					float distance = (float) lineCandidate.ptLineDist(middlePos);
					
					if (distance > 3.0f) {
						ok = false;
						break;
					}
				}
				
				if (ok && numSonarClose > 5) {
					
					Line2D.Float line = LinearRegression.GetLine(points);
					m_lines.add(line);
					//m_lines.add(lineCandidate);
					startIndex = endIndex-1;
					
					break;
				}
			}
		}*/
		
		
		

	}

	
	
	

	private Float getClosestLandmark(Float position) {
		Float ret = position;
		float closestDistance = 10000000.0f;
		for (Point2D.Float existing : m_landmarks) {
			float distance = (float) existing.distanceSq(position);
			if (distance < closestDistance) {
				ret = existing;
				closestDistance = distance;
			}
		}
		return ret;
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

	public float getDistance(State state, Direction direction2, float a_beamWidth) throws Exception {
		//WritableRaster raster = m_image.getRaster();
		Direction direction = state.m_heading.getHeadDirection(direction2);
		
		Point2D.Float headPosition = state.getRobotPosition();
		Point2D.Float start = headPosition;
		
		float minLenSquare = 1000000.0f;
		boolean found = false;
		
		for (Point2D.Float end : m_landmarks ) {
			float dx = (end.x - start.x);
			float dy = (end.y - start.y);
			
			Direction toObstacle = Direction.RadiansToDegrees((float) Math.atan2(dy, dx));
			
			float degreesDifference = toObstacle.GetDifferenceInDegrees(direction);
			
			if (degreesDifference < a_beamWidth / 2.0f) {
				float distanceSquare = dx * dx + dy * dy;
				if (distanceSquare  < minLenSquare) {
					minLenSquare = distanceSquare;
					found = true;
				}
			}
			//Point2D.Float end = state.m_position.x + Math.cos(direction) * ;
		}
		
		if (found == false) {
			throw new Exception("no distance here");
		}
		
		//raster.getPixels(x, y, w, h, iArray);
		return (float) Math.sqrt(minLenSquare);
	}

}
