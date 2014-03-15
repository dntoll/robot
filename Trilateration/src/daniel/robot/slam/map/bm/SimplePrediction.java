package daniel.robot.slam.map.bm;

import daniel.robot.glWindow.model.IPrediction;

public class SimplePrediction implements IPrediction {

	float distance;
	public SimplePrediction(float distance) {
		this.distance = distance;
		
	}

	@Override
	public float getDistance() {
		return distance;
	}

}
