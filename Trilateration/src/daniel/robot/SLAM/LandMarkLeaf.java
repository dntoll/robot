package daniel.robot.SLAM;

import java.awt.geom.Point2D.Float;
import java.util.ArrayList;
import java.util.Collection;

import daniel.robot.Direction;

public class LandMarkLeaf implements ILandmarkNode {
	private ArrayList<Landmark> landmarks = new ArrayList<Landmark>();
	


	
	public void add(Landmark lm) {
		landmarks.add(lm);
	}

	
	public void remove(Landmark landmark) {
		landmarks.remove(landmark);
	}

	public Prediction getClosestLandMark(float a_beamWidth,
			Direction direction, Float robotPosition) {
		float minLenSquare = java.lang.Float.MAX_VALUE;
		Prediction ret = null;
		
		for (Landmark obstacle : landmarks ) {
			float dx = (obstacle.pos.x - robotPosition.x);
			float dy = (obstacle.pos.y - robotPosition.y);
			
			Direction toObstacle = Direction.RadiansToDegrees((float) Math.atan2(dy, dx));
			
			float degreesDifference = toObstacle.GetDifferenceInDegrees(direction);
			
			if (degreesDifference < a_beamWidth ) {
				float distanceSquare = dx * dx + dy * dy;
				if (distanceSquare  < minLenSquare) {
					minLenSquare = distanceSquare;
					ret = new Prediction(minLenSquare, obstacle);
				}
			}
		}
		return ret;
	}


	@Override
	public Collection<? extends Landmark> getLandmarks() {
		return landmarks;
	}


	@Override
	public boolean canAdd() {
		return landmarks.size() < 200;
	}


	public float getMiddleValue(boolean xSplit) {
		float smallest = java.lang.Float.MAX_VALUE;
		float biggest = java.lang.Float.MIN_VALUE;
		
		if (xSplit) {
			for(Landmark lm : landmarks) {
				if (lm.pos.x < smallest) {
					smallest =lm.pos.x; 
				}
				if (lm.pos.x > biggest) {
					biggest  =lm.pos.x; 
				}
			}
		} else {
			for(Landmark lm : landmarks) {
				if (lm.pos.y < smallest) {
					smallest =lm.pos.y; 
				}
				if (lm.pos.y > biggest) {
					biggest  =lm.pos.y; 
				}
			}
		}
		return (biggest - smallest) / 2.0f + smallest;
	}


	@Override
	public ILandmarkNode getCopy() {
		LandMarkLeaf ret = new LandMarkLeaf();
		ret.landmarks.addAll(landmarks);
		return ret;
	}

}
