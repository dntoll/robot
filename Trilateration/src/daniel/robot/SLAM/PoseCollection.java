package daniel.robot.SLAM;

import java.util.ArrayList;

import daniel.robot.SLAM.ParticleFilter.ParticleFilter;
import daniel.robot.glWindow.model.DistanceSensorReadings;

public class PoseCollection {
	
	public ArrayList<Pose> poses = new ArrayList<Pose>();
	
	
	PoseCollection(ParticleFilter startPosition, DistanceSensorReadings reading) {
		Pose startPose = new Pose(startPosition, reading, new Movement(0, 0));
		poses.add(startPose);
	}

	

	public Pose getLastPose() {
		return poses.get(poses.size()-1);
	}
	
	public float getError() {
		return getLastPose().getBestGuess().getWeight();
	}

	public void moveAndSense(Movement move, DistanceSensorReadings sense) throws Exception {
		
		ParticleFilter newFilter = new ParticleFilter(getLastPose().m_position, move, sense);
		Pose newPose = new Pose(newFilter, sense, move);
		
		poses.add(newPose);
		
	//	newPose.m_position.getBestGuess().calculateWeight(sense);
		
		
	}

	
}
