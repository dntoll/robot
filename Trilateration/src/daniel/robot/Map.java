package daniel.robot;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.List;

import daniel.robot.sensors.IRReading;
import daniel.robot.sensors.SensorReading;

/**
 * Bitmap map of robot world, starts with 0.0 in the middle
 * @author dntoll
 *
 */
public class Map {

	public List<Point2D.Float> m_obstacles = new ArrayList<Point2D.Float>();
	
	public Map(float mapSizeInMeters, int a_pixelsPerMeter) {
		
	}

	public void Add(State bestGuess, SensorReading reading) {
		
		
		for (IRReading ir : reading.m_ir) {
			
			float distance = ir.m_distance;
			
			Direction direction = bestGuess.m_heading.getHeadDirection(ir.m_servo);
			
			float x = bestGuess.m_position.x + direction.getX() * distance;
			float y = bestGuess.m_position.y + direction.getY() * distance;
			//only close points
			
			m_obstacles.add(new Point2D.Float(x ,y ));
			
		}

	}

	

	public float getDistance(State state, float a_servoDirection, float beamWidth) throws Exception {
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
			
			if (degreesDifference < beamWidth / 2.0f) {
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