package daniel.robot.SLAM;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import daniel.robot.Direction;
import daniel.robot.glWindow.model.DirectionalReadingCollection;
import daniel.robot.glWindow.model.IPose;
import daniel.robot.glWindow.model.IRobotInterface;
import daniel.robot.glWindow.model.PoseCollection;
import daniel.robot.glWindow.model.persistence.SavedReadings;
import daniel.robot.sensors.Compass;


/**
 * This class reads from files instead of robot
 **/
public class SavedRobotReadings implements IRobotInterface {
	private SavedReadings saver = new SavedReadings();
	private String fileName;
	private int readingIndex = 0;
	
	DirectionalReadingCollection lastReading;
	
	public SavedRobotReadings(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public DirectionalReadingCollection makeReading() throws IOException,
			InterruptedException {
		lastReading = saver.load(new File(String.format("readings/%s_%d", fileName, readingIndex)));
		readingIndex++;
		return lastReading;
	}

	@Override
	public Movement makeMove() throws InterruptedException {
		float distance = 6.5f;
		//float distance = 0.0f;
		float turn = 0.0f;
		Movement move = new Movement(distance, turn);
		//model.waitForMove(move);
		
		return move;
	}

	@Override
	public void update() {
		
		
	}

	@Override
	public DirectionalReadingCollection getDistanceSensorReadings() {
		return lastReading;
	}
	
	

	@Override
	public Compass getCompass() {
		return new Compass();
	}

	@Override
	public float getTemperature() {
		return -22.0f;
	}

	@Override
	public Direction getCompassDirection() {
		return lastReading.getCompassDirection();
	}

	@Override
	public BufferedImage getPanoramaImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DirectionalReadingCollection makeCalibration() {
		return null;
	}



	

}
