package daniel.robot.glWindow.model;

import daniel.robot.Direction;
import daniel.robot.sensors.LongRangeSharp;
import daniel.robot.sensors.SharpMeasurement;
import daniel.robot.sensors.ShortRangeSharp;

public class DirectionalReading {

	Direction direction;
	
	SharpMeasurement shortRange = new ShortRangeSharp();
	SharpMeasurement longRange = new LongRangeSharp();

	public DirectionalReading(Direction direction2) {
		direction = new Direction(direction2.getHeadingDegrees());
	}

	public Direction getServoDirection() {
		return direction;
	}


	public SharpMeasurement getShortDistance() {
		return shortRange;
	}
	
	public SharpMeasurement getLongDistance() {
		return longRange;
	}

	public void addSharpReading(float reading, boolean isLongRange) {
		if (isLongRange) {
			this.longRange.add(reading);	
		} else {
			this.shortRange.add(reading);
		}
		
	}
	
	public void addSharpCM(float distance, boolean isLongRange) {
		if (isLongRange) {
			this.longRange.addCM(distance);
		} else {
			this.shortRange.addCM(distance);
		}
	}
	
	public String getSharpString() {
		
		return shortRange.toString() +  longRange.toString();
		
	}

	public SharpMeasurement getBestDistance() {
		if (shortRange.getMedian() < ShortRangeSharp.RELIABLE_DISTANCE)
			return shortRange;
		else 
			return longRange;
	}
}
