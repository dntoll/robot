package daniel.robot.glWindow.view;

import static javax.media.opengl.GL.GL_LINES;
import static javax.media.opengl.GL.GL_TRIANGLES;

import java.awt.geom.Point2D.Float;

import javax.media.opengl.GL2;
import daniel.robot.Direction;
import daniel.robot.SLAM.Landmark;
import daniel.robot.SLAM.Map;
import daniel.robot.SLAM.MapData;

import daniel.robot.glWindow.model.DirectionalReading;
import daniel.robot.glWindow.model.DistanceSensorReadings;
import daniel.robot.glWindow.model.State;

public class DistanceMeasurementView {
	
	private static final int CenterY = 320;
	private static final int CenterX = 200;
	private ViewCore core;

	DistanceMeasurementView(ViewCore core) {
		this.core = core;
	}
	
	void drawTopDown(GL2 gl, DistanceSensorReadings reading, State position) {
		gl.glLoadIdentity();  
		
		
		gl.glColor3f(0, 1, 0);
		
		Float pos = position.getRobotPosition();
		Direction robotDirection = position.m_heading;
		
		float cx = CenterX + pos.x+175;
		float cy = CenterY + pos.y;
		
		
		/*for(DirectionalReading reading1 : reading.getReadings().values()) {
			Direction servoDirection = reading1.getServoDirection();
			
			Direction direction = robotDirection.getHeadDirection(servoDirection);
			Direction opposite = new Direction(direction.getHeadingDegrees() + 180);
			
			
			gl.glColor4f(0, 1, 0, 0.1f);
			core.drawArc(gl, cx, cy, reading1.getSonar1Distance(), direction, 15);
		}*/
		
		for(DirectionalReading reading1 : reading.getReadings().values()) {
			Direction servoDirection = reading1.getServoDirection();
			
			Direction direction = robotDirection.getHeadDirection(servoDirection);
			//Direction opposite = new Direction(direction.getHeadingDegrees() + 180);
			
			
			if (reading1.getSharp1Distance().okDistance()) {
				gl.glColor4f(0, 1, 0, 1.0f);
				core.drawArc(gl, cx, cy, reading1.getSharp1Distance(), direction, 3);
				gl.glColor4f(0.3f, 0.3f, 0.3f, 1.0f);
				core.fillArc(gl, cx, cy, reading1.getSharp1Distance(), direction, 3);
				
			} else {
				gl.glColor4f(1, 0, 0, 1.0f);
				core.drawArc(gl, cx, cy, reading1.getSharp1Distance(), direction, 3);
				gl.glColor4f(0.3f, 0.3f, 0.3f, 1.0f);
				core.fillArc(gl, cx, cy, reading1.getSharp1Distance(), direction, 3);
			}
		}
		
		gl.glBegin(GL_LINES);
		gl.glColor4f(1, 1, 1, 0.5f);
		core.drawCircle(gl, cx, cy, 17.0f);
		core.drawCircle(gl, cx, cy, 50.0f);
		core.drawCircle(gl, cx, cy, 100.0f);
		core.drawCircle(gl, cx, cy, 140.0f);
		core.drawCircle(gl, cx, cy, 200.0f);
		
		
		
		
		gl.glEnd();
		
		core.drawText(gl, reading.getSonarString(), 30, 60);
		core.drawText(gl, reading.getSharpString(), 30, 30);
	}

	public void drawMap(GL2 gl, Map lastMap, State bestKnownPosition) {
		gl.glLoadIdentity(); 
		gl.glColor4f(1, 1, 1, 1.0f);
		gl.glBegin(GL_LINES);
		
		Landmark[] bestGuess = lastMap.getAllLandmarks();
		for ( Landmark l : bestGuess) {
			float cx = CenterX + l.pos.x;
			float cy = CenterY + l.pos.y;
			float deviation = l.deviation / 10.0f;
			if (deviation > 1.0f) {
				deviation = 1.0f;
			}
			gl.glColor4f(deviation, 1.0f, deviation, 1.0f);
			core.drawCircle(gl, cx, cy, 1);
			
		}
		gl.glEnd();
		gl.glBegin(GL_TRIANGLES);
		/*
		MapData map = lastMap.getMap();
		
		gl.glColor4f(0.1f, 0.1f, 0.1f, 1.0f);
		for (int x = 0; x < map.getSize(); x++) {
			for (int y = 0; y < map.getSize(); y++) {
				float vx = CenterX + (x - map.getSize()/2.0f) * map.getCellSize();
				float vy = CenterY + (y - map.getSize()/2.0f) * map.getCellSize();
				
				if (lastMap.isFree(x,y)) {
					gl.glColor4f(0.1f, 0.1f, 0.1f, 1.0f);
					core.drawQuad(gl, vx, vy, map.getCellSize(), map.getCellSize());
				}
				if (lastMap.isBlocked(x,y)) {
					gl.glColor4f(0.5f, 0.1f, 0.1f, 1.0f);
					core.drawQuad(gl, vx, vy, map.getCellSize(), map.getCellSize());
				}
			}
		}
		
		*/
		
		
		float x = bestKnownPosition.getRobotPosition().x;
		float y = bestKnownPosition.getRobotPosition().y;
		float vx = CenterX + (x );// / MapData.getCellSize();
		float vy = CenterY + (y);// / MapData.getCellSize();
		gl.glColor4f(0.0f, 1.0f, 0.0f, 1.0f);
		core.drawQuad(gl, vx-15, vy-15, 30, 30);
		
		gl.glEnd();
		
		
		
		gl.glColor4f(1, 1, 1, 1.0f);
		core.drawText(gl, "" + bestGuess.length+ " x: " + x + " y: " + y, 30, 550);
	}
}
