package daniel.robot.simulation;

import java.awt.geom.Point2D.Float;
import java.util.Random;

import daniel.robot.Direction;
import daniel.robot.glWindow.model.DistanceSensorReadings;
import daniel.robot.glWindow.model.State;

public class World {

	int size = 50;
	boolean[] grid = new boolean[size * size];
	
	public World() {
		Random rand = new Random();
		for (int x = 0;x< size; x++) {
			for (int y = 0; y < size; y++) {
				if (x == 0 || y == 0 || x == size-1 || y == size-1) {
					grid[getIndex(x, y)] = true;
				} else {
					if (rand.nextInt(100) == 0) {
						grid[getIndex(x, y)] = false;
					} else {
						grid[getIndex(x, y)] = false;
					}
				}
			}
		}
	}
	
	private int getIndex(int x, int y) {
		if (x < 0 || y < 0 || x > size-1 || y > size-1) {
			return 0;
		}
		return y * size + x;
	}
	
	public Float getStartPosition() {
		return new Float(size/2,size/2);
	}

	public Direction getStartDirection() {
		return new Direction(10);
	}

	public DistanceSensorReadings makeReading(State robot) {
		Direction compassDirection = robot.m_heading;
		DistanceSensorReadings ret = new DistanceSensorReadings(compassDirection);
		Random rand = new Random();
		
		for (int servoDegrees = 0; servoDegrees< 360; servoDegrees++) {
			float distance = getDistance(robot, new Direction(servoDegrees));
			for (int i  =0; i< 4; i++) {
				if (distance < 100) {
					ret.addSharpDistance(new Direction(servoDegrees), distance + rand.nextFloat() * 4.0f - 2.0f);
				} else if (distance < 150) {
					ret.addSharpDistance(new Direction(servoDegrees), distance + rand.nextFloat() * 10.0f - 5.0f);
				} else if (distance < 200) {
					ret.addSharpDistance(new Direction(servoDegrees), distance + rand.nextFloat() * 32.0f - 16.0f);
				} else {
					ret.addSharpDistance(new Direction(servoDegrees), distance + rand.nextFloat() * 100.0f - 50.0f);
				}
			}
		}
		
		return ret;
	}

	private float getDistance(State robot, Direction servo) {
		
		
		Float from = robot.getRobotPosition();
		
		Direction worldDirection = robot.m_heading.getHeadDirection(servo);
		
		float xdir = worldDirection.getX();
		float ydir = worldDirection.getY();
		
		
		
		float minu = java.lang.Float.MAX_VALUE;
		
		if (xdir > 0) {
			for (int x = (int)from.x+1; (float)x < size; x++) {
				float u = ((float)x - from.x) / xdir;
				float y = from.y + ydir * u;
				if ( y < 0 || y > size-1)
					break;
				if (grid[getIndex((int)x, (int)y)] == true) {
					if (u < minu)
						minu = u;
					break;
				}
				
			}
		} else if (xdir < 0) {
			for (int x = (int)from.x; (float)x > 0; x--) {
				float u = ((float)x - from.x) / xdir;
				float y = from.y + ydir * u;
				if ( y < 0 || y > size-1)
					istället returnera u för max y...
					break;
				if (grid[getIndex((int)x, (int)y)] == true) {
					if (u < minu)
						minu = u;
					break;
				}
			}
		}
		
		if (ydir > 0) {
			for (int y = (int)from.y+1; (float)y < size; y++) {
				float u = ((float)y - from.y) / ydir;
				float x = from.x + xdir * u;
				if ( x < 0 || x > size-1)
					break;
				if (grid[getIndex((int)x, (int)y)] == true) {
					if (u < minu)
						minu = u;
					break;
				}
				
			}
		} else if (ydir < 0) {
			for (int y = (int)from.y; (float)y > 0; y--) {
				float u = ((float)y - from.y) / ydir;
				float x = from.x + xdir * u;
				if ( x < 0 || x > size-1)
					break;
				if (grid[getIndex((int)x, (int)y)] == true) {
					if (u < minu)
						minu = u;
					break;
				}
			}
		}
		
		if (minu > size)
			minu = size;
		
		return minu;
		
	}

	
}
