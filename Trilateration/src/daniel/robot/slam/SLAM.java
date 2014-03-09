package daniel.robot.slam;

import java.awt.geom.Point2D.Float;

import daniel.robot.Direction;
import daniel.robot.glWindow.model.DirectionalReadingCollection;
import daniel.robot.glWindow.model.IRobotInterface;
import daniel.robot.glWindow.model.PoseCollection;
import daniel.robot.glWindow.model.State;

public class SLAM implements Runnable{
	
	public PoseCollection m_world;
	private IRobotInterface 		   m_robot;
	private Thread m_myThread;
	private boolean isRunning = false;
	
	public SLAM(IRobotInterface a_robot){
		m_robot = a_robot;
		
		m_myThread = new Thread(this);
		m_myThread.start();
		m_world = new PoseCollection();
	}

	
	public void run() {
		
		try {
			while (doRunSlam() == false) {
				Thread.sleep(1000);
			}
			DirectionalReadingCollection startUpReading = m_robot.makeReading();
			//Direction startupDirection = startUpReading.getCompassDirection();
			
			Direction startupDirection = new Direction(0);
			
			State startState = new State(new Float(0.0f, 0.0f), startupDirection);
			ParticleFilter startPosition = new ParticleFilter(startState, startUpReading);
			
			Pose startPose = new Pose(startPosition, startUpReading, new Movement(0, 0));
			m_world.add(startPose);
			Pose lastPose = startPose;
			
			for (int i =0 ;i< 150; i++) {
				
				while (doRunSlam() == false) {
					Thread.sleep(1000);
				}
				Long startTime = System.currentTimeMillis();
				Movement move = m_robot.makeMove();
				DirectionalReadingCollection sense = m_robot.makeReading();
				
				ParticleFilter newFilter = new ParticleFilter(lastPose.m_position, move, sense, m_world);
				Pose newPose = new Pose(newFilter, sense, move);
				
				
				m_world.add(newPose);
				
				
				
				lastPose = newPose;
				
				
				//View...
				MatchingError error = MatchingError.getMatchingError( lastPose.getBestMap(), m_world.getLastPose().getBestGuessPosition(), sense);
				System.out.println(error);
				Float bestGuessPos = m_world.getLastPose().getBestGuessPosition().getRobotPosition();
				System.out.println(String.format("x=%f y=%f", bestGuessPos.x, bestGuessPos.y));
				System.out.println(String.format("dir=%f", m_world.getLastPose().getBestGuessPosition().m_heading.getHeadingDegrees()));
				
				Long endTime = System.currentTimeMillis();
				
				System.out.println("Time " + (endTime-startTime)/1000.0f);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	
	private synchronized boolean doRunSlam() {
		return isRunning;
	}

	public synchronized void start() {
		isRunning = true;
	}
	
	public synchronized void stop() {
		isRunning = false;
		
	}
	
	
}
