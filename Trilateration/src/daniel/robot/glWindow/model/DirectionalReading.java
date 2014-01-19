package daniel.robot.glWindow.model;

import daniel.robot.Direction;

public class DirectionalReading {

	Direction direction;
	float sonar1 = 0.0f;
	float sonar2 = 0.0f;
	float sharp1 = 0.0f;
	float sharp2 = 0.0f;

	public DirectionalReading(float heading, float sonar1, float sharp1,
			float sonar2, float sharp2) {
		direction = new Direction((float)heading);
		this.sonar1 = sonar1;
		this.sharp1 = sharp1;
		this.sonar2 = sonar2;
		this.sharp2 = sharp2;
	}

	

	public Direction getDirection() {
		return direction;
	}



	public float getSonar1Distance() {
		return sonar1;
	}
	public float getSonar2Distance() {
		return sonar2;
	}
	public float getSharp1Distance() {
		return sharp1;
	}
	public float getSharp2Distance() {
		return sharp2;
	}

}
