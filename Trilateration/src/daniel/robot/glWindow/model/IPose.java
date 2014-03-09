package daniel.robot.glWindow.model;

import daniel.robot.SLAM.Map;
import daniel.robot.SLAM.ParticleFilter;

public interface IPose {

	DirectionalReadingCollection getDistanceSensorReadings();

	daniel.robot.glWindow.model.State getBestGuessPosition();

	float getError();

	Map getBestMap();

	ParticleFilter getParticleFilter();

}
