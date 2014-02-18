package daniel.robot.SLAM;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;

public class Landmark {
	public Point2D.Float pos;
	public float deviation;
	
	
	public Landmark(Float position, float stdv) {
		pos = position;
		deviation = stdv;
	}


	public boolean isBetter(Pair prediction) {
		if (deviation < prediction.landmark.deviation ) {
			return true;
		}
		
		return false;
	}
}