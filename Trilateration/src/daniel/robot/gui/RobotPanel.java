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

import javax.swing.JPanel;

import daniel.robot.IRReading;
import daniel.robot.Robot;
import daniel.robot.SensorReading;
import daniel.robot.SonarReading;

public class RobotPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Robot m_robot;
	List<SensorReading> m_readings = new ArrayList<SensorReading>();
    RobotPanel(Robot robot) {
        // set a preferred size for the custom panel.
        setPreferredSize(new Dimension(800,800));
        m_robot = robot;
    }

    public void updateRobot() {
    	try {
    		
    		
    		//m_readings.add(m_robot.SenseSome());
    		//validate();
    		m_readings.add(m_robot.SenseAll());
    		validate();
    		paintComponent(this.getGraphics());
    		Thread.sleep(5000);
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
        
        for (int i = 0; i < 4; i++) {
            g.setColor(Color.BLACK);
            float distance = (i * 50 + 50) * scale;
	        g.drawArc(-(int)distance/2, -(int)distance/2, (int)distance, (int)distance, 0, 180);
        }
        
        Graphics2D g2d = (Graphics2D) g;
        g2d.setComposite(AlphaComposite.getInstance(
                AlphaComposite.SRC_OVER, 0.2f));
        
        
        int layer = 0;
        for (SensorReading s : m_readings) {
        	layer++;
        	g.setColor(Color.BLACK);
        	g2d.setComposite(AlphaComposite.getInstance(
                    AlphaComposite.SRC_OVER, 0.5f));
        
        	drawArc(g, 1, 200*scale, s.m_compassDirection);    

        	float transparency = 0.5f * ((float)layer / (float)m_readings.size());
        	
            g2d.setComposite(AlphaComposite.getInstance(
                    AlphaComposite.SRC_OVER, transparency));
            
        	
            g.setColor(Color.GREEN);
            int degrees = 15;
            for(SonarReading sr : s.m_sonar) {
            	
            	float distance = sr.m_distance * scale;
	            float servo  = s.m_compassDirection -sr.m_servo +90;
	            drawArc(g, degrees, distance, servo);
            }
            
            
            degrees = 1;
            g.setColor(Color.RED);
            for(IRReading sr : s.m_ir) {
            	float distance = sr.m_distance * scale;
	            float servo  = s.m_compassDirection-sr.m_servo +90;
	            
	            drawArc(g, degrees, distance, servo);
            }
            
            
             
        } 
        g.setColor(Color.BLACK);
    	g2d.setComposite(AlphaComposite.getInstance(
                AlphaComposite.SRC_OVER, 1.0f));
    	
    	if (m_readings.size() > 0)
    		g.drawString("" + m_readings.get(m_readings.size()-1).m_compassDirection, 20, 20);        
        
        
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
