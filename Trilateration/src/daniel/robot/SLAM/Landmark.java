package daniel.robot.SLAM;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;

public class Landmark {
	public Point2D.Float pos;
	public float deviation;
	public float distanceMeasureFrom;
	
	
	public Landmark(Float position, float stdv, float distanceMeasureFrom) {
		pos = position;
		deviation = stdv;
		this.distanceMeasureFrom = distanceMeasureFrom;
	}


	public boolean isBetter(Prediction prediction) {
		if (deviation < prediction.landmark.deviation ) {
			return true;
		}
		
		return false;
	}


	public float getDifference(Landmark landmark) {
		// TODO Auto-generated method stub
		return (float) Math.sqrt((landmark.distanceMeasureFrom - distanceMeasureFrom)*(landmark.distanceMeasureFrom - distanceMeasureFrom));
	}
}