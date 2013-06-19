package daniel.robot.SLAM;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import daniel.robot.Direction;
import daniel.robot.sensors.DistanceReading;
import daniel.robot.sensors.SensorReading;

/**
 * Bitmap map of robot world, starts with 0.0 in the middle
 * @author dntoll
 *
 */ 
public class Map {

	public List<Point2D.Float> m_obstacles = new ArrayList<Point2D.Float>();
	
	public Map(State a_bestGuess, SensorReading a_reading) {
		
		Point2D.Float prevPosition = new Point2D.Float();
		Point2D.Float prevprevPosition = new Point2D.Float();
		for (DistanceReading distanceReading : a_reading.m_distances) {
			
			float distance = distanceReading.m_distance;
			
			Direction direction = a_bestGuess.m_heading.getHeadDirection(distanceReading.m_servo);
			
			float x = a_bestGuess.m_position.x + direction.getX() * distance;
			float y = a_bestGuess.m_position.y + direction.getY() * distance;
			Point2D.Float position = new Point2D.Float(x ,y );
			
			//only points within range and that is consistant with last reading
			if (distance > 20 && 
				distance < 120.0f && 
				position.distance(prevPosition) < 4.0f &&
				prevprevPosition.distance(prevPosition) < 4.0f)
			{
				m_obstacles.add(position);
			}
			prevprevPosition = prevPosition;
			prevPosition = position;
			
		}

	}

	public float getDistance(State state, float a_servoDirection, float a_beamWidth) throws Exception {
		//WritableRaster raster = m_image.getRaster();
		Direction direction = state.m_heading.getHeadDirection(a_servoDirection);
		Point2D.Float start = state.m_position;
		
		float minLenSquare = 1000000.0f;
		boolean found = false;
		
		for (Point2D.Float end : m_obstacles ) {
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
