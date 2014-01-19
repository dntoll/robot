package daniel.robot.SLAM.gui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import daniel.robot.Robot;
import daniel.robot.server.RobotFinder;

public class Window extends javax.swing.JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3804348774380077112L;

	private RobotPanel m_roboPanel;
	PanelUpdater m_updater;
	
	Thread m_panelThread;
	Robot m_robot;
	boolean m_calibration = false;
	JButton m_calibrationButton;
	
	public Window(Robot robot) {
		JPanel content = new JPanel();
		content.setLayout(new FlowLayout(FlowLayout.RIGHT));
		this.setContentPane(content);
		
		m_robot = robot;
		
		
		
	    m_roboPanel = new RobotPanel(robot);
	    m_roboPanel.setBackground(new java.awt.Color(255, 255, 255));
	    m_roboPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
	    m_updater = new PanelUpdater(m_roboPanel);
	    
	    
	    m_calibrationButton = new JButton("Calibration");
	    JButton nextButton = new JButton("Next");
	    
	    
	    content.add(m_roboPanel);
	    content.add(m_calibrationButton);
	    content.add(nextButton);
	    
	    m_calibrationButton.addActionListener(this);
	    m_calibrationButton.setEnabled(true);
	    
	    nextButton.addActionListener(m_roboPanel);
	    nextButton.setEnabled(true);
	    
        // be nice to testers..
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    pack();
	    
	   
	    
	}
	
	void startThread() {
		
		
		if (m_panelThread != null) {
			return;
		}
		m_panelThread = new Thread(m_updater);
		
		m_robot.StopCalibrateCompass();
		m_robot.Wait();
		m_roboPanel.startUp();
		
		m_panelThread.start();
	}
	
	
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (m_calibration == false) {
			m_robot.CalibrateCompass();
			m_calibrationButton.setText("Done Calibration");
		} else {
			startThread();
		}
	}
	
	public static void main(String args[]) {
	    java.awt.EventQueue.invokeLater(new Runnable() {

	        public void run() {
	        	RobotFinder finder = new RobotFinder();
				Robot robot;
				try {
					robot = new Robot(finder.findRobots());
				
				//	robot.Wait();
					
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
