package daniel.robot.glWindow.model;

import java.awt.image.BufferedImage;
import java.io.IOException;
import daniel.robot.Direction;
import daniel.robot.SLAM.Movement;
import daniel.robot.sensors.Compass;

public interface IRobotInterface {
	
	//Blocking
	public DirectionalReadingCollection makeReading() throws IOException, InterruptedException;

	//Blocking
	public Movement makeMove() throws InterruptedException;

	public void update();

	public DirectionalReadingCollection getDistanceSensorReadings();

	public Compass getCompass();

	public float getTemperature();

	public Direction getCompassDirection();

	public BufferedImage getPanoramaImage();

	public DirectionalReadingCollection makeCalibration();

}
