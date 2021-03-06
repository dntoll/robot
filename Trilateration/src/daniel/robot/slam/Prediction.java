package daniel.robot.slam;

import daniel.robot.glWindow.model.IPrediction;
import daniel.robot.slam.map.lm.Landmark;


public class Prediction implements IPrediction {
	private float minLenSquareDistance;
	private boolean hasCalculatedDistance = false;
	private float distance;
	private Landmark landmark;
	
	public Landmark getLandmark() {
		return landmark;
	}

	public Prediction(float minLenSquare, Landmark end) {
		minLenSquareDistance = minLenSquare;
		landmark = end;
	}
	
	public float getDistance() {
		if (hasCalculatedDistance == false) {
			distance = (float) Math.sqrt(minLenSquareDistance);
			hasCalculatedDistance = true;
		} 
		return distance;
		
	}
		
	public boolean isCloserThan(Prediction ret2) {
		return minLenSquareDistance < ret2.minLenSquareDistance;
	}
}