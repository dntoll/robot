package daniel.robot.SLAM.gui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import daniel.robot.Direction;
import daniel.robot.SLAM.Map;
import daniel.robot.SLAM.PoseCollection;
import daniel.robot.SLAM.Pose;
import daniel.robot.SLAM.State;
import daniel.robot.SLAM.ParticleFilter.Particle;
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
        float scale = 5.0f;
        
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
    		
    		
    		State bestGuess = reading.getBestGuess().getState();
    		
    		drawSensorReading(g2d, scale, reading);
    		Point2D.Float headPosition = bestGuess.getRobotPosition();
    		g.translate((int)(headPosition.x * scale), (int)(headPosition.y * scale));
    		drawDistanceRings(g2d, scale);
    		g.translate((int)(-headPosition.x * scale), -(int)(headPosition.y * scale));
    		
    		m_particleFilterView.draw(g2d, reading.m_position, scale);
    	}
    	
    	 //for (daniel.robot.SLAM.Pose s : a_knowledge.m_sensorScans) {
    		 Pose s = a_knowledge.getLastPose();
         	float transparency =  1.0f;//((float)layer / (float)m_slam.m_world.m_world.size());
         	drawPosition(g, scale, g2d, s.getBestGuess(), transparency);
         //}
    	
    	
    	g.translate(-posX, -posY);
    	
    	
    	String error = "Error :" + a_knowledge.getError();
    	g.drawString(error, 20, 20);
    }

	
	private void drawBestGuess(Graphics2D g2d, float scale, PoseCollection a_world) {
		 
		 Pose r = a_world.getLastPose();
		 //for (Pose r : a_world.G.) {
			g2d.setColor(Color.BLACK);
			
			Map map = r.getBestMap();
			if (map != null) {
				for (java.awt.geom.Point2D.Float fp : map.m_landmarks) {
					g2d.fillRect((int)(fp.x*scale), (int)(fp.y*scale), (int)scale, (int)scale);
				}
				
				
				g2d.setColor(Color.RED);
				for (java.awt.geom.Line2D.Float line : map.m_lines) {
					g2d.drawLine((int)(line.x1*scale), 
							     (int)(line.y1*scale),
							     (int)(line.x2*scale), 
							     (int)(line.y2*scale));
				}
			}
			
		//	break;
		// }
		
		
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
		g.setColor(Color.BLACK);
		float distance = 17.0f*2.0f * scale;
		g.drawArc(-(int)distance/2, -(int)distance/2, (int)distance, (int)distance, 0, 360);
	}
	
	private void drawSensorReading(Graphics2D g,  float scale, daniel.robot.SLAM.Pose reading) {
		State state = reading.getBestGuess().getState();
		
		//g.translate((int)(state.m_position.x * scale), (int)(state.m_position.y * scale));
		int viewPlayerPosX = (int)(state.getRobotPosition().x * scale);
		int viewPlayerPosY = (int)(state.getRobotPosition().y * scale);
		
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
			Particle reading, float transparency) {
		
		State state = reading.getState();
		Particle parent = reading.getParent();
		int viewPlayerPosX = (int)(state.getRobotPosition().x * scale);
		int viewPlayerPosY = (int)(state.getRobotPosition().y * scale);
		
		int headingX = viewPlayerPosX + (int)(state.m_heading.getX() * 20 *scale);
		int headingY = viewPlayerPosY + (int)(state.m_heading.getY() * 20 *scale);
		
		g2d.setComposite(AlphaComposite.getInstance(
		        AlphaComposite.SRC_OVER, transparency));
      
		
		
		if (parent != null) {
			drawPosition(g, scale, g2d, parent, transparency);
			
			int parentX = (int)(parent.getState().getRobotPosition().x * scale);
			int parentY = (int)(parent.getState().getRobotPosition().y * scale);
			
			g.setColor(Color.BLACK);
			g.drawLine(viewPlayerPosX, viewPlayerPosY, parentX, parentY);
		}
		
		//g.translate((int)(state.m_position.x * scale), (int)(state.m_position.y * scale));
		
		//int compassX = viewPlayerPosX + (int)(reading.m_sensorReading.m_compassDirection.getX() * 10 *scale);
		//int compassY = viewPlayerPosY + (int)(reading.m_sensorReading.m_compassDirection.getY() * 10 *scale);
		
		g.setColor(Color.BLACK);
		g.drawLine(viewPlayerPosX, viewPlayerPosY, headingX, headingY);
		
		//g.setColor(Color.CYAN);
		//g.drawLine(viewPlayerPosX, viewPlayerPosY, compassX, compassY);

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