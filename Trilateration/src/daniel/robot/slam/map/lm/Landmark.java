package daniel.robot.slam.map.lm;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;

import daniel.robot.slam.Prediction;

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
		if (distanceMeasureFrom < prediction.getLandmark().distanceMeasureFrom ) {
			return true;
		}
		
		if (deviation < prediction.getLandmark().deviation ) {
			return true;
		}
		
		return false;
	}


	public float getDifference(Landmark landmark) {
		return (float) Math.sqrt((landmark.distanceMeasureFrom - distanceMeasureFrom)*
								 (landmark.distanceMeasureFrom - distanceMeasureFrom));
	}
}