package daniel.robot.gui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

import daniel.robot.Direction;
import daniel.robot.Map;
import daniel.robot.Robot;
import daniel.robot.SLAM;
import daniel.robot.State;
import daniel.robot.World;
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
        g2d.setColor(Color.YELLOW);
        g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		
		//draw measurements
		drawErrors(g2d);

        g.translate(posX, posY);
        drawDistanceRings(g, scale);
        
        
        for (World.Reading s : m_slam.m_world.m_world) {
        	float transparency = 0.5f;// * ((float)layer / (float)m_slam.m_world.m_world.size());
        	drawSensorReading(g, scale, g2d, s, transparency);
        }
        
        
        
        g.setColor(Color.BLACK);
    	g2d.setComposite(AlphaComposite.getInstance(
                AlphaComposite.SRC_OVER, 1.0f));

    	drawBestGuess(g2d, scale, m_slam.m_world.m_map);
    	if (m_slam.m_world.m_world.size() > 0) {
    		int index = m_slam.m_world.m_world.size()-1;
    		World.Reading reading = m_slam.m_world.m_world.get(index);
    		m_particleFilterView.draw(g2d, reading.m_particles, scale);
    	}
    	
    	
    	g.translate(-posX, -posY);
    }

	private void drawErrors(Graphics2D g) {
		g.setColor(Color.BLACK);
		
		int reading = 0;
		for (World.Reading s : m_slam.m_world.m_world) {
        	State state = s.getBestGuess();
        	g.drawString(state.toString(),  20, reading * 30);
        	
        	reading++;
        }
	}

	

	private void drawBestGuess(Graphics2D g2d, float scale, Map m_map) {
		 g2d.setColor(Color.BLACK);
		for (java.awt.geom.Point2D.Float fp : m_map.m_obstacles) {
			g2d.drawRect((int)(fp.x*scale), (int)(fp.y*scale), 1, 1);
		}
		
		
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
		
		g.setColor(Color.CYAN);
		drawArc(g, 1, 200*scale, reading.m_reading.m_compassDirection);

		g.drawRect(-5, -5, 10, 10);
		
		/*
		g2d.setComposite(AlphaComposite.getInstance(
		        AlphaComposite.SRC_OVER, transparency));
		
		
		g.setColor(Color.GREEN);
		int degrees = 15;
		for(SonarReading sr : reading.m_reading.m_sonar) {
			
			float distance = sr.m_distance * scale;
		    Direction servo  = state.m_heading.getHeadDirection(sr.m_servo);
		    drawArc(g, degrees, distance, servo);
		}
		
		
		degrees = 1;
		g.setColor(Color.RED);
		for(IRReading sr : reading.m_reading.m_ir) {
			float distance = sr.m_distance * scale;
			Direction servo  = state.m_heading.getHeadDirection(sr.m_servo);
		    
		    drawArc(g, degrees, distance, servo);
		}*/
		
		g.translate(-(int)(state.m_position.x * scale), -(int)(state.m_position.y * scale));
	}

	private void drawArc(Graphics g, int degrees, float distance, Direction a_head) {
		
		
		g.fillArc(-(int)distance/2, -(int)distance/2, (int)distance, (int)distance, (int)(a_head.getHeadingDegrees())-degrees/2, degrees);
	}

	

	
}