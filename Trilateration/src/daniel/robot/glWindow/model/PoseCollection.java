package daniel.robot.glWindow.model;

import java.util.ArrayList;

public class PoseCollection {
	
	public ArrayList<IPose> poses = new ArrayList<IPose>();
	
	
	public PoseCollection() {
	}

	

	public IPose getLastPose() {
		if (poses.size() > 0)
			return poses.get(poses.size()-1);
		return null;
	}
	
	public float getError() {
		return getLastPose().getError();
	}

	public void add(IPose newPose) throws Exception {
		poses.add(newPose);
		
		
	}

	
}
