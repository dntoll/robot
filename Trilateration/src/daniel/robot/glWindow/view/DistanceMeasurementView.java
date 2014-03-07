package daniel.robot.glWindow.view;

import static javax.media.opengl.GL.GL_LINES;
import static javax.media.opengl.GL.GL_TRIANGLES;

import java.awt.geom.Point2D.Float;

import javax.media.opengl.GL2;
import daniel.robot.Direction;
import daniel.robot.SLAM.Landmark;
import daniel.robot.SLAM.Map;
import daniel.robot.SLAM.MapData;
import daniel.robot.SLAM.ParticleFilter.ParticleFilter;

import daniel.robot.glWindow.model.DirectionalReading;
import daniel.robot.glWindow.model.DistanceSensorReadings;
import daniel.robot.glWindow.model.State;
import daniel.robot.sensors.SharpMeasurement;

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
		float scale = 0.75f;
		
		
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
			
			//drawDistance(gl, cx, cy, scale, direction, reading1.getBestDistance());
			drawDistance(gl, cx, cy, scale, direction, reading1.getBestDistance());
		}
		
		gl.glBegin(GL_LINES);
		gl.glColor4f(1, 1, 1, 0.5f);
		core.drawCircle(gl, cx, cy, scale * 17.0f);
		core.drawCircle(gl, cx, cy, scale * 50.0f);
		core.drawCircle(gl, cx, cy, scale * 100.0f);
		//core.drawCircle(gl, cx, cy, scale * 150.0f);
		core.drawCircle(gl, cx, cy, scale * 200.0f);
		core.drawCircle(gl, cx, cy, scale * 300.0f);
		core.drawCircle(gl, cx, cy, scale * 400.0f);
		core.drawCircle(gl, cx, cy, scale * 500.0f);
		
		
		
		
		gl.glEnd();
		gl.glLoadIdentity(); 
		core.drawText(gl, "poo" + reading.getSharpString(), 30, 30);
		
		gl.glLoadIdentity();  
	}

	private void drawDistance(GL2 gl, float cx, float cy, float scale, Direction direction,
			SharpMeasurement distance) {
		
		if (distance.getValues().size() > 0) {
			if (distance.okDistance()) {
				gl.glColor4f(0, 1, 0, 1.0f);
				core.drawArc(gl, cx, cy, scale, distance, direction, 3);
				gl.glColor4f(0.3f, 0.3f, 0.3f, 1.0f);
				core.fillArc(gl, cx, cy, scale, distance, direction, 3);
				
			} else {
				gl.glColor4f(1, 0, 0, 1.0f);
				core.drawArc(gl, cx, cy, scale, distance, direction, 3);
				gl.glColor4f(0.3f, 0.3f, 0.3f, 1.0f);
				core.fillArc(gl, cx, cy, scale, distance, direction, 3);
			}
		}
	}

	public void drawMap(GL2 gl, Map lastMap, State bestKnownPosition, ParticleFilter diveristy) {
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
		
		gl.glColor4f(1.0f, 0.0f, 0.0f, 0.2f);
		for (int i = 0; i<  diveristy.getSize(); i++) {
			
			drawState(gl, diveristy.getState(i));
		}
		
		gl.glColor4f(0.4f, 0.4f, 1.0f, 1.0f);
		drawState(gl, bestKnownPosition);
		
		gl.glEnd();
		
		
		
		gl.glColor4f(1, 1, 1, 1.0f);
		float x = bestKnownPosition.getRobotPosition().x;
		float y = bestKnownPosition.getRobotPosition().y;
		core.drawText(gl, "" + bestGuess.length+ " x: " + x + " y: " + y, 30, 580);
	}
	
	void drawState(GL2 gl, State pos) {
		float x = pos.getRobotPosition().x;
		float y = pos.getRobotPosition().y;
		float vx = CenterX + x;// / MapData.getCellSize();
		float vy = CenterY + y;// / MapData.getCellSize();
		
		core.drawQuad(gl, vx-1, vy-1, 2, 2);
	}
}
