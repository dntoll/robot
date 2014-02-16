package daniel.robot.SLAM;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import daniel.robot.Direction;
import daniel.robot.glWindow.model.DistanceSensorReadings;
import daniel.robot.glWindow.model.IPose;
import daniel.robot.glWindow.model.IRobotInterface;
import daniel.robot.glWindow.model.PoseCollection;
import daniel.robot.glWindow.model.SavedReadings;
import daniel.robot.sensors.Compass;


/**
 * This class reads from files instead of robot
 **/
public class SavedRobotReadings implements IRobotInterface {
	private SavedReadings saver = new SavedReadings();
	private String fileName;
	private int readingIndex = 0;
	
	DistanceSensorReadings lastReading;
	
	public SavedRobotReadings(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public DistanceSensorReadings makeReading() throws IOException,
			InterruptedException {
		lastReading = saver.load(new File(String.format("readings/%s_%d", fileName, readingIndex)));
		readingIndex++;
		return lastReading;
	}

	@Override
	public Movement makeMove() throws InterruptedException {
		float distance = 0.5f;
		float turn = 0.0f;
		Movement move = new Movement(distance, turn);
		//model.waitForMove(move);
		
		return move;
	}

	@Override
	public void update() {
		
		
	}

	@Override
	public DistanceSensorReadings getDistanceSensorReadings() {
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



	

}
