package daniel.robot.sensors;

import java.util.ArrayList;
import java.util.List;

public class GyroAccelerometer {
	List<GyroAccelerometerReading> allReadings = new ArrayList<GyroAccelerometerReading>();
	
	public void add(GyroAccelerometerReading reading) {
		allReadings.add(reading);
	}

	

	public List<GyroAccelerometerReading> getAllReadings() {
		return allReadings;
	}
}
