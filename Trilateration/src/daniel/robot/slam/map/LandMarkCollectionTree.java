package daniel.robot.slam.map;

import java.awt.geom.Point2D.Float;
import java.util.ArrayList;
import daniel.robot.Direction;
import daniel.robot.slam.ILandmarkCollection;
import daniel.robot.slam.Prediction;


public class LandMarkCollectionTree  extends LandMarkNode implements ILandmarkCollection {
	
	
	public LandMarkCollectionTree() {
		super(true, 0, null);
	}
	
	
	
	@Override
	public void copy(ILandmarkCollection other) {
		LandMarkNode otherTree = (LandMarkNode) other;
		
		otherTree.copyTo( (LandMarkNode) this);
		
	}

	

	@Override
	public void add(Landmark lm) {
		super.add(lm);
	}

	
	@Override
	public void remove(Landmark lm) {
		super.remove(lm);
	}

	@Override
	public Landmark[] getAllLandmarks() {
		ArrayList<Landmark> all = super.getLandmarks();
		return all.toArray(new Landmark[all.size()]);
	}

	

	@Override
	public Prediction getClosestLandMark(float a_beamWidth,	Direction direction, Float robotPosition) {
		return super.getClosestLandMark( a_beamWidth,	direction, robotPosition);
		
	}
	

}
