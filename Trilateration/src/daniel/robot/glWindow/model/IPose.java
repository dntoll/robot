package daniel.robot.glWindow.model;

import daniel.robot.SLAM.Map;

public interface IPose {

	DistanceSensorReadings getDistanceSensorReadings();

	daniel.robot.glWindow.model.State getBestGuessPosition();

	float getError();

	Map getBestMap();

}
