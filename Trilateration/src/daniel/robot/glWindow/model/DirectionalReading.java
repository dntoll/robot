package daniel.robot.glWindow.model;

import daniel.robot.Direction;
import daniel.robot.sensors.LongRangeSharp;
import daniel.robot.sensors.SharpMeasurement;
import daniel.robot.sensors.ShortRangeSharp;

public class DirectionalReading {

	Direction direction;
	
	SharpMeasurement shortRange;
	SharpMeasurement longRange;// = new LongRangeSharp();

	public DirectionalReading(Direction direction2, CalibrationModel model) {
		//why is this copied?
		direction = new Direction(direction2.getHeadingDegrees());
		
		shortRange = new ShortRangeSharp(model);
		longRange = new LongRangeSharp(model);
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
			this.longRange.addValue(reading);	
		} else {
			this.shortRange.addValue(reading);
		}
		
	}
	
	public String getSharpString() {
		
		return shortRange.toString() +  longRange.toString();
		
	}

	public SharpMeasurement getBestDistance() {
		if (shortRange.getValues().size() > 0 && shortRange.getMedian() < ShortRangeSharp.RELIABLE_DISTANCE) {
			return shortRange;
		} else {
			if (longRange.getValues().size() > 0)
				return longRange;
			else
				return shortRange;
		}
	
	}
}
