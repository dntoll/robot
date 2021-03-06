package daniel.robot.glWindow.view;

import static javax.media.opengl.GL.GL_LINES;
import static javax.media.opengl.GL.GL_TRIANGLES;

import java.awt.geom.Point2D.Float;

import javax.media.opengl.GL2;
import daniel.robot.Direction;

import daniel.robot.glWindow.model.DirectionalReading;
import daniel.robot.glWindow.model.DirectionalReadingCollection;
import daniel.robot.glWindow.model.IMap;
import daniel.robot.glWindow.model.State;
import daniel.robot.sensors.SharpMeasurement;
import daniel.robot.slam.ParticleFilter;
import daniel.robot.slam.map.bm.BitmapMap;
import daniel.robot.slam.map.bm.MapData;
import daniel.robot.slam.map.lm.Landmark;
import daniel.robot.slam.map.lm.LandmarkMap;

public class DistanceMeasurementView {
	
	private static final int CenterY = 320;
	private static final int CenterX = 200;
	private ViewCore core;

	DistanceMeasurementView(ViewCore core) {
		this.core = core;
	}
	
	void drawTopDown(GL2 gl, DirectionalReadingCollection reading, State position) {
		gl.glLoadIdentity();  
		
		
		gl.glColor3f(0, 1, 0);
		
		Float pos = position.getRobotPosition();
		Direction robotDirection = position.m_heading;
		
		float cx = CenterX + pos.x+175;
		float cy = CenterY + pos.y;
		float scale = 0.6f;
		
		
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

	public void drawMap(GL2 gl, IMap lastMap, State bestKnownPosition, ParticleFilter diveristy) {
		gl.glLoadIdentity(); 
		
		if (lastMap instanceof LandmarkMap) {
			drawMapInner(gl, (LandmarkMap) lastMap);
		}
		if (lastMap instanceof BitmapMap) {
			drawMapInner(gl, (BitmapMap) lastMap);
		}
		
		drawPosition(gl, bestKnownPosition, diveristy);
	}

	public void drawPosition(GL2 gl, State bestKnownPosition,
			ParticleFilter diveristy) {
		gl.glBegin(GL_TRIANGLES);
		
		gl.glColor4f(1.0f, 0.0f, 0.0f, 0.2f);
		for (int i = 0; i<  diveristy.getSize(); i++) {
			
			drawState(gl, diveristy.getState(i));
		}
		
		gl.glColor4f(0.4f, 0.4f, 1.0f, 1.0f);
		drawState(gl, bestKnownPosition);
		
		gl.glEnd();
	}
	
	private void drawMapInner(GL2 gl, LandmarkMap lastMap) {
		
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
		
		
		
	}

	private void drawMapInner(GL2 gl, BitmapMap lastMap) {
		MapData map = lastMap.getMap();
		gl.glBegin(GL_LINES);
		gl.glColor4f(0.1f, 0.1f, 0.1f, 1.0f);
		for (int x = 0; x < map.getSize(); x++) {
			for (int y = 0; y < map.getSize(); y++) {
				float vx = CenterX + (x - map.getSize()/2.0f) * map.getCellSize();
				float vy = CenterY + (y - map.getSize()/2.0f) * map.getCellSize();
				
				if (map.isFree(x,y)) {
					gl.glColor4f(0.1f, 0.1f, 0.1f, 1.0f);
					core.drawQuad(gl, vx, vy, map.getCellSize(), map.getCellSize());
				}
				if (map.isBlocked(x,y)) {
					gl.glColor4f(0.5f, 0.1f, 0.1f, 1.0f);
					core.drawQuad(gl, vx, vy, map.getCellSize(), map.getCellSize());
				}
			}
		}
		gl.glEnd();
		
	}
	
	void drawState(GL2 gl, State pos) {
		float x = pos.getRobotPosition().x;
		float y = pos.getRobotPosition().y;
		float vx = CenterX + x;// / MapData.getCellSize();
		float vy = CenterY + y;// / MapData.getCellSize();
		
		core.drawQuad(gl, vx-1, vy-1, 2, 2);
	}
}
