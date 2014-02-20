package daniel.robot.glWindow.model;

import java.awt.image.BufferedImage;
import java.io.IOException;

import daniel.robot.Direction;
import daniel.robot.SLAM.Movement;
import daniel.robot.sensors.Compass;

public class RobotModel {
	SensorTower sensorTower;
	MotorBoard board;
	CameraSensor camera;
	
	public RobotModel(String serverAdress) throws Exception {
		sensorTower = new SensorTower(serverAdress);
		board = new MotorBoard(serverAdress);
		camera = new CameraSensor(serverAdress);
	}

	public DistanceSensorReadings getDistanceSensorReadings() {
		return sensorTower.getDistanceSensorReadings();
	}

	public void update() {
		try {
			while(sensorTower.update(board.getCompassDirection())) {
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

	public DistanceSensorReadings waitForFullReading(Direction compass) throws IOException, InterruptedException {
		sensorTower.askForMeasurement(compass);
		
		
		DistanceSensorReadings ret = null;
		do {
			ret = sensorTower.getFullCompassReading();
			
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
}
