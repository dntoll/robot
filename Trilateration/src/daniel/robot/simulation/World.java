package daniel.robot.simulation;

import java.awt.geom.Point2D.Float;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import daniel.robot.BitMapCell;
import daniel.robot.Bitmap;
import daniel.robot.Direction;
import daniel.robot.glWindow.model.CalibrationModel;
import daniel.robot.glWindow.model.DirectionalReadingCollection;
import daniel.robot.glWindow.model.State;
import daniel.robot.sensors.LongRangeSharp;
import daniel.robot.sensors.ShortRangeSharp;
import daniel.robot.slam.map.bm.MapData;

public class World extends Bitmap {

	CalibrationModel calibrationModel;
	
	public World(int size, CalibrationModel cm) {
		super(size);
		loadFromImage();
		calibrationModel = cm;
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
				grid[getIndex(x, y)] = BitMapCell.getEmpty();
				if (x == 0 || y == 0 || x == size-1 || y == size-1) {
					grid[getIndex(x, y)] = BitMapCell.getBlocked();
				} else {
					if (pixels[getIndex(x,y)] == -16777216) {
						grid[getIndex(x, y)] = BitMapCell.getBlocked();
					} else {
						grid[getIndex(x, y)] = BitMapCell.getEmpty();
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

	public DirectionalReadingCollection makeReading(State robot) {
		Direction compassDirection = robot.m_heading;
		DirectionalReadingCollection ret = new DirectionalReadingCollection(compassDirection, calibrationModel);
		Random rand = new Random();
		
		
		for (int servoDegrees = 0; servoDegrees< 360; servoDegrees++) {
			Direction worldDirection = robot.m_heading.getHeadDirection(new Direction(servoDegrees));
			try {
				float distance = getDistance(robot.getRobotPosition(), worldDirection);
			
				
				ShortRangeSharp srs = new ShortRangeSharp(calibrationModel);
				LongRangeSharp lrs = new LongRangeSharp(calibrationModel);
				for (int i  =0; i< 5; i++) {
					//if (distance < 100) {
					ret.addSharpReading(new Direction(servoDegrees + getAngleRandomness(rand) ), srs.fromDistance(getShortDistanceRandomness(distance, rand)), false);
					ret.addSharpReading(new Direction(servoDegrees + getAngleRandomness(rand) ), lrs.fromDistance(getLongDistanceRandomness(distance, rand)), true);
					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return ret;
	}

	
	float multiplier = 1.0f;
	private float getShortDistanceRandomness(float distance, Random rand) {
		
		float randomness = 0.0f;
		if (distance < 100) {
			randomness = 5.0f;
		} else if (distance < 150) {
			randomness = 15.0f;
		} else if (distance < 200) {
			randomness = 30.0f;
		} else if (distance < 300) {
			randomness = 100.0f;
		} /*else {
			randomness = 150.0f;
			return 300 + rand.nextFloat() * randomness - randomness/2.0f;
		}*/
		randomness *= multiplier; 
		return distance + rand.nextFloat() * randomness - randomness/2.0f;
	}
	
	private float getLongDistanceRandomness(float distance, Random rand) {
		float randomness = 0.0f;
		
		if (distance < 100) {
			return 300 + rand.nextFloat()*randomness - randomness /2.0f;
		} else if (distance < 150) {
			randomness = 5.0f;
		} else if (distance < 200) {
			randomness = 10.0f;
		} else if (distance < 500) {
			randomness = 30.0f;
		} /*else {
			randomness = 150.0f;
			return 500 + rand.nextFloat() * randomness - randomness/2.0f;
		}*/
		randomness *= multiplier; 
		return distance + rand.nextFloat() * randomness - randomness/2.0f;
	}

	
	private int getAngleRandomness(Random rand) {
		return (int) (multiplier * (rand.nextInt(3) -1));
	}

	

	
}
