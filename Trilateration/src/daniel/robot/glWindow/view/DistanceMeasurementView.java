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
	private static final int CenterX = 320;
	private ViewCore core;

	DistanceMeasurementView(ViewCore core) {
		this.core = core;
	}
	
	void drawTopDown(GL2 gl, DistanceSensorReadings reading, State position) {
		gl.glLoadIdentity();  
		
		
		gl.glColor3f(0, 1, 0);
		
		Float pos = position.getRobotPosition();
		Direction robotDirection = position.m_heading;
		
		float cx = CenterX + pos.x;
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
		for ( Landmark l : lastMap.m_landmarks) {
			float cx = CenterX + l.pos.x;
			float cy = CenterY + l.pos.y;
			core.drawCircle(gl, cx, cy, l.deviation);
			
		}
		gl.glEnd();
		
		
		gl.glBegin(GL_TRIANGLES);
		gl.glColor4f(0.1f, 0.1f, 0.1f, 1.0f);
		for (int x = 0; x < MapData.getSize(); x++) {
			for (int y = 0; y < MapData.getSize(); y++) {
				float vx = CenterX + (x - MapData.getSize()/2.0f) * MapData.getCellSize();
				float vy = CenterY + (y - MapData.getSize()/2.0f) * MapData.getCellSize();
				
				if (lastMap.isFree(x,y)) {
					gl.glColor4f(0.1f, 0.1f, 0.1f, 1.0f);
					core.drawQuad(gl, vx, vy, MapData.getCellSize(), MapData.getCellSize());
				}
				if (lastMap.isBlocked(x,y)) {
					gl.glColor4f(0.5f, 0.1f, 0.1f, 1.0f);
					core.drawQuad(gl, vx, vy, MapData.getCellSize(), MapData.getCellSize());
				}
			}
		}
		
		float x = bestKnownPosition.getRobotPosition().x;
		float y = bestKnownPosition.getRobotPosition().y;
		float vx = CenterX + (x );// / MapData.getCellSize();
		float vy = CenterY + (y);// / MapData.getCellSize();
		gl.glColor4f(0.0f, 1.0f, 0.0f, 1.0f);
		core.drawQuad(gl, vx, vy, MapData.getCellSize(), MapData.getCellSize());
		
		gl.glEnd();
		
		
		
		gl.glColor4f(1, 1, 1, 1.0f);
		core.drawText(gl, "" + lastMap.m_landmarks.size()+ " x: " + x + " y: " + y, 30, 400);
	}
}
