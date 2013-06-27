package daniel.robot.SLAM.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import daniel.robot.Robot;
import daniel.robot.SLAM.SLAM;

public class RobotPanel extends JPanel  implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	RobotDraw data = new RobotDraw(new ParticleFilterView());
	
	//private Robot m_robot;
	private SLAM m_slam;
	private boolean m_hasStarted = false;

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
    
    @Override
	public void actionPerformed(ActionEvent arg0) {
		if (m_hasStarted  == true) {
			updateRobot();
		} else {
			try {
				m_slam.startUp();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			m_hasStarted = true;
			data.draw(this.getGraphics(), getWidth(), getHeight(), m_slam.m_world);
		}
	}

    public void updateRobot() {
    	
    	try {
    		//this.invalidate();
    		//validate();
    		
    		
    		
    		
    		Thread.sleep(1000);
    		
    		System.out.println("start update");
    		m_slam.updateAfterMovement();
    		System.out.println("done update");
    		
    		data.draw(this.getGraphics(), getWidth(), getHeight(), m_slam.m_world);
    		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    

	
	
}
