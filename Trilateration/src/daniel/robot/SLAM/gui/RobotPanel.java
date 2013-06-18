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
import daniel.robot.SLAM.MeasurementCollection;
import daniel.robot.SLAM.Reading;
import daniel.robot.SLAM.SLAM;
import daniel.robot.SLAM.State;
import daniel.robot.sensors.IRReading;
import daniel.robot.sensors.SonarReading;

public class RobotPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Robot m_robot;
	SLAM m_slam;
	ParticleFilterView m_particleFilterView = new ParticleFilterView();
	
    RobotPanel(Robot robot) {
    	//super(true);
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
    		
    		
    		//m_readings.add(m_robot.SenseSome());
    		//validate();
    		//m_readings.add(m_robot.SenseAll());
    		this.invalidate();
    		validate();
    		Thread.sleep(1000);
    		
    		
    		update(this.getGraphics());
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
    
    

	//@Override
   // public void paintComponent(Graphics g) {
    //super.paintComponent(g);
	public void update(Graphics g) {
		
		int posY = 400;
        int posX = 400;
        float scale = 4.0f;
        
        Graphics2D g2d = (Graphics2D) g;
		
        g2d.setComposite(AlphaComposite.getInstance(
                AlphaComposite.SRC_OVER, 1.0f));
		//clean
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		
		//draw measurements
		//drawErrors(g2d);

        g.translate(posX, posY);
         
        g.setColor(Color.BLACK);
    	g2d.setComposite(AlphaComposite.getInstance(
                AlphaComposite.SRC_OVER, 1.0f));

    	drawBestGuess(g2d, scale, m_slam.m_world);
    	
    	if (m_slam.m_world.m_world.size() > 0) {
    		int index = m_slam.m_world.m_world.size()-1;
    		daniel.robot.SLAM.Reading reading = m_slam.m_world.m_world.get(index);
    		
    		
    		State bestGuess = reading.getBestGuess();
    		
    		drawSensorReading(g2d, scale, reading);
    		
    		g.translate((int)(bestGuess.m_position.x * scale), (int)(bestGuess.m_position.y * scale));
    		drawDistanceRings(g2d, scale);
    		g.translate((int)(-bestGuess.m_position.x * scale), -(int)(bestGuess.m_position.y * scale));
    		
    		m_particleFilterView.draw(g2d, reading.m_particles, scale);
    	}
    	
    	 int layer = 1;
         for (daniel.robot.SLAM.Reading s : m_slam.m_world.m_world) {
         	float transparency =  1.0f;//((float)layer / (float)m_slam.m_world.m_world.size());
         	drawPosition(g, scale, g2d, s, transparency);
         	layer++;
         }
    	
    	
    	g.translate(-posX, -posY);
    }

	/*private void drawErrors(Graphics2D g) {
		g.setColor(Color.BLACK);
		
		int reading = 0;
		for (World.Reading s : m_slam.m_world.m_world) {
        	State state = s.getBestGuess();
        	g.drawString(state.toString(),  20, reading * 30);
        	
        	reading++;
        }
	}*/

	

	private void drawBestGuess(Graphics2D g2d, float scale, MeasurementCollection a_world) {
		 g2d.setColor(Color.BLACK);
		 
		 for (Reading r : a_world.m_world) {
			for (java.awt.geom.Point2D.Float fp : r.m_map.m_obstacles) {
				g2d.fillRect((int)(fp.x*scale), (int)(fp.y*scale), (int)scale, (int)scale);
			}
		 }
		
		
	}

	private void drawDistanceRings(Graphics2D g, float scale) {
		g.setComposite(AlphaComposite.getInstance(
		        AlphaComposite.SRC_OVER, 0.5f));
		g.setColor(Color.BLUE);
		for (int i = 0; i < 15; i++) {
            //10 cm
            float distance = (i * 20 + 20) * scale;
	        g.drawArc(-(int)distance/2, -(int)distance/2, (int)distance, (int)distance, 0, 360);
        }
	}
	
	private void drawSensorReading(Graphics2D g,  float scale, daniel.robot.SLAM.Reading reading) {
		State state = reading.getBestGuess();
		
		//g.translate((int)(state.m_position.x * scale), (int)(state.m_position.y * scale));
		int viewPlayerPosX = (int)(state.m_position.x * scale);
		int viewPlayerPosY = (int)(state.m_position.y * scale);
		
		g.setComposite(AlphaComposite.getInstance(
		        AlphaComposite.SRC_OVER, 0.75f));
		
		
		
		//int degrees = 1;
		g.setColor(Color.ORANGE);
		for(IRReading sr : reading.m_reading.m_ir) {
			Direction servo  = state.m_heading.getHeadDirection(sr.m_servo);
			float distance = sr.m_distance * scale;
			drawArc(g, viewPlayerPosX, viewPlayerPosY, servo, distance);
		}
		
		//degrees = 15;
		g.setColor(Color.GREEN);
		for(SonarReading sr : reading.m_reading.m_sonar) {
			Direction servo  = state.m_heading.getHeadDirection(sr.m_servo);
			float distance = sr.m_distance * scale;
			drawArc(g, viewPlayerPosX, viewPlayerPosY, servo, distance);
		}
		
		
		
	}

	private void drawPosition(Graphics g, float scale, Graphics2D g2d,
			daniel.robot.SLAM.Reading reading, float transparency) {
		
		g2d.setComposite(AlphaComposite.getInstance(
		        AlphaComposite.SRC_OVER, transparency));
      
		State state = reading.getBestGuess();
		
		//g.translate((int)(state.m_position.x * scale), (int)(state.m_position.y * scale));
		int viewPlayerPosX = (int)(state.m_position.x * scale);
		int viewPlayerPosY = (int)(state.m_position.y * scale);
		
		int headingX = viewPlayerPosX + (int)(state.m_heading.getX() * 20 *scale);
		int headingY = viewPlayerPosY + (int)(state.m_heading.getY() * 20 *scale);
		int compassX = viewPlayerPosX + (int)(reading.m_reading.m_compassDirection.getX() * 10 *scale);
		int compassY = viewPlayerPosY + (int)(reading.m_reading.m_compassDirection.getY() * 10 *scale);
		
		g.setColor(Color.BLACK);
		g.drawLine(viewPlayerPosX, viewPlayerPosY, headingX, headingY);
		
		g.setColor(Color.CYAN);
		g.drawLine(viewPlayerPosX, viewPlayerPosY, compassX, compassY);

		g.setColor(Color.BLACK);
		g.drawRect(viewPlayerPosX-5, viewPlayerPosY-5, 10, 10);
		
		
		
	}

	private void drawArc(Graphics g, int viewPlayerPosX, int viewPlayerPosY,
			Direction servo, float distance) {
		int lineDirX = viewPlayerPosX + (int)(servo.getX() * distance);
		int lineDirY = viewPlayerPosY + (int)(servo.getY() * distance);
		
		g.drawLine(viewPlayerPosX, viewPlayerPosY, lineDirX, lineDirY);
	}
	
}
