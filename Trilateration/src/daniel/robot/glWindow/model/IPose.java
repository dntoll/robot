package daniel.robot.glWindow.model;

import daniel.robot.slam.Map;
import daniel.robot.slam.ParticleFilter;

public interface IPose {

	DirectionalReadingCollection getDistanceSensorReadings();

	daniel.robot.glWindow.model.State getBestGuessPosition();

	float getError();

	Map getBestMap();

	ParticleFilter getParticleFilter();

}
