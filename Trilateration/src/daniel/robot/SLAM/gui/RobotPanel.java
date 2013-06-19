package daniel.robot.SLAM.gui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

import daniel.robot.Direction;
import daniel.robot.Robot;
import daniel.robot.SLAM.Map;
import daniel.robot.SLAM.PoseCollection;
import daniel.robot.SLAM.Pose;
import daniel.robot.SLAM.SLAM;
import daniel.robot.SLAM.State;
import daniel.robot.sensors.IRReading;
import daniel.robot.sensors.SonarReading;

public class RobotPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	RobotDraw data = new RobotDraw(new ParticleFilterView());
	
	//private Robot m_robot;
	private SLAM m_slam;

	RobotPanel(Robot robot) {
    	//super(true);
        // set a preferred size for the custom panel.
        setPreferredSize(new Dimension(800,800));
        try {
			m_slam = new SLAM(robot);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    public void startUp() {
    	try {
			m_slam.startUp();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    public void updateRobot() {
    	
    	try {
    		//this.invalidate();
    		//validate();
    		Thread.sleep(1000);
    		
    		
    		data.draw(this.getGraphics(), getWidth(), getHeight(), m_slam.m_world);
    		for (int i = 10; i >= 0; i--) {
    			Thread.sleep(1000);
    			System.out.println(i);
    		}
    		
    		System.out.println("start update");
    		m_slam.updateAfterMovement();
    		System.out.println("done update");
    		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    

	
	
}
