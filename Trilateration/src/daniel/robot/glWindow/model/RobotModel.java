package daniel.robot.glWindow.model;

public class RobotModel {
	SensorTower sensorTower;
	MotorBoard engine;
	
	public RobotModel( ) throws Exception {
		sensorTower = new SensorTower();
		engine = new MotorBoard();
		
	}

	public DistanceSensorReadings getDistanceSensorReadings() {
		return sensorTower.getDistanceSensorReadings();
	}
}
