package daniel.robot.glWindow.model;

import java.io.IOException;
import java.util.Collection;

import daniel.robot.Direction;
import daniel.robot.SLAM.Movement;
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
