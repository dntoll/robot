package daniel.robot.sensors;

import java.util.ArrayList;
import java.util.List;

import daniel.robot.Direction;

public class GyroAccelerometer {
	List<GyroAccelerometerReading> allReadings = new ArrayList<GyroAccelerometerReading>();
	
	public void add(GyroAccelerometerReading reading) {
		allReadings.add(reading);
	}

	

	public List<GyroAccelerometerReading> getAllReadings() {
		return allReadings;
	}
}
