package daniel.robot.SLAM;

import java.awt.geom.Point2D;

import daniel.robot.Direction;

public interface ILandmarkCollection {

	public abstract void copy(ILandmarkCollection other);

	public abstract void add(Landmark lm);

	public abstract void remove(Landmark landmark);

	public abstract Landmark[] getAllLandmarks();
	
	public abstract Prediction getClosestLandMark(float a_beamWidth, Direction direction,
			Point2D.Float robotPosition);

}