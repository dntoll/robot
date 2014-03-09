package daniel.robot.slam.map;

import java.awt.geom.Point2D.Float;
import java.util.ArrayList;
import java.util.Collection;

import daniel.robot.Direction;
import daniel.robot.slam.ILandmarkNode;
import daniel.robot.slam.Prediction;

public class LandMarkNode implements ILandmarkNode {
	protected ILandmarkNode left = new LandMarkLeaf();
	protected ILandmarkNode right = new LandMarkLeaf();
	private boolean xSplit = true;
	private float splitAt = 0; //split at this x/y coordinate
	
	protected LandMarkNode(boolean xsplit, float splitAt, Collection<? extends Landmark> collection) {
		xSplit = xsplit;
		this.splitAt = splitAt;
		
		if (collection != null) {
			for(Landmark m : collection) {
				this.add(m);
			}
		}
	}
	
	
	public void add(Landmark lm) {
		ILandmarkNode child = getContainingNode(lm.pos);
		
		
		if (child.canAdd()) {
			child.add(lm);
		} else {
			float middle = ((LandMarkLeaf)child).getMiddleValue(!xSplit);
			child = new LandMarkNode(!xSplit, middle, child.getLandmarks());
			
			if (isLeftContaining(lm.pos)) {
				left = child;
			} else {
				right = child;
			}
		}
		
	}


	private ILandmarkNode getContainingNode(Float pos) {
		return isLeftContaining(pos) ? left : right;
	}


	private boolean isLeftContaining(Float position) {
		
		if (xSplit) {
			if (position.x < splitAt) {
				return true;
			} else {
				return false;
			}
		} else {
			if (position.y < splitAt) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	
	public void remove(Landmark lm) {
		
		ILandmarkNode child = getContainingNode(lm.pos);
		child.remove(lm);
	}
	
	public ArrayList<Landmark> getLandmarks() {
		ArrayList<Landmark> temp = new ArrayList<Landmark>();
		temp.addAll(left.getLandmarks());
		temp.addAll(right.getLandmarks());
		return temp;
	}
	
	@Override
	public boolean canAdd() {
		return true;
	}


	public void copyTo(LandMarkNode copy) {
		copy.left =  left.getCopy();
		copy.right =  right.getCopy();
		
		copy.xSplit = xSplit;
		copy.splitAt = splitAt; 
		
	}


	@Override
	public ILandmarkNode getCopy() {
		
		LandMarkNode clone = new LandMarkNode(xSplit, splitAt, null);
		clone.left =  left.getCopy();
		clone.right =  right.getCopy();
		return clone;
	}

	@Override
	public Prediction getClosestLandMark(float a_beamWidth, Direction direction,
			Float robotPosition) {
		
		
		float maxDistance = java.lang.Float.MAX_VALUE;
		Float target = new Float(robotPosition.x + direction.getX() * maxDistance, 
								 robotPosition.y + direction.getY() * maxDistance);
		
		ILandmarkNode nodeContainingRobot = getContainingNode(robotPosition);
		ILandmarkNode nodeContainingTarget = getContainingNode(target);
		
		if (nodeContainingRobot == nodeContainingTarget) {
			return nodeContainingTarget.getClosestLandMark(a_beamWidth, direction, robotPosition);
		} else {
			Prediction ret1 = nodeContainingRobot.getClosestLandMark(a_beamWidth, direction, robotPosition);
			Prediction ret2 = nodeContainingTarget.getClosestLandMark(a_beamWidth, direction, robotPosition);
			
			if (ret1 == null) {
				return ret2;
			}
			if (ret2 == null) {
				return ret1;
			}
			
			if (ret1.isCloserThan(ret2))
				return ret1;
			else
				return ret2;
		}
		
	}
}
