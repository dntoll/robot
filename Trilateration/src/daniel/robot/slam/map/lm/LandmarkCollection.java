package daniel.robot.slam.map.lm;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import daniel.robot.Direction;
import daniel.robot.slam.ILandmarkCollection;
import daniel.robot.slam.Prediction;

public class LandmarkCollection implements ILandmarkCollection {
	private ArrayList<Landmark> landmarks = new ArrayList<Landmark>();
	
	/* (non-Javadoc)
	 * @see daniel.robot.SLAM.ILandmarkCollection#copy(daniel.robot.SLAM.LandmarkCollection)
	 */
	@Override
	public void copy(ILandmarkCollection other) {
		LandmarkCollection otherArray = (LandmarkCollection) other;
		landmarks.addAll(otherArray.landmarks);
		
	}

	/* (non-Javadoc)
	 * @see daniel.robot.SLAM.ILandmarkCollection#add(daniel.robot.SLAM.Landmark)
	 */
	@Override
	public void add(Landmark lm) {
		landmarks.add(lm);
	}

	/* (non-Javadoc)
	 * @see daniel.robot.SLAM.ILandmarkCollection#remove(daniel.robot.SLAM.Landmark)
	 */
	@Override
	public void remove(Landmark landmark) {
		landmarks.remove(landmark);
	}
	
	public Prediction getClosestLandMark(float a_beamWidth, Direction direction,
			Point2D.Float robotPosition) {
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

	/* (non-Javadoc)
	 * @see daniel.robot.SLAM.ILandmarkCollection#getAllLandmarks()
	 */
	@Override
	public Landmark[] getAllLandmarks() {
		return landmarks.toArray(new Landmark[landmarks.size()]);
	}

}
