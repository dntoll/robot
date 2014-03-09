package daniel.robot.slam;

import java.awt.geom.Point2D.Float;
import java.util.Collection;

import daniel.robot.Direction;
import daniel.robot.slam.map.Landmark;

public interface ILandmarkNode {

	void add(Landmark lm);
	
	void remove(Landmark lm);

	Collection<? extends Landmark> getLandmarks();

//	boolean contains(Landmark lm);

	boolean canAdd();

	ILandmarkNode getCopy();

	Prediction getClosestLandMark(float a_beamWidth, Direction direction,
			Float robotPosition);

		

}
