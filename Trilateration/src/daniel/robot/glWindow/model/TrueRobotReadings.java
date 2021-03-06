package daniel.robot.glWindow.model;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;

import daniel.robot.Direction;
import daniel.robot.FloatCollection;
import daniel.robot.glWindow.model.persistence.SavedReadings;
import daniel.robot.sensors.Compass;
import daniel.robot.slam.Movement;

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
	
	public DirectionalReadingCollection makeReading() throws IOException,
			InterruptedException {
		Direction compassDirection = model.waitForCompassDirection();
		DirectionalReadingCollection startUpReading = model.waitForFullReading(compassDirection);
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
	public DirectionalReadingCollection getDistanceSensorReadings() {
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

	@Override
	public BufferedImage getPanoramaImage() {
		return model.getPanoramaImage();
	}

	@Override
	public FloatCollection[] makeCalibration() {
		try {
			return model.waitForCalibrationReading();
		} catch (InterruptedException e) {
			return null;
		} catch (IOException e) {
			return null;
		}
	}

	@Override
	public FloatCollection[] makeSingleDistanceRead() {
		try {
			return model.waitForSingleRead();
		} catch (InterruptedException e) {
			return null;
		} catch (IOException e) {
			return null;
		}
	}

	
}
