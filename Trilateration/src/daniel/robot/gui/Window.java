package daniel.robot.gui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import daniel.robot.Robot;
import daniel.robot.RobotFinder;
import daniel.robot.SLAM;
import daniel.robot.sensors.IRReading;
import daniel.robot.sensors.SensorReading;
import daniel.robot.sensors.SonarReading;

public class Window extends javax.swing.JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3804348774380077112L;

	private RobotPanel m_roboPanel;
	PanelUpdater m_updater;
	
	Thread m_panelThread;
	Robot m_robot;
	
	
	public Window(Robot robot) {
		m_robot = robot;
		

		
	    m_roboPanel = new RobotPanel(robot);
	    m_updater = new PanelUpdater(m_roboPanel);
	    
	    JButton b1 = new JButton("Done Calibration");
	    
	    m_roboPanel.setBackground(new java.awt.Color(255, 255, 255));
	    m_roboPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
	    
	    m_roboPanel.add(b1);
	    this.setContentPane(m_roboPanel);
	    b1.addActionListener(this);
	    b1.setEnabled(true);
	    
        // be nice to testers..
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    pack();
	    
	    robot.CalibrateCompass();
	    
	}
	
	void startThread() {
		
		m_robot.StopCalibrateCompass();
		m_robot.Wait();
		if (m_panelThread != null) {
			return;
		}
		m_roboPanel.startUp();
		m_panelThread = new Thread(m_updater);
		m_panelThread.start();
	}
	
	
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
    	startThread();
	}
	
	public static void main(String args[]) {
	    java.awt.EventQueue.invokeLater(new Runnable() {

	        public void run() {
	        	RobotFinder finder = new RobotFinder();
				Robot robot;
				try {
					robot = finder.findRobots();
				
					robot.Wait();
					
		            Window e = new Window(robot);
		            e.setVisible(true);
		            
	            
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
	    });
	}

	
}
