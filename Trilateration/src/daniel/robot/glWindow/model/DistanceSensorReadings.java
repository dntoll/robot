package daniel.robot.glWindow.model;

import java.util.ArrayList;
import java.util.List;

public class DistanceSensorReadings {
	
	List<DirectionalReading> readings = new ArrayList<DirectionalReading>();

	public List<DirectionalReading> getReadings() {
		return readings;
	}

	public void add(float direction, float sonar1, float sharp1,	float sonar2, float sharp2) {
		
		DirectionalReading reading = new DirectionalReading(direction, sonar1, sharp1, sonar2, sharp2);
		
		readings.add(reading);
	}

}
