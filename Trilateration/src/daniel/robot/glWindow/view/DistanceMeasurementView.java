package daniel.robot.glWindow.view;

import static javax.media.opengl.GL.GL_LINES;
import javax.media.opengl.GL2;
import daniel.robot.Direction;
import daniel.robot.glWindow.model.DirectionalReading;
import daniel.robot.glWindow.model.DistanceSensorReadings;

public class DistanceMeasurementView {
	
	private ViewCore core;

	DistanceMeasurementView(ViewCore core) {
		this.core = core;
	}
	
	void drawTopDown(GL2 gl, DistanceSensorReadings reading) {
		gl.glLoadIdentity();  
		
		gl.glColor3f(0, 1, 0);
		
		float cx = 320;
		float cy = 320;
		
		
		for(DirectionalReading reading1 : reading.getReadings().values()) {
			Direction direction = reading1.getServoDirection();
			Direction opposite = new Direction(direction.getHeadingDegrees() + 180);
			
			
			gl.glColor4f(0, 1, 0, 0.1f);
			core.drawArc(gl, cx, cy, reading1.getSonar1Distance(), direction, 15);
		}
		
		for(DirectionalReading reading1 : reading.getReadings().values()) {
			Direction direction = reading1.getServoDirection();
			Direction opposite = new Direction(direction.getHeadingDegrees() + 180);
			
			gl.glColor4f(1, 0, 0, 1.0f);
			core.drawArc(gl, cx, cy, reading1.getSharp1Distance(), direction, 3);
			gl.glColor4f(0.3f, 0.3f, 0.3f, 1.0f);
			core.fillArc(gl, cx, cy, reading1.getSharp1Distance(), direction, 3);
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
}
