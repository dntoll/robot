package daniel.robot.SLAM;

import java.awt.geom.Point2D.Float;
import daniel.robot.SLAM.ParticleFilter.ParticleFilter;
import daniel.robot.glWindow.model.DistanceSensorReadings;

public class SLAM implements Runnable{
	
	public PoseCollection m_world;
	IRobotInterface 		   m_robot;
	private Thread m_myThread;
	
	public SLAM(IRobotInterface a_robot){
		m_robot = a_robot;
		
		m_myThread = new Thread(this);
		m_myThread.start();
		
	}

	
	public void run() {
		//Direction compassDirection;
		
		
		
		try {
			
			DistanceSensorReadings startUpReading = m_robot.makeReading();
			
			State startState = new State(new Float(0.0f, 0.0f), startUpReading.getCompassDirection());
			ParticleFilter startPosition = new ParticleFilter(startState, startUpReading);
			
			m_world = new PoseCollection(startPosition, startUpReading);
			
			for (int i =0 ;i< 3; i++) {
				
				
				
				Movement move = m_robot.makeMove();
				DistanceSensorReadings Z = m_robot.makeReading();
				m_world.moveAndSense(move, Z);
				
				
				//View...
				MatchingError error = MatchingError.getMatchingError( m_world.getLastPose().getBestMap(), m_world.getLastPose().getBestGuess().getState(), Z);
				System.out.println(error);
				Float bestGuessPos = m_world.getLastPose().getBestGuess().getState().getRobotPosition();
				System.out.println(String.format("x=%f y=%f", bestGuessPos.x, bestGuessPos.y));
				System.out.println(String.format("dir=%f", m_world.getLastPose().getBestGuess().getState().m_heading.getHeadingDegrees()));
				
				Thread.sleep(1000);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
