package daniel.robot.glWindow.model;

import daniel.robot.Direction;

public class DirectionalReading {

	Direction direction;
	Measurement sonar1 = new Measurement();
	SharpMeasurement sharp1 = new SharpMeasurement();

	public DirectionalReading(Direction direction2) {
		//copy needed?
		direction = new Direction(direction2.getHeadingDegrees());
		
	}

	public Direction getServoDirection() {
		return direction;
	}



	public Measurement getSonar1Distance() {
		return sonar1;
	}
	public SharpMeasurement getSharp1Distance() {
		return sharp1;
	}

	public void addSonar(float distance) {
		this.sonar1.add(distance);
	}
	
	public void addSharpReading(float reading) {
		this.sharp1.add(reading);
	}
	
	public void addSharCM(float distance) {
		this.sharp1.addCM(distance);
	}

	

	

}
