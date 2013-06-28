package daniel.robot.SLAM;

import java.util.ArrayList;

import daniel.robot.SLAM.ParticleFilter.ParticleFilter;
import daniel.robot.sensors.SensorReading;

public class PoseCollection {
	
	public ArrayList<Pose> m_sensorScans = new ArrayList<Pose>();
	
	
	PoseCollection(ParticleFilter startPosition, SensorReading reading) {
		Pose startPose = new Pose(startPosition, reading, new Movement(0, 0));
		m_sensorScans.add(startPose);
	}

	

	public Pose getLastPose() {
		return m_sensorScans.get(m_sensorScans.size()-1);
	}
	
	public float getError() {
		return getLastPose().getBestGuess().getWeight();
	}

	public void moveAndSense(Movement move, SensorReading sense) throws Exception {
		
		ParticleFilter newFilter = new ParticleFilter(getLastPose().m_position, move, sense);
		Pose newPose = new Pose(newFilter, sense, move);
		
		m_sensorScans.add(newPose);
		
	//	newPose.m_position.getBestGuess().calculateWeight(sense);
		
		
	}

	
}
