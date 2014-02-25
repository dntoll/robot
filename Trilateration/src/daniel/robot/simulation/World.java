package daniel.robot.simulation;

import java.awt.geom.Point2D.Float;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import daniel.robot.Bitmap;
import daniel.robot.Direction;
import daniel.robot.SLAM.MapData;
import daniel.robot.glWindow.model.DistanceSensorReadings;
import daniel.robot.glWindow.model.State;

public class World extends Bitmap {

	//int size = 250;
	//boolean[] grid = new boolean[size * size];
	
	public World(int size) {
		super(size);
		loadFromImage();
	}

	private void loadFromImage() {
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File("level.bmp"));
		} catch (IOException e) {
		}
		
		int[] pixels = new int[image.getWidth() * image.getHeight()];
		image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());
		
		Random rand = new Random();
		for (int x = 0;x< size; x++) {
			for (int y = 0; y < size; y++) {
				grid[getIndex(x, y)] = CellContent.empty;
				if (x == 0 || y == 0 || x == size-1 || y == size-1) {
					grid[getIndex(x, y)] = CellContent.blocked;
				} else {
					if (pixels[getIndex(x,y)] == -16777216) {
						grid[getIndex(x, y)] = CellContent.blocked;
					} else {
						grid[getIndex(x, y)] = CellContent.empty;
					}
				}
			}
		}
	}
	
	
	
	public Float getStartPosition() {
		return new Float(size/4,size/2);
	}

	public Direction getStartDirection() {
		return new Direction(0);
	}

	public DistanceSensorReadings makeReading(State robot) {
		Direction compassDirection = robot.m_heading;
		DistanceSensorReadings ret = new DistanceSensorReadings(compassDirection);
		Random rand = new Random();
		
		for (int servoDegrees = 0; servoDegrees< 360; servoDegrees++) {
			Direction worldDirection = robot.m_heading.getHeadDirection(new Direction(servoDegrees));
			float distance = getDistance(robot.getRobotPosition(), worldDirection);
			for (int i  =0; i< 5; i++) {
				//if (distance < 100) {
				ret.addSharpDistance(new Direction(servoDegrees + getAngleRandomness(rand) ), getDistanceRandomness(distance, rand));
				
			}
		}
		
		return ret;
	}

	private float getDistanceRandomness(float distance, Random rand) {
		float randomness = 0;
		if (distance < 100) {
			randomness = 5.0f;
		} else if (distance < 150) {
			randomness = 15.0f;
		} else if (distance < 200) {
			randomness = 30.0f;
		} else if (distance < 300) {
			randomness = 150.0f;
		} else {
			randomness = 150.0f;
			return 300 + rand.nextFloat() * randomness - randomness/2.0f;
		}
		
		return distance + rand.nextFloat() * randomness - randomness/2.0f;
	}

	private float getAngleRandomness(Random rand) {
		return rand.nextFloat() * 2.0f-1.0f;
	}

	

	
}
