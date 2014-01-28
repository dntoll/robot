package daniel.robot.sensors;

import daniel.robot.Direction;
import daniel.robot.Vector3f;


//http://www.loveelectronics.co.uk/Tutorials/8/hmc5883l-tutorial-and-arduino-library
public class CompassReading extends Vector3f {

	public CompassReading() {
		super(0,0,0);
	}
	
	public CompassReading(float cx, float cy, float cz) {
		super(cx, cy, cz);
	}
	
	public Direction getHeading() {
		
		float heading = (float) Math.atan2(y, x);
		float declinationAngle = 0.07418f;
		heading += declinationAngle;   
		
		// Correct for when signs are reversed.
		if(heading < 0)
			heading += 2.0*Math.PI;
		    
		// Check for wrap due to addition of declination.
		if(heading > 2.0*Math.PI)
			heading -= 2.0*Math.PI;
		  
		return Direction.RadiansToDegrees(heading);
	}

	public float getFlatLength() {
		return (float) Math.sqrt(x * x + y * y);
	}
}
