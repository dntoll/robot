package daniel.robot.simulation;

import java.awt.geom.Point2D.Float;
import java.util.Random;

import daniel.robot.Direction;
import daniel.robot.glWindow.model.DistanceSensorReadings;
import daniel.robot.glWindow.model.State;

public class World {

	int size = 200;
	boolean[] grid = new boolean[size * size];
	
	public World() {
		Random rand = new Random();
		for (int x = 0;x< size; x++) {
			for (int y = 0; y < size; y++) {
				if (x == 0 || y == 0 || x == size-1 || y == size-1) {
					grid[getIndex(x, y)] = false;
				} else {
					if (rand.nextInt(20) == 0) {
						grid[getIndex(x, y)] = true;
					}
				}
			}
		}
	}
	
	private int getIndex(int x, int y) {
		return y * size + x;
	}
	
	public Float getStartPosition() {
		return new Float(100,100);
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
		
		
		Float position = robot.getRobotPosition();
		
		Direction worldDirection = robot.m_heading.getHeadDirection(servo);
		
		float xdir = worldDirection.getX();
		float ydir = worldDirection.getY();
		
		float u[] = new float[4];
		for(int i = 0;i< 4; i++) {
			u[i]= java.lang.Float.MAX_VALUE;
		}
		
		//find u > 0;
		
		if (xdir != 0.0f) {
			//u * xdir +position.x = 0; 
			u[0] = -position.x / xdir;
			//u * xdir +position.x = width;
			u[1] = (size -position.x) / xdir;
		}
		if (ydir != 0.0f) {
			u[2] = -position.y / ydir;
			u[3] = (size-position.y) / ydir;
		}
		
		float minu = java.lang.Float.MAX_VALUE;
		for(int i = 0;i< 4; i++) {
			
			if (u[i] > 0 && u[i] < minu) {
				minu = u[i];
			}
		}
		
		return minu;
		
	}

	
}
