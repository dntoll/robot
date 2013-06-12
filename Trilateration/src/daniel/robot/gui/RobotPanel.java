package daniel.robot.gui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImageOp;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import daniel.robot.IRReading;
import daniel.robot.Map;
import daniel.robot.Robot;
import daniel.robot.SLAM;
import daniel.robot.SensorReading;
import daniel.robot.SonarReading;
import daniel.robot.State;
import daniel.robot.World;

public class RobotPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Robot m_robot;
	SLAM m_slam;
	
    RobotPanel(Robot robot) {
        // set a preferred size for the custom panel.
        setPreferredSize(new Dimension(800,800));
        m_robot = robot;
        try {
			m_slam = new SLAM(m_robot);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public void startUp() {
    	try {
			m_slam.startUp();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

    public void updateRobot() {
    	try {
    		
    		m_slam.updateAfterMovement();
    		//m_readings.add(m_robot.SenseSome());
    		//validate();
    		//m_readings.add(m_robot.SenseAll());
    		validate();
    		paintComponent(this.getGraphics());
    		Thread.sleep(1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
    

	@Override
    public void paintComponent(Graphics g) {
		super.paintComponent(g);

        int posY = 400;
        int posX = 400;
        g.drawRect(posX-5, posY-5, 10, 10);
        
        g.translate(posX, posY);
        float scale = 4.0f;
        
        drawDistanceRings(g, scale);
        
        Graphics2D g2d = (Graphics2D) g;
        
        int layer = 0;
        for (World.Reading s : m_slam.m_world.m_world) {
        	layer++;
        	float transparency = 0.5f;// * ((float)layer / (float)m_slam.m_world.m_world.size());
        	
        	
        	drawSensorReading(g, scale, g2d, s, transparency);
        }
        
        
        
        g.setColor(Color.BLACK);
    	g2d.setComposite(AlphaComposite.getInstance(
                AlphaComposite.SRC_OVER, 1.0f));
    	
    //	if (m_readings.size() > 0)
    //		g.drawString("" + m_readings.get(m_readings.size()-1).m_compassDirection, 20, 20);        
        
    	g.translate(-posX, -posY);
    	
    	drawBestGuess(g2d, scale, m_slam.m_world.m_map);
    }

	private void drawBestGuess(Graphics2D g2d, float scale, Map map) {
		g2d.drawImage(map.getImage(), 0, 0, null);
		
		
	}

	private void drawDistanceRings(Graphics g, float scale) {
		for (int i = 0; i < 4; i++) {
            g.setColor(Color.BLACK);
            float distance = (i * 50 + 50) * scale;
	        g.drawArc(-(int)distance/2, -(int)distance/2, (int)distance, (int)distance, 0, 360);
        }
	}

	private void drawSensorReading(Graphics g, float scale, Graphics2D g2d,
			World.Reading reading, float transparency) {
		g.setColor(Color.BLACK);
		g2d.setComposite(AlphaComposite.getInstance(
		        AlphaComposite.SRC_OVER, 0.5f));
      
		State state = reading.getBestGuess();
		
		g.translate((int)(state.m_position.x * scale), (int)(state.m_position.y * scale));
		
		
		drawArc(g, 1, 200*scale, state.m_heading);    

		
		
		g2d.setComposite(AlphaComposite.getInstance(
		        AlphaComposite.SRC_OVER, transparency));
		
		
		g.setColor(Color.GREEN);
		int degrees = 15;
		for(SonarReading sr : reading.m_reading.m_sonar) {
			
			float distance = sr.m_distance * scale;
		    float servo  = state.m_heading -sr.m_servo +90;
		    drawArc(g, degrees, distance, servo);
		}
		
		
		degrees = 1;
		g.setColor(Color.RED);
		for(IRReading sr : reading.m_reading.m_ir) {
			float distance = sr.m_distance * scale;
		    float servo  = state.m_heading - sr.m_servo +90;
		    
		    drawArc(g, degrees, distance, servo);
		}
		
		g.translate(-(int)(state.m_position.x * scale), -(int)(state.m_position.y * scale));
	}

	private void drawArc(Graphics g, int degrees, float distance, float servo) {
		
		servo+= 180.0f;
		
		while (servo > 360)
			servo -= 360;
		while (servo < 0)
			servo += 360;
		
		servo = 360 - servo;
		
		
		g.fillArc(-(int)distance/2, -(int)distance/2, (int)distance, (int)distance, (int)servo-degrees/2, degrees);
	}

	

	
}
