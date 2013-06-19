package daniel.robot.SLAM.gui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import daniel.robot.Direction;
import daniel.robot.Robot;
import daniel.robot.SLAM.PoseCollection;
import daniel.robot.SLAM.Pose;
import daniel.robot.SLAM.SLAM;
import daniel.robot.SLAM.State;
import daniel.robot.sensors.IRReading;
import daniel.robot.sensors.SonarReading;

public class RobotDraw {
	
	
	public ParticleFilterView m_particleFilterView;

	public RobotDraw(ParticleFilterView m_particleFilterView) {
		this.m_particleFilterView = m_particleFilterView;
	}
	
	public void draw(Graphics g, int a_width, int a_height, PoseCollection a_knowledge) {
		
		int posY = 400;
        int posX = 400;
        float scale = 4.0f;
        
        Graphics2D g2d = (Graphics2D) g;
		
        g2d.setComposite(AlphaComposite.getInstance(
                AlphaComposite.SRC_OVER, 1.0f));
		//clean
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, a_width, a_height);
		

        g.translate(posX, posY);
         
        g.setColor(Color.BLACK);
    	g2d.setComposite(AlphaComposite.getInstance(
                AlphaComposite.SRC_OVER, 1.0f));

    	drawBestGuess(g2d, scale, a_knowledge);
    	
    	if (a_knowledge.m_sensorScans.size() > 0) {
    		int index = a_knowledge.m_sensorScans.size()-1;
    		daniel.robot.SLAM.Pose reading = a_knowledge.m_sensorScans.get(index);
    		
    		
    		State bestGuess = reading.getBestGuess();
    		
    		drawSensorReading(g2d, scale, reading);
    		
    		g.translate((int)(bestGuess.m_position.x * scale), (int)(bestGuess.m_position.y * scale));
    		drawDistanceRings(g2d, scale);
    		g.translate((int)(-bestGuess.m_position.x * scale), -(int)(bestGuess.m_position.y * scale));
    		
    		m_particleFilterView.draw(g2d, reading.m_position, scale);
    	}
    	
    	 int layer = 1;
         for (daniel.robot.SLAM.Pose s : a_knowledge.m_sensorScans) {
         	float transparency =  1.0f;//((float)layer / (float)m_slam.m_world.m_world.size());
         	drawPosition(g, scale, g2d, s, transparency);
         	layer++;
         }
    	
    	
    	g.translate(-posX, -posY);
    }

	
	private void drawBestGuess(Graphics2D g2d, float scale, PoseCollection a_world) {
		 g2d.setColor(Color.BLACK);
		 
		 for (Pose r : a_world.m_sensorScans) {
			for (java.awt.geom.Point2D.Float fp : r.m_bestGuessMap.m_obstacles) {
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
	
	private void drawSensorReading(Graphics2D g,  float scale, daniel.robot.SLAM.Pose reading) {
		State state = reading.getBestGuess();
		
		//g.translate((int)(state.m_position.x * scale), (int)(state.m_position.y * scale));
		int viewPlayerPosX = (int)(state.m_position.x * scale);
		int viewPlayerPosY = (int)(state.m_position.y * scale);
		
		g.setComposite(AlphaComposite.getInstance(
		        AlphaComposite.SRC_OVER, 0.75f));
		
		
		
		//int degrees = 1;
		g.setColor(Color.ORANGE);
		for(IRReading sr : reading.m_sensorReading.m_ir) {
			Direction servo  = state.m_heading.getHeadDirection(sr.m_servo);
			float distance = sr.m_distance * scale;
			drawArc(g, viewPlayerPosX, viewPlayerPosY, servo, distance);
		}
		
		//degrees = 15;
		g.setColor(Color.GREEN);
		for(SonarReading sr : reading.m_sensorReading.m_sonar) {
			Direction servo  = state.m_heading.getHeadDirection(sr.m_servo);
			float distance = sr.m_distance * scale;
			drawArc(g, viewPlayerPosX, viewPlayerPosY, servo, distance);
		}
		
		
		
	}

	private void drawPosition(Graphics g, float scale, Graphics2D g2d,
			daniel.robot.SLAM.Pose reading, float transparency) {
		
		g2d.setComposite(AlphaComposite.getInstance(
		        AlphaComposite.SRC_OVER, transparency));
      
		State state = reading.getBestGuess();
		
		//g.translate((int)(state.m_position.x * scale), (int)(state.m_position.y * scale));
		int viewPlayerPosX = (int)(state.m_position.x * scale);
		int viewPlayerPosY = (int)(state.m_position.y * scale);
		
		int headingX = viewPlayerPosX + (int)(state.m_heading.getX() * 20 *scale);
		int headingY = viewPlayerPosY + (int)(state.m_heading.getY() * 20 *scale);
		int compassX = viewPlayerPosX + (int)(reading.m_sensorReading.m_compassDirection.getX() * 10 *scale);
		int compassY = viewPlayerPosY + (int)(reading.m_sensorReading.m_compassDirection.getY() * 10 *scale);
		
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