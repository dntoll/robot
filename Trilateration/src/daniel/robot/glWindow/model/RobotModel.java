package daniel.robot.glWindow.model;

import daniel.robot.Direction;
import daniel.robot.sensors.Compass;

public class RobotModel {
	SensorTower sensorTower;
	MotorBoard board;
	
	public RobotModel(String serverAdress) throws Exception {
		sensorTower = new SensorTower(serverAdress);
		board = new MotorBoard(serverAdress);
		
	}

	public DistanceSensorReadings getDistanceSensorReadings() {
		return sensorTower.getDistanceSensorReadings();
	}

	public void update() {
		while(sensorTower.update()) {
			;
		}
		
		board.update();
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
}
