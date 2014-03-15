package daniel.robot.slam;

import java.awt.geom.Point2D.Float;
import java.io.IOException;

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
			doWait();
			Pose lastPose = getStartupPose();
			m_world.add(lastPose);
			for (int i =0 ;i< 150; i++) {
				
				doWait();
				
				Movement move = m_robot.makeMove();
				DirectionalReadingCollection sense = m_robot.makeReading();
				
				Long startTime = System.currentTimeMillis();
				lastPose = getNextPose(lastPose.m_position, move, sense);
				m_world.add(lastPose);
				
				Long endTime = System.currentTimeMillis();
				printPoseError(lastPose, endTime - startTime, sense);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Pose getStartupPose() throws IOException, InterruptedException,
	Exception {
		DirectionalReadingCollection startUpReading = m_robot.makeReading();
		Direction startupDirection = startUpReading.getCompassDirection();
		
		State startState = new State(new Float(0.0f, 0.0f), startupDirection);
		ParticleFilter startPosition = new ParticleFilter(startState, startUpReading);
		
		Pose startPose = new Pose(startPosition, startUpReading, new Movement(0, 0));
		return startPose;
	}

	public Pose getNextPose(ParticleFilter lastPosition, Movement move, DirectionalReadingCollection sense) throws InterruptedException,
			IOException, Exception {
		
		ParticleFilter newFilter = new ParticleFilter(lastPosition, move, sense, m_world);
		Pose newPose = new Pose(newFilter, sense, move);
		return newPose;
	}


	public void doWait() throws InterruptedException {
		while (doRunSlam() == false) {
			Thread.sleep(1000);
		}
	}


	public void printPoseError(Pose lastPose, Long time,
			DirectionalReadingCollection sense) {
		
		
		MatchingError error = MatchingError.getMatchingError( lastPose.getBestMap(), m_world.getLastPose().getBestGuessPosition(), sense);
		Float bestGuessPos = m_world.getLastPose().getBestGuessPosition().getRobotPosition();
		
		System.out.println(error);
		System.out.println(String.format("x=%f y=%f", bestGuessPos.x, bestGuessPos.y));
		System.out.println(String.format("dir=%f", m_world.getLastPose().getBestGuessPosition().m_heading.getHeadingDegrees()));
		System.out.println("Computation Time " + (time)/1000.0f);
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
