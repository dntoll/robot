package daniel.robot.SLAM;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import daniel.robot.Direction;
import daniel.robot.sensors.IRReading;
import daniel.robot.sensors.SensorReading;
import daniel.robot.statistics.LinearRegression;

/**
 * Bitmap map of robot world, starts with 0.0 in the middle
 * @author dntoll
 *
 */ 
public class Map {

	public ArrayList<Point2D.Float> m_landmarks = new ArrayList<Point2D.Float>();
	
	public List<Line2D.Float> m_lines = new ArrayList<Line2D.Float>();
	
	public Map(State a_bestGuess, SensorReading a_reading) {
		
		Point2D.Float prevPosition = new Point2D.Float();
		Point2D.Float prevprevPosition = new Point2D.Float();
		
		
		for (IRReading distanceReading : a_reading.m_ir) {
			
			Point2D.Float position = getIRPosition(a_bestGuess, distanceReading);
			
			//only points within range and that is consistant with last reading
			if (distanceReading.okDistance() && 
				position.distance(prevPosition) < 4.0f &&
				prevprevPosition.distance(prevPosition) < 4.0f)
			{
				
				if (a_reading.hasCloseSonar(distanceReading)) {
					m_landmarks.add(position);
				}
			}
			prevprevPosition = prevPosition;
			prevPosition = position;
		}

		
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
						if (middlePos.distance(lastMiddlePos) > 7.0f ) {
							ok = false;
							break;
						}
					}
					
					
					points[middleIndex-startIndex] = middlePos;
					
					if (a_reading.hasCloseSonar(a_reading.m_ir[middleIndex])) {
						numSonarClose++;
					}
					
					float distance = (float) lineCandidate.ptLineDist(middlePos);
					
					if (distance > 8.0f) {
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
		}
		
		
		

	}

	private Point2D.Float getIRPosition(State a_bestGuess,
			IRReading distanceReading) {
		float distance = distanceReading.m_distance;
		Direction direction = a_bestGuess.m_heading.getHeadDirection(distanceReading.m_servo);
		
		float x = a_bestGuess.m_position.x + direction.getX() * distance;
		float y = a_bestGuess.m_position.y + direction.getY() * distance;
		Point2D.Float position = new Point2D.Float(x ,y );
		return position;
	}

	public float getDistance(State state, float a_servoDirection, float a_beamWidth) throws Exception {
		//WritableRaster raster = m_image.getRaster();
		Direction direction = state.m_heading.getHeadDirection(a_servoDirection);
		Point2D.Float start = state.m_position;
		
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
