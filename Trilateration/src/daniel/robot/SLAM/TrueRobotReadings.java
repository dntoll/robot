package daniel.robot.SLAM;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;

import daniel.robot.Direction;
import daniel.robot.glWindow.model.DistanceSensorReadings;
import daniel.robot.glWindow.model.IPose;
import daniel.robot.glWindow.model.IRobotInterface;
import daniel.robot.glWindow.model.PoseCollection;
import daniel.robot.glWindow.model.RobotModel;
import daniel.robot.glWindow.model.SavedReadings;
import daniel.robot.sensors.Compass;

/**
 * This class connects to an actual robot...
 */
public class TrueRobotReadings implements IRobotInterface{
	private int readingIndex = 0;
	private Date day = new Date();
	private SavedReadings saver = new SavedReadings();
	private RobotModel model;
	
	public TrueRobotReadings(RobotModel model) {
		this.model = model;
	}
	
	public DistanceSensorReadings makeReading() throws IOException,
			InterruptedException {
		Direction compassDirection = model.waitForCompassDirection();
		DistanceSensorReadings startUpReading = model.waitForFullReading(compassDirection);
		saver.save(startUpReading, new File(String.format("readings/%s_%d", day.toString(), readingIndex)));
		readingIndex++;
		return startUpReading;
	}

	@Override
	public Movement makeMove() throws InterruptedException {
		//System.out.println("Move now");
		//Thread.sleep(10000);
		float distance = 0.0f;
		float turn = 0.0f;
		Movement move = new Movement(distance, turn);
		model.waitForMove(move);
		
		return move;
	}

	@Override
	public void update() {
		model.update();
	}

	@Override
	public DistanceSensorReadings getDistanceSensorReadings() {
		return model.getDistanceSensorReadings();
	}

	@Override
	public Compass getCompass() {
		return model.getCompass();
	}

	@Override
	public float getTemperature() {
		return model.getTemperature();
	}

	@Override
	public Direction getCompassDirection() {
		return model.getCompassDirection();
	}

	
}
