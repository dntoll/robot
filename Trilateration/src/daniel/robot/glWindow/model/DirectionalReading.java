package daniel.robot.glWindow.model;

import daniel.robot.Direction;

public class DirectionalReading {

	Direction direction;
	
	SharpMeasurement sharp = new SharpMeasurement();

	public DirectionalReading(Direction direction2) {
		direction = new Direction(direction2.getHeadingDegrees());
	}

	public Direction getServoDirection() {
		return direction;
	}


	public SharpMeasurement getSharp1Distance() {
		return sharp;
	}

	public void addSharpReading(float reading, boolean isLongRange) {
		this.sharp.add(reading, isLongRange);
	}
	
	public void addSharCM(float distance) {
		this.sharp.addCM(distance);
	}
}
