package daniel.robot.glWindow.model;

import java.awt.image.BufferedImage;
import java.io.IOException;

import daniel.robot.Direction;
import daniel.robot.FloatCollection;
import daniel.robot.sensors.CameraSensor;
import daniel.robot.sensors.Compass;
import daniel.robot.slam.Movement;

public class RobotModel {
	SensorTower sensorTower;
	MotorBoard board;
	CameraSensor camera;
	
	public RobotModel(String serverAdress, CalibrationModel model) throws Exception {
		sensorTower = new SensorTower(serverAdress, model);
		board = new MotorBoard(serverAdress);
		camera = new CameraSensor(serverAdress);
	}

	public DirectionalReadingCollection getDistanceSensorReadings() {
		return sensorTower.getDistanceSensorReadings();
	}

	public void update() {
		try {
			while(sensorTower.update()) {
				;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		board.update();
		
		camera.update(sensorTower.getLastDirection());
	}

	public float getTemperature() {
		return board.getTemperature();
	}

	public Direction getCompassDirection() {
		
		return board.getCompassDirection();
	}

	public Compass getCompass() {
		return board.getCompass();
	}

	public BufferedImage getPanoramaImage() {
		return camera.getPanoramaImage();
	}

	public Direction waitForCompassDirection() throws InterruptedException {
		Direction ret = null;
		do {
			ret = getCompassDirection();
			
			Thread.sleep(100);
			
		} while (ret == null);
		return ret;
	}

	public DirectionalReadingCollection waitForFullReading(Direction compass) throws IOException, InterruptedException {
		sensorTower.askForMeasurement(compass);
		
		
		DirectionalReadingCollection ret = null;
		do {
			ret = sensorTower.getFullDistanceReading();
			
			Thread.sleep(100);
			
		} while (ret == null);
		return ret;
	}

	public void waitForMove(Movement move) throws InterruptedException {
		board.askForMovement(move);
		
		
		while(board.isStillMoving()) {
			Thread.sleep(100);
		}
	}

	public FloatCollection[] waitForCalibrationReading() throws InterruptedException, IOException {
		
		sensorTower.askForCalibrationMeasurement();
		
		FloatCollection[] ret = null;
		do {
			ret = sensorTower.getCalibrationReading();
			
			Thread.sleep(100);
			
			while(sensorTower.update()) {
				;
			}
			
		} while (ret == null);
		return ret;
	}

	public FloatCollection[] waitForSingleRead() throws IOException, InterruptedException {
		sensorTower.askForSingleRead();
		
		FloatCollection[] ret = null;
		do {
			ret = sensorTower.getCalibrationReading();
			
			Thread.sleep(100);
			
			while(sensorTower.update()) {
				;
			}
			
		} while (ret == null);
		return ret;
	}
}
