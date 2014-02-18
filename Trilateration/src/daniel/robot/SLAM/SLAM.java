package daniel.robot.SLAM;

import java.awt.geom.Point2D.Float;

import daniel.robot.Direction;
import daniel.robot.SLAM.ParticleFilter.ParticleFilter;
import daniel.robot.glWindow.model.DistanceSensorReadings;
import daniel.robot.glWindow.model.IRobotInterface;
import daniel.robot.glWindow.model.PoseCollection;
import daniel.robot.glWindow.model.State;

public class SLAM implements Runnable{
	
	public PoseCollection m_world;
	IRobotInterface 		   m_robot;
	private Thread m_myThread;
	
	public SLAM(IRobotInterface a_robot){
		m_robot = a_robot;
		
		m_myThread = new Thread(this);
		m_myThread.start();
		m_world = new PoseCollection();
	}

	
	public void run() {
		//Direction compassDirection;
		
		
		
		try {
			
			DistanceSensorReadings startUpReading = m_robot.makeReading();
			//Direction startupDirection = startUpReading.getCompassDirection();
			
			Direction startupDirection = new Direction(0);
			
			State startState = new State(new Float(0.0f, 0.0f), startupDirection);
			ParticleFilter startPosition = new ParticleFilter(startState, startUpReading);
			
			Pose startPose = new Pose(startPosition, startUpReading, new Movement(0, 0));
			m_world.add(startPose);
			Pose lastPose = startPose;
			
			for (int i =0 ;i< 15; i++) {
				
				Movement move = m_robot.makeMove();
				DistanceSensorReadings Z = m_robot.makeReading();
				ParticleFilter newFilter = new ParticleFilter(lastPose.m_position, move, Z);
				Pose newPose = new Pose(newFilter, Z, move);
				
				m_world.add(newPose);
				
				lastPose = newPose;
				
				
				//View...
				MatchingError error = MatchingError.getMatchingError( lastPose.getBestMap(), m_world.getLastPose().getBestGuessPosition(), Z);
				System.out.println(error);
				Float bestGuessPos = m_world.getLastPose().getBestGuessPosition().getRobotPosition();
				System.out.println(String.format("x=%f y=%f", bestGuessPos.x, bestGuessPos.y));
				System.out.println(String.format("dir=%f", m_world.getLastPose().getBestGuessPosition().m_heading.getHeadingDegrees()));
				
				Thread.sleep(1000);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
