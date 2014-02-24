package daniel.robot.SLAM;

import java.awt.geom.Point2D.Float;
import java.util.ArrayList;
import java.util.Collection;

import daniel.robot.Direction;

public class LandMarkCollectionTree implements ILandmarkCollection {
	private ArrayList<Landmark> landmarks = new ArrayList<Landmark>();
	private LandMarkCollectionTree left = new LandMarkCollectionTree();
	private LandMarkCollectionTree right = new LandMarkCollectionTree();
	
	@Override
	public void copy(ILandmarkCollection other) {
		LandMarkCollectionTree otherTree = (LandMarkCollectionTree) other;
		left = otherTree.left.clone();
		right = otherTree.right.clone();
	}

	@Override
	public void add(Landmark lm) {
		if (left.contains(lm)) {
			left.add(lm);
		} else {
			right.add(lm);
		}
		
	}

	@Override
	public void remove(Landmark lm) {
		if (left.contains(lm)) {
			left.remove(lm);
		} else {
			right.remove(lm);
		}
	}

	@Override
	public Landmark[] getAllLandmarks() {
		
		
		ArrayList<Landmark> all = getLandmarks();
		return all.toArray(new Landmark[all.size()]);
	}

	private ArrayList<Landmark> getLandmarks() {
		ArrayList<Landmark> temp = new ArrayList<Landmark>();
		temp.addAll(temp);
		temp.addAll(left.getLandmarks());
		temp.addAll(right.getLandmarks());
		return temp;
	}

	@Override
	public Prediction getClosestLandMark(float a_beamWidth,
			Direction direction, Float robotPosition) {
		// TODO Auto-generated method stub
		return null;
	}

}
