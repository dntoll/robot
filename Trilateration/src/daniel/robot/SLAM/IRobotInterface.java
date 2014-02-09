package daniel.robot.SLAM;

import java.io.IOException;

import daniel.robot.Direction;
import daniel.robot.glWindow.model.DistanceSensorReadings;
import daniel.robot.sensors.Compass;

public interface IRobotInterface {
	
	//Blocking
	public DistanceSensorReadings makeReading() throws IOException, InterruptedException;

	//Blocking
	public Movement makeMove() throws InterruptedException;

	public void update();

	public DistanceSensorReadings getDistanceSensorReadings();

	public Compass getCompass();

	public float getTemperature();

	public Direction getCompassDirection();
}
