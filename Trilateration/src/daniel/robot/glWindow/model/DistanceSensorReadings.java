package daniel.robot.glWindow.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import daniel.robot.Direction;

public class DistanceSensorReadings {
	HashMap<Integer, DirectionalReading> readings = new HashMap<Integer, DirectionalReading>(360);
	
	private DirectionalReading latest;

	private Date startTime;

	private Direction compass;
	
	public DistanceSensorReadings(Direction compass) {
		startTime = new Date();
		this.compass = compass;
	}

	public Map<Integer, DirectionalReading> getReadings() {
		return readings;
	}
	
	

	
	
	public void addSharpReading(Direction direction, float value, boolean isLongRange) {
		DirectionalReading dr = getReading(direction);
		dr.addSharpReading(value, isLongRange);
	}
	
	public void addSharpDistance(Direction direction, float distance) {
		DirectionalReading dr = getReading(direction);
		dr.addSharCM(distance);
	}
	
	private DirectionalReading getReading(Direction direction) {
		Integer key = new Integer((int)direction.getHeadingDegrees());
		DirectionalReading reading = readings.get(key);
		
		if (reading == null) 
		{
			reading = new DirectionalReading(direction);
			readings.put(key, reading);
		}
		return reading;
	}

		
	public String getSharpString() {
		if (latest != null)
			return latest.sharp.toString();
		return "";
	}

	

	public Direction getCompassDirection() {
		return compass;
	}

	

	

}
